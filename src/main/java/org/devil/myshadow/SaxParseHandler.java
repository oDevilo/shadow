package org.devil.myshadow;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParseHandler extends DefaultHandler {

    // 用来存放每次遍历后的元素名称(节点名称)
    private String tagName;
    
    private DBServer server;
    
    private List<DBServer> servers;
    
 // 只调用一次 初始化list集合
    @Override
    public void startDocument() throws SAXException {
        servers = new ArrayList<DBServer>();
    }
    

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (TagConstants.dbServer.equals(qName)) {
            server = new DBServer();
        }
        this.tagName = qName;
    }

    // 调用多次
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.tagName = null;
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (null != tagName && null != server) {
            String date = new String(ch, start, length);
            if(TagConstants.DbServer.name.equals(this.tagName)) {
                server.setName(date);
            }
            if(TagConstants.DbServer.type.equals(this.tagName)) {
                server.setType(date);
            }
            if (TagConstants.DbServer.driver.equals(this.tagName)) {
                server.setDriver(date);
            }
            if(TagConstants.DbServer.url.equals(this.tagName)) {
                server.setUrl(date);
            }
            if(TagConstants.DbServer.username.equals(this.tagName)) {
                server.setUsername(date);
            }
            if(TagConstants.DbServer.password.equals(this.tagName)) {
                server.setPassword(date);
            }
        }
    }
    
}
