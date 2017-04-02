package anagramlistener.configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.skife.jdbi.v2.DBI;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;

public class ConfigurationProvider {

    private ConfigurationProvider() { }

    public static TwitterStream buildTwitterStream(TwitterApiConfiguration twitterApiConfiguration, ApplicationConfiguration applicationConfiguration) {
        Configuration twitter4JConfiguration = new ConfigurationBuilder()
                .setOAuthConsumerKey(twitterApiConfiguration.getConsumerKey())
                .setOAuthConsumerSecret(twitterApiConfiguration.getConsumerSecret())
                .setOAuthAccessToken(twitterApiConfiguration.getAccessToken())
                .setOAuthAccessTokenSecret(twitterApiConfiguration.getAccessTokenSecret())
                .setAsyncNumThreads(applicationConfiguration.getNumberOfAsyncThreads())
                .build();

        return new TwitterStreamFactory(twitter4JConfiguration).getInstance();
    }

    static Config loadConfigFromFileIfExistsOtherwiseResources(String configurationFileName) {
        Config applicationConfig;
        File f = new File(configurationFileName);
        if (f.exists()) {
            applicationConfig = ConfigFactory.parseFile(f);
        } else {
            applicationConfig = ConfigFactory.load(configurationFileName);
        }
        return applicationConfig;
    }

    public static DBI configureDatabase(ApplicationConfiguration applicationConfiguration) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(applicationConfiguration.getDatabaseUrl());
        hikariConfig.setUsername(applicationConfiguration.getDatabaseUser());
        hikariConfig.setPassword(applicationConfiguration.getDatabasePassword());
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        return new DBI(hikariDataSource);
    }
}
