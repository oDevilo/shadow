package org.devil.shadow.config;

/**
 * Created by devil on 2017/10/18.
 */
public class TableStrategyConfig {
    private String name;
    private String tables;
    private String strategyClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }

    public String getStrategyClass() {
        return strategyClass;
    }

    public void setStrategyClass(String strategyClass) {
        this.strategyClass = strategyClass;
    }
}
