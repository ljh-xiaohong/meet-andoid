package com.yuejian.meet.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.adapter.ReleasePicAdapter;
import com.yuejian.meet.activities.custom.view.RoundAngleImageView;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.DynamicPrivatePicBean;
import com.yuejian.meet.bean.FamilyFollowEntity;
import com.yuejian.meet.bean.Image;
import com.yuejian.meet.bean.ZanBean;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.FolderTextView;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.CircleImageView;

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
    private boolean isOne;

    public void setOnClickListener(onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClick(int position, boolean me);
        void onComment(int position);
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

        Date date = new Date(Long.parseLong(entity.getCreateTime())*1000);
        String createdTime = TimeUtils.formatDateTime(date);
        holder.createdTimeView.setText(createdTime);

        if (!CommonUtil.isNull(entity.getVipType())&&Integer.parseInt(entity.getVipType())==1) {
            holder.tv_family_follow_item_name_tag.setVisibility(View.VISIBLE);
        }else {
            holder.tv_family_follow_item_name_tag.setVisibility(View.GONE);
        }
        holder.zan_check_num.setText(entity.getFabulousNum()+"");
        holder.more_operation.setOnClickListener(v -> {
              mOnClickListener.onClick(position,entity.isMe());
        });

        if (Integer.parseInt(entity.getType())==1) {
            holder.img.setVisibility(View.GONE);
            holder.contentImgView.setVisibility(View.GONE);
            holder.contentTextView.setText(entity.getContent());
            if (!CommonUtil.isNull(entity.getPhotoAndVideoUrl())) {
                String[] imgs = entity.getPhotoAndVideoUrl().split(",");
                final List<Image> picData = new ArrayList<>();
                for (int i = 0; i < imgs.length; i++) {
                    Image pb = new Image();
                    pb.setBitmap(null);
                    pb.setUrl("");
                    pb.setSelect(true);
                    pb.setTake(true);
                    pb.setPath(imgs[i]);
                    picData.add(pb);
                }
                if (picData.size() == 1) {
                    isOne = true;
                    GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
                    holder.iv_family_follow_item_list.setLayoutManager(layoutManager);
                } else {
                    isOne = false;
                    GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
                    holder.iv_family_follow_item_list.setLayoutManager(layoutManager);
                }
                ReleasePicAdapter mAdapter = new ReleasePicAdapter(mContext, picData, isOne);
                holder.iv_family_follow_item_list.setAdapter(mAdapter);
                mAdapter.setClickPic(new ReleasePicAdapter.OnClickPic() {
                    @Override
                    public void clickPic(int position) {
                        List<DynamicPrivatePicBean> actionPhoto = new ArrayList<>();
                        List<String> list = new ArrayList<String>();
                        for (int i = 0; i < picData.size(); i++) {
                            actionPhoto.add(new DynamicPrivatePicBean(picData.get(i).getPath(), "1"));
                            list.add(picData.get(i).getPath());
                        }
                        Utils.displayImages((Activity) mContext, list, position, null);
                    }

                    @Override
                    public void delectPic(int position) {

                    }
                });
                mAdapter.notifyDataSetChanged();
                holder.iv_family_follow_item_list.setVisibility(View.VISIBLE);
                holder.iv_family_follow_item_list_lay.setVisibility(View.VISIBLE);
            } else {
                holder.iv_family_follow_item_list.setVisibility(View.GONE);
                holder.iv_family_follow_item_list_lay.setVisibility(View.GONE);
            }
            holder.article_lay.setVisibility(View.GONE);
            holder.contentTextView.setVisibility(View.VISIBLE);
            holder.contentImgView.setVisibility(View.GONE);
            holder.img.setVisibility(View.GONE);
        }else if (Integer.parseInt(entity.getType())==2){
            holder.article_lay.setVisibility(View.VISIBLE);
            holder.contentTextView.setVisibility(View.GONE);
            holder.iv_family_follow_item_list_lay.setVisibility(View.GONE);
            holder.iv_family_follow_item_list.setVisibility(View.GONE);
            holder.contentImgView.setVisibility(View.GONE);
            holder.img.setVisibility(View.GONE);
            holder.article_title.setText(entity.getTitle());
            holder.article_content.setText(entity.getContent());
            if (!TextUtils.isEmpty(entity.getPhotoAndVideoUrl())) {
                Glide.with(mContext).load(entity.getPhotoAndVideoUrl()).into(holder.article_img);
            }
        }else if (Integer.parseInt(entity.getType())==4){
            holder.article_lay.setVisibility(View.GONE);
            holder.iv_family_follow_item_list_lay.setVisibility(View.VISIBLE);
            holder.iv_family_follow_item_list.setVisibility(View.GONE);
            holder.contentImgView.setVisibility(View.VISIBLE);
            holder.contentTextView.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(entity.getPhotoAndVideoUrl())) {
                Glide.with(mContext).load(entity.getPhotoAndVideoUrl()).into(holder.contentImgView);
            }
            if (!CommonUtil.isNull(entity.getTitle())) {
                holder.contentTextView.setText(entity.getTitle());
            }else {
                holder.contentTextView.setText("");
            }
        }


        if (Integer.parseInt(entity.getIsPraise())==1){
            holder.zan_check.setChecked(true);
        }else {
            holder.zan_check.setChecked(false);
        }
        holder.zan_check.setOnClickListener(v -> {
            Map<String, Object> params = new HashMap<>();
            params.put("customerId", AppConfig.CustomerId);
            params.put("crId", String.valueOf(entity.getId()));
            apiImp.praiseArticles(params, this, new DataIdCallback<String>() {
                @Override
                public void onSuccess(String data, int id) {
                    try {
                        ZanBean zanBean= new Gson().fromJson(data, ZanBean.class);
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
        if (entity.getCommentMap().size()>0){
            CommentAdapter commentAdapter=new CommentAdapter((Activity) mContext,entity.getCommentMap());
            holder.comment_list.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
            holder.comment_list.setAdapter(commentAdapter);
            commentAdapter.notifyDataSetChanged();
            holder.comment_list.setVisibility(View.VISIBLE);
            if (entity.getCommentMap().size()>2){
                holder.commentCount.setText("查看" + entity.getCommentMap().size() + "条评论 >");
            }else {
                holder.commentCount.setText("去评论 >");
            }
        }else {
            holder.comment_list.setVisibility(View.GONE);
            holder.commentCount.setText("去评论 >");
        }
        holder.commentCount.setOnClickListener(v -> mOnClickListener.onComment(position));
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
//            this.mFollowEntities.addAll(followEntities);
            notifyDataSetChanged();
    }

    public interface OnFollowListItemClickListener {
        void onListItemClick(int type, int id);
    }

    class FamilyCircleFollowListViewHolder extends RecyclerView.ViewHolder {

        RoundAngleImageView contentImgView,article_img;
        FolderTextView contentTextView;
        TextView commentCount;
        TextView nameTextView;
        TextView createdTimeView,article_title,article_content;
        CircleImageView headImageView;
        LinearLayout zanBtn;
        ImageView zanIcon;
        LinearLayout commentBtn;
        LinearLayout shareBtn;
        RelativeLayout iv_family_follow_item_list_lay,article_lay;


        RecyclerView comment_list,iv_family_follow_item_list;
        CheckBox zan_check;
        TextView zan_check_num;
        ImageView more_operation,tv_family_follow_item_name_tag,img;
        public FamilyCircleFollowListViewHolder(View itemView) {
            super(itemView);
            headImageView = (CircleImageView) itemView.findViewById(R.id.iv_family_follow_item_head);
            article_img = (RoundAngleImageView) itemView.findViewById(R.id.article_img);
            contentImgView = (RoundAngleImageView) itemView.findViewById(R.id.iv_family_follow_item_img);
            contentTextView = (FolderTextView) itemView.findViewById(R.id.tv_family_follow_item_content);
            article_title = (TextView) itemView.findViewById(R.id.article_title);
            article_content = (TextView) itemView.findViewById(R.id.article_content);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_family_follow_item_name);
            createdTimeView = (TextView) itemView.findViewById(R.id.tv_family_follow_item_created_time);
            zanBtn = (LinearLayout) itemView.findViewById(R.id.ll_family_follow_zan_root);
            zanIcon = (ImageView) itemView.findViewById(R.id.iv_family_follow_zan_icon);
            img = (ImageView) itemView.findViewById(R.id.img);
            commentBtn = (LinearLayout) itemView.findViewById(R.id.ll_family_follow_comment_root);
            shareBtn = (LinearLayout) itemView.findViewById(R.id.ll_family_follow_share_root);
            iv_family_follow_item_list_lay = (RelativeLayout) itemView.findViewById(R.id.iv_family_follow_item_list_lay);
            article_lay = (RelativeLayout) itemView.findViewById(R.id.article_lay);
            commentCount = (TextView) itemView.findViewById(R.id.comment_count);
            comment_list=itemView.findViewById(R.id.comment_list);
            iv_family_follow_item_list=itemView.findViewById(R.id.iv_family_follow_item_list);
            zan_check=itemView.findViewById(R.id.zan_check);
            zan_check_num=itemView.findViewById(R.id.zan_check_num);
            more_operation=itemView.findViewById(R.id.more_operation);
            tv_family_follow_item_name_tag=itemView.findViewById(R.id.tv_family_follow_item_name_tag);
        }
    }
}
