package org.sweet.jazz.core.cdi;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

public class NamedQualifier extends AnnotationLiteral<Named> implements Named {

    private final String value;

    public NamedQualifier(String value) {
        if (value == null) {
            throw new NullPointerException();
        }

        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
