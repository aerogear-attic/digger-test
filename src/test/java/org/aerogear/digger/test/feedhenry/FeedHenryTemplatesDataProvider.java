package org.aerogear.digger.test.feedhenry;

import com.google.common.base.Charsets;
import org.aerogear.digger.test.DiggerTestDataProvider;
import org.aerogear.digger.test.DiggerTestingEnv;
import org.aerogear.digger.test.Template;
import org.aerogear.digger.test.helper.GitHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.Collections.singleton;

public class FeedHenryTemplatesDataProvider implements DiggerTestDataProvider {

    private static final Logger LOG = LoggerFactory.getLogger(FeedHenryTemplatesDataProvider.class);

    private static final String TEMPLATES_PARENT_DIR_PATH = FilenameUtils.concat(System.getProperty("user.dir"), ".templates");

    private DiggerTestingEnv diggerTestingEnv;
    private GitHelper gitHelper;

    private Object[][] androidTemplates;
    private Object[][] iosTemplates;
    private Object[][] cordovaTemplates;

    // don't inject a FeedHenryTemplatesDiggerTestingEnv as that will create a new instance inject it.
    // the binding definition should be for DiggerTestingEnv
    @Inject
    public FeedHenryTemplatesDataProvider(DiggerTestingEnv diggerTestingEnv, GitHelper gitHelper) {
        this.diggerTestingEnv = diggerTestingEnv;
        this.gitHelper = gitHelper;
    }

    @Override
    public void initialize() {
        final FeedHenryTemplatesDiggerTestingEnv testingEnv = (FeedHenryTemplatesDiggerTestingEnv) this.diggerTestingEnv;

        LOG.debug("Initializing FeedHenryTemplatesDataProvider");
        LOG.debug("Fetching templates from FeedHenry template apps repo");
        LOG.debug("Repo: {}, branch: {}", testingEnv.fhtaRepoUrl, testingEnv.fhtaRepoBranch);
        LOG.info("Templates will be based on {}:{}:/global.json", testingEnv.fhtaRepoUrl, testingEnv.fhtaRepoBranch);

        final LinkedHashSet<Template> templates = this.fetchTemplateConfigs(testingEnv);
        LOG.debug("TEMPLATES TO TEST -------------------------------------");
        for (Template template : templates) {
            LOG.debug("{}", template);
        }

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

    private LinkedHashSet<Template> fetchTemplateConfigs(FeedHenryTemplatesDiggerTestingEnv testingEnv) {
        // TODO: don't delete templates in the future.
        // TODO: just do "git pull" if everything is the same (repo, branch, state etc.)

        try {
            FileUtils.deleteDirectory(new File(TEMPLATES_PARENT_DIR_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Unable to delete templates parent dir: " + TEMPLATES_PARENT_DIR_PATH, e);
        }

        final String fhTemplateAppsPath = FilenameUtils.concat(TEMPLATES_PARENT_DIR_PATH, "fh-template-apps");

        LOG.info("Going to clone templates to path: " + fhTemplateAppsPath);

        try {
            this.gitHelper.clone(testingEnv.getFhtaRepoUrl(), fhTemplateAppsPath, testingEnv.getFhtaRepoBranch());
        } catch (RuntimeException e) {
            throw new RuntimeException("Unable to clone FHTA repository.", e);
        }

        final String globalJsonPath = FilenameUtils.concat(fhTemplateAppsPath, "global.json");

        final String globalJsonContent;
        try {
            LOG.debug("Parsing global.json");
            globalJsonContent = FileUtils.readFileToString(new File(globalJsonPath), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read global.json file", e);
        }

        // don't marshal with Jackson or Gson. no need. the information we need is very little anyway.
        // just parse with JSONObject and then process manually.

        final JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(globalJsonContent);
        } catch (RuntimeException e) {
            throw new RuntimeException("Unable to parse global.json", e);
        }

        final List<Template> appTemplatesList = new ArrayList<>();

        try {
            final JSONObject show = jsonObject.getJSONObject("show");
            final JSONArray projectTemplates = show.getJSONArray("projectTemplates");
            final JSONArray appTemplates = show.getJSONArray("appTemplates");

            LOG.debug("Found {} project templates and {} app templates", projectTemplates.length(), appTemplates.length());

            LOG.debug("Processing project templates");
            // first, iterate over the project templates and then the inner app templates in the project templates
            for (int i = 0; i < projectTemplates.length(); i++) {
                final JSONObject projectTemplate = projectTemplates.getJSONObject(i);
                LOG.debug("Reading the app templates in the project template {}", projectTemplate.getString("id"));
                if (projectTemplate.has("appTemplates")) {
                    final JSONArray projectAppTemplates = projectTemplate.getJSONArray("appTemplates");
                    LOG.debug("Found {} app templates in project template {}", projectAppTemplates.length(), projectTemplate.getString("id"));
                    for (int j = 0; j < projectAppTemplates.length(); j++) {
                        final Object appTemplateObject = projectAppTemplates.get(j);
                        if (appTemplateObject instanceof String) {
                            // ignore, as it is a reference to an app template that is defined somewhere else
                            LOG.debug("Skipping project app template which is a reference to an app template {}", appTemplateObject.toString());
                        } else {
                            final JSONObject jsonTemplate = projectAppTemplates.getJSONObject(j);
                            LOG.debug("Found project app template {}", jsonTemplate.getString("id"));
                            appTemplatesList.add(new Template(jsonTemplate));
                        }
                    }
                }
            }

            LOG.debug("Processing app templates");
            // then iterate over the app templates
            for (int i = 0; i < appTemplates.length(); i++) {
                final JSONObject jsonTemplate = appTemplates.getJSONObject(i);
                LOG.debug("Found app template {}", jsonTemplate.getString("id"));
                appTemplatesList.add(new Template(jsonTemplate));
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while processing global.json", e);
        }

        // using a set so that we don't have duplicates
        // using a linked hash set so that we have an iteration order same as insertion order
        final LinkedHashSet<Template> templatesToTest = new LinkedHashSet<>(appTemplatesList);
        if (templatesToTest.size() != appTemplatesList.size()) {
            LOG.warn("There are templates with duplicate id in global.json file!");
            final Collection<Template> disjunction = CollectionUtils.disjunction(templatesToTest, appTemplatesList);
            for (Template template : disjunction) {
                LOG.warn("Duplicate template: {}", template);
            }
        }

        LOG.info("Found {} app templates to test", templatesToTest.size());
        return templatesToTest;
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
