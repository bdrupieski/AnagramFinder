package anagramutils.processing;

import anagramutils.IsSameWhenRearrangedEnum;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MatchMetrics {

    private static HashSet<String> englishWords;

    static {
        ClassLoader classLoader = MatchMetrics.class.getClassLoader();

        try {
            InputStream resourceAsStream = classLoader.getResourceAsStream("english_words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream));
            englishWords = new HashSet<>();
            String line;
            while ((line = reader.readLine()) != null) {
                englishWords.add(line);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private MatchMetrics() {
    }

    public static int demerauLevenshteinDistance(String a, String b) {
        String aUpper = a.toUpperCase();
        String bUpper = b.toUpperCase();

        int aLength = aUpper.length();
        int bLength = bUpper.length();

        int[][] matrix = new int[aLength + 1][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = new int[bLength + 1];
        }

        for (int i = 0; i <= aLength; i++) {
            matrix[i][0] = i;
        }

        for (int j = 0; j <= bLength; j++) {
            matrix[0][j] = j;
        }

        for (int i = 1; i <= aLength; i++) {
            for (int j = 1; j <= bLength; j++) {
                int cost;

                if (bUpper.charAt(j - 1) == aUpper.charAt(i - 1)) {
                    cost = 0;
                } else {
                    cost = 1;
                }

                matrix[i][j] = Math.min(matrix[i - 1][j    ] + 1,
                               Math.min(matrix[i    ][j - 1] + 1,
                                        matrix[i - 1][j - 1] + cost));

                if (i > 1 && j > 1 && aUpper.charAt(i - 1) == bUpper.charAt(j - 2) && aUpper.charAt(i - 2) == bUpper.charAt(j - 1)) {
                    matrix[i][j] = Math.min(matrix[i    ][j    ],
                                            matrix[i - 2][j - 2] + cost);
                }
            }
        }


        return matrix[aLength][bLength];
    }

    public static int hammingDistance(String a, String b) {

        int distance = 0;

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                distance++;
            }
        }

        return distance;
    }

    public static int longestCommonSubstring(String a, String b) {

        int[][] matrix = new int[a.length()][];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = new int[b.length()];
        }
        int maxLength = 0;

        for (int i = 0; i < a.length(); i++) {
            for (int j = 0; j < b.length(); j++) {
                if (a.charAt(i) != b.charAt(j)) {
                    matrix[i][j] = 0;
                } else {
                    if (i == 0 || j == 0) {
                        matrix[i][j] = 1;
                    } else {
                        matrix[i][j] = 1 + matrix[i - 1][j - 1];
                    }

                    if (matrix[i][j] > maxLength) {
                        maxLength = matrix[i][j];
                    }
                }
            }
        }

        return maxLength;
    }

    private static String[] tokenizeTweetText(String originalText) {
        String formatted = originalText.toLowerCase().replaceAll("'", "").replaceAll("[^a-z0-9 ]+", " ");
        return formatted.trim().split("\\s+");
    }

    private static Map<String, Integer> getWordCount(String originalText) {
        String[] words = tokenizeTweetText(originalText);
        return Arrays.stream(words).collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(x -> 1)));
    }

    public static WordCountDifference getWordCountDifference(String a, String b) {

        Map<String, Integer> tweet1Counts = getWordCount(a);
        Map<String, Integer> tweet2Counts = getWordCount(b);

        Set<String> allWords = new HashSet<>(tweet1Counts.keySet());
        allWords.addAll(tweet2Counts.keySet());

        int wordDifferenceCount = 0;

        for (String word : allWords) {
            int countInTweet1 = tweet1Counts.getOrDefault(word, 0);
            int countInTweet2 = tweet2Counts.getOrDefault(word, 0);

            wordDifferenceCount += Math.abs(countInTweet1 - countInTweet2);
        }

        int totalWords =
                tweet1Counts.values().stream().mapToInt(Integer::intValue).sum() +
                tweet2Counts.values().stream().mapToInt(Integer::intValue).sum();

        return new WordCountDifference(wordDifferenceCount, totalWords);
    }

    public static class WordCountDifference {
        private int wordCountDifference;
        private int totalWords;

        WordCountDifference(int wordCountDifference, int totalWords) {
            this.wordCountDifference = wordCountDifference;
            this.totalWords = totalWords;
        }

        public int getWordCountDifference() {
            return wordCountDifference;
        }

        public int getTotalWords() {
            return totalWords;
        }
    }

    private static Set<String> permutations(String[] arr) {
        Set<String> perms = new HashSet<>();

        permutations(arr, 0, perms);

        return perms;
    }

    private static void permutations(String[] arr, int k, Set<String> perms) {
        for (int i = k; i < arr.length; i++) {
            swap(arr, i, k);
            permutations(arr, k + 1, perms);
            swap(arr, k, i);
        }
        if (k == arr.length - 1) {
            perms.add(String.join("", arr));
        }
    }

    private static void swap(String[] arr, int aIndex, int bIndex) {
        String temp = arr[aIndex];
        arr[aIndex] = arr[bIndex];
        arr[bIndex] = temp;
    }

    public static IsSameWhenRearrangedEnum isSameWhenWordsRearranged(String a, String b) {

        String[] s1Words = tokenizeTweetText(a);
        String[] s2Words = tokenizeTweetText(b);

        // all permutations of 6 elements is 720 items.
        // 7 elements is 5,040. That's too much.
        if (s1Words.length >= 7 || s2Words.length >= 7) {
            return IsSameWhenRearrangedEnum.TOO_LONG_TO_COMPUTE;
        } else {
            Set<String> s1Permutations = permutations(s1Words);
            Set<String> s2Permutations = permutations(s2Words);

            int s1PermCount = s1Permutations.size();
            int s2PermCount = s2Permutations.size();
            int totalPermutationsIfNoOverlap = s1PermCount + s2PermCount;

            s1Permutations.addAll(s2Permutations);

            if (s1Permutations.size() != totalPermutationsIfNoOverlap) {
                return IsSameWhenRearrangedEnum.TRUE;
            } else {
                return IsSameWhenRearrangedEnum.FALSE;
            }
        }
    }

    public static int numberOfEnglishWords(String a) {
        String[] words = tokenizeTweetText(a);

        int count = 0;

        for (String word : words) {
            if (englishWords.contains(word.toLowerCase())) {
                count++;
            }
        }

        return count;
    }
}
