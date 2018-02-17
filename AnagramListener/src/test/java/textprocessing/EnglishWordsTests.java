package textprocessing;

import org.junit.Assert;
import org.junit.Test;

public class EnglishWordsTests {

    private static int numberOfEnglishWords(String sentence) {
        return MatchScoringMetrics.numberOfEnglishWords(MatchScoringMetrics.tokenizeTweetText(sentence));
    }

    @Test
    public void simpleSentences() {
        Assert.assertEquals(3, numberOfEnglishWords("one two three"));
        Assert.assertEquals(6, numberOfEnglishWords("I can't, believe. it's  not butter!!"));
        Assert.assertEquals(0, numberOfEnglishWords("NOOOO IIITT CAAANNT BEEEE!!!"));
        Assert.assertEquals(3, numberOfEnglishWords("some are words AAAAAAND SOMEOFTHEMARENOT!!!"));
    }

    @Test
    public void contractions() {

        Assert.assertEquals(1, numberOfEnglishWords("I'm"));
        Assert.assertEquals(1, numberOfEnglishWords("I'll"));
        Assert.assertEquals(1, numberOfEnglishWords("I'd"));
        Assert.assertEquals(1, numberOfEnglishWords("I've"));

        Assert.assertEquals(1, numberOfEnglishWords("you're"));
        Assert.assertEquals(1, numberOfEnglishWords("you'll"));
        Assert.assertEquals(1, numberOfEnglishWords("you'd"));
        Assert.assertEquals(1, numberOfEnglishWords("you've"));

        Assert.assertEquals(1, numberOfEnglishWords("he's"));
        Assert.assertEquals(1, numberOfEnglishWords("he'll"));
        Assert.assertEquals(1, numberOfEnglishWords("he'd"));
        Assert.assertEquals(1, numberOfEnglishWords("he's"));

        Assert.assertEquals(1, numberOfEnglishWords("she's"));
        Assert.assertEquals(1, numberOfEnglishWords("she'll"));
        Assert.assertEquals(1, numberOfEnglishWords("she'd"));
        Assert.assertEquals(1, numberOfEnglishWords("she's"));

        Assert.assertEquals(1, numberOfEnglishWords("it's"));
        Assert.assertEquals(1, numberOfEnglishWords("it'll"));
        Assert.assertEquals(1, numberOfEnglishWords("it'd"));

        Assert.assertEquals(1, numberOfEnglishWords("we're"));
        Assert.assertEquals(1, numberOfEnglishWords("we'll"));
        Assert.assertEquals(1, numberOfEnglishWords("we'd"));
        Assert.assertEquals(1, numberOfEnglishWords("we've"));

        Assert.assertEquals(1, numberOfEnglishWords("they're"));
        Assert.assertEquals(1, numberOfEnglishWords("they'll"));
        Assert.assertEquals(1, numberOfEnglishWords("they'd"));
        Assert.assertEquals(1, numberOfEnglishWords("they've"));

        Assert.assertEquals(1, numberOfEnglishWords("that's"));
        Assert.assertEquals(1, numberOfEnglishWords("that'll"));
        Assert.assertEquals(1, numberOfEnglishWords("that'd"));

        Assert.assertEquals(1, numberOfEnglishWords("who's"));
        Assert.assertEquals(1, numberOfEnglishWords("who'll"));
        Assert.assertEquals(1, numberOfEnglishWords("who'd"));

        Assert.assertEquals(1, numberOfEnglishWords("what's"));
        Assert.assertEquals(1, numberOfEnglishWords("what're"));
        Assert.assertEquals(1, numberOfEnglishWords("what'll"));
        Assert.assertEquals(1, numberOfEnglishWords("what'd"));

        Assert.assertEquals(1, numberOfEnglishWords("where's"));
        Assert.assertEquals(1, numberOfEnglishWords("where'll"));
        Assert.assertEquals(1, numberOfEnglishWords("where'd"));

        Assert.assertEquals(1, numberOfEnglishWords("when's"));
        Assert.assertEquals(1, numberOfEnglishWords("when'll"));
        Assert.assertEquals(1, numberOfEnglishWords("when'd"));

        Assert.assertEquals(1, numberOfEnglishWords("why's"));
        Assert.assertEquals(1, numberOfEnglishWords("why'll"));
        Assert.assertEquals(1, numberOfEnglishWords("why'd"));

        Assert.assertEquals(1, numberOfEnglishWords("how's"));
        Assert.assertEquals(1, numberOfEnglishWords("how'll"));
        Assert.assertEquals(1, numberOfEnglishWords("how'd"));
    }
}
