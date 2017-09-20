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

package org.aerogear.digger.test.helper;

import com.google.inject.name.Named;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.URIish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singleton;
import static org.eclipse.jgit.lib.ConfigConstants.*;

/**
 * Provides functions to help with syncing a Git repo.
 */
public class GitHelper {

    // TODO: check timeout
    // TODO: check isSuccessfull
    // TODO: refactor into small methods

    private static final Logger LOG = LoggerFactory.getLogger(GitHelper.class);

    private static final String DEFAULT_REMOTE_NAME = "origin";

    private String templatesDir;

    @Inject
    public GitHelper(@Named("templatesDir") String templatesDir) {
        this.templatesDir = templatesDir;
    }

    /**
     * Syncs a Git repo. If there is no existing local repository, this method clones the remote
     * repository. If there is a local repository already, this method pulls the changes from
     * the given remote repository.
     * <p>
     * Pulling a remote repository would require some things:
     * <p>
     * <ul>
     * <li>Checking if the Git <code>remote</code> exists and updating/adding it if necessary</li>
     * <li>Fetching the remote</li>
     * <li>Hard resetting current branch to HEAD</li>
     * <li>Checking out specified repository branch</li>
     * <li>Pulling from the remote branch</li>
     * </ul>
     * <p>
     * Target path is computed using <code>@Named</code> {@link #templatesDir} parameter.
     * Essentially it is <code>templatesDir + "/" + projectName</code>.
     *
     * @param repoUrl     - remote repository URL
     * @param repoBranch  - branch name to sync in remote repository
     * @param projectName - project name to use for logging and also computing the path for the local repository
     * @return the path of the target local repository
     */
    public String sync(String repoUrl, String repoBranch, String projectName) {
        final String targetPath = FilenameUtils.concat(this.templatesDir, projectName);

        LOG.info("Going to clone or update project. url={}, targetDir={}, branch={}, projectName={}", repoUrl, targetPath, repoBranch, projectName);

        final File repoDir = new File(targetPath);
        if (!repoDir.exists()) {
            LOG.debug("Local repository not found, going to clone. projectName={}", projectName);
            gitClone(repoUrl, targetPath, repoBranch, projectName);
            return targetPath;
        }

        if (!repoDir.isDirectory()) {
            throw new RuntimeException("Target repository path for the project " + projectName + " is not a directory.");
        }

        final String dotGitPath = FilenameUtils.concat(targetPath, ".git");
        final File dotGitDir = new File(dotGitPath);
        if (!dotGitDir.exists()) {
            throw new RuntimeException(".git directory in target repository path for the project " + projectName + " is does not exist.");
        }

        if (!dotGitDir.isDirectory()) {
            throw new RuntimeException(".git directory in target repository path for the project " + projectName + " is not a directory.");
        }

        LOG.debug("Local repository found, going to use it. projectName={}", projectName);
        final Repository repository = openRepository(repoUrl, dotGitPath, repoBranch, projectName);

        this.addRemote(repository, repoUrl, projectName);
        this.fetchRemote(repository, projectName);
        this.resetToHead(repository, projectName);
        this.checkoutRepoBranch(repository, repoUrl, repoBranch, projectName);
        this.resetToRepoBranch(repository, repoBranch, projectName);
        this.pullRemote(repository, repoBranch, projectName);

        return targetPath;
    }

    private void pullRemote(Repository repository, String repoBranch, String projectName) {
        LOG.trace("Pulling '{}' branch in repository for projectName={}", repoBranch, projectName);
        final Git git = new Git(repository);

        try {
            git.pull()
                    .setRemote(DEFAULT_REMOTE_NAME)
                    .setRemoteBranchName(repoBranch)
                    .setStrategy(MergeStrategy.THEIRS)
                    .call();
        } catch (GitAPIException e) {
            LOG.error("Error while pulling for project={}", repoBranch, projectName);
            LOG.error("Reason", e);
            throw new RuntimeException("Unable to pull", e);
        }
    }

    private void checkoutRepoBranch(Repository repository, String repoUrl, String repoBranch, String projectName) {
        LOG.trace("Checking out '{}' branch in repository for projectName={}", repoBranch, projectName);
        final Git git = new Git(repository);
        final List<Ref> refList;
        try {
            refList = git
                    .branchList()
                    //.setListMode()  // don't set this to get local branches only
                    .call();
        } catch (GitAPIException e) {
            LOG.error("Error while listing local branches for project={}", projectName);
            LOG.error("Reason", e);
            throw new RuntimeException("Unable to list local branches", e);
        }

        boolean foundBranch = false;
        for (Ref ref : refList) {
            if (ref.getName().equals("refs/heads/" + repoBranch)) {
                foundBranch = true;
            }
        }

        if (!foundBranch) {
            LOG.trace("Branch '{}' is not found in repository for projectName={}. Creating it.", repoBranch, projectName);
            try {
                git.branchCreate()
                        .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                        .setName(repoBranch)
                        .setStartPoint(DEFAULT_REMOTE_NAME + "/" + repoBranch)
                        .call();
            } catch (GitAPIException e) {
                LOG.error("Error while creating branch `{}` for project={}", repoBranch, projectName);
                LOG.error("Reason", e);
                throw new RuntimeException("Unable to create branch", e);
            }
        } else {
            LOG.trace("Branch '{}' is found in repository for projectName={}. Checking its track.", repoBranch, projectName);
            // check remote and track
            final String branchRemote = repository.getConfig().getString(CONFIG_BRANCH_SECTION, repoBranch, CONFIG_KEY_REMOTE);
            final String mergeRef = repository.getConfig().getString(CONFIG_BRANCH_SECTION, repoBranch, CONFIG_KEY_MERGE);
            if (!StringUtils.equals(branchRemote, DEFAULT_REMOTE_NAME) || !StringUtils.equals(mergeRef, "refs/heads/" + repoBranch)) {
                // delete and recreate

                LOG.info("Branch '{}' does not point to track remote {} and/or track branch '{}'. Overwriting track info.", repoBranch, branchRemote, repoBranch);
                repository.getConfig().setString(CONFIG_BRANCH_SECTION, repoBranch, CONFIG_KEY_REMOTE, DEFAULT_REMOTE_NAME);
                repository.getConfig().setString(CONFIG_BRANCH_SECTION, repoBranch, CONFIG_KEY_MERGE, "refs/heads/" + repoBranch);
                try {
                    repository.getConfig().save();
                } catch (IOException e) {
                    LOG.error("Error while overwriting track info for branch `{}` for project={}", repoBranch, projectName);
                    LOG.error("Reason", e);
                    throw new RuntimeException("Unable to overwrite track info", e);
                }
            } else {
                LOG.trace("Branch '{}' has correct track remote and branch.", repoBranch);
            }

        }

        try {
            git.checkout()
                    .setName(repoBranch)
                    .setCreateBranch(false)
                    .call();
        } catch (GitAPIException e) {
            LOG.error("Error while checking out branch `{}` for project={}", repoBranch, projectName);
            LOG.error("Reason", e);
            throw new RuntimeException("Unable to check out branch", e);
        }
    }

    private void resetToHead(Repository repository, String projectName) {
        LOG.trace("Hard reseting current branch of repository to HEAD for projectName={}", projectName);
        final Git git = new Git(repository);
        try {
            git.reset().setMode(ResetCommand.ResetType.HARD).call();
        } catch (GitAPIException e) {
            LOG.error("Error while hard reseting repository for project={}", projectName);
            LOG.error("Reason", e);
            throw new RuntimeException("Unable to hard reset", e);
        }

    }

    private void resetToRepoBranch(Repository repository, String repoBranch, String projectName) {
        LOG.trace("Hard reseting current branch of repository for projectName={}", projectName);
        final Git git = new Git(repository);
        try {
            git.reset().setMode(ResetCommand.ResetType.HARD).setRef("refs/heads/" + repoBranch).call();
        } catch (GitAPIException e) {
            LOG.error("Error while hard reseting repository for project={}", projectName);
            LOG.error("Reason", e);
            throw new RuntimeException("Unable to hard reset", e);
        }

    }

    private void fetchRemote(Repository repository, String projectName) {
        LOG.trace("Fetching remote '{}' for projectName={}", DEFAULT_REMOTE_NAME, projectName);
        final Git git = new Git(repository);
        try {
            git.fetch()
                    .setRemote(DEFAULT_REMOTE_NAME)
                    .setCheckFetchedObjects(true)
                    .call();
        } catch (GitAPIException e) {
            LOG.error("Error while fetching remote `{}` for project={}", DEFAULT_REMOTE_NAME, projectName);
            LOG.error("Reason", e);
            throw new RuntimeException("Unable to fetch remote", e);
        }
    }

    private void addRemote(Repository repository, String repoUrl, String projectName) {
        LOG.trace("Adding remote to repository if necessary url={}, projectName={}", repoUrl, projectName);

        final StoredConfig config = repository.getConfig();
        final Set<String> remotes = config.getSubsections("remote");
        final Git git = new Git(repository);
        final URIish remoteURI;
        try {
            remoteURI = new URIish(repoUrl);
        } catch (URISyntaxException e) {
            LOG.error("Unable to parse remote url for project={} with url={}", projectName, repoUrl);
            LOG.error("Reason", e);
            throw new RuntimeException("Unable to overwrite remote url", e);
        }

        boolean defaultRemoteNameFound = false;
        for (String remote : remotes) {
            if (remote.equals(DEFAULT_REMOTE_NAME)) {
                final String currentRemoteUrlForDefaultRemoteName = config.getString(CONFIG_REMOTE_SECTION, DEFAULT_REMOTE_NAME, CONFIG_KEY_URL);
                if (!StringUtils.equals(currentRemoteUrlForDefaultRemoteName, repoUrl)) {
                    LOG.info("Found a different URL for remote '{}' for project={}. Overwriting", DEFAULT_REMOTE_NAME, projectName);
                    final RemoteSetUrlCommand remoteSetUrlCommand = git.remoteSetUrl();
                    remoteSetUrlCommand.setName(DEFAULT_REMOTE_NAME);
                    remoteSetUrlCommand.setUri(remoteURI);
                    try {
                        remoteSetUrlCommand.call();
                    } catch (GitAPIException e) {
                        LOG.error("Unable to overwrite remote url for remote={} with url={}", DEFAULT_REMOTE_NAME, repoUrl);
                        LOG.error("Reason", e);
                        throw new RuntimeException("Unable to overwrite remote url", e);
                    }

                } else {
                    LOG.trace("Found remote with same url url={}, projectName={}", repoUrl, projectName);
                }
                defaultRemoteNameFound = true;
            }
        }

        if (!defaultRemoteNameFound) {
            LOG.trace("Unable to find remote '{}'. Adding it", DEFAULT_REMOTE_NAME);
            final RemoteAddCommand remoteAddCommand = git.remoteAdd();
            remoteAddCommand.setName(DEFAULT_REMOTE_NAME);
            remoteAddCommand.setUri(remoteURI);
            try {
                remoteAddCommand.call();
            } catch (GitAPIException e) {
                LOG.error("Unable to add remote url for remote={} with url={}", DEFAULT_REMOTE_NAME, repoUrl);
                LOG.error("Reason", e);
                throw new RuntimeException("Unable to add remote url", e);
            }
        }
    }

    private Repository openRepository(String repoUrl, String targetPath, String repoBranch, String projectName) {
        LOG.trace("Opening repository url={}, targetDir={}, branch={}, projectName={}", repoUrl, targetPath, repoBranch, projectName);

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            return builder.setGitDir(new File(targetPath))
                    // don't do these
                    // .readEnvironment() // scan environment GIT_* variables
                    // .findGitDir() // scan up the file system tree
                    .build();
        } catch (Exception e) {
            LOG.error("Error while opening repository for reading, url={}, targetDir={}, branch={}, projectName={}", repoUrl, targetPath, repoBranch, projectName);
            LOG.error("Reason", e);
            throw new RuntimeException("Unable to clone repository.", e);
        }
    }

    private void gitClone(String repoUrl, String targetDirectory, String branch, String projectName) {
        LOG.trace("Cloning... url={}, targetDir={}, branch={}, projectName={}", repoUrl, targetDirectory, branch, projectName);
        try {
            // cannot shallow copy unfortunately :(
            // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=475615
            Git.cloneRepository()
                    .setRemote(DEFAULT_REMOTE_NAME)
                    .setURI(repoUrl)
                    .setDirectory(new File(targetDirectory))
                    .setBranchesToClone(singleton("refs/heads/" + branch))
                    .setBranch("refs/heads/" + branch)
                    .call();
        } catch (GitAPIException e) {
            LOG.error("Error while cloning repository, url={}, targetDir={}, branch={}, projectName={}", repoUrl, targetDirectory, branch, projectName);
            LOG.error("Reason", e);
            throw new RuntimeException("Unable to clone repository.", e);
        }
    }
}
