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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.bean.ChooseIndustryBean;

import java.util.List;


public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ChooseIndustryBean.DataBean> list;
    private int previousPostion=0;
    private OnClick onClick;
    //接口回调
    public interface OnClick{
        public void click(int position);
    }
    public void setClick(OnClick onClick) {
        this.onClick = onClick;
    }
    public FirstAdapter(Context context, List<ChooseIndustryBean.DataBean> data) {
        this.context = context;
        this.list = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.first_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ChooseIndustryBean.DataBean dataBean= list.get(position);
        if (dataBean.isIsSelect()){
            holder.name.setTextColor(context.getResources().getColor(R.color.select_tv));
            holder.name.setBackgroundColor(context.getResources().getColor(R.color.select_bg));
        }else {
            holder.name.setTextColor(context.getResources().getColor(R.color.nor_tv));
            holder.name.setBackgroundColor(context.getResources().getColor(R.color.nor_bg));
        }
        holder.name.setText(dataBean.getJob());
        holder.name.setOnClickListener(v -> {
            list.get(previousPostion).setIsSelect(false);
            dataBean.setIsSelect(true);
            previousPostion=position;
            notifyDataSetChanged();
            onClick.click(position);
        });
        if (position==list.size()-1){
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.name_lay.getLayoutParams();
            params.setMargins(0,0,0,400);
        }else {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.name_lay.getLayoutParams();
            params.setMargins(0,0,0,0);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout name_lay;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            name_lay = itemView.findViewById(R.id.name_lay);
        }
    }
}

