package org.devil.shadow.strategy;

public interface TableStrategy {
    /**
     * 返回转换后的table NAME
     * @param tableName
     * @param param
     * @param mapperId
     * @return
     */
    String convertTable(String tableName, Object param, String mapperId);
}
