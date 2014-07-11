package org.sweet.jazz.core.log.impl;

import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.PredictableWorkLoad;
import org.sweet.jazz.core.log.UnknownWorkLoad;

public class CompositeActivity extends AbstractActivity {

    private final Activity[] activities;

    public CompositeActivity(String name, Activity... activities) {
        this(name, null, activities);
    }

    protected CompositeActivity(String name, Activity parent, Activity[] activities) {
        super(name, parent);

        if (activities == null) {
            throw new NullPointerException();
        }

        this.activities = activities;
    }

    public void log(String message) {
        for (Activity activity : activities) {
            activity.log(message);
        }
    }

    public Activity createSubActivity(String name) {
        final int length = activities.length;
        Activity[] subActivities = new Activity[length];

        for (int i = 0; i < length; ++i) {
            subActivities[i] = activities[i].createSubActivity(name);
        }

        return new CompositeActivity(name, this, subActivities);
    }

    @Override
    protected boolean log() {
        return false;
    }

    @Override
    protected void doCancel() {
        for (Activity activity : activities) {
            activity.cancel();
        }
    }

    @Override
    protected UnknownWorkLoad doStart() {
        final int length = activities.length;
        UnknownWorkLoad[] workLoads = new UnknownWorkLoad[length];

        for (int i = 0; i < length; ++i) {
            workLoads[i] = activities[i].start();
        }

        return new CompositeUnknownWorkLoad(this, workLoads);
    }

    @Override
    protected PredictableWorkLoad doStart(int totalAmount) {
        final int length = activities.length;
        PredictableWorkLoad[] workLoads = new PredictableWorkLoad[length];

        for (int i = 0; i < length; ++i) {
            workLoads[i] = activities[i].start(totalAmount);
        }

        return new CompositePredictableWorkLoad(this, workLoads);
    }
}
