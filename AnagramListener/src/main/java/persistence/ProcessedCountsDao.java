package persistence;

import models.ProcessedCounts;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface ProcessedCountsDao {

    @SqlUpdate("insert into public.processed_counts " +
            "(" +
            "now, " +
            "previous_reset, " +
            "seconds_since_previous_reset, " +
            "received_status_count_since_previous_reset, " +
            "status_met_filter_count_since_previous_reset, " +
            "tweet_met_filter_count_since_previous_reset, " +
            "saved_tweet_count_since_previous_reset, " +
            "saved_anagram_count_since_previous_reset " +
            ") " +
            "values " +
            "(" +
            ":now, " +
            ":previousReset, " +
            ":secondsSincePreviousReset, " +
            ":receivedStatusCountSincePreviousReset, " +
            ":statusMetFilterCountSincePreviousReset, " +
            ":tweetMetFilterCountSincePreviousReset, " +
            ":savedTweetCountSincePreviousReset, " +
            ":savedAnagramCountSincePreviousReset " +
            ")")
    void insert(@BindBean ProcessedCounts processedCounts);
}
