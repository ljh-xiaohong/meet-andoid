package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.HistoryFeedbackAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.UserFeedbackEntity;
import com.yuejian.meet.common.Constants;
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
 * 历史反馈
 */
public class HistoryFeedbackActivity extends BaseActivity implements SpringView.OnFreshListener{

    @Bind(R.id.feedback_spring)
    SpringView springView;
    @Bind(R.id.feedback_list)
    ListView listView;
    @Bind(R.id.txt_titlebar_release)
    TextView txt_titlebar_release;
    HistoryFeedbackAdapter mAdapter;
    List<UserFeedbackEntity> listData=new ArrayList<>();
    int pageIndex=1;
    int status=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_feedback);
        if (getIntent().hasExtra("status")){
            status=getIntent().getIntExtra("status",0);
        }
        setTitleText(getString(R.string.history_feedback));
        initView();
        requstData();
    }
    public void initView(){
        txt_titlebar_release.setVisibility(status==0?View.VISIBLE:View.GONE);
        txt_titlebar_release.setText(R.string.user_feedback_bug5);
        txt_titlebar_release.setTextColor(Color.parseColor("#b89477"));
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        mAdapter=new HistoryFeedbackAdapter(listView,listData,R.layout.item_history_feedback_list_layout);
        listView.setAdapter(mAdapter);
        mAdapter.setStatus(status);
        mAdapter.notifyDataSetChanged();

    }
    public void requstData(){
        final Map<String,Object> params=new HashMap<>();
        params.put("pageIndex", ""+pageIndex);
        params.put("pageItemCount", Constants.pageItemCount);
        params.put("feedback_status",status+"");
        params.put("customer_id",user.getCustomer_id());
        apiImp.getFeedback(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex==1){
                    listData.clear();
                }
                listData.addAll(JSON.parseArray(data,UserFeedbackEntity.class));

                mAdapter.refresh(listData);

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @OnClick({R.id.txt_titlebar_release})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_titlebar_release:
                Intent intent=new Intent(this,HistoryFeedbackActivity.class);
                intent.putExtra("status",1);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==RESULT_OK){
            switch (requestCode){
                case 23:
                    pageIndex=1;
                    requstData();
                    break;
            }
        }
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        requstData();
    }

    @Override
    public void onLoadmore() {
        pageIndex+=1;
        requstData();
    }
}
