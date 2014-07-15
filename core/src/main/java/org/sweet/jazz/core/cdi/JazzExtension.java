package org.sweet.jazz.core.cdi;

import org.jboss.weld.environment.se.StartMain;
import org.sweet.bumblebee.bean.Argument;
import org.sweet.bumblebee.bean.ArrayArgumentProvider;
import org.sweet.jazz.core.Arguments;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import javax.enterprise.util.AnnotationLiteral;
import java.util.HashSet;
import java.util.Set;

class JazzExtension implements Extension {

    private Set<ArgumentBean<?>> beans;

    private Iterable<Argument> arguments;

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery event) {
        this.beans = new HashSet<>();

        String[] args = StartMain.PARAMETERS;
        String[] arguments = null;

        if (args != null && args.length > 2) {
            arguments = new String[args.length - 2];

            System.arraycopy(args, 2, arguments, 0, args.length - 2);
        }

        this.arguments = new ArrayArgumentProvider(arguments);
    }

    @SuppressWarnings("unchecked")
    void processInjectionPoint(@Observes ProcessInjectionPoint event, BeanManager beanManager) {
        InjectionPoint injectionPoint = event.getInjectionPoint();

        if (injectionPoint.getQualifiers().contains(new AnnotationLiteral<Arguments>() {

        }) && injectionPoint.getType() instanceof Class) {
            Class<?> beanClass = (Class<?>) injectionPoint.getType();
            AnnotatedType<?> annotatedType = beanManager.createAnnotatedType(beanClass);
            InjectionTarget<?> injectionTarget = beanManager.createInjectionTarget(annotatedType);

            beans.add(new ArgumentBean(beanClass, injectionTarget, arguments));
        }
    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery event) {
        for (ArgumentBean<?> bean : beans) {
            event.addBean(bean);
        }
    }
}
