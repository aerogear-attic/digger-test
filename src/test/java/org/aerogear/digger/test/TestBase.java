package org.aerogear.digger.test;

import java.util.Arrays;
import java.util.List;

import org.aerogear.digger.client.DiggerClient;
import org.aerogear.digger.client.util.DiggerClientException;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

public abstract class TestBase {

    private static String prefix;
    private static String target;
    private static String host;
    private static String user;
    private static String pass;
    private static String fhtaRepoUrl;
    private static String fhtaRepoBranch = "dummy-jenkinsfile";
    private static String selfManBranch;

    private static Object[][] androidTemplates;
    private static Object[][] iosTemplates;
    private static Object[][] cordovaTemplates;

    @BeforeSuite(alwaysRun = true, dependsOnMethods="cleanUp")
    public void prepareSuite(ITestContext context) throws Exception {
        // TODO test connectivity to $host

        if (selfManBranch != null && selfManBranch.length() > 0) {
            // TODO fetch git://pkgs.devel.redhat.com/rpms/rhmap-fh-sdks-docker
            // from Dockerfile extract fh-template-apps-version and fh-template-apps-builde-number
            // fetch artifact from S3 and un-targz
        } else {
            fetchTemplatesFromGithub();
        }

        parseTemplates();
    }

    @BeforeSuite(alwaysRun = true)
    public void cleanUp(ITestContext context) throws Exception {
        initParams();

        // TODO get list of apps from jenkins and remove all with $prefix
    }

    private void fetchTemplatesFromGithub() {
        // TODO fetch templates from $fhtaRepoUrl / $fhtaRepoBranch
    }

    private void parseTemplates() {
        // TODO parse templates from global.json file and sort them based on type into androidTemplates/iosTemplates/cordovaTemplates
        System.out.println("fake templates !"); // temporary
        androidTemplates = new Object[][]{
            {new Template("sync_android_app", "Sync Android App", "git://github.com/feedhenry-templates/sync-android-app.git", fhtaRepoBranch)}, // temporary
            {new Template("saml_android", "SAML Android", "git://github.com/feedhenry-templates/saml-android-app.git", fhtaRepoBranch)} // temporary
        };
        iosTemplates = new Object[][]{
            {new Template("sync_ios_objectivec_app", "Sync iOS (Objective-C) App", "git://github.com/feedhenry-templates/sync-ios-app.git", fhtaRepoBranch)} // temporary
        };
        cordovaTemplates = new Object[][]{
            {new Template("sync_app", "Sync App", "git://github.com/feedhenry-templates/sync-cordova-app.git", fhtaRepoBranch)} // temporary
        };
    }

    private void initParams() throws Exception {
        prefix = System.getProperty("prefix");
        target = System.getProperty("target");
        if (target == null || target.length() == 0) throw new Exception("target property needs to be defined");
        System.out.println("Target: " + target);
        // TODO get host, user and pass from targets.json file based on $target

        fhtaRepoUrl = System.getProperty("fhta-url");
        fhtaRepoBranch = System.getProperty("fhta-branch");
        selfManBranch = System.getProperty("self-man-branch");
        System.out.println("Templates will be based on " + fhtaRepoUrl + ":" + fhtaRepoBranch + "/global.json");
    }

    public static DiggerClient createClient() throws DiggerClientException {
        return DiggerClient.createDefaultWithAuth(host, user, pass);
    }

    public static String getPrefix() {
        return prefix;
    }

    @DataProvider(name = "androidTemplates")
    public static Object[][] provideAndroidTemplates(ITestContext context) {
        return filterSmokeTemplates(context, androidTemplates);
    }

    @DataProvider(name = "iosTemplates")
    public static Object[][] provideIosTemplates(ITestContext context) {
        return filterSmokeTemplates(context, iosTemplates);
    }

    @DataProvider(name = "cordovaTemplates")
    public static Object[][] provideCordovaTemplates(ITestContext context) {
        return filterSmokeTemplates(context, cordovaTemplates);
    }

    public static Object[][] filterSmokeTemplates(ITestContext context, Object[][] templates) {
        List<String> includedGroups = Arrays.asList(context.getIncludedGroups());

        if(includedGroups.contains("smoke")) {
            System.out.println("Only testing templates from 'smoke' list.");
            // TODO return only templates which ids are contained in "smoke templates list"
        }

        return templates;
    }
}
