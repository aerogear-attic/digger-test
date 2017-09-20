package org.aerogear.digger.test;

import org.aerogear.digger.client.util.DiggerClientException;
import org.aerogear.digger.test.helper.TemplateTester;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import javax.inject.Inject;

@Guice(modules = DiggerTestModule.class)
@Test(groups = {"android"}, dataProviderClass = DiggerTestDataProvider.class)
public class AndroidTest extends DiggerSuite {

    // TODO: how to make use of groups here?
    // TODO: a test method per:
    // TODO: - smoke / all ???
    // TODO: - debug/release ???

    @Inject
    private TemplateTester templateTester;

    @Test(groups = {"native", "other"})
    public void otherNativeTest() {
        // TODO do we need this @omatskiv?
    }

    @Test(groups = {"native", "templates", "smoke"}, dataProvider = "androidTemplates")
    public void testNativeTemplatesDebugBuild(Template t) throws Exception {
        templateTester.test(t, "android", "debug", null);
    }

    @Test(groups = {"hybrid", "templates", "smoke"}, dataProvider = "cordovaTemplates")
    public void testHybridTemplates(Template t) throws Exception {
        templateTester.test(t, "android", "debug", null);
    }
}
