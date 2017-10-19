package org.devil.shadow.testshard;

import org.devil.shadow.model.TestModel;
import org.devil.shadow.support.MyBatisSqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
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
;        return getSqlSession().selectOne("org.devil.shadow.model.TestMapper.selectByPrimaryKey", param);
    }

    @Override
    public int batchInsert(List<TestModel> list) {
        return batchInsert("org.devil.shadow.model.TestMapper.BATCH_INSERT", list);
    }

    @Override
    public List<TestModel> selectListShards(List<Long> ids) {
        return selectList("org.devil.shadow.model.TestMapper.SELECT_BY_IDS", ids);
    }

}
