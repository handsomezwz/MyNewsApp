package com.zwz.android.mynews.implement.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
import com.zwz.android.mynews.MainActivity;
import com.zwz.android.mynews.R;
import com.zwz.android.mynews.base.BaseMenuDetailPager;
import com.zwz.android.mynews.domain.NewsMenuData;

import java.util.ArrayList;

/**
 * 菜单详情页-新闻
 * Created by 伟洲 on 2016/4/7.
 * ViewPagerIndicator使用流程：
 * 1.引入Library库
 * 2.布局文件中配置TabPageIndicator
 * 3.将指针和ViewPager关联起来
 * 4.重写getPageTitle方法，返回每个页面的标题（PagerAdapter）
 * 5.修改activity主题样式
 * 6.修改源码中的样式（修改图片，文字颜色）
 */
public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener{
    //这玩意好像是一定要放在最上面
    @ViewInject(R.id.vp_new_detail)
    private ViewPager mViewPager;

    @ViewInject(R.id.indicator)
    private TabPageIndicator mTabPageIndicator;

    //页签网络数据集合
    private ArrayList<NewsMenuData.NewsTabData> mTabList;
    //页签页面集合
    private ArrayList<TabDetailPager> mTabPagers;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsMenuData.NewsTabData> children) {
        super(activity);
        mTabList = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_detail_news, null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        //初始化12个页签<服务器又几个就有几个>遍历所有页签
        mTabPagers = new ArrayList<TabDetailPager>();
        for (NewsMenuData.NewsTabData tabData: mTabList){
            //创建一个页签对象
            TabDetailPager pager = new TabDetailPager(mActivity,tabData);
            mTabPagers.add(pager);
        }

        mViewPager.setAdapter(new NewsMenuAdapter());

        //mViewPager.setOnPageChangeListener(this);

        //此方法在viewPager设置完数据之后再调用
        mTabPageIndicator.setViewPager(mViewPager);/*将页面指示器和ViewPager关联起来*/
        /*当viewpager和指针绑定时，需要将页面奇幻事件监听设置给指针*/
        mTabPageIndicator.setOnPageChangeListener(this);
    }

    class NewsMenuAdapter extends PagerAdapter{

        //返回页面指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabList.get(position).title;
        }

        @Override
        public int getCount() {
            return mTabPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mTabPagers.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0){
            //开启侧边栏
            setSlidingMenuEnable(false);
        }else {
            //关闭侧边栏
            setSlidingMenuEnable(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

    //小箭头的作用，下一页
    @OnClick(R.id.iv_next_page)
    public void nextPage(View view){
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }

}
