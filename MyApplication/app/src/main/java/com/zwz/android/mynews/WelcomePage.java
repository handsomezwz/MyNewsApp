package com.zwz.android.mynews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.zwz.android.mynews.utiles.PreSharedPreferences;

public class WelcomePage extends Activity {

    private RelativeLayout welcome_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        //实现动画效果，旋转，缩放，渐变
        welcome_ll = (RelativeLayout) findViewById(R.id.welcome_ll);
        /**
         * 旋转动画
         * 1.获取RotateAnimation对象
         * 2.设置动画时间
         * 3.给layout实现效果
         */
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        //
        rotateAnimation.setFillAfter(true);
//        welcome_ll.setAnimation(rotateAnimation);

        /**
         * 缩放动画
         * 1.获取ScaleAnimation对象
         * 2.设置动画时间
         * 3.给layout实现效果
         */
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
//        welcome_ll.setAnimation(scaleAnimation);

        /**
         * 缩放动画
         * 1.获取AlphaAnimation对象
         * 2.设置动画时间
         * 3.给layout实现效果
         */
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
//        welcome_ll.setAnimation(alphaAnimation);

        /**
         * 如果要动画同时进行，那么就要一个动画集合
         * 构造方法中要传入一个boolean值判断是否动画们公用一个计时器
         */
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        //给welcome_ll设置动画集合
        welcome_ll.setAnimation(animationSet);

        /**
         * 要跳转到主界面，监听动画结束时间
         */
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


                boolean isGuideShowed = PreSharedPreferences.getBoolean("is_guide_showed",false,getApplicationContext());
                Intent i = null;
                if (isGuideShowed == false) {
                    i = new Intent(getApplicationContext(), GuideActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    //把当前页面关了
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


}
