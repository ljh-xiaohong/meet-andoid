package com.yuejian.meet.activities.creation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.DraftAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.DraftEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

public class DraftActivity extends BaseActivity {


    @Bind(R.id.activity_draft_back)
    View back;

    @Bind(R.id.activity_draft_recyclerview)
    RecyclerView recyclerView;

    DraftAdapter adapter;

    private int type = -1;

    private List<DraftEntity> draftEntities;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft);
        if (!getData()) return;
        init();
        getDataFromNet();
    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, DraftActivity.class);
        intent.putExtra("DraftActivity.type", type);
        context.startActivity(intent);
    }

    private boolean getData() {
        type = getIntent().getIntExtra("DraftActivity.type", type);
        return type != -1;
    }

    private void init() {
        adapter = new DraftAdapter(recyclerView, mContext, type);

    }

    private void getDataFromNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("customerId", AppConfig.CustomerId);
        apiImp.getDraftsList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (TextUtils.isEmpty(data)) return;
                JSONObject jo = JSONObject.parseObject(data);
                if (null == jo && !jo.getString("code").equals("0")) return;
                draftEntities = JSON.parseArray(jo.getString("data"), DraftEntity.class);
                if (draftEntities != null && draftEntities.size() > 0) {
                    adapter.refresh(draftEntities);
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }


}
