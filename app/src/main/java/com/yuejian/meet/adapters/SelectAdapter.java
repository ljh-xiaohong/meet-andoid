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
import com.yuejian.meet.bean.SelectBean;

import java.util.List;


public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<SelectBean> list;
    private OnClick onClick;
    //接口回调
    public interface OnClick{
        public void click(int position);
    }
    public void setClick(OnClick onClick) {
        this.onClick = onClick;
    }
    public SelectAdapter(Context context, List<SelectBean> data) {
        this.context = context;
        this.list=data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.select_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        SelectBean jobsBean= list.get(position);
        holder.select.setText(jobsBean.getName());
        holder.delete.setOnClickListener(v -> {
            onClick.click(position);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView select;
        ImageView delete;
        public MyViewHolder(View itemView) {
            super(itemView);
            select = itemView.findViewById(R.id.select);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}

