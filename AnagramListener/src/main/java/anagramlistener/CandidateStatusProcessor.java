package anagramlistener;

import models.AnagramMatch;
import models.Tweet;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import persistence.AnagramMatchDao;
import persistence.TweetDao;
import textprocessing.AnagramMatchFilter;
import textprocessing.StatusFilter;
import textprocessing.TweetFilter;
import twitter4j.Status;

import java.util.List;
import java.util.stream.Collectors;

public class CandidateStatusProcessor implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(CandidateStatusProcessor.class);

    private final Status status;
    private final DBI dbi;
    private final ProcessedTweetCountLogger countsLogger;
    private final AnagramMatchFilter anagramMatchFilter;

    CandidateStatusProcessor(Status status, DBI dbi, ProcessedTweetCountLogger countsLogger, AnagramMatchFilter anagramMatchFilter) {
        this.status = status;
        this.dbi = dbi;
        this.countsLogger = countsLogger;
        this.anagramMatchFilter = anagramMatchFilter;
    }

    @Override
    public void run() {
        try {
            Tweet goodNewTweet = checkIfStatusIsAnyGoodAndReturnTweetIfItIs(status);
            if (goodNewTweet != null) {
                saveTweetAndCheckIfItAnagramsWithAnyPreviouslySavedTweets(goodNewTweet);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private Tweet checkIfStatusIsAnyGoodAndReturnTweetIfItIs(Status status) {
        Tweet newTweetIfAnyGood = null;

        countsLogger.incrementReceivedStatusCount();
        if (StatusFilter.isGoodStatus(status)) {
            countsLogger.incrementStatusMetFilterCount();

            Tweet tweet = Tweet.fromStatus(status);

            if (TweetFilter.isGoodTweet(tweet)) {
                logger.debug("tweet met filter: {}", tweet.getTweetOriginalText());
                countsLogger.incrementTweetMetFilterCount();
                newTweetIfAnyGood = tweet;
            }
        }

        return newTweetIfAnyGood;
    }

    private void saveTweetAndCheckIfItAnagramsWithAnyPreviouslySavedTweets(Tweet tweet) {
        logger.debug("processing {}", tweet.getTweetSortedStrippedText());

        TweetDao tweetDao = dbi.onDemand(TweetDao.class);
        List<Tweet> candidateMatches = tweetDao.findCandidateMatches(tweet.getTweetSortedStrippedText());

        List<Tweet> existingDuplicates = candidateMatches
                .stream()
                .filter(x -> !x.getIsInRetweetedMatch())
                .filter(x -> x.getTweetStrippedText().equalsIgnoreCase(tweet.getTweetStrippedText()))
                .collect(Collectors.toList());

        if (existingDuplicates.size() > 0) {

            String duplicateTweetsOriginalText = existingDuplicates
                    .stream()
                    .map(x -> String.format("%s (%s)", x.getTweetOriginalText(), x.getId()))
                    .collect(Collectors.joining(", "));
            logger.debug("DUPLICATE: {} AND [{}]", tweet.getTweetOriginalText(), duplicateTweetsOriginalText);

        } else {

            tweetDao = dbi.onDemand(TweetDao.class);
            tweetDao.insert(tweet);
            countsLogger.incrementSavedTweets();

            if (candidateMatches.size() > 0) {

                List<AnagramMatch> goodMatches = candidateMatches
                        .stream()
                        .map(x -> AnagramMatch.fromTweetPair(tweet, x))
                        .filter(anagramMatchFilter::isGoodMatch)
                        .collect(Collectors.toList());

                if (goodMatches.size() > 0) {

                    String interestingFactors = goodMatches
                            .stream()
                            .map(x -> Float.toString(x.getInterestingFactor()))
                            .collect(Collectors.joining(", "));
                    logger.info("MATCH: {}. IFs: {}.", tweet.getTweetOriginalText(), interestingFactors);

                    AnagramMatchDao anagramMatchDao = dbi.onDemand(AnagramMatchDao.class);
                    anagramMatchDao.insert(goodMatches);

                    countsLogger.incrementSavedAnagrams(goodMatches.size());
                }
            }
        }
    }
}
