package com.yuejian.meet.activities.message;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.NewActionAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.NewsActionEntity;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
/**
 * @author :
 * @time   : 2018/11/16 16:24
 * @desc   : 百家姓新闻动态
 * @version: V1.0
 * @update : 2018/11/16 16:24
 */

public class NewActionActivity extends BaseActivity implements SpringView.OnFreshListener {
    @Bind(R.id.new_action_list)
    ListView listView;
    @Bind(R.id.new_action_spring)
    SpringView springView;
    NewActionAdapter mAdapter;
    List<String> listStringData=new ArrayList<>();
    List<NewsActionEntity> listData=new ArrayList<>();
    int pageIndex=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_action);
        initView();
    }
    public void initView(){
        setTitleText("百家姓新闻动态");
        mAdapter=new NewActionAdapter(listView,listStringData,R.layout.item_new_action_main_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        springView.setHeader(new DefaultHeader(this));
        springView.setListener(this);
        requstData();
    }

    /**
     * 加载新闻动态
     */
    public void requstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("pageIndex",pageIndex+"");
        params.put("pageItemCount", "3");
        params.put("listLastId","");
        apiImp.getNewActionList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<String> listString= JSON.parseArray(data,String.class);
                for (int i=0;i<listString.size();i++){
                    listStringData.add(listString.get(i));
                }
//                listString.addAll(0,JSON.parseArray(data,String.class));
                mAdapter.refresh(listStringData);
                if (pageIndex==1){
                    listView.setSelection(mAdapter.getCount()-1);
                }
//                listData.addAll(0, JSON.parseArray(data,NewsActionEntity.class));
//                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex+=1;
        requstData();

    }

    @Override
    public void onLoadmore() {

    }
}
