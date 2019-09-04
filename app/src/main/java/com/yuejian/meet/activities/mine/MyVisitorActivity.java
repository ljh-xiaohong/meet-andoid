package com.yuejian.meet.activities.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.MyVisitorAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.VisitListEntity;
import com.yuejian.meet.bean.VisitorAcEntity;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 我的访问量
 */
public class MyVisitorActivity extends BaseActivity implements SpringView.OnFreshListener{
    @Bind(R.id.txt_find_count)
    TextView txt_find_count;
    @Bind(R.id.txt_visit_number)
    TextView txt_visit_number;
    @Bind(R.id.spring)
    SpringView spring;
    @Bind(R.id.list_view)
    ListView list_view;
    VisitorAcEntity entity;
    List<VisitListEntity> listData=new ArrayList<>();
    MyVisitorAdapter mAdapter;
    int pageIndex=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_visitor);
        initData();
    }
    public void initData(){
        setTitle("我的访客");
        spring.setFooter(new DefaultFooter(this));
        spring.setHeader(new DefaultHeader(this));
        spring.setListener(this);
        mAdapter=new MyVisitorAdapter(list_view,listData,R.layout.item_visitor_list_layout);
        list_view.setAdapter(mAdapter);
        requstData();
    }
    public void requstData(){
        final Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("pageIndex",""+pageIndex);
        params.put("pageItemCount","20");
        apiImp.getfindVisitInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity= JSON.parseObject(data,VisitorAcEntity.class);
                if (pageIndex==1){
                    listData.clear();
                }
                txt_find_count.setText(entity.getVisitCount());
                txt_visit_number.setText(entity.getTodayVisitCount());
                listData.addAll(JSON.parseArray(entity.getVisitList(),VisitListEntity.class));
                mAdapter.refresh(listData);
                spring.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                spring.onFinishFreshAndLoad();
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
        pageIndex+=1;
        requstData();
    }
}
