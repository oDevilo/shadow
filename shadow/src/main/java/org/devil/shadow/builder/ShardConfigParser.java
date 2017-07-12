package org.devil.shadow.builder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.devil.shadow.strategy.ShardStrategy;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class ShardConfigParser {
    private static final Log log = LogFactory.getLog(ShardConfigParser.class);
    private static final String SHARD_CONFIG_DTD = "/shadow/src/main/java/org/devil/shadow/builder/shardbatis-config.dtd";
    private static final Map<String, String> DOC_TYPE_MAP = new HashMap();

    public ShardConfigParser() {
    }

    public ShardConfigHolder parse(InputStream input) throws Exception {
        final ShardConfigHolder configHolder = ShardConfigHolder.getInstance();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(true);
        spf.setNamespaceAware(true);
        SAXParser parser = spf.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        DefaultHandler handler = new DefaultHandler() {
            private String parentElement;

            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if("strategy".equals(qName)) {
                    String table = attributes.getValue("tableName");
                    String className = attributes.getValue("strategyClass");

                    try {
                        Class<?> clazz = Class.forName(className);
                        ShardStrategy strategy = (ShardStrategy)clazz.newInstance();
                        configHolder.register(table, strategy);
                    } catch (ClassNotFoundException var9) {
                        throw new SAXException(var9);
                    } catch (InstantiationException var10) {
                        throw new SAXException(var10);
                    } catch (IllegalAccessException var11) {
                        throw new SAXException(var11);
                    }
                }

                if("ignoreList".equals(qName) || "parseList".equals(qName)) {
                    this.parentElement = qName;
                }

            }

            public void characters(char[] ch, int start, int length) throws SAXException {
                if("ignoreList".equals(this.parentElement)) {
                    configHolder.addIgnoreId((new String(ch, start, length)).trim());
                } else if("parseList".equals(this.parentElement)) {
                    configHolder.addParseId((new String(ch, start, length)).trim());
                }

            }

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
                    String path = (String)ShardConfigParser.DOC_TYPE_MAP.get(publicId);
                    source = ShardConfigParser.this.getInputSource(path, source);
                    if(source == null) {
                        path = (String)ShardConfigParser.DOC_TYPE_MAP.get(systemId);
                        source = ShardConfigParser.this.getInputSource(path, source);
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

    static {
        DOC_TYPE_MAP.put("http://shardbatis.googlecode.com/dtd/shardbatis-config.dtd".toLowerCase(), "com/google/code/shardbatis/builder/shardbatis-config.dtd");
        DOC_TYPE_MAP.put("-//shardbatis.googlecode.com//DTD Shardbatis 2.0//EN".toLowerCase(), "com/google/code/shardbatis/builder/shardbatis-config.dtd");
    }
}
