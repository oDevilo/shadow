package org.devil.shadow.constants;

import java.util.HashMap;
import java.util.Map;

public class TagConstants {
    public static final String STRATEGY = "strategy";
    public static final String TABLE_STRATEGY = "tableStrategy";
    public static final String TABLE_ROUTER_CONFIG_DTD = "org/devil/shadow/config/tableStrategy-config.dtd";
    public static final Map<String, String> DOC_TYPE_MAP = new HashMap<String, String>();
    static {
        DOC_TYPE_MAP.put("http://shadow.devil.com/dtd/tableStrategy-config.dtd", TABLE_ROUTER_CONFIG_DTD);
        DOC_TYPE_MAP.put("-//shadow.devil.com//DTD shadow 2.0//EN", TABLE_ROUTER_CONFIG_DTD);
    }

    public interface TableStrategyTag {
        String NAME = "name";
        String TABLES = "tables";
        String STRATEGY_CLASS = "strategyClass";
    }

}
