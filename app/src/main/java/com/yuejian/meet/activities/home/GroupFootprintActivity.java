package com.yuejian.meet.activities.home;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.GroupFootprintAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.GroupFootprintEntity;
import com.yuejian.meet.utils.ImUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

import static com.yuejian.meet.utils.tinkerutil.SampleApplicationContext.context;

/**
 * 群聊足迹
 */
public class GroupFootprintActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    @Bind(R.id.footprint_list)
    ListView listView;
    GroupFootprintAdapter mAdapter;
    List<GroupFootprintEntity> listData=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_footprint);
        initData();
    }
    public void initData(){
        setTitleText("足迹");
        listView.setOnItemClickListener(this);
        mAdapter=new GroupFootprintAdapter(listView,listData,R.layout.item_group_footprint_list);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        requstData();
    }
    public void requstData(){
        new ApiImp().getFootmrk(new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData= JSON.parseArray(data,GroupFootprintEntity.class);
                mAdapter.refresh(listData);
            }
            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final ProgressDialog dialog=new ProgressDialog(context);
        dialog.setMessage("正在操作..");
        dialog.show();
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("t_id",listData.get(position).getT_id());
        new ApiImp().intoGroup(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog!=null)dialog.dismiss();
                ImUtils.toTeamSession(mContext,listData.get(position).getT_id(),data);
            }
            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)dialog.dismiss();
            }
        });
    }
}
