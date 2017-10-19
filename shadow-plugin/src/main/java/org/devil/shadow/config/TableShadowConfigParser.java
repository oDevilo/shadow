package org.devil.shadow.config;

import com.thoughtworks.xstream.XStream;
import org.devil.shadow.strategy.TableStrategy;

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
        TableShadowConfig tableShadow = (TableShadowConfig) xstream.fromXML(input);
        for (TableStrategyConfig strategyConfig : tableShadow.getTableStrategys()) {
            Class<?> clazz = Class.forName(strategyConfig.getStrategyClass());
            TableStrategy strategy = (TableStrategy) clazz.newInstance();
            TabelStrategyConfigHolder.register(strategyConfig.getName(), strategy);
        }
    }
}
