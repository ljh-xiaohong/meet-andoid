package com.aliyun.svideo.editor.effects.sound;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aliyun.svideo.editor.R;
import com.aliyun.svideo.editor.effects.control.BaseChooser;
import com.aliyun.svideo.editor.effects.control.EffectInfo;
import com.aliyun.svideo.editor.effects.control.OnItemClickListener;
import com.aliyun.svideo.editor.effects.control.SoundEffectInfo;
import com.aliyun.svideo.editor.effects.control.SpaceItemDecoration;
import com.aliyun.svideo.editor.effects.filter.FilterAdapter;
import com.aliyun.svideo.editor.msg.Dispatcher;
import com.aliyun.svideo.editor.msg.body.SelectColorFilter;
import com.aliyun.svideo.editor.util.Common;

public class SoundEffectChooseView extends BaseChooser {

    private RecyclerView mRecyclerView;
    private TextView mSoundTitle;
    private EffectSoundAdapter adapter;

    public SoundEffectChooseView(@NonNull Context context) {
        this(context, null);
    }

    public SoundEffectChooseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundEffectChooseView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.alivc_svideo_effect_sound_view, this);
        mRecyclerView = findViewById(R.id.effect_sound_list_filter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),  LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(getContext().getResources().getDimensionPixelSize(R.dimen.list_item_space)));

        if (adapter ==null){
            adapter = new EffectSoundAdapter(getContext());
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public boolean onItemClick(EffectInfo effectInfo, int index) {
                    Dispatcher.getInstance().postMsg(new SelectEffectSound.Builder()
                        .effectInfo(effectInfo).oldInfo(MockEffectSoundData.getEffectSound(index))
                        .index(index).build());
                    return false;
                }
            });
            adapter.setDataList(MockEffectSoundData.getEffectSound());
        }
        mRecyclerView.setAdapter(adapter);

        mSoundTitle = findViewById(R.id.effect_sound_title_tv);
        mSoundTitle.setText(R.string.aliyun_svideo_title_effect_sound);
        Drawable top = getContext().getResources().getDrawable(R.mipmap.alivc_svideo_icon_tab_filter);
        top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());
        mSoundTitle.setCompoundDrawables(top, null, null,null );
    }

    @Override
    public boolean isPlayerNeedZoom() {
        return false;
    }
}
