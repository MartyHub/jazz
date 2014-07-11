package org.sweet.jazz.core.log;

public interface Work {

    boolean isCancelled();

    void cancel();

    void log(String message);
}
