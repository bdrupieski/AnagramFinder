import anagramlistener.configuration.ApplicationConfiguration;
import anagramlistener.configuration.ConfigurationProvider;
import anagramutils.models.Tweet;
import anagramutils.persistence.TweetDao;
import org.junit.Assert;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import java.util.List;

public class TweetDaoTests {

    @Test
    public void sameStringHasADistanceOfZero() {
        DBI dbi = buildDbi();
        TweetDao tweetDao = dbi.onDemand(TweetDao.class);

        List<Tweet> matchingTweets = tweetDao.findCandidateMatches("aaaaaaceeeggiikmmnrrt");

        Assert.assertTrue(matchingTweets.size() > 0);
        Assert.assertTrue(matchingTweets.stream().filter(x -> x.getIsInRetweetedMatch()).count() > 0);
        Assert.assertTrue(matchingTweets.stream().filter(x -> !x.getIsInRetweetedMatch()).count() > 0);
    }

    private DBI buildDbi() {
        return ConfigurationProvider.configureDatabase(ApplicationConfiguration.FromFileOrResources());
    }
}
