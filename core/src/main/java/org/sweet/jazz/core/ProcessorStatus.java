package org.sweet.jazz.core;

public enum ProcessorStatus {

    // keep order
    OK, ERROR, CANCEL;

    public boolean isSuccess() {
        return OK == this;
    }

    public boolean isFailure() {
        return ERROR == this;
    }

    public boolean isCancel() {
        return CANCEL == this;
    }
}
