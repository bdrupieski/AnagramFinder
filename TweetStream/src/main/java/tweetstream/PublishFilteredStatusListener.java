package tweetstream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import anagramutils.Tweet;
import anagramutils.filters.StatusFilter;
import anagramutils.filters.TweetFilter;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import java.io.IOException;

public class PublishFilteredStatusListener implements StatusListener {

    private static Logger logger = LoggerFactory.getLogger(PublishFilteredStatusListener.class);

    private ObjectMapper mapper = new ObjectMapper();
    private ZMQ.Socket publisher;
    private PublishedTweetCountLogger publishedTweetCountLogger = new PublishedTweetCountLogger();

    PublishFilteredStatusListener(ZMQ.Socket publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onStatus(Status status) {
        publishedTweetCountLogger.incrementReceivedStatusCount();
        if (StatusFilter.isGoodStatus(status)) {
            publishedTweetCountLogger.incrementStatusMetFilterCount();
            Tweet tweet = Tweet.fromStatus(status);
            if (TweetFilter.isGoodTweet(tweet)) {
                try {
                    logger.debug("publishing tweet: {}", tweet.getTweetOriginalText());
                    String tweetJson = mapper.writeValueAsString(tweet);
                    publisher.send(tweetJson);
                    publishedTweetCountLogger.incrementPublishedTweetCount();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        logger.error("status deleted: {} User: {}", statusDeletionNotice.getStatusId(), statusDeletionNotice.getUserId());
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        logger.error("track limitation: {}", i);
    }

    @Override
    public void onScrubGeo(long l, long l1) {
        logger.error("geo scrub l: {} l1: {}", l, l1);
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
        logger.error("stall warning: {}", stallWarning.getMessage());
    }

    @Override
    public void onException(Exception e) {
        logger.error("exception: {}", e.getMessage());
    }
}
