package com.yuejian.meet.activities.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import com.yuejian.meet.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageSettingActivity extends AppCompatActivity {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.select_city)
    TextView selectCity;
    @Bind(R.id.select_industry)
    TextView selectIndustry;
    @Bind(R.id.recommended_user_switch)
    Switch recommendedUserSwitch;
    @Bind(R.id.recommended_product_switch)
    Switch recommendedProductSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setText("精准推送设置");
        back.setOnClickListener(v -> finish());
    }

    @OnClick({R.id.select_city, R.id.select_industry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_city:
                startActivity(new Intent(this, ProvinceActivity.class));
                break;
            case R.id.select_industry:
                startActivity(new Intent(this, NewChooseIndustryActivity.class));
                break;
        }
    }
}
