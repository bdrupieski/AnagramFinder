package models;

class ProcessedTweetText {
    private String originalText;
    private String strippedText;
    private String sortedStrippedText;

    ProcessedTweetText(String originalText, String strippedText, String sortedStrippedText) {
        this.originalText = originalText;
        this.strippedText = strippedText;
        this.sortedStrippedText = sortedStrippedText;
    }

    String getOriginalText() {
        return originalText;
    }

    String getStrippedText() {
        return strippedText;
    }

    String getSortedStrippedText() {
        return sortedStrippedText;
    }
}
