import junit.framework.Assert;
import org.junit.Test;
import anagramutils.AnagramMatch;
import anagramutils.Tweet;
import anagramutils.processing.ProcessedTweetText;

import java.sql.Timestamp;
import java.util.UUID;

public class FullRatioTests {

    private static AnagramMatch matchFromText(String a, String b) {
        Tweet tweetA = Util.tweetFromText(a);
        Tweet tweetB = Util.tweetFromText(b);
        return AnagramMatch.fromTweetPair(tweetA, tweetB);
    }

    private static void ratios(String a, String b, float lcs, float edit, float wc, float ewc, float tl) {
        AnagramMatch match = matchFromText(a, b);

        Assert.assertEquals(lcs, match.getLcsLengthToTotalLengthRatio());
        Assert.assertEquals(edit, match.getEditDistanceToLengthRatio());
        Assert.assertEquals(wc, match.getDifferentWordCountToTotalWordCount());
        Assert.assertEquals(ewc, match.getEnglishWordsToTotalWordCount());
        Assert.assertEquals(tl, match.getTotalLengthToHighestLengthCapturedRatio());
    }

    @Test
    public void testSomeRatios() {
        ratios("You gonna ride the wave", "what are you even doing", 0.84210527f, 1.0f, 0.8f, 1.0f, 0.5f);
        ratios("ROOT IS A LEGEND!", "Noodle is great", 0.84615386f, 0.7692308f, 0.71428573f, 1.0f, 0.22727273f);
        ratios("Not pleased", "And to sleep", 0.8f, 0.7f, 1.0f, 1.0f, 0.09090909f);
        ratios("yay restock", "Yay rockets", 0.6f, 0.6f, 0.5f, 1.0f, 0.09090909f);
        ratios("Now that's a game", "what GOAT means", 0.7692308f, 0.7692308f, 1.0f, 1.0f, 0.22727273f);
    }

    @Test
    public void sameTweet() {
        ratios("Not pleased", "Not pleased", 0.0f, 0.0f, 0.0f, 1.0f, 0.09090909f);
        ratios("aaaaaaaa", "aaaaaaaa", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        ratios("ABCABCABC", "abcabcabc", 0.0f, 0.0f, 0.0f, 0.0f, 0.045454547f);
        ratios("abcabcabc", "ABCABCABC", 0.0f, 0.0f, 0.0f, 0.0f, 0.045454547f);
        ratios("        abcabcabc        ", "        ABCABCABC            ", 0.0f, 0.0f, 0.0f, 0.0f, 0.045454547f);
    }
}
