package org.aerogear.digger.test.feedhenry;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import org.aerogear.digger.test.DiggerTestDataProvider;
import org.aerogear.digger.test.DiggerTestingEnv;

public class DiggerFeedHenryTemplatesTestModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(DiggerTestDataProvider.class).to(FeedHenryTemplatesDataProvider.class).in(Scopes.SINGLETON);
        binder.bind(DiggerTestingEnv.class).to(FeedHenryTemplatesDiggerTestingEnv.class).in(Scopes.SINGLETON);

    }
}
