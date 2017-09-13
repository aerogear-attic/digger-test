package org.aerogear.digger.test.feedhenry;

import lombok.Getter;
import org.aerogear.digger.test.DiggerTestingEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        LOG.info("Templates will be based on {}:{}/global.json", fhtaRepoUrl, fhtaRepoBranch, "/global.json");
    }
}
