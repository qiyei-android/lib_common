package com.qiyei.android.common.xml;

import java.io.File;
import java.io.InputStream;

public interface IXmlParser {

    /**
     * 解析xml文件
     * @param xmlFile
     */
    void parseXml(File xmlFile, XmlParserListener listener);

    /**
     * 解析xml
     * @param inputStream
     */
    void parseXml(InputStream inputStream, XmlParserListener listener);

}
