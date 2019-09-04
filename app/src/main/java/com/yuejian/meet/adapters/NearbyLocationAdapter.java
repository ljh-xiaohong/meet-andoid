package com.yuejian.meet.adapters;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.nearby.NearbyInfo;
import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.MembersEntity;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 附近位置adpter
 */

public class NearbyLocationAdapter extends FKAdapter<PoiItem> {
    private AdapterHolder mHelper;
    private Context context;


    public NearbyLocationAdapter(AbsListView view, List<PoiItem> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
    }
    public void convert(AdapterHolder helper, PoiItem item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, PoiItem item, final int position){
        this.mHelper=helper;
        mHelper.setText(R.id.txt_at_location,item.getTitle());
        mHelper.setText(R.id.txt_info_location,item.getProvinceName()+item.getCityName()+item.getAdName()+item.getSnippet());
    }
}
