import org.junit.Assert;
import org.junit.Test;
import anagramutils.processing.Normalize;

public class NormalizeStringTests {

    private static void norm(String crazy, String normalized) {
        Assert.assertEquals(normalized, Normalize.normalize(crazy));
    }

    @Test
    public void normalizeNonAscii() {
        norm("ÀÁÂÃĀĂȦÄẢÅǍȀȂĄẠḀẦẤàáâä", "aaaaaaaaaaaaaaaaaaaaaa");
        norm("ÉÊẼĒĔËȆȄȨĖèéêẽēȅë", "eeeeeeeeeeeeeeeee");
        norm("ÌÍÏïØøÒÖÔöÜüŇñÇçß", "iiiioooooouunnccss");
    }

    @Test
    public void lowercase() {
        norm("ABCDabcd", "abcdabcd");
    }

    @Test
    public void spaces() {
        norm("This is a test!", "thisisatest");
    }
}
