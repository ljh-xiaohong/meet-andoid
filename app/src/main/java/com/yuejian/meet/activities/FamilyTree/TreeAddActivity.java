package com.yuejian.meet.activities.FamilyTree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.PerfectUserDataActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.TreeAddAdapter1;
import com.yuejian.meet.adapters.TreeAddAdapter2;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.FindMemberEntity;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.bean.TreeAddEntity;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.InnerListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *待处理事项
 */
public class TreeAddActivity extends BaseActivity {
    @Bind(R.id.tree_list1)
    InnerListView tree_list1;
    @Bind(R.id.tree_list2)
    InnerListView tree_list2;
    @Bind(R.id.no_user_requst)
    ImageView no_user_requst;
    TextView suer_perfect_data;
    TextView foot_user_name,foot_company_name,foot_job;
    TreeAddEntity entity=new TreeAddEntity();
    List<FindMemberEntity> listTop=new ArrayList<>();
    List<FindMemberEntity> list=new ArrayList<>();
    TreeAddAdapter1 mAdapter1;
    TreeAddAdapter2 mAdapter2;
    private Mine mine;
    View view;

//    item_tree_add_list_layout
//    item_tree_add_list_top_layout
//    item_tree_user_top_layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_add);
        initData();
    }
    public void initData(){
        setTitleText(getString(R.string.treeadd_name2));
        view=View.inflate(this,R.layout.item_tree_user_top_layout,null);
        view.setTag("100");
        suer_perfect_data= (TextView) view.findViewById(R.id.suer_perfect_data);
        foot_user_name= (TextView) view.findViewById(R.id.foot_user_name);
        foot_company_name= (TextView) view.findViewById(R.id.foot_company_name);
        foot_job= (TextView) view.findViewById(R.id.foot_job);
        Glide.with(this).load(user.getPhoto()).error(R.mipmap.ic_default).into((ImageView) view.findViewById(R.id.foot_photo));
        suer_perfect_data.setOnClickListener(this);
        suer_perfect_data.setSelected(true);
        mAdapter1=new TreeAddAdapter1(tree_list1,listTop,R.layout.item_tree_add_list_top_layout);
        if (StringUtils.isEmpty(PreferencesUtil.get(mContext,user.getCustomer_id()+PreferencesUtil.Company_name,"")) && StringUtils.isEmpty(user.getCompany_name())){
            tree_list1.addFooterView(view);
        }
        mAdapter1.notifyDataSetChanged();
        tree_list1.setAdapter(mAdapter1);
        mAdapter2=new TreeAddAdapter2(tree_list2,list,R.layout.item_tree_add_list_layout);
        tree_list2.setAdapter(mAdapter2);
        requstData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.suer_perfect_data:
                Intent intent=new Intent(this, PerfectUserDataActivity.class);
                intent.putExtra("noRegister",true);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        findMyInfo();
    }

    private void findMyInfo() {
        if (!StringUtils.isEmpty(PreferencesUtil.get(mContext,user.getCustomer_id()+PreferencesUtil.Company_name,"")) || !StringUtils.isEmpty(user.getCompany_name())){
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.getCustomer_id());
        apiImp.findMyInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                mine = JSON.parseObject(data, Mine.class);
                if (!StringUtils.isEmpty(mine.getCompany_name())){
                    PreferencesUtil.put(mContext,user.getCustomer_id()+PreferencesUtil.Company_name,mine.getCompany_name());
                    if (tree_list1.getFooterViewsCount()==1){
                        tree_list1.removeFooterView(view);
                    }
                }
                setMyView();
                refreshList();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
    public void setMyView(){
        if (StringUtils.isEmpty(mine.getCompany_name())){
            suer_perfect_data.setVisibility(View.VISIBLE);
        }else {
            suer_perfect_data.setVisibility(View.GONE);
            foot_user_name.setText(mine.getSurname()+mine.getName());
            foot_company_name.setText(mine.getCompany_name());
            foot_job.setText(mine.getJob());
        }
    }

    public void requstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        apiImp.getFriendRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity=JSON.parseObject(data,TreeAddEntity.class);
                listTop= JSON.parseArray(entity.getRequestList(),FindMemberEntity.class);
                list= JSON.parseArray(entity.getMayKnowList(),FindMemberEntity.class);
                mAdapter1.refresh(listTop);
                mAdapter2.refresh(list);
                refreshList();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void postData(String poId,String status){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("friend_request_id",poId);
        params.put("status",status);
        apiImp.getAddFriend(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                requstData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void refreshList(){
        if (listTop.size()==0 ){
            if (!StringUtils.isEmpty(PreferencesUtil.get(mContext,user.getCustomer_id()+PreferencesUtil.Company_name,"")) || !StringUtils.isEmpty(user.getCompany_name())){
                no_user_requst.setVisibility(View.VISIBLE);
                tree_list1.setVisibility(View.GONE);
            }else {
                no_user_requst.setVisibility(View.GONE);
                tree_list1.setVisibility(View.VISIBLE);
            }
        }else {
            no_user_requst.setVisibility(View.GONE);
            tree_list1.setVisibility(View.VISIBLE);
        }
    }
}
