package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.activities.web.WebWxPayActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.PayResult;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.utils.WxPayOrderInfo;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author :
 * @time   : 2018/11/12 11:32
 * @desc   : 购买金币 支付界面
 * @version: V1.0
 * @update : 2018/11/12 11:32
 */

public class InCashActivity extends BaseActivity {
    private IWXAPI iwxapi;
    private final static String APP_ID = Constants.WX_APP_ID;
    private final static String PARTNER_ID = Constants.WX_PARTNER_ID;

    private void initWxPayApi() {
        iwxapi = WXAPIFactory.createWXAPI(this, APP_ID);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                String resultStatus = payResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    Toast.makeText(InCashActivity.this, R.string.payment_success, Toast.LENGTH_SHORT).show();
                    if ("1".equals(isUpVip)) {
                        startActivity(new Intent(getApplicationContext(), SelectGoodsActivity.class));
                    }
                    finish();
                }
            } else if (msg.what == 2) {
                initCnyButtons();
            }
            return false;
        }
    });

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 支付成功之后 重新查余额
     */
    public void getBal() {
        new NetApi().getBal(AppConfig.CustomerId, this, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                MonyEntity monyEntity = JSON.parseObject(data, MonyEntity.class);
                if (monyEntity == null) {
                    ViewInject.shortToast(getApplication(), "更新钱包失败");
                    return;
                }
                UtilsIm.setMyMoney(InCashActivity.this, monyEntity);
                Bus.getDefault().post(AppConfig.updateMoney);///发通知给视频重新算视频时长
            }

            @Override
            public void onFailed(String errCode, String errMsg) {

            }
        });
    }

    @Bind(R.id.alipay_way_layout)
    LinearLayout alipayWayLy;
    @Bind(R.id.wxpay_way_layout)
    LinearLayout wxPayWayLy;
    @Bind(R.id.unionpay_way_layout)
    LinearLayout unionPayWayLy;
    @Bind(R.id.incash_tag_grid)
    GridLayout inCashGrid;
    @Bind(R.id.in_cash_btn)
    Button inCashBtn;
    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton back;
    @Bind(R.id.txt_titlebar_title)
    TextView title;

    private int sourceType = 0;
    private int cny;

    private String isUpVip = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.ONLINE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_cash);
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.text_mine_recharge_lable);
        Intent intent = getIntent();
        initInCashView(intent);
        initWxPayApi();
        Bus.getDefault().register(this);
    }

    private void initInCashView(Intent intent) {

        if (intent.hasExtra("isUpVip")) {
            isUpVip = intent.getStringExtra("isUpVip");
            cny = 10000;
            wxPayWayLy.setEnabled(false);
            inCashBtn.setText(getString(R.string.casht_text5) + cny);
            inCashGrid.setVisibility(View.INVISIBLE);
        }
        alipayWayLy.setSelected(true);
        wxPayWayLy.setSelected(false);
        unionPayWayLy.setSelected(false);
        sourceType = 1;
        handler.sendEmptyMessage(2);
    }

    private int[] accounts = {1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000};

    private void initCnyButtons() {
        if (inCashGrid == null) {
            inCashGrid = (GridLayout) findViewById(R.id.incash_tag_grid);
        }
        for (int i = 0; i < accounts.length; i++) {
            Button child = (Button) inCashGrid.getChildAt(i);
            child.setText(accounts[i] + getString(R.string.text_mine_money_lable));
            child.setTag(accounts[i]);
        }
    }


    @OnClick({R.id.alipay_way_layout, R.id.wxpay_way_layout, R.id.unionpay_way_layout, R.id.in_cash_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alipay_way_layout:
                alipayWayLy.setSelected(true);
                wxPayWayLy.setSelected(false);
                unionPayWayLy.setSelected(false);
                sourceType = 1;
                break;
            case R.id.wxpay_way_layout:
                alipayWayLy.setSelected(false);
                wxPayWayLy.setSelected(true);
                unionPayWayLy.setSelected(false);
                sourceType = 2;
                break;
            case R.id.unionpay_way_layout:
                alipayWayLy.setSelected(false);
                wxPayWayLy.setSelected(false);
                unionPayWayLy.setSelected(true);
                sourceType = 4;
                break;
            case R.id.in_cash_rule:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.ExplainURL.INCASH);
                startActivity(intent);
                break;
        }
    }

    public void onTagClick(View view) {
        cny = (int) view.getTag() / 100;
        view.setSelected(true);
        ((Button) view).setTextColor(getResources().getColor(R.color.in_cash_account_bg_sel));
        for (int i = 0; i < inCashGrid.getChildCount(); i++) {
            Button child = (Button) inCashGrid.getChildAt(i);
            if (view != child) {
                child.setSelected(false);
                child.setTextColor(getResources().getColor(R.color.in_cash_account_bg_nor));
            }
        }

        inCashBtn.setText(getString(R.string.casht_text5) + cny);
    }

    public void inCash(View view) {
        doInCash(sourceType, cny);
    }

    //sourceType 1:支付宝，2.微信，3.银联方式
    private void doInCash(final int sourceType, final int cny) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        if (sourceType > 2) {
            Toast.makeText(this, R.string.casht_text6, Toast.LENGTH_SHORT).show();
            return;
        } else if (sourceType < 1) {
            Toast.makeText(this, R.string.please_select_recharge_mode, Toast.LENGTH_SHORT).show();
            return;
        }

        params.put("source_type", String.valueOf(sourceType));
        params.put("cny", String.valueOf(cny));
        if (isUpVip.equals("1")) {
            if (StringUtil.isEmpty(AppConfig.family_id)) {
                params.put("area_name", AppConfig.AreaName);
            } else {
                params.put("family_id", AppConfig.family_id);
            }
        }
        if (sourceType == 2) {
            if (!Utils.isWeixinAvilible(getApplicationContext())) {
                Toast.makeText(this, R.string.casht_text7, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        apiImp.doInCash(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (sourceType == 1) {
                    final String orderInfo = data;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            PayTask task = new PayTask(InCashActivity.this);
                            Map<String, String> result = task.payV2(orderInfo, true);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = result;
                            handler.sendMessage(msg);
                        }
                    }).start();
                } else if (sourceType == 2) {
                    Log.d("wxPay", data);
                    final WxPayOrderInfo orderInfo = JSON.parseObject(data, WxPayOrderInfo.class);
                    PayReq request = new PayReq();
                    request.appId = APP_ID;
                    request.partnerId = PARTNER_ID;
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
            }
        });
    }

    private void doInCashH5Alipay(int cny) {
        if (user != null) {
            Intent intent = new Intent(this, WebActivity.class);
            String token = user.token;
            String tokenCustomerId = user.customer_id;
            String requestUrl = UrlConstant.POST_DO_IN_CASH_H5_ALIPAY +
                    "?" + "customer_id=" + user.customer_id +
                    "&" + "cny=" + String.valueOf(cny) +
                    "&" + "token=" + token + "&token_customer_id=" + tokenCustomerId;
            if ("1".equals(isUpVip)) {
                if (StringUtil.isEmpty(AppConfig.family_id)) {
                    requestUrl += "&" + "area_name=" + AppConfig.AreaName;
                } else {
                    requestUrl += "&" + "family_id=" + AppConfig.family_id;
                }

                intent.putExtra("isUpVip", true);
            }
            intent.putExtra(Constants.URL, requestUrl);
            startActivity(intent);
        }
    }


    private void doWxH5Pay(int cny) {
        if (user != null) {
            Intent intent = new Intent(this, WebWxPayActivity.class);
            String url = "http://app.michatting.com/michat-app/wx/pay/toH5Pay";
            String requestUrl = url + "?" + "customerId=" + user.customer_id + "&" + "cny=" + String.valueOf(cny);
            if ("1".equals(isUpVip)) {
                if (StringUtil.isEmpty(AppConfig.family_id)) {
                    requestUrl += "&" + "area_name=" + AppConfig.AreaName;
                } else {
                    requestUrl += "&" + "family_id=" + AppConfig.family_id;
                }
            }
            intent.putExtra(Constants.URL, requestUrl);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        Bus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("WXPAY_SUCCESS", false)) {
            finish();
        } else {
            initInCashView(intent);
        }
    }
}
