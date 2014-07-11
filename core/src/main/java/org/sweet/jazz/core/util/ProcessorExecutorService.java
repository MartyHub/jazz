package org.sweet.jazz.core.util;

import org.sweet.jazz.core.Processor;
import org.sweet.jazz.core.ProcessorReport;
import org.sweet.jazz.core.bean.MultiThreadBean;
import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.PredictableWorkLoad;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ProcessorExecutorService<R> {

    private final ExecutorService executorService;

    private final ExecutorCompletionService<ProcessorReport<R>> executorCompletionService;

    private final PredictableWorkLoad pw;

    private int count = 0;

    public ProcessorExecutorService(Activity activity, MultiThreadBean multiThreadBean, final int taskCount) {
        this.executorService = multiThreadBean.createExecutorService(activity, taskCount);
        this.executorCompletionService = new ExecutorCompletionService<ProcessorReport<R>>(executorService, new ArrayBlockingQueue<Future<ProcessorReport<R>>>
                (taskCount));
        this.pw = activity.start(taskCount)
                .synchronize();
    }

    public void submit(final Processor<R> processor) {
        executorCompletionService.submit(new Callable<ProcessorReport<R>>() {

            @Override
            public ProcessorReport<R> call() throws Exception {
                try {
                    ProcessorReport<R> report = processor.process();

                    pw.worked(report.getStatus()
                            .isSuccess(), report.getMessage(), report.getCause());

                    return report;
                } catch (Exception e) {
                    pw.failed(e.getMessage(), e);

                    return ProcessorReport.failure(e);
                }
            }
        });

        ++count;
    }

    public ProcessorReport<List<R>> getReport() throws InterruptedException, ExecutionException {
        executorService.shutdown();

        ProcessorReport.MergeBuilder reportBuilder = new ProcessorReport.MergeBuilder(count);

        while (count > 0) {
            final Future<ProcessorReport<R>> future = executorCompletionService.take();

            --count;

            final ProcessorReport<R> report = future.get();

            reportBuilder.report(report);
        }

        return reportBuilder.build();
    }
}
