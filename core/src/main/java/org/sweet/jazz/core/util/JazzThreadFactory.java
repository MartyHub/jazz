package org.sweet.jazz.core.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.ThreadFactory;

public class JazzThreadFactory implements ThreadFactory {

    private final String prefix;

    private final NumberFormat formatter;

    private int threadIndex = 0;

    public JazzThreadFactory(String prefix, final int threadCount) {
        this.prefix = prefix + '_';
        this.formatter = new DecimalFormat(JazzCoreHelper.repeat('0', String.valueOf(threadCount)
                .length()));
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, nextThreadName());
    }

    private String nextThreadName() {
        return prefix + formatter.format(++threadIndex);
    }
}
