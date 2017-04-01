package anagramlistener;

import anagramutils.filters.StatusFilter;
import anagramutils.filters.TweetFilter;
import anagramutils.persistence.AnagramMatchDao;
import anagramutils.persistence.TweetDao;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import anagramutils.AnagramMatch;
import anagramutils.Tweet;
import anagramutils.filters.AnagramMatchFilter;
import twitter4j.Status;

import java.util.List;
import java.util.stream.Collectors;

public class CandidateStatusProcessor implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(CandidateStatusProcessor.class);

    private Status status;
    private DBI dbi;
    private ProcessedTweetCountLogger countsLogger;

    CandidateStatusProcessor(Status status, DBI dbi, ProcessedTweetCountLogger countsLogger) {
        this.status = status;
        this.dbi = dbi;
        this.countsLogger = countsLogger;
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
        List<Tweet> candidateMatches = tweetDao.findCandidateMatches(tweet);

        List<Tweet> existingDuplicates = candidateMatches
                .stream()
                .filter(x -> x.getTweetStrippedText().equalsIgnoreCase(tweet.getTweetStrippedText()))
                .collect(Collectors.toList());

        if (existingDuplicates.size() > 0) {

            String duplicateTweetOrigText = existingDuplicates.stream().map(Tweet::getTweetOriginalText).collect(Collectors.joining(", "));
            logger.debug("DUPLICATE: {} AND [{}]", tweet.getTweetOriginalText(), duplicateTweetOrigText);

        } else {

            tweetDao = dbi.onDemand(TweetDao.class);
            tweetDao.insert(tweet);
            countsLogger.incrementSavedTweets();

            if (candidateMatches.size() > 0) {

                List<AnagramMatch> goodMatches = candidateMatches
                        .stream()
                        .map(x -> AnagramMatch.fromTweetPair(tweet, x))
                        .filter(AnagramMatchFilter::isGoodMatch)
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
