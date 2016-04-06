import junit.framework.Assert;
import org.junit.Test;
import anagramutils.AnagramMatch;
import anagramutils.Tweet;
import anagramutils.processing.ProcessedTweetText;

import java.sql.Timestamp;
import java.util.UUID;

public class FullRatioTest {

    private static Tweet tweetFromText(String text) {
        ProcessedTweetText processedTweetText = Tweet.processTweetText(text);
        return new Tweet(UUID.randomUUID(), 1, new Timestamp(1), text,
                processedTweetText.getSortedStrippedText(), 1L, "", false);
    }

    private static AnagramMatch matchFromText(String a, String b) {
        Tweet tweetA = tweetFromText(a);
        Tweet tweetB = tweetFromText(b);
        return AnagramMatch.fromTweetPair(tweetA, tweetB);
    }

    private static void ratios(String a, String b, float lcs, float edit, float wc, float interesting) {
        AnagramMatch match = matchFromText(a, b);

        Assert.assertEquals(lcs, match.getLcsLengthToTotalLengthRatio());
        Assert.assertEquals(edit, match.getEditDistanceToLengthRatio());
        Assert.assertEquals(wc, match.getDifferentWordCountToTotalWordCount());
        Assert.assertEquals(interesting, match.getInterestingFactor());
    }

    @Test
    public void testSomeRatios() {
        ratios("You gonna ride the wave", "what are you even doing", 0.84210527f, 1.0f, 0.8f, 0.8807018f);
        ratios("ROOT IS A LEGEND!", "Noodle is great", 0.84615386f, 0.7692308f, 0.71428573f, 0.7765568f);
        ratios("Not pleased", "And to sleep", 0.8f, 0.7f, 1.0f, 0.8333333f);
        ratios("yay restock", "Yay rockets", 0.6f, 0.6f, 0.5f, 0.56666666f);
        ratios("Now that's a game", "what GOAT means", 0.7692308f, 0.7692308f, 1.0f, 0.8461539f);
    }

    @Test
    public void sameTweet() {
        ratios("Not pleased", "Not pleased", 0.0f, 0.0f, 0.0f, 0.0f);
        ratios("a", "a", 0.0f, 0.0f, 0.0f, 0.0f);
        ratios("ABC", "abc", 0.0f, 0.0f, 0.0f, 0.0f);
        ratios("abc", "ABC", 0.0f, 0.0f, 0.0f, 0.0f);
        ratios("        abc", "ABC            ", 0.0f, 0.0f, 0.0f, 0.0f);
    }
}
