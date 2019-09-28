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
import com.yuejian.meet.bean.PushUseBean;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.List;


public class PrecisePushAdapter extends RecyclerView.Adapter<PrecisePushAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<PushUseBean.DataBean> list;
    private PrecisePushAdapter.onClickListener mOnClickListener;
    public void setOnClickListener(PrecisePushAdapter.onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClick(int position);
    }
    public PrecisePushAdapter(Context context, List<PushUseBean.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.precise_push_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        PushUseBean.DataBean dataBean= list.get(position);
        if (!TextUtils.isEmpty(dataBean.getUserPhoto())) {
            Glide.with(context).load(dataBean.getUserPhoto()).into(holder.iv_icon);
        }
        if (!CommonUtil.isNull(dataBean.getVipType())&&Integer.parseInt(dataBean.getVipType())==1) {
            holder.vip_img.setVisibility(View.VISIBLE);
        }else {
            holder.vip_img.setVisibility(View.GONE);
        }
        holder.name.setText(dataBean.getUserName());
        holder.content.setText(dataBean.getLastLoginCity()+"");
        holder.attention.setOnClickListener(v -> mOnClickListener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_icon;
        ImageView vip_img;
        TextView name,content,attention;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            vip_img = itemView.findViewById(R.id.vip_img);
            name = itemView.findViewById(R.id.name);
            content = itemView.findViewById(R.id.content);
            attention = itemView.findViewById(R.id.attention);
        }
    }
}

