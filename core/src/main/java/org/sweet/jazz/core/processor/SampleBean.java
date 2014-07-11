package org.sweet.jazz.core.processor;

import org.sweet.bumblebee.Optional;

public class SampleBean {

    private String name;

    public SampleBean() {
    }

    public SampleBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Optional
    public void setName(String name) {
        this.name = name;
    }
}
