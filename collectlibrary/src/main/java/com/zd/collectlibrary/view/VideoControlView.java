package com.zd.collectlibrary.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

import com.zd.collectlibrary.R;
import com.zd.collectlibrary.videoview.SurfaceVideoLayout;

import java.lang.ref.WeakReference;
import java.util.HashMap;
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
    private SurfaceVideoLayout videoView;
    private int[] startSize = new int[2];//初始视频尺寸 [宽,高]
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private String[] list = {
            "https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/31/80/245568031/245568031-1-16.mp4?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1602843564&gen=playurl&os=cosbv&oi=1974294459&trid=dd3c4d0674b8406da48e2b356410aa7dh&platform=html5&upsig=d579b7d091bf3bf0a82b3c579f6e5180&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&logo=80000000",
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
        imgBack.setOnClickListener(this);
        bottomPlay.setOnClickListener(this);
        bottomScreen.setOnClickListener(this);

        progressBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void setMediaPlayer(SurfaceVideoLayout videoView) {
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
            //改变横竖屏方向
            changeScreenOrientation();
        } else if (id == R.id.img_back) {
            clickBack();
        }
    }

    public void setVideoInformation(String url) {
        new GetBitmapThread("getBitmap", url, coverImg).start();
    }

    private void clickBack() {
        Activity activity = (Activity) getContext();
        //当前是横屏，则退出横屏
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attrs);
            activity.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            ViewGroup.LayoutParams params = videoView.getLayoutParams();
            params.width = startSize[0] == 0 ? videoView.getMeasuredWidth() : startSize[0];
            params.height = startSize[1] == 0 ? videoView.getMeasuredHeight() : startSize[1];
            videoView.setLayoutParams(params);

            changShowView(true);
        } else {
            //当前竖屏状态,则关闭页面
            activity.finish();
        }
    }

    int showIndex[] = {-1, -1};

    private void changeScreenOrientation() {

        Activity activity = (Activity) getContext();
        //当前是竖屏
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            //记录初始播放控件尺寸
            if (startSize[0] == 0 || startSize[1] == 0) {
                startSize[0] = videoView.getMeasuredWidth();
                startSize[1] = videoView.getMeasuredHeight();
            }

            ViewParent viewParent = getParent();
            if (viewParent instanceof ViewGroup) {
                showIndex[0] = ((ViewGroup) viewParent).indexOfChild(videoView);
                showIndex[1] = ((ViewGroup) viewParent).indexOfChild(this);
            }

            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(attrs);
            activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            ViewGroup.LayoutParams params = videoView.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoView.setLayoutParams(params);
            changShowView(false);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attrs);
            activity.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            ViewGroup.LayoutParams params = videoView.getLayoutParams();
            params.height = startSize[1] == 0 ? videoView.getMeasuredHeight() : startSize[1];
            videoView.setLayoutParams(params);

            changShowView(true);
        }
    }

    private void changShowView(boolean isShowAll) {
        if (getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) getParent();
            if (isShowAll) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    parent.getChildAt(i).setVisibility(VISIBLE);
                }
            } else {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    if (i == showIndex[0] || i == showIndex[1])
                        parent.getChildAt(i).setVisibility(VISIBLE);
                    else parent.getChildAt(i).setVisibility(GONE);
                }
            }
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

    /**
     * 耗时操作
     *
     * @param videoUrl 视频地址
     * @return 第一帧
     */
    private Bitmap getFirstBitmap(String videoUrl) {
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoUrl, new HashMap<String, String>());
            return mediaMetadataRetriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class GetBitmapThread extends Thread {
        private String videoUrl;
        private WeakReference<ImageView> coverImg;

        public GetBitmapThread(@NonNull String name, String videoUrl, ImageView imageView) {
            super(name);
            this.videoUrl = videoUrl;
            this.coverImg = new WeakReference<>(imageView);
        }

        @Override
        public void run() {
            super.run();
            if (TextUtils.isEmpty(videoUrl) || null == coverImg || null == coverImg.get())
                return;
            final Bitmap firstBitmap = getFirstBitmap(videoUrl);

            if (null == firstBitmap || null == coverImg || null == coverImg.get())
                return;

            //需要切换到主线程显示
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    showCoverImage(coverImg.get(), firstBitmap);
                }
            });
        }
    }

    private void showCoverImage(ImageView imageView, Bitmap firstBitmap) {
        if (null != imageView)
            imageView.setImageBitmap(firstBitmap);
    }
}
