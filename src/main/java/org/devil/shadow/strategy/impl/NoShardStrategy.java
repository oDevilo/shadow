package org.devil.shadow.strategy.impl;

import org.devil.shadow.strategy.ShardStrategy;

public class NoShardStrategy implements ShardStrategy {
    public NoShardStrategy() {
    }

    public String getTargetTableName(String baseTableName, Object params, String mapperId) {
        return baseTableName;
    }
}