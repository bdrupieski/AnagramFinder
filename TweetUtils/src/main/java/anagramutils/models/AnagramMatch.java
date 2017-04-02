package anagramutils.models;

import anagramutils.textprocessing.MatchScoringMetrics;

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
    private float englishWordsToTotalWordCount;
    private float totalLengthToHighestLengthCapturedRatio;
    private IsSameWhenRearrangedEnum isSameRearranged;
    private float interestingFactor;

    public AnagramMatch(int id, UUID tweet1Id, UUID tweet2Id, int editDistanceOriginalText, int editDistanceStrippedText, int hammingDistanceStrippedText, int longestCommonSubstringLengthStrippedText, int wordCountDifference, int totalUniqueWords, float lcsLengthToTotalLengthRatio, float editDistanceToLengthRatio, float differentWordCountToTotalWordCount, float englishWordsToTotalWordCount, float totalLengthToHighestLengthCapturedRatio, IsSameWhenRearrangedEnum isSameRearranged, float interestingFactor) {
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
        this.englishWordsToTotalWordCount = englishWordsToTotalWordCount;
        this.totalLengthToHighestLengthCapturedRatio = totalLengthToHighestLengthCapturedRatio;
        this.isSameRearranged = isSameRearranged;
        this.interestingFactor = interestingFactor;
    }

    public static AnagramMatch fromTweetPair(Tweet a, Tweet b) {

        String[] tweet1Words = MatchScoringMetrics.tokenizeTweetText(a.getTweetOriginalText());
        String[] tweet2Words = MatchScoringMetrics.tokenizeTweetText(b.getTweetOriginalText());

        int originalTextEditDistance = MatchScoringMetrics.demerauLevenshteinDistance(a.getTweetOriginalText(), b.getTweetOriginalText());
        int strippedTextEditDistance = MatchScoringMetrics.demerauLevenshteinDistance(a.getTweetStrippedText(), b.getTweetStrippedText());
        int hammingDistanceStrippedText = MatchScoringMetrics.hammingDistance(a.getTweetStrippedText(), b.getTweetStrippedText());
        int lcsLengthStrippedText = MatchScoringMetrics.longestCommonSubstring(a.getTweetStrippedText(), b.getTweetStrippedText());
        MatchScoringMetrics.WordCountDifference wordCountDifference = MatchScoringMetrics.getWordCountDifference(tweet1Words, tweet2Words);
        IsSameWhenRearrangedEnum sameWhenWordsRearranged = MatchScoringMetrics.isSameWhenWordsRearranged(tweet1Words, tweet2Words);
        int length = a.getTweetSortedStrippedText().length();
        float inverseLcsLengthToLengthRatio = 1 - ((float) lcsLengthStrippedText / length);
        float editDistanceToLengthRatio = (float)strippedTextEditDistance / length;
        float diffWordCountToTotalWordCountRatio = (float)wordCountDifference.getWordCountDifference() / wordCountDifference.getTotalWords();
        int englishWordsInTweetA = MatchScoringMetrics.numberOfEnglishWords(tweet1Words);
        int englishWordsInTweetB = MatchScoringMetrics.numberOfEnglishWords(tweet2Words);
        float englishWordsToTotalWordCountRatio = (float)(englishWordsInTweetA + englishWordsInTweetB) / wordCountDifference.getTotalWords();
        float totalLengthRatio = MatchScoringMetrics.totalLengthRatio(a.getTweetStrippedText().length());
        float interestingFactor =
                (inverseLcsLengthToLengthRatio +
                        editDistanceToLengthRatio +
                        diffWordCountToTotalWordCountRatio +
                        englishWordsToTotalWordCountRatio +
                        totalLengthRatio) / 5.0f;

        return new AnagramMatch(0, a.getId(), b.getId(), originalTextEditDistance, strippedTextEditDistance,
                hammingDistanceStrippedText, lcsLengthStrippedText,
                wordCountDifference.getWordCountDifference(), wordCountDifference.getTotalWords(),
                inverseLcsLengthToLengthRatio, editDistanceToLengthRatio, diffWordCountToTotalWordCountRatio,
                englishWordsToTotalWordCountRatio, totalLengthRatio, sameWhenWordsRearranged, interestingFactor);
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

    public float getEnglishWordsToTotalWordCount() {
        return englishWordsToTotalWordCount;
    }

    public float getTotalLengthToHighestLengthCapturedRatio() {
        return totalLengthToHighestLengthCapturedRatio;
    }

    public IsSameWhenRearrangedEnum getIsSameRearranged() {
        return isSameRearranged;
    }

    public float getInterestingFactor() {
        return interestingFactor;
    }
}
