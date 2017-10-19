package org.devil.shadow.testmy;

import org.devil.shadow.model.TestModel;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by devil on 2017/6/13.
 */
public class PluginTest {
    String resource = "org/devil/shadow/testmy/mybatis-config.xml";

    @Before
    public void before() {
//        PropertyConfigurator.configure("log4j.properties");
    }

    @Test
    public void test1() {
        //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        InputStream is = PluginTest.class.getClassLoader().getResourceAsStream(resource);
        if (null == is) {
            throw new RuntimeException("加载配置文件失败");
        }
//        XMLConfigBuilder xf = new XMLConfigBuilder(is);
//        System.out.println(xf);
//        Configuration conf = xf.parse();
//        System.out.println(conf);
        //构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        //使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
        //Reader reader = Resources.getResourceAsReader(resource);
        //构建sqlSession的工厂
        //SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        //创建能执行映射文件中sql的sqlSession
        Configuration configuration = sessionFactory.getConfiguration();
        System.out.println(configuration);
        SqlSession session = sessionFactory.openSession();
        /**
         * 映射sql的标识字符串，
         * me.gacl.mapping.userMapper是userMapper.xml文件中mapper标签的namespace属性的值，
         * getUser是select标签的id属性值，通过select标签的id属性值就可以找到要执行的SQL
         */
        String statement = "com.devil.shadow.model.TestModel.selectByPrimaryKey";//映射sql的标识字符串
        //执行查询返回一个唯一user对象的sql
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        List<TestModel> user = session.selectList(statement, map);
        System.out.println(user);
    }

    @Test
    public void testXml() throws Exception {
        //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        InputStream is = PluginTest.class.getClassLoader().getResourceAsStream(resource);
        if (null == is) {
            throw new RuntimeException("加载配置文件失败");
        }
        XPathParser parser = new XPathParser(is, true, null, new XMLMapperEntityResolver());
        XNode root = parser.evalNode("/configuration");
        XNode environments = root.evalNode("environments");
        EnvironmentBuilder builder = new EnvironmentBuilder();
        List<Environment> list = new ArrayList<>();
        for (XNode xNode : environments.getChildren()) {
            String id = xNode.getStringAttribute("id"); // 只要获取所有的id就能加载了
            System.out.println(id);
            list.add(builder.test(xNode));
        }
        System.out.println(root);
    }

    @Test
    public void testConf() {
        //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        InputStream is = PluginTest.class.getClassLoader().getResourceAsStream(resource);
        if (null == is) {
            throw new RuntimeException("加载配置文件失败");
        }
        //构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        System.out.println(sessionFactory);
    }


}
