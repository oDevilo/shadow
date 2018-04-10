package org.devil.shadow.router.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by devil on 2017/10/20.
 */
public class ShadowMultipleException extends Throwable {

    private List<Throwable> causes = new ArrayList<Throwable>();

    public ShadowMultipleException() {
    }

    public ShadowMultipleException(List<Throwable> causes) {
        if (!(causes == null || causes.isEmpty())) this.causes.addAll(causes);
    }

    public void add(Throwable cause) {
        this.causes.add(cause);
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        for (Throwable cause : causes) {
            StringWriter sw = new StringWriter();
            cause.printStackTrace(new PrintWriter(sw));
            sb.append("\n").append(sw.toString());
        }
        return sb.toString();
    }

    public List<Throwable> getCauses() {
        return new ArrayList<Throwable>(this.causes);
    }
}
