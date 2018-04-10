package org.devil.shadow.test.testmy;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by devil on 2017/7/14.
 */
public class EnvironmentBuilder extends BaseBuilder {

    public EnvironmentBuilder(){
        super(new Configuration());
    }

    private TransactionFactory transactionManagerElement(XNode context) throws Exception {
        if (context != null) {
            String type = context.getStringAttribute("type");
            Properties props = context.getChildrenAsProperties();
            TransactionFactory factory = (TransactionFactory) resolveClass(type).newInstance();
            factory.setProperties(props);
            return factory;
        }
        throw new BuilderException("Environment declaration requires a TransactionFactory.");
    }

    private DataSourceFactory dataSourceElement(XNode context) throws Exception {
        if (context != null) {
            String type = context.getStringAttribute("type");
            Properties props = context.getChildrenAsProperties();
            DataSourceFactory factory = (DataSourceFactory) resolveClass(type).newInstance();
            factory.setProperties(props);
            return factory;
        }
        throw new BuilderException("Environment declaration requires a DataSourceFactory.");
    }

    public Environment test(XNode child) throws Exception {
            String id = child.getStringAttribute("id");
            TransactionFactory txFactory = transactionManagerElement(child.evalNode("transactionManager"));
            DataSourceFactory dsFactory = dataSourceElement(child.evalNode("dataSource"));
            DataSource dataSource = dsFactory.getDataSource();
            Environment.Builder environmentBuilder = new Environment.Builder(id).transactionFactory(txFactory).dataSource(dataSource);
            return  environmentBuilder.build();
    }

}
