package anagramlistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

class ProcessedTweetCountLogger {

    private long processedCountThreshold;

    private Logger logger = LoggerFactory.getLogger(ProcessedTweetCountLogger.class);

    final private AtomicLong receivedStatusCount = new AtomicLong();
    final private AtomicLong statusMetFilterCount = new AtomicLong();
    final private AtomicLong tweetMetFilterCount = new AtomicLong();
    final private AtomicLong savedTweetCount = new AtomicLong();
    final private AtomicLong savedAnagramCount = new AtomicLong();

    final private AtomicLong receivedStatusCountSinceLastReset = new AtomicLong();
    final private AtomicLong statusMetFilterCountSinceLastReset = new AtomicLong();
    final private AtomicLong tweetMetFilterCountSinceLastReset = new AtomicLong();
    final private AtomicLong savedTweetCountSinceLastReset = new AtomicLong();
    final private AtomicLong savedAnagramCountSinceLastReset = new AtomicLong();

    private Instant lastReset = Instant.now();

    ProcessedTweetCountLogger(long processedCountThreshold) {
        this.processedCountThreshold = processedCountThreshold;
    }

    void incrementReceivedStatusCount() {
        receivedStatusCount.incrementAndGet();
        receivedStatusCountSinceLastReset.incrementAndGet();
    }

    void incrementStatusMetFilterCount() {
        statusMetFilterCount.incrementAndGet();
        statusMetFilterCountSinceLastReset.incrementAndGet();
    }

    void incrementTweetMetFilterCount() {
        long count = tweetMetFilterCount.incrementAndGet();
        tweetMetFilterCountSinceLastReset.incrementAndGet();

        ReportProgress(count);
    }

    void incrementSavedTweets() {
        savedTweetCount.incrementAndGet();
        savedTweetCountSinceLastReset.incrementAndGet();
    }

    void incrementSavedAnagrams(int num) {
        savedAnagramCount.addAndGet(num);
        savedAnagramCountSinceLastReset.addAndGet(num);
    }

    private void ReportProgress(long tweetsMetFilterCount) {
        if (tweetsMetFilterCount % processedCountThreshold == 0) {
            Instant now = Instant.now();

            Duration timeSinceLastReset = Duration.between(lastReset, now);

            double fractionOfASecond = (double)timeSinceLastReset.getNano() / 1_000_000_000;
            double secondsSinceReset = timeSinceLastReset.getSeconds() + fractionOfASecond;
            double hoursSinceReset = secondsSinceReset / 3600;

            double statusesReceivedPerSecond = receivedStatusCountSinceLastReset.getAndSet(0) / secondsSinceReset;
            double statusesMetFilterPerSecond = statusMetFilterCountSinceLastReset.getAndSet(0) / secondsSinceReset;
            double tweetsMetFilterPerSecond = tweetMetFilterCountSinceLastReset.getAndSet(0) / secondsSinceReset;
            double tweetsSavedPerSecond = savedTweetCountSinceLastReset.getAndSet(0) / secondsSinceReset;
            double anagramsCreatedPerHour = savedAnagramCountSinceLastReset.getAndSet(0) / hoursSinceReset;
            lastReset = now;

            logger.info("Another {} tweets met filter.", processedCountThreshold);
            logger.info("In the previous {}:", formatDuration(timeSinceLastReset));

            logger.info("   {} statuses/second received ({} seconds/status)",
                    String.format("%.2f", statusesReceivedPerSecond),
                    String.format("%.2f", 1 / statusesReceivedPerSecond));
            logger.info("   {} statuses/second met filter ({} seconds/status)",
                    String.format("%.2f", statusesMetFilterPerSecond),
                    String.format("%.2f", 1 / statusesMetFilterPerSecond));
            logger.info("   {} tweets/second met filter ({} seconds/tweet)",
                    String.format("%.2f", tweetsMetFilterPerSecond),
                    String.format("%.2f", 1 / tweetsMetFilterPerSecond));
            logger.info("   {} tweets/second saved (non-duplicates) ({} seconds/tweet)",
                    String.format("%.2f", tweetsSavedPerSecond),
                    String.format("%.2f", 1 / tweetsSavedPerSecond));
            logger.info("   {} anagrams/hour created ({} hours/anagram)",
                    String.format("%.2f", anagramsCreatedPerHour),
                    String.format("%.2f", 1 / anagramsCreatedPerHour));

            logger.info("Current totals:");
            logger.info("   {} total statuses received.", receivedStatusCount.get());
            logger.info("   {} total statuses met filter.", statusMetFilterCount.get());
            logger.info("   {} total tweets met filter.", tweetMetFilterCount.get());
            logger.info("   {} total tweets saved.", savedTweetCount.get());
            logger.info("   {} total anagrams created.", savedAnagramCount.get());
        }
    }

    private String formatDuration(Duration durationSinceLastReset) {
        long seconds = durationSinceLastReset.getSeconds();

        long hoursSinceLastReset = seconds / 3600;
        long minutesWithinHourSinceLastReset = (seconds % 3600) / 60;
        long secondsWithinMinuteSinceLastReset = seconds % 60;

        return String.format("%d:%02d:%02d", hoursSinceLastReset, minutesWithinHourSinceLastReset, secondsWithinMinuteSinceLastReset);
    }
}
