package com.zwz.android.mynews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NewsDatailActivity extends Activity implements View.OnClickListener {

    @ViewInject(R.id.ib_back)
    private ImageButton ib_back;
    @ViewInject(R.id.ib_share)
    private ImageButton ib_share;
    @ViewInject(R.id.ib_textsize)
    private ImageButton ib_textsize;
    @ViewInject(R.id.wv_webview)
    private WebView wv_webview;
    @ViewInject(R.id.pb_loading)
    private ProgressBar pb_loading;
    private String mUrl;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_datail);
        ViewUtils.inject(this);

        //主页面传过来要加载的页面
        mUrl = getIntent().getStringExtra("url");

        //加载网页
        wv_webview.loadUrl(mUrl);
        //拿到控件的设定
        settings = wv_webview.getSettings();
        //放大缩小功能
        settings.setBuiltInZoomControls(true);
        //双击实现缩放功能
        settings.setUseWideViewPort(true);
        //打开js功能
        settings.setJavaScriptEnabled(true);

        wv_webview.setWebViewClient(new WebViewClient() {
            //网页开始加载
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d("webView:", "网页开始加载");
            }

            //网页跳转
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("webView:", "网页跳转");
                //强制在当前页面加载页面，不用跳到浏览器
                view.loadUrl(url);
                return true;
            }

            //网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("webView:", "网页加载结束");
                pb_loading.setVisibility(View.GONE);
            }
        });

        wv_webview.setWebChromeClient(new WebChromeClient() {
            //加载进度回调
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.d("进度回调:", newProgress + "");
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("标题:", title);
            }
        });

        //设置监听
        ib_share.setOnClickListener(this);
        ib_textsize.setOnClickListener(this);
        ib_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_textsize:
                showChooseDialog();
                break;
            case R.id.ib_share:
                break;
        }
    }

    /**
     * 选择字体大小的弹窗
     */
    //点击确定前，用户选择的字体大小
    private int mChooseItem;
    //点击确定后，记录用户选择的字体大小
    private int mRecordItem = 2;

    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
        builder.setSingleChoiceItems(items, mRecordItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mChooseItem = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (mChooseItem) {
                    case 0:
                        settings.setTextZoom(200);
                        break;
                    case 1:
                        settings.setTextZoom(150);
                        break;
                    case 2:
                        settings.setTextZoom(100);
                        break;
                    case 3:
                        settings.setTextZoom(50);
                        break;
                    case 4:
                        settings.setTextZoom(10);
                        break;
                    default:
                        break;
                }
                mRecordItem = mChooseItem;
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
//        builder.show();
    }
}
