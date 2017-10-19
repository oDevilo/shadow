package org.devil.shadow.support;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.devil.shadow.exception.ShadowSpringException;
import org.devil.shadow.strategy.ShardStrategy;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;
import java.util.*;

/**
 * Created by devil on 2017/8/7.
 */
public class MyBatisSqlSessionTemplate extends AbstractUnsupportedSqlSessionTemplate implements InitializingBean, SqlSession {
    private final Log log = LogFactory.getLog(getClass());

    private Map<String, SqlSessionTemplate> sqlSessionTemplates = new HashMap<String, SqlSessionTemplate>();
    private Map<String, DataSource> shards;
    private String shardStrategy;
    private ShardStrategy strategy;
    private String[] configLocations;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            if (shards != null) {
                for (String name : shards.keySet()) {
                    MySqlSessionFactoryBean factoryBean = new MySqlSessionFactoryBean();
                    factoryBean.setConfigLocations(configLocations);
                    factoryBean.setDataSource(shards.get(name));

                    SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
                    sqlSessionTemplates.put(name, new SqlSessionTemplate(sqlSessionFactory));
                }
            }
        } catch (Exception e) {
            throw new ShadowSpringException("create sqlSessionFactory error!", e);
        }

        try {
            strategy = (ShardStrategy) Class.forName(shardStrategy).newInstance();
        } catch (Exception e) {
            throw new ShadowSpringException("create strategy error!", e);
        }

        log.debug("MyBatisSqlSessionTemplate afterPropertiesSet");
    }

    /**
     * 根据策略，找到对应的库
     *
     * @param parameter
     * @return
     */
    public SqlSessionTemplate findStrategyTemplate(Object parameter) {
        String shardName = strategy.convertDbServer(parameter);
        SqlSessionTemplate template = sqlSessionTemplates.get(shardName);
        if (template == null) {
            throw new ShadowSpringException("find no match template");
        }
        return template;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return findStrategyTemplate(parameter).selectOne(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return findStrategyTemplate(parameter).selectList(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return findStrategyTemplate(parameter).selectList(statement, parameter, rowBounds);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return findStrategyTemplate(parameter).selectMap(statement, parameter, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return findStrategyTemplate(parameter).selectMap(statement, parameter, mapKey, rowBounds);
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
        findStrategyTemplate(parameter).select(statement, parameter, handler);
    }

    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        findStrategyTemplate(parameter).select(statement, parameter, rowBounds, handler);
    }

    @Override
    public int insert(String statement, Object parameter) {
        return findStrategyTemplate(parameter).insert(statement, parameter);
    }

    @Override
    public int update(String statement, Object parameter) {
        return findStrategyTemplate(parameter).update(statement, parameter);
    }

    @Override
    public int delete(String statement, Object parameter) {
        return findStrategyTemplate(parameter).delete(statement, parameter);
    }

    public ShardStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(ShardStrategy strategy) {
        this.strategy = strategy;
    }

    public Map<String, DataSource> getShards() {
        return shards;
    }

    public void setShards(Map<String, DataSource> shards) {
        this.shards = shards;
    }

    public String getShardStrategy() {
        return shardStrategy;
    }

    public void setShardStrategy(String shardStrategy) {
        this.shardStrategy = shardStrategy;
    }

    public void setConfigLocations(String[] configLocations) {
        this.configLocations = configLocations;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocations = new String[]{configLocation};
    }

    public Map<String, SqlSessionTemplate> getSqlSessionTemplates() {
        return sqlSessionTemplates;
    }

    public void setSqlSessionTemplates(Map<String, SqlSessionTemplate> sqlSessionTemplates) {
        this.sqlSessionTemplates = sqlSessionTemplates;
    }
}
