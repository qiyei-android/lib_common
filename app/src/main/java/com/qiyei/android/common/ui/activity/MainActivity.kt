package com.qiyei.android.common.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.qiyei.android.common.R
import com.qiyei.android.common.databinding.ActivityMainBinding
import com.qiyei.android.common.entity.MainMenu
import com.qiyei.android.common.listener.OnItemClickListener
import com.qiyei.android.common.ui.adapter.MainMenuAdapter
import com.qiyei.android.common.ui.viewmodel.MainMenuViewModel
import com.qiyei.android.common.view.CategoryItemDecoration
import java.util.*

class MainActivity : AppCompatActivity() {
    private val mMenuList: MutableList<MainMenu> = ArrayList()

    private val names = arrayOf(
        "测试1 Xml Demo"
    )
    private val clazzs = arrayOf<Class<out Activity>>(
        XmlDemoActivity::class.java
    )

    /**
     * ViewModel
     */
    private lateinit var mMenuViewModel: MainMenuViewModel
    private lateinit var mMenuAdapter: MainMenuAdapter
    private lateinit var mActivityMainBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mActivityMainBinding.root)
        initView()
        initData()
    }

    private fun initView() {
        mActivityMainBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mActivityMainBinding.recyclerView.addItemDecoration(CategoryItemDecoration(getDrawable(R.drawable.recyclerview_decoration)))
        //初始化Adapter
        mMenuAdapter = MainMenuAdapter(this,mutableListOf<MainMenu>())
        mMenuAdapter.mClickListener = MyListener()
        //初始化ViewModel 2.2.0 用新方法初始化
        mMenuViewModel = ViewModelProvider(this)[MainMenuViewModel::class.java]

        mMenuViewModel.liveData.observe(
            this,
            Observer { mainMenus ->
                //update UI
                mMenuAdapter.mDatas = mainMenus
            })

        mActivityMainBinding.recyclerView.adapter = mMenuAdapter
    }

    private fun initData() {
        for (i in names.indices) {
            val menu = MainMenu(i + 1, names[i], clazzs[i])
            mMenuList.add(menu)
        }
        //主动更新数据
        mMenuViewModel.liveData.value = mMenuList
    }

    /**
     * 跳转到菜单
     * @param menu
     */
    fun gotoMenuActivity(menu: MainMenu) {
        startActivity(Intent(this,menu.clazz))
    }

    private inner class MyListener : OnItemClickListener<MainMenu> {
        override fun click(v: View, item: MainMenu, position: Int) {
            gotoMenuActivity(item)
        }
    }
}