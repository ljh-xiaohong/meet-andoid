package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.AllVipPermissionShowingListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.SingleVipDetailEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.common.GlobalVipInfo;
import com.yuejian.meet.ui.SingleLineItemDecoration;
import com.yuejian.meet.utils.PayResult;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.WxPayOrderInfo;
import com.yuejian.meet.widgets.PaymentBottomDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/05/18 10:08
 * @desc : 我的 - 点亮VIP - 开通全部会员特权
 */
public class BuyAllVipPermissionActivity extends BaseActivity {

    @Bind(R.id.rv_show_all_permission)
    RecyclerView mShowAllPermissionListView;
    @Bind(R.id.cl_buy_all_vip_permission)
    ConstraintLayout mBuyBtn;
    @Bind(R.id.iv_buy_all_vip_back_btn)
    ImageView mBuyAllVipBackBtn;
    @Bind(R.id.tv_buy_all_vip_time)
    TextView mBuyAllVipTimeView;
    @Bind(R.id.tv_buy_all_vip_price)
    TextView mBuyAllVipPriceView;
    @Bind(R.id.tv_buy_all_vip_price_not)
    TextView mBuyAllVipPriceNotView;
    @Bind(R.id.tv_buy_all_vip_price_sum)
    TextView mBuyAllVipPriceSum;

    private IWXAPI iwxapi;
    private GlobalVipInfo mGlobalVipInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_all_vip_permission);

        mGlobalVipInfo = GlobalVipInfo.getInstance(getApplicationContext());

        initView();
        initWxPayApi();
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        AllVipPermissionShowingListAdapter adapter = new AllVipPermissionShowingListAdapter();
        mShowAllPermissionListView.setLayoutManager(layoutManager);
        mShowAllPermissionListView.setAdapter(adapter);
        mShowAllPermissionListView.addItemDecoration(new SingleLineItemDecoration(24));

        mBuyAllVipPriceSum.setText(String.format("合计:￥%s", mGlobalVipInfo.getPayAllVipPrice()));
        //全VIP权限剩余天数
        mBuyAllVipTimeView.setText(String.format("剩余%s天", mGlobalVipInfo.getPayAllVipTime()));
        mBuyAllVipTimeView.setVisibility(mGlobalVipInfo.getPayAllVipTime() > 0 ? View.VISIBLE : View.INVISIBLE);
        //全VIP权限优惠价格
        mBuyAllVipPriceView.setText(String.format("%s ￥%s/年", mGlobalVipInfo.getPayAllVipTime() > 0 ? "续费" : "开通", mGlobalVipInfo.getPayAllVipPrice()));
        //全VIP权限价格设置中划线
        mBuyAllVipPriceNotView.setText(String.format("￥%s", mGlobalVipInfo.getPayAllVipPriceNot()));
        mBuyAllVipPriceNotView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        List<Pair<String, String>> itemList = new ArrayList<>();
        for (int i = 0; i < mGlobalVipInfo.getSingleVipDetailEntities().size(); i++) {
            SingleVipDetailEntity entity = mGlobalVipInfo.getSingleVipDetailEntities().get(i);
            Pair<String, String> item = new Pair<>(entity.getVip_name(), entity.getVip_introduce());
            itemList.add(item);
        }
        adapter.updateData(itemList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateData();
    }

    @OnClick({R.id.cl_buy_all_vip_permission, R.id.iv_buy_all_vip_back_btn, R.id.tv_pay_all_accpect})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_buy_all_vip_permission:
                showPaymentDialog();
                break;
            case R.id.iv_buy_all_vip_back_btn:
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

    private void showPaymentDialog() {
        View.OnClickListener zhifubaoPay = v -> doInCash(1);
        View.OnClickListener wechatPay = v -> doInCash(2);
        PaymentBottomDialog dialog = new PaymentBottomDialog(this, zhifubaoPay, wechatPay);
        dialog.show();
    }

    private void initWxPayApi() {
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);

    }

    /**
     * 充值
     *
     * @param sourceType 1：支付宝  2:微信
     */
    private void doInCash(final int sourceType) {
        Map<String, Object> params = new HashMap<>();
        //约见ID
        params.put("customer_id", user.customer_id);
        //充值平台
        params.put("source_type", String.valueOf(sourceType));
        //VIP类型
        params.put("vip_type", "1");
        //类型ID
        params.put("introduce_id", String.valueOf(mGlobalVipInfo.getPayAllVipId()));
        //判断是否安装微信
        if (sourceType == 2) {
            if (!Utils.isWeixinAvilible(getApplicationContext())) {
                Toast.makeText(this, R.string.casht_text7, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //调用支付api
        apiImp.doInCashVip(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (sourceType == 1) {  //支付宝支付
                    final String orderInfo = data;
                    new Thread(() -> {
                        PayTask task = new PayTask(BuyAllVipPermissionActivity.this);
                        Map<String, String> result = task.payV2(orderInfo, true);
                        runOnUiThread(() -> {
                            PayResult payResult = new PayResult(result);
                            String resultStatus = payResult.getResultStatus();
                            if (TextUtils.equals(resultStatus, "9000")) {
                                Toast.makeText(BuyAllVipPermissionActivity.this, R.string.payment_success, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }).start();
                } else if (sourceType == 2) {  //微信支付
                    Log.d("wxPay", data);
                    final WxPayOrderInfo orderInfo = JSON.parseObject(data, WxPayOrderInfo.class);
                    PayReq request = new PayReq();
                    request.appId = Constants.WX_APP_ID;
                    request.partnerId = Constants.WX_PARTNER_ID;
                    request.prepayId = orderInfo.prepay_id;
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = orderInfo.nonceStr;
                    request.timeStamp = orderInfo.timeStamp;
                    request.sign = orderInfo.paySign;
                    iwxapi.sendReq(request);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                LogUtil.e("BuyAllVip", "errCode = " + errCode + ", errMsg = " + errMsg);
            }
        });
    }


    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType() == BusEnum.payment_success) {

            finish();
        }
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
                    mBuyAllVipTimeView.setText(String.format("剩余%s天", mGlobalVipInfo.getPayAllVipTime()));
                    mBuyAllVipTimeView.setVisibility(mGlobalVipInfo.getPayAllVipTime() > 0 ? View.VISIBLE : View.INVISIBLE);
                    //全VIP权限优惠价格
                    mGlobalVipInfo.setPayAllVipPrice(yearObject.getInt("year_money"));
                    mBuyAllVipPriceView.setText(String.format("%s ￥%s/年", mGlobalVipInfo.getPayAllVipTime() > 0 ? "续费" : "开通", mGlobalVipInfo.getPayAllVipPrice()));
                    //全VIP权限价格
                    mGlobalVipInfo.setPayAllVipPriceNot(yearObject.getInt("yearly_price"));
                    mBuyAllVipPriceNotView.setText(String.format("￥%s", mGlobalVipInfo.getPayAllVipPriceNot()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }


}
