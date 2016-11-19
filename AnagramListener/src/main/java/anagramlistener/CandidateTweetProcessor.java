package anagramlistener;

import anagramutils.persistence.AnagramMatchDao;
import anagramutils.persistence.TweetDao;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import anagramutils.AnagramMatch;
import anagramutils.Tweet;
import anagramutils.filters.AnagramMatchFilter;

import java.util.List;
import java.util.stream.Collectors;

public class CandidateTweetProcessor implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(CandidateTweetProcessor.class);

    private Tweet newTweet;
    private DBI dbi;
    private ProcessedTweetCountLogger counts;

    public CandidateTweetProcessor(Tweet newTweet, DBI dbi, ProcessedTweetCountLogger counts) {
        this.newTweet = newTweet;
        this.dbi = dbi;
        this.counts = counts;
    }

    @Override
    public void run() {
        try {
            work();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void work() {
        logger.debug("processing {}", newTweet.getTweetSortedStrippedText());
        counts.incrementProcessed();

        TweetDao tweetDao = dbi.onDemand(TweetDao.class);
        List<Tweet> candidateMatches = tweetDao.findCandidateMatches(newTweet);

        List<Tweet> existingDuplicates = candidateMatches
                .stream()
                .filter(x -> x.getTweetStrippedText().equalsIgnoreCase(newTweet.getTweetStrippedText()))
                .collect(Collectors.toList());

        if (existingDuplicates.size() > 0) {

            String duplicateTweetOrigText = existingDuplicates.stream().map(Tweet::getTweetOriginalText).collect(Collectors.joining(", "));
            logger.debug("DUPLICATE: {} AND [{}]", newTweet.getTweetOriginalText(), duplicateTweetOrigText);

        } else {

            tweetDao = dbi.onDemand(TweetDao.class);
            tweetDao.insert(newTweet);
            counts.incrementSavedTweets();

            if (candidateMatches.size() > 0) {

                List<AnagramMatch> goodMatches = candidateMatches
                        .stream()
                        .map(x -> AnagramMatch.fromTweetPair(newTweet, x))
                        .filter(AnagramMatchFilter::isGoodMatch)
                        .collect(Collectors.toList());

                if (goodMatches.size() > 0) {

                    String interestingFactors = goodMatches
                            .stream()
                            .map(x -> Float.toString(x.getInterestingFactor()))
                            .collect(Collectors.joining(", "));
                    logger.info("MATCH: {}. IFs: {}.", newTweet.getTweetOriginalText(), interestingFactors);

                    AnagramMatchDao anagramMatchDao = dbi.onDemand(AnagramMatchDao.class);
                    anagramMatchDao.insert(goodMatches);

                    counts.incrementSavedAnagrams(goodMatches.size());
                }
            }
        }
    }
}
