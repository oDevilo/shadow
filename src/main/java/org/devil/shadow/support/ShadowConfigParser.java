package org.devil.shadow.support;

import org.devil.shadow.config.TabelStrategyConfigHolder;
import org.xml.sax.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

/**
 * Created by devil on 2017/7/14.
 */
public class ShadowConfigParser {
    private static final TableStrategyParseHandler handler = new TableStrategyParseHandler();

    public void parse(InputStream input) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setValidating(true);
        spf.setNamespaceAware(true);
        SAXParser parser = spf.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(handler);
        reader.setEntityResolver(handler);
        reader.setErrorHandler(handler);
        reader.parse(new InputSource(input));
    }

}
