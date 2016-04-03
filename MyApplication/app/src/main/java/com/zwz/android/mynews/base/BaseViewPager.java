package com.zwz.android.mynews.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zwz.android.mynews.R;

/**
 * Created by 伟洲 on 2016/4/1.
 */
public abstract class BaseViewPager {
    public Activity mActivity;
    public TextView tv_title;
    public View mRootView;
    public ImageButton btn_menu;
    public FrameLayout flContent;

    /**
     * 构造方法
     */
    public BaseViewPager(Activity activity){
        mActivity = activity;
        initView();
    }

    /**
     * 初始化页面
     */
    public void initView() {
        mRootView = View.inflate(mActivity, R.layout.base_page, null);
        tv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
        btn_menu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
    }

    /**
     * 初始化数据
     */
    public abstract void initData();
}
