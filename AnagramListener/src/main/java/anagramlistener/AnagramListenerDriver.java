package anagramlistener;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;

public class AnagramListenerDriver {

    private static Logger logger = LoggerFactory.getLogger(AnagramListenerDriver.class);

    private TwitterStream twitterStream;
    private DBI dbi;
    private long processedCountThreshold;

    private AnagramListenerDriver() {
        twitterStream = new TwitterStreamFactory(buildTwitterConfig()).getInstance();

        Config appConfig = getApplicationConfig();
        processedCountThreshold = appConfig.getLong("processedCountThreshold");
        dbi = configureDatabase(appConfig);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("interrupt received, shutting down producer...");
            cleanUp();
        }));
    }

    private Configuration buildTwitterConfig() {
        Config twitterConfig = getTwitterConfig();
        return new ConfigurationBuilder()
                .setOAuthConsumerKey(twitterConfig.getString("twitter.consumerkey"))
                .setOAuthConsumerSecret(twitterConfig.getString("twitter.consumersecret"))
                .setOAuthAccessToken(twitterConfig.getString("twitter.accesstoken"))
                .setOAuthAccessTokenSecret(twitterConfig.getString("twitter.accesstokensecret"))
                .setAsyncNumThreads(1)
                .build();
    }

    private Config getTwitterConfig() {
        Config twitterConfig;
        String twitterConfigName = "twitterapi.conf";
        File f = new File(twitterConfigName);
        if (f.exists()) {
            twitterConfig = ConfigFactory.parseFile(f);
        } else {
            twitterConfig = ConfigFactory.load(twitterConfigName);
        }
        return twitterConfig;
    }

    private Config getApplicationConfig() {
        Config applicationConfig;
        File f = new File("application.conf");
        if (f.exists()) {
            applicationConfig = ConfigFactory.parseFile(f);
        } else {
            applicationConfig = ConfigFactory.load();
        }
        return applicationConfig;
    }

    private DBI configureDatabase(Config appConfig) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(appConfig.getString("database.url"));
        hikariConfig.setUsername(appConfig.getString("database.user"));
        hikariConfig.setPassword(appConfig.getString("database.password"));
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        return new DBI(hikariDataSource);
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
