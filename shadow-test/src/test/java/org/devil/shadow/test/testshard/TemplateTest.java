//package org.devil.shadow.test.testshard;
//
//import org.devil.shadow.test.model.TestModel;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * Created by devil on 2017/10/19.
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:org/devil/shadow/test/testshard/applicationContext-mybatis-template.xml"})
//public class TemplateTest {
//
//    @Autowired
//    private TestDao testDao;
//
//    @Test
//    public void test() {
//        List<TestModel> list = testDao.selectListShards(Arrays.asList(1L, 2L, 3L, 4L));
//        for (TestModel testModel : list) {
//            System.out.println(testModel);
//        }
//    }
//}
