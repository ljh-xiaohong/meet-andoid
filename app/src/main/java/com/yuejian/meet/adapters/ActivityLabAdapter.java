package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.video.common.utils.FastClickUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yuejian.meet.bean.ActivityLabEntity;
import com.yuejian.meet.widgets.RecommendView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class ActivityLabAdapter extends BaseAdapter<ActivityLabAdapter.ActivityLab, ActivityLabEntity.Content> {

    public ActivityLabAdapter(RecyclerView recyclerView, Context context) {
        super(recyclerView, context);
        this.layoutManager.setAutoMeasureEnabled(true);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void refresh(List<ActivityLabEntity.Content> activityLabEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data = activityLabEntities;
        notifyDataSetChanged();
    }

    @Override
    public void Loadmore(List<ActivityLabEntity.Content> activityLabEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(activityLabEntities);
        notifyDataSetChanged();
    }

    @Override
    public ActivityLab onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ActivityLab(new RecommendView(context));


    }

    @Override
    public void onBindViewHolder(ActivityLab holder, int position) {
        ActivityLabEntity.Content entity = data.get(position);
        if (!(holder.itemView instanceof RecommendView)) return;
        RecommendView rv = (RecommendView) holder.itemView;

        switch (entity.getType()) {
            //文章
            case 2:
                rv.setViewStatus(RecommendView.ViewType.ARTICLE, ViewGroup.LayoutParams.WRAP_CONTENT);
                rv.article_img.getLayoutParams().height = itemWidth;
                Glide.with(context).load(entity.getCoverPhoto()).into(rv.article_img);
                rv.article_tag.setText(entity.getLabelName());
                rv.article_content.setText(entity.getTitle());
                rv.setLike(RecommendView.ViewType.ARTICLE, entity.isIsPraise(), entity.getFabulousNum() + "");


                break;

            //视频
            case 4:

                if (entity.getCoveSizeType() == 0) {
                    rv.setViewStatus(RecommendView.ViewType.VIDEO_VERTICAL, itemHeight);
                    Glide.with(context).load(entity.getCoverPhoto()).into(rv.video_vertical_img);
                    rv.video_vertical_content.setText(entity.getTitle());
                    rv.video_vertical_tag.setText(entity.getLabelName());
                    rv.setLike(RecommendView.ViewType.VIDEO_VERTICAL, entity.isIsPraise(), entity.getFabulousNum() + "");
                } else {
                    rv.setViewStatus(RecommendView.ViewType.VIDEO_HORIZONTAL, itemHeight);
                    Glide.with(context).load(entity.getCoverPhoto()).bitmapTransform(new BlurTransformation(context, 30)).into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            rv.video_horizontal_blur.setBackground(glideDrawable);
                        }
                    });
                    Glide.with(context).load(entity.getCoverPhoto()).into(rv.video_horizontal_video);

                    rv.video_horizontal_video.getLayoutParams().height = itemWidth / 16 * 9;
                    rv.video_horizontal_content.setText(entity.getTitle());
                    rv.video_horizontal_tag.setText(entity.getLabelName());
                    rv.setLike(RecommendView.ViewType.VIDEO_HORIZONTAL, entity.isIsPraise(), entity.getFabulousNum() + "");
                }


                break;

            //模板
            case 3:
                rv.setViewStatus(RecommendView.ViewType.NONE, 0);
//                rv.setViewStatus(RecommendView.ViewType.MOULD, itemHeight);
//                Glide.with(context).load(entity.getCoverUrl()).into(rv.mould_img);
//                rv.mould_content.setText(entity.getTitle());
//                if (entity.getCustomerPhoto() != null && entity.getCustomerPhoto().size() > 0) {
//                    rv.mould_pictureLayout.initInformations(entity.getCustomerPhoto(), entity.getUseNumber() + "人在使用", context);
//                }
//                if (entity.getLabelName() != null) {
//                    String[] labs = entity.getLabelName().trim().replaceAll(" ", "").split("#");
//                    if (labs != null && labs.length > 0) {
//                        for (String s : labs) {
//                            TextView lab = new TextView(context, rv.attributes);
//                            lab.setText(s);
//                            rv.mould_tag_layout.addView(lab);
//                        }
//
//                    }
//                }

                break;

            //活动
//            case 4:
//                rv.setViewStatus(RecommendView.ViewType.ACTIVITY, itemHeight);
//                rv.activity_title.setText(entity.getTitle());
//                if (entity.getCustomerPhoto() != null && entity.getCustomerPhoto().size() > 0) {
//                    rv.activity_picturelayout.initInformations(entity.getCustomerPhoto(), entity.getJoinnumTrue() + "人参与>", context);
//                }
//
//                Glide.with(context).load(entity.getCoverUrl()).bitmapTransform(new BlurTransformation(context,30)).into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        rv.activity_img_blur.setBackground(glideDrawable);
//                    }
//                });
//
//                break;
//
            default:
                rv.setViewStatus(RecommendView.ViewType.NONE, 0);
                break;
        }

    }

    class ActivityLab extends RecyclerView.ViewHolder {

        public ActivityLab(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (FastClickUtil.isFastClick()) {
                    return;
                }
                if (listener != null) listener.onItemClick(view, getAdapterPosition());
            });
        }
    }


}
