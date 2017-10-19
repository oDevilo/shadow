package org.devil.shadow.support;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import static org.springframework.util.Assert.notNull;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * Created by devil on 2017/10/18.
 */
public class MySqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
    private final Log log = LogFactory.getLog(getClass());

    private String[] configLocations;
    private DataSource dataSource;
    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();

    private Interceptor[] plugins;

    private TypeHandler<?>[] typeHandlers;

    private Resource[] mapperLocations;

    private String environment = MySqlSessionFactoryBean.class.getSimpleName();

    private String typeHandlersPackage;

    private Class<?>[] typeAliases;

    private String typeAliasesPackage;

    private Properties configurationProperties;

    private TransactionFactory transactionFactory;

    private DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();

    private boolean failFast;

    @Override
    public SqlSessionFactory getObject() throws Exception {
        if (this.sqlSessionFactory == null) {
            afterPropertiesSet();
        }

        return this.sqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(dataSource, "Property 'dataSource' is required");

        this.sqlSessionFactory = buildSqlSessionFactory();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (failFast && event instanceof ContextRefreshedEvent) {
            this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
        }
    }

    /**
     * 主要是对mybatis配置文件的一些合并和处理
     * @return
     * @throws IOException
     */
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {

        Configuration configuration;
        XMLConfigBuilder xmlConfigBuilder = null;

        if (this.configLocations != null) {
            Document document = this.SQLConfigMap();
            Element root = document.getRootElement();
            Element elementMapper = root.element("mappers");
            Element elementTypeAlias = root.element("typeAliases");

            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            for (String configLocation : configLocations) {
                Resource[] resources = resourcePatternResolver.getResources(configLocation);

                for (Resource resource : resources) {
                    readXML(resource, elementTypeAlias, elementMapper);
                }
            }

            log.debug(document.asXML());

            InputStream inputSteam = new ByteArrayInputStream(document.asXML().getBytes());
            xmlConfigBuilder = new XMLConfigBuilder(inputSteam, null, this.configurationProperties);
            configuration = xmlConfigBuilder.getConfiguration();
            if (inputSteam != null) {
                inputSteam.close();
            }
        } else {
            this.log.debug("Property 'configLocation' not specified,using default MyBatis Configuration ");
            configuration = new Configuration();
            configuration.setVariables(this.configurationProperties);
        }

        if (hasLength(this.typeAliasesPackage)) {
            String[] typeAliasPackageArray = tokenizeToStringArray(this.typeAliasesPackage,
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String packageToScan : typeAliasPackageArray) {
                configuration.getTypeAliasRegistry().registerAliases(packageToScan);
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Scanned package: '" + packageToScan + "' for aliases");
                }
            }
        }

        if (!isEmpty(this.typeAliases)) {
            for (Class<?> typeAlias : this.typeAliases) {
                configuration.getTypeAliasRegistry().registerAlias(typeAlias);
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Registered type alias: '" + typeAlias + "'");
                }
            }
        }

        if (!isEmpty(this.plugins)) {
            for (Interceptor plugin : this.plugins) {
                configuration.addInterceptor(plugin);
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Registered plugin: '" + plugin + "'");
                }
            }
        }

        if (StringUtils.hasLength(this.typeHandlersPackage)) {
            String[] typeHandlersPackageArray = tokenizeToStringArray(this.typeHandlersPackage,
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String packageToScan : typeHandlersPackageArray) {
                configuration.getTypeHandlerRegistry().register(packageToScan);
                this.log.debug("Scanned package: '" + packageToScan + "' for type handlers");
            }
        }

        if (!isEmpty(this.typeHandlers)) {
            for (TypeHandler<?> typeHandler : this.typeHandlers) {
                configuration.getTypeHandlerRegistry().register(typeHandler);
                this.log.debug("Registered type handler: '" + typeHandler + "'");
            }
        }

        if (xmlConfigBuilder != null) {
            try {
                xmlConfigBuilder.parse();

                this.log.debug("Parsed configuration file: '" + this.configLocations + "'");
            } catch (Exception ex) {
                throw new NestedIOException("Failed to parse config resource: " + this.configLocations, ex);
            } finally {
                ErrorContext.instance().reset();
            }
        }

        if (this.transactionFactory == null) {
            this.transactionFactory = new SpringManagedTransactionFactory();
        }

        Environment environment = new Environment(this.environment, this.transactionFactory, this.dataSource);
        configuration.setEnvironment(environment);

        if (this.databaseIdProvider != null) {
            try {
                configuration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
            } catch (SQLException e) {
                throw new NestedIOException("Failed getting a databaseId", e);
            }
        }

        if (!isEmpty(this.mapperLocations)) {
            for (Resource mapperLocation : this.mapperLocations) {
                if (mapperLocation == null) {
                    continue;
                }

                try {
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(),
                            configuration, mapperLocation.toString(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                } catch (Exception e) {
                    throw new NestedIOException("Failed to parse mapping resource: '" +
                            mapperLocation + "'", e);
                } finally {
                    ErrorContext.instance().reset();
                }

                this.log.debug("Parsed mapper file: '" + mapperLocation + "'");
            }
        } else {
            this.log.debug("Property 'mapperLocations' was not specified orno matching resources found");
        }

        return this.sqlSessionFactoryBuilder.build(configuration);
    }


    /**
     * 合并mybatis配置文件
     */
    public Document SQLConfigMap() {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");
        DocumentFactory documentFactory = new DocumentFactory();
        DocumentType docType = documentFactory.createDocType("configuration",
                "-//mybatis.org//DTD Config 3.0//EN", "http://mybatis.org/dtd/mybatis-3-config.dtd");
        doc.setDocType(docType);
        Element rootElement = doc.addElement("configuration");
        rootElement.addElement("typeAliases");
        rootElement.addElement("mappers");
        return doc;
    }

    /**
     * 读取配置文件
     *
     * @param configXML
     * @param elementTypeAlias
     * @param elementMapper
     */
    public void readXML(Resource configXML, final Element elementTypeAlias, final Element elementMapper) {

        SAXReader saxReader = new SAXReader();
        // 对dom4j的SAXReader进行设置，不去下载外部dtd文件来对xml进行验证
        saxReader.setValidation(false);
        try {
            saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        // 合并typeAliases
        saxReader.addHandler("/configuration/typeAliases/typeAlias", new ElementHandler() {

            @Override
            public void onEnd(ElementPath path) {
                Element row = path.getCurrent();
                Element els = elementTypeAlias.addElement("typeAlias");
                els.addAttribute("alias", row.attributeValue("alias")).addAttribute("type",
                        row.attributeValue("type"));
                row.detach();

            }

            @Override
            public void onStart(ElementPath arg0) {
            }
        });
        // 合并mapper
        saxReader.addHandler("/configuration/mappers/mapper", new ElementHandler() {

            @Override
            public void onEnd(ElementPath path) {
                Element row = path.getCurrent();
                Element els = elementMapper.addElement("mapper");
                String mapper = row.attributeValue("mapper");
                String resource = row.attributeValue("resource");
                els.addAttribute("mapper", mapper);
                els.addAttribute("resource", resource);
                row.detach();
            }

            @Override
            public void onStart(ElementPath arg0) {

            }
        });

        try {
            saxReader.read(configXML.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Interceptor[] getPlugins() {
        return plugins;
    }

    public void setPlugins(Interceptor[] plugins) {
        this.plugins = plugins;
    }

    public TypeHandler<?>[] getTypeHandlers() {
        return typeHandlers;
    }

    public void setTypeHandlers(TypeHandler<?>[] typeHandlers) {
        this.typeHandlers = typeHandlers;
    }

    public String getTypeHandlersPackage() {
        return typeHandlersPackage;
    }

    public void setTypeHandlersPackage(String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    public Class<?>[] getTypeAliases() {
        return typeAliases;
    }

    public void setTypeAliases(Class<?>[] typeAliases) {
        this.typeAliases = typeAliases;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public String[] getConfigLocations() {
        return configLocations;
    }

    public void setConfigLocations(String[] configLocations) {
        this.configLocations = configLocations;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocations = configLocation != null ? new String[]{configLocation} : null;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy) {
            // 如果是TransactionAwareDataSourceProxy，则需要执行目标数据源
            this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        } else {
            this.dataSource = dataSource;
        }
    }


    public Resource[] getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Properties getConfigurationProperties() {
        return configurationProperties;
    }

    public void setConfigurationProperties(Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public DatabaseIdProvider getDatabaseIdProvider() {
        return databaseIdProvider;
    }

    public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
        this.databaseIdProvider = databaseIdProvider;
    }

    public boolean isFailFast() {
        return failFast;
    }

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }
}
