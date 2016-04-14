package com.zwz.android.mynews.implement.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zwz.android.mynews.base.BaseMenuDetailPager;

/**
 * 菜单详情页-专题
 * Created by 伟洲 on 2016/4/7.
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager {

    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView tv_content = new TextView(mActivity);
        tv_content.setText("专题");

        tv_content.setTextColor(Color.RED);
        tv_content.setGravity(Gravity.CENTER);
        tv_content.setTextSize(22);


        return tv_content;
    }
}
