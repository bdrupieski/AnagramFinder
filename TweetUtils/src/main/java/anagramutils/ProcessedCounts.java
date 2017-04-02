package anagramutils;

import java.sql.Timestamp;
import java.time.Instant;

public class ProcessedCounts {

    private Timestamp now;
    private Timestamp previousReset;
    private double secondsSincePreviousReset;
    private long receivedStatusCountSincePreviousReset;
    private long statusMetFilterCountSincePreviousReset;
    private long tweetMetFilterCountSincePreviousReset;
    private long savedTweetCountSincePreviousReset;
    private long savedAnagramCountSincePreviousReset;

    public ProcessedCounts(
            Instant now,
            Instant previousReset,
            double secondsSincePreviousReset,
            long receivedStatusCountSincePreviousReset,
            long statusMetFilterCountSincePreviousReset,
            long tweetMetFilterCountSincePreviousReset,
            long savedTweetCountSincePreviousReset,
            long savedAnagramCountSincePreviousReset) {
        this.now = java.sql.Timestamp.from(now);
        this.previousReset = java.sql.Timestamp.from(previousReset);
        this.secondsSincePreviousReset = secondsSincePreviousReset;
        this.receivedStatusCountSincePreviousReset = receivedStatusCountSincePreviousReset;
        this.statusMetFilterCountSincePreviousReset = statusMetFilterCountSincePreviousReset;
        this.tweetMetFilterCountSincePreviousReset = tweetMetFilterCountSincePreviousReset;
        this.savedTweetCountSincePreviousReset = savedTweetCountSincePreviousReset;
        this.savedAnagramCountSincePreviousReset = savedAnagramCountSincePreviousReset;
    }

    public Timestamp getNow() {
        return now;
    }

    public Timestamp getPreviousReset() {
        return previousReset;
    }

    public double getSecondsSincePreviousReset() {
        return secondsSincePreviousReset;
    }

    public long getReceivedStatusCountSincePreviousReset() {
        return receivedStatusCountSincePreviousReset;
    }

    public long getStatusMetFilterCountSincePreviousReset() {
        return statusMetFilterCountSincePreviousReset;
    }

    public long getTweetMetFilterCountSincePreviousReset() {
        return tweetMetFilterCountSincePreviousReset;
    }

    public long getSavedTweetCountSincePreviousReset() {
        return savedTweetCountSincePreviousReset;
    }

    public long getSavedAnagramCountSincePreviousReset() {
        return savedAnagramCountSincePreviousReset;
    }
}
