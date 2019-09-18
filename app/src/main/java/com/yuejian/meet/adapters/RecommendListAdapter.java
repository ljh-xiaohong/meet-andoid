package com.yuejian.meet.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import com.yuejian.meet.bean.RecommendEntity;
import com.yuejian.meet.utils.BlurUtils;
import com.yuejian.meet.widgets.RecommendView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class RecommendListAdapter extends BaseAdapter<RecommendListAdapter.RecommendHoldView, RecommendEntity> {


    public RecommendListAdapter(RecyclerView recyclerView, Context context) {
        super(recyclerView, context);
    }


    @Override
    public RecommendHoldView onCreateViewHolder(ViewGroup parent, int viewType) {

        return new RecommendHoldView(new RecommendView(context));
    }

    @Override
    public void refresh(List<RecommendEntity> recommendEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data = recommendEntities;
        notifyDataSetChanged();
    }

    @Override
    public void Loadmore(List<RecommendEntity> recommendEntities) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.addAll(recommendEntities);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(RecommendHoldView holder, int position) {
        RecommendEntity entity = data.get(position);
        if (!(holder.itemView instanceof RecommendView)) return;
        RecommendView rv = (RecommendView) holder.itemView;


        switch (entity.getType()) {
            //文章
            case 1:
                rv.setViewStatus(RecommendView.ViewType.ARTICLE, ViewGroup.LayoutParams.WRAP_CONTENT);
                rv.article_img.getLayoutParams().height = itemWidth;
                Glide.with(context).load(entity.getCoverUrl()).into(rv.article_img);
                rv.article_tag.setText(entity.getLabelName());
                rv.article_content.setText(entity.getTitle());
                rv.setLike(RecommendView.ViewType.ARTICLE, entity.isIsPraise(), entity.getFabulousNum() + "");


                break;

            //视频
            case 2:

                if (entity.getCoveSizeType() == 0) {
                    rv.setViewStatus(RecommendView.ViewType.VIDEO_VERTICAL, itemHeight);
                    Glide.with(context).load(entity.getCoverUrl()).into(rv.video_vertical_img);
                    rv.video_vertical_content.setText(entity.getTitle());
                    rv.video_vertical_tag.setText(entity.getLabelName());
                    rv.setLike(RecommendView.ViewType.VIDEO_VERTICAL, entity.isIsPraise(), entity.getFabulousNum() + "");
                } else {
                    rv.setViewStatus(RecommendView.ViewType.VIDEO_HORIZONTAL, itemHeight);
//                    Glide.with(context).load(entity.getCoverUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            rv.video_horizontal_blur.setBackground(new BitmapDrawable(BlurUtils.doBlur(resource, 30, false)));
//                            rv.video_horizontal_video.setImageBitmap(resource);
//                        }
//                    });
                    Glide.with(context).load(entity.getCoverUrl()).bitmapTransform(new BlurTransformation(context,30)).into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            rv.video_horizontal_blur.setBackground(glideDrawable);
                        }
                    });
                    Glide.with(context).load(entity.getCoverUrl()).into(rv.video_horizontal_video);

                    rv.video_horizontal_video.getLayoutParams().height = itemWidth / 16 * 9;
                    rv.video_horizontal_content.setText(entity.getTitle());
                    rv.video_horizontal_tag.setText(entity.getLabelName());
                    rv.setLike(RecommendView.ViewType.VIDEO_HORIZONTAL, entity.isIsPraise(), entity.getFabulousNum() + "");
                }


//                Glide.with(context).load(entity.getCoverUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//
//                        if (null != resource) {
//                            if (resource.getHeight() > resource.getWidth()) {
//                                //竖屏
//
//                            } else {
//                                //横屏
//
//
//                            }
//
//                        }
//
////                        if (entity.getTitle().equalsIgnoreCase("测试标题99924") || entity.getTitle().equalsIgnoreCase("测试标题99925")) {
////                            rv.setViewStatus(RecommendView.ViewType.VIDEO_HORIZONTAL, itemHeight);
////                            rv.video_horizontal_blur.setBackground(new BitmapDrawable(BlurUtils.doBlur(resource, 30, false)));
////                            rv.video_horizontal_video.getLayoutParams().height = itemWidth / 16 * 9;
////                            rv.video_horizontal_video.setImageBitmap(resource);
////                            rv.video_horizontal_content.setText(entity.getTitle());
////                            rv.video_horizontal_tag.setText(entity.getLabelName());
////                            rv.video_horizontal_like.setCompoundDrawablesWithIntrinsicBounds(
////                                    context.getResources().getDrawable(entity.isIsPraise() ? R.mipmap.icon_zan_sel : R.mipmap.icon_zan_nor_w),
////                                    null,
////                                    null,
////                                    null);
////                            rv.video_horizontal_like.setText(entity.getFabulousNum() + "");
////                        }
//
//                    }
//                });

                break;

            //模板
            case 3:
                rv.setViewStatus(RecommendView.ViewType.MOULD, itemHeight);
                Glide.with(context).load(entity.getCoverUrl()).into(rv.mould_img);
                rv.mould_content.setText(entity.getTitle());
                if (entity.getCustomerPhoto() != null && entity.getCustomerPhoto().size() > 0) {
                    rv.mould_pictureLayout.initInformations(entity.getCustomerPhoto(), entity.getUseNumber() + "人在使用", context);
                }
                if (entity.getLabelName() != null) {
                    String[] labs = entity.getLabelName().trim().replaceAll(" ", "").split("#");
                    if (labs != null && labs.length > 0) {
                        for (String s : labs) {
                            TextView lab = new TextView(context, rv.attributes);
                            lab.setText(s);
                            rv.mould_tag_layout.addView(lab);
                        }

                    }
                }

                break;

            //活动
            case 4:
                rv.setViewStatus(RecommendView.ViewType.ACTIVITY, itemHeight);
                rv.activity_title.setText(entity.getTitle());
                if (entity.getCustomerPhoto() != null && entity.getCustomerPhoto().size() > 0) {
                    rv.activity_picturelayout.initInformations(entity.getCustomerPhoto(), entity.getJoinnumTrue() + "人参与>", context);
                }
//                Glide.with(context).load(entity.getCoverUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        if (null != resource) {
//                            rv.activity_img_blur.setImageBitmap(BlurUtils.doBlur(resource, 30, false));
//                        }
//                    }
//                });

                Glide.with(context).load(entity.getCoverUrl()).bitmapTransform(new BlurTransformation(context,30)).into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        rv.activity_img_blur.setBackground(glideDrawable);
                    }
                });

                break;

            default:
//                rv.setViewStatus(RecommendView.ViewType.NONE, 0);
                break;
        }

    }


    @Override
    public int getItemCount() {

        return super.getItemCount();
    }

    class RecommendHoldView extends RecyclerView.ViewHolder {


        public RecommendHoldView(View itemView) {
            super(itemView);
            if (listener != null) {
                itemView.setOnClickListener(view -> {
                    listener.onItemClick(view, this.getAdapterPosition());
                });
            }
        }
    }


}
