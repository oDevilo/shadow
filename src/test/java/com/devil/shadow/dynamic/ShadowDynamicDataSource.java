package com.devil.shadow.dynamic;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.devil.shadow.strategy.ShardStrategy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by devil on 2017/10/18.
 */
public class ShadowDynamicDataSource extends AbstractRoutingDataSource {
    private static final Log log = LogFactory.getLog(ShadowDynamicDataSource.class);
    private String shardStrategy;
    private ShardStrategy strategy;

    @Override
    protected Object determineCurrentLookupKey() {
        return TestConf.testSource;
    }

    public void init() throws Exception {
        this.strategy = (ShardStrategy) Class.forName(shardStrategy).newInstance();
    }

    public void setShardStrategy(String shardStrategy) {
        this.shardStrategy = shardStrategy;
    }

    @Override
    public void afterPropertiesSet() {
        try {
            init();
            super.afterPropertiesSet();
        } catch (Exception e) {
            log.error("init ShadowDynamicDataSource error ", e);
        }
    }

}
