package com.zwz.android.mynews.domain;

import java.util.ArrayList;

/**
 * Created by 伟洲 on 2016/4/7.
 * 新闻中西分类数据
 * 解析gson对象封装原则：
 * 遇到{}就是一个对象
 * 遇到[]就是一个ArrayList
 * 对象中所有属性命名必须和服务器返回字段完全一致
 */
public class NewsMenuData {
    public int retcode = 200;
    public ArrayList<String> extend;
    public ArrayList<NewsData> data;

    public class NewsData {
        public String id;
        public String title;
        public int type;
        public ArrayList<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsData [title = " + title + ",children = " + children + "]";
        }
    }

    //页签的信息封装
    public class NewsTabData {
        public String id;
        public String title;
        public String url;
        public int type;

        @Override
        public String toString() {
            return "NewsTabData [title=" + title + "]";
        }
    }

    @Override
    public String toString() {
        return "NewsMenuData [data = " + data + "]";
    }
}
