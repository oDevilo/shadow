package org.devil.shadow.testshard;

import org.devil.shadow.model.TestModel;
import org.devil.shadow.strategy.ShardStrategy;

import java.util.List;
import java.util.Map;

/**
 * Created by devil on 2017/8/16.
 */
public class MyShardStragy implements ShardStrategy {

    @Override
    public String convertDbServer(Object param) {
        if (param instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) param;
            if (((Long) map.get("id") % 2) == 1) {
                return "db1";
            } else {
                return "db2";
            }
        } else if (param instanceof TestModel) {
            TestModel model = (TestModel) param;
            if ((model.getId() % 2) == 1) {
                return "db1";
            } else {
                return "db2";
            }
        } else if (param instanceof Long) {
            if (((Long) param % 2) == 1) {
                return "db1";
            } else {
                return "db2";
            }
        }
        return "db1";
    }

    @Override
    public List<String> convertDbServers(Object param) {
        return null;
    }


}
