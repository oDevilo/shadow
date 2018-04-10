package org.devil.shadow.plugin.converter;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;

public class UpdateSqlConverter extends AbstractSqlConverter {
    public UpdateSqlConverter() {
    }

    protected Statement doConvert(Statement statement, Object params, String mapperId) {
        if(!(statement instanceof Update)) {
            throw new IllegalArgumentException("The argument statement must is instance of Update.");
        } else {
            Update update = (Update)statement;
            String name = update.getTable().getName();
            update.getTable().setName(this.convertTableName(name, params, mapperId));
            return update;
        }
    }
}
