package anagramlistener;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class AnagramMatchingStatusListener implements StatusListener {

    private static Logger logger = LoggerFactory.getLogger(AnagramMatchingStatusListener.class);

    private ProcessedTweetCountLogger processedTweetCountLogger;
    private DBI dbi;

    AnagramMatchingStatusListener(DBI dbi, long processedCountThreshold) {
        this.dbi = dbi;
        this.processedTweetCountLogger = new ProcessedTweetCountLogger(processedCountThreshold);
    }

    @Override
    public void onStatus(Status status) {
        CandidateStatusProcessor candidateTweetProcessor = new CandidateStatusProcessor(status, dbi, processedTweetCountLogger);
        candidateTweetProcessor.run();
    }

    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        logger.error("status deleted: {} User: {}", statusDeletionNotice.getStatusId(), statusDeletionNotice.getUserId());
    }

    @Override
    public void onTrackLimitationNotice(int i) {
        logger.error("track limitation: {}", i);
    }

    @Override
    public void onScrubGeo(long l, long l1) {
        logger.error("geo scrub l: {} l1: {}", l, l1);
    }

    @Override
    public void onStallWarning(StallWarning stallWarning) {
        logger.error("stall warning: {}", stallWarning.getMessage());
    }

    @Override
    public void onException(Exception e) {
        logger.error("exception: {}", e.getMessage());
    }
}
