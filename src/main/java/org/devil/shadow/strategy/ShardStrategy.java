package org.devil.shadow.strategy;

import java.util.List;

/**
 * Created by devil on 2017/8/16.
 */
public interface ShardStrategy {
    /**
     * 返回转换后的DbServer NAME
     * @param param
     * @return
     */
    String convertDbServer(Object param);

    /**
     * 返回转换后的DbServers NAME
     * @param param
     * @return
     */
    List<String> convertDbServers(Object param);
}
