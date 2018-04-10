package org.devil.shadow.router.support;

import org.apache.ibatis.session.SqlSession;
import org.devil.shadow.router.exception.ShadowSpringException;
import org.devil.shadow.router.strategy.ShardStrategy;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;

import java.util.*;

import static org.springframework.util.Assert.notNull;

/**
 * Created by devil on 2017/8/7.
 */
public class MyBatisSqlSessionDaoSupport extends DaoSupport {

    private SqlSession sqlSession;
    private ShardStrategy strategy;
    private Map<String, SqlSessionTemplate> sqlSessionTemplates;

    @Autowired(required = false)
    public final void setSqlSessionTemplate(MyBatisSqlSessionTemplate shardSqlSessionTemplate) {
        this.sqlSession = shardSqlSessionTemplate;
        this.strategy = shardSqlSessionTemplate.getStrategy();
        this.sqlSessionTemplates = shardSqlSessionTemplate.getSqlSessionTemplates();
    }

    public final SqlSession getSqlSession() {
        return this.sqlSession;
    }

    @Override
    protected void checkDaoConfig() throws IllegalArgumentException {
        notNull(this.sqlSession, "Property 'sqlSessionTemplate' are required");
    }

    /**
     * Collection对象分到对应的库
     *
     * @return
     */
    private Map<String, Collection<?>> classify(Collection<?> entities) {
        Map<String, Collection<?>> shardEntityMap = new HashMap<>();
        for (Object entity : entities) {
            String shardName = strategy.convertDbServer(entity);
            Collection shardEntities = shardEntityMap.get(shardName);
            if (shardEntities == null) {
                if (sqlSessionTemplates.get(shardName) == null) {
                    throw new ShadowSpringException("find no match template");
                }
                shardEntities = new ArrayList<>();
                shardEntityMap.put(shardName, shardEntities);
            }
            shardEntities.add(entity);
        }
        return shardEntityMap;
    }

    // TODO 批量操作，可以考虑future多线程来跑
    public int batchInsert(String statement, Collection<?> parameter) {
        Map<String, Collection<?>> classifyMap = classify(parameter);
        int count = 0;
        for (String shardName : classifyMap.keySet()) {
            SqlSessionTemplate template = sqlSessionTemplates.get(shardName);
            count += template.insert(statement, classifyMap.get(shardName));
        }
        return count;
    }

    public int batchUpdate(String statement, Collection<?> parameter) {
        Map<String, Collection<?>> classifyMap = classify(parameter);
        int count = 0;
        for (String shardName : classifyMap.keySet()) {
            SqlSessionTemplate template = sqlSessionTemplates.get(shardName);
            count += template.update(statement, classifyMap.get(shardName));
        }
        return count;
    }

    public int batchDelete(String statement, Collection<?> parameter) {
        Map<String, Collection<?>> classifyMap = classify(parameter);
        int count = 0;
        for (String shardName : classifyMap.keySet()) {
            SqlSessionTemplate template = sqlSessionTemplates.get(shardName);
            count += template.delete(statement, classifyMap.get(shardName));
        }
        return count;
    }

    public <E> List<E> selectList(String statement, Collection<?> parameter) {
        Map<String, Collection<?>> classifyMap = classify(parameter);
        List result = new ArrayList<>();
        for (String shardName : classifyMap.keySet()) {
            SqlSessionTemplate template = sqlSessionTemplates.get(shardName);
            result.addAll(template.selectList(statement, classifyMap.get(shardName)));
        }
        return result;
    }
}
