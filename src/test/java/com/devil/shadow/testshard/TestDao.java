package com.devil.shadow.testshard;

import com.devil.shadow.model.TestModel;

/**
 * Created by devil on 2017/10/18.
 */
public interface TestDao {
    TestModel selectById(Long id);
}
