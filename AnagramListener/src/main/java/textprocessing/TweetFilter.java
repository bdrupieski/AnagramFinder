package textprocessing;

import models.Tweet;

public class TweetFilter {

    static final int TWEET_MIN_LENGTH_EXCLUSIVE = 8;
    static final int TWEET_MAX_LENGTH_INCLUSIVE = 30;

    private TweetFilter() {
    }

    private static boolean isLongEnough(Tweet tweet) {
        return tweet.getTweetStrippedText().length() > TWEET_MIN_LENGTH_EXCLUSIVE;
    }

    private static boolean isNotTooLong(Tweet tweet) {
        return tweet.getTweetStrippedText().length() <= TWEET_MAX_LENGTH_INCLUSIVE;
    }

    private static boolean doesNotContainNumber(Tweet tweet) {
        return !tweet.getTweetSortedStrippedText().matches(".*[0-9].*");
    }

    public static boolean isGoodTweet(Tweet tweet) {
        return isLongEnough(tweet) &&
                isNotTooLong(tweet) &&
                doesNotContainNumber(tweet);
    }
}
