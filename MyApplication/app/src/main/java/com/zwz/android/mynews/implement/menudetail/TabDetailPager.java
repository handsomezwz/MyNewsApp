package com.zwz.android.mynews.implement.menudetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.zwz.android.mynews.MainActivity;
import com.zwz.android.mynews.NewsDatailActivity;
import com.zwz.android.mynews.R;
import com.zwz.android.mynews.base.BaseMenuDetailPager;
import com.zwz.android.mynews.domain.NewsData;
import com.zwz.android.mynews.domain.NewsMenuData;
import com.zwz.android.mynews.global.Contants;
import com.zwz.android.mynews.utiles.CacheUtils;
import com.zwz.android.mynews.utiles.PreSharedPreferences;
import com.zwz.android.mynews.view.RefreshListView;

import java.util.ArrayList;

/**
 * Created by 伟洲 on 2016/4/10.
 * 12个页签的页面对象封装
 */
public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenuData.NewsTabData mTabDatal;
    //    private TextView view;
    @ViewInject(R.id.vp_tab_detail)
    private ViewPager mViewPager;
    //ListView
    @ViewInject(R.id.lv_tab_detail)
    private RefreshListView mListView;
    //指示器
    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;

    private String mUrl;
    private NewsData mNewsTabData;
    private ArrayList<NewsData.NewsTab.TopNews> mTopNewsList;
    private ArrayList<NewsData.NewsTab.News> mNewsList;

    private String mMoreUrl;//下一页的链接
    //适配器
    private NewsAdapter mNewsAdapter;

    private Handler mHandler = null;

    /**
     * 自己写的ListView
     * private TextView lv_tv_title;
     * private TextView lv_tv_des;
     * private ImageView lv_iv_icon;
     */

    public TabDetailPager(Activity activity, NewsMenuData.NewsTabData tabData) {
        super(activity);
        mTabDatal = tabData;
        mUrl = Contants.SERVER_URL + mTabDatal.url;
    }

    @Override
    public View initView() {
       /* view = new TextView(mActivity);
        view.setText("页签");
        view.setGravity(Gravity.CENTER);
        return view;*/
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);

        View header = View.inflate(mActivity, R.layout.list_header, null);
        ViewUtils.inject(this, header);

        //给listView添加头布局
        mListView.addHeaderView(header);

        //设置下拉刷新的监听
        mListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //从网络加载数据
                getDataFromServer();

            }

            @Override
            public void loadMore() {
                //加载更多数据
                if (mMoreUrl != null) {
                    Log.d("loadMore", "加载下一页数据....");
                    getMoreDataFromServer();
                } else {
                    mListView.OnRefreshComplete(false);
                    Toast.makeText(mActivity, "没有更多了哦", Toast.LENGTH_SHORT).show();

                }

            }
        });

        //条目点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("点击的位置", "" + position);
                //当前点击的item的标题颜色设置为灰色
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);

                //将已读状态持久化到本地
                //key:read_ids; value:1324,1325,1326
                String readIds = PreSharedPreferences.getString("read_ids", "", mActivity);
                if (!readIds.contains(mNewsList.get(position).id)) {
                    readIds = readIds + mNewsList.get(position).id + ",";
                    PreSharedPreferences.putString("read_ids", readIds, mActivity);
                }
                Intent intent = new Intent(mActivity, NewsDatailActivity.class);
                intent.putExtra("url", mNewsList.get(position).url);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void initData() {
//        view.setText(mTabDatal.title);
        String cache = CacheUtils.getCacheUtils(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processResult(cache, false);
        }
        getDataFromServer();


    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processResult(result, false);
                CacheUtils.setCacheUtils(mUrl, result, mActivity);

                //收起刷新控件
                mListView.OnRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //收起刷新控件
                mListView.OnRefreshComplete(false);
                Toast.makeText(mActivity, "刷新失败咯", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processResult(String result, boolean isMore) {
        Gson gson = new Gson();
        mNewsTabData = gson.fromJson(result, NewsData.class);

        //Log.d("Gson:", "News解析结果" + mNewsTabData);


        if (TextUtils.isEmpty(mNewsTabData.data.more)) {
            //没有下一页了
            mMoreUrl = null;
        } else {
            //初始化下一页
            mMoreUrl = Contants.SERVER_URL + mNewsTabData.data.more;
            Log.d("mMoreUrl-----", mMoreUrl);
        }


        if (!isMore) {
            //初始化头条新闻
            mTopNewsList = mNewsTabData.data.topnews;

            //这样写更加安全
            if (mTopNewsList != null) {
                TopNewsAdapter TopNewsAdapter = new TopNewsAdapter();
                mViewPager.setAdapter(TopNewsAdapter);
                //指示器与ViewPager绑定，绑定ViewPager一定要配好适配器
                mIndicator.setViewPager(mViewPager);
                mIndicator.setSnap(true);
                //将小圆点位置归零，解决它会在页面销毁时仍然记录上次位置的bug
                mIndicator.onPageSelected(0);

                mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        //拿到一个TopNews对象
                        NewsData.NewsTab.TopNews topNews = mTopNewsList.get(position);
                        tv_title.setText(topNews.title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                //初始化第一页标题
                tv_title.setText(mTopNewsList.get(0).title);
            }

            //初始化ListView
            mNewsList = mNewsTabData.data.news;
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                mListView.setAdapter(mNewsAdapter);
            }

            //避免总是发送
            if (mHandler == null){
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = mViewPager.getCurrentItem();
                        if (currentItem<mTopNewsList.size()-1){
                            currentItem++;
                        }else {
                            currentItem=0;
                        }
                        mViewPager.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0, 2000);
                        //Log.d("mHandle","开始广告咯");
                    }
                };
                //延时2秒切换广告条
                mHandler.sendEmptyMessageDelayed(0,2000);

                //按住ViewPager他不动
                mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                //删除所有消息
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_CANCEL:
                                //事件取消

                            case MotionEvent.ACTION_UP:
                                //延时2秒切换广告条
                                mHandler.sendEmptyMessageDelayed(0,2000);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }

        } else {
            //加载更多
            ArrayList<NewsData.NewsTab.News> moreData = mNewsTabData.data.news;
            //追加数据
            mNewsList.addAll(moreData);
            //刷新ListView
            mNewsAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 加载更多数据
     */
    public void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processResult(result, true);
                //收起刷新控件
                mListView.OnRefreshComplete(false);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(mActivity, "加载失败了", Toast.LENGTH_SHORT).show();
                //收起刷新控件
                mListView.OnRefreshComplete(false);
            }
        });
    }

    //ListView适配器
    class NewsAdapter extends BaseAdapter {

        public BitmapUtils bitmapUtils;

        public NewsAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsData.NewsTab.News getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /**这个是自己写的，没有考虑用到convertView的用法
             View view = View.inflate(mActivity, R.layout.list_item_news, null);
             lv_tv_title = (TextView) view.findViewById(R.id.tv_title);
             lv_iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
             lv_tv_des = (TextView)view.findViewById(R.id.tv_des);

             lv_tv_des.setText(mNewsList.get(position).pubdate);
             lv_tv_title.setText(mNewsList.get(position).title);
             bitmapUtils.display(lv_iv_icon,mNewsList.get(position).listimage);

             return view;
             */
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                holder = new ViewHolder();
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_des);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                //不加下面那句会报空指针的错误
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            NewsData.NewsTab.News news = getItem(position);
            holder.tvDate.setText(news.pubdate);
            holder.tvTitle.setText(news.title);
            bitmapUtils.display(holder.ivIcon, news.listimage);

            //标记已读未读
            String readIds = PreSharedPreferences.getString("read_ids", "", mActivity);
            if (readIds.contains(mNewsList.get(position).id)){
                //已读
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                //未读
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            return convertView;
        }
    }

    //
    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivIcon;
    }

    //Pager头条适配器
    class TopNewsAdapter extends PagerAdapter {

        BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            //ImageView自身的属性，用于填充父窗体
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //获取图片链接，使用链接下载图片，降图片设置个ImageView，考虑内存溢出问题
            mBitmapUtils.display(view, mTopNewsList.get(position).topimage);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
