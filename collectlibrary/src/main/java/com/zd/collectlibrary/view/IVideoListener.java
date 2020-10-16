package com.zd.collectlibrary.view;

import android.media.MediaPlayer;

/**
 * Package: com.zd.collectlibrary.view
 * <p>
 * describe://视频播放监听 重写自己需要的
 *
 * @author zhangdong on 2020/10/13
 * @version 1.0
 * @see .
 * @since 1.0
 */
public abstract class IVideoListener implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    public void onSizeChange() {

    }
}
