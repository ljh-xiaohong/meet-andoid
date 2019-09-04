package com.yuejian.meet.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.os.Build;

public class MediaPlayerController {

    private AudioManager am;
    private Context mContext;
    private MediaPlayer mediaPlayer;

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //失去焦点之后的操作
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    stopMediaPlayer();
                }
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                startMediaPlayer();
            }
        }
    };

    public MediaPlayerController(Context paramContext, MediaPlayer paramMediaPlayer) {
        this.mediaPlayer = paramMediaPlayer;
        this.mContext = paramContext;
        this.am = ((AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE));
    }

    public void startMediaPlayer() {
        if (this.am.requestAudioFocus(this.afChangeListener, 3, 2) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            this.mediaPlayer.start();
        }
    }

    public void stopMediaPlayer() {
        if ((this.mediaPlayer != null) && (this.mediaPlayer.isPlaying())) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        this.am.abandonAudioFocus(this.afChangeListener);
        if (this.mediaPlayer != null) {
            this.mediaPlayer = null;
        }
    }
}
