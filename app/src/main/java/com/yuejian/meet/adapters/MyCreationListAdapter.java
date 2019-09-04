package com.yuejian.meet.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.MineCreationEntity;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : g000gle
 * @time : 2019/6/17 11:00
 * @desc : 我的创作列表适配器
 */
public class MyCreationListAdapter extends RecyclerView.Adapter<MyCreationListAdapter.MyCreationListViewHolder> {

    private List<MineCreationEntity> mMineCreationEntities;
    private Context mContext;
    private Activity mActivity;
    private ApiImp apiImp;
    private OnCreationListListener mListItemClickListener;
    public String article_photo="http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/2019062821201820186833.png";

    public MyCreationListAdapter(Context context, ApiImp apiImp, OnCreationListListener listItemClickListener,Activity activity) {
        mContext = context;
        mActivity =activity;
        this.apiImp = apiImp;
        mListItemClickListener = listItemClickListener;
    }

    @Override
    public MyCreationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_family_circle_follow, parent, false);
        return new MyCreationListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyCreationListViewHolder holder, int position) {
        MineCreationEntity entity = mMineCreationEntities.get(position);
        String headUrl = entity.photo;
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.headImageView);
        }
        holder.nameTextView.setText(String.format("%s", entity.name));
        String createdTime = TimeUtils.formatDateTime(new Date(entity.create_time));
        holder.createdTimeView.setText(createdTime);
        holder.contentTextView.setText(entity.title);
        String imgUrl = entity.photo_and_video_url;
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(mContext).load(imgUrl).placeholder(R.mipmap.loading_pic)
                    .error(R.mipmap.loading_unpic).centerCrop().into(holder.contentImgView);
        }
        boolean is_praise = entity.is_praise;
        holder.zanIcon.setImageResource(is_praise ? R.mipmap.icon_pingjia_zan_sel : R.mipmap.icon_mine_zan_nor);
        holder.zanBtn.setOnClickListener(v -> {
            Map<String, Object> params = new HashMap<>();
            params.put("customer_id", AppConfig.userEntity.customer_id);
            params.put("articles_id", String.valueOf(entity.id));
            apiImp.praiseArticles(params, this, new DataIdCallback<String>() {
                @Override
                public void onSuccess(String data, int id) {
                    try {
                        JSONObject result = new JSONObject(data);
                        int is_praise = result.getInt("is_praise");
                        holder.zanIcon.setImageResource(is_praise == 1 ? R.mipmap.icon_pingjia_zan_sel : R.mipmap.icon_mine_zan_nor);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }
            });
        });

        holder.shareBtn.setOnClickListener(v -> {
            Glide.with(mContext).load(article_photo).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    String title = entity.title;
                    String shareUrl = "http://app2.yuejianchina.com/yuejian-app/release/blank.html?type="+entity.type+"&id="+entity.id;
                    Utils.umengShareByList(mActivity, resource, title, "来自 约见·百家姓", shareUrl);
                }
            });
        });

        holder.itemView.setOnClickListener(v -> mListItemClickListener.onListItemClick(entity.type, entity.id));
    }

    @Override
    public int getItemCount() {
        if (mMineCreationEntities == null) return 0;
        return mMineCreationEntities.size();
    }

    public void refresh(List<MineCreationEntity> mineCreationEntities) {
        if (this.mMineCreationEntities == null) {
            this.mMineCreationEntities = new ArrayList<>();
        }
        this.mMineCreationEntities = mineCreationEntities;
        notifyDataSetChanged();
    }

    public interface OnCreationListListener {
        void onListItemClick(int type, int id);
    }

    class MyCreationListViewHolder extends RecyclerView.ViewHolder {

        ImageView contentImgView;
        TextView contentTextView;
        TextView nameTextView;
        TextView createdTimeView;
        ImageView zanIcon;
        LinearLayout zanBtn;
        LinearLayout shareBtn;
        CircleImageView headImageView;

        public MyCreationListViewHolder(View itemView) {
            super(itemView);
            headImageView = (CircleImageView) itemView.findViewById(R.id.iv_family_follow_item_head);
            contentImgView = (ImageView) itemView.findViewById(R.id.iv_family_follow_item_img);
            contentTextView = (TextView) itemView.findViewById(R.id.tv_family_follow_item_content);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_family_follow_item_name);
            zanBtn = (LinearLayout) itemView.findViewById(R.id.ll_family_follow_zan_root);
            zanIcon = (ImageView) itemView.findViewById(R.id.iv_family_follow_zan_icon);
            shareBtn = (LinearLayout) itemView.findViewById(R.id.ll_family_follow_share_root);
            createdTimeView = (TextView) itemView.findViewById(R.id.tv_family_follow_item_created_time);
        }
    }
}
