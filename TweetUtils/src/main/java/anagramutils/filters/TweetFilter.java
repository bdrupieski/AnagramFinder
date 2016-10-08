package anagramutils.filters;

import anagramutils.Tweet;

public class TweetFilter {

    private TweetFilter() {
    }

    private static boolean isLongEnough(Tweet tweet) {
        return tweet.getTweetStrippedText().length() > 8;
    }

    private static boolean isNotTooLong(Tweet tweet) {
        return tweet.getTweetStrippedText().length() <= 30;
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
