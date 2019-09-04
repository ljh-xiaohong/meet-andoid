package com.yuejian.meet.framents.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.GroupListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.GroupAllEntity;
import com.yuejian.meet.bean.GroupEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 群聊搜索
 */
public class GroupChatSearchActivity extends BaseActivity implements TextView.OnEditorActionListener{
    @Bind(R.id.et_group_search)
    EditText et_group_search;
    @Bind(R.id.group_list)
    ListView listView;
    GroupListAdapter mAdapter;
    List<GroupEntity> listData=new ArrayList<>();
    GroupEntity myGroupEntity=new GroupEntity();
    GroupEntity familyGroup=new GroupEntity();
    GroupAllEntity groupAllEntity=new GroupAllEntity();
    ApiImp api=new ApiImp();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_search);
        initData();
    }
    public void initData(){
        et_group_search.setOnEditorActionListener(this);
        mAdapter=new GroupListAdapter(listView,listData,R.layout.item_group_list_parent_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.job_imgBtn_search,R.id.group_chat_cancel})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.job_imgBtn_search:
                search(et_group_search.getText().toString());
                hintKbTwo();
                break;
            case R.id.group_chat_cancel:
                finish();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER))
        {
            search(v.getText().toString());
            hintKbTwo();
            return true;
        }
        return false;
    }
    public void search(String name){
        if (user==null)return;
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("keyword",name);
        api.getRoupAllName(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData.clear();
                groupAllEntity= JSON.parseObject(data,GroupAllEntity.class);
                listData= JSON.parseArray(groupAllEntity.getCity_list(),GroupEntity.class);
                myGroupEntity=JSON.parseObject(groupAllEntity.getMy_group(),GroupEntity.class);
                familyGroup=JSON.parseObject(groupAllEntity.getFamily_group(),GroupEntity.class);
                if (familyGroup.getList().length()>4)
                    listData.add(0,familyGroup);
                listData.add(0,myGroupEntity);
                mAdapter.refresh(listData);
            }
            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
