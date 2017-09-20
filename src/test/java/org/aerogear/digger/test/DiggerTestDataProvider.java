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
