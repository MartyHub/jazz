package org.sweet.jazz.core.cdi;

import org.sweet.bumblebee.BeanArgumentsIntrospector;
import org.sweet.bumblebee.StringTransformerRegistryBuilder;
import org.sweet.bumblebee.bean.Argument;
import org.sweet.jazz.core.Arguments;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ArgumentBean<T> implements Bean<T> {

    private final Class<T> beanClass;

    private final InjectionTarget<T> injectionTarget;

    private final Iterable<Argument> arguments;

    public ArgumentBean(Class<T> beanClass, InjectionTarget<T> injectionTarget, Iterable<Argument> arguments) {
        if (beanClass == null) {
            throw new NullPointerException();
        }

        if (injectionTarget == null) {
            throw new NullPointerException();
        }

        if (arguments == null) {
            throw new NullPointerException();
        }

        this.beanClass = beanClass;
        this.injectionTarget = injectionTarget;
        this.arguments = arguments;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return injectionTarget.getInjectionPoints();
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public Set<Type> getTypes() {
        Set<Type> types = new HashSet<Type>(2);

        types.add(beanClass);
        types.add(Object.class);

        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        Set<Annotation> qualifiers = new HashSet<Annotation>(3);

        qualifiers.add(new AnnotationLiteral<Default>() {
        });
        qualifiers.add(new AnnotationLiteral<Any>() {
        });
        qualifiers.add(new AnnotationLiteral<Arguments>() {
        });

        return qualifiers;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    @Override
    public String getName() {
        return Introspector.decapitalize(beanClass.getSimpleName());
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public T create(CreationalContext<T> creationalContext) {
        T instance = injectionTarget.produce(creationalContext);

        injectionTarget.inject(instance, creationalContext);

        new BeanArgumentsIntrospector(new StringTransformerRegistryBuilder().withAll()
                .build(), beanClass).fill(instance, arguments);

        injectionTarget.postConstruct(instance);

        return instance;
    }

    @Override
    public void destroy(T instance, CreationalContext<T> creationalContext) {
        injectionTarget.preDestroy(instance);
        injectionTarget.dispose(instance);

        creationalContext.release();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ArgumentBean that = (ArgumentBean) o;

        return beanClass.equals(that.beanClass);
    }

    @Override
    public int hashCode() {
        return beanClass.hashCode();
    }
}
