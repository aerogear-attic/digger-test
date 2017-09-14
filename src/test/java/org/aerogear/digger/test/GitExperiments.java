package org.aerogear.digger.test;

import org.aerogear.digger.test.helper.GitHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.testng.annotations.Test;

/**
 * @author Ali Ok (ali.ok@apache.org)
 * 14/09/2017 15:05
 **/
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
                .cloneOrUpdate(repoUrl, repoBranch, projectName);
    }


}
