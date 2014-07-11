package org.sweet.jazz.core.log.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweet.jazz.core.log.Activity;

public class Slf4jActivity extends AbstractActivity {

    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jActivity.class);

    public Slf4jActivity(String name) {
        super(name);
    }

    protected Slf4jActivity(String name, Activity parent) {
        super(name, parent);
    }

    public void log(String message) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("<{}> {}", getName(), message);
        }
    }

    public Activity createSubActivity(String name) {
        return new Slf4jActivity(String.format("%s / %s", getName(), name), this);
    }
}
