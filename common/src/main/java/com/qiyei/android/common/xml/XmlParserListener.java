package com.qiyei.android.common.xml;

public interface XmlParserListener {

    void onAttributeStart(String root);

    void onAttribute(String root,String key,String value);

    void onAttributeEnd(String root);
}
