package com.zd.collectlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zd.collectlibrary.R;

/**
 * Package: com.zd.collectlibrary.view
 * <p>
 * describe: 多状态切换
 *
 * @author zhangdong on 2020/10/10
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class StatesLayout extends FrameLayout {

    public static final String STATE_LOADING = "loadingView";
    public static final String STATE_EMPTY = "emptyView";
    public static final String STATE_CONTENT = "contentView";

    private static final int NULL_ID = -1;
    private int loadingViewId;
    private int emptyViewId;
    private View loadingView;
    private View emptyView;

    private String nowStates = STATE_CONTENT;//当前显示状态

    public String getNowStates() {
        return nowStates;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public StatesLayout(@NonNull Context context) {
        super(context);
    }

    public StatesLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatesLayout);
        loadingViewId = typedArray.getResourceId(R.styleable.StatesLayout_loadingView, NULL_ID);
        emptyViewId = typedArray.getResourceId(R.styleable.StatesLayout_emptyView, NULL_ID);
        typedArray.recycle();

        createView();
    }

    public StatesLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void createView() {
        if (loadingViewId != NULL_ID) {
            createLoadingView();
        }
        if (emptyViewId != NULL_ID) {
            createEmptyView();
        }
    }

    /**
     * 创建加载视图
     */
    private void createLoadingView() {
        try {
            loadingView = LayoutInflater.from(getContext())
                    .inflate(loadingViewId, this, false);
            loadingView.setTag(STATE_LOADING);
            addView(loadingView);
            loadingView.setVisibility(GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建空试图
     */
    private void createEmptyView() {
        try {
            emptyView = LayoutInflater.from(getContext())
                    .inflate(emptyViewId, this, false);
            emptyView.setTag(STATE_EMPTY);
            addView(emptyView);
            emptyView.setVisibility(GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        showContentView();
    }

    /**
     * 显示内容视图
     */
    public synchronized final void showContentView() {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            //隐藏加载和空布局视图
            if (childView.getTag() instanceof String
                    && (STATE_LOADING.equals(childView.getTag())
                    || STATE_EMPTY.equals(childView.getTag()))) {
                childView.setVisibility(GONE);
            } else {
                childView.setVisibility(VISIBLE);
            }
        }
        //记录当前显示状态为内容显示
        nowStates = STATE_CONTENT;
    }

    /**
     * 显示加载视图
     */
    public synchronized final void showLoadingView() {
        if (null == loadingView)
            return;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            //显示加载视图
            if (childView.getTag() instanceof String
                    && STATE_LOADING.equals(childView.getTag())) {
                childView.setVisibility(VISIBLE);
            } else {
                childView.setVisibility(GONE);
            }
        }
        //记录当前状态
        nowStates = STATE_LOADING;
    }

    /**
     * 显示空试图
     */
    public synchronized final void showEmptyView() {
        if (null == emptyView)
            return;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            //显示空视图
            if (childView.getTag() instanceof String
                    && STATE_EMPTY.equals(childView.getTag())) {
                childView.setVisibility(VISIBLE);
            } else {
                childView.setVisibility(GONE);
            }
        }
        //记录当前状态
        nowStates = STATE_EMPTY;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllViews();
        loadingView = null;
        emptyView = null;
    }
}
