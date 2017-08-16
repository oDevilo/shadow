package org.devil.shadow.support;

import org.apache.ibatis.session.SqlSessionFactory;
import org.devil.shadow.strategy.ShardStrategy;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by devil on 2017/8/8.
 */
public class ShardDateSouceFactoryBean implements FactoryBean<Map<String, SqlSessionFactory>>, InitializingBean, DisposableBean {

    private Map<String, SqlSessionFactory> shards;
    private String shardStrategy;

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public Map<String, SqlSessionFactory> getObject() throws Exception {
        return shards;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public Map<String, SqlSessionFactory> getShards() {
        return shards;
    }

    public void setShards(Map<String, SqlSessionFactory> shards) {
        this.shards = shards;
    }

    public String getShardStrategy() {
        return shardStrategy;
    }

    public void setShardStrategy(String shardStrategy) {
        this.shardStrategy = shardStrategy;
    }
}
