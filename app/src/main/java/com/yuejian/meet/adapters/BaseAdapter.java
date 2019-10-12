package com.yuejian.meet.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

    OnBottomListener bottomListener;

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

        this.BottomListener();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnBottomListener<DATA> {
        void onBottom();

        boolean canScroll(List<DATA> data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnBottomListener(OnBottomListener listener) {
        bottomListener = listener;
    }


    protected void BottomListener() {
        if (recyclerView == null) return;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (bottomListener == null) return;
                if (!bottomListener.canScroll(data)) return;
                //当前RecyclerView显示出来的最后一个的item的position
                int lastPosition = -1;

                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                    if (layoutManager instanceof GridLayoutManager) {
//                        //通过LayoutManager找到当前显示的最后的item的position
//                        lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
//                    } else if (layoutManager instanceof LinearLayoutManager) {
//                        lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
//                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//                        //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
//                        //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
//                        int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
//                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
//                        lastPosition = findMax(lastPositions);
//                    }
                    //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                    //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                    int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                    lastPosition = findMax(lastPositions);

                    //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                    //如果相等则说明已经滑动到最后了
                    if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                        //TODO
                        bottomListener.onBottom();
                    }

                }

            }


        });
    }


    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

}
