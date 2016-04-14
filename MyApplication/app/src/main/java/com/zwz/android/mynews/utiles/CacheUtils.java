package com.zwz.android.mynews.utiles;

import android.content.Context;

/**
 * Created by 伟洲 on 2016/4/9.
 * 网络缓存工具类
 * 通常缓存Json数据
 * 原则：
 * 以url+参数为key，以json为value，保存起来
 */
public class CacheUtils {
    //写，可以用数据库，shared，sd卡
    public static void setCacheUtils(String url, String json, Context context) {
        PreSharedPreferences.putString(url, json, context);
    }

    //读
    public static String getCacheUtils(String url, Context context) {
        return PreSharedPreferences.getString(url, null, context);
    }
}
