package org.sweet.jazz.core.log.impl;

import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.PredictableWorkLoad;

public class CompositePredictableWorkLoad implements PredictableWorkLoad {

    private final PredictableWorkLoad[] workLoads;

    private final Activity parent;

    CompositePredictableWorkLoad(Activity parent, PredictableWorkLoad[] workLoads) {
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

        for (PredictableWorkLoad pw : workLoads) {
            pw.cancel();
        }
    }

    public void log(String message) {
        parent.log(message);
    }

    public void worked(String message) {
        for (PredictableWorkLoad pw : workLoads) {
            pw.worked(message);
        }
    }

    public void failed(String message, Throwable cause) {
        for (PredictableWorkLoad pw : workLoads) {
            pw.failed(message, cause);
        }
    }

    public void worked(final boolean ok, String message, Throwable cause) {
        for (PredictableWorkLoad pw : workLoads) {
            pw.worked(ok, message, cause);
        }
    }

    public PredictableWorkLoad synchronize() {
        return new SynchronizedPredictableWorkLoad(this);
    }
}
