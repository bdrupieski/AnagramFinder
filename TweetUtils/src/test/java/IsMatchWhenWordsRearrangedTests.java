import org.junit.Assert;
import org.junit.Test;
import anagramutils.IsSameWhenRearrangedEnum;
import anagramutils.processing.MatchMetrics;

public class IsMatchWhenWordsRearrangedTests {

    private static void isMatch(String a, String b, IsSameWhenRearrangedEnum isSame) {
        String[] aWords = MatchMetrics.tokenizeTweetText(a);
        String[] bWords = MatchMetrics.tokenizeTweetText(b);
        Assert.assertEquals(isSame, MatchMetrics.isSameWhenWordsRearranged(aWords, bWords));
    }

    @Test
    public void simpleMatching() {
        isMatch("this is a test", "thisis a test", IsSameWhenRearrangedEnum.TRUE);
        isMatch("this is a test", "thisisatest", IsSameWhenRearrangedEnum.TRUE);
        isMatch("this is a test", "a test this is", IsSameWhenRearrangedEnum.TRUE);
        isMatch("this is a test", "a test this is", IsSameWhenRearrangedEnum.TRUE);
        isMatch("this is a test", "atestthisis", IsSameWhenRearrangedEnum.TRUE);
        isMatch("this is a test", "testathisis", IsSameWhenRearrangedEnum.TRUE);
        isMatch("this is a test", "THIS... IS... A TEST!!!!", IsSameWhenRearrangedEnum.TRUE);
    }

    @Test
    public void realTweets() {
        isMatch("heartbroken", "broken heart", IsSameWhenRearrangedEnum.TRUE);
        isMatch("Heartbroken \uD83D\uDE1E\uD83D\uDC94", "broken heart", IsSameWhenRearrangedEnum.TRUE);
        isMatch("Me is happy", "Happyisme", IsSameWhenRearrangedEnum.TRUE);
        isMatch("☺☺☺☺☺AllYouNeedIsLove☺☺☺☺☺", "Love is all you need.", IsSameWhenRearrangedEnum.TRUE);
    }

    @Test
    public void notAMatch() {
        isMatch("twd is on yes", "ITS SO WENDY", IsSameWhenRearrangedEnum.FALSE);
        isMatch("Weather is so cute ❤️⛄️", "<Eraticus> oh sweet", IsSameWhenRearrangedEnum.FALSE);
        isMatch("Hungriest.", "Sure thing \uD83D\uDE0F", IsSameWhenRearrangedEnum.FALSE);
        isMatch("hostia, y med'an?", "I hate Mondays \uD83D\uDD95", IsSameWhenRearrangedEnum.FALSE);
        isMatch("The Game is ON", "Omg he's eatin!!!", IsSameWhenRearrangedEnum.FALSE);
    }

    @Test
    public void cantHandleIfAllOneWord() {
        isMatch("yougonnaridethewave", "gonnaridethewaveyou", IsSameWhenRearrangedEnum.FALSE);
    }

    @Test
    public void dontCalculatePermutationsForLongStrings() {
        isMatch("yes this is a very long string", "yes this is a very long string", IsSameWhenRearrangedEnum.TOO_LONG_TO_COMPUTE);
    }
}
