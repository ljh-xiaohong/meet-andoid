package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SelectMemberCityActivity;
import com.yuejian.meet.activities.home.ClaimSucceedActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/10/12/012.
 */

public class SelectGoodsActivity extends BaseActivity {
    @Bind(R.id.phone_rbt)
    RadioButton phoneRbt;
    @Bind(R.id.suo_rbt)
    RadioButton suoRbt;
    @Bind(R.id.phone)
    EditText phoneEdt;
    @Bind(R.id.address1)
    TextView address1;
    @Bind(R.id.address2)
    EditText address2;
    @Bind(R.id.tuijian_customer_id)
    EditText tuijianCustomerId;
    @Bind(R.id.person)
    EditText personEdt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_goods);
        setTitleText("填写信息");
        findViewById(R.id.titlebar_imgBtn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showNoTitleDialog(SelectGoodsActivity.this, getResources().getString(R.string.tips_select_goods), "继续填写", "退出", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        });
    }

    public void applyFamilyMaster(String p, String phone, String referrerId, String address1, String address2, String person) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("select_package", p);
        params.put("contact_phone", phone);
        params.put("referrer_id", referrerId);
        params.put("address1", address1);//收货地址
        params.put("address2", address2);//详细地址
        params.put("person", person);//收货人
        apiImp.orderFamilyMasterGoods(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Intent intent = new Intent(getApplication(), ClaimSucceedActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

    }

    @OnClick({R.id.submit, R.id.select_address, R.id.phone_check, R.id.suo_check, R.id.b_phone, R.id.b_suo})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                String p = phoneRbt.isChecked() ? phoneRbt.getText().toString() : suoRbt.getText().toString();
                String phone = phoneEdt.getText().toString();
                if (StringUtils.isEmpty(phone)) {
                    Toast.makeText(mContext, "请输入联系电话", Toast.LENGTH_SHORT).show();
                    return;
                }

                String a1 = address1.getText().toString();
                if (StringUtils.isEmpty(a1)) {
                    Toast.makeText(mContext, "请选择收货地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                String a2 = address2.getText().toString();
                if (StringUtils.isEmpty(a2)) {
                    Toast.makeText(mContext, "请填写详细地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                String person = personEdt.getText().toString();
                if (StringUtils.isEmpty(person)) {
                    Toast.makeText(mContext, "请填写收货人", Toast.LENGTH_SHORT).show();
                    return;
                }

                String referrerId = tuijianCustomerId.getText().toString();
                applyFamilyMaster(p, phone, referrerId, a1, a2, person);
                break;

            case R.id.select_address:
                Intent intent = new Intent(this, SelectMemberCityActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_check:
                phoneRbt.setChecked(true);
                suoRbt.setChecked(false);
                break;
            case R.id.suo_check:
                phoneRbt.setChecked(false);
                suoRbt.setChecked(true);
                break;
            case R.id.b_phone:
                ArrayList<Integer> resources = new ArrayList<>();
                resources.add(R.mipmap.b_phone);
                resources.add(R.mipmap.b_suo);
                Utils.displayImagesResources(this, resources, 0, null);
                break;
            case R.id.b_suo:
                resources = new ArrayList<>();
                resources.add(R.mipmap.b_phone);
                resources.add(R.mipmap.b_suo);
                Utils.displayImagesResources(this, resources, 1, null);
                break;
        }
    }

    String address = "";

    @Override
    public void onSomeEvent(BusCallEntity event) {
        super.onSomeEvent(event);
        if (event == null) return;

        if (event.getCallType() == BusEnum.PROVINCE) {
            address = event.data;
        } else if (event.getCallType() == BusEnum.CITY) {
            address += event.data;
        } else if (event.getCallType() == BusEnum.district) {
            if (StringUtils.isEmpty(address)) {
                if (event.data.equals("全部")) {
                    Toast.makeText(mContext, "请选择正确的省份城市", Toast.LENGTH_SHORT).show();
                    address = "";
                } else {
                    address = event.data;
                }
            }

            address1.setText(address);
            address = "";

            if (AppConfig.city.equals(event.data)) {
                address1.setText(AppConfig.province + AppConfig.city);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (Utils.isAnyWindowsIsShowing()) {
            Utils.dismissAnyWindows();
        } else {
            Utils.showNoTitleDialog(this, getResources().getString(R.string.tips_select_goods), "继续填写", "退出", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
