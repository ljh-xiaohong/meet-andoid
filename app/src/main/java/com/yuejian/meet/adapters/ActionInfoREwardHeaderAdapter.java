package com.yuejian.meet.adapters;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.bean.ReplyListEntity;
import com.yuejian.meet.bean.RewardListEntity;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 动态头访问adpter
 */

public class ActionInfoREwardHeaderAdapter extends FKAdapter<RewardListEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public ActionInfoREwardHeaderAdapter(AbsListView view, List<RewardListEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
    }
    public void convert(AdapterHolder helper, RewardListEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, RewardListEntity item, final int position){
        this.mHelper=helper;
        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.reward_header_img));
    }
}
