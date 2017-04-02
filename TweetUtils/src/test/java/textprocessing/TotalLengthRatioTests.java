package metricandprocessingtests;

import anagramutils.textprocessing.MatchScoringMetrics;
import junit.framework.Assert;
import org.junit.Test;

public class TotalLengthRatioTests {

    private final float floatComparisonDelta = 0.0001f;

    @Test
    public void ZeroMin() {
        int minLength = 0;
        int maxLength = 10;

        Assert.assertEquals(0.1, MatchScoringMetrics.totalLengthRatio(1, minLength, maxLength), floatComparisonDelta);
        Assert.assertEquals(0.5, MatchScoringMetrics.totalLengthRatio(5, minLength, maxLength), floatComparisonDelta);
        Assert.assertEquals(1.0, MatchScoringMetrics.totalLengthRatio(10, minLength, maxLength), floatComparisonDelta);
    }

    @Test
    public void NonzeroMin() {
        int minLength = 8;
        int maxLength = 18;

        Assert.assertEquals(0.1, MatchScoringMetrics.totalLengthRatio(9, minLength, maxLength), floatComparisonDelta);
        Assert.assertEquals(0.5, MatchScoringMetrics.totalLengthRatio(13, minLength, maxLength), floatComparisonDelta);
        Assert.assertEquals(1.0, MatchScoringMetrics.totalLengthRatio(18, minLength, maxLength), floatComparisonDelta);
    }
}
