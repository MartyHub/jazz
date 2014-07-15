package org.sweet.jazz.core.processor;

import org.sweet.jazz.core.Arguments;
import org.sweet.jazz.core.Processor;
import org.sweet.jazz.core.ProcessorReport;
import org.sweet.jazz.core.bean.MultiThreadBean;
import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.util.ProcessorExecutorService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.New;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Null;
import java.util.List;

@Named
@ApplicationScoped
public class MultiThreadSampleProcessor implements Processor<List<String>> {

    @Inject
    private Activity activity;

    @Inject
    @Arguments
    private MultiThreadBean multiThreadBean;

    @Inject
    @Arguments
    private MultiThreadSampleBean multiThreadSampleBean;

    @Inject
    @New
    private Instance<SampleBeanProcessor> instance;

    @Override
    public ProcessorReport<List<String>> process() throws Exception {
        ProcessorExecutorService<String> processorExecutorService = new ProcessorExecutorService<>(activity, multiThreadBean, multiThreadSampleBean.getNames().length);

        for (String name : multiThreadSampleBean.getNames()) {
            final SampleBeanProcessor processor = instance.get();

            processor.getSampleBean().setName(name);

            processorExecutorService.submit(processor);
        }

        return processorExecutorService.getReport();
    }
}
