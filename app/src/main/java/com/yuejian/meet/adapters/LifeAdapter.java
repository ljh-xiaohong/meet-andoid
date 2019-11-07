package com.yuejian.meet.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yuejian.meet.bean.LifeEntity;
import com.yuejian.meet.utils.BlurUtils;
import com.yuejian.meet.widgets.RecommendView;

import java.util.ArrayList;
import java.util.List;

public class LifeAdapter extends BaseAdapter<LifeAdapter.ViewHolder, LifeEntity> {


    public LifeAdapter(RecyclerView recyclerView, Context context) {
        super(recyclerView, context);
    }

    @Override
    public void refresh(List<LifeEntity> recommendEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }else {
            data.clear();
        }
        data = recommendEntities;
        notifyDataSetChanged();
    }

    @Override
    public void Loadmore(List<LifeEntity> lifeEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(lifeEntities);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new RecommendView(context));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        LifeEntity entity = data.get(position);

        if (!(holder.itemView instanceof RecommendView)) return;
        RecommendView rv = (RecommendView) holder.itemView;
        holder.itemView.setOnClickListener(v ->
                listener.onItemClick(holder.itemView,position))
        ;
        switch (entity.getType()) {
            //文章
            case 2:
                rv.setViewStatus(RecommendView.ViewType.ARTICLE, ViewGroup.LayoutParams.WRAP_CONTENT);
                Glide.with(context).load(entity.getPhotoAndVideoUrl()).asBitmap().into(rv.article_img);
                rv.article_content.setText(entity.getTitle());
                rv.article_tag.setVisibility(View.INVISIBLE);
                rv.setLike(RecommendView.ViewType.ARTICLE, false, entity.getFabulousNum() + "");
                break;
            //视频
            case 4:
                if (entity.getCoveSizeType() == 0) {
                    rv.setViewStatus(RecommendView.ViewType.VIDEO_VERTICAL, itemHeight);
                    Glide.with(context).load(entity.getPhotoAndVideoUrl()).into(rv.video_vertical_img);
                    rv.video_vertical_content.setText(entity.getTitle());
                    rv.video_vertical_tag.setVisibility(View.INVISIBLE);
                    rv.setLike(RecommendView.ViewType.VIDEO_VERTICAL, false, entity.getFabulousNum() + "");
                } else {
                    rv.setViewStatus(RecommendView.ViewType.VIDEO_HORIZONTAL, itemHeight);
                    rv.video_horizontal_video.getLayoutParams().height = itemWidth / 16 * 9;
                    rv.video_horizontal_content.setText(entity.getTitle());
                    rv.setLike(RecommendView.ViewType.VIDEO_HORIZONTAL, false, entity.getFabulousNum() + "");
                    rv.video_horizontal_tag.setVisibility(View.INVISIBLE);
                    Glide.with(context).load(entity.getPhotoAndVideoUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            rv.video_horizontal_video.setImageBitmap(resource);
//                            rv.video_horizontal_blur.setBackground(new BitmapDrawable(BlurUtils.doBlur(resource, 30, false)));
                            rv.video_horizontal_blur.setBackground(new BitmapDrawable(BlurUtils.rsBlur(context, resource, 30)));
                        }
                    });
                }
                break;
        }


    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
