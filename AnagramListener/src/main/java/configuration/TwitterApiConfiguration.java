package configuration;

import com.typesafe.config.Config;

public class TwitterApiConfiguration {

    private TwitterApiConfiguration() { }

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public static TwitterApiConfiguration FromFileOrResources() {
        return FromFileOrResources("twitterapi.conf");
    }

    public static TwitterApiConfiguration FromFileOrResources(String filename) {
        Config config = ConfigurationProvider.loadConfigFromFileIfExistsOtherwiseResources(filename);

        TwitterApiConfiguration twitterApiConfiguration = new TwitterApiConfiguration();
        twitterApiConfiguration.consumerKey = config.getString("twitter.consumerkey");
        twitterApiConfiguration.consumerSecret = config.getString("twitter.consumersecret");
        twitterApiConfiguration.accessToken = config.getString("twitter.accesstoken");
        twitterApiConfiguration.accessTokenSecret = config.getString("twitter.accesstokensecret");

        return twitterApiConfiguration;
    }
}
