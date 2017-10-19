package org.devil.shadow.testshard;

import org.devil.shadow.model.TestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by devil on 2017/10/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:org/devil/shadow/testshard/applicationContext-mybatis-datasource.xml"})
public class DatasourceTest {

    @Autowired
    private TestDao testDao;

    @Test
    public void test1() {
        TestModel m1 = new TestModel();
        m1.setId(1L);
        m1.setName("222");
        m1.setValue("222");
        TestModel m2 = new TestModel();
        m2.setId(2L);
        m2.setName("111");
        m2.setValue("111");
        TestModel m3 = new TestModel();
        m3.setId(3L);
        m3.setName("222");
        m3.setValue("222");
        TestModel m4 = new TestModel();
        m4.setId(4L);
        m4.setName("111");
        m4.setValue("111");
        List<TestModel> list = new ArrayList<>();
        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.add(m4);
        System.out.println(testDao.batchInsert(list));
    }

    @Test
    public void test2() {
        System.out.println(testDao.selectById(1L));
    }

    @Test
    public void test3() {
        List<TestModel> list = testDao.selectListShards(Arrays.asList(1L, 2L, 3L, 4L));
        for (TestModel testModel : list) {
            System.out.println(testModel);
        }
    }

}
