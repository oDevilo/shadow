package org.devil.shadow.support;

import org.devil.shadow.constants.TagConstants;
import org.devil.shadow.config.ShadowConfigHolder;
import org.devil.shadow.exception.ShadowException;
import org.devil.shadow.strategy.TableStrategy;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SaxParseHandler extends DefaultHandler {

    // 用来存放每次遍历后的元素名称(节点名称)
    private String tagName;
    private String strategyName;
    private TableStrategy tableStrategy;
    private List<TableStrategy> tableStrategys;

    // 只调用一次 初始化list集合
    @Override
    public void startDocument() throws SAXException {
        tableStrategys = new ArrayList<TableStrategy>();
    }

    // 每个标签开始调用
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (TagConstants.strategy.equals(qName)) {
            String name = attributes.getValue(TagConstants.TableStrategyTag.name);
            if (ShadowConfigHolder.existTableStrategy(name))
                throw new SAXException("already exist table strategy: " + name);

            this.strategyName = name;
            String className = attributes.getValue(TagConstants.TableStrategyTag.strategyClass);
            try {
                Class<?> clazz = Class.forName(className);
                TableStrategy strategy = (TableStrategy) clazz.newInstance();
                ShadowConfigHolder.register(name, strategy);
            } catch (ClassNotFoundException var1) {
                throw new SAXException("table strategy : " + name + " strategyClass Not Found Class");
            } catch (Exception e) {
                throw new SAXException("startElement error");
            }
        }
        this.tagName = qName;
    }

    // 调用多次
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.tagName = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (null != tagName && null != strategyName) {
            String date = new String(ch, start, length);
            if (TagConstants.TableStrategyTag.tables.equals(this.tagName)) {
                try {
                    ShadowConfigHolder.addTables(strategyName, date);
                } catch (ShadowException e) {
                    throw new SAXException(e);
                }
            }
        }
    }


}
