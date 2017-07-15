package org.devil.myshadow;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.devil.shadow.builder.ShardConfigParser;
import org.devil.shadow.plugin.ShadowPlugin;
import org.devil.shadow.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

/**
 * Created by devil on 2017/6/13.
 *
 * @Intercepts用于表明当前的对象是一个Interceptor
 * @Signature则表明要拦截的接口、方法以及对应的参数类型 type 表示拦截的类
 * method 拦截的方法
 * args 方法的参数列表
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class MyPlugin implements Interceptor {
    private static final Log log = LogFactory.getLog(MyPlugin.class);
    public static final String SHADOW_CONFIG = "shadowConfig";
    /**
     * 实现拦截逻辑的地方，内部要通过invocation.proceed()显式地推进责任链前进，也就是调用下一个拦截器拦截目标方法。
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MappedStatement mappedStatement;
        if (statementHandler instanceof RoutingStatementHandler) {
            StatementHandler delegate = (StatementHandler) ReflectionUtils.getFieldValue(statementHandler, "delegate");
            mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(delegate, "mappedStatement");
        } else {
            mappedStatement = (MappedStatement) ReflectionUtils.getFieldValue(statementHandler, "mappedStatement");
        }

        String mapperId = mappedStatement.getId();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        log.debug("Original Sql [" + mapperId + "]:" + sql);

        Object params = statementHandler.getBoundSql().getParameterObject();
        log.debug("Converted Sql [" + mapperId + "]:" + sql);
        ReflectionUtils.setFieldValue(statementHandler.getBoundSql(), "sql", sql);
        return invocation.proceed();
    }

    /**
     * 用当前这个拦截器生成对目标target的代理，实际是通过Plugin.wrap(target,this) 来完成的，
     * 把目标target和拦截器this传给了包装函数。
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 设置额外的参数，参数配置在拦截器的Properties节点里
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        String config = properties.getProperty(SHADOW_CONFIG);
        if(null != config && config.trim().length() > 0) {
            InputStream input = null;
            try {
                input = Resources.getResourceAsStream(config);
                ShadowConfigParser parser = new ShadowConfigParser();
                parser.parse(input);
            } catch (IOException e1) {
                log.error("Get shadow config file failed.", e1);
                throw new IllegalArgumentException(e1);
            } catch (Exception e5) {
                log.error("Parse shadow config file failed.", e5);
                throw new IllegalArgumentException(e5);
            } finally {
                if(input != null) {
                    try {
                        input.close();
                    } catch (IOException e1) {
                        log.error(e1.getMessage(), e1);
                    }
                }

            }

        } else {
            throw new IllegalArgumentException("property 'shadowConfig' is not found.");
        }
    }
}
