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
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.GroupCityListActivity;
import com.yuejian.meet.activities.home.GroupFootprintActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.GroupChatEntity;
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
 * 群列表adpter
 */

public class GroupChatListAdapter extends FKAdapter<GroupChatEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public GroupChatListAdapter(AbsListView view, List<GroupChatEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
    }
    public void convert(AdapterHolder helper, GroupChatEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, final GroupChatEntity item, final int position){
        this.mHelper=helper;
        mHelper.setText(R.id.group_city_name,item.getGroupName());
        mHelper.getView(R.id.group_count).setVisibility(item.getNumbers()>0?View.VISIBLE:View.GONE);
        mHelper.setText(R.id.group_count,"("+item.getNumbers()+")");
//        mHelper.getView(R.id.img_group_pull_down).setVisibility(position==0?View.GONE:View.VISIBLE);
        final InnerListView listView=mHelper.getView(R.id.group_chat_item_list);
        listView.setVisibility(item.getOpen()==false?View.GONE:View.VISIBLE);
        final GroupListAdapter mAdapter= new GroupListAdapter(listView, item.getGroupList(), R.layout.item_group_list_parent_layout);
        if (item.getOpen()){
            listView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
//        final List<GroupSeedEntity> listData= JSON.parseArray(item.getCity_family_list()==null?item.getList():item.getCity_family_list(),GroupSeedEntity.class);
//        final listAdpter mAdpter=new listAdpter(listView,listData,R.layout.item_group_list_layout);
        final ImageView selectImg=mHelper.getView(R.id.img_group_pull_down);
        selectImg.setSelected(item.getOpen());
//        if (item.getOpen()||position==0){
//            listView.setAdapter(mAdpter);
//            mAdpter.notifyDataSetChanged();
//        }
        mHelper.getView(R.id.group_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setOpen(!item.getOpen());
                listView.setVisibility(item.getOpen()==false?View.GONE:View.VISIBLE);
                if (item.getOpen()){
                    listView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                selectImg.setSelected(item.getOpen());
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                if (listData.get(position).getT_id()!=null){
//                    if (!StringUtil.isEmpty(item.getProvince())){
//                        if (item.getProvince().contains("好友群聊")){
//                            ImUtils.toAssemblyHall(mCxt,listData.get(position).getT_id(), ChatEnum.FoundGroup,"0");
////                        ImUtils.toTeamSession(mCxt,listData.get(position).getT_id());
//                            return;
//                        }
//                    }
//                    final ProgressDialog dialog=new ProgressDialog(context);
//                    dialog.setMessage("正在操作..");
//                    dialog.show();
//                    Map<String,String> params=new HashMap<String, String>();
//                    params.put("customer_id", AppConfig.CustomerId);
//                    params.put("t_id",listData.get(position).getT_id());
//                    new ApiImp().intoGroup(params, this, new DataIdCallback<String>() {
//                        @Override
//                        public void onSuccess(String data, int id) {
//                            if (dialog!=null)dialog.dismiss();
//                            ImUtils.toTeamSession(mCxt,listData.get(position).getT_id(),data);
//                        }
//                        @Override
//                        public void onFailed(String errCode, String errMsg, int id) {
//                            if (dialog!=null)dialog.dismiss();
//                        }
//                    });
////                    ImUtils.toTeamSession(mCxt,listData.get(position).getT_id());
//                }else {
//                    Intent intent=new Intent(mCxt, GroupCityListActivity.class);
//                    intent.putExtra("province",item.getProvince());
//                    intent.putExtra("city",listData.get(position).getCity());
//                    mCxt.startActivity(intent);
//                }
////                NimUIKit.startTeamSession(mCxt,item.get);
//            }
//        });
//        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.img_member_header));
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
            helper.getView(R.id.txt_group_seed_cnt).setVisibility(item.getMember_cnt()==null?View.GONE:View.VISIBLE);
            if (position==0){
                helper.getView(R.id.item_view).setVisibility(View.GONE);
            }else {
                helper.getView(R.id.item_view).setVisibility(View.VISIBLE);
            }

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
                        helper.setText(R.id.group_notice_tip,redDot>99?"99+":redDot+"");
                    }
                }
                Glide.with(context).load(item.getGroup_photo()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) helper.getView(R.id.img_group_seed_header));
            }
        }
    }
}
