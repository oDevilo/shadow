package org.devil.shadow.plugin.config;

import com.thoughtworks.xstream.XStream;

import java.io.InputStream;

/**
 * Created by devil on 2017/7/14.
 */
public class TableShadowConfigParser {

    public void parse(InputStream input) throws Exception {
        XStream xstream = new XStream();
        xstream.alias("tableShadow", TableShadowConfig.class);
        xstream.alias("tableStrategy", TableStrategyConfig.class);
        xstream.addImplicitArray(TableShadowConfig.class, "tableStrategys");
        xstream.useAttributeFor(TableStrategyConfig.class, "name");
        xstream.useAttributeFor(TableStrategyConfig.class, "strategyClass");
        TableShadowConfig tableShadow = (TableShadowConfig) xstream.fromXML(input);
        for (TableStrategyConfig strategyConfig : tableShadow.getTableStrategys()) {
            TabelStrategyConfigHolder.register(strategyConfig);
        }
    }
}
