package anagramlistener.configuration;

import com.typesafe.config.Config;

public class ApplicationConfiguration {

    private ApplicationConfiguration() { }

    private String databaseUrl;
    private String databaseUser;
    private String databasePassword;
    private long processedCountThreshold;
    private int numberOfAsyncThreads;

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public long getProcessedCountThreshold() {
        return processedCountThreshold;
    }

    public int getNumberOfAsyncThreads() {
        return numberOfAsyncThreads;
    }

    public static ApplicationConfiguration FromFileOrResources() {
        return FromFileOrResources("application.conf");
    }

    public static ApplicationConfiguration FromFileOrResources(String filename) {
        Config config = ConfigurationProvider.loadConfigFromFileIfExistsOtherwiseResources(filename);

        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
        applicationConfiguration.databaseUrl = config.getString("database.url");
        applicationConfiguration.databaseUser = config.getString("database.user");
        applicationConfiguration.databasePassword = config.getString("database.password");
        applicationConfiguration.processedCountThreshold = config.getLong("processedCountThreshold");
        applicationConfiguration.numberOfAsyncThreads = config.getInt("numberOfAsyncThreads");

        return applicationConfiguration;
    }
}
