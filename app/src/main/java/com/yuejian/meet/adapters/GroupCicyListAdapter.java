package com.yuejian.meet.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.GroupCityListActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.GroupEntity;
import com.yuejian.meet.bean.GroupSeedEntity;
import com.yuejian.meet.utils.ImMesssageRedDot;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.widgets.InnerListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh03 on 2017/6/13.
 * 城市群列表adpter
 */

public class GroupCicyListAdapter extends FKAdapter<GroupEntity> {
    private AdapterHolder mHelper;
    private Context context;
    ApiImp api=new ApiImp();


    public GroupCicyListAdapter(AbsListView view, List<GroupEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
    }
    public void convert(AdapterHolder helper, GroupEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, final GroupEntity item, final int position){
        this.mHelper=helper;
        mHelper.setText(R.id.group_city_name,item.getFamily_name());
        mHelper.getView(R.id.img_group_pull_down).setVisibility(View.GONE);
        final InnerListView listView=mHelper.getView(R.id.group_item_list);
        final List<GroupSeedEntity> listData= JSON.parseArray(item.getChat_group_list(),GroupSeedEntity.class);
        final listAdpter mAdpter=new listAdpter(listView,listData,R.layout.item_group_list_layout);
        listView.setAdapter(mAdpter);
        mAdpter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (listData.get(position).getT_id()!=null){
//                    if (listData.get(position).getT_id().equals(AppConfig.userEntity.getChat_group_tid())){
//                        ImUtils.toTeamSession(mCxt,listData.get(position).getT_id());
//                    }else {
                    final ProgressDialog dialog=new ProgressDialog(context);
                    dialog.setMessage("正在操作..");
                    dialog.show();
                        Map<String,Object> params=new HashMap<>();
                        params.put("customer_id",AppConfig.CustomerId);
                        params.put("t_id",listData.get(position).getT_id());
                        api.intoGroup(params, this, new DataIdCallback<String>() {
                            @Override
                            public void onSuccess(String data, int id) {
                                if (dialog!=null)dialog.dismiss();
                                ImUtils.toTeamSession(mCxt,listData.get(position).getT_id(),data);
                            }
                            @Override
                            public void onFailed(String errCode, String errMsg, int id) {
                                if (dialog!=null)dialog.dismiss();
                            }
                        });
//                    }
                }else {
                    Intent intent=new Intent(mCxt, GroupCityListActivity.class);
                    intent.putExtra("province",item.getProvince());
                    intent.putExtra("city",listData.get(position).getCity());
                    mCxt.startActivity(intent);
                }
            }
        });
    }
    public class listAdpter extends FKAdapter<GroupSeedEntity>{

        public listAdpter(AbsListView view, List<GroupSeedEntity> mDatas, int itemLayoutId) {
            super(view, mDatas, itemLayoutId);
        }
        public void convert(AdapterHolder helper, GroupSeedEntity item, boolean isScrolling, int position) {
            convert(helper, getItem(position), isScrolling);
            initNearByData(helper, item, position);
        }
        private void initNearByData(AdapterHolder helper, GroupSeedEntity item, final int position){
            helper.setText(R.id.txt_group_seed_name,item.getCity_family()==null?item.getGroup_name():item.getCity_family());
            helper.setText(R.id.txt_group_seed_cnt,"("+item.getMember_cnt()+")");
            helper.getView(R.id.group_seed_layout).setVisibility(item.getGroup_photo().equals("")?View.GONE:View.VISIBLE);
            if (!item.getGroup_photo().equals("")){
                helper.getView(R.id.group_notice_tip).setVisibility(View.GONE);
                helper.getView(R.id.img_group_gfit).setVisibility(View.GONE);
                if (item.getInviter_gift_cnt()>0){
                    helper.getView(R.id.img_group_gfit).setVisibility(View.VISIBLE);
                }else {
                    int redDot= ImMesssageRedDot.getUnreadCount(item.getT_id());
                    if (redDot>0){
                        helper.getView(R.id.group_notice_tip).setVisibility(View.VISIBLE);
                        helper.setText(R.id.group_notice_tip, redDot>99?"99+":redDot+"");
                    }
                }
                Glide.with(context).load(item.getGroup_photo()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) helper.getView(R.id.img_group_seed_header));
            }
        }
    }
}
