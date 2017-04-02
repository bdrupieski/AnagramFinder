package models;

import twitter4j.Status;

import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Pattern;

public class Tweet {

    private static Pattern alphanumericRegex = Pattern.compile("[^a-z0-9]");

    private UUID id;
    private long statusId;
    private Timestamp createdAt;
    private String tweetOriginalText;
    private String tweetStrippedText;
    private String tweetSortedStrippedText;
    private Long userId;
    private String userName;
    private boolean isInRetweetedMatch;

    public Tweet(UUID id, long statusId, Timestamp createdAt, String tweetOriginalText, String tweetStrippedText, String tweetSortedStrippedText, Long userId, String userName, boolean isInRetweetedMatch) {
        this.id = id;
        this.statusId = statusId;
        this.createdAt = createdAt;
        this.tweetOriginalText = tweetOriginalText;
        this.tweetStrippedText = tweetStrippedText;
        this.tweetSortedStrippedText = tweetSortedStrippedText;
        this.userId = userId;
        this.userName = userName;
        this.isInRetweetedMatch = isInRetweetedMatch;
    }

    // This constructor should only be used by TweetMapper so
    // that when retrieving tweets from the database we can
    // re-hydrate the tweetStrippedText property and don't
    // need to persist it in the database.
    public Tweet(UUID id, long statusId, Timestamp createdAt, String tweetOriginalText, String tweetSortedStrippedText, Long userId, String userName, boolean isInRetweetedMatch) {
        this(id, statusId, createdAt, tweetOriginalText, stripText(tweetOriginalText), tweetSortedStrippedText, userId, userName, isInRetweetedMatch);
    }

    public static Tweet fromStatus(Status status) {
        ProcessedTweetText processedTweetText = processTweetText(status.getText());

        return new Tweet(UUID.randomUUID(), status.getId(), new java.sql.Timestamp(status.getCreatedAt().getTime()),
                processedTweetText.getOriginalText(), processedTweetText.getStrippedText(),
                processedTweetText.getSortedStrippedText(), status.getUser().getId(),
                status.getUser().getScreenName(), false);
    }

    public static Tweet fromText(String text) {
        ProcessedTweetText processedTweetText = Tweet.processTweetText(text);
        return new Tweet(null, 0L, null, text, processedTweetText.getStrippedText(),
                processedTweetText.getSortedStrippedText(), 0L, null, false);
    }

    private static String stripText(String originalText) {
        String cleaned = Normalizer.normalize(originalText.trim().toLowerCase(), Normalizer.Form.NFD);
        String transformed = cleaned.replaceAll("ß", "ss").replaceAll("ø", "o");
        return alphanumericRegex.matcher(transformed).replaceAll("");
    }

    private static ProcessedTweetText processTweetText(String originalText) {
        String strippedText = stripText(originalText);

        char[] chars = strippedText.toCharArray();
        Arrays.sort(chars);
        String sortedStrippedText = new String(chars);

        return new ProcessedTweetText(originalText, strippedText, sortedStrippedText);
    }

    public UUID getId() {
        return id;
    }

    public long getStatusId() {
        return statusId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getTweetOriginalText() {
        return tweetOriginalText;
    }

    public String getTweetStrippedText() {
        return tweetStrippedText;
    }

    public String getTweetSortedStrippedText() {
        return tweetSortedStrippedText;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean getIsInRetweetedMatch() {
        return isInRetweetedMatch;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", statusId=" + statusId +
                ", createdAt=" + createdAt +
                ", tweetOriginalText='" + tweetOriginalText + '\'' +
                ", tweetStrippedText='" + tweetStrippedText + '\'' +
                ", tweetSortedStrippedText='" + tweetSortedStrippedText + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", isInRetweetedMatch=" + isInRetweetedMatch +
                '}';
    }
}
