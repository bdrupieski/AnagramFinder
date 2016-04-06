package anagramutils.filters;

import anagramutils.Tweet;

public class TweetFilter {

    private TweetFilter() {
    }

    private static boolean isLongEnough(Tweet tweet) {
        return tweet.getTweetStrippedText().length() > 8;
    }

    public static boolean isGoodTweet(Tweet tweet) {
        return isLongEnough(tweet);
    }
}
