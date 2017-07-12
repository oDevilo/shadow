package org.devil.shadow.converter;

import org.devil.shadow.builder.ShardConfigHolder;
import org.devil.shadow.strategy.ShardStrategy;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

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
        ShardConfigHolder configFactory = ShardConfigHolder.getInstance();
        ShardStrategy strategy = configFactory.getStrategy(tableName);
        return strategy == null?tableName:strategy.getTargetTableName(tableName, params, mapperId);
    }

    protected abstract Statement doConvert(Statement var1, Object var2, String var3);
}
