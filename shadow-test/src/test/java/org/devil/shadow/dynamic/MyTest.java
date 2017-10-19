package org.devil.shadow.dynamic;

import org.devil.shadow.model.TestMapper;
import org.devil.shadow.model.TestModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:org/devil/shadow/dynamic/applicationContext-mybatis.xml"})
public class MyTest {

    @Autowired
    private TestMapper testMapper;

    @Before
    public void before() {
    }

    @Test
    public void test1() {
//        TestConf.testSource = "less";
        TestModel test = testMapper.selectByPrimaryKey(1L);
        System.out.println(test);
    }
}
