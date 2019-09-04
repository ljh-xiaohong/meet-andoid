package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.adapters.BotActityAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.BotEntity;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的宗享会项目
 */
public class MyBotActivity extends BaseActivity implements SpringView.OnFreshListener{
    @Bind(R.id.bot_spring)
    SpringView springView;
    @Bind(R.id.bot_list)
    ListView listView;
    List<BotEntity> listData=new ArrayList<>();
    BotActityAdapter mAdapter;
    int pageIndex=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bot);
        setTitleText(getString(R.string.bot_txt_0));
        springView.setFooter(new DefaultFooter(this));
        springView.setHeader(new DefaultHeader(this));
        springView.setListener(this);
        mAdapter=new BotActityAdapter(listView,listData,R.layout.item_bot_list_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        getRequstData();
    }
    public void getRequstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("pageIndex",pageIndex+"");
        params.put("pageItemCount",20+"");
        apiImp.getBotMyList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex==1){
                    listData.clear();
                }
                listData.addAll(JSON.parseArray(data,BotEntity.class));
                mAdapter.refresh(listData);
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                springView.onFinishFreshAndLoad();
            }
        });
    }

    @OnClick({R.id.launch_enjoy, R.id.bu_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.launch_enjoy:
                startActivity(new Intent(this,CreateEnjoyActivity.class));
                break;
            case R.id.bu_next:
                if (user==null){
                    startActivity(new Intent(this, LoginActivity.class));
                }else {
                    startActivityIfNeeded(new Intent(this,CreateBotActivity.class),110);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==RESULT_OK){
            pageIndex=1;
            getRequstData();
        }
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        getRequstData();
    }

    @Override
    public void onLoadmore() {
        pageIndex+=1;
        getRequstData();
    }
}
