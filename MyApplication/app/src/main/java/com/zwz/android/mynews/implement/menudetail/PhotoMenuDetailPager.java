package com.zwz.android.mynews.implement.menudetail;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.zwz.android.mynews.R;
import com.zwz.android.mynews.base.BaseMenuDetailPager;
import com.zwz.android.mynews.domain.PhotoBean;
import com.zwz.android.mynews.global.Contants;
import com.zwz.android.mynews.utiles.CacheUtils;
import com.zwz.android.mynews.utiles.PreSharedPreferences;

import java.util.ArrayList;

/**
 * 菜单详情页-图片
 * Created by 伟洲 on 2016/4/7.
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {

    @ViewInject(R.id.lv_list)
    private ListView lvList;
    @ViewInject(R.id.gv_list)
    private GridView gvList;
    private ArrayList<PhotoBean.PhotoNewsData> mPhotoList;

    private boolean isList = true;

    public PhotoMenuDetailPager(Activity activity, final ImageButton ibDisplay) {
        super(activity);
        ibDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isList) {
                    ibDisplay.setImageResource(R.drawable.icon_pic_list_type);
                    isList = false;
                    lvList.setVisibility(View.GONE);
                    gvList.setVisibility(View.VISIBLE);
                } else {
                    ibDisplay.setImageResource(R.drawable.icon_pic_grid_type);
                    isList = true;
                    lvList.setVisibility(View.VISIBLE);
                    gvList.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_detail_photo, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        //拿缓存
        String cache = CacheUtils.getCacheUtils(Contants.PHOTO_URL, mActivity);

        if (!TextUtils.isEmpty(cache)){
            processResult(cache);
        }

        getDataFromServer();
    }

    public void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Contants.PHOTO_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                processResult(responseInfo.result);
                Log.d("解析图片", "" + responseInfo.result);
                CacheUtils.setCacheUtils(Contants.PHOTO_URL,responseInfo.result,mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                Toast.makeText(mActivity, "出错了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processResult(String result) {
        Gson gson = new Gson();
        PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);

        mPhotoList = photoBean.data.news;

        lvList.setAdapter(new PhotoAdapter());
        gvList.setAdapter(new PhotoAdapter());
    }

    class PhotoAdapter extends BaseAdapter {

        private BitmapUtils mBitmapUtils;

        public PhotoAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }

        @Override
        public int getCount() {
            return mPhotoList.size();
        }

        @Override
        public PhotoBean.PhotoNewsData getItem(int position) {
            return mPhotoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_photo, null);

                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_photo_title);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_photo_icon);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PhotoBean.PhotoNewsData item = getItem(position);
            holder.tvTitle.setText(item.title);
            mBitmapUtils.display(holder.ivIcon, item.listimage);
            return convertView;
        }
    }

    public static class ViewHolder {
        public TextView tvTitle;
        public ImageView ivIcon;
    }
}
