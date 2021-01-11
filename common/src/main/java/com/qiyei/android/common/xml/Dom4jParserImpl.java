package com.qiyei.android.common.xml;

import android.util.Log;



import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @author Created by qiyei2015 on 2017/12/31.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public class Dom4jParserImpl implements IXmlParser{

    private static final String TAG = "DOM4J";

    /**
     * 解析xml文件
     * @param xmlFile
     */
    public void parseXml(File xmlFile, XmlParserListener listener){
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(xmlFile);
            Element element = document.getRootElement();
            parserNode(element,"",listener);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析xml
     * @param inputStream
     */
    public void parseXml(InputStream inputStream, XmlParserListener listener){
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            Element element = document.getRootElement();
            parserNode(element,"", listener);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归调用，即可解析xml
     * @param root
     */
    private void parserNode(Element root, String prefix,XmlParserListener listener){
        if (root == null){
            return;
        }
        listener.onAttributeStart(root.getName());
        Log.i(TAG,prefix +"name:" + root.getName());
        //获取所有的属性
        List<Attribute> attrs = root.attributes();
        if (attrs != null && attrs.size() > 0){
            for (Attribute attribute : attrs){
                listener.onAttribute(root.getName(),attribute.getName(),attribute.getValue());
                Log.i(TAG,"attribute name:" + attribute.getName() + " value:" + attribute.getValue());
            }
        }
        listener.onAttributeEnd(root.getName());
        prefix += "\t";
        List<Element> childNodes = root.elements();
        for (Element element : childNodes){
            parserNode(element,prefix,listener);
        }
    }

}
