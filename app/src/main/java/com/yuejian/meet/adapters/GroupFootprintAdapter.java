package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.GroupFootprintEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.utils.StringUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 群聊足迹adpter
 */

public class GroupFootprintAdapter extends FKAdapter<GroupFootprintEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public GroupFootprintAdapter(AbsListView view, List<GroupFootprintEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
    }
    public void convert(AdapterHolder helper, GroupFootprintEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, GroupFootprintEntity item, final int position){
        this.mHelper=helper;
        mHelper.setText(R.id.footprint_name,item.getGroup_name());
        mHelper.setText(R.id.footprint_time,"上次访问  "+ StringUtils.friendlyTime(item.getAccess_time()));
//        mHelper.setText(R.id.member_job,item.getJob());
//        mHelper.setText(R.id.txt_member_age," "+item.getAge());
//        mHelper.getView(R.id.member_distance_layout).setVisibility(item.distance.equals("-1")? View.GONE:View.VISIBLE);
//        mHelper.setText(R.id.txt_member_distance,item.distance+"km");
//        mHelper.getView(R.id.txt_member_age).setSelected(item.getSex().equals("1")?true:false);
//        mHelper.getView(R.id.img_member_phone).setSelected(item.getIs_mobile_certified().endsWith("-1")?false:true);
//        mHelper.getView(R.id.img_member_weixin).setSelected(item.is_weixin_certified.endsWith("-1")?false:true);
//        mHelper.getView(R.id.img_member_identity).setSelected(item.is_idcard_certified.endsWith("-1")?false:true);
//        mHelper.getView(R.id.img_member_enterprise).setSelected(item.is_business_license_certified.endsWith("-1")?false:true);
        Glide.with(context).load(item.group_photo).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.footprint_photo));
    }
}
