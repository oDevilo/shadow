package org.devil.shadow.support;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by devil on 2017/8/8.
 */
public class ShardDateSouceFactoryBean implements FactoryBean<Map<String, SqlSessionFactory>>, InitializingBean, ApplicationListener<ApplicationEvent> {

    private Map<String, SqlSessionFactory> shards;
    private Map<String, SqlSessionFactory> sessionFactorys;
    private String shardStrategy;

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

    public Map<String, SqlSessionFactory> getSessionFactorys() {
        return sessionFactorys;
    }

    public void setSessionFactorys(Map<String, SqlSessionFactory> sessionFactorys) {
        this.sessionFactorys = sessionFactorys;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

    }
}
