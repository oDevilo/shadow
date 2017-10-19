package com.devil.shadow.testshard;

import com.devil.shadow.model.TestModel;
import org.devil.shadow.support.MyBatisSqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by devil on 2017/10/18.
 */
@Repository
public class TestDaoImpl extends MyBatisSqlSessionDaoSupport implements TestDao {
    @Override
    public TestModel selectById(Long id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id)
;        return getSqlSession().selectOne("com.devil.shadow.model.TestMapper.selectByPrimaryKey", param);
    }
}
