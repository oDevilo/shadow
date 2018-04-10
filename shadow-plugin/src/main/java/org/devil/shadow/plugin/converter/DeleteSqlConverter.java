package org.devil.shadow.plugin.converter;



import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;

public class DeleteSqlConverter extends AbstractSqlConverter {
    public DeleteSqlConverter() {
    }

    protected Statement doConvert(Statement statement, Object params, String mapperId) {
        if(!(statement instanceof Delete)) {
            throw new IllegalArgumentException("The argument statement must is instance of Delete.");
        } else {
            Delete delete = (Delete)statement;
            String name = delete.getTable().getName();
            delete.getTable().setName(this.convertTableName(name, params, mapperId));
            return delete;
        }
    }
}