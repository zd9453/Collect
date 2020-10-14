package com.zd.collectlibrary.view;

import android.view.View;

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

    void setEnabled(boolean enable);

    void show(int position);

    void setMediaPlayer(CustomSurfaceVideoView videoView);

    void setAnchorView(View anchor);

    void hide();

    void show();
}
