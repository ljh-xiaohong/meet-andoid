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

import com.aliyun.demo.recorder.util.gles.GeneratedTexture;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nim.uikit.common.util.media.BitmapUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.FamilySameCityEntity;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/5/14 19:20
 * @desc : 家圈同城列表适配器
 */
public class FamilyCircleSameCityListAdapter extends RecyclerView.Adapter<FamilyCircleSameCityListAdapter.FamilyCircleSameCityListViewHolder> {

    private List<FamilySameCityEntity> sameCityEntities;
    private Context mContext;
    private OnListItemClickListener mListItemClickListener;

    public FamilyCircleSameCityListAdapter(Context context, OnListItemClickListener listItemClickListener) {
        mContext = context;
        mListItemClickListener = listItemClickListener;
    }

    @Override
    public FamilyCircleSameCityListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_family_circle_recommend, parent, false);
        return new FamilyCircleSameCityListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FamilyCircleSameCityListViewHolder holder, int position) {
        FamilySameCityEntity entity = sameCityEntities.get(position);
        String imgUrl = entity.photo_and_video_url;
        if (!TextUtils.isEmpty(imgUrl)) {
//            Glide.with(mContext).load(imgUrl).placeholder(R.mipmap.loading_pic)
//                    .error(R.mipmap.loading_unpic).into(holder.contentImgView);


            Glide.with(mContext).load(imgUrl).asBitmap().placeholder(R.mipmap.default_pic).error(R.mipmap.default_unpic).into(new SimpleTarget<Bitmap>() {
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

        //根据类型获取图片
        //类型：1-随笔，2-文章，3-相册，4-视频
        switch (entity.type) {
            case 2:
                holder.typeImgView.setImageResource(R.mipmap.label_article);
                break;

            case 4:
                holder.typeImgView.setImageResource(R.mipmap.label_video);
                break;
        }

        String headUrl = entity.photo;
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.headImageView);
        }
        holder.contentTextView.setText(entity.title);
        holder.nameTextView.setText(String.format("%s", entity.name));
        holder.zanCountView.setText(String.valueOf(entity.fabulous_num));
        boolean is_praise = entity.is_praise;
        holder.zanIconView.setImageResource(is_praise ? R.mipmap.icon_pingjia_zan_sel : R.mipmap.icon_zan_nor);
        holder.itemView.setOnClickListener(v -> mListItemClickListener.onItemClick(entity.type, entity.id));
    }

    @Override
    public int getItemCount() {
        if (sameCityEntities == null) return 0;
        return sameCityEntities.size();
    }

    public void refresh(List<FamilySameCityEntity> recommendEntities) {
        if (this.sameCityEntities == null) {
            this.sameCityEntities = new ArrayList<>();
        }
        this.sameCityEntities = recommendEntities;
        notifyDataSetChanged();
    }

    public void Loadmore(List<FamilySameCityEntity> recommendEntities) {
        if (this.sameCityEntities == null) {
            this.sameCityEntities = new ArrayList<>();
        }
        this.sameCityEntities.addAll(recommendEntities);
        notifyDataSetChanged();
    }

    public interface OnListItemClickListener {
        void onItemClick(int type, int id);
    }

    class FamilyCircleSameCityListViewHolder extends RecyclerView.ViewHolder {

        ImageView contentImgView;
        TextView contentTextView;
        TextView nameTextView;
        TextView zanCountView;
        ImageView zanIconView;
        CircleImageView headImageView;
        ImageView typeImgView;

        public FamilyCircleSameCityListViewHolder(View itemView) {
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
