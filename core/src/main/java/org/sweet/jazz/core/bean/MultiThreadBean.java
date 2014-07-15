package org.sweet.jazz.core.bean;

import org.sweet.bumblebee.Doc;
import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.util.JazzCoreHelper;
import org.sweet.jazz.core.util.JazzThreadFactory;

import javax.enterprise.inject.Vetoed;
import javax.validation.constraints.Min;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Vetoed
public class MultiThreadBean {

    public static interface HasMultiThreadBean {

        MultiThreadBean getMultiThreadBean();
    }

    @Min(1)
    private int threadCount = getDefaultThreadCount();

    public int getOptionalThreadCount() {
        return threadCount;
    }

    @Doc("Number of threads to use")
    public void setOptionalThreadCount(final int threadCount) {
        this.threadCount = threadCount;
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

        activity.log(String.format("#setup with %s thread(s)", JazzCoreHelper.createPrettyIntegerFormatter().format(nThreads)));

        return Executors.newFixedThreadPool(nThreads, new JazzThreadFactory(activity.getName(), nThreads));
    }

    protected int getDefaultThreadCount() {
        final int availableProcessors = Runtime.getRuntime().availableProcessors();

        return Math.min(4, availableProcessors);
    }
}
