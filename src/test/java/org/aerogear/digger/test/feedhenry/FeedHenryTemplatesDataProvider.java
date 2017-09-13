package org.aerogear.digger.test.feedhenry;

import org.aerogear.digger.test.DiggerTestDataProvider;
import org.aerogear.digger.test.DiggerTestingEnv;
import org.aerogear.digger.test.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

public class FeedHenryTemplatesDataProvider implements DiggerTestDataProvider {

    private static final Logger LOG = LoggerFactory.getLogger(FeedHenryTemplatesDataProvider.class);

    // don't inject a FeedHenryTemplatesDiggerTestingEnv as that will create a new instance inject it.
    // the binding definition should be for DiggerTestingEnv
    @Inject
    private DiggerTestingEnv diggerTestingEnv;

    private Object[][] androidTemplates;
    private Object[][] iosTemplates;
    private Object[][] cordovaTemplates;

    // keep this no-arg constructor for Guice and TestNG.
    public FeedHenryTemplatesDataProvider() {
    }

    @Override
    public void initialize() {
        final FeedHenryTemplatesDiggerTestingEnv testingEnv = (FeedHenryTemplatesDiggerTestingEnv) this.diggerTestingEnv;

        LOG.debug("Initializing FeedHenryTemplatesDataProvider");
        LOG.debug("Fetching templates from FeedHenry template apps repo");
        LOG.debug("Repo: {}, branch: {}", testingEnv.fhtaRepoUrl, testingEnv.fhtaRepoBranch);
        LOG.info("Templates will be based on {}:{}:/global.json", testingEnv.fhtaRepoUrl, testingEnv.fhtaRepoBranch);

        System.out.println(this.diggerTestingEnv);

        // TODO fetch templates from $fhtaRepoUrl / $fhtaRepoBranch
        // TODO parse templates from global.json file and sort them based on type into androidTemplates/iosTemplates/cordovaTemplates

        androidTemplates = new Object[][]{
                {new Template("sync_android_app", "Sync Android App", "git://github.com/feedhenry-templates/sync-android-app.git", "master")}, // temporary
                {new Template("saml_android", "SAML Android", "git://github.com/feedhenry-templates/saml-android-app.git", "master")} // temporary
        };
        iosTemplates = new Object[][]{
                {new Template("sync_ios_objectivec_app", "Sync iOS (Objective-C) App", "git://github.com/feedhenry-templates/sync-ios-app.git", "master")} // temporary
        };
        cordovaTemplates = new Object[][]{
                {new Template("sync_app", "Sync App", "git://github.com/feedhenry-templates/sync-cordova-app.git", "master")} // temporary
        };
    }

    @Override
    @DataProvider(name = "androidTemplates")
    public Object[][] provideAndroidTemplates(ITestContext context) {
        return filterSmokeTemplates(context, androidTemplates);
    }

    @Override
    @DataProvider(name = "iosTemplates")
    public Object[][] provideIosTemplates(ITestContext context) {
        return filterSmokeTemplates(context, iosTemplates);
    }

    @Override
    @DataProvider(name = "cordovaTemplates")
    public Object[][] provideCordovaTemplates(ITestContext context) {
        return filterSmokeTemplates(context, cordovaTemplates);
    }

    private Object[][] filterSmokeTemplates(ITestContext context, Object[][] templates) {
        List<String> includedGroups = Arrays.asList(context.getIncludedGroups());

        if (includedGroups.contains("smoke")) {
            System.out.println("Only testing templates from 'smoke' list.");
            // TODO return only templates which ids are contained in "smoke templates list"
        }

        return templates;
    }
}
