package anagramutils.persistence;

import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import anagramutils.Tweet;

import java.util.List;

@RegisterMapper(TweetMapper.class)
public interface TweetDao {
    @SqlUpdate("insert into public.tweets " +
            "(" +
            "id, " +
            "status_id, " +
            "created_at, " +
            "original_text, " +
            "stripped_sorted_text, " +
            "user_id, " +
            "user_name" +
            ") " +
            "values " +
            "(" +
            ":id, " +
            ":statusId, " +
            ":createdAt, " +
            ":tweetOriginalText, " +
            ":tweetSortedStrippedText, " +
            ":userId, " +
            ":userName" +
            ")")
    void insert(@BindBean Tweet tweet);

    @SqlQuery("select * from tweets where stripped_sorted_text = :tweetSortedStrippedText")
    List<Tweet> findCandidateMatches(@BindBean Tweet tweet);

    void close();
}
