package com.yuejian.meet.activities.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.MessageSettingBean;
import com.yuejian.meet.bean.NewFriendBean;
import com.yuejian.meet.bean.SelectBean;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ApiImp apiImp = new ApiImp();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        apiImp.getUsrServiceInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                MessageSettingBean bean=new Gson().fromJson(data,MessageSettingBean.class);
                if (bean.getCode()!=0) {
                    ViewInject.shortToast(MessageSettingActivity.this,bean.getMessage());
                    return;
                }
                if (bean.getData()==null) return;

                if (CommonUtil.isNull(bean.getData().getPushAddress())) {

                }else {
                    selectCity.setText(bean.getData().getPushAddress());
                }
                if (CommonUtil.isNull(bean.getData().getAttentionIndustry())) {

                }else {
                    selectIndustry.setText(bean.getData().getAttentionIndustry());
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(MessageSettingActivity.this,errMsg);
            }
        });
    }
    private void initView() {
        title.setText("精准推送设置");
        back.setOnClickListener(v -> finish());
        recommendedUserSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    recommendedUserSwitch.setChecked(false);
                }else {
                    recommendedUserSwitch.setChecked(true);
                }
            }
        });
        recommendedProductSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    recommendedProductSwitch.setChecked(false);
                }else {
                    recommendedProductSwitch.setChecked(true);
                }
            }
        });
    }

    @OnClick({R.id.select_city, R.id.select_industry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_city:
                startActivityForResult(new Intent(this, ProvinceActivity.class),2);
                break;
            case R.id.select_industry:
                startActivityForResult(new Intent(this, NewChooseIndustryActivity.class),1);
                break;
        }
    }
     String selectIndustryTv="";
     String industryId="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                List<SelectBean> selectImages = (List<SelectBean>) data.getSerializableExtra("selectData");
                for (int i=0;i<selectImages.size();i++){
                    selectIndustryTv+=selectImages.get(i).getName()+"、";
                    industryId+=selectImages.get(i).getSecondPosition()+",";
                }
                if (!CommonUtil.isNull(selectIndustryTv)) {
                    selectIndustry.setText(selectIndustryTv.substring(0,selectIndustryTv.length()-1));
                }
                commit();
            }else if (requestCode == 2){
                selectCity.setText(data.getStringExtra("city"));
            }
        }
    }

    private void commit() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        if (!CommonUtil.isNull(industryId)){
            params.put("industryId", industryId.substring(0,industryId.length()-1));
        }else {
            params.put("industryId", industryId);
        }
        params.put("pushAddress", selectCity.getText().toString());
        params.put("userFlag", recommendedUserSwitch.isChecked());
        params.put("goodsFlag", recommendedProductSwitch.isChecked());
        apiImp.meessageSetting(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                MessageSettingBean bean=new Gson().fromJson(data,MessageSettingBean.class);
                ViewInject.shortToast(MessageSettingActivity.this,bean.getMessage());
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(MessageSettingActivity.this,errMsg);
            }
        });
    }
}
