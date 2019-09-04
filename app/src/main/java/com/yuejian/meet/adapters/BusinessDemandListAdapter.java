package com.yuejian.meet.adapters;

import android.content.Context;
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
import com.yuejian.meet.bean.BusinessDemandListEntity;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.widgets.CircleImageView;
import com.yuejian.meet.widgets.springview.MyGridView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/6/18 14:10
 * @desc : 商圈需求列表适配器
 */
public class BusinessDemandListAdapter extends RecyclerView.Adapter<BusinessDemandListAdapter.BusinessDemandListViewHolder> {

    private List<BusinessDemandListEntity> mDemandListEntities;
    private Context mContext;

    @Override
    public BusinessDemandListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business_xuqiu, parent, false);
        mContext = view.getContext();
        return new BusinessDemandListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessDemandListViewHolder holder, int position) {
        String headUrl = mDemandListEntities.get(position).photo;
        if (!TextUtils.isEmpty(headUrl)) {
            Glide.with(mContext).load(headUrl).into(holder.headImageView);
        }
        holder.nameTextView.setText(String.format("%s", mDemandListEntities.get(position).name));
        String createdTime = TimeUtils.formatDateTime(new Date(mDemandListEntities.get(position).create_time));
        holder.createdTimeView.setText(createdTime);
        holder.contentTextView.setText(mDemandListEntities.get(position).title);
        holder.compTextView.setText(mDemandListEntities.get(position).job);
        holder.tagTextView.setText(mDemandListEntities.get(position).label_name);
        holder.praiseImage.setImageResource(mDemandListEntities.get(position).getIs_praise() ? R.mipmap.icon_video_zan_sel : R.mipmap.icon_mine_zan_nor);
        holder.followTextView.setText(mDemandListEntities.get(position).getIs_bind() ? "已关注" : "+ 关注");
        holder.read.setText(String.format("%s 人看过", mDemandListEntities.get(position).getView_count()));
        MyInnerAdatper adatper = new MyInnerAdatper(mDemandListEntities.get(position).getResources_url(), mContext);
        holder.gvs.setAdapter(adatper);
        adatper.refresh(mDemandListEntities.get(position).getResources_url());

        holder.followTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //触发自定义监听的单击事件
                onItemClickListener.onFollowClick(view, position, mDemandListEntities.get(position));
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, position, mDemandListEntities.get(position));
            }
        });

        holder.praiseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onPraiseClick(holder.praiseImage, position, mDemandListEntities.get(position));
            }
        });

        holder.push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onPush(view, position, mDemandListEntities.get(position));
            }
        });


        // TODO
//        String imgUrl = mActivityListEntities.get(position).photo_and_video_url;
//        if (!TextUtils.isEmpty(imgUrl)) {
//            Glide.with(mContext).load(imgUrl).placeholder(R.mipmap.loading_pic)
//                    .error(R.mipmap.loading_unpic).centerCrop().into(holder.contentImgView);
//        }
    }

    @Override
    public int getItemCount() {
        if (mDemandListEntities == null) return 0;
        return mDemandListEntities.size();
    }

    public void refresh(List<BusinessDemandListEntity> demandListEntities) {
        if (this.mDemandListEntities == null) {
            this.mDemandListEntities = new ArrayList<>();
        }
        this.mDemandListEntities = demandListEntities;
        notifyDataSetChanged();
    }

    class BusinessDemandListViewHolder extends RecyclerView.ViewHolder {

        TextView contentTextView;
        TextView nameTextView;
        TextView createdTimeView;
        TextView compTextView;
        TextView tagTextView;
        TextView followTextView;
        TextView read;
        ImageView praiseImage;
        LinearLayout praiseLayout;
        LinearLayout push;
        LinearLayout layout;
        CircleImageView headImageView;
        MyGridView gvs;

        public BusinessDemandListViewHolder(View itemView) {
            super(itemView);
            headImageView = (CircleImageView) itemView.findViewById(R.id.iv_business_xuqiu_item_head);
            contentTextView = (TextView) itemView.findViewById(R.id.tv_business_xuqiu_item_content);
            nameTextView = (TextView) itemView.findViewById(R.id.tv_business_xuqiu_item_name);
            createdTimeView = (TextView) itemView.findViewById(R.id.tv_business_xuqiu_item_time);
            compTextView = (TextView) itemView.findViewById(R.id.tv_business_xuqiu_item_comp);
            tagTextView = (TextView) itemView.findViewById(R.id.tv_business_xuqiu_tag);
            followTextView = (TextView) itemView.findViewById(R.id.tv_business_xuqiu_item_follow);
            layout = (LinearLayout) itemView.findViewById(R.id.ll_business_xuqiu_item);
            praiseImage = (ImageView) itemView.findViewById(R.id.iv_business_xuqiu_zan_icon);
            praiseLayout = (LinearLayout) itemView.findViewById(R.id.ll_business_xuqiu_zan_root);
            read = (TextView) itemView.findViewById(R.id.textView4);
            push = (LinearLayout) itemView.findViewById(R.id.ll_business_xuqiu_share_root);
            gvs = (MyGridView) itemView.findViewById(R.id.ll_business_xuqiu_imgs);
            gvs.setClickable(false);
            gvs.setPressed(false);
            gvs.setEnabled(false);
        }
    }

    public void setOnItemClickListener(BusinessDemandListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class MyInnerAdatper extends BaseAdapter {

        List<BusinessDemandListEntity.Resources_url> datas;

        Context context;

        public MyInnerAdatper(List<BusinessDemandListEntity.Resources_url> datas, Context context) {
            if (null == datas) {
                datas = new ArrayList<>();
            }
            this.datas = datas;
            this.context = context;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void refresh(List<BusinessDemandListEntity.Resources_url> urls) {
            if (null == urls) {
                urls = new ArrayList<>();
            }
            this.datas = urls;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View containView, ViewGroup viewGroup) {

            if (containView == null) {
                containView = new ImageView(this.context);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DensityUtils.dip2px(this.context, 95), DensityUtils.dip2px(this.context, 95));
                ((ImageView)containView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                containView.setLayoutParams(params);
            }
            if (null != datas && !datas.isEmpty()) {
                Glide.with(this.context).load(datas.get(position).getSrc()).into((ImageView) containView);
            }

            return containView;
        }


    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, BusinessDemandListEntity entity);

        void onFollowClick(View view, int position, BusinessDemandListEntity entity);

        void onPraiseClick(View view, int postion, BusinessDemandListEntity entity);

        void onPush(View view, int postion, BusinessDemandListEntity entity);
    }
}
