package com.zwz.android.mynews;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.zwz.android.mynews.utiles.PreSharedPreferences;

import java.util.ArrayList;


public class GuideActivity extends Activity implements View.OnClickListener{

    private Button btn_welcome;
    private ViewPager vp;
    private int[] mImageIds;
    private ArrayList<ImageView> mImageViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        btn_welcome = (Button) findViewById(R.id.btn_welcome);
        vp = (ViewPager) findViewById(R.id.vp);

        //按钮设置监听
        btn_welcome.setOnClickListener(this);

        //初始化ImageView
        mImageIds = new int[]{R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
        mImageViewList = new ArrayList<ImageView>();
        for (int i =0;i<mImageIds.length;i++){
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(imageView);
        }
        vp.setAdapter(new MyViewPagerAdapter());


        /**
         *  setOnPageChangeListener(ViewPager.OnPageChangeListener listener)
         *  This method is deprecated. Use addOnPageChangeListener(OnPageChangeListener) and removeOnPageChangeListener(OnPageChangeListener) instead.
         */
        /*vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImageIds.length-1){
                    btn_welcome.setVisibility(View.VISIBLE);
                }else{
                    btn_welcome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImageIds.length-1){
                    btn_welcome.setVisibility(View.VISIBLE);
                }else{
                    btn_welcome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_welcome:
                PreSharedPreferences.putBoolean("is_guide_showed",true,getApplicationContext());
                Intent i = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }
    }

    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            /*ImageView imageView = new ImageView(getApplicationContext());
            imageView.setBackgroundResource(mImageIds[position]);
            container.addView(imageView);*/
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
