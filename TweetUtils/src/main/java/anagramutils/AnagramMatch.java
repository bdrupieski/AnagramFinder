package anagramutils;

import anagramutils.processing.MatchMetrics;

import java.util.UUID;

public class AnagramMatch {

    private int id;
    private UUID tweet1Id;
    private UUID tweet2Id;
    private int editDistanceOriginalText;
    private int editDistanceStrippedText;
    private int hammingDistanceStrippedText;
    private int longestCommonSubstringLengthStrippedText;
    private int wordCountDifference;
    private int totalUniqueWords;
    private float lcsLengthToTotalLengthRatio;
    private float editDistanceToLengthRatio;
    private float differentWordCountToTotalWordCount;
    private IsSameWhenRearrangedEnum isSameRearranged;
    private float interestingFactor;
    private boolean posted;
    private boolean rejected;

    private AnagramMatch() {
    }

    public AnagramMatch(int id, UUID tweet1Id, UUID tweet2Id, int editDistanceOriginalText, int editDistanceStrippedText, int hammingDistanceStrippedText, int longestCommonSubstringLengthStrippedText, int wordCountDifference, int totalUniqueWords, float lcsLengthToTotalLengthRatio, float editDistanceToLengthRatio, float differentWordCountToTotalWordCount, IsSameWhenRearrangedEnum isSameRearranged, float interestingFactor, boolean posted, boolean rejected) {
        this.id = id;
        this.tweet1Id = tweet1Id;
        this.tweet2Id = tweet2Id;
        this.editDistanceOriginalText = editDistanceOriginalText;
        this.editDistanceStrippedText = editDistanceStrippedText;
        this.hammingDistanceStrippedText = hammingDistanceStrippedText;
        this.longestCommonSubstringLengthStrippedText = longestCommonSubstringLengthStrippedText;
        this.wordCountDifference = wordCountDifference;
        this.totalUniqueWords = totalUniqueWords;
        this.lcsLengthToTotalLengthRatio = lcsLengthToTotalLengthRatio;
        this.editDistanceToLengthRatio = editDistanceToLengthRatio;
        this.differentWordCountToTotalWordCount = differentWordCountToTotalWordCount;
        this.isSameRearranged = isSameRearranged;
        this.interestingFactor = interestingFactor;
        this.posted = posted;
        this.rejected = rejected;
    }

    public static AnagramMatch fromTweetPair(Tweet a, Tweet b) {
        int originalTextEditDistance = MatchMetrics.demerauLevenshteinDistance(a.getTweetOriginalText(), b.getTweetOriginalText());
        int strippedTextEditDistance = MatchMetrics.demerauLevenshteinDistance(a.getTweetStrippedText(), b.getTweetStrippedText());
        int hammingDistanceStrippedText = MatchMetrics.hammingDistance(a.getTweetStrippedText(), b.getTweetStrippedText());
        int lcsLengthStrippedText = MatchMetrics.longestCommonSubstring(a.getTweetStrippedText(), b.getTweetStrippedText());
        MatchMetrics.WordCountDifference wordCountDifference = MatchMetrics.getWordCountDifference(a.getTweetOriginalText(), b.getTweetOriginalText());
        IsSameWhenRearrangedEnum sameWhenWordsRearranged = MatchMetrics.isSameWhenWordsRearranged(a.getTweetStrippedText(), b.getTweetStrippedText());
        int length = a.getTweetSortedStrippedText().length();
        float inverseLcsLengthToLengthRatio = 1 - ((float) lcsLengthStrippedText / length);
        float editDistanceToLengthRatio = (float)strippedTextEditDistance / length;
        float diffWordCountToTotalWordCountRatio = (float)wordCountDifference.getWordCountDifference() / wordCountDifference.getTotalWords();
        float interestingFactor = (inverseLcsLengthToLengthRatio + editDistanceToLengthRatio + diffWordCountToTotalWordCountRatio) / 3.0f;

        return new AnagramMatch(0, a.getId(), b.getId(), originalTextEditDistance, strippedTextEditDistance,
                hammingDistanceStrippedText, lcsLengthStrippedText,
                wordCountDifference.getWordCountDifference(), wordCountDifference.getTotalWords(),
                inverseLcsLengthToLengthRatio, editDistanceToLengthRatio, diffWordCountToTotalWordCountRatio,
                sameWhenWordsRearranged,interestingFactor, false, false);
    }

    public int getId() {
        return id;
    }

    public UUID getTweet1Id() {
        return tweet1Id;
    }

    public UUID getTweet2Id() {
        return tweet2Id;
    }

    public int getEditDistanceOriginalText() {
        return editDistanceOriginalText;
    }

    public int getEditDistanceStrippedText() {
        return editDistanceStrippedText;
    }

    public int getHammingDistanceStrippedText() {
        return hammingDistanceStrippedText;
    }

    public int getLongestCommonSubstringLengthStrippedText() {
        return longestCommonSubstringLengthStrippedText;
    }

    public int getWordCountDifference() {
        return wordCountDifference;
    }

    public int getTotalUniqueWords() {
        return totalUniqueWords;
    }

    public float getLcsLengthToTotalLengthRatio() {
        return lcsLengthToTotalLengthRatio;
    }

    public float getEditDistanceToLengthRatio() {
        return editDistanceToLengthRatio;
    }

    public float getDifferentWordCountToTotalWordCount() {
        return differentWordCountToTotalWordCount;
    }

    public IsSameWhenRearrangedEnum getIsSameRearranged() {
        return isSameRearranged;
    }

    public float getInterestingFactor() {
        return interestingFactor;
    }

    public boolean IsPosted() {
        return posted;
    }

    public boolean IsRejected() {
        return rejected;
    }
}
