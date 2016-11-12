package anagramutils.filters;

import anagramutils.AnagramMatch;
import anagramutils.IsSameWhenRearrangedEnum;

public class AnagramMatchFilter {

    private AnagramMatchFilter() {
    }

    public static boolean isGoodMatch(AnagramMatch anagramMatch) {
        return anagramMatch.getInterestingFactor() > 0.6 &&
                anagramMatch.getLcsLengthToTotalLengthRatio() > 0.4 &&
                anagramMatch.getEditDistanceToLengthRatio() > 0.4 &&
                anagramMatch.getWordCountDifference() > 0.4 &&
                anagramMatch.getIsSameRearranged() != IsSameWhenRearrangedEnum.TRUE;
    }
}
