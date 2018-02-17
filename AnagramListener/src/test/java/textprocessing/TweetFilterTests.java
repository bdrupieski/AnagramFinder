package textprocessing;

import junit.framework.Assert;
import models.Tweet;
import org.junit.Test;

public class TweetFilterTests {

    @Test
    public void when_tweetIsTooShort_then_doesNotPassFilter() {
        Tweet tweet = Tweet.fromText("short");
        Assert.assertFalse(TweetFilter.isGoodTweet(tweet));
    }

    @Test
    public void when_tweetIsTooLong_then_doesNotPassFilter() {
        Tweet tweet = Tweet.fromText("a tweet that is altogether way too long");
        Assert.assertFalse(TweetFilter.isGoodTweet(tweet));
    }

    @Test
    public void when_tweetIsAtMinimumOfDesiredLengthRange_then_passesFilter() {
        Tweet tweet = Tweet.fromText("nine chars");
        Assert.assertTrue(TweetFilter.isGoodTweet(tweet));
    }

    @Test
    public void when_tweetIsAtMaximumOfDesiredLengthRange_then_passesFilter() {
        Tweet tweet = Tweet.fromText("this tweet is long enough yes indeed");
        Assert.assertTrue(TweetFilter.isGoodTweet(tweet));
    }

    @Test
    public void when_tweetContainsNumbers_then_doesNotPassFilter() {
        Tweet tweet = Tweet.fromText("long enough but contains 4");
        Assert.assertFalse(TweetFilter.isGoodTweet(tweet));
    }
}
