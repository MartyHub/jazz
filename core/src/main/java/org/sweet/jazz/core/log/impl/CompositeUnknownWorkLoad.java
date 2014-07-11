package org.sweet.jazz.core.log.impl;

import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.UnknownWorkLoad;

public class CompositeUnknownWorkLoad implements UnknownWorkLoad {

    private final Activity parent;

    private final UnknownWorkLoad[] workLoads;

    CompositeUnknownWorkLoad(Activity parent, UnknownWorkLoad[] workLoads) {
        if (parent == null) {
            throw new NullPointerException();
        }

        if (workLoads == null) {
            throw new NullPointerException();
        }

        this.parent = parent;
        this.workLoads = workLoads;
    }

    public boolean isCancelled() {
        return parent.isCancelled();
    }

    public void cancel() {
        parent.cancel();

        for (UnknownWorkLoad uw : workLoads) {
            uw.cancel();
        }
    }

    public void log(String message) {
        parent.log(message);
    }

    public void done() {
        for (UnknownWorkLoad uw : workLoads) {
            uw.done();
        }
    }
}
