package anagramlistener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import anagramutils.Tweet;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AnagramListener {

    private static Logger logger = LoggerFactory.getLogger(AnagramListener.class);

    private ObjectMapper mapper = new ObjectMapper();
    private ZMQ.Context context;
    private ZMQ.Socket subscriber;
    private ExecutorService executorService;
    private DBI dbi;

    public AnagramListener() {
        context = ZMQ.context(1);
        subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://localhost:5558");
        subscriber.subscribe(new byte[0]);
        executorService = Executors.newCachedThreadPool();
        dbi = configureDatabase();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("interrupt received, shutting down listener...");
                cleanUp();
            }
        });
    }

    private DBI configureDatabase() {
        Config dbConfig = ConfigFactory.load("db");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbConfig.getString("database.url"));
        config.setUsername(dbConfig.getString("database.user"));
        config.setPassword(dbConfig.getString("database.password"));
        HikariDataSource hikariDataSource = new HikariDataSource(config);

        return new DBI(hikariDataSource);
    }

    private void cleanUp() {
        logger.info("cleaning up.");
        subscriber.close();
        context.term();
        executorService.shutdown();
        logger.info("finished cleaning up.");
    }

    public void run() {

        ProcessedTweetCounts counts = new ProcessedTweetCounts();

        while (!Thread.currentThread().isInterrupted()) {
            String tweetJson = subscriber.recvStr(0).trim();
            try {
                Tweet tweet = mapper.readValue(tweetJson, Tweet.class);
                executorService.submit(new CandidateTweetProcessor(tweet, dbi, counts));
            }
            catch (IOException e) {
                logger.error(e.getMessage());
            }

            if (counts.getProcessedTweetCount() % 10_000 == 0) {
                logger.info("{} processed tweets. {} saved tweets. {} saved matches.",
                        counts.getProcessedTweetCount(), counts.getSavedTweetCount(),
                        counts.getSavedAnagramCount());
            }
        }
    }

    public static void main(String[] args) {
        AnagramListener listener = new AnagramListener();
        listener.run();
    }
}
