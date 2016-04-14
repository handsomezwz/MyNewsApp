package com.zwz.android.mynews.utiles.bitmap;

import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 网络缓存
 * Created by 伟洲 on 2016/4/14.
 */
public class NetCacheUtils {
    public void getBitmapFromNet(ImageView imageView, String url) {
        //异步加载
        BitmapTask task = new BitmapTask();
        task.execute(imageView,url);
    }

    /**
     * AsyncTask是线程池+handler的封装
     * 第一个泛型：传参的参数类型的类型(doInBackground一致)
     * 第二个泛型：更新进度的参数类型(onProgressUpdate一致)
     * 第三个泛型：
     */
    class BitmapTask extends AsyncTask<Object,Integer,Void>{

        //主线程运行，预加载
        @Override
        protected void onPreExecute() {
        }

        //子线程运行，异步加载逻辑在此方法中处理
        //params参数数组
        @Override
        protected Void doInBackground(Object... params) {
            //下载图片
            ImageView imageView = (ImageView) params[0];
            String url = (String) params[1];

            //publishProgress(values);//通知进度
            return null;
        }

        //主线程运行，更新进度


        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        //主线程运行，更新主界面
        @Override
        protected void onPostExecute(Void aVoid) {
            //给imageView设置图片
        }
    }
}
