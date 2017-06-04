package textprocessing;

import configuration.AnagramMatchMetricsConfiguration;
import models.AnagramMatch;
import models.IsSameWhenRearrangedEnum;

public class AnagramMatchFilter {

    private AnagramMatchMetricsConfiguration anagramMatchMetricsConfiguration;

    public AnagramMatchFilter(AnagramMatchMetricsConfiguration anagramMatchMetricsConfiguration) {
        this.anagramMatchMetricsConfiguration = anagramMatchMetricsConfiguration;
    }

    public boolean isGoodMatch(AnagramMatch anagramMatch) {
        return anagramMatch.getEditDistanceToLengthRatio() > anagramMatchMetricsConfiguration.getEditDistanceRatioMinimumThreshold() &&
                anagramMatch.getLcsLengthToTotalLengthRatio() > anagramMatchMetricsConfiguration.getLongestCommonSubstringRatioMinimumThreshold() &&
                anagramMatch.getDifferentWordCountToTotalWordCount() > anagramMatchMetricsConfiguration.getDifferentWordCountRatioMinimumThreshold() &&
                anagramMatch.getEnglishWordsToTotalWordCount() > anagramMatchMetricsConfiguration.getEnglishWordsToTotalWordCountRatioMinimumThreshold() &&
                anagramMatch.getInterestingFactor() > anagramMatchMetricsConfiguration.getInterestingFactorMinimumThreshold() &&
                anagramMatch.getIsSameRearranged() != IsSameWhenRearrangedEnum.TRUE;
    }
}
