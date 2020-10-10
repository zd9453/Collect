package com.zd.collect.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.zd.collect.R;
import com.zd.collectlibrary.utils.StatesBarUtil;

public class StatesUtilsActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        StatesBarUtil.changeStateBar(this, Color.TRANSPARENT);
        StatesBarUtil.changeNavigationBar(this);

        setContentView(R.layout.activity_states_utils);

        StatesBarUtil.getPhoneInformation();

    }

    private void displayViewInfo(View decorView) {
        Log.e(">>>>>", "displayViewInfo: ---- " + decorView.getClass().getName() + "  " + decorView.getId());
        if (decorView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) decorView).getChildCount(); i++) {
                displayViewInfo(((ViewGroup) decorView).getChildAt(i));
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        /*if (hasFocus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }*/

//        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//        }

    }

    private void changeState() {
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        decorView.setSystemUiVisibility(option);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);

        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar)
            actionBar.hide();

    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
//        changeState();
    }
}
