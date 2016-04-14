package com.zwz.android.mynews.utiles.bitmap;

import android.widget.ImageView;

/**
 * Created by 伟洲 on 2016/4/14.
 * 三级缓存
 */
public class MyBitmapUtils {

    private NetCacheUtils mNetUtils;

    //网络缓存工具类
    public MyBitmapUtils(){
        mNetUtils = new NetCacheUtils();
    }

    public void display(ImageView imageView,String url){
        //先从内存缓存加载
        //再从本地缓存加载
        //从网络缓存加载

        mNetUtils.getBitmapFromNet(imageView,url);
    }
}
