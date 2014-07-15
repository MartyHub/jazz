package org.sweet.jazz.core.processor;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class SampleBean {

    @NotNull
    @NotBlank
    private String name;

    public SampleBean() {
    }

    public SampleBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
