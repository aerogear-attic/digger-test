package org.aerogear.digger.test.helper;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import org.aerogear.digger.client.DiggerClient;
import org.aerogear.digger.client.model.BuildParameter;
import org.aerogear.digger.client.model.BuildTriggerStatus;
import org.aerogear.digger.client.util.DiggerClientException;
import org.aerogear.digger.test.DiggerTestingEnv;
import org.aerogear.digger.test.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.List;
import java.util.UUID;

@Singleton
public class TemplateTester {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateTester.class);

    private static final long BUILD_WAIT_POLL_STEP_MS = 500;
    private static final long DIGGER_BUILD_TIMEOUT = DiggerClient.DEFAULT_BUILD_TIMEOUT;

    private static final String PLATFORM_PARAM_KEY = "PLATFORM";
    private static final String BUILD_CONFIG_PARAM_KEY = "BUILD_CONFIG";
    private static final String BUILD_CREDENTIAL_ID_PARAM_KEY = "BUILD_CREDENTIAL_ID";

    private DiggerTestingEnv testingEnv;

    @Inject
    public TemplateTester(DiggerTestingEnv testingEnv) {
        this.testingEnv = testingEnv;
    }

    // TODO: timeout for the entire thing ?
    public void test(Template template, String platform, String buildConfig, String codeSignProfileId) throws Exception {

        final DiggerClient diggerClient = DiggerClient.createDefaultWithAuth(
                testingEnv.getDiggerTargetUrl(),
                testingEnv.getDiggerUsername(),
                testingEnv.getDiggerPassword()
        );

        final String jobName = testingEnv.getPrefix() + "-" + template.getTemplateId() + "-" + UUID.randomUUID().toString();
        final List<BuildParameter> jobParams = Lists.newArrayList(
                new BuildParameter(PLATFORM_PARAM_KEY),
                new BuildParameter(BUILD_CONFIG_PARAM_KEY),
                new BuildParameter(BUILD_CREDENTIAL_ID_PARAM_KEY)
        );

        LOG.debug("Creating job in Digger. jobName={}, templateId={}, jobParams={}", jobName, template.getTemplateId(), jobParams);
        diggerClient.createJob(jobName, template.getRepoUrl(), template.getRepoBranch(), null, jobParams);

        final ImmutableMap<String, String> params = ImmutableMap.<String, String>builder()
                .put(PLATFORM_PARAM_KEY, platform)
                .put(BUILD_CONFIG_PARAM_KEY, buildConfig)
                .put(BUILD_CREDENTIAL_ID_PARAM_KEY, StringUtils.defaultString(codeSignProfileId))
                .build();

        LOG.debug("Triggering a build. jobName={}, templateId={}, platform={}, params={}", jobName, template.getTemplateId(), platform, params);
        final BuildTriggerStatus buildTriggerStatus = diggerClient.build(jobName, DIGGER_BUILD_TIMEOUT, params);

        Assert.assertEquals(buildTriggerStatus.getState(), BuildTriggerStatus.State.STARTED_BUILDING, "Build has not started succesfully");

        LOG.debug("Build started successfully. jobName={}, buildNumber={}, templateId={}, platform={}", jobName, buildTriggerStatus.getBuildNumber(), template.getTemplateId(), platform);

        LOG.debug("Going to wait until the build completes.");
        final BuildWithDetails buildDetails = this.waitUntilCompletes(diggerClient, jobName, buildTriggerStatus.getBuildNumber());

        // don't use testng.Assert here as we would like to report some stuff
        if (BuildResult.SUCCESS.equals(buildDetails.getResult())) {
            LOG.debug("Successfully built template={} for platform={}", template.getTemplateId(), platform);
        } else {
            LOG.error("Build failed for template={} for platform={} with params={}", template.getTemplateId(), platform, params);
            LOG.error("Here is the build output:");
            LOG.error(buildDetails.getConsoleOutputText());
            Assert.fail("Build failed for template=" + template.getTemplateId() + " for platform=" + platform);
        }
    }

    private BuildWithDetails waitUntilCompletes(DiggerClient diggerClient, String jobName, int buildNumber) throws InterruptedException, DiggerClientException {
        BuildWithDetails buildDetails = diggerClient.getBuildDetails(jobName, buildNumber);

        // no need to timeout on this as build is started with a timeout value on Digger side
        while (buildDetails.isBuilding()) {
            Thread.sleep(BUILD_WAIT_POLL_STEP_MS);
            buildDetails = diggerClient.getBuildDetails(jobName, buildNumber);
        }

        return buildDetails;
    }
}
