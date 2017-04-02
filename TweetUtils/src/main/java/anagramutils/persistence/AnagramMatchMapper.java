package anagramutils.persistence;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import anagramutils.AnagramMatch;
import anagramutils.IsSameWhenRearrangedEnum;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AnagramMatchMapper implements ResultSetMapper<AnagramMatch> {
    public AnagramMatch map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new AnagramMatch(
                r.getInt("id"),
                (java.util.UUID)r.getObject("tweet1_id"),
                (java.util.UUID)r.getObject("tweet2_id"),
                r.getInt("edit_distance_original_text"),
                r.getInt("edit_distance_stripped_text"),
                r.getInt("hamming_distance_stripped_text"),
                r.getInt("longest_common_substring_length_stripped_text"),
                r.getInt("word_count_difference"),
                r.getInt("total_words"),
                r.getFloat("inverse_lcs_length_to_total_length_ratio"),
                r.getFloat("edit_distance_to_length_ratio"),
                r.getFloat("different_word_count_to_total_word_count_ratio"),
                r.getFloat("english_words_to_total_word_count_ratio"),
                r.getFloat("total_length_to_highest_length_captured_ratio"),
                IsSameWhenRearrangedEnum.fromDatabaseInt(r.getInt("is_same_rearranged")),
                r.getFloat("interesting_factor")
        );
    }
}
