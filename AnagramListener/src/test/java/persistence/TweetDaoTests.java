package persistence;

import configuration.ApplicationConfiguration;
import configuration.ConfigurationProvider;
import models.Tweet;
import org.junit.Assert;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import java.util.List;

public class TweetDaoTests {

    @Test
    public void when_queryingCandidateTweetsForCommonTweet_then_someInMatchesAndSomeNot() {
        DBI dbi = buildDbi();
        TweetDao tweetDao = dbi.onDemand(TweetDao.class);

        // "well that was a letdown"
        List<Tweet> matchingTweets = tweetDao.findCandidateMatches("aaadeehlllnostttwww");

        Assert.assertTrue(matchingTweets.size() > 0);
        Assert.assertTrue(matchingTweets.stream().filter(x -> x.getIsInRetweetedMatch()).count() > 0);
        Assert.assertTrue(matchingTweets.stream().filter(x -> !x.getIsInRetweetedMatch()).count() > 0);
    }

    private DBI buildDbi() {
        return ConfigurationProvider.configureDatabase(ApplicationConfiguration.FromFileOrResources());
    }
}
