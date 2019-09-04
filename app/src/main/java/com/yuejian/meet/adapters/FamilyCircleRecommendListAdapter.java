package com.yuejian.meet.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.media.BitmapUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.FamilyRecommendEntity;
import com.yuejian.meet.widgets.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : g000gle
 * @time : 2019/5/14 19:20
 * @desc : 家圈推荐列表适配器
 */
public class FamilyCircleRecommendListAdapter extends RecyclerView.Adapter<FamilyCircleRecommendListAdapter.FamilyCircleRecommendListViewHolder> {

    private List<FamilyRecommendEntity> recommendEntities;
    private Context mContext;
    private OnListItemClickListener mOnListItemClickListener;

    public FamilyCircleRecommendListAdapter(Context context, OnListItemClickListener onListItemClickListener) {
        mContext = context;
        mOnListItemClickListener = onListItemClickListener;
    }

    public List<FamilyRecommendEntity> getRecommendList() {
        return this.recommendEntities;
    }

    @Override
    public FamilyCircleRecommendListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_family_circle_recommend, parent, false);
        return new FamilyCircleRecommendListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FamilyCircleRecommendListViewHolder holder, int position) {
        FamilyRecommendEntity entity = recommendEntities.get(position);
        //根据类型获取图片
        //类型：1-随笔，2-文章，3-相册，4-视频
        switch (entity.getType()) {
            case "2":
                holder.typeImgView.setImageResource(R.mipmap.label_article);
                break;

            case "4":
                holder.typeImgView.setImageResource(R.mipmap.label_video);
                break;
        }

        String imgUrl = entity.getPhoto_and_video_url();
        if (!TextUtils.isEmpty(imgUrl)) {

            Glide.with(mContext).load(imgUrl).asBitmap().placeholder(R.mipmap.loading_pic).error(R.mipmap.loading_unpic).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                    if (BitmapUtil.isLongSize(resource)) {
//                        holder.contentImgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        ViewGroup.LayoutParams lp = holder.contentImgView.getLayoutParams();
                        lp.height = ScreenUtil.getDisplayHeight() / 3;
                        holder.contentImgView.setLayoutParams(lp);
                    } else {
//                        holder.contentImgView.setScaleType(ImageView.ScaleType.FIT_XY);
                        ViewGroup.LayoutParams lp = holder.contentImgView.getLayoutParams();
                        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        holder.contentImgView.setLayoutParams(lp);
                    }

                    holder.contentImgView.setImageBitmap(resource);

                }
            });
        }

        String headUrl = entity.getPhoto();
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.headImageView);
        }
        holder.contentTextView.setText(entity.getTitle());
        holder.nameTextView.setText(String.format("%s", entity.getName()));

        boolean is_praise = Boolean.valueOf(entity.getIs_praise());
        holder.zanIconView.setImageResource(is_praise ? R.mipmap.icon_pingjia_zan_sel : R.mipmap.icon_zan_nor);

        holder.zanCountView.setText(entity.getFabulous_num());
        holder.itemView.setOnClickListener(v -> mOnListItemClickListener.onItemClick(entity));
    }

    @Override
    public int getItemCount() {
        if (recommendEntities == null) return 0;
        return recommendEntities.size();
    }

    public void refresh(List<FamilyRecommendEntity> recommendEntities) {
        if (this.recommendEntities == null) {
            this.recommendEntities = new ArrayList<>();
        }
        this.recommendEntities = recommendEntities;
        notifyDataSetChanged();
    }

    public void Loadmore(List<FamilyRecommendEntity> recommendEntities) {
        if (this.recommendEntities == null) {
            this.recommendEntities = new ArrayList<>();
        }
        this.recommendEntities.addAll(recommendEntities);
        notifyDataSetChanged();
    }

    public interface OnListItemClickListener {
        void onItemClick(FamilyRecommendEntity entity);
    }

    class FamilyCircleRecommendListViewHolder extends RecyclerView.ViewHolder {

        ImageView contentImgView;
        TextView contentTextView;
        TextView nameTextView;
        TextView zanCountView;
        ImageView zanIconView;
        CircleImageView headImageView;
        ImageView typeImgView;

        public FamilyCircleRecommendListViewHolder(View itemView) {
            super(itemView);
            headImageView = (CircleImageView) itemView.findViewById(R.id.iv_family_recommend_item_head);
            contentImgView = (ImageView) itemView.findViewById(R.id.iv_family_recommend_item_img);
            contentTextView = (TextView) itemView.findViewById(R.id.tv_family_recommend_item_content);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_family_recommend_item_name);
            zanIconView = (ImageView) itemView.findViewById(R.id.tv_family_recommend_item_zan_icon);
            zanCountView = (TextView) itemView.findViewById(R.id.tv_family_recommend_item_zan_count);
            typeImgView = (ImageView)itemView.findViewById(R.id.iv_family_recommend_item_type);
        }
    }
}
