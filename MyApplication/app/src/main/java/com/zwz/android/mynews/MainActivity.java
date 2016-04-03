package com.zwz.android.mynews;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zwz.android.mynews.implement.ContentFragment;
import com.zwz.android.mynews.implement.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG_CONTENT = "TAG_CONTENT";
    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //添加侧边框
        setBehindContentView(R.layout.left_menu);
        /*深入定制*/
        SlidingMenu slidingMenu = getSlidingMenu();
        //slidingMenu.setMenu(R.layout.left_menu);//不知道什么用
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(200);

        //初始化Fragment
        initFragment();

    }

    /**
     * 初始化Fragment
     */
    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        //开始事务
        FragmentTransaction transaction = fm.beginTransaction();
        //将帧布局替换为对应的Fragment
        transaction.replace(R.id.activity_main, new ContentFragment(), TAG_CONTENT);
        transaction.replace(R.id.left_menu, new LeftMenuFragment(), TAG_LEFT_MENU);
        //提交事务
        transaction.commit();
    }


}
