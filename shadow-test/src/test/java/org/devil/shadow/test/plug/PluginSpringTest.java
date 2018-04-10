package org.devil.shadow.test.plug;

import org.devil.shadow.test.model.TestMapper;
import org.devil.shadow.test.model.TestModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author devil
 * @date 2018/4/10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/devil/shadow/test/plug/spring-mybatis.xml"})
public class PluginSpringTest {

    @Autowired
    private TestMapper testMapper;

    @Before
    public void before() {
    }

    @Test
    public void test1() {
        TestModel test = testMapper.selectByPrimaryKey(1L);
        System.out.println(test);
    }
}
