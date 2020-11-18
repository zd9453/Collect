package com.zd.collect.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.zd.collect.R;
import com.zd.collectlibrary.utils.DisplayUtils;
import com.zd.collectlibrary.utils.StatesBarUtil;

public class StatesUtilsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_states_utils);
    }

    boolean isShow = false;

    public void 改变顶部状态栏(View view) {

        DisplayUtils.changeStatesBarVisibility(this, isShow);
        isShow = !isShow;

    }

    public void 全屏(View view) {
        Log.e(">>>>>>", "全屏: --------- " + isShow);
        DisplayUtils.fullScreen(this, isShow ? DisplayUtils.VERTICAL : DisplayUtils.HORIZONTAL);
        isShow = !isShow;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//        DisplayUtils.fullScreen(this, -1);
//        StatesBarUtil.changeNavigationBar(this);
//        }
        Log.e(">>>>>>", "onWindowFocusChanged: ---------- " + hasFocus);
    }

    public void 透明顶部状态栏(View view) {
        DisplayUtils.transparentStateBar(this);
    }

    public void 隐藏NavigationBar(View view) {
        StatesBarUtil.changeNavigationBar(this);
    }

    public void 透明底部导航栏状态栏(View view) {
        DisplayUtils.transparentNavigationBar(this);
    }
}
