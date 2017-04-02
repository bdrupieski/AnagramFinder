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
    private long processedCountThreshold;

    private AnagramListenerDriver() {
        Config applicationConfig = ConfigurationProvider.getApplicationConfig();
        processedCountThreshold = applicationConfig.getLong("processedCountThreshold");
        int numberOfAsyncThreads = applicationConfig.getInt("numberOfAsyncThreads");

        dbi = ConfigurationProvider.configureDatabase(applicationConfig);
        Configuration twitterConfiguration = ConfigurationProvider.buildTwitterConfig(numberOfAsyncThreads);
        twitterStream = new TwitterStreamFactory(twitterConfiguration).getInstance();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("interrupt received, shutting down producer...");
            cleanUp();
        }));
    }

    private void cleanUp() {
        logger.info("cleaning up.");
        twitterStream.cleanUp();
        twitterStream.shutdown();
        logger.info("finished cleaning up.");
    }

    private void run() {
        AnagramMatchingStatusListener publishFilteredStatusListener = new AnagramMatchingStatusListener(dbi, processedCountThreshold);

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
