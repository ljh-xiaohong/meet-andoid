package com.yuejian.meet.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.bean.IncomeListEntity;
import com.yuejian.meet.utils.DateUtil;


import java.util.ArrayList;
import java.util.List;

public class IncomeListAdapter extends BaseAdapter {

    List<IncomeListEntity> entities;
    Context context;

    public IncomeListAdapter(Context context, List<IncomeListEntity> entities) {

        if (null == entities) entities = new ArrayList<>();
        this.entities = entities;
        this.context = context;
    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public Object getItem(int i) {
        return entities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return entities.size();
    }

    @Override
    public View getView(int position, View containView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (null == containView) {
            holder = new ViewHolder();
            containView = LayoutInflater.from(context).inflate(R.layout.item_income_list_item, null);
            holder.time = (TextView) containView.findViewById(R.id.item_income_list_item_time);
            holder.name = (TextView) containView.findViewById(R.id.item_income_list_item_name);
            holder.num = (TextView) containView.findViewById(R.id.item_income_list_item_num);
            holder.event = (TextView) containView.findViewById(R.id.item_income_list_item_event);
            holder.income = (TextView) containView.findViewById(R.id.item_income_list_item_income);
            holder.background = (LinearLayout) containView.findViewById(R.id.item_income_list_item_background);

            containView.setTag(holder);
        } else {
            holder = (ViewHolder) containView.getTag();
        }
        if (position % 2 == 0) {
            holder.background.setBackgroundColor(Color.parseColor("#ffffffff"));
        } else {
            holder.background.setBackgroundColor(Color.parseColor("#ffe6e6e6"));

        }
        holder.time.setText(DateUtil.generateTimestamp(Long.valueOf(entities.get(position).getCreate_time())));
        holder.name.setText(entities.get(position).getName());
        holder.num.setText(entities.get(position).getCustomer_id());
        holder.event.setText(entities.get(position).getEvent_name());
        holder.income.setText(entities.get(position).getOp_income());

        return containView;
    }

    private class ViewHolder {

        TextView time, name, num, event, income;
        LinearLayout background;

    }
}
