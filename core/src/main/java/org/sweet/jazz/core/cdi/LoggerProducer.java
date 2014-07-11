package org.sweet.jazz.core.cdi;

import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
class LoggerProducer {

    @Produces
    Logger createLogger(InjectionPoint injectionPoint) {
        return org.slf4j.LoggerFactory.getLogger(injectionPoint.getMember()
                .getDeclaringClass());
    }
}
