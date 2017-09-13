package org.aerogear.digger.test;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public interface DiggerTestDataProvider {

    void initialize();

    @DataProvider(name = "androidTemplates")
    Object[][] provideAndroidTemplates(ITestContext context);

    @DataProvider(name = "iosTemplates")
    Object[][] provideIosTemplates(ITestContext context);

    @DataProvider(name = "cordovaTemplates")
    Object[][] provideCordovaTemplates(ITestContext context);
}
