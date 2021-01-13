package com.qiyei.android.common.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.qiyei.android.common.R
import com.qiyei.android.common.databinding.ActivityXmlDemoBinding
import com.qiyei.android.common.xml.XmlManager
import com.qiyei.android.common.xml.XmlParserListener

class XmlDemoActivity : AppCompatActivity() {

    companion object {
        const val TAG = "XmlDemoActivity"
    }

    private lateinit var mActivityXmlDemoBinding: ActivityXmlDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityXmlDemoBinding = ActivityXmlDemoBinding.inflate(layoutInflater)
        setContentView(mActivityXmlDemoBinding.root)

        mActivityXmlDemoBinding.xmlParserTv.setOnClickListener {
            XmlManager.parseXml(resources.openRawResource(R.raw.configure),object :XmlParserListener{
                override fun onAttributeStart(root: String?) {
                    Log.i(TAG, "onAttributeStart root=$root")
                }

                override fun onAttribute(root: String?, key: String?, value: String?) {
                    Log.i(TAG, "onAttribute root=$root key=$key value=$value")
                }

                override fun onAttributeEnd(root: String?) {
                    Log.i(TAG, "onAttributeEnd root=$root")
                }
            })
        }

    }
}