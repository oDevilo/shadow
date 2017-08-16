package com.devil.shadow.testshard;

import org.devil.shadow.strategy.ShardStrategy;

/**
 * Created by devil on 2017/8/16.
 */
public class MyShardStragy implements ShardStrategy {
    @Override
    public String convertDbServer(String tableName, Object param, String mapperId) {
        return null;
    }
}
