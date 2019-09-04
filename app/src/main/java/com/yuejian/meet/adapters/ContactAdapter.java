package com.yuejian.meet.adapters;

/**
 * Created by zh03 on 2017/11/25.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.utils.Utils;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.group.SelectContactActivity;
import com.yuejian.meet.bean.Contact;

import java.util.List;

/**
 * 联系人adapter
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{
    List<Contact> listData;
    Context context;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view=View.inflate(context, R.layout.invite_group_member_layout,null);
        return new ViewHolder(view);
    }
    public void setListData(List<Contact> listData){
        this.listData=listData;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final  ViewHolder holder,final int position) {
        Glide.with(context).load(listData.get(position).getPhoto()).override(Utils.dp2px(context, 33),Utils.dp2px(context, 33)).into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData.get(position).setSelect(false);
                ((SelectContactActivity)context).delectSelectData(listData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData==null?0:listData.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public ViewHolder(View view){
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.group_member_img);
        }
    }
}
