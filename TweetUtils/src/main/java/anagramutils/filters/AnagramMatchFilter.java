package anagramutils.filters;

import anagramutils.AnagramMatch;
import anagramutils.IsSameWhenRearrangedEnum;

public class AnagramMatchFilter {

    private AnagramMatchFilter() {
    }

    public static boolean isGoodMatch(AnagramMatch anagramMatch) {
        return anagramMatch.getEditDistanceToLengthRatio() > 0.3 &&
                anagramMatch.getLcsLengthToTotalLengthRatio() > 0.3 &&
                anagramMatch.getDifferentWordCountToTotalWordCount() > 0.3 &&
                anagramMatch.getEnglishWordsToTotalWordCount() > 0.3 &&
                anagramMatch.getInterestingFactor() > 0.6 &&
                anagramMatch.getIsSameRearranged() != IsSameWhenRearrangedEnum.TRUE;
    }
}
