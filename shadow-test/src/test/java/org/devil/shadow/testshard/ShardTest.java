package org.devil.shadow.testshard;

import org.devil.shadow.model.TestMapper;
import org.devil.shadow.ApplicationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by devil on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:org/devil/shadow/testshard/applicationContext-mybatis.xml"})
public class ShardTest {
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private TestDao testDao;

    @Test
    public void testShard() {
        System.out.println(testMapper.selectByPrimaryKey(1L));
        Map<String, DataSource> bean = (Map<String, DataSource>) ApplicationUtil.getBean("shardSqlSessionFactory");
        System.out.println(bean);
    }

    @Test
    public void testDao() {
        System.out.println(testDao.selectById(1L));
    }

}
