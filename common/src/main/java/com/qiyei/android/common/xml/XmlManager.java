package com.qiyei.android.common.xml;


import java.io.File;
import java.io.InputStream;

public class XmlManager {


    /**
     * 解析xml文件
     * @param xmlFile
     */
    public static void parseXml(File xmlFile,XmlParserListener listener){
        IXmlParser parser = new Dom4jParserImpl();
        parser.parseXml(xmlFile,listener);
    }

    /**
     * 解析xml
     * @param inputStream
     */
    public static void parseXml(InputStream inputStream,XmlParserListener listener){
        IXmlParser parser = new Dom4jParserImpl();
        parser.parseXml(inputStream,listener);
    }

}
