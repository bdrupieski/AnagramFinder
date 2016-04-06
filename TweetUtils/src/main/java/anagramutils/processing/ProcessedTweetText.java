package anagramutils.processing;

public class ProcessedTweetText {
    private String originalText;
    private String strippedText;
    private String sortedStrippedText;

    public ProcessedTweetText(String originalText, String strippedText, String sortedStrippedText) {
        this.originalText = originalText;
        this.strippedText = strippedText;
        this.sortedStrippedText = sortedStrippedText;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getStrippedText() {
        return strippedText;
    }

    public String getSortedStrippedText() {
        return sortedStrippedText;
    }
}
