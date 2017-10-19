package com.devil.shadow.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by devil on 2017/10/18.
 */
public class ShadowDynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TestConf.testSource;
    }

}
