package anagramlistener;

import java.util.concurrent.atomic.AtomicLong;

public class ProcessedTweetCounts {
    final private AtomicLong processedTweetCount = new AtomicLong();
    final private AtomicLong savedTweetCount = new AtomicLong();
    final private AtomicLong savedAnagramCount = new AtomicLong();

    public void incrementProcessed() {
        processedTweetCount.incrementAndGet();
    }

    public void incrementSavedTweets() {
        savedTweetCount.incrementAndGet();
    }

    public void incrementSavedAnagrams(int num) {
        savedAnagramCount.addAndGet(num);
    }

    public long getProcessedTweetCount() {
        return processedTweetCount.get();
    }

    public long getSavedTweetCount() {
        return savedTweetCount.get();
    }

    public long getSavedAnagramCount() {
        return savedAnagramCount.get();
    }
}
