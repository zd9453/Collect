package com.zd.collect.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.zd.collect.R;
import com.zd.collectlibrary.utils.StatesBarUtil;
import com.zd.collectlibrary.view.CustomSurfaceVideoView;
import com.zd.collectlibrary.view.VideoControlView;

public class CustomSurfaceVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private CustomSurfaceVideoView surfaceVideoView;
    private boolean isPortrait = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_surface_video);

        surfaceVideoView = (CustomSurfaceVideoView) findViewById(R.id.video_view);

//        surfaceVideoView.setOnClickListener(this);

        VideoControlView controlView = (VideoControlView) findViewById(R.id.control);
        controlView.setMediaPlayer(surfaceVideoView);
        surfaceVideoView.setMediaController(controlView);

    }

    @Override
    public void onClick(View v) {

        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();

        int width = v.getMeasuredWidth();
        int height = v.getMeasuredHeight();

        if (isPortrait) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            Log.e(">>>>>", "onClick: ------ w:" + getResources().getDisplayMetrics().widthPixels + "  " + getResources().getDisplayMetrics().heightPixels);
//            layoutParams.width = getResources().getDisplayMetrics().heightPixels;
//            layoutParams.height = getResources().getDisplayMetrics().widthPixels;

            StatesBarUtil.changeStateBar(this, Color.TRANSPARENT);
            StatesBarUtil.changeNavigationBar(this);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Log.e(">>>>>", "onClick: ------ w:" + getResources().getDisplayMetrics().widthPixels + "  " + getResources().getDisplayMetrics().heightPixels);
//            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
//            layoutParams.height = getResources().getDisplayMetrics().heightPixels * width / height;

        }
        isPortrait = !isPortrait;
//        v.setLayoutParams(layoutParams);
    }
}
