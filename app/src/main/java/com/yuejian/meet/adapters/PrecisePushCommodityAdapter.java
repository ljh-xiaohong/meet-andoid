package com.yuejian.meet.adapters;

/**
 * @author : ljh
 * @time : 2019/9/8 12:55
 * @desc :
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.custom.view.RoundAngleImageView;
import com.yuejian.meet.bean.PushCommodityBean;
import com.yuejian.meet.bean.PushListBean;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;


public class PrecisePushCommodityAdapter extends RecyclerView.Adapter<PrecisePushCommodityAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;


    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

     private List<PushCommodityBean.DataBean> listCommodity;

    private View mHeaderView;
    public PrecisePushCommodityAdapter(Context context, List<PushCommodityBean.DataBean> listCommodity) {
        this.context = context;
        this.listCommodity = listCommodity;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new MyViewHolder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.precise_push_content_type2_item, parent, false);
        return new MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        PushCommodityBean.DataBean dataBean=listCommodity.get(position);
        if (!TextUtils.isEmpty(dataBean.getPhoto())) {
            Glide.with(context).load(dataBean.getPhoto()).into(holder.msg_img);
        }
        holder.original_money.setText(dataBean.getGPriceVip()+"");
        holder.money.setText(dataBean.getPrice()+"");
        holder.title.setText(dataBean.getGName()+"");
    }


    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }


    @Override
    public int getItemCount() {
        return mHeaderView == null ? listCommodity.size() : listCommodity.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RoundAngleImageView vip_img,msg_img;
        TextView title,money,original_money;
        public MyViewHolder(View itemView) {
            super(itemView);
            if(itemView == mHeaderView) return;
            vip_img = itemView.findViewById(R.id.vip_img);
            msg_img = itemView.findViewById(R.id.msg_img);
            title = itemView.findViewById(R.id.title);
            money = itemView.findViewById(R.id.money);
            original_money = itemView.findViewById(R.id.original_money);
        }
    }
}

