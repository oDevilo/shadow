package org.devil.myshadow;

import org.devil.shadow.builder.ShardConfigHolder;

/**
 * Created by devil on 2017/7/14.
 */
public class ShadowConfigHolder {
    private static final ShadowConfigHolder instance = new ShadowConfigHolder();

    public static ShadowConfigHolder getInstance() {
        return instance;
    }
}

