import anagramutils.Tweet;
import anagramutils.processing.ProcessedTweetText;

import java.sql.Timestamp;
import java.util.UUID;

class Util {

    private Util() { }

    static Tweet tweetFromText(String text) {
        ProcessedTweetText processedTweetText = Tweet.processTweetText(text);
        return new Tweet(UUID.randomUUID(), 1, new Timestamp(1), text,
                processedTweetText.getSortedStrippedText(), 1L, "", false);
    }

}
