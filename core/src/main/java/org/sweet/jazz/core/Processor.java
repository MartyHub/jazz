package org.sweet.jazz.core;

public interface Processor<R> {

    ProcessorReport<R> process() throws Exception;
}
