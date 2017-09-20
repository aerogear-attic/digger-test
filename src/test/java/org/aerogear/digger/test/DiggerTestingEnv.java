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

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * We have this class to hold all parameters related to testing.
 * The reason that we have it this way is we want to externalize
 * reading of these parameters.
 * Also, this approach would make the code extendable.
 **/
@Getter
public class DiggerTestingEnv {

    private static final Logger LOG = LoggerFactory.getLogger(DiggerTestingEnv.class);

    protected String prefix;
    protected String diggerTargetUrl;
    protected String diggerUsername;
    protected String diggerPassword;

    public void initialize() {
        LOG.debug("Reading Digger testing environment parameters");

        this.prefix = System.getProperty("prefix");
        this.diggerTargetUrl = System.getProperty("diggerTargetUrl");
        this.diggerUsername = System.getProperty("diggerUsername");
        this.diggerPassword = System.getProperty("diggerPassword");

        LOG.debug("Here are env: prefix={}, diggerTargetUrl={}, diggerUsername={}, diggerPassword={}",
                this.prefix, this.diggerTargetUrl, this.diggerUsername, this.diggerPassword);

        if (StringUtils.isBlank(diggerTargetUrl)) {
            throw new RuntimeException("diggerTargetUrl is required");
        }

        if (StringUtils.isBlank(prefix)) {
            throw new RuntimeException("prefix is required");
        }
    }
}
