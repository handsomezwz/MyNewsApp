package com.zwz.android.mynews.implement;

import android.view.View;

import com.zwz.android.mynews.R;
import com.zwz.android.mynews.base.BaseFragment;

/**
 * Created by 伟洲 on 2016/4/1.
 * 侧边栏Fragment
 */
public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu,null);
        return view;
    }
}
