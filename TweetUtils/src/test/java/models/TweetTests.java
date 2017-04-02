package textprocessing;

import anagramutils.models.Tweet;
import junit.framework.Assert;
import org.junit.Test;

public class TweetTests {

    @Test
    public void BasicTweet() {
        Tweet tweet = Tweet.fromText("What are you even doing?");
        Assert.assertEquals("What are you even doing?", tweet.getTweetOriginalText());
        Assert.assertEquals("whatareyouevendoing", tweet.getTweetStrippedText());
        Assert.assertEquals("aadeeeghinnoortuvwy", tweet.getTweetSortedStrippedText());
    }

    @Test
    public void normalizeNonAsciiCharacters() {
        AssertTextNormalizedAndStripped("aaaaaaaaaaaaaaaaaaaaaa", "ÀÁÂÃĀĂȦÄẢÅǍȀȂĄẠḀẦẤàáâä");
        AssertTextNormalizedAndStripped("eeeeeeeeeeeeeeeee", "ÉÊẼĒĔËȆȄȨĖèéêẽēȅë");
        AssertTextNormalizedAndStripped("iiiioooooouunnccss", "ÌÍÏïØøÒÖÔöÜüŇñÇçß");
    }

    @Test
    public void lowercase() {
        AssertTextNormalizedAndStripped("abcdabcd", "ABCDabcd");
    }

    @Test
    public void spaces() {
        AssertTextNormalizedAndStripped("thisisatest", "This is a test!");
    }

    private static void AssertTextNormalizedAndStripped(String expected, String actual) {
        Assert.assertEquals(Tweet.fromText(actual).getTweetStrippedText(), expected);
    }
}
