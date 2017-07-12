package org.devil.shadow.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.devil.shadow.builder.ShardConfigHolder;
import org.devil.shadow.builder.ShardConfigParser;
import org.devil.shadow.converter.SqlConverterFactory;
import org.devil.shadow.util.ReflectionUtils;

/**
 * Created by devil on 2017/6/13.
 */
@Intercepts({ @Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = { Connection.class}
)})
public class ShadowPlugin implements Interceptor {
    private static final Log log = LogFactory.getLog(ShadowPlugin.class);
    public static final String SHADOW_CONFIG = "shadowConfig";
    private static final ConcurrentHashMap<String, Boolean> cache = new ConcurrentHashMap<String, Boolean>();

    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
        MappedStatement mappedStatement;
        if(statementHandler instanceof RoutingStatementHandler) {
            StatementHandler delegate = (StatementHandler) ReflectionUtils.getFieldValue(statementHandler, "delegate");
            mappedStatement = (MappedStatement)ReflectionUtils.getFieldValue(delegate, "mappedStatement");
        } else {
            mappedStatement = (MappedStatement)ReflectionUtils.getFieldValue(statementHandler, "mappedStatement");
        }

        String mapperId = mappedStatement.getId();
        if(this.isShouldParse(mapperId)) {
            String sql = statementHandler.getBoundSql().getSql();
            if(log.isDebugEnabled()) {
                log.debug("Original Sql [" + mapperId + "]:" + sql);
            }

            Object params = statementHandler.getBoundSql().getParameterObject();
            SqlConverterFactory cf = SqlConverterFactory.getInstance();
            sql = cf.convert(sql, params, mapperId);
            if(log.isDebugEnabled()) {
                log.debug("Converted Sql [" + mapperId + "]:" + sql);
            }

            ReflectionUtils.setFieldValue(statementHandler.getBoundSql(), "sql", sql);
        }
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        String config = properties.getProperty(SHADOW_CONFIG, null);
        if(config != null && config.trim().length() != 0) {
            ShardConfigParser parser = new ShardConfigParser();

            InputStream input = null;
            try {
                input = Resources.getResourceAsStream(config);
                parser.parse(input);
            } catch (IOException v1) {
                log.error("Get shadow config file failed.", v1);
                throw new IllegalArgumentException(v1);
            } catch (Exception v5) {
                log.error("Parse shadow config file failed.", v5);
                throw new IllegalArgumentException(v5);
            } finally {
                if(input != null) {
                    try {
                        input.close();
                    } catch (IOException v1) {
                        log.error(v1.getMessage(), v1);
                    }
                }

            }

        } else {
            throw new IllegalArgumentException("property 'shadowConfig' is not found.");
        }
    }

    private boolean isShouldParse(String mapperId) {
        Boolean parse = cache.get(mapperId);
        if(parse != null) {
            return parse.booleanValue();
        } else {
            if(!mapperId.endsWith("!selectKey")) {
                ShardConfigHolder configHolder = ShardConfigHolder.getInstance();
                if(!configHolder.isIgnoreId(mapperId) && (!configHolder.isConfigParseId() || configHolder.isParseId(mapperId))) {
                    parse = Boolean.valueOf(true);
                }
            }

            if(parse == null) {
                parse = Boolean.valueOf(false);
            }

            cache.put(mapperId, parse);
            return parse.booleanValue();
        }
    }
}
