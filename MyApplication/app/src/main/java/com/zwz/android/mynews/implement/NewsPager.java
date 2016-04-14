package com.zwz.android.mynews.implement;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zwz.android.mynews.MainActivity;
import com.zwz.android.mynews.base.BaseMenuDetailPager;
import com.zwz.android.mynews.base.BaseViewPager;
import com.zwz.android.mynews.domain.NewsMenuData;
import com.zwz.android.mynews.global.Contants;
import com.zwz.android.mynews.implement.menudetail.InteractMenuDetailPager;
import com.zwz.android.mynews.implement.menudetail.NewsMenuDetailPager;
import com.zwz.android.mynews.implement.menudetail.PhotoMenuDetailPager;
import com.zwz.android.mynews.implement.menudetail.TopicMenuDetailPager;
import com.zwz.android.mynews.utiles.CacheUtils;

import java.util.ArrayList;

/**
 * Created by 伟洲 on 2016/4/1.
 */
public class NewsPager extends BaseViewPager {

    private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;
    private NewsMenuData mNewsMenuData;

    /**
     * 构造方法
     *
     * @param activity
     */
    public NewsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        /*TextView tv_content = new TextView(mActivity);
        tv_content.setText("新闻");

        tv_content.setTextColor(Color.RED);
        tv_content.setGravity(Gravity.CENTER);
        tv_content.setTextSize(22);

        flContent.addView(tv_content);*/

        tv_title.setText("新闻");
        //1.首先看看本地有没有缓存
        //2.有缓存，直接加载缓存
        String cache = CacheUtils.getCacheUtils(Contants.CATEGORIES_URL, mActivity);
        if (!TextUtils.isEmpty(cache)){
            //发现有缓存
            Log.d("Cache","发现有缓存");
            processResult(cache);
        }else {
            getDataFromServer();
        }

    }

    //写一个方法从服务器获取数据
    private void getDataFromServer() {
        final HttpUtils utils = new HttpUtils();
        /**
         * 请求一个链接
         * 第一个参数是请求方式，第二个参数是UTL，第三个是callback
         */
        utils.send(HttpRequest.HttpMethod.GET, Contants.CATEGORIES_URL,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        //获取json字符串
                        String result = responseInfo.result;
                        Log.d("resultTTTTT", result);
                        //写缓存
                        CacheUtils.setCacheUtils(Contants.CATEGORIES_URL,result,mActivity);
                        processResult(result);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        e.printStackTrace();
                        Toast.makeText(mActivity, "失败了", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 解析json数据
     * @param result
     */
    protected void processResult(String result) {
        Gson gson = new Gson();
        mNewsMenuData = gson.fromJson(result, NewsMenuData.class);
        Log.d("gson", mNewsMenuData.toString());

        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        //将网络数据设置给侧边栏
        leftMenuFragment.setData(mNewsMenuData.data);

        //初始化4个菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mNewsMenuData.data.get(0).children));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotoMenuDetailPager(mActivity,ibDisplay));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
        //菜单详情页-新闻作为初始页面
        setCurrentMenuDetailPager(0);
    }

    /**
     * 给新闻中心页面的FrameLayout填充布局
     */
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);


        //移除之前所有的view对象，清理屏幕
        flContent.removeAllViews();
        flContent.addView(pager.mRootView);
        //初始化数据
        pager.initData();
        //设置切换页面的title
        tv_title.setText(mNewsMenuData.data.get(position).title);
        if (pager instanceof PhotoMenuDetailPager){
            ibDisplay.setVisibility(View.VISIBLE);
        }else {
            ibDisplay.setVisibility(View.GONE);
        }
    }


}
