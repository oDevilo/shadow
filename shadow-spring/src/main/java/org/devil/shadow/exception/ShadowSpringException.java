package org.devil.shadow.exception;

public class ShadowSpringException extends RuntimeException {

    public ShadowSpringException() {
    }

    public ShadowSpringException(String msg) {
        super(msg);
    }

    public ShadowSpringException(String msg, Throwable t) {
        super(msg, t);
    }

    public ShadowSpringException(Throwable t) {
        super(t);
    }
}