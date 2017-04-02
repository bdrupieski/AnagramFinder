package persistence;

import models.Tweet;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TweetMapper implements ResultSetMapper<Tweet> {
    public Tweet map(int index, ResultSet r, StatementContext ctx) throws SQLException {
        return new Tweet(
                (java.util.UUID)r.getObject("id"),
                r.getLong("status_id"),
                r.getTimestamp("created_at"),
                r.getString("original_text"),
                r.getString("stripped_sorted_text"),
                r.getLong("user_id"),
                r.getString("user_name"),
                r.getBoolean("is_in_retweeted_match")
        );
    }
}
