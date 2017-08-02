package org.devil.shadow.converter;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.StatementDeParser;
import org.devil.shadow.config.ShadowConfigHolder;
import org.devil.shadow.strategy.TableStrategy;

public abstract class AbstractSqlConverter implements SqlConverter {
    public AbstractSqlConverter() {
    }

    public String convert(Statement statement, Object params, String mapperId) {
        return this.doDeParse(this.doConvert(statement, params, mapperId));
    }

    protected String doDeParse(Statement statement) {
        StatementDeParser deParser = new StatementDeParser(new StringBuffer());
        statement.accept(deParser);
        return deParser.getBuffer().toString();
    }

    protected String convertTableName(String tableName, Object params, String mapperId) {
        TableStrategy strategy = ShadowConfigHolder.getTableStrategy(tableName);
        return strategy == null ? tableName : strategy.convertTable(tableName, params, mapperId);
    }

    protected abstract Statement doConvert(Statement var1, Object var2, String var3);
}
