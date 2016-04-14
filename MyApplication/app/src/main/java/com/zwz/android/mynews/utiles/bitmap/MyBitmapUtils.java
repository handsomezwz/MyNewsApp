package com.zwz.android.mynews.utiles.bitmap;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.zwz.android.mynews.R;

/**
 * Created by 伟洲 on 2016/4/14.
 * 三级缓存
 */
public class MyBitmapUtils {

    private NetCacheUtils mNetUtils;
    private LocalCacheUtils mLocalUtils;
    private MemoryCacheUtils mMemoryUtils;

    //网络缓存工具类
    public MyBitmapUtils() {
        //他妹的，这里顺序要排好啊！
        mLocalUtils = new LocalCacheUtils();

        mMemoryUtils = new MemoryCacheUtils();

        mNetUtils = new NetCacheUtils(mLocalUtils,mMemoryUtils);

    }

    public void display(ImageView imageView, String url) {
        Bitmap bitmap;
        //设置默认加载图片
        imageView.setImageResource(R.drawable.news_pic_default);

        //先从内存缓存加载
        bitmap = mMemoryUtils.getBitmapFromMemory(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.d("读取图片的来源：", "内存");
            return;//用于跳出该方法
        }

        //再从本地缓存加载
        bitmap = mLocalUtils.getBitmapFromLocal(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.d("读取图片的来源：", "本地");
            //给内存设置图片
            mMemoryUtils.setBitmapToMemory(url,bitmap);
            return;
        }

        //从网络缓存加载
        mNetUtils.getBitmapFromNet(imageView, url);
        Log.d("读取图片的来源：", "网络");

    }
}
