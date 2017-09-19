package org.aerogear.digger.test;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

/**
 * A TestNG data provider for the tests. Implementations
 * should provide these data.
 * <p>
 * We went with a non-static data provider that is injected to tests.
 * The reason is, it is super bad to make project extendable with static
 * things.
 */
public interface DiggerTestDataProvider {

    void initialize();

    @DataProvider(name = "androidTemplates")
    Object[][] provideAndroidTemplates(ITestContext context);

    @DataProvider(name = "iosTemplates")
    Object[][] provideIosTemplates(ITestContext context);

    @DataProvider(name = "cordovaTemplates")
    Object[][] provideCordovaTemplates(ITestContext context);
}
