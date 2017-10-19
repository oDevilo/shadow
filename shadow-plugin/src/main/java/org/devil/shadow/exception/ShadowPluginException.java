package org.devil.shadow.exception;

public class ShadowPluginException extends RuntimeException {

    public ShadowPluginException() {
    }

    public ShadowPluginException(String msg) {
        super(msg);
    }

    public ShadowPluginException(String msg, Throwable t) {
        super(msg, t);
    }

    public ShadowPluginException(Throwable t) {
        super(t);
    }
}