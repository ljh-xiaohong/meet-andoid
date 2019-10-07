package com.yuejian.meet.activities.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.CityAdapter;
import com.yuejian.meet.bean.ProvinceBean;
import com.yuejian.meet.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProvinceActivity extends FragmentActivity {


    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.positioning_city)
    TextView positioningCity;
    @Bind(R.id.shenzhen)
    TextView shenzhen;
    @Bind(R.id.beijing)
    TextView beijing;
    @Bind(R.id.shanghai)
    TextView shanghai;
    @Bind(R.id.guangzhou)
    TextView guangzhou;
    @Bind(R.id.list)
    RecyclerView list;
    List<ProvinceBean.DataBean> data=new ArrayList<>();
    List<String> datas=new ArrayList<>();
    CityAdapter cityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        initView();
        initData();
    }
    protected void initData() {
        String json= Utils.getFromAssets("province", this);
        ProvinceBean provinceBean= new Gson().fromJson(json, ProvinceBean.class);
        data.addAll(provinceBean.getData());
        for (int i=0;i<data.size();i++){
            datas.add(data.get(i).getProvince());
        }
        cityAdapter.notifyDataSetChanged();
    }
    private void initView() {
        cityAdapter = new CityAdapter(this,datas);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(cityAdapter);
        cityAdapter.setClick(new CityAdapter.OnClick() {
            @Override
            public void click(int position) {
                Intent intent=new Intent(ProvinceActivity.this,CityACtivity.class);
                intent.putExtra("province",data.get(position).getProvince());
                startActivityForResult(intent,3);
            }
        });
    }

    @OnClick({R.id.back, R.id.positioning_city, R.id.shenzhen, R.id.beijing, R.id.shanghai, R.id.guangzhou})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.positioning_city:
                intent.putExtra("city", positioningCity.getText().toString());
                break;
            case R.id.shenzhen:
                intent.putExtra("city", shenzhen.getText().toString());
                break;
            case R.id.beijing:
                intent.putExtra("city", beijing.getText().toString());
                break;
            case R.id.shanghai:
                intent.putExtra("city", shanghai.getText().toString());
                break;
            case R.id.guangzhou:
                intent.putExtra("city", guangzhou.getText().toString());
                break;
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                Intent intent = new Intent();
                intent.putExtra("city", data.getStringExtra("city"));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
