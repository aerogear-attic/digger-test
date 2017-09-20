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

package org.aerogear.digger.test.feedhenry;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import org.aerogear.digger.test.DiggerTestDataProvider;
import org.aerogear.digger.test.DiggerTestingEnv;
import org.apache.commons.io.FilenameUtils;

/**
 * Defines some bindings for the Digger testing project with providing data
 * of FeedHenry templates. This is used as the <code>parent-module</code> in
 * <code>testng.xml</code> file.
 */
public class DiggerFeedHenryTemplatesTestModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder
                .bind(DiggerTestDataProvider.class)
                .to(FeedHenryTemplatesDataProvider.class)
                .in(Scopes.SINGLETON);

        binder
                .bind(DiggerTestingEnv.class)
                .to(FeedHenryTemplatesDiggerTestingEnv.class)
                .in(Scopes.SINGLETON);

        binder
                .bindConstant()
                .annotatedWith(Names.named("templatesDir"))
                .to(FilenameUtils.concat(System.getProperty("user.dir"), ".templates"));


    }
}
