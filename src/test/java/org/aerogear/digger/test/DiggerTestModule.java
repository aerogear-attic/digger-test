package org.aerogear.digger.test;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * @author Ali Ok (ali.ok@apache.org)
 * 13/09/2017 12:38
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
