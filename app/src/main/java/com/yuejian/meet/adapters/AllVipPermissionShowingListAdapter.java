package com.yuejian.meet.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuejian.meet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/5/18 11:04
 * @desc : 开通VIP特权包界面 - VIP权限展示列表适配器
 */
public class AllVipPermissionShowingListAdapter extends RecyclerView.Adapter<AllVipPermissionShowingListAdapter.AllVipPermissionShowingViewHolder> {

    private List<Pair<String, String>> mItemList;

    @Override
    public AllVipPermissionShowingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_vip_permission_showing, parent, false);
        return new AllVipPermissionShowingListAdapter.AllVipPermissionShowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllVipPermissionShowingViewHolder holder, int position) {
        holder.nameView.setText(mItemList.get(position).first);
        holder.descView.setText(mItemList.get(position).second);
    }

    @Override
    public int getItemCount() {
        if (mItemList == null) return 0;
        return mItemList.size();
    }

    public void updateData(List<Pair<String, String>> itemList) {
        if (mItemList == null) {
            mItemList = new ArrayList<>();
        }
        mItemList = itemList;
        notifyDataSetChanged();
    }

    class AllVipPermissionShowingViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView descView;

        public AllVipPermissionShowingViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.tv_all_vip_permission_item_name);
            descView = (TextView) itemView.findViewById(R.id.tv_all_vip_permission_item_desc);
        }
    }
}
