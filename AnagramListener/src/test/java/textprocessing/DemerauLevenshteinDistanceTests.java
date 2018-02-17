package textprocessing;

import org.junit.Assert;
import org.junit.Test;

public class DemerauLevenshteinDistanceTests {

    private static void dist(String a, String b, int distance) {
        Assert.assertEquals(distance, MatchScoringMetrics.demerauLevenshteinDistance(a, b));
    }

    @Test
    public void when_sameString_then_distanceIsZero() {
        dist("", "", 0);
        dist("b", "b", 0);
        dist("brian", "brian", 0);
        dist("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "ABCDEFGHIJKLMNOPQRSTUVWXYZ", 0);
    }

    @Test
    public void when_differentCase_then_caseIsIgnored() {
        dist("ABC", "abc", 0);
        dist("abc", "ACB", 1);
        dist("ACB", "abc", 1);
    }

    @Test
    public void when_singleLetterTransposition_then_distanceIsOne() {
        dist("ABC", "ACB", 1);
        dist("brian", "brina", 1);
        dist("brian", "rbian", 1);
    }

    @Test
    public void when_singleInsertionOrDeletion_then_distanceIsOne() {
        dist("ABC", "ABCD", 1);
        dist("ABCD", "ABC", 1);

        dist("AABC", "ABC", 1);
        dist("ABC", "AABC", 1);

        dist("whoops", "whoops1", 1);
        dist("whoops1", "whoops", 1);
        dist("1whoops", "whoops", 1);
        dist("whoops", "1whoops", 1);
        dist("who1ops", "whoops", 1);
        dist("whoops", "who1ops", 1);
    }

    @Test
    public void when_multipleInsertionsDeletionsTranspositions_then_scoreIsCorrectForAllOfThem() {
        dist("this just in", "thisjustin", 2);
        dist("thisjustin", "this just in", 2);
        dist("this just in", "XXthisjustinXX", 6);
        dist("XXthisjustinXX", "this just in", 6);

        dist("the world is round", "th3 w0rld 1s r0und!!", 6);
        dist("th3 w0rld 1s r0und!!", "the world is round", 6);

        dist("12345", "2134", 2);
        dist("2134", "12345", 2);

        dist("12345", "54321", 4);
        dist("54321", "12345", 4);
    }

    @Test
    public void when_stringsCompletelyDifferent_then_scoreIsStringLength() {
        dist("whoa", "HEYO", 4);
        dist("ABCDEFGHI", "123456789", 9);
        dist("", "123456789", 9);
        dist("ABCDEFGHI", "", 9);
    }
}
