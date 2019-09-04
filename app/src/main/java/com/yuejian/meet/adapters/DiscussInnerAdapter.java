package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.bean.ReleaseContentEntity;

import java.util.ArrayList;
import java.util.List;

public class DiscussInnerAdapter extends BaseAdapter {

    List<ReleaseContentEntity.Reply_list> datas;
    Context mContext;

    public DiscussInnerAdapter(List<ReleaseContentEntity.Reply_list> datas, Context mContext) {
        if (null == datas) {
            datas = new ArrayList<>();
        }
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return this.datas.size();
    }

    @Override
    public Object getItem(int i) {
        return this.datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_discuss_inner_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.item_discuss_inner_name);
            holder.content = (TextView) view.findViewById(R.id.item_discuss_inner_content);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(datas.get(position).getName());
        holder.content.setText(datas.get(position).getArticle_comment_content());


        return view;
    }


    public class ViewHolder {
        TextView name;
        TextView content;
    }


}
