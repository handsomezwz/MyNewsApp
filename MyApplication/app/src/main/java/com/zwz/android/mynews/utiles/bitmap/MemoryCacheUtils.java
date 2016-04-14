package com.zwz.android.mynews.utiles.bitmap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/**
 * google建议：
 * API9，也就是android2.3后不推荐利用软弱引用去解决内存溢出问题，推荐用LRU的方法
 *
 * Created by 伟洲 on 2016/4/14.
 * 内存缓存工具类
 * <p/>
 * 这种用法很容易造成内存溢出，为了解决这种状况，会使用软引用，弱引用，虚引用来解决
 * 引用分4中：强引用<Per p = new Per()>,软引用，弱引用，虚引用<比较少用>
 * <p/>
 * 把Bitmap变成软引用
 */
public class MemoryCacheUtils {

    private LruCache<String ,Bitmap> mCache;

    public MemoryCacheUtils() {
        //获取虚拟机分配的最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //LRU最近最少使用，通过控制内存不要超过最大值（由开发者指定），来解决内存溢出
        mCache = new LruCache<String, Bitmap>(maxMemory / 8){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //计算一个bitmap的大小
//                int size = value.getRowBytes()*value.getHeight();//也可以这样写，兼容低版本
                int size = value.getByteCount();
                return super.sizeOf(key, value);
            }
        };
    }

//    private HashMap<String, SoftReference<Bitmap>> mMemoryCache =
//            new HashMap<String, SoftReference<Bitmap>>();

    public Bitmap getBitmapFromMemory(String url) {
//        SoftReference<Bitmap> softReference = mMemoryCache.get(url);
//        if (softReference != null) {
//            Bitmap bitmap = softReference.get();
//            return bitmap;
//        }
//        return null;

        return mCache.get(url);
    }

    public void setBitmapToMemory(String url, Bitmap bitmap) {
//        SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
//        mMemoryCache.put(url, softReference);
        mCache.put(url,bitmap);
    }
}
