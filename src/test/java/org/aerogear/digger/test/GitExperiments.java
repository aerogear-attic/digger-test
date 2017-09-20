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

import org.aerogear.digger.test.helper.GitHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.testng.annotations.Test;

// TODO: to be deleted... this class is used to quickly test the GitHelper class
@Test
public class GitExperiments {

    @Test
    public void testExperiment() {
        // change level on the runtime for ad-hoc experiments
        final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("org.aerogear.digger.test");
        logger.setLevel(Level.TRACE);

        final String repoUrl = "git://github.com/matskiv/fh-template-apps.git";
        final String repoBranch = "dummy-jenkinsfile";
        final String projectName = "fh-template-apps";

        new GitHelper(FilenameUtils.concat(System.getProperty("user.dir"), ".experiment-templates"))
                .sync(repoUrl, repoBranch, projectName);
    }


}
