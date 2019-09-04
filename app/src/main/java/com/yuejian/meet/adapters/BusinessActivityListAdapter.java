package com.yuejian.meet.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.BusinessActivityListEntity;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.TextUtil;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.widgets.CircleImageView;
import com.yuejian.meet.widgets.LikerItemView;
import com.yuejian.meet.widgets.springview.MyGridView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/6/18 10:50
 * @desc : 商圈活动列表适配器
 */
public class BusinessActivityListAdapter extends RecyclerView.Adapter<BusinessActivityListAdapter.BusinessActivityListViewHolder> {

    private List<BusinessActivityListEntity> mActivityListEntities;
    Context mContext;
    private static final String[] statusArr = new String[]{"未开始", "进行中", "已完成"};
    private static final String[] PASS_ACTION = {"一键参与", "已参加", "停止报名"};
    private OnItemClickListener listener;


    public BusinessActivityListAdapter(Context context, OnItemClickListener activityItemOnClickListener) {
        mContext = context;
        this.listener = activityItemOnClickListener;
    }

    @Override
    public BusinessActivityListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //   View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business_activitys, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business_activity_new, parent, false);
        return new BusinessActivityListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessActivityListViewHolder holder, int position) {
        String headUrl = mActivityListEntities.get(position).getPhoto();
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.headImageView);
        }
        holder.nameTextView.setText(TextUtil.notNull(mActivityListEntities.get(position).getUserName()));
        String createdTime = TimeUtils.formatDateTime(new Date(Long.valueOf(mActivityListEntities.get(position).getCreate_time())));
        holder.createdTimeView.setText(TextUtil.notNull(createdTime));
        holder.compTextView.setText(TextUtil.notNull(mActivityListEntities.get(position).getCompany_name()));
        holder.statusView.setText(statusArr[Integer.valueOf(mActivityListEntities.get(position).getActivity_state())]);
        holder.contentTextView.setText(TextUtil.notNull(mActivityListEntities.get(position).getContent()));
        holder.readAndJoinView.setText(String.format("%s人看过，%s人参加",
                TextUtil.notNull(
                        mActivityListEntities.get(position).getRead_num()), TextUtil.notNull((mActivityListEntities.get(position).getApply_num_yet()))));

        switch (TextUtil.notNull(mActivityListEntities.get(position).getIs_pass())) {

            case "0":
                holder.joinActivityBtn.setText("一键参与");
                holder.joinActivityBtn.setTextColor(Color.parseColor("#FFF2F5F7"));
                holder.joinActivityBtn.setEnabled(true);
                break;

            case "1":
                holder.joinActivityBtn.setText("已参加");
                holder.joinActivityBtn.setTextColor(Color.parseColor("#4dF2F5F7"));
                holder.joinActivityBtn.setEnabled(false);

                break;

            case "2":
                holder.joinActivityBtn.setText("停止报名");
                holder.joinActivityBtn.setTextColor(Color.parseColor("#4dF2F5F7"));
                holder.joinActivityBtn.setEnabled(false);
                break;

            default:
                holder.joinActivityBtn.setText("一键参与");
                holder.joinActivityBtn.setTextColor(Color.parseColor("#FFF2F5F7"));
                holder.joinActivityBtn.setEnabled(true);
                break;

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClick(view, position);

            }
        });

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFollowClick(view, position);
            }
        });


        ImagesAdapter adapter = new ImagesAdapter(mContext, mActivityListEntities.get(position).getResources_url());
        holder.imgs.setAdapter(adapter);
        adapter.refresh(mActivityListEntities.get(position).getResources_url());
//        adapter.refresh(mActivityListEntities.get(position).getResources_url());
        holder.imgs.setClickable(false);
        holder.imgs.setPressed(false);
        holder.imgs.setEnabled(false);
        holder.likers.removeAllViews();
        if (null != mActivityListEntities.get(position).getJoinList() && mActivityListEntities.get(position).getJoinList().size() > 0) {
            for (BusinessActivityListEntity.JoinList join : mActivityListEntities.get(position).getJoinList()) {
                LikerItemView item = new LikerItemView(this.mContext);
                item.setItemInfo(join.getNumber(), join.getUserName(), TimeUtils.formatDateTime(new Date(Long.valueOf(join.getCreate_time()))), join.getPhoto());
                holder.likers.addView(item);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (mActivityListEntities == null) return 0;
        return mActivityListEntities.size();
    }

    public void refresh(List<BusinessActivityListEntity> activityListEntities) {
        if (this.mActivityListEntities == null) {
            this.mActivityListEntities = new ArrayList<>();
        }
        this.mActivityListEntities = activityListEntities;
        notifyDataSetChanged();
    }

    public interface ActivityItemOnClickListener {
        void onActivityItemClick(int activityId);
    }

    class BusinessActivityListViewHolder extends RecyclerView.ViewHolder {

        TextView contentTextView;
        TextView nameTextView;
        TextView createdTimeView;
        TextView compTextView;
        CircleImageView headImageView;
        TextView statusView;
        TextView readAndJoinView;
        TextView joinActivityBtn;
        TextView follow;
        MyGridView imgs;
        LinearLayout likers;
        ImagesAdapter adapter;


        public BusinessActivityListViewHolder(View itemView) {
            super(itemView);
            headImageView = (CircleImageView) itemView.findViewById(R.id.iv_business_activity_item_head);
            //   contentImgView = (ImageView) itemView.findViewById(R.id.iv_business_activity_item_content_img);
            contentTextView = (TextView) itemView.findViewById(R.id.tv_business_activity_item_content);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_business_activity_item_name);
            createdTimeView = (TextView) itemView.findViewById(R.id.tv_business_activity_item_time);
            compTextView = (TextView) itemView.findViewById(R.id.tv_business_activity_item_comp);
            statusView = (TextView) itemView.findViewById(R.id.tv_business_activity_item_status);
            readAndJoinView = (TextView) itemView.findViewById(R.id.tv_read_and_join_num);
            joinActivityBtn = (TextView) itemView.findViewById(R.id.tv_join_activity_btn);
            follow = (TextView) itemView.findViewById(R.id.tv_business_activity_item_follow);
            //  list_likes = (LinearLayout) itemView.findViewById(R.id.ll_business_activity_item_comment);
            imgs = (MyGridView) itemView.findViewById(R.id.gv_business_activity_item_imgs);
            likers = (LinearLayout) itemView.findViewById(R.id.ll_business_activity_item_likers);


        }
    }


    public void setOnItemClikListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onFollowClick(View view, int position);

        void onItemClick(View view, int position);

        void onJoinClick(View view, int position);
    }

    public static class ImagesAdapter extends BaseAdapter {

        List<BusinessActivityListEntity.Resources_url> urls;
        Context context;

        public ImagesAdapter(Context context, List<BusinessActivityListEntity.Resources_url> entities) {
            this.context = context;
            if (null == entities) {
                entities = new ArrayList<>();
            }
            this.urls = entities;
        }

        public void refresh(List<BusinessActivityListEntity.Resources_url> urls) {
            if (null == urls) {
                urls = new ArrayList<>();
            }
            this.urls = urls;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public Object getItem(int i) {
            return urls.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {


            if (convertView == null) {
                convertView = new ImageView(this.context);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DensityUtils.dip2px(this.context, 95), DensityUtils.dip2px(this.context, 95));
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                convertView.setLayoutParams(params);
            }

            if (null != urls && !urls.isEmpty()) {
                Glide.with(this.context).load(urls.get(i).getSrc()).into((ImageView) convertView);
            }

            return convertView;
        }


    }


}
