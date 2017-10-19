package org.devil.shadow.support;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;

import static org.springframework.util.Assert.notNull;

/**
 * Created by devil on 2017/8/7.
 */
public class MyBatisSqlSessionDaoSupport extends DaoSupport {

    private SqlSession sqlSession;

    @Autowired(required = false)
    public final void setSqlSessionTemplate(MyBatisSqlSessionTemplate shardSqlSessionTemplate) {
        this.sqlSession = shardSqlSessionTemplate;
    }

    public final SqlSession getSqlSession() {
        return this.sqlSession;
    }

    @Override
    protected void checkDaoConfig() throws IllegalArgumentException {
        notNull(this.sqlSession, "Property 'sqlSessionTemplate' are required");
    }
}
