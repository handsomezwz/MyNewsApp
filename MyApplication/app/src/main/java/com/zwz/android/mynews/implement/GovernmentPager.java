package com.zwz.android.mynews.implement;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.zwz.android.mynews.base.BaseViewPager;

/**
 * Created by 伟洲 on 2016/4/1.
 */
public class GovernmentPager extends BaseViewPager {
    /**
     * 构造方法
     *
     * @param activity
     */
    public GovernmentPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tv_title.setText("政务");
        TextView tv_content = new TextView(mActivity);
        tv_content.setText("政务");
        tv_content.setTextColor(Color.RED);
        tv_content.setGravity(Gravity.CENTER);
        tv_content.setTextSize(22);

        flContent.addView(tv_content);


    }
}
