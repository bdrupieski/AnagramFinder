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
            long secondsSinceReset = timeSinceLastReset.getSeconds();
            double hoursSinceReset = secondsSinceReset / (double) 3600;

            logger.info("Another {} tweets met filter.", processedCountThreshold);

            String formattedTimespan = String.format("%d:%02d:%02d", secondsSinceReset / 3600, (secondsSinceReset % 3600) / 60, (secondsSinceReset % 60));
            logger.info("In the previous {}:", formattedTimespan);

            double statusesReceivedPerSecond = (double) receivedStatusCountSinceLastReset.get() / secondsSinceReset;
            double statusesMetFilterPerSecond = (double) statusMetFilterCountSinceLastReset.get() / secondsSinceReset;
            double tweetsMetFilterPerSecond = (double) tweetsMetFilterCount / secondsSinceReset;
            double tweetsSavedPerSecond = (double) savedTweetCountSinceLastReset.get() / secondsSinceReset;
            double anagramsCreatedPerHour = (double) savedAnagramCountSinceLastReset.get() / hoursSinceReset;

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
            logger.info("   {} total tweets met filter.", tweetsMetFilterCount);
            logger.info("   {} total tweets saved.", savedTweetCount.get());
            logger.info("   {} total anagrams created.", savedAnagramCount.get());
            receivedStatusCountSinceLastReset.set(0);
            statusMetFilterCountSinceLastReset.set(0);
            tweetMetFilterCountSinceLastReset.set(0);
            savedTweetCountSinceLastReset.set(0);
            savedAnagramCountSinceLastReset.set(0);
            lastReset = now;
        }
    }
}
