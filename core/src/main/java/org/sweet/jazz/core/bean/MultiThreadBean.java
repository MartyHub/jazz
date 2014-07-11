package org.sweet.jazz.core.bean;

import org.sweet.bumblebee.Doc;
import org.sweet.bumblebee.bean.ValidatableBean;
import org.sweet.bumblebee.util.ValidationResult;
import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.util.JazzCoreHelper;
import org.sweet.jazz.core.util.JazzThreadFactory;

import javax.enterprise.inject.Vetoed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Vetoed
public class MultiThreadBean implements ValidatableBean {

    public static interface HasMultiThreadBean {

        MultiThreadBean getMultiThreadBean();
    }

    private int threadCount = getDefaultThreadCount();

    public int getOptionalThreadCount() {
        return threadCount;
    }

    @Doc("Number of threads to use")
    public void setOptionalThreadCount(final int threadCount) {
        this.threadCount = threadCount;
    }

    public void validate(ValidationResult result) {
        if (threadCount < 1) {
            result.addError("threadCount must be > 0");
        }
    }

    public ExecutorService createExecutorService(Activity activity, final int taskCount) {
        if (taskCount < 0) {
            throw new IllegalArgumentException("task count must be >= 0 : " + taskCount);
        }

        int nThreads = threadCount;

        if (nThreads > taskCount) {
            nThreads = taskCount;
        }

        if (nThreads == 0) {
            nThreads = 1;
        }

        activity.log(String.format("#setup with %s thread(s)", JazzCoreHelper.createPrettyIntegerFormatter()
                .format(nThreads)));

        return Executors.newFixedThreadPool(nThreads, new JazzThreadFactory(activity.getName(), nThreads));
    }

    protected int getDefaultThreadCount() {
        final int availableProcessors = Runtime.getRuntime()
                .availableProcessors();

        return Math.min(4, availableProcessors);
    }
}
