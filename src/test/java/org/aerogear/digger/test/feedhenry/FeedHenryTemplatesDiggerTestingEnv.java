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

import lombok.Getter;
import org.aerogear.digger.test.DiggerTestingEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extends {@link DiggerTestingEnv} with some parameters specific to FeedHenry templates.
 */
@Getter
public class FeedHenryTemplatesDiggerTestingEnv extends DiggerTestingEnv {

    private static final Logger LOG = LoggerFactory.getLogger(FeedHenryTemplatesDiggerTestingEnv.class);

    protected String fhtaRepoUrl;
    protected String fhtaRepoBranch;

    @Override
    public void initialize() {
        super.initialize();

        LOG.debug("Reading Digger FeedHenry templates testing environment parameters");

        this.fhtaRepoUrl = System.getProperty("fhtaRepoUrl");
        this.fhtaRepoBranch = System.getProperty("fhtaRepoBranch");

        LOG.debug("Here are env: fhtaRepoUrl={}, fhtaRepoBranch={}",
                this.fhtaRepoUrl, this.fhtaRepoBranch);
    }
}
