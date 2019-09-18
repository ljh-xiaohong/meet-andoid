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
import com.yuejian.meet.bean.ChooseIndustryBean;

import java.util.List;


public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ChooseIndustryBean.DataBean.JobsBean> list;
    private OnClick onClick;
    //接口回调
    public interface OnClick{
        public void click(int position);
    }
    public void setClick(OnClick onClick) {
        this.onClick = onClick;
    }
    public SecondAdapter(Context context, List<ChooseIndustryBean.DataBean.JobsBean> data) {
        this.context = context;
        this.list = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.second_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChooseIndustryBean.DataBean.JobsBean jobsBean= list.get(position);
        if (jobsBean.isIsSelect()){
            holder.name.setTextColor(context.getResources().getColor(R.color.select_tv));
            holder.select.setVisibility(View.VISIBLE);
        }else {
            holder.name.setTextColor(context.getResources().getColor(R.color.nor_tv));
            holder.select.setVisibility(View.GONE);
        }
        holder.name.setText(jobsBean.getJob());
        holder.name.setOnClickListener(v -> {
            if (jobsBean.isIsSelect()){
                jobsBean.setIsSelect(false);
            }else {
                jobsBean.setIsSelect(true);
            }
            notifyDataSetChanged();
            onClick.click(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView select;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            select = itemView.findViewById(R.id.select);
        }
    }
}

