package com.zwz.android.mynews.implement;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zwz.android.mynews.MainActivity;
import com.zwz.android.mynews.R;
import com.zwz.android.mynews.base.BaseFragment;
import com.zwz.android.mynews.domain.NewsMenuData;

import java.util.ArrayList;


/**
 * Created by 伟洲 on 2016/4/1.
 * 侧边栏Fragment
 */
public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_list)
    private ListView lv_list;

    private int mCurrentPos;

    private ArrayList<NewsMenuData.NewsData> mMenuList;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        com.lidroid.xutils.ViewUtils.inject(this, view);

        return view;
    }


    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public NewsMenuData.NewsData getItem(int position) {
            return mMenuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);

            NewsMenuData.NewsData data = getItem(position);
            TextView tv_menu = (TextView) view.findViewById(R.id.tv_menu);
            tv_menu.setText(data.title);

            if (mCurrentPos == position){
                tv_menu.setEnabled(true);
            }else {
                tv_menu.setEnabled(false);
            }
            return view;
        }
    }

    /**
     * 设置网络数据，该方法有NewsCenterPager调用来传递数据
     *
     * @param data
     */
    public void setData(ArrayList<NewsMenuData.NewsData> data) {
        mMenuList = data;
        final MenuAdapter menuAdapter = new MenuAdapter();
        lv_list.setAdapter(menuAdapter);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                menuAdapter.notifyDataSetChanged();//刷新

                //通知新闻中心，切换页面
                setCurrentMenuDetailPager(position);
                //隐藏侧边栏
                toggle();
            }
        });
        mCurrentPos = 0;
    }

    private void setCurrentMenuDetailPager(int position) {
        //获取新闻中心对象NewsCenterPager
        //1.先获取MainActivity.
        //2.通过MainActivity获取ContentFragment
        //3.通过ContentFragment获取NewsCenterPager

        MainActivity mainActivity = (MainActivity) mActivity;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsPager newsPager = contentFragment.getNewsCenterPager();

        //给新闻中心页面的FrameLayout填充布局
        newsPager.setCurrentMenuDetailPager(position);

    }

    /**
     * 展开或隐藏侧边栏
     */
    private void toggle(){
        MainActivity mainActivity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        //开关（如果开，他就关，如果关，他就开）
        slidingMenu.toggle();
    }
}
