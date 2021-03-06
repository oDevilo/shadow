package org.devil.shadow.plugin.converter;



import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

public class InsertSqlConverter extends AbstractSqlConverter {
    public InsertSqlConverter() {
    }

    protected Statement doConvert(Statement statement, Object params, String mapperId) {
        if(!(statement instanceof Insert)) {
            throw new IllegalArgumentException("The argument statement must is instance of Insert.");
        } else {
            Insert insert = (Insert)statement;
            String name = insert.getTable().getName();
            insert.getTable().setName(this.convertTableName(name, params, mapperId));
            return insert;
        }
    }
}
