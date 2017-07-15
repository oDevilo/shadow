package org.devil.myshadow;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.devil.shadow.strategy.ShardStrategy;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by devil on 2017/7/14.
 */
public class ShadowConfigParser {
    private static final Log log = LogFactory.getLog(ShadowConfigParser.class);
    private static final Map<String, String> DOC_TYPE_MAP = new HashMap();

    public ShadowConfigHolder parse(InputStream input) throws Exception {
        final ShadowConfigHolder configHolder = ShadowConfigHolder.getInstance();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(true);
        spf.setNamespaceAware(true);
        SAXParser parser = spf.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        DefaultHandler handler = new DefaultHandler() {
            private String parentElement;

            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//                if("dbServer".equals(qName)) {
//                    String table = attributes.getValue("tableName");
//                    String className = attributes.getValue("strategyClass");
//
//                    try {
//                        Class<?> clazz = Class.forName(className);
//                        ShardStrategy strategy = (ShardStrategy)clazz.newInstance();
//                        configHolder.register(table, strategy);
//                    } catch (ClassNotFoundException var9) {
//                        throw new SAXException(var9);
//                    } catch (InstantiationException var10) {
//                        throw new SAXException(var10);
//                    } catch (IllegalAccessException var11) {
//                        throw new SAXException(var11);
//                    }
//                }

                if("dbServer".equals(qName)) {
                    this.parentElement = qName;
                }

            }

//            public void characters(char[] ch, int start, int length) throws SAXException {
//                if("ignoreList".equals(this.parentElement)) {
//                    configHolder.addIgnoreId((new String(ch, start, length)).trim());
//                } else if("parseList".equals(this.parentElement)) {
//                    configHolder.addParseId((new String(ch, start, length)).trim());
//                }
//
//            }

            public void error(SAXParseException e) throws SAXException {
                throw e;
            }

            public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
                if(publicId != null) {
                    publicId = publicId.toLowerCase();
                }

                if(systemId != null) {
                    systemId = systemId.toLowerCase();
                }

                InputSource source = null;

                try {
                    String path = (String) ShadowConfigParser.DOC_TYPE_MAP.get(publicId);
                    source = ShadowConfigParser.this.getInputSource(path, source);
                    if(source == null) {
                        path = (String)ShadowConfigParser.DOC_TYPE_MAP.get(systemId);
                        source = ShadowConfigParser.this.getInputSource(path, source);
                    }

                    return source;
                } catch (Exception var5) {
                    throw new SAXException(var5.toString());
                }
            }
        };
        reader.setContentHandler(handler);
        reader.setEntityResolver(handler);
        reader.setErrorHandler(handler);
        reader.parse(new InputSource(input));
        return configHolder;
    }

    private InputSource getInputSource(String path, InputSource source) {
        if(path != null) {
            InputStream in = null;

            try {
                in = Resources.getResourceAsStream(path);
                source = new InputSource(in);
            } catch (IOException var5) {
                log.warn(var5.getMessage());
            }
        }

        return source;
    }
}
