package anagramutils.textprocessing;

import anagramutils.models.AnagramMatch;
import anagramutils.models.IsSameWhenRearrangedEnum;

public class AnagramMatchFilter {

    private AnagramMatchFilter() {
    }

    public static boolean isGoodMatch(AnagramMatch anagramMatch) {
        return anagramMatch.getEditDistanceToLengthRatio() > 0.3 &&
                anagramMatch.getLcsLengthToTotalLengthRatio() > 0.3 &&
                anagramMatch.getDifferentWordCountToTotalWordCount() > 0.3 &&
                anagramMatch.getEnglishWordsToTotalWordCount() > 0.3 &&
                anagramMatch.getInterestingFactor() > 0.5 &&
                anagramMatch.getIsSameRearranged() != IsSameWhenRearrangedEnum.TRUE;
    }
}
