package persistence;

import configuration.ApplicationConfiguration;
import configuration.ConfigurationProvider;
import models.ProcessedCounts;
import org.junit.Ignore;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import java.time.Instant;

public class ProcessedCountsDaoTests {

    // Ad-hoc integration test. Unignore to run this, then delete the row it creates.
    @Ignore
    @Test
    public void canSuccessfullyInsertProcessedCounts() {

        DBI dbi = buildDbi();
        ProcessedCountsDao processedCountsDao = dbi.onDemand(ProcessedCountsDao.class);

        Instant now = Instant.now();
        Instant previousReset = Instant.now();

        ProcessedCounts processedCounts = new ProcessedCounts(
                now,
                previousReset,
                -12.4,
                -1,
                -2,
                -3,
                -4,
                -5);

        processedCountsDao.insert(processedCounts);
    }

    private DBI buildDbi() {
        return ConfigurationProvider.configureDatabase(ApplicationConfiguration.FromFileOrResources());
    }
}
