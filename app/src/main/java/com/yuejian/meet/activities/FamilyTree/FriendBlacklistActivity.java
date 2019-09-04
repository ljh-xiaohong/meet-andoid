package com.yuejian.meet.activities.FamilyTree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.FriendBlackListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.AddressListEntity;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.CleanableEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 黑名单
 */
public class FriendBlacklistActivity extends BaseActivity implements TextView.OnEditorActionListener{
    @Bind(R.id.black_list)
    ListView listView;
    @Bind(R.id.et_search_all)
    CleanableEditText editText;
    FriendBlackListAdapter mAdapter;
    List<AddressListEntity> listData=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_friend_blacklist);
        initView();
        initData();
    }
    public void initData(){
        setResult(RESULT_OK);
        setTitleText(getString(R.string.blacklist));
        mAdapter=new FriendBlackListAdapter(listView,listData,R.layout.item_balck_list_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        requstData();
    }
    public void initView(){
        editText.setOnEditorActionListener(this);
    }
    public void postBalck(String opCustomeerId){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("op_customer_id",opCustomeerId);
        params.put("is_black","0");
        apiImp.getBlack(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                requstData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    public void requstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("name",editText.getText().toString());
        apiImp.getBalck(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData= JSON.parseArray(data,AddressListEntity.class);
                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            requstData();
            Utils.hideSystemKeyBoard(this,editText);
            return true;
        }
        return false;
    }
}
