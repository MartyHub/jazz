package org.sweet.jazz.core.processor;

import org.sweet.jazz.core.Arguments;
import org.sweet.jazz.core.Processor;
import org.sweet.jazz.core.ProcessorReport;
import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.UnknownWorkLoad;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ApplicationScoped
public class SampleBeanProcessor implements Processor<String> {

    @Inject
    private Activity activity;

    @Inject
    @Arguments
    private SampleBean sampleBean;

    public SampleBean getSampleBean() {
        return sampleBean;
    }

    @Override
    public ProcessorReport<String> process() throws Exception {
        UnknownWorkLoad uw = activity.start();
        ProcessorReport<String> report = ProcessorReport.success(sampleBean.getName(), sampleBean.getName());

        uw.done();

        return report;
    }
}
