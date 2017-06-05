package org.aerogear.digger.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.aerogear.digger.client.*;
import org.aerogear.digger.client.model.BuildStatus;
import org.aerogear.digger.client.util.DiggerClientException;
import java.util.UUID;

public class AndroidTemplatesTest {

    @Test
    public void testBlankProject() throws DiggerClientException {
        DiggerClient client = DiggerClient.createDefaultWithAuth("https://jenkins-digger-jenkins.omatskivmulti.skunkhenry.com", "api_user", "Password1");
        String jobName = System.getenv().get("prefix")+"-blank-"+UUID.randomUUID().toString();
        client.createJob(jobName, "https://github.com/maleck13/blank-android-gradle.git", "master");
        BuildStatus status = client.build(jobName);
        System.out.println(status);
    }
}
