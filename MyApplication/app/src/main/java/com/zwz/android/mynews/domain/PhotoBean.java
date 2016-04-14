package com.zwz.android.mynews.domain;

import java.util.ArrayList;

/**
 * Created by 伟洲 on 2016/4/13.
 * 组图对象封装
 */
public class PhotoBean {
    public int retcode;
    public PhotoData data;

    public class PhotoData{
        //public String countcommenturl;
        //public String more;
        public ArrayList<PhotoNewsData> news;
        //public String title;
        //public ArrayList<> topic;
    }

    public class PhotoNewsData{
        public  String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String url;

    }
}
