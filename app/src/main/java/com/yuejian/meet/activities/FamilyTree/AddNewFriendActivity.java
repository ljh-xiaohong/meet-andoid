package com.yuejian.meet.activities.FamilyTree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.AddNewFriendAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.FindMemberEntity;
import com.yuejian.meet.bean.TreeAddEntity;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 添加亲朋友
 */
public class AddNewFriendActivity extends BaseActivity implements TextView.OnEditorActionListener,SpringView.OnFreshListener{
    @Bind(R.id.et_search_all)
    CleanableEditText editText;
    @Bind(R.id.friend_list)
    ListView listView;
    @Bind(R.id.friend_spring)
    SpringView springView;
    TreeAddEntity entity=new TreeAddEntity();
    List<FindMemberEntity> listData=new ArrayList<>();
    AddNewFriendAdapter mAdapter;
    int pageIndext=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_friend);
        initData();
    }
    public void initData(){
        setTitleText(getString(R.string.treeadd_name5));
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(this);
        editText.setOnEditorActionListener(this);
        mAdapter= new AddNewFriendAdapter(listView,listData,R.layout.item_add_new_friend_list_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        requstData();
    }
    public void requstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("keyword",editText.getText().toString());
        params.put("pageIndex",pageIndext+"");
        params.put("pageItemCount","20");
        apiImp.getMayKnowList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndext==1){
                    listData.clear();
                }
                entity= JSON.parseObject(data,TreeAddEntity.class);
                listData.addAll(JSON.parseArray(entity.getMayKnowList(),FindMemberEntity.class));
                mAdapter.refresh(listData);
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            pageIndext=1;
            requstData();
            hintKbTwo();
            return true;
        }
        return false;
    }
    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onRefresh() {
        pageIndext=1;
        requstData();
    }

    @Override
    public void onLoadmore() {
        pageIndext+=1;
        requstData();
    }
}
