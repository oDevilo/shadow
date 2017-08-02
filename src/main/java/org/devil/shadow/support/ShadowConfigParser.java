package org.devil.shadow.support;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.devil.shadow.config.ShadowConfigHolder;
import org.xml.sax.*;

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
    private static final SaxParseHandler handler = new SaxParseHandler();

    public ShadowConfigHolder parse(InputStream input) throws Exception {
        final ShadowConfigHolder configHolder = ShadowConfigHolder.getInstance();
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(true);
        spf.setNamespaceAware(true);
        SAXParser parser = spf.newSAXParser();
        XMLReader reader = parser.getXMLReader();
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
