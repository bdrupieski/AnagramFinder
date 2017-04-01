package datamanipulation;

import anagramutils.AnagramMatch;
import anagramutils.Tweet;
import anagramutils.processing.ProcessedTweetText;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.Update;

import java.util.List;
import java.util.Map;

public class OneOffMetricsUpdates {

    public static void main(String[] args)  {

    }

    private static List<Map<String, Object>> getAllMatches(Handle handle) {
        return handle.select(
                "SELECT\n" +
                        "  anagram_matches.id,\n" +
                        "  tweet1.original_text AS t1_originalText,\n" +
                        "  tweet2.original_text AS t2_originalText\n" +
                        "FROM\n" +
                        "  anagram_matches\n" +
                        "  INNER JOIN tweets tweet1 ON anagram_matches.tweet1_id = tweet1.id\n" +
                        "  INNER JOIN tweets tweet2 ON anagram_matches.tweet2_id = tweet2.id");
    }

    private static void backFillTotalLengthRatio() {
        DBI dbi = buildDbi();
        Handle h = dbi.open();

        List<Map<String, Object>> allMatches = getAllMatches(h);

        for (Map<String, Object> props: allMatches) {
            Integer id = (Integer)props.get("id");
            String t1OriginalText = (String)props.get("t1_originalText");
            String t2OriginalText = (String)props.get("t2_originalText");

            Tweet a = tweetFromText(t1OriginalText);
            Tweet b = tweetFromText(t2OriginalText);
            AnagramMatch match = AnagramMatch.fromTweetPair(a, b);

            float totalLengthRatio = match.getTotalLengthToHighestLengthCapturedRatio();
            float altInterestingFactor = (
                    match.getLcsLengthToTotalLengthRatio() +
                    match.getEditDistanceToLengthRatio() +
                    match.getDifferentWordCountToTotalWordCount() +
                    match.getEnglishWordsToTotalWordCount() +
                    match.getTotalLengthToHighestLengthCapturedRatio()) / 5.0f;

            System.out.println(id + " " + t1OriginalText + " " + t2OriginalText + " " + totalLengthRatio);
            System.out.println(id + " old IF: " + match.getInterestingFactor() + " alt IF:" + altInterestingFactor);

            Update s = h.createStatement("UPDATE anagram_matches SET\n" +
                    "total_length_to_highest_length_captured_ratio = :ratio,\n" +
                    "alt_interesting_factor = :altInterestingFactor\n" +
                    "WHERE id = :id");
            s.bind("ratio", totalLengthRatio);
            s.bind("altInterestingFactor", altInterestingFactor);
            s.bind("id", id);
            s.execute();
        }

        h.close();
    }

    private static void backFillEnglishWordAndAltInterestingFactor() {
        DBI dbi = buildDbi();
        Handle h = dbi.open();

        List<Map<String, Object>> allMatches = getAllMatches(h);

        for (Map<String, Object> props: allMatches) {
            Integer id = (Integer)props.get("id");
            String t1OriginalText = (String)props.get("t1_originalText");
            String t2OriginalText = (String)props.get("t2_originalText");

            Tweet a = tweetFromText(t1OriginalText);
            Tweet b = tweetFromText(t2OriginalText);
            AnagramMatch match = AnagramMatch.fromTweetPair(a, b);

            float englishWordsToTotalWordCountRatio = match.getEnglishWordsToTotalWordCount();
            float altInterestingFactor = (match.getLcsLengthToTotalLengthRatio() +
                    match.getEditDistanceToLengthRatio() +
                    match.getDifferentWordCountToTotalWordCount() +
                    match.getEnglishWordsToTotalWordCount()) / 4.0f;

            System.out.println(id + " " + t1OriginalText + " " + t2OriginalText + " " + englishWordsToTotalWordCountRatio);
            System.out.println(id + " old IF: " + match.getInterestingFactor() + " alt IF:" + altInterestingFactor);

            Update s = h.createStatement("UPDATE anagram_matches SET\n" +
                    "english_words_to_total_word_count_ratio = :ratio,\n" +
                    "alt_interesting_factor = :altInterestingFactor\n" +
                    "WHERE id = :id");
            s.bind("ratio", englishWordsToTotalWordCountRatio);
            s.bind("altInterestingFactor", altInterestingFactor);
            s.bind("id", id);
            s.execute();
        }

        h.close();
    }

    private static void updateInterestingFactor() {
        DBI dbi = buildDbi();
        Handle h = dbi.open();

        List<Map<String, Object>> rs = h.select(
                "SELECT\n" +
                        "  anagram_matches.id,\n" +
                        "  anagram_matches.interesting_factor,\n" +
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
            float originalInterestingFactor = (float)props.get("interesting_factor");

            Tweet a = tweetFromText(t1OriginalText);
            Tweet b = tweetFromText(t2OriginalText);
            AnagramMatch match = AnagramMatch.fromTweetPair(a, b);
            float newInterestingFactor = match.getInterestingFactor();

            System.out.println(id + " old IF: " + originalInterestingFactor + " new IF:" + newInterestingFactor);

            Update s = h.createStatement("UPDATE anagram_matches SET\n" +
                    "interesting_factor = :interestingFactor\n" +
                    "WHERE id = :id");
            s.bind("interestingFactor", newInterestingFactor);
            s.bind("id", id);
            s.execute();
        }

        h.close();
    }

    private static Tweet tweetFromText(String originalText) {
        ProcessedTweetText processedTweetText = Tweet.processTweetText(originalText);

        return new Tweet(null, 0, null,
                originalText, processedTweetText.getStrippedText(),
                processedTweetText.getSortedStrippedText(), 0L,
                null);
    }

    private static DBI buildDbi() {
        Config appConfig = ConfigFactory.load();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(appConfig.getString("database.url"));
        hikariConfig.setUsername(appConfig.getString("database.user"));
        hikariConfig.setPassword(appConfig.getString("database.password"));
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        return new DBI(hikariDataSource);
    }
}
