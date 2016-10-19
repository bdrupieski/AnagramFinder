package anagramutils.persistence;

import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import anagramutils.AnagramMatch;

@RegisterMapper(AnagramMatchMapper.class)
public interface AnagramMatchDao {
    @SqlBatch("insert into anagram_matches " +
            "(" +
            "tweet1_id, " +
            "tweet2_id, " +
            "edit_distance_original_text, " +
            "edit_distance_stripped_text, " +
            "hamming_distance_stripped_text, " +
            "longest_common_substring_length_stripped_text, " +
            "word_count_difference, " +
            "total_words, " +
            "inverse_lcs_length_to_total_length_ratio, " +
            "edit_distance_to_length_ratio, " +
            "different_word_count_to_total_word_count_ratio, " +
            "is_same_rearranged, " +
            "interesting_factor, " +
            ") " +
            "values " +
            "(" +
            ":tweet1Id, " +
            ":tweet2Id, " +
            ":editDistanceOriginalText, " +
            ":editDistanceStrippedText, " +
            ":hammingDistanceStrippedText, " +
            ":longestCommonSubstringLengthStrippedText, " +
            ":wordCountDifference, " +
            ":totalUniqueWords, " +
            ":lcsLengthToTotalLengthRatio, " +
            ":editDistanceToLengthRatio, " +
            ":differentWordCountToTotalWordCount, " +
            ":isSameRearranged, " +
            ":interestingFactor, " +
            ")")
    void insert(@BindAnagramMatch Iterable<AnagramMatch> anagramMatches);

    void close();
}