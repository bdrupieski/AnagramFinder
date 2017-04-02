package anagramlistener;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.skife.jdbi.v2.DBI;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.File;

public class ConfigurationProvider {

    private ConfigurationProvider() { }

    public static Config getApplicationConfig() {
        Config applicationConfig;
        File f = new File("application.conf");
        if (f.exists()) {
            applicationConfig = ConfigFactory.parseFile(f);
        } else {
            applicationConfig = ConfigFactory.load();
        }
        return applicationConfig;
    }

    static Configuration buildTwitterConfig(int numberOfAsyncThreads) {
        Config twitterConfig = ConfigurationProvider.getTwitterConfig();
        return new ConfigurationBuilder()
                .setOAuthConsumerKey(twitterConfig.getString("twitter.consumerkey"))
                .setOAuthConsumerSecret(twitterConfig.getString("twitter.consumersecret"))
                .setOAuthAccessToken(twitterConfig.getString("twitter.accesstoken"))
                .setOAuthAccessTokenSecret(twitterConfig.getString("twitter.accesstokensecret"))
                .setAsyncNumThreads(numberOfAsyncThreads)
                .build();
    }

    private static Config getTwitterConfig() {
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

    public static DBI configureDatabase(Config applicationConfig) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(applicationConfig.getString("database.url"));
        hikariConfig.setUsername(applicationConfig.getString("database.user"));
        hikariConfig.setPassword(applicationConfig.getString("database.password"));
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        return new DBI(hikariDataSource);
    }
}
