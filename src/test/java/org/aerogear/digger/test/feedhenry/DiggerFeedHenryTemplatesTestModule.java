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
