package org.sweet.jazz.core.log.impl;

import org.sweet.jazz.core.log.PredictableWorkLoad;

public class SynchronizedPredictableWorkLoad implements PredictableWorkLoad {

    private final PredictableWorkLoad delegate;

    public SynchronizedPredictableWorkLoad(PredictableWorkLoad delegate) {
        if (delegate == null) {
            throw new NullPointerException();
        }

        this.delegate = delegate;
    }

    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public synchronized void cancel() {
        delegate.cancel();
    }

    public synchronized void log(String message) {
        delegate.log(appendThreadName(message));
    }

    public synchronized void worked(String message) {
        delegate.worked(appendThreadName(message));
    }

    public synchronized void failed(String message, Throwable cause) {
        delegate.failed(appendThreadName(message), cause);
    }

    public synchronized void worked(final boolean ok, String message, Throwable cause) {
        delegate.worked(ok, appendThreadName(message), cause);
    }

    public PredictableWorkLoad synchronize() {
        return this;
    }

    private String appendThreadName(String message) {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        sb.append(Thread.currentThread()
                .getName());
        sb.append("] ");
        sb.append(message);

        return sb.toString();
    }
}
