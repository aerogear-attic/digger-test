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

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * This is the Guice module that TestNG uses.
 * It is meant to be empty because the <code>parent-module</code>
 * in <code>testng.xml</code> should be the place where bindings are defined.
 * <p>
 * Note that any binding defined here will override the bindings
 * in the <code>parent-module</code>.
 * <p>
 * Implementations of this testing project should define a new module and use it
 * as the <code>parent-module</code> in <code>testng.xml</code>.
 * See {@link org.aerogear.digger.test.feedhenry.DiggerFeedHenryTemplatesTestModule} as
 * and example.
 **/
public class DiggerTestModule implements Module {
    @Override
    public void configure(Binder binder) {
        // do nothing. things are defined in parent.
        // parent module is specified in testng.xml file.
        // if any project extends this test suite, it must be
        // done in a way that only parent module in that file
        // is changed.
    }
}
