package com.yuejian.meet.activities.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.MyInheritorEarningEntity;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/6/18 17:09
 * @desc : 钱包 - VIP收益
 */
public class VipEarningsActivity extends BaseActivity {

    @Bind(R.id.iv_vip_earning_back_btn)
    ImageView mBackBtn;
    @Bind(R.id.vip_earning_user_header_img)
    CircleImageView mHeaderView;
    @Bind(R.id.tv_vip_earning_name)
    TextView mUserNameView;
    @Bind(R.id.tv_vip_earning_id)
    TextView mIdView;
    @Bind(R.id.tv_vip_earning_total_inheritor_amt)
    TextView mInheritorAmtView;
    @Bind(R.id.tv_vip_earning_inheritor_bal)
    TextView mInheritorBalView;
    @Bind(R.id.cl_vip_earnings_inheritor_list)
    ConstraintLayout mInheritorListBtn;
    @Bind(R.id.tv_vip_earning_tuijian_num)
    TextView mTuijianNumView;
    @Bind(R.id.tv_vip_earning_zhijie_num)
    TextView mZhijieNumView;
    @Bind(R.id.tv_vip_earning_jianjie_num)
    TextView mJianjieNumView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_earnings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            getVipEraningsData(user.customer_id);
        }
    }

    @OnClick({R.id.iv_vip_earning_back_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_vip_earning_back_btn:
                finish();
                break;
        }
    }

    private void getVipEraningsData(String customerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        apiImp.getMyInheritorEarnings(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                MyInheritorEarningEntity earningEntity = JSON.parseObject(data, MyInheritorEarningEntity.class);
                Glide.with(VipEarningsActivity.this).load(user.photo).into(mHeaderView);
                mUserNameView.setText(user.name);
                mIdView.setText(String.format("约见号：%s", user.customer_id));
                mInheritorAmtView.setText(String.format("￥%s", earningEntity.total_inheritor_amt));
                mInheritorBalView.setText(String.format("￥%s", earningEntity.inheritor_bal));
                mTuijianNumView.setText(String.format("%s人", 0)); // TODO

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
