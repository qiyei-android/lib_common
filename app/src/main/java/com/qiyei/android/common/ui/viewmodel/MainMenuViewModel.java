package com.qiyei.android.common.ui.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.qiyei.android.common.entity.MainMenu;

import java.util.List;

/**
 * @author Created by qiyei2015 on 2018/8/18.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 公共列表的ViewModel
 */
public class MainMenuViewModel extends ViewModel {
    /**
     * LiveData
     */
    protected MutableLiveData<List<MainMenu>> mLiveData;

    public MainMenuViewModel() {
        mLiveData = new MutableLiveData<>();
    }

    /**
     * @return {@link #mLiveData}
     */
    public MutableLiveData<List<MainMenu>> getLiveData() {
        return mLiveData;
    }

}
