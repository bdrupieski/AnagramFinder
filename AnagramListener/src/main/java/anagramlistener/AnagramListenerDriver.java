package anagramlistener;

import com.typesafe.config.Config;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;

public class AnagramListenerDriver {

    private static Logger logger = LoggerFactory.getLogger(AnagramListenerDriver.class);

    private TwitterStream twitterStream;
    private DBI dbi;
    private ProcessedTweetCountLogger processedTweetCountLogger;

    private AnagramListenerDriver() {
        Config applicationConfig = ConfigurationProvider.getApplicationConfig();
        long processedCountThreshold = applicationConfig.getLong("processedCountThreshold");
        int numberOfAsyncThreads = applicationConfig.getInt("numberOfAsyncThreads");

        dbi = ConfigurationProvider.configureDatabase(applicationConfig);
        Configuration twitterConfiguration = ConfigurationProvider.buildTwitterConfig(numberOfAsyncThreads);
        twitterStream = new TwitterStreamFactory(twitterConfiguration).getInstance();

        processedTweetCountLogger = new ProcessedTweetCountLogger(dbi, processedCountThreshold);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("interrupt received, shutting down producer...");
            cleanUp();
        }));
    }

    private void cleanUp() {
        logger.info("shutting down twitter stream.");
        twitterStream.cleanUp();
        twitterStream.shutdown();
        logger.info("finished shutting down twitter stream.");

        logger.info("logging processed counts.");
        processedTweetCountLogger.logProcessedCounts();
        logger.info("finished logging processed counts.");
    }

    private void run() {
        AnagramMatchingStatusListener publishFilteredStatusListener = new AnagramMatchingStatusListener(dbi, processedTweetCountLogger);

        twitterStream.addListener(publishFilteredStatusListener);
        twitterStream.sample("en");

        while (!Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
                cleanUp();
            }
        }
    }

    public static void main(String[] args) {
        AnagramListenerDriver producer = new AnagramListenerDriver();
        producer.run();
    }
}
