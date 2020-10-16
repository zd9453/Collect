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
import com.zd.collectlibrary.videoview.SurfaceVideoLayout;
import com.zd.collectlibrary.view.VideoControlView;

public class CustomSurfaceVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private SurfaceVideoLayout surfaceVideoView;
    private VideoControlView controlView;
    private boolean isPortrait = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_custom_surface_video);

        surfaceVideoView = (SurfaceVideoLayout) findViewById(R.id.video_view);

//        surfaceVideoView.setOnClickListener(this);

        controlView = (VideoControlView) findViewById(R.id.control);
        controlView.setMediaPlayer(surfaceVideoView);
        surfaceVideoView.setMediaController(controlView);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        controlView.setVideoInformation("https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/31/80/245568031/245568031-1-16.mp4?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1602843564&gen=playurl&os=cosbv&oi=1974294459&trid=dd3c4d0674b8406da48e2b356410aa7dh&platform=html5&upsig=d579b7d091bf3bf0a82b3c579f6e5180&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&logo=80000000");
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
