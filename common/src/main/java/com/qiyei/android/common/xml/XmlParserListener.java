package com.qiyei.android.common.xml;

public interface XmlParserListener {

    void onAttributeStart(String parent);

    void onAttribute(String parent,String name,String value);

    void onAttributeEnd(String parent);
}
