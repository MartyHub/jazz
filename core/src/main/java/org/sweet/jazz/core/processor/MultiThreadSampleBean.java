package org.sweet.jazz.core.processor;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class MultiThreadSampleBean {

    @NotNull
    @NotEmpty
    private String[] names;

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }
}
