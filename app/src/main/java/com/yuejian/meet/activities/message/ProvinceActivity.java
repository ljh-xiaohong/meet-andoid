package com.yuejian.meet.activities.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.adapters.CityAdapter;

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
    List<String> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        CityAdapter cityAdapter = new CityAdapter(this,data);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(cityAdapter);
        cityAdapter.setClick(new CityAdapter.OnClick() {
            @Override
            public void click(int position) {
                startActivity(new Intent(ProvinceActivity.this,CityACtivity.class));
            }
        });
    }

    @OnClick({R.id.back, R.id.positioning_city, R.id.shenzhen, R.id.beijing, R.id.shanghai, R.id.guangzhou})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.positioning_city:
                break;
            case R.id.shenzhen:
                break;
            case R.id.beijing:
                break;
            case R.id.shanghai:
                break;
            case R.id.guangzhou:
                break;
        }
    }
}
