package org.devil.shadow.support;

import org.apache.ibatis.io.Resources;
import org.devil.shadow.config.TabelStrategyConfigHolder;
import org.devil.shadow.constants.TagConstants;
import org.devil.shadow.exception.ShadowException;
import org.devil.shadow.strategy.RouterStrategy;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

public class TableStrategyParseHandler extends DefaultHandler {

    // 用来存放每次遍历后的元素名称(节点名称)
    private String tagName;
    private String strategyName;

    // 只调用一次 初始化list集合
    @Override
    public void startDocument() throws SAXException {
    }

    // 每个标签开始调用
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (TagConstants.STRATEGY.equals(qName)) {
            String name = attributes.getValue(TagConstants.TableStrategyTag.NAME);
            if (TabelStrategyConfigHolder.existTableStrategy(name))
                throw new SAXException("already exist table STRATEGY: " + name);

            this.strategyName = name;
            String className = attributes.getValue(TagConstants.TableStrategyTag.STRATEGY_CLASS);
            try {
                Class<?> clazz = Class.forName(className);
                RouterStrategy strategy = (RouterStrategy) clazz.newInstance();
                TabelStrategyConfigHolder.register(name, strategy);
            } catch (ClassNotFoundException var1) {
                throw new SAXException("table STRATEGY : " + name + " STRATEGY_CLASS Not Found Class");
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
            if (TagConstants.TableStrategyTag.TABLES.equals(this.tagName)) {
                try {
                    TabelStrategyConfigHolder.addTables(strategyName, date);
                } catch (ShadowException e) {
                    throw e;
                }
            }
        }
    }

    /**
     * 读取!DOCTYPE内容
     *
     * @param publicId
     * @param systemId
     * @return
     * @throws IOException
     * @throws SAXException
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
        InputSource source = null;
        String path = TagConstants.DOC_TYPE_MAP.get(publicId);
        source = getInputSource(path, source);
        if (source == null) {
            try {
                path = TagConstants.DOC_TYPE_MAP.get(systemId);
                source = getInputSource(path, source);
            } catch (Exception e) {
                throw new SAXException(e.toString());
            }
        }
        return source;
    }

    private InputSource getInputSource(String path, InputSource source) throws IOException {
        if (path != null) {
            InputStream in = Resources.getResourceAsStream(path);
            source = new InputSource(in);
        }
        return source;
    }
}
