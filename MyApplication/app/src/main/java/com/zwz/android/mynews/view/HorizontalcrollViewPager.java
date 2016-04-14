package com.zwz.android.mynews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 伟洲 on 2016/4/12.
 * 分情况决定父控件是否需要拦截事件
 * 1.上下滑动拦截
 * 2.向右滑动并且第一个页面，拦截
 * 3.向左滑动并且最后一个页面，拦截
 */
public class HorizontalcrollViewPager extends ViewPager {

    private int startY;
    private int startX;
    private int endY;
    private int endX;

    public HorizontalcrollViewPager(Context context) {
        super(context);
    }

    public HorizontalcrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求父控件及祖宗不要拦截事件了
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = (int) ev.getX();
                endY = (int) ev.getY();

                int dx = endX - startX;
                int dy = endY - startY;

                if (Math.abs(dx)>Math.abs(dy)){
                    //左右滑动
                    if (dx > 0){
                        //向右滑动
                        if (this.getCurrentItem() == 0){
                            //第一个页面
                            //请求父控件及祖宗控件拦截时间
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }else {
                        //向左滑动
                        if ( getCurrentItem()== this.getAdapter().getCount()-1){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }
                }else {
                    //上下滑动
                    //请求父控件及祖宗控件不要拦截事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
