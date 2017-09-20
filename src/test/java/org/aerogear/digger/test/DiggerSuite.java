package org.aerogear.digger.test;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;
import org.aerogear.digger.client.DiggerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;

import javax.inject.Inject;
import java.util.Map;


/**
 * This is a base for the tests. Test classes should extend this class
 * to make sure parameters are read and data provider is initialized.
 * <p>
 * Although this class doesn't provide any methods to be used by the test
 * classes, unfortunately there is no other way in TestNG to make sure
 * {@link BeforeSuite} methods are triggered.
 */
public abstract class DiggerSuite {

    private static final Logger LOG = LoggerFactory.getLogger(DiggerSuite.class);

    @Inject
    private DiggerTestDataProvider diggerTestDataProvider;

    @Inject
    private DiggerTestingEnv diggerTestingEnv;

    // @BeforeSuite is only run once, regardless of what class it belongs to.
    @BeforeSuite(alwaysRun = true)
    public void initialize(ITestContext context) throws Exception {
        LOG.info("Initializing Digger test suite");

        // TODO test connectivity to $host

        this.diggerTestingEnv.initialize();
    }

    // @BeforeSuite is only run once, regardless of what class it belongs to.
    @BeforeSuite(alwaysRun = true, dependsOnMethods = "initialize")
    public void cleanUp(ITestContext context) throws Exception {
        LOG.info("Cleaning up");

        final DiggerClient diggerClient = DiggerClient.createDefaultWithAuth(
                this.diggerTestingEnv.getDiggerTargetUrl(),
                this.diggerTestingEnv.getDiggerUsername(),
                this.diggerTestingEnv.getDiggerPassword()
        );

        final JenkinsServer jenkinsServer = diggerClient.getJenkinsServer();

        // get list of apps from jenkins and remove all with $prefix
        final Map<String, Job> jobs = jenkinsServer.getJobs();
        for (String jobKey : jobs.keySet()) {
            if (jobKey.startsWith(this.diggerTestingEnv.getPrefix())) {
                LOG.debug("Deleting job that matches the prefix. Job key: {}", jobKey);
                jenkinsServer.deleteJob(jobKey);
            }
        }

    }

    // @BeforeSuite is only run once, regardless of what class it belongs to.
    @BeforeSuite(alwaysRun = true, dependsOnMethods = "cleanUp")
    public void prepareSuite(ITestContext context) throws Exception {
        LOG.info("Preparing the suite");

        this.diggerTestDataProvider.initialize();
    }

}
