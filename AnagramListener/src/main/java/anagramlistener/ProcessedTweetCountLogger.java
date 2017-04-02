package anagramlistener;

import anagramutils.ProcessedCounts;
import anagramutils.persistence.ProcessedCountsDao;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

class ProcessedTweetCountLogger {

    private DBI dbi;
    private long processedCountThreshold;

    private Logger logger = LoggerFactory.getLogger(ProcessedTweetCountLogger.class);

    final private AtomicLong receivedStatusCount = new AtomicLong();
    final private AtomicLong statusMetFilterCount = new AtomicLong();
    final private AtomicLong tweetMetFilterCount = new AtomicLong();
    final private AtomicLong savedTweetCount = new AtomicLong();
    final private AtomicLong savedAnagramCount = new AtomicLong();

    final private AtomicLong receivedStatusCountSincePreviousReset = new AtomicLong();
    final private AtomicLong statusMetFilterCountSincePreviousReset = new AtomicLong();
    final private AtomicLong tweetMetFilterCountSincePreviousReset = new AtomicLong();
    final private AtomicLong savedTweetCountSincePreviousReset = new AtomicLong();
    final private AtomicLong savedAnagramCountSincePreviousReset = new AtomicLong();

    private Instant previousReset = Instant.now();

    ProcessedTweetCountLogger(DBI dbi, long processedCountThreshold) {
        this.dbi = dbi;
        this.processedCountThreshold = processedCountThreshold;
    }

    void incrementReceivedStatusCount() {
        receivedStatusCount.incrementAndGet();
        receivedStatusCountSincePreviousReset.incrementAndGet();
    }

    void incrementStatusMetFilterCount() {
        statusMetFilterCount.incrementAndGet();
        statusMetFilterCountSincePreviousReset.incrementAndGet();
    }

    void incrementTweetMetFilterCount() {
        long count = tweetMetFilterCount.incrementAndGet();
        tweetMetFilterCountSincePreviousReset.incrementAndGet();

        automaticallyLogProgress(count);
    }

    void incrementSavedTweets() {
        savedTweetCount.incrementAndGet();
        savedTweetCountSincePreviousReset.incrementAndGet();
    }

    void incrementSavedAnagrams(int num) {
        savedAnagramCount.addAndGet(num);
        savedAnagramCountSincePreviousReset.addAndGet(num);
    }

    private boolean isItTimeToAutomaticallyLogProgress(long tweetsMetFilterCount) {
        return (tweetsMetFilterCount % processedCountThreshold) == 0;
    }

    private void automaticallyLogProgress(long tweetsMetFilterCount) {
        if (isItTimeToAutomaticallyLogProgress(tweetsMetFilterCount)) {
            logProcessedCounts();
        }
    }

    void logProcessedCounts() {
        Instant now = Instant.now();
        Duration timeSincePreviousReset = Duration.between(previousReset, now);

        double fractionOfASecond = (double) timeSincePreviousReset.getNano() / 1_000_000_000;
        double secondsSincePreviousReset = timeSincePreviousReset.getSeconds() + fractionOfASecond;
        double hoursSincePreviousReset = secondsSincePreviousReset / 3600;

        long statusesReceived = receivedStatusCountSincePreviousReset.getAndSet(0);
        long statusesMetFilter = statusMetFilterCountSincePreviousReset.getAndSet(0);
        long tweetsMetFilter = tweetMetFilterCountSincePreviousReset.getAndSet(0);
        long savedTweets = savedTweetCountSincePreviousReset.getAndSet(0);
        long savedAnagrams = savedAnagramCountSincePreviousReset.getAndSet(0);

        ProcessedCountsDao processedCountsDao = dbi.onDemand(ProcessedCountsDao.class);
        ProcessedCounts processedCounts = new ProcessedCounts(now, previousReset,
                secondsSincePreviousReset,
                statusesReceived,
                statusesMetFilter,
                tweetsMetFilter,
                savedTweets,
                savedAnagrams);
        processedCountsDao.insert(processedCounts);

        double statusesReceivedPerSecond = statusesReceived / secondsSincePreviousReset;
        double statusesMetFilterPerSecond = statusesMetFilter / secondsSincePreviousReset;
        double tweetsMetFilterPerSecond = tweetsMetFilter / secondsSincePreviousReset;
        double tweetsSavedPerSecond = savedTweets / secondsSincePreviousReset;
        double anagramsCreatedPerHour = savedAnagrams / hoursSincePreviousReset;

        previousReset = now;

        logger.info("{} tweets met filter since previous reset.", tweetsMetFilter);
        logger.info("In the previous {}:", formatDuration(timeSincePreviousReset));

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

    private String formatDuration(Duration durationSincePreviousReset) {
        long seconds = durationSincePreviousReset.getSeconds();

        long hoursSincePreviousReset = seconds / 3600;
        long minutesWithinHourSincePreviousReset = (seconds % 3600) / 60;
        long secondsWithinMinuteSincePreviousReset = seconds % 60;

        return String.format("%d:%02d:%02d", hoursSincePreviousReset, minutesWithinHourSincePreviousReset, secondsWithinMinuteSincePreviousReset);
    }
}
