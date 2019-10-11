package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.video.common.utils.FastClickUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.CreationEntity;
import com.yuejian.meet.widgets.RecommendView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class CreationAdapter extends BaseAdapter<CreationAdapter.ViewHolder, CreationEntity> {

    private int type;
    private boolean hasDraftBox;  //是否显示草稿箱

    public CreationAdapter(RecyclerView recyclerView, Context context, int type, boolean hasDraftBox) {
        super(recyclerView, context);
        this.type = type;
        this.hasDraftBox = hasDraftBox;
    }

    @Override
    public void refresh(List<CreationEntity> creationEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        if (hasDraftBox && (type == 1 || type == 2)) {
            CreationEntity entity = new CreationEntity();
            entity.setDraftsId(type);
            if (creationEntities == null) creationEntities = new ArrayList<>();
            creationEntities.add(0, entity);
        }
        data = creationEntities;
        notifyDataSetChanged();
    }

    @Override
    public void Loadmore(List<CreationEntity> creationEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(creationEntities);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new RecommendView(context));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CreationEntity entity = data.get(position);

        if (!(holder.itemView instanceof RecommendView)) return;
        RecommendView rv = (RecommendView) holder.itemView;

        switch (type) {
            //海报模板
            case 3:
                rv.setViewStatus(RecommendView.ViewType.POSTER, itemHeight);
                Glide.with(context).load(entity.getPreviewUrl()).into(rv.poster_img);
                rv.poster_content.setText(entity.getContentTitle());
                if (rv.poster_tag.getChildCount() == 0) {
                    TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.textview_tag, null);
                    textView.setText(entity.getLabelName().replaceAll("#", ""));
                    rv.poster_tag.addView(textView);
                } else {
                    TextView textView = (TextView) rv.poster_tag.getChildAt(0);
                    if (textView == null) return;
                    textView.setText(entity.getLabelName().replaceAll("#", ""));
                }

                break;
            //视频
            case 2:
                //第一个默认草稿
                if (position == 0 && hasDraftBox) {
                    rv.setViewStatus(RecommendView.ViewType.DRAFT, itemHeight);
                    return;
                }
                if (entity.getCoverSizeType().equals("0")) {
                    //竖屏
                    rv.setViewStatus(RecommendView.ViewType.VIDEO_VERTICAL, itemHeight);
                    Glide.with(context).load(entity.getPhotoAndVideoUrl()).into(rv.video_vertical_img);
                    rv.video_vertical_content.setText(entity.getContentTitle());
                    if (!TextUtils.isEmpty(entity.getLabelName())) {
                        rv.video_vertical_tag.setText(entity.getLabelName().replaceAll("#", ""));
                        rv.video_vertical_tag.setVisibility(View.VISIBLE);
                    } else {
                        rv.video_vertical_tag.setVisibility(View.GONE);
                    }
                    rv.setLike(RecommendView.ViewType.VIDEO_VERTICAL, entity.isPraise(), entity.getFabulousNum() + "");
                } else {
                    //横屏
                    rv.setViewStatus(RecommendView.ViewType.VIDEO_HORIZONTAL, itemHeight);
                    Glide.with(context).load(entity.getPhotoAndVideoUrl()).bitmapTransform(new BlurTransformation(context, 30)).into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            rv.video_horizontal_blur.setBackground(glideDrawable);
                        }
                    });
                    Glide.with(context).load(entity.getPhotoAndVideoUrl()).into(rv.video_horizontal_video);

                    rv.video_horizontal_video.getLayoutParams().height = itemWidth / 16 * 9;
                    rv.video_horizontal_content.setText(entity.getContentTitle());
                    if (!TextUtils.isEmpty(entity.getLabelName())) {
                        rv.video_horizontal_tag.setText(entity.getLabelName().replaceAll("#", ""));
                        rv.video_horizontal_tag.setVisibility(View.VISIBLE);
                    } else {
                        rv.video_horizontal_tag.setVisibility(View.GONE);
                    }

                    rv.setLike(RecommendView.ViewType.VIDEO_HORIZONTAL, entity.isPraise(), entity.getFabulousNum() + "");
                }

                break;
            //文章
            case 1:
                //第一个默认草稿
                if (position == 0 && hasDraftBox) {
                    rv.setViewStatus(RecommendView.ViewType.DRAFT, itemHeight);
                    return;
                }
                rv.setViewStatus(RecommendView.ViewType.ARTICLE, ViewGroup.LayoutParams.WRAP_CONTENT);
                Glide.with(context).load(entity.getPhotoAndVideoUrl()).into(rv.article_img);
                rv.setLike(RecommendView.ViewType.ARTICLE, entity.isPraise(), entity.getFabulousNum() + "");
                rv.article_content.setText(entity.getContentTitle());
                rv.article_tag.setText(entity.getLabelName());
                break;
        }


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (listener != null) listener.onItemClick(itemView, getAdapterPosition());
            });

        }
    }
}
