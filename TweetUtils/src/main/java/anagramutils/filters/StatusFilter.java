package anagramutils.filters;

import twitter4j.Status;

public class StatusFilter {

    private StatusFilter() {
    }

    private static boolean isNotARetweet(Status status) {
        return !status.isRetweet();
    }

    private static boolean isEnglish(Status status) {
        return status.getLang().equals("en");
    }

    private static boolean containsNoUrls(Status status) {
        return status.getURLEntities().length == 0 &&
                status.getMediaEntities().length == 0 &&
                status.getExtendedMediaEntities().length == 0;
    }

    private static boolean hasNoHashtag(Status status) {
        return status.getHashtagEntities().length == 0;
    }

    private static boolean containsNoMentions(Status status) {
        return status.getUserMentionEntities().length == 0;
    }

    private static boolean isNotWeatherUpdateBot(Status status) {
        return !status.getText().startsWith("Get Weather Updates");
    }

    public static boolean isGoodStatus(Status status) {
        return isNotARetweet(status) &&
                isEnglish(status) &&
                containsNoUrls(status) &&
                hasNoHashtag(status) &&
                containsNoMentions(status) &&
                isNotWeatherUpdateBot(status);
    }
}
