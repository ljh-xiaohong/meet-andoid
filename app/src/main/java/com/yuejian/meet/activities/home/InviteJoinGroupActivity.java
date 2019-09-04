package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.activity.GiftGroupActivity;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.InviteJoinGroupAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.GroupSameSurnameEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 邀请聊天
 */
public class InviteJoinGroupActivity extends BaseActivity implements SpringView.OnFreshListener,AdapterView.OnItemClickListener{

    @Bind(R.id.invite_spring)
    SpringView springView;
    @Bind(R.id.invtie_list)
    ListView listView;
    @Bind(R.id.invtie_content)
    TextView invtie_content;
    @Bind(R.id.invite_cb)
    CheckBox invite_cb;
    InviteJoinGroupAdapter mAdapter;
    List<GroupSameSurnameEntity> listData=new ArrayList<>();
    String customer_ids="";
    String tid;
    int pageIndex=1;
    ApiImp api=new ApiImp();

    private LoadingDialogFragment dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_join_group);
        tid=getIntent().getStringExtra("tid");
        initData();
    }
    public void initData(){
        dialog= new LoadingDialogFragment().newInstance("正在邀请");
        setTitleText("邀请聊天");
        mAdapter=new InviteJoinGroupAdapter(listView,listData,R.layout.item_invite_join_list_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        listView.setOnItemClickListener(this);
        requstData();
    }
    public void requstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("t_id",tid);
        params.put("pageIndex",pageIndex+"");
        params.put("pageItemCount", Constants.pageItemCount);
        api.getNoTeamSameSurname(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex==1){
                    listData.clear();
                }
                pageIndex++;
                listData.addAll(JSON.parseArray(data,GroupSameSurnameEntity.class));
                mAdapter.refresh(listData);

                if (springView!=null)springView.onFinishFreshAndLoad();

            }
            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView!=null)springView.onFinishFreshAndLoad();
            }
        });
    }

    @OnClick({R.id.invite_cb,R.id.invtie_content})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invite_cb://选择
                for (int n=0;n<listData.size();n++){
                    listData.get(n).setChoose(invite_cb.isChecked());
                }
                mAdapter.refresh(listData);
                selChcked();
                break;
            case R.id.invtie_content://邀请
                selGift();
                break;
        }
    }
    //选择
    public void selChcked(){
        int countSle=0;
        for (int i=0;i<listData.size();i++){
            if (listData.get(i).getChoose()){
                ++countSle;
            }
        }
        invtie_content.setText("邀请("+countSle+")");
        if (countSle==listData.size()){
            invite_cb.setChecked(true);
        }else {
            invite_cb.setChecked(false);
        }
    }
    public void selGift(){
        customer_ids="";
        for (int i=0;i<listData.size();i++){
            if (StringUtil.isEmpty(customer_ids)){
                if (listData.get(i).getChoose())
                    customer_ids+=listData.get(i).getCustomer_id();
            }else {
                if (listData.get(i).getChoose())
                    customer_ids+=","+listData.get(i).getCustomer_id();
            }
        }
        String[] checkedSize=customer_ids.split(",");
        if (StringUtil.isEmpty(customer_ids)){
            ViewInject.shortToast(this,"请至少选择一个");
            return;
        }
        Intent intent=new Intent(this, GiftGroupActivity.class);
        intent.putExtra("count",checkedSize.length+"");
        startActivity(intent);
        new GiftGroupActivity().setOnSendGiftLister(new GiftGroupActivity.OnSendGiftListener() {
            @Override
            public void sendGift(GiftAllEntity giftBean, JSONObject jsonData) {
                sendInvite(giftBean);
            }
        },tid);
    }
    //发送邀请
    public void sendInvite(GiftAllEntity giftBean){
        if (dialog!=null){
            dialog.show(getFragmentManager(),"");
        }
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        params.put("customer_ids",customer_ids);
        params.put("t_id",tid);
        params.put("gift_id",giftBean.getId());
        api.invtieIntoGroup(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ViewInject.shortToast(getApplication(),"邀请成功");
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)dialog.dismiss();
            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        requstData();
    }

    @Override
    public void onLoadmore() {
        requstData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listData.get(position).setChoose(!listData.get(position).getChoose());
        mAdapter.refresh(listData);
    }
}
