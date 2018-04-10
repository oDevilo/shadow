package org.devil.shadow.test.plug;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.devil.shadow.test.model.TestModel;
import org.devil.shadow.test.model.UserModel;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by devil on 2017/6/13.
 */
public class PluginTest {
    String resource = "org/devil/shadow/test/plug/mybatis-config.xml";

    @Test
    public void testNormal() {
        //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        InputStream is = PluginTest.class.getClassLoader().getResourceAsStream(resource);
        if (null == is) {
            throw new RuntimeException("加载配置文件失败");
        }
        //构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();
        //执行查询返回一个唯一user对象的sql
        Map<String, Object> map = new HashMap<>();
        map.put("id", 101L);
        map.put("NAME", "this is ");
        // TODO 同名配置被覆盖
        List<TestModel> test = session.selectList("org.devil.shadow.test.model.TestMapper.selectByPrimaryKey", map);
        System.out.println(test);
        // obj 方式
        UserModel user = new UserModel();
        user.setId(1L);
        user.setName("this is ");
        List<UserModel> u = session.selectList("org.devil.shadow.test.model.UserMapper.selectByPrimaryKey", user);
        System.out.println(u);
    }

}
