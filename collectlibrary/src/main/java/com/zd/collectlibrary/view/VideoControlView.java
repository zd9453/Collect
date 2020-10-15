package com.zd.collectlibrary.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

import com.zd.collectlibrary.R;
import com.zd.collectlibrary.utils.StatesBarUtil;

import java.util.Locale;

/**
 * Package: com.zd.collectlibrary.view
 * <p>
 * describe:
 *
 * @author zhangdong on 2020/10/14
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class VideoControlView extends FrameLayout
        implements IVideoControl, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private ImageView coverImg;
    private ImageView centerPlay;
    private ImageView imgBack;
    private ImageView bottomPlay;
    private ImageView bottomScreen;
    private TextView playTime;
    private TextView totalTime;
    private SeekBar progressBar;
    private Group coverGroup;
    private Group controlGroup;
    private CustomSurfaceVideoView videoView;

    private String[] list = {
            "https://upos-sz-mirrorkodo.bilivideo.com/upgcxcode/39/35/237293539/237293539-1-16.mp4?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1602756387&gen=playurl&os=kodobv&oi=1974294459&trid=ce53526bbc184dac9ca045103e66b956h&platform=html5&upsig=149a27a3b39f22f36c6dfc8366102098&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&logo=80000000",
    };
    private ProgressBar loadingView;

    public VideoControlView(@NonNull Context context) {
        super(context);
        createControlView();
    }

    public VideoControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createControlView();
    }

    public VideoControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createControlView();
    }

    private void createControlView() {
        setBackgroundColor(0);
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_video_control, this, false);
        coverImg = ((ImageView) inflate.findViewById(R.id.video_cover));
        centerPlay = ((ImageView) inflate.findViewById(R.id.center_play));
        imgBack = (ImageView) inflate.findViewById(R.id.img_back);
        bottomPlay = (ImageView) inflate.findViewById(R.id.bottom_play);
        bottomScreen = (ImageView) inflate.findViewById(R.id.bottom_screen);
        playTime = (TextView) inflate.findViewById(R.id.tv_play_time);
        totalTime = (TextView) inflate.findViewById(R.id.tv_total_time);
        progressBar = ((SeekBar) inflate.findViewById(R.id.progress_horizontal));
        coverGroup = ((Group) inflate.findViewById(R.id.cover_group));
        controlGroup = ((Group) inflate.findViewById(R.id.control_group));
        loadingView = ((ProgressBar) inflate.findViewById(R.id.loading));
        loadingView.setVisibility(GONE);

        addView(inflate);

        centerPlay.setOnClickListener(this);
        bottomPlay.setOnClickListener(this);
        bottomScreen.setOnClickListener(this);

        progressBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void setMediaPlayer(CustomSurfaceVideoView videoView) {
        this.videoView = videoView;
    }

    @Override
    public void hideControl() {
        controlGroup.setVisibility(GONE);
    }

    @Override
    public void showControl() {
        controlGroup.setVisibility(VISIBLE);
    }

    @Override
    public void showLoading(boolean isShow) {
        loadingView.setVisibility(isShow ? VISIBLE : GONE);
    }

    @Override
    public void updateTime(boolean isPlaying, int currentPosition) {
        playTime.setText(getTimeText(currentPosition));
        progressBar.setProgress(currentPosition);
        bottomPlay.setSelected(isPlaying);
    }

    @Override
    public void updateTotalTime(int duration) {
        totalTime.setText(getTimeText(duration));
        progressBar.setMax(duration);
        showControl();
    }

    private String getTimeText(int time) {
        if (time <= 0)
            return "00:00";
        //毫秒转化成秒 向上取整
        int second = (int) Math.ceil(time / 1000f);
        //不足1分钟
        if (second < 60)
            return String.format(Locale.CHINA, "00:%02d", second);
        //余下秒
        int lastSecond = second % 60;
        //总分钟
        int minute = second / 60;
        //不足1小时
        if (minute < 60)
            return String.format(Locale.CHINA, "%02d:%02d", minute, lastSecond);
        //超过小时
        //剩余的分钟
        int lastMinute = minute % 60;
        //小时
        int hour = minute / 60;
        return String.format(Locale.CHINA, "%02d:%02d:%02d", hour, lastMinute, lastSecond);
    }

    private int dex = 0;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.center_play) {
            videoView.setPlaySource(list[dex % list.length], true);
            coverGroup.setVisibility(GONE);
        } else if (id == R.id.bottom_play) {
            videoView.changePlayStates();
        } else if (id == R.id.bottom_screen) {
            ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            StatesBarUtil.changeStateBar((Activity) getContext(), Color.TRANSPARENT);
            StatesBarUtil.changeNavigationBar((Activity) getContext());
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (tracking && null != playTime)
            playTime.setText(getTimeText(seekBar.getProgress()));
    }

    private boolean tracking = false;//是否在拖拽进度条

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        tracking = true;
        videoView.stopUpdateProgress(true);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        videoView.changePosition(seekBar.getProgress());
        videoView.stopUpdateProgress(false);
        tracking = false;
    }
}
