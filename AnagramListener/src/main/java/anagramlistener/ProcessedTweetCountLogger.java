package anagramlistener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessedTweetCountLogger {

    private Logger logger = LoggerFactory.getLogger(ProcessedTweetCountLogger.class);

    final private AtomicLong processedTweetCount = new AtomicLong();
    final private AtomicLong savedTweetCount = new AtomicLong();
    final private AtomicLong savedAnagramCount = new AtomicLong();

    final private AtomicLong processedTweetCountSinceLastReset = new AtomicLong();
    final private AtomicLong savedTweetCountSinceLastReset = new AtomicLong();
    final private AtomicLong savedAnagramCountSinceLastReset = new AtomicLong();

    private Instant lastReset = Instant.now();

    public void incrementProcessed() {
        long processedCount = processedTweetCount.incrementAndGet();
        processedTweetCountSinceLastReset.incrementAndGet();

        long processedThreshold = 10_000;
        if (processedCount % processedThreshold == 0) {
            Instant now = Instant.now();

            Duration timeSinceLastReset = Duration.between(lastReset, now);
            long secondsSinceReset = timeSinceLastReset.getSeconds();
            double hoursSinceReset = secondsSinceReset / (double)3600;

            logger.info("Another {} processed. In the previous {}:", processedThreshold, String.format("%d:%02d:%02d",
                    secondsSinceReset / 3600,
                    (secondsSinceReset % 3600) / 60,
                    (secondsSinceReset % 60)));

            double processedPerSecond = (double)processedTweetCountSinceLastReset.get() / secondsSinceReset;
            double tweetsSavedPerSecond = (double)savedTweetCountSinceLastReset.get() / secondsSinceReset;
            double anagramsCreatedPerHour = (double)savedAnagramCountSinceLastReset.get() / hoursSinceReset;

            logger.info("   {} tweets/second processed ({} seconds/tweet)",
                    String.format("%.2f", processedPerSecond),
                    String.format("%.2f", 1 / processedPerSecond));
            logger.info("   {} tweets/second saved (non-duplicates) ({} seconds/tweet)",
                    String.format("%.2f", tweetsSavedPerSecond),
                    String.format("%.2f", 1 / tweetsSavedPerSecond));
            logger.info("   {} anagrams/hour created ({} hours/anagram)",
                    String.format("%.2f", anagramsCreatedPerHour),
                    String.format("%.2f", 1 / anagramsCreatedPerHour));

            logger.info("Current totals:");
            logger.info("   {} total tweets processed.", processedCount);
            logger.info("   {} total tweets saved.", savedTweetCount.get());
            logger.info("   {} total anagrams created.", savedAnagramCount);
            processedTweetCountSinceLastReset.set(0);
            savedTweetCountSinceLastReset.set(0);
            savedAnagramCountSinceLastReset.set(0);
            lastReset = now;
        }
    }

    public void incrementSavedTweets() {
        savedTweetCount.incrementAndGet();
        savedTweetCountSinceLastReset.incrementAndGet();
    }

    public void incrementSavedAnagrams(int num) {
        savedAnagramCount.addAndGet(num);
        savedAnagramCountSinceLastReset.addAndGet(num);
    }
}
