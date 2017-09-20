/*
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.aerogear.digger.test;

import org.aerogear.digger.client.util.DiggerClientException;
import org.aerogear.digger.test.helper.TemplateTester;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import javax.inject.Inject;

@Guice(modules = DiggerTestModule.class)
@Test(groups = {"ios"}, dataProviderClass = DiggerTestDataProvider.class)
public class IosTest extends DiggerSuite {

    @Inject
    private TemplateTester templateTester;

    // TODO: how to make use of groups here!

    @Test(groups = {"native", "other", "smoke"})
    public void otherNativeTest() {
        // TODO do we need this @omatskiv?
    }

    @Test(groups = {"native", "templates", "smoke"}, dataProvider = "iosTemplates")
    public void testNativeTemplates(Template t) throws DiggerClientException {
//        templateTester.test(t);

        // DiggerClient client = createClient();
        // String jobName = getPrefix() + "-" + t.getTemplateId() + "-" + UUID.randomUUID().toString();

        // System.out.println("Creating job: " + jobName);
        //client.createJob(jobName, t.getRepoUrl(), t.getRepoBranch());
        //BuildTriggerStatus status = client.build(jobName);
        //System.out.println(status);
    }

    @Test(groups = {"hybrid", "templates", "smoke"}, dataProvider = "cordovaTemplates")
    public void testHybridTemplates(Template t) throws DiggerClientException {
//        templateTester.test(t);

        // DiggerClient client = createClient();
        // String jobName = getPrefix() + "-" + t.getTemplateId() + "-" + UUID.randomUUID().toString();

        // System.out.println("Creating job: " + jobName);
        //client.createJob(jobName, t.getRepoUrl(), t.getRepoBranch());
        //BuildTriggerStatus status = client.build(jobName);
        //System.out.println(status);
    }
}
