package tweetstream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

public class PublishedTweetCountLogger {

    private static Logger logger = LoggerFactory.getLogger(PublishedTweetCountLogger.class);

    final private AtomicLong receivedStatusCount = new AtomicLong();
    final private AtomicLong statusMetFilterCount = new AtomicLong();
    final private AtomicLong publishedTweetCount = new AtomicLong();

    final private AtomicLong receivedStatusCountSinceLastReset = new AtomicLong();
    final private AtomicLong statusMetFilterCountSinceLastReset = new AtomicLong();
    final private AtomicLong publishedTweetCountSinceLastReset = new AtomicLong();

    private Instant lastReset = Instant.now();

    public void incrementReceivedStatusCount() {
        receivedStatusCount.incrementAndGet();
        receivedStatusCountSinceLastReset.incrementAndGet();
    }

    public void incrementStatusMetFilterCount() {
        statusMetFilterCount.incrementAndGet();
        statusMetFilterCountSinceLastReset.incrementAndGet();
    }

    public void incrementPublishedTweetCount() {
        long tweetCount = publishedTweetCount.incrementAndGet();
        publishedTweetCountSinceLastReset.incrementAndGet();

        long processedThreshold = 10_000;
        if (tweetCount % processedThreshold == 0) {
            Instant now = Instant.now();

            Duration timeSinceLastReset = Duration.between(lastReset, now);
            long secondsSinceReset = timeSinceLastReset.getSeconds();

            logger.info("Another {} processed. In the previous {}:", processedThreshold, String.format("%d:%02d:%02d",
                    secondsSinceReset / 3600,
                    (secondsSinceReset % 3600) / 60,
                    (secondsSinceReset % 60)));

            double statusesPerSecond = (double)receivedStatusCountSinceLastReset.get() / secondsSinceReset;
            double statusesMetFilterPerSecond = (double)statusMetFilterCountSinceLastReset.get() / secondsSinceReset;
            double publishedPerSecond = (double)publishedTweetCountSinceLastReset.get() / secondsSinceReset;

            logger.info("   {} statuses/second ({} seconds/status)",
                    String.format("%.2f", statusesPerSecond),
                    String.format("%.2f", 1 / statusesPerSecond));
            logger.info("   {} statuses/second met filter ({} seconds/status)",
                    String.format("%.2f", statusesMetFilterPerSecond),
                    String.format("%.2f", 1 / statusesMetFilterPerSecond));
            logger.info("   {} tweets/second published ({} seconds/tweet)",
                    String.format("%.2f", publishedPerSecond),
                    String.format("%.2f", 1 / publishedPerSecond));

            logger.info("Current totals:");
            logger.info("   {} total statuses received.", receivedStatusCount.get());
            logger.info("   {} total statuses met filter.", statusMetFilterCount.get());
            logger.info("   {} total tweets met filter and published.", tweetCount);
            receivedStatusCountSinceLastReset.set(0);
            statusMetFilterCountSinceLastReset.set(0);
            publishedTweetCountSinceLastReset.set(0);
            lastReset = now;
        }
    }
}
