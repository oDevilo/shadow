package org.devil.shadow.converter;

import net.sf.jsqlparser.statement.Statement;

public interface SqlConverter {
    String convert(Statement var1, Object var2, String var3);
}