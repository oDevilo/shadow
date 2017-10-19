package com.devil.shadow.testshard;

import org.devil.shadow.strategy.ShardStrategy;

import java.util.List;

/**
 * Created by devil on 2017/8/16.
 */
public class MyShardStragy implements ShardStrategy {

    @Override
    public String convertDbServer(Object param) {
        return "db2";
    }

    @Override
    public List<String> convertDbServers(Object param) {
        return null;
    }
}
