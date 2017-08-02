package com.devil.shadow.plugTable;

import com.devil.shadow.model.TestModel;
import org.devil.shadow.strategy.TableStrategy;

import java.util.HashMap;

/**
 * Created by devil on 2017/8/2.
 */
public class TestStra implements TableStrategy {
    @Override
    public String convertTable(String tableName, Object param, String mapperId) {
        Long id;
        if (param instanceof TestModel) {
            id = ((TestModel) param).getId();
        } else if (param instanceof HashMap){
            id = (Long) ((HashMap) param).get("id");
        } else {
            throw new RuntimeException("类型不正确");
        }

        if (null == id)
            throw new RuntimeException("id不存在");

        return tableName + "_" + id % 4;
    }
}
