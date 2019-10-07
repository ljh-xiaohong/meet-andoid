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
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.ServiceBean;
import com.yuejian.meet.utils.GlideUtils;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;


public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;

    private ServiceAdapter.onClickListener mOnClickListener;
    public void setOnClickListener(ServiceAdapter.onClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClick(int position);
    }
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    private List<ServiceBean.DataBean> mDatas = new ArrayList<>();

    private View mHeaderView;
    public ServiceAdapter(Context context,List<ServiceBean.DataBean> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        mDatas=list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) return new MyViewHolder(mHeaderView);
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false);
        return new MyViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_HEADER) return;
        ServiceBean.DataBean bean= mDatas.get(position);
        if (!TextUtils.isEmpty(bean.getPhoto())) {
            Glide.with(context).load(bean.getPhoto()).into(holder.iv_icon);
        }else {
            GlideUtils.displayNative(holder.iv_icon,R.mipmap.loading_unpic);
        }
        if (!TextUtils.isEmpty(bean.getUserName())) {
            holder.name.setText(bean.getUserName());
        }else {
            holder.name.setText("");
        }
        if (bean.getIncomeFlag()==0){
            holder.attention.setText("服务");
            holder.attention.setVisibility(View.VISIBLE);
            holder.attention.setBackgroundResource(R.drawable.black11);
            holder.attention.setEnabled(true);
        }else {
            holder.attention.setText("已服务");
            holder.attention.setBackgroundResource(R.drawable.gray11);
            holder.attention.setVisibility(View.VISIBLE);
            holder.attention.setEnabled(false);
        }
        holder.attention.setOnClickListener(v -> mOnClickListener.onClick(position));
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }


    @Override
    public int getItemViewType(int position) {
        if(mHeaderView == null) return TYPE_NORMAL;
        if(position == 0) return TYPE_HEADER;
        return TYPE_NORMAL;
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_icon;
        TextView attention,name;
        public MyViewHolder(View itemView) {
            super(itemView);
            if(itemView == mHeaderView) return;
            iv_icon = itemView.findViewById(R.id.iv_icon);
            attention = itemView.findViewById(R.id.attention);
            name = itemView.findViewById(R.id.name);
        }
    }
}

