package com.yuejian.meet.adapters;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.MembersEntity;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 群 成员-临时成员adpter
 */

public class TeamTempyAdapter extends FKAdapter<MembersEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public TeamTempyAdapter(AbsListView view, List<MembersEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
    }
    public void convert(AdapterHolder helper, MembersEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, MembersEntity item, final int position){
        this.mHelper=helper;
//        mHelper.setText(R.id.txt_member_name,item.getSurname()+item.getName());
//        mHelper.setText(R.id.member_job,item.getJob());
        int v = (int) (AppConfig.width * 0.8) / 7;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(v, v);
        ImageView viewImg=mHelper.getView(R.id.reward_header_img);
        viewImg.setLayoutParams(params);
        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into(viewImg);
    }
}
