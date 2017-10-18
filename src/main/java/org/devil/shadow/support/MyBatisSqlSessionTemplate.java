package org.devil.shadow.support;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by devil on 2017/8/7.
 */
public class MyBatisSqlSessionTemplate implements InitializingBean, SqlSession {

    protected Map<String, SqlSessionTemplate> sqlSessionTemplates = new HashMap<String, SqlSessionTemplate>();
    private Map<String, DataSource> shards;
    private String shardStrategy;

    @Override
    public void afterPropertiesSet() throws Exception {
        for (String name : shards.keySet()) {
            if (null == sqlSessionTemplates.get(name)) { // 没有找到对应的template则自己新建
                sqlSessionTemplates.put(name, new SqlSessionTemplate());
            }
        }
        
        // TODO 没有找到对应的template则自己新建
//        for (Shard shard : shards) {
//            MySqlSessionFactoryBean factoryBean = new MySqlSessionFactoryBean();
//            factoryBean.setConfigLocations(configLocations);
//            factoryBean.setDataSource(shard.getDataSource());
//            try {
//                SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
//                CURRENT_THREAD_SQLMAP_CLIENT_TEMPLATES.put(shard.getId(), new SqlSessionTemplate(sqlSessionFactory));
//            } catch (Exception e) {
//                throw new SystemErrorException("create sqlSessionFactory error!", e);
//            }
//        }
    }

    @Override
    public <T> T selectOne(String statement) {
        return null;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return null;
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return null;
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return null;
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return null;
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return null;
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {

    }

    @Override
    public void select(String statement, ResultHandler handler) {

    }

    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {

    }

    @Override
    public int insert(String statement) {
        return 0;
    }

    @Override
    public int insert(String statement, Object parameter) {
        return 0;
    }

    @Override
    public int update(String statement) {
        return 0;
    }

    @Override
    public int update(String statement, Object parameter) {
        return 0;
    }

    @Override
    public int delete(String statement) {
        return 0;
    }

    @Override
    public int delete(String statement, Object parameter) {
        return 0;
    }

    @Override
    public void commit() {

    }

    @Override
    public void commit(boolean force) {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void rollback(boolean force) {

    }

    @Override
    public List<BatchResult> flushStatements() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public void clearCache() {

    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return null;
    }

    @Override
    public Connection getConnection() {
        return null;
    }


    public Map<String, DataSource> getShards() {
        return shards;
    }

    public void setShards(Map<String, DataSource> shards) {
        this.shards = shards;
    }

    public Map<String, SqlSessionTemplate> getSqlSessionTemplates() {
        return sqlSessionTemplates;
    }

    public void setSqlSessionTemplates(Map<String, SqlSessionTemplate> sqlSessionTemplates) {
        this.sqlSessionTemplates = sqlSessionTemplates;
    }

    public String getShardStrategy() {
        return shardStrategy;
    }

    public void setShardStrategy(String shardStrategy) {
        this.shardStrategy = shardStrategy;
    }
}
