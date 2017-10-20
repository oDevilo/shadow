package org.devil.shadow.testshard;

import org.devil.shadow.model.TestModel;

import java.util.List;

/**
 * Created by devil on 2017/10/18.
 */
public interface TestDao {
    TestModel selectById(Long id);
    int batchInsert(List<TestModel> list);
    List<TestModel> selectListShards(List<Long> ids);
    int update(Long id);
}
