package com.zd.collectlibrary.view;

/**
 * Package: com.zd.collectlibrary.view
 * <p>
 * describe: //音频控制  控制view来实现具体的控制方式
 *
 * @author zhangdong on 2020/10/13
 * @version 1.0
 * @see .
 * @since 1.0
 */
public interface IVideoControl {

    /**
     * 是否可操作
     *
     * @param enable 是否可操作
     */
    void setEnabled(boolean enable);

    void setMediaPlayer(CustomSurfaceVideoView videoView);

    void hideControl();

    void showControl();

    /**
     * 当前播放进度
     *
     * @param isPlaying       是否在播放
     * @param currentPosition 当前播放到的毫秒值
     */
    void updateTime(boolean isPlaying, int currentPosition);

    /**
     * 总时长
     *
     * @param duration 总时长 毫秒值
     */
    void updateTotalTime(int duration);

    /**
     * 显示加载视图
     *
     * @param isShow 是否显示
     */
    void showLoading(boolean isShow);
}
