package org.sweet.jazz.core.cdi;

import org.sweet.jazz.core.log.Activity;
import org.sweet.jazz.core.log.impl.CompositeActivity;
import org.sweet.jazz.core.log.impl.ConsoleActivity;
import org.sweet.jazz.core.log.impl.Slf4jActivity;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;

@ApplicationScoped
class ActivityProducer {

    @Produces
    Activity createActivity(InjectionPoint injectionPoint) {
        Class<?> declaringClass = injectionPoint.getMember()
                .getDeclaringClass();
        String name = declaringClass.getSimpleName();
        Named annotation = declaringClass.getAnnotation(Named.class);

        if (annotation != null && !"".equals(annotation.value())) {
            name = annotation.value();
        }

        final int index = name.indexOf("Processor");

        if (index > 0) {
            name = name.substring(0, index);
        }

        return new CompositeActivity(name, new ConsoleActivity(name), new Slf4jActivity(name));
    }
}
