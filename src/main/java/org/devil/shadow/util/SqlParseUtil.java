package org.devil.shadow.util;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 不用进行数据库操作，而直接输出SQL，方便测试
 * 
 * @author Devil
 *
 */
public class SqlParseUtil {

    private static Configuration configuration = new Configuration();
    private String entityPackage;
    private String xmlPackage;

    public SqlParseUtil(String entityPackage, String xmlPackage) {
        this.entityPackage = entityPackage;
        this.xmlPackage = xmlPackage;
        addMapper(xmlPackage);
    }

    public void byNameSpaces(String namespace, Object param) {
        MappedStatement ms = configuration.getMappedStatement(namespace);
        BoundSql boundSql = ms.getBoundSql(param);

        System.out.println("Preparing: " + removeBreakingWhitespace(boundSql.getSql()));
        System.out.println("Parameters: " + getParameters(boundSql));
    }
    
    public <T> T byMapper(Class<T> clazz){
        T mapper = configuration.getMapper(clazz, null);
        // TODO 代理模式
        return mapper;
    }

    /**
     * 获取参数信息
     * 
     * @param boundSql
     * @return
     */
    public String getParameters(BoundSql boundSql) {
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        StringBuffer sb = new StringBuffer();
        Object parameterObject = boundSql.getParameterObject();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }

                    if (value == null) {
                        sb.append("null,");
                    } else {
                        sb.append(value + "(" + value.getClass().getSimpleName() + "),");
                    }

                }
            }
        }
        return sb.toString();
    }

    /**
     * 使用Spring的读取资源方式
     * 
     * 参考PathMatchingResourcePatternResolver
     * 
     * @throws IOException
     */
    public static void addMapper(String resourcePath) {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(resourcePath);
            int length = resources.length;
            for (int i = 0; i < length; i++) {
                // InputStream inputStream =
                // Resources.getResourceAsStream("com/ys/mybatis/StudentMapper.xml");
                // 这个是ibatis的读取方式
                InputStream inputStream = resources[i].getInputStream();
                XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resources[i].getFilename(), configuration.getSqlFragments());
                mapperParser.parse(); // 将resource读入configuration中
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过StringTokenizer去除换行空格等符号
     * 
     * @param original
     * @return
     */
    private static String removeBreakingWhitespace(String original) {
        StringTokenizer whitespaceStripper = new StringTokenizer(original);
        StringBuilder builder = new StringBuilder();
        while (whitespaceStripper.hasMoreTokens()) {
            builder.append(whitespaceStripper.nextToken());
            builder.append(" ");
        }
        return builder.toString();
    }

}
