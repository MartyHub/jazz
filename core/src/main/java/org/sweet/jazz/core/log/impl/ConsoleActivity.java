package org.sweet.jazz.core.log.impl;

import org.sweet.jazz.core.log.Activity;

public class ConsoleActivity extends AbstractActivity {

    public ConsoleActivity(String name) {
        super(name);
    }

    protected ConsoleActivity(String name, Activity parent) {
        super(name, parent);
    }

    public void log(String message) {
        System.out.println(String.format("%s - %s", getName(), message));
    }

    public Activity createSubActivity(String name) {
        return new ConsoleActivity(String.format("%s / %s", getName(), name), this);
    }
}
