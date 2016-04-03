package com.zwz.android.mynews.utiles;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 伟洲 on 2016/3/30.
 */
public class PreSharedPreferences {
    /**
     * Boolean的操作
     *
     * @param key     键对的key
     * @param value   键对的value
     * @param context 上下文
     */
    public static void putBoolean(String key, Boolean value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * @param key      键对的key
     * @param defValue 如果key不存在则返回输入的默认值
     * @param context  上下文
     * @return
     */
    public static boolean getBoolean(String key, boolean defValue, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean b = sharedPreferences.getBoolean(key, defValue);
        return b;
    }

    /**
     * int的操作
     *
     * @param key     键对的key
     * @param value   键对的value
     * @param context 上下文
     */
    public static void putInt(String key, int value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * @param key      键对的key
     * @param defValue 如果key不存在则返回输入的默认值
     * @param context  上下文
     * @return
     */
    public static int getInt(String key, int defValue, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        int b = sharedPreferences.getInt(key, defValue);
        return b;
    }

    /**
     * String的操作
     *
     * @param key     键对的key
     * @param value   键对的value
     * @param context 上下文
     */
    public static void putString(String key, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * @param key      键对的key
     * @param defValue 如果key不存在则返回输入的默认值
     * @param context  上下文
     * @return
     */
    public static String getString(String key, String defValue, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String b = sharedPreferences.getString(key, defValue);
        return b;
    }

    /**
     * 清空所有的键对
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void remove(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
