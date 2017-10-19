package org.devil.shadow.converter;

import net.sf.jsqlparser.statement.Statement;

public interface SqlConverter {
    String convert(Statement statement, Object param, String mapperId);
}