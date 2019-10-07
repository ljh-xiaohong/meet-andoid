package com.yuejian.meet.activities.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.CityAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.CityBean;
import com.yuejian.meet.utils.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CityACtivity extends BaseActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.list)
    RecyclerView list;
    List<String> datas = new ArrayList<>();
    CityAdapter cityAdapter;
    @Bind(R.id.select_lay)
    LinearLayout selectLay;
    @Bind(R.id.tips)
    TextView tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        initView();
        getCity();
    }

    private void getCity() {
        Map<String, Object> params = new HashMap<>();
        params.put("province", getIntent().getStringExtra("province"));
        apiImp.acquireCity(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                CityBean bean = new Gson().fromJson(data, CityBean.class);
                for (int i = 0; i < bean.getData().size(); i++) {
                    datas.add(bean.getData().get(i).getCity());
                    cityAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(CityACtivity.this, errMsg);
            }
        });
    }

    private void initView() {
        selectLay.setVisibility(View.GONE);
        tips.setText("选择城市");
        cityAdapter = new CityAdapter(this, datas);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(cityAdapter);
        cityAdapter.setClick(new CityAdapter.OnClick() {
            @Override
            public void click(int position) {
                Intent intent = new Intent();
                intent.putExtra("city", datas.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
