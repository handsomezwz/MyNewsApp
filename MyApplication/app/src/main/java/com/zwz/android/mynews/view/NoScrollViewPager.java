package com.zwz.android.mynews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 伟洲 on 2016/4/2.
 */
class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //重写了ViewPager的动画滑动效果，使其不能用
        return true;
    }

    //决定事件是否中断，因为我们项目中是一个viewPager中嵌套一个viewPager
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //不拦截事件，让嵌套的viewPager有机会响应触摸事件
        return false;
    }
}
