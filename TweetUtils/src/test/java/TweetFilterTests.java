import anagramutils.Tweet;
import anagramutils.filters.TweetFilter;
import junit.framework.Assert;
import org.junit.Test;

public class TweetFilterTests {

    @Test
    public void isTooShort() {
        Tweet tweet = Util.tweetFromText("short");
        Assert.assertFalse(TweetFilter.isGoodTweet(tweet));
    }

    @Test
    public void isLongEnough() {
        Tweet tweet = Util.tweetFromText("nine chars");
        Assert.assertTrue(TweetFilter.isGoodTweet(tweet));
    }

    @Test
    public void isNotTooLong() {
        Tweet tweet = Util.tweetFromText("a tweet that is altogether way too long");
        Assert.assertFalse(TweetFilter.isGoodTweet(tweet));
    }

    @Test
    public void doesNotContainNumbers() {
        Tweet tweet = Util.tweetFromText("long enough but contains 4");
        Assert.assertFalse(TweetFilter.isGoodTweet(tweet));
    }
}
