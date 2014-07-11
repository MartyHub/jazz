package org.sweet.jazz.core.log;

public interface Activity extends Work {

    String getName();

    Activity createSubActivity(String name);

    UnknownWorkLoad start();

    PredictableWorkLoad start(final int totalAmount);
}
