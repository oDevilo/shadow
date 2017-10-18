package org.devil.shadow.config;

import org.devil.shadow.exception.ShadowException;
import org.devil.shadow.strategy.TableStrategy;

import java.util.*;

/**
 * Created by devil on 2017/8/2.
 */
public class TabelStrategyConfigHolder {
    private static Map<String, TableStrategy> strategyRegister = new HashMap<String, TableStrategy>();
    private static Map<String, List<String>> strategyTables = new HashMap<String, List<String>>();
    private static Map<String, TableStrategy> tableStrategyRouter = new HashMap<String, TableStrategy>();

    public static void register(String name, TableStrategy strategy) {
        strategyRegister.put(name, strategy);
    }

    public static boolean existTableStrategy(String name) {
        return strategyRegister.containsKey(name);
    }

    /**
     * 查看是否存在相同的table NAME
     * @param tableName
     * @return
     */
    public static boolean existTable(String tableName) {
        for (String strategyName : strategyTables.keySet()) {
            List<String> tables = strategyTables.get(strategyName);
            if (null != tables && !tables.isEmpty() && tables.contains(tableName))
                    return true;

        }
        return false;
    }

    public static void addTables(String name, String tablesStr) {
        String[] tables = tablesStr.split(",");
        for (String table : tables) {
            if (existTable(table))
                throw new ShadowException("has same table : " + table +", in <tables> node");
        }
        List<String> tableList = Arrays.asList(tables);
        strategyTables.put(name, tableList);

        // 设置表对应的策略
        for (String table : tables) {
            tableStrategyRouter.put(table, strategyRegister.get(name));
        }
    }

    public static Map<String, TableStrategy> getStrategyRegister(){
        return strategyRegister;
    }

    public static Map<String, List<String>> getStrategyTables() {
        return strategyTables;
    }

    public static TableStrategy getTableStrategy(String tableName) {
        return tableStrategyRouter.get(tableName);
    }

}
