package com.yuejian.meet.activities.web;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yuejian.meet.R;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.YojianVideoView;

public class VideoViewActivity extends AppCompatActivity {

    private YojianVideoView videoView;
    private ProgressBar progressBar;
    private Uri videoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video_view);
        //网络视频
        String videoUrl = getIntent().getStringExtra(Constants.VIDEO_URL);
        videoUri = Uri.parse(videoUrl);
        videoView = (YojianVideoView) this.findViewById(R.id.videoView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (getIntent().getExtras().getBoolean("localVideoUrl"))
            progressBar.setVisibility(View.GONE);
        //设置视频控制器
//        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(new MediaController(this));
        //播放完成回调
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    int currentPosition, duration;

                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        // 获得当前播放时间和当前视频的长度
                        currentPosition = videoView.getCurrentPosition();
                        duration = videoView.getDuration();
                        int time = ((currentPosition * 100) / duration);
                        // 设置进度条的主要进度，表示当前的播放时间
//                        SeekBar seekBar = new SeekBar(VideoViewActivity.this);
                        progressBar.setProgress(time);
                        // 设置进度条的次要进度，表示视频的缓冲进度
                        progressBar.setSecondaryProgress(percent);
                        if (percent == 100) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                mp.start();
                mp.setLooping(true);
            }
        });

        videoView.setOnPlayListener(new YojianVideoView.OnPlayListener() {
            @Override
            public void onPlay() {
                if (!Utils.isNetLink()) {
                    Toast.makeText(VideoViewActivity.this, "当前网络不可链接，请检查网络连接情况", Toast.LENGTH_SHORT).show();
                    videoView.pause();
                }
            }

            @Override
            public void onPause() {

            }
        });

        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
        //设置视频路径
        videoView.setVideoURI(videoUri);
        //开始播放视频
        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }

    private class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            videoView.setVideoURI(videoUri);
            videoView.start();
        }
    }
}