package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.LightUpSingleVipPermissionListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.SingleVipDetailEntity;
import com.yuejian.meet.common.GlobalVipInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/05/14 15:00
 * @desc : 我的 - 点亮VIP
 */
public class LightUpActivity extends BaseActivity {

    @Bind(R.id.iv_light_up_vip_back_btn)
    ImageView mLightUpVipBackBtn;
    @Bind(R.id.rv_single_vip_permission_list)
    RecyclerView mSingleVipPermissionListView;
    @Bind(R.id.cl_pay_all_vip_btn)
    ConstraintLayout mPayAllVipBtn;
    @Bind(R.id.tv_buy_all_vip_time)
    TextView mPayAllVipTimeView;
    @Bind(R.id.tv_pay_all_vip_price)
    TextView mPayAllVipPriceView;
    @Bind(R.id.tv_pay_all_vip_price_not)
    TextView mPayAllVipPriceNotView;
    @Bind(R.id.tv_pay_all_accpect)
    TextView tvAccpect;
    private LightUpSingleVipPermissionListAdapter mAdapter;

    private GlobalVipInfo mGlobalVipInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_up);

        mGlobalVipInfo = GlobalVipInfo.getInstance(getApplicationContext());

        initView();
    }

    private void initView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mAdapter = new LightUpSingleVipPermissionListAdapter(this);
        mSingleVipPermissionListView.setLayoutManager(layoutManager);
        mSingleVipPermissionListView.setAdapter(mAdapter);

        //全VIP权限剩余天数
        mPayAllVipTimeView.setText(String.format("剩余%s天", mGlobalVipInfo.getPayAllVipId()));
        mPayAllVipTimeView.setVisibility(mGlobalVipInfo.getPayAllVipId() > 0 ? View.VISIBLE : View.INVISIBLE);
        //全VIP权限优惠价格
        mPayAllVipPriceView.setText(String.format("%s ￥%s/年", mGlobalVipInfo.getPayAllVipId() > 0 ? "续费" : "开通", mGlobalVipInfo.getPayAllVipPrice()));
        //全VIP权限价格设置中划线
        mPayAllVipPriceNotView.setText(String.format("￥%s", mGlobalVipInfo.getPayAllVipPriceNot()));
        mPayAllVipPriceNotView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateData();
    }

    private void updateData() {
        Map<String, Object> params = new HashMap<>();
        //约见ID
        params.put("customer_id", user.customer_id);
        apiImp.findIntroduceVo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(data);
                    org.json.JSONArray entityArray = jsonObject.getJSONArray("single_vip");
                    mGlobalVipInfo.setSingleVipDetailEntities(JSON.parseArray(entityArray.toString(), SingleVipDetailEntity.class));

                    org.json.JSONObject yearObject = jsonObject.getJSONObject("year_vip");
                    mGlobalVipInfo.setPayAllVipId(yearObject.getInt("id"));
                    //全VIP权限剩余天数
                    mGlobalVipInfo.setPayAllVipTime(yearObject.getInt("remaining_days"));
                    mPayAllVipTimeView.setText(String.format("剩余%s天", mGlobalVipInfo.getPayAllVipTime()));
                    mPayAllVipTimeView.setVisibility(mGlobalVipInfo.getPayAllVipTime() > 0 ? View.VISIBLE : View.INVISIBLE);
                    //全VIP权限优惠价格
                    mGlobalVipInfo.setPayAllVipPrice(yearObject.getInt("year_money"));
                    mPayAllVipPriceView.setText(String.format("%s ￥%s/年", mGlobalVipInfo.getPayAllVipTime() > 0 ? "续费" : "开通", mGlobalVipInfo.getPayAllVipPrice()));
                    //全VIP权限价格
                    mGlobalVipInfo.setPayAllVipPriceNot(yearObject.getInt("yearly_price"));
                    mPayAllVipPriceNotView.setText(String.format("￥%s", mGlobalVipInfo.getPayAllVipPriceNot()));

                    mAdapter.updateListData(mGlobalVipInfo.getSingleVipDetailEntities());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @OnClick({R.id.cl_pay_all_vip_btn, R.id.iv_light_up_vip_back_btn, R.id.tv_pay_all_accpect})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_pay_all_vip_btn:
                Intent toPayAllIntent = new Intent(this, BuyAllVipPermissionActivity.class);
                startActivity(toPayAllIntent);
                break;

            case R.id.iv_light_up_vip_back_btn:
                finish();
                break;

            case R.id.tv_pay_all_accpect:
                Intent webActivity = new Intent(this, WebActivity.class);
                webActivity.putExtra("url", UrlConstant.VIPACCEPCT());
                webActivity.putExtra("No_Title", true);
                startActivity(webActivity);
                break;
        }
    }
}
