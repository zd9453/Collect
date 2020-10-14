package com.zd.collectlibrary.view;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

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
public class CustomSurfaceVideoView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = ">>>>>SurfaceView";
    private SurfaceHolder mSurfaceHolder;   //承载视频播放画面
    private MediaPlayer mMediaPlayer;       //视频播放组件
    private IVideoControl mMediaController; //控制视频播放逻辑
    private IVideoListener iVideoListener;  //视频播放回调
    private int mSurfaceWidth, mSurfaceHeight;//视频展示控件的宽高
    private int mVideoWidth, mVideoHeight;  //视频宽高
    private boolean isPrepared = false;     //资源是否装载完成

    //视频地址
    private Uri mUri = Uri.parse("https://upos-sz-mirrorhw.bilivideo.com/upgcxcode/78/94/243269478/243269478-1-16.mp4?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1602647131&gen=playurl&os=hwbv&oi=1974294459&trid=cd18a6b64a134b82893aa39a49b9eb9bh&platform=html5&upsig=6749a379ec7da8c8bf57b697259dce53&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=0&logo=80000000");//播放的地址
    private int mCurrentPos;//当前进度
    private int mDuration = -1;//当前播放视频时长
    private int mCurrentBufferPer;//当前缓冲进度--网络

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
        //创建视频播放组件
        createMediaPlayer();
        getHolder().addCallback(this);
    }

    private void createMediaPlayer() {
        isPrepared = false;
        if (null == mMediaPlayer) {
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(getContext(), mUri);
                //音频类型设置
                AudioAttributes attributes =
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                                .build();
                mMediaPlayer.setAudioAttributes(attributes);
                mMediaPlayer.prepareAsync();

                Log.e(TAG, "createMediaPlayer: ------------ create success");

                //准备监听
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        isPrepared = true;
                        if (mMediaController != null) {//控制器可用
                            mMediaController.setEnabled(true);
                        }
                        if (iVideoListener != null) {//自己的回调
                            iVideoListener.onPrepared(mp);
                        }
                        mVideoWidth = mp.getVideoWidth();
                        mVideoHeight = mp.getVideoHeight();
                        /*if (mVideoWidth != 0 && mVideoHeight != 0) {
                            getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                            //开始初始化
                            initPosition();
                            if (mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
                                if (!isPlaying() && mCurrentPos != 0 || getCurrentPosition() > 0) {
                                    if (mMediaController != null) {
                                        mMediaController.show(0);
                                    }
                                }
                            }
                        }*/
                        start();

                        Log.e(TAG, "onPrepared: -----------");
                    }

                });
                //尺寸改变监听
                mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mVideoWidth = mp.getVideoWidth();
                        mVideoHeight = mp.getVideoHeight();
                        if (iVideoListener != null) {
                            iVideoListener.onSizeChange();
                        }
                        /*if (mVideoWidth != 0 && mVideoHeight != 0) {
                            getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                        }*/

                        Log.e(TAG, "onVideoSizeChanged: ------------" + mVideoWidth + "  " + mVideoHeight);
                    }
                });
                //完成监听
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        hideController();
                        start();
                        if (iVideoListener != null) {
                            iVideoListener.onCompletion(mp);
                        }

                        Log.e(TAG, "onCompletion: ----------");
                    }
                });
                //错误监听
                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        hideController();
                        if (iVideoListener != null) {
                            iVideoListener.onError(mp, what, extra);
                        }
                        Log.e(TAG, "onError: ---------- " + what + "  " + extra);
                        return true;
                    }
                });
                mMediaPlayer.setOnBufferingUpdateListener(
                        new MediaPlayer.OnBufferingUpdateListener() {
                            @Override
                            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                                mCurrentBufferPer = percent;
                            }
                        });

            } catch (Exception e) {
                Log.e(TAG, "createMediaPlayer: -- create mediaPlayer error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
//        openVideo();
        Log.e(TAG, "surfaceCreated: -------");
        if (null != mMediaPlayer)
            mMediaPlayer.setDisplay(mSurfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHeight = height;
        mSurfaceWidth = width;
        Log.e(TAG, "surfaceChanged: ------ format:" + format + "  width:" + width + "  height:" + height);
        if (mMediaPlayer != null && isPrepared) {
            initPosition();
            mMediaPlayer.start();//开始播放
            showCtrl();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "surfaceDestroyed: -------");
        mSurfaceHolder = null;
        hideController();
//        releasePlayer();
    }

    /**
     * 显示控制器
     */
    private void showCtrl() {
        if (mMediaController != null) {
            mMediaController.show();
        }
    }

    /**
     * 隐藏控制器
     */
    private void hideController() {
        if (mMediaController != null) {
            mMediaController.hide();
        }
    }

    /**
     * 初始化最初位置
     */
    private void initPosition() {
        if (mCurrentPos != 0) {
            mMediaPlayer.seekTo(mCurrentPos);
            mCurrentPos = 0;
        }
    }

    private void openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            return;
        }
        isPrepared = false;//没有准备完成
        releasePlayer();
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(getContext(), mUri);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);//播放时屏幕一直亮着
            mMediaPlayer.prepareAsync();//异步准备
            attach2Ctrl();//绑定媒体控制器
        } catch (Exception e) {
            Log.e(">>>>>", "openVideo error: " + e.getMessage());
            e.printStackTrace();
        }
        //准备监听
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepared = true;
                if (mMediaController != null) {//控制器可用
                    mMediaController.setEnabled(true);
                }
                if (iVideoListener != null) {//自己的回调
                    iVideoListener.onPrepared(mp);
                }
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
                if (mVideoWidth != 0 && mVideoHeight != 0) {
                    getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                    //开始初始化
                    initPosition();
                    if (mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
                        if (!isPlaying() && mCurrentPos != 0 || getCurrentPosition() > 0) {
                            if (mMediaController != null) {
                                mMediaController.show(0);
                            }
                        }
                    }
                }
                start();
            }
        });
        //尺寸改变监听
        mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
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
            }
        });
        //完成监听
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                hideController();
                start();
                if (iVideoListener != null) {
                    iVideoListener.onCompletion(mp);
                }
            }
        });
        //错误监听
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                hideController();
                if (iVideoListener != null) {
                    iVideoListener.onError(mp, what, extra);
                }
                return true;
            }
        });
        mMediaPlayer.setOnBufferingUpdateListener(
                new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        mCurrentBufferPer = percent;
                    }
                });
    }

    private void start() {
        if (canPlay())
            mMediaPlayer.start();

        getHolder().setKeepScreenOn(true);
    }

    private boolean canPlay() {
        return mMediaPlayer != null && isPrepared;
    }

    private boolean isPlaying() {
        if (canPlay())
            return mMediaPlayer.isPlaying();
        return false;
    }

    private int getCurrentPosition() {

        return 0;
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
    }

    private void attach2Ctrl() {
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController.setMediaPlayer(this);
            View anchor = this.getParent() instanceof View ? (View) this.getParent() : this;
            mMediaController.setAnchorView(anchor);
            mMediaController.setEnabled(true);
        }
    }

}
