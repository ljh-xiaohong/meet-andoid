package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yuejian.meet.bean.DraftEntity;
import com.yuejian.meet.widgets.RecommendView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class DraftAdapter extends BaseAdapter<DraftAdapter.ViewHolder,DraftEntity> {

    private int type;

    public DraftAdapter(RecyclerView recyclerView, Context context, int type) {
        super(recyclerView, context);
        this.type = type;
    }

    @Override
    public void refresh(List<DraftEntity> draftEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data = draftEntities;
        notifyDataSetChanged();
    }

    @Override
    public void Loadmore(List<DraftEntity> draftEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(draftEntities);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new RecommendView(context));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DraftEntity entity = data.get(position);

        if (!(holder.itemView instanceof RecommendView)) return;
        RecommendView rv = (RecommendView) holder.itemView;

        switch (type) {
            //文章
            case 1:
                rv.setViewStatus(RecommendView.ViewType.ARTICLE, ViewGroup.LayoutParams.WRAP_CONTENT);
                Glide.with(context).load(entity.getPhotoAndVideoUrl()).into(rv.article_img);
                rv.article_tag.setText(entity.getLabelTitle());
                rv.article_content.setText(entity.getContentTitle());

                break;
            //视频
            case 2:
                //0竖屏1横屏
                if (entity.getCoverSizeType() == 0) {
                    rv.setViewStatus(RecommendView.ViewType.VIDEO_VERTICAL, itemHeight);
                    Glide.with(context).load(entity.getPhotoAndVideoUrl()).into(rv.video_vertical_img);
                    rv.video_vertical_content.setText(entity.getContentTitle());
                    rv.video_vertical_tag.setText(entity.getLabelTitle());

                } else {
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
                    rv.video_horizontal_tag.setText(entity.getLabelTitle());


                }
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (listener != null) listener.onItemClick(view, getAdapterPosition());
            });
        }
    }


}
