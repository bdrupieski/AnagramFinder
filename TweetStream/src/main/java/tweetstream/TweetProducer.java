package tweetstream;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TweetProducer {

    private static Logger logger = LoggerFactory.getLogger(TweetProducer.class);

    private TwitterStream twitterStream;
    private ZMQ.Context context;
    private ZMQ.Socket publisher;

    public TweetProducer() {
        twitterStream = new TwitterStreamFactory(buildTwitterConfig()).getInstance();
        context = ZMQ.context(1);
        publisher = context.socket(ZMQ.PUB);
        publisher.bind("tcp://localhost:5558");

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("interrupt received, shutting down producer...");
                cleanUp();
            }
        });
    }

    private Configuration buildTwitterConfig() {
        Config twitterConfig = ConfigFactory.load("twitterapi.conf");
        return new ConfigurationBuilder()
                .setOAuthConsumerKey(twitterConfig.getString("twitter.consumerkey"))
                .setOAuthConsumerSecret(twitterConfig.getString("twitter.consumersecret"))
                .setOAuthAccessToken(twitterConfig.getString("twitter.accesstoken"))
                .setOAuthAccessTokenSecret(twitterConfig.getString("twitter.accesstokensecret"))
                .setAsyncNumThreads(1)
                .build();
    }

    private void cleanUp() {
        logger.info("cleaning up.");
        twitterStream.cleanUp();
        twitterStream.shutdown();
        publisher.close();
        context.term();
        logger.info("finished cleaning up.");
    }

    public void run() {
        twitterStream.addListener(new PublishFilteredStatusListener(publisher));
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
        TweetProducer producer = new TweetProducer();
        producer.run();
    }
}
