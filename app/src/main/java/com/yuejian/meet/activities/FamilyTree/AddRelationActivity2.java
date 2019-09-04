package com.yuejian.meet.activities.FamilyTree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.TreeTagAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.FriendTreeTagEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.InnerGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class AddRelationActivity2 extends BaseActivity{
    @Bind(R.id.my_op_relation)
    TextView my_op_relation;
    @Bind(R.id.op_my_relation)
    TextView op_my_relation;
    @Bind(R.id.add_relation)
    TextView add_relation;
    @Bind(R.id.hint_sel)
    TextView hint_sel;
    @Bind(R.id.hint_top)
    TextView hint_top;
    @Bind(R.id.realtion_grid)
    InnerGridView gridView;
    String userName;
    String op_customer_id;
    List<FriendTreeTagEntity> listData=new ArrayList<>();
    TreeTagAdapter mAdapter;
    String friend_name;
    MembersEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_relation2);
        setContentView(R.layout.activity_add_relation3);
        initData();
    }
    public void initData(){
        ImUtils.loginIm();
//        userName=getIntent().getStringExtra("userName");
//        op_customer_id=getIntent().getStringExtra("op_customer_id");
        setTitleText(getString(R.string.treeadd_name8));
//        op_my_relation.setText("我是"+userName+"的");
//        my_op_relation.setText(userName+"是我的");
        add_relation.setSelected(true);
        mAdapter=new TreeTagAdapter(gridView,listData,R.layout.item_tree_tag_layout);
        gridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        getUserData();
    }
    public void getUserData(){
        String inviteCode = PreferencesUtil.get(getApplicationContext(), AppConfig.INVITE_CODE, "");
        Map<String,Object> params=new HashMap<>();
        params.put("invite_code",inviteCode);
        apiImp.getCodeInfoUserData(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity= JSON.parseObject(data,MembersEntity.class);
                userName=entity.getSurname()+entity.getName();
                op_customer_id=entity.getCustomer_id();

                op_my_relation.setText(getString(R.string.treeadd_name9)+userName+"的");
                my_op_relation.setText(userName+getString(R.string.treeadd_name10));
                hint_top.setText(getString(R.string.treeadd_name18)+userName+getString(R.string.treeadd_name19));
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @OnClick({R.id.sel_layout,R.id.submit,R.id.next})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.sel_layout:
                startActivityIfNeeded(new Intent(this,TreeTagActivity.class),20);
                break;
            case R.id.submit:
                if (StringUtils.isEmpty(friend_name)){
                    ViewInject.toast(getBaseContext(),R.string.treeadd_name11);
                    return;
                }
                requsetFriend();
                break;
            case R.id.next:
                intentMain();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 20:
                    friend_name=data.getStringExtra("friend_name");
                    add_relation.setText(friend_name);
                    add_relation.setVisibility(View.VISIBLE);
                    hint_sel.setText("");
                    getOpname();
                    break;
            }
        }
    }
    public void setSel(FriendTreeTagEntity item){
        for (int i=0;i<listData.size();i++){
            if (item.getFriend_name().equals(listData.get(i).getFriend_name())){
                listData.get(i).setSelect(true);
            }else {
                listData.get(i).setSelect(false);
            }
        }
        mAdapter.refresh(listData);
    }
    public void getOpname(){
        Map<String,Object> params=new HashMap<>();
        params.put("friend_name",friend_name);
        params.put("sex",user.getSex());
        apiImp.getOpName(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData.clear();
                String[] arrayName=data.split(",");
                for (int i=0;i<arrayName.length;i++){
                    FriendTreeTagEntity entity=new FriendTreeTagEntity();
                    entity.setFriend_name(arrayName[i]);
                    listData.add(entity);
                }
                listData.get(0).setSelect(true);
                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void requsetFriend(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("op_customer_id",op_customer_id);
        params.put("friend_name",friend_name);
        params.put("op_friend_name",getOpFriend_name());
        apiImp.postRequstFriend(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                intentMain();
                ViewInject.toast(getApplication(),R.string.treeadd_name20);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public String getOpFriend_name(){
        String name="";
        for (int i=0;i<listData.size();i++){
            if (listData.get(i).isSelect()){
                name=listData.get(i).getFriend_name();
                break;
            }
        }
        return name;
    }
    public void intentMain(){
        Intent intent=new Intent(this, MainActivity.class);
        intent.putExtra("finish_register", true);
        startActivity(intent);
        finish();
    }
}
