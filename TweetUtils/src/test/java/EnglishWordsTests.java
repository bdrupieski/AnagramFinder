import anagramutils.processing.MatchMetrics;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class EnglishWordsTests {

    private static int NumberOfEnglishWords(String sentence) {
        return MatchMetrics.numberOfEnglishWords(MatchMetrics.tokenizeTweetText(sentence));
    }

    @Test
    public void simpleSentences() {
        Assert.assertEquals(3, NumberOfEnglishWords("one two three"));
        Assert.assertEquals(6, NumberOfEnglishWords("I can't, believe. it's  not butter!!"));
        Assert.assertEquals(0, NumberOfEnglishWords("NOOOO IIITT CAAANNT BEEEE!!!"));
        Assert.assertEquals(3, NumberOfEnglishWords("some are words AAAAAAND SOMEOFTHEMARENOT!!!"));
    }

    @Test
    public void contractions() {

        Assert.assertEquals(1, NumberOfEnglishWords("I'm"));
        Assert.assertEquals(1, NumberOfEnglishWords("I'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("I'd"));
        Assert.assertEquals(1, NumberOfEnglishWords("I've"));

        Assert.assertEquals(1, NumberOfEnglishWords("you're"));
        Assert.assertEquals(1, NumberOfEnglishWords("you'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("you'd"));
        Assert.assertEquals(1, NumberOfEnglishWords("you've"));

        Assert.assertEquals(1, NumberOfEnglishWords("he's"));
        Assert.assertEquals(1, NumberOfEnglishWords("he'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("he'd"));
        Assert.assertEquals(1, NumberOfEnglishWords("he's"));

        Assert.assertEquals(1, NumberOfEnglishWords("she's"));
        Assert.assertEquals(1, NumberOfEnglishWords("she'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("she'd"));
        Assert.assertEquals(1, NumberOfEnglishWords("she's"));

        Assert.assertEquals(1, NumberOfEnglishWords("it's"));
        Assert.assertEquals(1, NumberOfEnglishWords("it'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("it'd"));

        Assert.assertEquals(1, NumberOfEnglishWords("we're"));
        Assert.assertEquals(1, NumberOfEnglishWords("we'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("we'd"));
        Assert.assertEquals(1, NumberOfEnglishWords("we've"));

        Assert.assertEquals(1, NumberOfEnglishWords("they're"));
        Assert.assertEquals(1, NumberOfEnglishWords("they'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("they'd"));
        Assert.assertEquals(1, NumberOfEnglishWords("they've"));

        Assert.assertEquals(1, NumberOfEnglishWords("that's"));
        Assert.assertEquals(1, NumberOfEnglishWords("that'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("that'd"));

        Assert.assertEquals(1, NumberOfEnglishWords("who's"));
        Assert.assertEquals(1, NumberOfEnglishWords("who'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("who'd"));

        Assert.assertEquals(1, NumberOfEnglishWords("what's"));
        Assert.assertEquals(1, NumberOfEnglishWords("what're"));
        Assert.assertEquals(1, NumberOfEnglishWords("what'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("what'd"));

        Assert.assertEquals(1, NumberOfEnglishWords("where's"));
        Assert.assertEquals(1, NumberOfEnglishWords("where'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("where'd"));

        Assert.assertEquals(1, NumberOfEnglishWords("when's"));
        Assert.assertEquals(1, NumberOfEnglishWords("when'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("when'd"));

        Assert.assertEquals(1, NumberOfEnglishWords("why's"));
        Assert.assertEquals(1, NumberOfEnglishWords("why'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("why'd"));

        Assert.assertEquals(1, NumberOfEnglishWords("how's"));
        Assert.assertEquals(1, NumberOfEnglishWords("how'll"));
        Assert.assertEquals(1, NumberOfEnglishWords("how'd"));
    }
}
