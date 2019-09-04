package com.yuejian.meet.framents.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.MemberAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/10/23/023.
 */

public class MemberSearchActivity extends BaseActivity implements SpringView.OnFreshListener {

    private ListView listView;
    private MemberAdapter adapter;
    private SpringView springView;
    List<MembersEntity> listData = new ArrayList<>();
    private int pageIndex = 1;
    private EditText searchEdt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY_NAME = "宗亲成员搜索";
        setContentView(R.layout.activity_member_search);
        springView = (SpringView) findViewById(R.id.spring_view);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(this);
        listView = (ListView) findViewById(R.id.member_list);
        adapter = new MemberAdapter(listView, listData, R.layout.item_member_fragment);
        listView.setAdapter(adapter);
        searchEdt = (EditText) findViewById(R.id.et_search);
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    String keyWord = searchEdt.getText().toString().trim();
                    listData.clear();
                    onSearch(keyWord);
                }
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppUitls.goToPersonHome(mContext,  listData.get(position).customer_id);
            }
        });
        findViewById(R.id.search_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onSearch(String keyWord) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("queryParam", keyWord);
        params.put("pageIndex", String.valueOf(pageIndex));
        apiImp.searchCustomerByIdOrName(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<MembersEntity> membersEntities = JSON.parseArray(data, MembersEntity.class);
                if (membersEntities != null && !membersEntities.isEmpty()) {
                    findViewById(R.id.none_data).setVisibility(View.GONE);
                    if (pageIndex == 1) {
                        if (listData.isEmpty()) {
                            listData.addAll(membersEntities);
                        } else if (!membersEntities.get(0).customer_id.equals(listData.get(0).customer_id)) {
                            listData.addAll(0, membersEntities);
                        }
                    } else {
                        listData.addAll(membersEntities);
                    }
                } else {
                    if (listData.isEmpty()) {
                        findViewById(R.id.none_data).setVisibility(View.VISIBLE);
                    }
                    if (pageIndex > 1) {
                        pageIndex--;
                    }
                }
                adapter.refresh(listData);
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        onSearch(searchEdt.getText().toString());
    }

    @Override
    public void onLoadmore() {
        pageIndex++;
        onSearch(searchEdt.getText().toString());
    }
}
