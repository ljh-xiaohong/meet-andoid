package com.yuejian.meet.adapters;

/**
 * @author : ljh
 * @time : 2019/9/8 12:55
 * @desc :
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.bean.PushListBean;
import com.yuejian.meet.bean.PushProjectBean;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;


public class PrecisePushContentAdapter extends RecyclerView.Adapter<PrecisePushContentAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<PushProjectBean.DataBean> list ;

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;


    private View mHeaderView;
    public PrecisePushContentAdapter(Context context, List<PushProjectBean.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new MyViewHolder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.precise_push_content_type1_item, parent, false);
        return new MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        PushProjectBean.DataBean dataBean=list.get(position);
        if (!TextUtils.isEmpty(dataBean.getUserPhoto())) {
            Glide.with(context).load(dataBean.getUserPhoto()).into(holder.iv_icon);
        }
        if (!TextUtils.isEmpty(dataBean.getCoverUrl())) {
            Glide.with(context).load(dataBean.getCoverUrl()).into(holder.msg_img);
        }
        if (!CommonUtil.isNull(dataBean.getVipType())&&Integer.parseInt(dataBean.getVipType())==1) {
            holder.vip_img.setVisibility(View.VISIBLE);
        }else {
            holder.vip_img.setVisibility(View.GONE);
        }
        holder.name.setText(dataBean.getUserName());
        holder.content.setText(dataBean.getContent()+"");
        holder.title.setText(dataBean.getTitle()+"");
        holder.itemView.setOnClickListener(v -> {
            String  urlShop = String.format("http://app2.yuejianchina.com/yuejian-app/personal_center/projectDetail.html?customerId=%s&id=%s&phone=true", AppConfig.CustomerId, dataBean.getId());
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra(Constants.URL, urlShop);
            intent.putExtra("No_Title", true);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }


    @Override
    public int getItemCount() {
        return mHeaderView == null ? list.size() : list.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_icon;
        ImageView vip_img,msg_img;
        TextView title,content,name;
        public MyViewHolder(View itemView) {
            super(itemView);
            if(itemView == mHeaderView) return;
            iv_icon = itemView.findViewById(R.id.iv_icon);
            vip_img = itemView.findViewById(R.id.vip_img);
            msg_img = itemView.findViewById(R.id.msg_img);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.name);
        }
    }
}

