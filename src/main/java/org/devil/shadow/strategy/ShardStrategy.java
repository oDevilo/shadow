package org.devil.shadow.strategy;

/**
 * Created by devil on 2017/8/16.
 */
public interface ShardStrategy {
    /**
     * 返回转换后的DbServer NAME
     * @param tableName
     * @param param
     * @param mapperId
     * @return
     */
    String convertDbServer(String tableName, Object param, String mapperId);
}
