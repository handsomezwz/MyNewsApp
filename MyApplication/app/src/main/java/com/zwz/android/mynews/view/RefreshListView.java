package com.zwz.android.mynews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zwz.android.mynews.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 伟洲 on 2016/4/12.
 * 下拉刷新的ListView
 */
public class RefreshListView extends ListView
        implements AbsListView.OnScrollListener,AdapterView.OnItemClickListener {

    private static final int STATE_PULL_TO_REFRESH = 1;//下拉刷新
    private static final int STATE_RELEASE_TO_REFRESH = 2;//松开刷新
    private static final int STATE_REFRESHING = 3;//正在刷新

    private boolean isLoadingMore;//标记是否加载更多

    private int mCurrentState = STATE_PULL_TO_REFRESH;//默认是下拉刷新

    private int startY = -1;
    private int endY;
    private int measuredHeight;
    private View mHeaderView;
    private TextView textView;
    private ImageView ivArrow;
    private ProgressBar pbLoading;
    private RotateAnimation animationUp;//箭头向上的动画
    private RotateAnimation animationDown;
    private TextView tv_time;
    private View mFooterView;
    private int mFooterViewHeight;

    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }


    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.list_refresh_header, null);
        //添加头布局
        this.addHeaderView(mHeaderView);

        //隐藏头布局：1.获取头布局高度 2.设置负paddingTop，布局就会往上走
        /**这个方法只能等到界面绘制好才可以用
         * int heght = mHeaderView.getHeight();*/
        //绘制之前就要获取布局的高度
        mHeaderView.measure(0, 0);//手动测量布局
        measuredHeight = mHeaderView.getMeasuredHeight();
        //隐藏
        mHeaderView.setPadding(0, -measuredHeight, 0, 0);
        //找到相应控件
        textView = (TextView) mHeaderView.findViewById(R.id.tv_title);
        ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        pbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
        tv_time = (TextView) mHeaderView.findViewById(R.id.tv_time);
        //初始化好动画
        initAnim();
        setCurrentTime();
    }

    /**
     * 初始化脚布局
     */
    public void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.list_refresh_footer, null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        //隐藏
        //mFooterView.setPadding(0,0,0,-mFooterViewHeight);自己写的，可以实现
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        //设置滑动监听
        this.setOnScrollListener(this);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    //如果用户按住头条新闻向下滑动，会导致listView无法拿到ACTION_DOWN,此时要重新获取startY
                    startY = (int) ev.getY();
                }

                //如果正在刷新，就什么都不执行跳出switch
                if (mCurrentState == STATE_REFRESHING) {
                    break;
                }

                endY = (int) ev.getY();
                int dy = endY - startY;

                //表示向下滑动&当前显示的是第一个item，才允许下拉刷新出来
                if (dy > 0 && getFirstVisiblePosition() == 0) {
                    //计算当前的paddingtop的值
                    int paddingTop = dy - measuredHeight;

                    if (paddingTop >= 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                        //切换到松开刷新
                        mCurrentState = STATE_RELEASE_TO_REFRESH;
                        refreshState();
                    } else if (paddingTop < 0 && mCurrentState != STATE_PULL_TO_REFRESH) {
                        //切换到下拉刷新
                        mCurrentState = STATE_PULL_TO_REFRESH;
                        refreshState();
                    }
                    //重新设置头布局padding
                    mHeaderView.setPadding(0, paddingTop, 0, 0);
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;
                if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                    //如果当前是松开刷新，就要切换为正在刷新
                    //显示头布局
                    mHeaderView.setPadding(0, 0, 0, 0);
                    mCurrentState = STATE_REFRESHING;
                    refreshState();

                    //下拉刷新回调
                    if (mListener != null) {
                        mListener.onRefresh();
                    }

                } else if (mCurrentState == STATE_PULL_TO_REFRESH) {
                    //隐藏头布局
                    mHeaderView.setPadding(0, -measuredHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据当前状态刷新界面
     */
    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_TO_REFRESH:
                textView.setText("下拉刷新");
                ivArrow.startAnimation(animationDown);
                pbLoading.setVisibility(INVISIBLE);
                break;
            case STATE_REFRESHING:
                textView.setText("正在刷新");
                //动画没有停止没法隐藏控件
                ivArrow.clearAnimation();
                ivArrow.setVisibility(INVISIBLE);
                pbLoading.setVisibility(VISIBLE);
                break;
            case STATE_RELEASE_TO_REFRESH:
                textView.setText("松开刷新");
                //箭头向上移动
                ivArrow.startAnimation(animationUp);
                pbLoading.setVisibility(INVISIBLE);
                break;
        }
    }

    /**
     * 初始化箭头动画
     */
    private void initAnim() {
        animationUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        animationUp.setDuration(500);
        animationUp.setFillAfter(true);

        animationDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        animationDown.setDuration(500);
        animationDown.setFillAfter(true);
    }

    /**
     * 回调刷新接口
     */
    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            //获取当前可见的最后一个item的位置
            int lastVisiblePosition = getLastVisiblePosition();
            if (lastVisiblePosition >= getCount() - 1 && isLoadingMore == false) {
                isLoadingMore = true;

                //Log.d("Bottom:","out");有执行到这

                //加载更多了....（到底了）
                //显示脚布局
                mFooterView.setPadding(0, 0, 0, 0);

                //listView设置当前要展示的item的位置
                setSelection(getCount() - 1);//直接显示脚布局

                if (mListener != null) {
                    mListener.loadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }



    /////////////////////////////////////////////////////////////////////////////////////////////
    public interface OnRefreshListener {
        //下拉刷新的回调
        public void onRefresh();

        //加载更多的回调
        public void loadMore();
    }

    public void OnRefreshComplete(boolean success) {
        if (!isLoadingMore){
            mCurrentState = STATE_PULL_TO_REFRESH;
            mHeaderView.setPadding(0, -measuredHeight, 0, 0);
            pbLoading.setVisibility(INVISIBLE);
            ivArrow.setVisibility(VISIBLE);
            textView.setText("下拉刷新");

            //刷新失败，不需要刷新
            if (success) {
                setCurrentTime();
            }
        }else {
            //隐藏脚布局
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
            isLoadingMore = false;
        }
    }


    //设置上次刷新的时间
    private void setCurrentTime() {
        //HH是24小时制，hh是12小时
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        tv_time.setText(time);
    }


    /**
     * 重写item点击方法
     * 偷偷的更改了下position，中介人
     */
    private OnItemClickListener mItemClickListener;
    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
        //将点击事件设置给当前的RefreshListView
        super.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick(parent,view,position-getHeaderViewsCount(),id);
        }
    }
}