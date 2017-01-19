import anagramutils.processing.MatchMetrics;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Update;

import java.util.List;
import java.util.Map;

public class BackfillEnglishWordRatio {

    public static void main(String[] args)  {

        Config appConfig = ConfigFactory.load();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(appConfig.getString("database.url"));
        hikariConfig.setUsername(appConfig.getString("database.user"));
        hikariConfig.setPassword(appConfig.getString("database.password"));
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        DBI dbi = new DBI(hikariDataSource);
        Handle h = dbi.open();

        List<Map<String, Object>> rs = h.select(
                "SELECT\n" +
                        "  anagram_matches.id,\n" +
                        "  tweet1.original_text AS t1_originalText,\n" +
                        "  tweet2.original_text AS t2_originalText\n" +
                        "FROM\n" +
                        "  anagram_matches\n" +
                        "  INNER JOIN tweets tweet1 ON anagram_matches.tweet1_id = tweet1.id\n" +
                        "  INNER JOIN tweets tweet2 ON anagram_matches.tweet2_id = tweet2.id");

        for (Map<String, Object> props: rs) {
            Integer id = (Integer)props.get("id");
            String t1OriginalText = (String)props.get("t1_originalText");
            String t2OriginalText = (String)props.get("t2_originalText");

            MatchMetrics.WordCountDifference wordCountDifference = MatchMetrics.getWordCountDifference(t1OriginalText, t2OriginalText);

            int englishWordsInTweetA = MatchMetrics.numberOfEnglishWords(t1OriginalText);
            int englishWordsInTweetB = MatchMetrics.numberOfEnglishWords(t2OriginalText);
            float englishWordsToTotalWordCountRatio = (float)(englishWordsInTweetA + englishWordsInTweetB) / wordCountDifference.getTotalWords();

            System.out.println(id + " " + t1OriginalText + " " + t2OriginalText + " " + englishWordsToTotalWordCountRatio);

            Update s = h.createStatement("UPDATE anagram_matches\n" +
                    "SET english_words_to_total_word_count_ratio = :ratio\n" +
                    "WHERE id = :id");
            s.bind("ratio", englishWordsToTotalWordCountRatio);
            s.bind("id", id);
            s.execute();
        }

        h.close();
    }
}
