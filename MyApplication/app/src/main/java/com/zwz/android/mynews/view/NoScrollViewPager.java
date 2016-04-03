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
}
