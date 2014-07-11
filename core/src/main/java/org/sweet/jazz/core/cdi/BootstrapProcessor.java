package org.sweet.jazz.core.cdi;

import org.jboss.weld.environment.se.bindings.Parameters;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.slf4j.Logger;
import org.sweet.bumblebee.Doc;
import org.sweet.jazz.core.Processor;
import org.sweet.jazz.core.ProcessorReport;
import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.UnknownWorkLoad;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.beans.Introspector;
import java.util.List;

@ApplicationScoped
class BootstrapProcessor implements Processor {

    @Inject
    private Logger logger;

    @Inject
    @Parameters
    private List<String> args;

    @Inject
    private Activity activity;

    @Inject
    @Any
    private Instance<Processor<?>> processors;

    @Inject
    private BeanManager beanManager;

    void start(@Observes ContainerInitialized event) {
        UnknownWorkLoad uw = activity.start();
        ProcessorReport report = null;

        try {
            report = process();
        } catch (Exception e) {
            report = ProcessorReport.failure(e.getMessage(), e);
        }

        if (report.getStatus()
                .isFailure()) {
            logger.error(report.getMessage(), report.getCause());
        }

        report.log(activity);

        uw.done();
    }

    @Override
    public ProcessorReport process() throws Exception {
        if (args == null || args.isEmpty()) {
            logUsage();

            return ProcessorReport.failure("missing mandatory first parameter");
        }

        if (args.size() < 2) {
            logUsage();
            logProcessors();

            return ProcessorReport.failure("missing mandatory second parameter");
        }

        String propertiesFileName = args.get(0);
        String processorName = args.get(1);
        Instance<Processor<?>> instance = processors.select(new NamedQualifier(processorName));

        if (instance.isUnsatisfied()) {
            instance = processors.select(new NamedQualifier(Introspector.decapitalize(processorName) + "Processor"));

            if (instance.isUnsatisfied()) {
                logProcessors();

                return ProcessorReport.failure(String.format("<%s> is unknown", processorName));
            }
        }

        Processor<?> processor = instance.get();

        return processor.process();
    }

    private void logUsage() {
        activity.log("Usage : configuration.properties Name [-option=value]*");
    }

    private void logProcessors() {
        activity.log("known names :");

        for (Bean<?> bean : beanManager.getBeans(Object.class)) {
            if (Processor.class.isAssignableFrom(bean.getBeanClass())) {
                String name = bean.getName();

                if (name != null) {
                    final int index = name.indexOf("Processor");

                    if (index > 0) {
                        name = name.substring(0, index);
                    }

                    final Doc doc = bean.getClass()
                            .getAnnotation(Doc.class);

                    if (doc == null) {
                        activity.log(name);
                    } else {
                        activity.log(String.format("%s : %s", name, doc.value()));
                    }
                }
            }
        }
    }
}
