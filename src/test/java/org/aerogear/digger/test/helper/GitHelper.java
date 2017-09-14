package org.aerogear.digger.test.helper;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;

import static java.util.Collections.singleton;

/**
 * @author Ali Ok (ali.ok@apache.org)
 * 14/09/2017 14:58
 **/
public class GitHelper {
    public void clone(String repoUrl, String targetDirectory, String branch) {
        try {
            // cannot shallow copy unfortunately :(
            // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=475615
            Git.cloneRepository()
                    .setRemote("origin")
                    .setURI(repoUrl)
                    .setDirectory(new File(targetDirectory))
                    .setBranchesToClone(singleton("refs/heads/" + branch))
                    .setBranch("refs/heads/" + branch)
                    .call();
        } catch (GitAPIException e) {
            throw new RuntimeException("Unable to repository.", e);
        }
    }
}
