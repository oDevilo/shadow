package com.devil.shadow.testshard;

import com.devil.shadow.model.TestMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by devil on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:com/devil/shadow/testshard/applicationContext-mybatis.xml"})
public class ShardTest {
    @Autowired
    private TestMapper testMapper;

    @Test
    public void testShard(){
        testMapper.selectByPrimaryKey(1L);
    }
}
