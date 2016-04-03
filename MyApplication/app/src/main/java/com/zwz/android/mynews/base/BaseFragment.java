package com.zwz.android.mynews.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 伟洲 on 2016/3/31.
 * Fragment基类
 * 1.初始化布局initView
 * 2.初始化数据initData
 */
public abstract class BaseFragment extends Fragment {

    //mActivity就是我们的根Activity，这里就是MainActivity
    public Activity mActivity;
    //Fragment被创建
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取所在的Activity对象
        mActivity = getActivity();
    }

    //初始化Fragment布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据，子类可以不实现
     */
    public void initData() {
    }

    /**
     * 初始化布局,子类必须实现
     */
    public abstract View initView();
}
