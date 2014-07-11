package org.sweet.jazz.core.log.impl;

import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.Work;
import org.threeten.bp.Instant;

public abstract class AbstractWork implements Work {

    protected final Instant start = Instant.now();

    private final Activity parent;

    protected AbstractWork(Activity parent) {
        if (parent == null) {
            throw new NullPointerException();
        }

        this.parent = parent;
    }

    public final boolean isCancelled() {
        return parent.isCancelled();
    }

    public final void cancel() {
        parent.cancel();
    }

    public final void log(String message) {
        parent.log(message);
    }
}
