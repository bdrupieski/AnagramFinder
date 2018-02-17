package models;

import junit.framework.Assert;
import org.junit.Test;

public class AnagramTests {

    @Test
    public void when_someArbitraryTweets_then_ratiosAreStillWhateverTheyWereWhenIWroteThisTest() {
        assertRatiosEqual("You gonna ride the wave", "what are you even doing", 0.84210527f, 1.0f, 0.8f, 1.0f, 0.5f);
        assertRatiosEqual("ROOT IS A LEGEND!", "Noodle is great", 0.84615386f, 0.7692308f, 0.71428573f, 1.0f, 0.22727273f);
        assertRatiosEqual("Not pleased", "And to sleep", 0.8f, 0.7f, 1.0f, 1.0f, 0.09090909f);
        assertRatiosEqual("yay restock", "Yay rockets", 0.6f, 0.6f, 0.5f, 1.0f, 0.09090909f);
        assertRatiosEqual("Now that's a game", "what GOAT means", 0.7692308f, 0.7692308f, 1.0f, 1.0f, 0.22727273f);
    }

    @Test
    public void when_tweetTextTheSame_then_matchRatiosAreZero() {
        assertRatiosEqual("Not pleased", "Not pleased", 0.0f, 0.0f, 0.0f, 1.0f, 0.09090909f);
        assertRatiosEqual("aaaaaaaa", "aaaaaaaa", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        assertRatiosEqual("ABCABCABC", "abcabcabc", 0.0f, 0.0f, 0.0f, 0.0f, 0.045454547f);
        assertRatiosEqual("abcabcabc", "ABCABCABC", 0.0f, 0.0f, 0.0f, 0.0f, 0.045454547f);
        assertRatiosEqual("        abcabcabc        ", "        ABCABCABC            ", 0.0f, 0.0f, 0.0f, 0.0f, 0.045454547f);
    }

    private static AnagramMatch buildAnagramMatchFromText(String a, String b) {
        Tweet tweetA = Tweet.fromText(a);
        Tweet tweetB = Tweet.fromText(b);
        return AnagramMatch.fromTweetPair(tweetA, tweetB);
    }

    private static void assertRatiosEqual(String a, String b, float longestCommonSubstring, float editDistance, float wordCount, float englishWordCount, float totalLength) {
        AnagramMatch match = buildAnagramMatchFromText(a, b);

        Assert.assertEquals(longestCommonSubstring, match.getLcsLengthToTotalLengthRatio());
        Assert.assertEquals(editDistance, match.getEditDistanceToLengthRatio());
        Assert.assertEquals(wordCount, match.getDifferentWordCountToTotalWordCount());
        Assert.assertEquals(englishWordCount, match.getEnglishWordsToTotalWordCount());
        Assert.assertEquals(totalLength, match.getTotalLengthToHighestLengthCapturedRatio());
    }
}
