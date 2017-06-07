package org.aerogear.digger.test;

import org.testng.annotations.Test;
import org.aerogear.digger.client.*;
import org.aerogear.digger.client.model.BuildTriggerStatus;
import org.aerogear.digger.client.util.DiggerClientException;
import java.util.UUID;

@Test(groups = { "ios" }, dataProviderClass = TestBase.class)
public class IosTest extends TestBase {

    @Test(groups = { "native", "other", "smoke" })
    public void otherNativeTest() {
        // just example
        System.out.println("Example test iOS, also smoke group");
    }

    @Test(groups = { "native", "templates", "smoke" }, dataProvider = "iosTemplates")
    public void testNativeTemplates(Template t) throws DiggerClientException {
        DiggerClient client = createClient();
        String jobName = getPrefix()+"-"+t.getTemplateId()+"-"+UUID.randomUUID().toString();

        System.out.println("Creating job: "+jobName);
        //client.createJob(jobName, t.getRepoUrl(), t.getRepoBranch());
        //BuildTriggerStatus status = client.build(jobName);
        //System.out.println(status);
    }

    @Test(groups = { "hybrid", "templates", "smoke" }, dataProvider = "cordovaTemplates")
    public void testHybridTemplates(Template t) throws DiggerClientException {
        DiggerClient client = createClient();
        String jobName = getPrefix()+"-"+t.getTemplateId()+"-"+UUID.randomUUID().toString();

        System.out.println("Creating job: "+jobName);
        //client.createJob(jobName, t.getRepoUrl(), t.getRepoBranch());
        //BuildTriggerStatus status = client.build(jobName);
        //System.out.println(status);
    }
}
