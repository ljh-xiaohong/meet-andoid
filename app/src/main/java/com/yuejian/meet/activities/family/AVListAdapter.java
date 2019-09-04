package com.yuejian.meet.activities.family;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.GifTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.AVData;

import java.util.List;

public class AVListAdapter extends BaseAdapter {
    private List<AVData> avDataList = null;
    private Context context = null;
    private int currentPlayingIndex = 0;

    public AVListAdapter(Context paramContext, List<AVData> paramList) {
        this.avDataList = paramList;
        this.context = paramContext;
    }

    public int getCount() {
        if (this.avDataList == null) {
            return 0;
        }
        return this.avDataList.size();
    }

    public Object getItem(int paramInt) {
        if (this.avDataList == null) {
            return null;
        }
        return this.avDataList.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        paramView = View.inflate(this.context, R.layout.item_av_source, null);
        if ((this.avDataList == null) || (this.avDataList.isEmpty())) {
            return paramView;
        }
        AVData avData = this.avDataList.get(paramInt);
        TextView title = (TextView) paramView.findViewById(R.id.title);
        title.setText(avData.getAv_title());
        TextView author = (TextView) paramView.findViewById(R.id.author);
        author.setText(new StringBuffer("作者: " + avData.av_author));
        ImageView voiceIcon = (ImageView) paramView.findViewById(R.id.voice_icon);
        TextView time = (TextView) paramView.findViewById(R.id.duration);
        if (this.currentPlayingIndex == paramInt) {
            voiceIcon.setVisibility(View.VISIBLE);
            time.setVisibility(View.GONE);
            Glide.with(this.context).load(R.mipmap.voice).asGif().into(voiceIcon);
            return paramView;
        }
        voiceIcon.setVisibility(View.GONE);
        time.setVisibility(View.VISIBLE);
        time.setText(avData.av_time);
        return paramView;
    }

    public void setCurrentPlayingIndex(int paramInt) {
        this.currentPlayingIndex = paramInt;
    }
}
