package configuration;

import com.typesafe.config.Config;

public class AnagramMatchMetricsConfiguration {

    private double editDistanceRatioMinimumThreshold;
    private double longestCommonSubstringRatioMinimumThreshold;
    private double differentWordCountRatioMinimumThreshold;
    private double englishWordsToTotalWordCountRatioMinimumThreshold;
    private double interestingFactorMinimumThreshold;

    public double getEditDistanceRatioMinimumThreshold() {
        return editDistanceRatioMinimumThreshold;
    }

    public double getLongestCommonSubstringRatioMinimumThreshold() {
        return longestCommonSubstringRatioMinimumThreshold;
    }

    public double getDifferentWordCountRatioMinimumThreshold() {
        return differentWordCountRatioMinimumThreshold;
    }

    public double getEnglishWordsToTotalWordCountRatioMinimumThreshold() {
        return englishWordsToTotalWordCountRatioMinimumThreshold;
    }

    public double getInterestingFactorMinimumThreshold() {
        return interestingFactorMinimumThreshold;
    }

    public static AnagramMatchMetricsConfiguration FromFileOrResources() {
        return FromFileOrResources("application.conf");
    }

    private static AnagramMatchMetricsConfiguration FromFileOrResources(String filename) {
        Config config = ConfigurationProvider.loadConfigFromFileIfExistsOtherwiseResources(filename);

        AnagramMatchMetricsConfiguration anagramMatchMetricsConfiguration = new AnagramMatchMetricsConfiguration();
        anagramMatchMetricsConfiguration.editDistanceRatioMinimumThreshold = config.getDouble("thresholds.editdistance");
        anagramMatchMetricsConfiguration.longestCommonSubstringRatioMinimumThreshold = config.getDouble("thresholds.longestcommonsubstring");
        anagramMatchMetricsConfiguration.differentWordCountRatioMinimumThreshold = config.getDouble("thresholds.differentwordcount");
        anagramMatchMetricsConfiguration.englishWordsToTotalWordCountRatioMinimumThreshold = config.getDouble("thresholds.englishwordcount");
        anagramMatchMetricsConfiguration.interestingFactorMinimumThreshold = config.getDouble("thresholds.interestingfactor");

        return anagramMatchMetricsConfiguration;
    }
}
