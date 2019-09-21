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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.message.CustomerServiceDetails;
import com.yuejian.meet.bean.MessageBean;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.widgets.CircleImageView;
import com.yuejian.meet.widgets.SwipeMenuLayout;

import java.util.Date;
import java.util.List;


public class CustomerServiceAdapter extends RecyclerView.Adapter<CustomerServiceAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<MessageBean.DataBean> list;
    public CustomerServiceAdapter(Context context, List<MessageBean.DataBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customer_service, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MessageBean.DataBean dataBean= list.get(position);
        holder.swipeMenuLayout.setIos(false);//设置是否开启IOS阻塞式交互
        holder.swipeMenuLayout.setLeftSwipe(true);//true往左滑动,false为往右侧滑动
        holder.swipeMenuLayout.setSwipeEnable(true);//设置侧滑功能开关
        holder.rlItem.setOnClickListener(v -> {
          Intent intent= new Intent(context, CustomerServiceDetails.class);
          intent.putExtra("messageId",dataBean.getId());
          intent.putExtra("content",dataBean.getMsgRemark());
            context.startActivity(intent);
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"删除", Toast.LENGTH_SHORT).show();
            }
        });
        if (!TextUtils.isEmpty(dataBean.getMsgPhoto())) {
            Glide.with(context).load(dataBean.getMsgPhoto()).into(holder.iv_icon);
        }
        if (!TextUtils.isEmpty(dataBean.getTitle())) {
            holder.title.setText(dataBean.getTitle());
        }else {
            holder.title.setText("");
        }
        if (!TextUtils.isEmpty(dataBean.getMsgRemark())) {
            holder.content.setText(dataBean.getMsgRemark());
        }else {
            holder.content.setText("");
        }
        Date date = new Date(dataBean.getCreateTime()*1000);
        String createdTime = TimeUtils.formatDateTime(date);
        holder.time.setText(createdTime);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        SwipeMenuLayout swipeMenuLayout;
        CircleImageView iv_icon;
        TextView title,content,time;
        Button btnDelete;
        RelativeLayout rlItem;
        public MyViewHolder(View itemView) {
            super(itemView);
            rlItem = itemView.findViewById(R.id.rlItem);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            swipeMenuLayout = itemView.findViewById(R.id.swipeMenuLayout);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

