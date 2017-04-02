package metricandprocessingtests;

import anagramutils.models.Tweet;
import anagramutils.textprocessing.TweetFilter;
import junit.framework.Assert;
import org.junit.Test;

public class TweetFilterTests {

    @Test
    public void isTooShort() {
        Tweet tweet = Tweet.fromText("short");
        Assert.assertFalse(TweetFilter.isGoodTweet(tweet));
    }

    @Test
    public void isLongEnough() {
        Tweet tweet = Tweet.fromText("nine chars");
        Assert.assertTrue(TweetFilter.isGoodTweet(tweet));
    }

    @Test
    public void isNotTooLong() {
        Tweet tweet = Tweet.fromText("a tweet that is altogether way too long");
        Assert.assertFalse(TweetFilter.isGoodTweet(tweet));
    }

    @Test
    public void doesNotContainNumbers() {
        Tweet tweet = Tweet.fromText("long enough but contains 4");
        Assert.assertFalse(TweetFilter.isGoodTweet(tweet));
    }
}
