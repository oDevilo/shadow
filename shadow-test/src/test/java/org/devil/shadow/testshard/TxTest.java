package org.devil.shadow.testshard;

import org.devil.shadow.ApplicationUtil;
import org.devil.shadow.model.TestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by devil on 2017/10/19.
 */
@Service
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:org/devil/shadow/testshard/applicationContext-mybatis-tx.xml"})
public class TxTest {

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
        list.add(m2);
        list.add(m4);
        list.add(m1);
        list.add(m3);

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

    @Test
    public void update() {
        testDao.update(1L);
    }


    // 使用@Test测试总是db2的插入成功db1的失败，因为代码使用map会对key排序，spring-test来做的rollback
    @Test
    @Transactional
    public void testTx() {
        System.out.println(ApplicationUtil.getBeanByType(TxTest.class));
        test1();
        update();
        if (true) {
            throw new RuntimeException();
        }
    }

    // 使用main方法测试，db生成事务的规则和配置有关，先加入配置的会有事务，因为在获取事务的时候，只获取一个连接做了connnection.setAutoCommit(false);
    public static void main(String[] args) {
        // 通过手动加载spring配置文件的方式获取bean
        ApplicationContext context = new ClassPathXmlApplicationContext("org/devil/shadow/testshard/applicationContext-mybatis-tx.xml");
        TxTest test = ApplicationUtil.getBeanByType(TxTest.class);
        test.testTx();
    }

}
