import org.junit.Assert;
import org.junit.Test;
import anagramutils.processing.MatchMetrics;

public class LongestCommonSubstringLengthTests {

    private static void length(String a, String b, int length) {
        Assert.assertEquals(length, MatchMetrics.longestCommonSubstring(a, b));
    }

    @Test
    public void sameStringHasSubstringEqualToLength() {
        length("hello", "hello", 5);
        length("hello how are you", "hello how are you", 17);
    }

    @Test
    public void completelyDifferentStrings() {
        length("hello", "goodbye", 1); // "o"
        length("one two three", "four five six", 2); // "e "
    }

    @Test
    public void anagrams() {
        length("The Game is ON", "Omg he's eatin!!!", 2); // "s "
        length("I'm used to it", "Studiotime.", 1);
    }
}
