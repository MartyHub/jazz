package org.sweet.jazz.core;

import org.sweet.jazz.core.log.Work;
import org.sweet.jazz.core.util.JazzCoreHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessorReport<R> {

    public static final ProcessorReport success(String message) {
        return new ProcessorReport(ProcessorStatus.OK, message, null, null);
    }

    public static final <R> ProcessorReport<R> success(String message, R result) {
        return new ProcessorReport<R>(ProcessorStatus.OK, message, result, null);
    }

    public static final ProcessorReport cancel(String message) {
        return new ProcessorReport(ProcessorStatus.CANCEL, message, null, null);
    }

    public static final ProcessorReport failure(String message) {
        return new ProcessorReport(ProcessorStatus.ERROR, message, null, null);
    }

    public static final ProcessorReport failure(String message, Exception cause) {
        return new ProcessorReport(ProcessorStatus.ERROR, message, null, cause);
    }

    public static final ProcessorReport failure(Exception cause) {
        if (cause == null) {
            throw new NullPointerException();
        }

        return new ProcessorReport(ProcessorStatus.ERROR, cause.getMessage(), null, cause);
    }

    public static final MergeBuilder merge(final int size) {
        return new MergeBuilder(size);
    }

    public static class MergeBuilder<T> {

        private Map<ProcessorStatus, AtomicInteger> statusCount;

        private final List<T> results;

        private Exception firstException;

        public MergeBuilder(final int size) {
            this.results = new ArrayList<T>(size);
            this.statusCount = new HashMap<ProcessorStatus, AtomicInteger>(size);

            for (ProcessorStatus status : ProcessorStatus.values()) {
                statusCount.put(status, new AtomicInteger());
            }
        }

        public ProcessorReport<List<T>> build() {
            ProcessorStatus status = ProcessorStatus.OK;
            final int errorCount = statusCount.get(ProcessorStatus.ERROR)
                    .get();

            if (statusCount.get(ProcessorStatus.CANCEL)
                    .get() > 0) {
                status = ProcessorStatus.CANCEL;
            } else if (errorCount > 0) {
                status = ProcessorStatus.ERROR;
            }

            NumberFormat prettyIntegerFormatter = JazzCoreHelper.createPrettyIntegerFormatter();
            String message = String.format("%s error(s) / %s task(s)", prettyIntegerFormatter.format(errorCount), prettyIntegerFormatter.format(results.size()));

            return new ProcessorReport<List<T>>(status, message, results, firstException);
        }

        public MergeBuilder report(ProcessorReport<T> report) {
            results.add(report.getResult());
            statusCount.get(report.getStatus())
                    .incrementAndGet();

            if (firstException == null) {
                firstException = report.getCause();
            }

            return this;
        }
    }

    private final ProcessorStatus status;

    private final String message;

    private final R result;

    private final Exception cause;

    private ProcessorReport(ProcessorStatus status, String message, R result, Exception cause) {
        if (status == null) {
            throw new NullPointerException();
        }

        this.status = status;
        this.message = message;
        this.result = result;
        this.cause = cause;
    }

    public ProcessorStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public R getResult() {
        return result;
    }

    public Exception getCause() {
        return cause;
    }

    public void log(Work work) {
        if (cause != null) {
            cause.printStackTrace();
        }

        work.log(toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(status);

        if (message != null) {
            sb.append(" : ");
            sb.append(message);
        }

        if (cause != null && cause.getMessage() != null && !cause.getMessage()
                .equals(message)) {
            sb.append(" (");
            sb.append(cause.getMessage());
            sb.append(")");
        }

        return sb.toString();
    }
}
