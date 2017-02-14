import anagramutils.Tweet;
import junit.framework.Assert;
import org.junit.Test;

public class TweetTests {

    @Test
    public void BasicTweet() {
        Tweet tweet = Util.tweetFromText("What are you even doing?");
        Assert.assertEquals("What are you even doing?", tweet.getTweetOriginalText());
        Assert.assertEquals("whatareyouevendoing", tweet.getTweetStrippedText());
        Assert.assertEquals("aadeeeghinnoortuvwy", tweet.getTweetSortedStrippedText());
    }
}
