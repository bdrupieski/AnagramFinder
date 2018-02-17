package models;

import junit.framework.Assert;
import org.junit.Test;

public class TweetTests {

    @Test
    public void when_basicTweetText_then_stripsOutWhitespaceAndSortsCharacters() {
        Tweet tweet = Tweet.fromText("What are you even doing?");
        Assert.assertEquals("What are you even doing?", tweet.getTweetOriginalText());
        Assert.assertEquals("whatareyouevendoing", tweet.getTweetStrippedText());
        Assert.assertEquals("aadeeeghinnoortuvwy", tweet.getTweetSortedStrippedText());
    }

    @Test
    public void when_nonAsciiCharacters_then_normalizedToAsciiCharacters() {
        assertTextNormalizedAndStripped("aaaaaaaaaaaaaaaaaaaaaa", "ÀÁÂÃĀĂȦÄẢÅǍȀȂĄẠḀẦẤàáâä");
        assertTextNormalizedAndStripped("eeeeeeeeeeeeeeeee", "ÉÊẼĒĔËȆȄȨĖèéêẽēȅë");
        assertTextNormalizedAndStripped("iiiioooooouunnccss", "ÌÍÏïØøÒÖÔöÜüŇñÇçß");
    }

    @Test
    public void when_tweetContainsMixedCase_then_normalizesToLowercase() {
        assertTextNormalizedAndStripped("abcdabcd", "ABCDabcd");
    }

    @Test
    public void when_tweetContainsWhitespace_then_normalizesToNoWhitespace() {
        assertTextNormalizedAndStripped("thisisatest", "This is a test!");
    }

    private static void assertTextNormalizedAndStripped(String expected, String actual) {
        Assert.assertEquals(Tweet.fromText(actual).getTweetStrippedText(), expected);
    }
}
