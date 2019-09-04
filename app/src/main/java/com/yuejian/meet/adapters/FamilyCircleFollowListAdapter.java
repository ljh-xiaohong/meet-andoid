package com.yuejian.meet.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.ArticleInfoActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.AVData;
import com.yuejian.meet.bean.FamilyFollowEntity;
import com.yuejian.meet.bean.ZanBean;
import com.yuejian.meet.utils.FolderTextView;
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
 * @time : 2019/5/14 19:20
 * @desc : 家圈关注列表适配器
 */
public class FamilyCircleFollowListAdapter extends RecyclerView.Adapter<FamilyCircleFollowListAdapter.FamilyCircleFollowListViewHolder> {

    private List<FamilyFollowEntity.DataBean> mFollowEntities;
    private Context mContext;
    private Activity mActivity;
    private OnFollowListItemClickListener mListItemClickListener;
    private ApiImp apiImp;
    public String article_photo="http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/2019062821201820186833.png";
    private int check_num=0;
    private boolean isCheckeds;
    private onClickListener mOnClickListener;

    public void setOnClickListener(onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClick(int position);
    }
    public FamilyCircleFollowListAdapter(Context context, OnFollowListItemClickListener listItemClickListener, ApiImp apiImp, Activity activity) {
        mContext = context;
        mActivity =activity;
        mListItemClickListener = listItemClickListener;
        this.apiImp = apiImp;
    }

    @Override
    public FamilyCircleFollowListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_family_circle_follow, parent, false);
        return new FamilyCircleFollowListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FamilyCircleFollowListViewHolder holder, int position) {
        FamilyFollowEntity.DataBean entity = mFollowEntities.get(position);
        String headUrl = entity.getPhoto();
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.headImageView);
        }
        holder.nameTextView.setText(String.format("%s", entity.getName()));
        String createdTime = TimeUtils.formatDateTime(new Date(entity.getCreateTime()));
        holder.createdTimeView.setText(createdTime);
        holder.contentTextView.setText(entity.getTitle());
//        String imgUrl = entity.get;

//        if (!TextUtils.isEmpty(imgUrl)) {
//            Glide.with(mContext).load(imgUrl).placeholder(R.mipmap.loading_pic)
//                    .error(R.mipmap.loading_unpic).centerCrop().into(holder.contentImgView);
//        }
//        holder.iv_family_follow_item_list.setOnClickListener(v -> mListItemClickListener.onListItemClick(entity.type, entity.id));
//        boolean is_praise = entity.get;
//        holder.zanIcon.setImageResource(is_praise ? R.mipmap.icon_pingjia_zan_sel : R.mipmap.icon_mine_zan_nor);
//        holder.zan_check.setOnCheckedChangeListener((buttonView, isChecked)->{
//            if (isChecked){
//                check_num=check_num+1;
//                holder.zan_check_num.setText(check_num+"");
//            }else {
//                check_num=check_num-1;
//                holder.zan_check_num.setText(check_num+"");
//            }
//        });
        if (entity.getVipType()==1) {
            holder.tv_family_follow_item_name_tag.setVisibility(View.VISIBLE);
        }else {
            holder.tv_family_follow_item_name_tag.setVisibility(View.GONE);
        }
        holder.zan_check_num.setText(entity.getCommentNum()+"");
        holder.more_operation.setOnClickListener(v -> {
              mOnClickListener.onClick(position);
        });
        holder.zan_check.setOnClickListener(v -> {
            Map<String, Object> params = new HashMap<>();
            params.put("customerId", AppConfig.CustomerId);
            params.put("crId", String.valueOf(entity.getId()));
            apiImp.praiseArticles(params, this, new DataIdCallback<String>() {
                @Override
                public void onSuccess(String data, int id) {
                    try {
                        ZanBean zanBean= new Gson().fromJson(data,ZanBean.class);
                        if (zanBean.getCode()==0){
                            holder.zan_check.setChecked(zanBean.getData().getIsPraise() == 1 ?true : false);
                            holder.zan_check_num.setText(zanBean.getData().getPraiseCnt()+"");
                        }else {
                            holder.zan_check.setChecked(false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }
            });
        });
//        holder.commentBtn.setOnClickListener(v -> mListItemClickListener.onListItemClick(entity.type, entity.id));
//        holder.shareBtn.setOnClickListener(v -> {
//            Glide.with(mContext).load(article_photo).asBitmap().into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    String title = entity.title;
//                    String shareUrl = "http://app2.yuejianchina.com/yuejian-app/release/blank.html?type="+entity.type+"&id="+entity.id;
//                    Utils.umengShareByList(mActivity, resource, title, "来自 约见·百家姓", shareUrl);
//                }
//            });
//        });
    }

    @Override
    public int getItemCount() {
        if (mFollowEntities == null) return 0;
        return mFollowEntities.size();
    }

    public void refresh(List<FamilyFollowEntity.DataBean> followEntities) {
        if (this.mFollowEntities == null) {
            this.mFollowEntities = new ArrayList<>();
        }
        this.mFollowEntities = followEntities;
        notifyDataSetChanged();
    }

    public void Loadmore(List<FamilyFollowEntity.DataBean> followEntities) {
        if (this.mFollowEntities == null) {
            this.mFollowEntities = new ArrayList<>();
        }
            this.mFollowEntities.addAll(followEntities);
            notifyDataSetChanged();
    }

    public interface OnFollowListItemClickListener {
        void onListItemClick(int type, int id);
    }

    class FamilyCircleFollowListViewHolder extends RecyclerView.ViewHolder {

        ImageView contentImgView;
        FolderTextView contentTextView;
        TextView nameTextView;
        TextView createdTimeView;
        CircleImageView headImageView;
        LinearLayout zanBtn;
        ImageView zanIcon;
        LinearLayout commentBtn;
        LinearLayout shareBtn;


        RecyclerView comment_list,iv_family_follow_item_list;
        CheckBox zan_check;
        TextView zan_check_num;
        ImageView more_operation,tv_family_follow_item_name_tag;
        public FamilyCircleFollowListViewHolder(View itemView) {
            super(itemView);
            headImageView = (CircleImageView) itemView.findViewById(R.id.iv_family_follow_item_head);
            contentImgView = (ImageView) itemView.findViewById(R.id.iv_family_follow_item_img);
            contentTextView = (FolderTextView) itemView.findViewById(R.id.tv_family_follow_item_content);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_family_follow_item_name);
            createdTimeView = (TextView) itemView.findViewById(R.id.tv_family_follow_item_created_time);
            zanBtn = (LinearLayout) itemView.findViewById(R.id.ll_family_follow_zan_root);
            zanIcon = (ImageView) itemView.findViewById(R.id.iv_family_follow_zan_icon);
            commentBtn = (LinearLayout) itemView.findViewById(R.id.ll_family_follow_comment_root);
            shareBtn = (LinearLayout) itemView.findViewById(R.id.ll_family_follow_share_root);


            comment_list=itemView.findViewById(R.id.comment_list);
            iv_family_follow_item_list=itemView.findViewById(R.id.iv_family_follow_item_list);
            zan_check=itemView.findViewById(R.id.zan_check);
            zan_check_num=itemView.findViewById(R.id.zan_check_num);
            more_operation=itemView.findViewById(R.id.more_operation);
            tv_family_follow_item_name_tag=itemView.findViewById(R.id.tv_family_follow_item_name_tag);
        }
    }
}
