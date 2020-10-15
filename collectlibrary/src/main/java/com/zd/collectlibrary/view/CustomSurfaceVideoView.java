package com.zd.collectlibrary.view;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Package: com.zd.collectlibrary.view
 * <p>
 * describe: 写自己的视频播放器
 *
 * @author zhangdong on 2020/10/13
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class CustomSurfaceVideoView extends SurfaceView implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private static final String TAG = ">>>>>SurfaceView";
    private SurfaceHolder mSurfaceHolder;   //承载视频播放画面
    private MediaPlayer mMediaPlayer;       //视频播放组件
    private IVideoControl mMediaController; //控制视频播放逻辑
    private IVideoListener iVideoListener;  //视频播放回调
    private int mSurfaceWidth, mSurfaceHeight;//视频展示控件的宽高
    private int mVideoWidth, mVideoHeight;  //视频宽高
    private boolean isPrepared = false;     //资源是否装载完成

    private Handler progressHandler = new Handler(Looper.getMainLooper());
    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            //回调播放进度
            if (null != mMediaController && canPlay()) {
                mMediaController.updateTime(mMediaPlayer.isPlaying(), mMediaPlayer.getCurrentPosition());
                //控制视图的显示隐藏 无操作4秒隐藏
                if (System.currentTimeMillis() - timeMillis > 4000) {
                    hideController();
                } else
                    showController();
            }
            //继续监听
            if (null != progressHandler)
                progressHandler.postDelayed(this, 200);
        }
    };

    private int mCurrentPos;    //当前进度
    private long timeMillis;    //最近触摸时间
    private boolean isAutoPlay; //是否加载好了就播放

    public void setMediaController(IVideoControl mMediaController) {
        this.mMediaController = mMediaController;
    }

    public void setVideoListener(IVideoListener iVideoListener) {
        this.iVideoListener = iVideoListener;
    }

    public CustomSurfaceVideoView(Context context) {
        super(context);
        init();
    }

    public CustomSurfaceVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSurfaceVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //获取焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated: -------");
        mSurfaceHolder = holder;
        createMediaPlayer();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHeight = height;
        mSurfaceWidth = width;
        Log.e(TAG, "surfaceChanged: ------ format:" + format + "  width:" + width + "  height:" + height);

        initPosition();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed: -------");
        //锁屏 或者返回桌面都会走这儿 因此暂停视频即可  记录位置 方便下次恢复
        mSurfaceHolder = null;
        //播放进度先不跟新
        if (null != progressHandler && null != progressRunnable)
            progressHandler.removeCallbacks(progressRunnable);
        //暂停播放
        controlPause();
    }

    /**
     * 创建音频播放组件
     */
    private synchronized void createMediaPlayer() {
        try {
            //创建播放组件
            if (null == mMediaPlayer) {
                isPrepared = false;
                mMediaPlayer = new MediaPlayer();
                //音频类型设置
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                        .build();
                mMediaPlayer.setAudioAttributes(attributes);
                mMediaPlayer.setScreenOnWhilePlaying(true);//播放时屏幕一直亮着
                mMediaPlayer.setOnPreparedListener(this);//准备监听
                mMediaPlayer.setOnVideoSizeChangedListener(this);//尺寸改变监听
                mMediaPlayer.setOnCompletionListener(this);//完成监听
                mMediaPlayer.setOnErrorListener(this);//错误监听
//                mMediaPlayer.setOnBufferingUpdateListener(this);//缓冲监听
            }
            //设置播放承载的surface
            mMediaPlayer.setDisplay(mSurfaceHolder);
            //播放进度更新
            if (null != progressHandler)
                progressHandler.post(progressRunnable);
        } catch (Exception e) {
            Log.e(TAG, "createMediaPlayer: -- create mediaPlayer error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 音频装载完成
     *
     * @param mp .
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        showLoadingControl(false);
        if (mMediaController != null) {
            //控制器可用
            mMediaController.setEnabled(true);
            //视频总时长回调出去
            mMediaController.updateTotalTime(mp.getDuration());
        }
        if (iVideoListener != null) {//自己的回调
            iVideoListener.onPrepared(mp);
        }
        mVideoWidth = mp.getVideoWidth();
        mVideoHeight = mp.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            getHolder().setFixedSize(mVideoWidth, mVideoHeight);
        }
        if (isAutoPlay)
            controlStart();
        Log.e(TAG, "onPrepared: -----------");
    }

    /**
     * 视频尺寸改变
     *
     * @param mp     .
     * @param width  视频w
     * @param height 视频H
     */
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        mVideoWidth = mp.getVideoWidth();
        mVideoHeight = mp.getVideoHeight();
        if (iVideoListener != null) {
            iVideoListener.onSizeChange();
        }
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            getHolder().setFixedSize(mVideoWidth, mVideoHeight);
        }
        Log.e(TAG, "onVideoSizeChanged: ------------ w:" + width + "  H:" + height);
    }

    /**
     * 视频播放完成
     *
     * @param mp .
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.seekTo(0);
        mp.pause();
        //回调播放完成
        if (iVideoListener != null)
            iVideoListener.onCompletion(mp);
        Log.e(TAG, "onCompletion: ----------");
    }

    /**
     * 视频播放出错
     *
     * @param mp    .
     * @param what  .
     * @param extra .
     * @return .
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //回调播放出错
        if (iVideoListener != null)
            iVideoListener.onError(mp, what, extra);
        Log.e(TAG, "onError: ---------- " + what + "  " + extra);
        return true;
    }

    /**
     * 显示控制器
     */
    private synchronized void showController() {
        if (mMediaController != null) {
            mMediaController.showControl();
        }
    }

    /**
     * 隐藏控制器
     */
    private synchronized void hideController() {
        if (mMediaController != null) {
            mMediaController.hideControl();
        }
    }

    /**
     * 是否显示加载视图
     *
     * @param isShow .
     */
    private synchronized void showLoadingControl(boolean isShow) {
        if (null != mMediaController)
            mMediaController.showLoading(isShow);
    }

    /**
     * 初始化最初位置
     */
    private void initPosition() {
        if (!canPlay())
            return;
        if (mCurrentPos != 0)
            mMediaPlayer.seekTo(mCurrentPos);
        mCurrentPos = 0;
    }

    /**
     * 是否可播放
     *
     * @return .
     */
    private boolean canPlay() {
        return mMediaPlayer != null && isPrepared;
    }

    private boolean isPlaying() {
        return canPlay() && mMediaPlayer.isPlaying();
    }

    private int getCurrentPosition() {
        if (null == mMediaPlayer || !isPrepared)
            mCurrentPos = 0;
        else
            mCurrentPos = mMediaPlayer.getCurrentPosition();
        return mCurrentPos;
    }

    /**
     * 释放播放器
     */
    private void releasePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (null != progressHandler) {
            progressHandler.removeCallbacks(progressRunnable);
            progressHandler.removeCallbacksAndMessages(0);
        }
        progressHandler = null;
    }

    /**
     * 设置播放资源
     *
     * @param playUrl  播放地址
     * @param autoPlay 是否加载完成就播放
     */
    public synchronized void setPlaySource(String playUrl, boolean autoPlay) {
        if (null == mMediaPlayer)
            createMediaPlayer();

        showLoadingControl(true);

        if (mMediaPlayer.isPlaying())
            mMediaPlayer.pause();
        mMediaPlayer.reset();
        isPrepared = false;
        isAutoPlay = autoPlay;

        try {
            mMediaPlayer.setDataSource(playUrl);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放
     */
    public synchronized void controlStart() {
        if (canPlay()) {
            mMediaPlayer.start();
        }
        updateTouchTime();
    }

    /**
     * 暂停播放
     */
    public synchronized void controlPause() {
        if (canPlay()) {
            mMediaPlayer.pause();
            mCurrentPos = mMediaPlayer.getCurrentPosition();
        }
        updateTouchTime();
    }

    /**
     * 改变播放状态
     */
    public synchronized void changePlayStates() {
        if (!canPlay())
            return;

        if (mMediaPlayer.isPlaying())
            mMediaPlayer.pause();
        else
            mMediaPlayer.start();

        updateTouchTime();
    }

    /**
     * 停止播放
     */
    public synchronized void controlStop() {
        if (canPlay())
            mMediaPlayer.stop();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releasePlayer();
    }


    public synchronized void changePosition(int progress) {
        if (canPlay())
            mMediaPlayer.seekTo(progress);

        updateTouchTime();
    }

    public synchronized void stopUpdateProgress(boolean isStopUpdate) {
        if (null == progressHandler || null == progressRunnable)
            return;
        if (isStopUpdate) {
            progressHandler.removeCallbacks(progressRunnable);
        } else
            progressHandler.post(progressRunnable);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        updateTouchTime();
        return super.onTouchEvent(event);
    }

    /**
     * 更新上次触摸时间
     */
    private synchronized void updateTouchTime() {
        timeMillis = System.currentTimeMillis();
    }
}
