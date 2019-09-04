package com.yuejian.meet.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by zh02 on 2017/9/5.
 */

public class YojianVideoView extends VideoView {
    public YojianVideoView(Context context) {
        super(context);
    }

    public YojianVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YojianVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void start() {
        super.start();
        if (playListener != null) {
            playListener.onPlay();
        }

    }

    @Override
    public void pause() {
        super.pause();
        if (playListener != null) {
            playListener.onPause();
        }
    }

    private OnPlayListener playListener = null;

    public void setOnPlayListener(OnPlayListener playListener) {
        this.playListener = playListener;
    }

    public interface OnPlayListener {
        void onPlay();

        void onPause();
    }
}
