package org.devil.shadow.test.plug;

import org.devil.shadow.plugin.strategy.TableStrategy;
import org.devil.shadow.test.model.TestModel;
import org.devil.shadow.test.model.UserModel;

import java.util.HashMap;

/**
 * Created by devil on 2017/8/2.
 */
public class TestStrategy implements TableStrategy {
    @Override
    public String convertTable(String tableName, Object param, String mapperId) {
        Long id;
        if (param instanceof TestModel) {
            id = ((TestModel) param).getId();
        } else if (param instanceof UserModel) {
            id = ((UserModel) param).getId();
        } else if (param instanceof HashMap) {
            id = (Long) ((HashMap) param).get("id");
        } else {
            throw new RuntimeException("类型不正确");
        }

        if (null == id)
            throw new RuntimeException("id不存在");

        return tableName + "_" + id % 4;
    }
}
