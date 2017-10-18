package com.devil.shadow.dynamic;

import org.devil.shadow.strategy.ShardStrategy;

/**
 * Created by devil on 2017/10/18.
 */
public class MyStrategy implements ShardStrategy {
    @Override
    public String convertDbServer(String tableName, Object param, String mapperId) {
        return null;
    }
}
