package org.devil.shadow.support;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.devil.shadow.exception.ShadowSpringException;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Created by devil on 2017/10/19.
 */
public abstract class AbstractUnsupportedSqlSessionTemplate implements SqlSession {



    @Override
    public int update(String statement) {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public int insert(String statement) {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public void select(String statement, ResultHandler handler) {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public <E> List<E> selectList(String statement) {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public <T> T selectOne(String statement) {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public int delete(String statement) {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public void commit() {
        throw new ShadowSpringException("unsupported sql method");

    }

    @Override
    public void commit(boolean force) {
        throw new ShadowSpringException("unsupported sql method");

    }

    @Override
    public void rollback() {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public void rollback(boolean force) {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public List<BatchResult> flushStatements() {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public void close() {
        throw new ShadowSpringException("unsupported sql method");

    }

    @Override
    public void clearCache() {
        throw new ShadowSpringException("unsupported sql method");

    }

    @Override
    public Configuration getConfiguration() {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        throw new ShadowSpringException("unsupported sql method");
    }

    @Override
    public Connection getConnection() {
        throw new ShadowSpringException("unsupported sql method");
    }
}
