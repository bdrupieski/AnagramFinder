package textprocessing;

import org.junit.Assert;
import org.junit.Test;

public class LongestCommonSubstringLengthTests {

    @Test
    public void when_sameStrings_then_longestCommonSubstringIsEqualToStringLength() {
        String hello = "hello";
        length(hello, hello, hello.length());

        String helloHowAreYou = "hello how are you";
        length(helloHowAreYou, helloHowAreYou, helloHowAreYou.length());
    }

    @Test
    public void when_stringsCompletelyDifferent_then_longestCommonSubstringIsOnlyAnySharedCharacters() {
        length("hello", "goodbye", 1); // "o"
        length("one two three", "four five six", 2); // "e "
    }

    @Test
    public void when_actualAnagrams_then_longestCommonSubstringIsWhateverItIs() {
        length("The Game is ON", "Omg he's eatin!!!", 2); // "s "
        length("I'm used to it", "Studiotime.", 1);
    }

    private static void length(String a, String b, int length) {
        Assert.assertEquals(length, MatchScoringMetrics.longestCommonSubstring(a, b));
    }
}
