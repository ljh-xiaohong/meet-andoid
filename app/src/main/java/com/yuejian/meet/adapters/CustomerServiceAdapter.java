package com.yuejian.meet.adapters;

/**
 * @author : ljh
 * @time : 2019/9/8 12:55
 * @desc :
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.message.CustomerServiceDetails;
import com.yuejian.meet.widgets.SwipeMenuLayout;


public class CustomerServiceAdapter extends RecyclerView.Adapter<CustomerServiceAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;

    public CustomerServiceAdapter(Context context) {
        this.context = context;
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
        holder.swipeMenuLayout.setIos(false);//设置是否开启IOS阻塞式交互
        holder.swipeMenuLayout.setLeftSwipe(true);//true往左滑动,false为往右侧滑动
        holder.swipeMenuLayout.setSwipeEnable(true);//设置侧滑功能开关
        holder.rlItem.setOnClickListener(v -> context.startActivity(new Intent(context, CustomerServiceDetails.class)));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"删除", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        SwipeMenuLayout swipeMenuLayout;
        Button btnTop,btnUnRead,btnDelete;
        RelativeLayout rlItem;
        public MyViewHolder(View itemView) {
            super(itemView);
            rlItem = itemView.findViewById(R.id.rlItem);
            swipeMenuLayout = itemView.findViewById(R.id.swipeMenuLayout);
            btnTop = itemView.findViewById(R.id.btnTop);
            btnUnRead = itemView.findViewById(R.id.btnUnRead);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

