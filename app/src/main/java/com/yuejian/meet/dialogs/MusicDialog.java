package com.yuejian.meet.dialogs;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.aliyun.apsaravideo.music.music.EffectBody;
import com.aliyun.apsaravideo.music.music.MusicLoader;
import com.aliyun.svideo.base.http.MusicFileBean;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.adapter.MusicAdapter;

import java.util.ArrayList;
import java.util.List;

public class MusicDialog extends DialogFragment {

    private View view;
    private View v_Cancel;
    private View v_Finish;
    private RecyclerView recyclerView;
    private MusicAdapter adapter;
    private OnMusicListener listener;

    /**
     * 音乐播放器
     */
    private MediaPlayer mMediaPlayer;


    private MusicLoader musicLoader;
    private ArrayList<EffectBody<MusicFileBean>> mLocalMusicList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogFullStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_music_chooser, container);
        initViews(view);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();

        if (window != null) {
            //设置dialog动画
            window.getAttributes().windowAnimations = com.aliyun.demo.R.style.record_bottom_dialog_animation;
        }
        initData();

    }

    private void initViews(View view) {
        v_Cancel = view.findViewById(R.id.dialog_music_cancel);
        v_Finish = view.findViewById(R.id.dialog_music_finish);
        recyclerView = view.findViewById(R.id.dialog_music_recyclerView);

        adapter = new MusicAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnMusicItemClickListener(new MusicAdapter.OnMusicItemClickListener() {
            @Override
            public void onItemClick(int position, EffectBody<MusicFileBean> music) {
                if (null != listener) {
                    listener.musicInfo(music.getData().getPath(), 20 * 1000, music.getData().duration);

                }
                dismiss();
            }

        });

    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        Window window = null;
        if (null != dialog) {
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            window = getDialog().getWindow();
        }

        if (null != window) {
            window.setGravity(Gravity.BOTTOM);
            DisplayMetrics dpMetrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
            WindowManager.LayoutParams p = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            p.dimAmount = 0.0f;
            p.y = 100;

        }


    }

    private void initData() {
        if (musicLoader == null) {
            musicLoader = new MusicLoader(getContext());
            musicLoader.setCallback(new MusicLoader.LoadCallback() {
                @Override
                public void onLoadLocalMusicCompleted(List<EffectBody<MusicFileBean>> loacalMusic) {
                    mLocalMusicList.clear();
                    mLocalMusicList.addAll(loacalMusic);
                    adapter.setData(mLocalMusicList, 0);

                }

                @Override
                public void onLoadNetMusicCompleted(List<EffectBody<MusicFileBean>> netMusic) {

                }

                @Override
                public void onSearchNetMusicCompleted(List<EffectBody<MusicFileBean>> result) {

                }
            });
            musicLoader.loadAllMusic();
        }
    }

    public interface OnMusicListener {
        void musicInfo(String path, int startTime, int duration);
    }

    public void setOnMusicListener(OnMusicListener musicListener) {
        this.listener = musicListener;
    }


}
