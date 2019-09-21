package com.yuejian.meet.adapters;

/**
 * @author : ljh
 * @time : 2019/9/8 12:55
 * @desc :
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.widgets.CircleImageView;


public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;

    public NewFriendAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.new_friend_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_icon;
        ImageView vip_img;
        TextView name,attention;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            vip_img = itemView.findViewById(R.id.vip_img);
            name = itemView.findViewById(R.id.name);
            attention = itemView.findViewById(R.id.attention);
        }
    }
}

