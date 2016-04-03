package com.zwz.android.mynews.implement;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zwz.android.mynews.MainActivity;
import com.zwz.android.mynews.R;
import com.zwz.android.mynews.base.BaseFragment;
import com.zwz.android.mynews.base.BaseViewPager;

import java.util.ArrayList;

/**
 * Created by 伟洲 on 2016/4/1.
 * 主页Fragment
 */
public class ContentFragment extends BaseFragment {
    private RadioGroup rb_group;
    private ArrayList<BaseViewPager> pagerArrayList;
    private ViewPager vp_content;
    private View view;

    @Override
    public View initView() {
        view = View.inflate(mActivity, R.layout.fragment_content, null);
        vp_content = (ViewPager) view.findViewById(R.id.vp_content);
        return view;
    }

    @Override
    public void initData() {
        pagerArrayList = new ArrayList<BaseViewPager>();
        pagerArrayList.add(new HomePager(mActivity));
        pagerArrayList.add(new NewsPager(mActivity));
        pagerArrayList.add(new SmartPager(mActivity));
        pagerArrayList.add(new GovernmentPager(mActivity));
        pagerArrayList.add(new SettingPager(mActivity));

        vp_content.setAdapter(new ContentAdapter());

        //监听RadioGroup的选择页面并且优化其点击效果，节省流量
        rb_group = (RadioGroup) view.findViewById(R.id.rb_group);
        rb_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        /**
                         * 参数说明
                         * vp_content.setCurrentItem(0,false);
                         * vp_content是ViewPager容器，第一个参数设置当前Pager，
                         *第二个参数是切换页面是否以滑动的动画进行
                         *
                         * pagerArrayList.get(0).initData();
                         * pagerArrayList是一个Pager的集合，得到相应的Pager后初始化数据
                         */
                        vp_content.setCurrentItem(0, false);
                        pagerArrayList.get(0).initData();

                        setSlidingMenuEnable(true);
                        break;
                    case R.id.rb_news:
                        vp_content.setCurrentItem(1, false);
                        pagerArrayList.get(1).initData();
                        setSlidingMenuEnable(false);
                        break;
                    case R.id.rb_smart:
                        vp_content.setCurrentItem(2, false);
                        pagerArrayList.get(2).initData();
                        setSlidingMenuEnable(false);
                        break;
                    case R.id.rb_zw:
                        vp_content.setCurrentItem(3, false);
                        pagerArrayList.get(3).initData();
                        setSlidingMenuEnable(false);
                        break;
                    case R.id.rb_setting:
                        vp_content.setCurrentItem(4, false);
                        pagerArrayList.get(4).initData();
                        setSlidingMenuEnable(true);
                        break;
                }
            }
        });
        //进入程序后一定初始化首页的界面
        pagerArrayList.get(0).initData();
        setSlidingMenuEnable(true);

    }

    /**
     * 侧边栏可用不可用的判断
     */
    private void setSlidingMenuEnable(boolean enable){
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        }
    }

    private class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagerArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            BaseViewPager pager = pagerArrayList.get(position);
            container.addView(pager.mRootView);

            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
