package com.zwz.android.mynews.domain;

import java.util.ArrayList;

/**
 * Created by 伟洲 on 2016/4/11.
 * 新闻列表数据
 */
public class NewsData {
    public int retcode;
    public NewsTab data;

    public class NewsTab {
        //        public ArrayList<> news;
        public String more;
        public ArrayList<News> news;
        public String title;
        //public ArrayList<TopNews> topic;
        public ArrayList<TopNews> topnews;
        @Override
        public String toString() {
            return "NewsTab{" +
                    "title='" + title + '\'' +
                    ", topnews=" + topnews +
                    ", news=" + news +
                    '}';
        }

        public class TopNews {
            public String id;
            public String pubdate;
            public String title;
            public String topimage;
            public String url;
            @Override
            public String toString() {
                return "TopNews{" +
                        "title='" + title +
                        '}';
            }

        }

        public class News {
            public String id;
            public String pubdate;
            public String title;
            public String listimage;
            public String url;

            @Override
            public String toString() {
                return "News{" +
                        "title='" + title + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "data=" + data +
                '}';
    }
}
