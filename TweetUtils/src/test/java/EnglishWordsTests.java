import anagramutils.processing.MatchMetrics;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class EnglishWordsTests {

    @Test
    public void simpleSentences() {
        Assert.assertEquals(3, MatchMetrics.numberOfEnglishWords("one two three"));
        Assert.assertEquals(6, MatchMetrics.numberOfEnglishWords("I can't, believe. it's  not butter!!"));
        Assert.assertEquals(0, MatchMetrics.numberOfEnglishWords("NOOOO IIITT CAAANNT BEEEE!!!"));
        Assert.assertEquals(3, MatchMetrics.numberOfEnglishWords("some are words AAAAAAND SOMEOFTHEMARENOT!!!"));
    }

    @Test
    public void contractions() {

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("I'm"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("I'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("I'd"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("I've"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("you're"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("you'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("you'd"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("you've"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("he's"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("he'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("he'd"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("he's"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("she's"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("she'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("she'd"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("she's"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("it's"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("it'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("it'd"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("we're"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("we'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("we'd"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("we've"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("they're"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("they'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("they'd"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("they've"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("that's"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("that'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("that'd"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("who's"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("who'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("who'd"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("what's"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("what're"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("what'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("what'd"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("where's"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("where'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("where'd"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("when's"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("when'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("when'd"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("why's"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("why'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("why'd"));

        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("how's"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("how'll"));
        Assert.assertEquals(1, MatchMetrics.numberOfEnglishWords("how'd"));
    }
}
