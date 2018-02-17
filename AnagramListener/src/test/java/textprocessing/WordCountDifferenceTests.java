package textprocessing;

import junit.framework.Assert;
import org.junit.Test;

public class WordCountDifferenceTests {

    @Test
    public void when_textPairsAreSameWordsButOnlyDifferentOrder_then_NoDifferenceInWordCount() {
        wordCount("this is a test", "this is a test", 0, 8);
        wordCount("this is a test", "is this a test?", 0, 8);
        wordCount("this is a test", "a test, this is", 0, 8);
        wordCount("this is a test", "this, a test, is", 0, 8);
        wordCount("this is a test", "$THIS IS A TEST!!!!", 0, 8);
    }

    @Test
    public void when_textPairsAreSameExceptOneAdditionalWord_then_differenceOfOne() {
        wordCount("this is a test", "this is a test too", 1, 9);
        wordCount("this is a test", "this is also a test", 1, 9);
        wordCount("this is a test", "yes this is a test", 1, 9);
    }

    @Test
    public void when_textPairsAreSameExceptMultipleAdditionalWords_then_countEqualToNumberOfAdditionalWords() {
        wordCount("this is a test", "this is a test, indeed it is", 3, 11);
        wordCount("this is a test", "this is a test of the emergency broadcast system", 5, 13);
    }

    @Test
    public void when_textPairsDifferentButWithSomeOverlap_then_wordCountDifferenceEqualToOverlap() {
        wordCount("one two three", "two three four", 2, 6);
        wordCount("two three four", "three four five six", 3, 7);
    }

    @Test
    public void when_textPairsCompletelyDifferent_wordCountDifferenceEqualToTotalNumberOfWords() {
        wordCount("one two three", "four five six", 6, 6);
        wordCount("to be or not to be", "indeed", 7, 7);
        wordCount("yes yes yes yes yes", "no no no no no", 10, 10);
        wordCount("yes yes yes yes yes", "nononono non nonono nono on", 10, 10);
    }

    @Test
    public void when_textPairsHaveWeirdWhitespace_then_countsNotAffectedByWhitespace() {
        wordCount("        yes       ", "      no            ", 2, 2);
        wordCount("yes yes yes", "   no no no", 6, 6);
        wordCount("   yes   yes   yes   ", "   no   no   no   ", 6, 6);
        wordCount("yes\nyes\nyes\n", "\r\nno\nno\t\tno", 6, 6);
        wordCount("yes\n\t\r\nyes\nyes\n", " \r\nyes\t\r\n\n\r  \nyes\t \tyes", 0, 6);
    }

    @Test
    public void when_textPairsHaveWeirdCharacters_then_weirdCharactersTreatedAsWhitespace() {
        wordCount("this.is.a.test", "this is a test", 0, 8);
        wordCount("!!!~~this.is\t\r\r\r\n\n\na.test%~ ~ ~ ~ ~", ")!@$)&*^!@$&*this.~^()is%a    ! !! !~ test%%", 0, 8);
        wordCount("Why I can't sleep _-_!@#$%^&*()_+-=~`/", "Why cant i sleep", 0, 8);
    }

    private static void wordCount(String a, String b, int countDiff, int totalWords) {
        String[] aWords = MatchScoringMetrics.tokenizeTweetText(a);
        String[] bWords = MatchScoringMetrics.tokenizeTweetText(b);
        MatchScoringMetrics.WordCountDifference wordCountDifference = MatchScoringMetrics.getWordCountDifference(aWords, bWords);
        Assert.assertEquals(countDiff, wordCountDifference.getWordCountDifference());
        Assert.assertEquals(totalWords, wordCountDifference.getTotalWords());
    }
}
