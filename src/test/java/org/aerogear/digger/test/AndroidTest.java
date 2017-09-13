package org.aerogear.digger.test;

import org.aerogear.digger.client.util.DiggerClientException;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

@Guice(modules = DiggerTestModule.class)
@Test(groups = {"android"}, dataProviderClass = DiggerTestDataProvider.class)
public class AndroidTest extends DiggerSuite {

    @Test(groups = {"native", "other"})
    public void otherNativeTest() {
        // just example
        System.out.println("Example test android");
    }

    @Test(groups = {"native", "templates", "smoke"}, dataProvider = "androidTemplates")
    public void testNativeTemplates(Template t) throws DiggerClientException {
        // DiggerClient client = createClient();
        // String jobName = getPrefix() + "-" + t.getTemplateId() + "-" + UUID.randomUUID().toString();

        // System.out.println("Creating job: " + jobName);
        //client.createJob(jobName, t.getRepoUrl(), t.getRepoBranch());
        //BuildTriggerStatus status = client.build(jobName);
        //System.out.println(status);
    }

    @Test(groups = {"hybrid", "templates", "smoke"}, dataProvider = "cordovaTemplates")
    public void testHybridTemplates(Template t) throws DiggerClientException {
        // DiggerClient client = createClient();
        // String jobName = getPrefix() + "-" + t.getTemplateId() + "-" + UUID.randomUUID().toString();

        // System.out.println("Creating job: " + jobName);
        //client.createJob(jobName, t.getRepoUrl(), t.getRepoBranch());
        //BuildTriggerStatus status = client.build(jobName);
        //System.out.println(status);
    }
}
