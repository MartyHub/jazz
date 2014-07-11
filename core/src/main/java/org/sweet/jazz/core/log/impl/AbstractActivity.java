package org.sweet.jazz.core.log.impl;

import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.PredictableWorkLoad;
import org.sweet.jazz.core.log.UnknownWorkLoad;
import org.sweet.jazz.core.util.JazzCoreHelper;

public abstract class AbstractActivity implements Activity {

    protected final String name;

    protected final Activity parent;

    private boolean cancelled = false;

    protected AbstractActivity(String name) {
        this(name, null);
    }

    protected AbstractActivity(String name, Activity parent) {
        if (name == null) {
            throw new NullPointerException();
        }

        this.name = name;
        this.parent = parent;
    }

    public final String getName() {
        if (cancelled) {
            return name + " (Cancelled)";
        } else {
            return name;
        }
    }

    public final boolean isCancelled() {
        if (parent != null) {
            return parent.isCancelled();
        }

        return cancelled;
    }

    public final void cancel() {
        if (parent != null) {
            parent.cancel();
        } else {
            if (log()) {
                log("#cancelled");
            }

            this.cancelled = true;
        }
    }

    public final UnknownWorkLoad start() {
        if (log()) {
            log("#start");
        }

        return doStart();
    }

    public final PredictableWorkLoad start(final int totalAmount) {
        if (log()) {
            StringBuilder sb = new StringBuilder();

            sb.append("#start with ");
            sb.append(JazzCoreHelper.createPrettyIntegerFormatter()
                    .format(totalAmount));
            sb.append(" task(s)");

            log(sb.toString());
        }

        return doStart(totalAmount);
    }

    @Override
    public String toString() {
        return name;
    }

    protected boolean log() {
        return true;
    }

    protected void doCancel() {
    }

    protected UnknownWorkLoad doStart() {
        return new DefaultUnknownWorkLoad(this);
    }

    protected PredictableWorkLoad doStart(final int totalAmount) {
        return new DefaultPredictableWorkLoad(this, totalAmount);
    }
}
