package org.devil.shadow.exception;

public class ShadowException extends Exception {

    public ShadowException() {
    }

    public ShadowException(String msg) {
        super(msg);
    }

    public ShadowException(String msg, Throwable t) {
        super(msg, t);
    }

    public ShadowException(Throwable t) {
        super(t);
    }
}