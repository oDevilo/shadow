package org.devil.shadow.config;

import java.util.List;

/**
 * Created by devil on 2017/10/18.
 */
public class TableShadowConfig {
    private List<TableStrategyConfig> tableStrategys;

    public List<TableStrategyConfig> getTableStrategys() {
        return tableStrategys;
    }

    public void setTableStrategys(List<TableStrategyConfig> tableStrategys) {
        this.tableStrategys = tableStrategys;
    }
}
