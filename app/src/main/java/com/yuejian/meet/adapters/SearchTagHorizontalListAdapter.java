package com.yuejian.meet.adapters;

import android.view.View;
import android.widget.ImageView;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.HotSeacher;
import java.util.List;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhy.autolayout.AutoLinearLayout;

public class SearchTagHorizontalListAdapter extends RecyclerView.Adapter<SearchTagHorizontalListAdapter.NormalHolder> {
    private LayoutInflater mInflater;
    private List<HotSeacher> mData;
    private Activity contexts;

    public SearchTagHorizontalListAdapter(Activity context, List<HotSeacher> data) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        contexts=context;
    }

    private OnChange onChange;
    //接口回调
    public interface OnChange{
        public void click(int id);
    }
    public void setChange(OnChange onChange) {
        this.onChange = onChange;
    }
    @Override
    public NormalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.activity_shop_type_item, parent,false);
        NormalHolder viewHolder = new NormalHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NormalHolder holder, final int position) {
        final HotSeacher goods=mData.get(position);
        holder.shop_name.setText(goods.getKeyword());
        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChange.click(position);
            }
        });
        if (mData.get(position).isCompany()){
            holder.line.setBackgroundColor(contexts.getResources().getColor(R.color.line_select));
            holder.shop_name.setTextColor(contexts.getResources().getColor(R.color.black3));
            holder.shop_name.getPaint().setFakeBoldText(true);
            holder.shop_name.getPaint().setFakeBoldText(true);
        }else {
            holder.shop_name.setTextColor(contexts.getResources().getColor(R.color.black6));
            holder.line.setBackgroundColor(contexts.getResources().getColor(R.color.white));
            holder.shop_name.getPaint().setFakeBoldText(false);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class NormalHolder extends RecyclerView.ViewHolder
    {
        private ImageView shop_img;
        private TextView shop_name;
        private AutoLinearLayout lay;
        private View line;
        public NormalHolder(View view)
        {
            super(view);
            shop_name = (TextView) view.findViewById(R.id.shop_name);
            lay = (AutoLinearLayout) view.findViewById(R.id.lay);
            line = (View) view.findViewById(R.id.line);
        }
        public void setVisibility(boolean isVisible){
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            if (isVisible){
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            }else{
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

}
