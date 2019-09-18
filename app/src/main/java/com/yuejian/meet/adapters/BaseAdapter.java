package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.yuejian.meet.ui.SpacesItemDecoration;
import com.yuejian.meet.utils.ScreenUtils;

import java.util.List;


public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, DATA> extends RecyclerView.Adapter<VH> {
    protected Context context;

    protected List<DATA> data;

    protected RecyclerView recyclerView;

    protected int Spaces = 20;
    protected int spanCount = 2;

    protected OnItemClickListener listener;

    protected StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);


    protected SpacesItemDecoration decoration = new SpacesItemDecoration(Spaces);

    protected int itemWidth;
    protected int itemHeight;

    private BaseAdapter() {

    }

    abstract void refresh(List<DATA> data);

    abstract void Loadmore(List<DATA> data);

    public List<DATA> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        if (data != null) return data.size();
        return 0;
    }

    public BaseAdapter(RecyclerView recyclerView, Context context) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.addItemDecoration(decoration);
        this.recyclerView.setAdapter(this);

        //设置item的高度，文章自适应，其他都是3：5；
        itemWidth = (ScreenUtils.getScreenWidth(recyclerView.getContext()) - Spaces * 3) / spanCount;
        itemHeight = itemWidth / 3 * 5;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
