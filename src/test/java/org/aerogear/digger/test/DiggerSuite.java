package org.aerogear.digger.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;

import javax.inject.Inject;


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
    @BeforeSuite(alwaysRun = true, dependsOnMethods = "cleanUp")
    public void prepareSuite(ITestContext context) throws Exception {
        LOG.info("Initializing Digger test suite... Preparing the suite");

        // TODO test connectivity to $host

        this.diggerTestingEnv.initialize();
        this.diggerTestDataProvider.initialize();
    }

    // @BeforeSuite is only run once, regardless of what class it belongs to.
    @BeforeSuite(alwaysRun = true)
    public void cleanUp(ITestContext context) throws Exception {
        LOG.info("Cleaning up");

        // TODO get list of apps from jenkins and remove all with $prefix
    }

}
