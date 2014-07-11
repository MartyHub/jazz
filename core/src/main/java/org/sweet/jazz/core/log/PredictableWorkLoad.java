package org.sweet.jazz.core.log;

public interface PredictableWorkLoad extends Work {

    void worked(String message);

    void failed(String message, Throwable cause);

    void worked(final boolean ok, String message, Throwable cause);

    PredictableWorkLoad synchronize();
}
