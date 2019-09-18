package com.yuejian.meet.activities.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.apsaravideo.music.music.EffectBody;
import com.aliyun.svideo.base.http.MusicFileBean;
import com.yuejian.meet.R;
import com.yuejian.meet.widgets.aliyun.MusicHorizontalScrollView;
import com.yuejian.meet.widgets.aliyun.MusicWaveView;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    /**
     * 选中的角标
     */
    private int mSelectPosition;

    private int[] mScrollX;

    /**
     * 记录选择的下标
     */
    private int mSelectIndex = 0;


    private ArrayList<EffectBody<MusicFileBean>> mLocalMusicList = new ArrayList<>();

    /**
     * 截取音乐是时长 默认为10秒；
     */
    private int mStreamDuration = 10 * 1000;


    private OnMusicItemClickListener itemclickListener;


    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MusicViewHolder holder, final int position) {

        final EffectBody<MusicFileBean> music = mLocalMusicList.get(position);

        switch (holder.musicInfoLayout.getVisibility()) {

            case GONE:
                holder.musicInfoLayout.setVisibility(VISIBLE);
                holder.musicName.setText(music.getData().getDisplayName());
                holder.musicSinger.setText(music.getData().getArtist());
                holder.musicWave.setDisplayTime(10 * 1000);
                holder.musicWave.setTotalTime(music.getData().getDuration());
                holder.musicWave.layout();
                holder.scrollBar.setScrollViewListener(new MusicHorizontalScrollView.ScrollViewListener() {
                    @Override
                    public void onScrollChanged(HorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                        if (position < mScrollX.length) {
                            mScrollX[position] = x;
                            setDurationTxt(holder, x, music.getData().duration);
                        }
                    }

                    @Override
                    public void onScrollStop() {

                    }
                });


                break;

            case VISIBLE:
                holder.musicInfoLayout.setVisibility(GONE);

                break;

        }
        if (holder.musicInfoLayout.getVisibility() == View.VISIBLE) {

        } else {

        }
        holder.musicInfoLayout.getVisibility();


    }


    @Override
    public int getItemCount() {
        return mLocalMusicList.size();
    }


    class MusicViewHolder extends RecyclerView.ViewHolder {

        public TextView musicName;
        public ImageView musicImage;
        public TextView musicSinger;
        public MusicWaveView musicWave;
        public RelativeLayout musicInfoLayout;
        public RelativeLayout musicNameLayout;
        public MusicHorizontalScrollView scrollBar;
        public TextView musicStartTxt;
        public TextView musicEndTxt;
        private int mPosition;
        private EffectBody<MusicFileBean> mData;

        public void updateData(int position, EffectBody<MusicFileBean> data) {
            this.mData = data;
            this.mPosition = position;
            MusicFileBean music = data.getData();
            musicName.setText(music.title);
            if (music.artist == null || music.artist.isEmpty()) {
                musicSinger.setVisibility(GONE);
            } else {
                musicSinger.setVisibility(View.VISIBLE);
                musicSinger.setText("- " + music.artist);
            }
        }

        public MusicViewHolder(View itemView) {
            super(itemView);

            musicNameLayout = itemView.findViewById(R.id.item_music_name_layout);
            musicInfoLayout = itemView.findViewById(R.id.item_music_info_layout);
            musicName = itemView.findViewById(R.id.item_music_name_tv);
            musicImage = itemView.findViewById(R.id.item_music_player_img);
            musicSinger = itemView.findViewById(R.id.item_music_singer_name);
            musicStartTxt = itemView.findViewById(R.id.item_music_starttime);
            musicEndTxt = itemView.findViewById(R.id.item_music_endtime);
            musicWave = itemView.findViewById(R.id.item_music_waveview);
            scrollBar = itemView.findViewById(R.id.item_music_wave_scrollView);
            setDurationTxt(this, 0, 0);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemclickListener != null) {
                        itemclickListener.onItemClick(getAdapterPosition(), mLocalMusicList.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    private void setDurationTxt(MusicViewHolder holder, int x, int duration) {
        int leftTime = (int) ((float) x / holder.musicWave.getMusicLayoutWidth() * duration);
        int rightTime = leftTime + mStreamDuration;
        int time = leftTime / 1000;
        int min = time / 60;
        int sec = time % 60;
        holder.musicStartTxt.setText(String.format("%1$02d:%2$02d", min, sec));
        time = rightTime / 1000;
        min = time / 60;
        sec = time % 60;
        holder.musicEndTxt.setText(String.format("%1$02d:%2$02d", min, sec));
    }

    public void setData(ArrayList<EffectBody<MusicFileBean>> dataList, int selectIndex) {

        this.mLocalMusicList.clear();
        this.mLocalMusicList.addAll(dataList);
        MusicFileBean mediaEntity = new MusicFileBean();
//        EffectBody<MusicFileBean> effectBody = new EffectBody<>(mediaEntity, true);
//        this.mLocalMusicList.add(0, effectBody);
        mScrollX = new int[this.mLocalMusicList.size()];
        mSelectIndex = selectIndex;
//        if (onMusicSeek != null) {
//            onMusicSeek.onSelectMusic(selectIndex, this.dataList.get(selectIndex));
//        }
        notifyDataSetChanged();
    }

    public interface OnMusicItemClickListener {
        void onItemClick(int position, EffectBody<MusicFileBean> music);
    }

    public void setOnMusicItemClickListener(OnMusicItemClickListener listener) {
        this.itemclickListener = listener;
    }

}
