import anagramlistener.configuration.ApplicationConfiguration;
import anagramlistener.configuration.ConfigurationProvider;
import anagramutils.ProcessedCounts;
import anagramutils.persistence.ProcessedCountsDao;
import org.junit.Ignore;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import java.time.Instant;

public class ProcessedCountsDaoTests {

    @Ignore
    @Test
    public void insertProcessedCounts() {

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
