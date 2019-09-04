package com.yuejian.meet.framents.find;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.mcxiaoke.bus.Bus;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.MyMeritsEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.PayResult;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.WxPayOrderInfo;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * @author :
 * @time : 2018/11/10 17:10
 * @desc : 充值功德
 * @version: V1.0
 * @update : 2018/11/10 17:10
 */

public class RechargeMeritsActivity extends BaseActivity {


  @Bind(R.id.recharge_merits_act_ib_back)
  ImageButton mForHomeActIbBack;
  /**
   * 总功德值
   */
  @Bind(R.id.for_home_act_tv_allmerits)
  TextView mTvAllmerits;

  @Bind(R.id.recharge_merits_act_rb_wushi_money)
  RadioButton mRbWushiMoney;
  @Bind(R.id.recharge_merits_act_rb_yibai_money)
  RadioButton mRbYibaiMoney;
  @Bind(R.id.recharge_merits_act_rb_wubai_money)
  RadioButton mRbWubaiMoney;
  @Bind(R.id.recharge_merits_act_rg_selected_money)
  RadioGroup mRgSelectedMoney;
  /**
   * 微信支付
   */
  @Bind(R.id.recharge_merits_act_rl_weixinpay)
  RelativeLayout mRlWeixinpay;
  /**
   * 阿里支付
   */
  @Bind(R.id.recharge_merits_act_rl_alipay)
  RelativeLayout mRlAlipay;

  /**
   * 支付金额
   */
  private double money;
  /**
   * 支付方式
   * 1 支付宝
   * 2.微信
   */
  private int payType = 0;


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
          Toast.makeText(RechargeMeritsActivity.this, R.string.payment_success, Toast.LENGTH_SHORT).show();
          initData();
        }

      } else if (msg.what == 2) {

      }
      return false;
    }
  });
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recharge_merits);
    initview();
    initData();
    initWxPayApi();
    initLiseener();


  }

  private void initview() {
    mRbWushiMoney.setChecked(true);
    money = 50;

  }

  private void initLiseener() {
    mForHomeActIbBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    mRgSelectedMoney.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
          case R.id.recharge_merits_act_rb_wushi_money:
            money = 0.1;
            break;

          case R.id.recharge_merits_act_rb_yibai_money:
            money = 0.1;
            break;

          case R.id.recharge_merits_act_rb_wubai_money:
            money = 0.1;
            break;

        }


      }
    });

    mRlAlipay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        payType = 1;
        doInCash(payType, money);
      }
    });

    mRlWeixinpay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        payType = 2;
        doInCash(payType, money);

      }
    });

  }

  private void initData() {
    Map<String,Object> params=new HashMap<>();
    params.put("customer_id",user.getCustomer_id());
    apiImp.getMeritsData(params, this, new DataIdCallback<String>() {
      @Override
      public void onSuccess(String data, int id) {
        MyMeritsEntity myMeritsEntity = JSON.parseObject(data, MyMeritsEntity.class);
        mTvAllmerits.setText(myMeritsEntity.getPractice_total() + "");


      }

      @Override
      public void onFailed(String errCode, String errMsg, int id) {

      }
    });


  }
  //sourceType 1:支付宝，2.微信
  private void doInCash(final int payType, final double money) {
    Map<String, Object> params = new HashMap<>();
    params.put("customer_id", user.customer_id);
    params.put("source_type", String.valueOf(payType));
    params.put("cny", String.valueOf(money));

    if (payType == 2) {
      if (!Utils.isWeixinAvilible(getApplicationContext())) {
        Toast.makeText(this, R.string.casht_text7, Toast.LENGTH_SHORT).show();
        return;
      }
    }

    apiImp.doInCash(params, this, new DataIdCallback<String>() {
      @Override
      public void onSuccess(String data, int id) {
        if (payType == 1) {
          final String orderInfo = data;
          new Thread(new Runnable() {
            @Override
            public void run() {
              PayTask task = new PayTask(RechargeMeritsActivity.this);
              Map<String, String> result = task.payV2(orderInfo, true);
              Message msg = new Message();
              msg.what = 1;
              msg.obj = result;
              handler.sendMessage(msg);
            }
          }).start();
        } else if (payType == 2) {
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
  @Override
  protected void onDestroy() {
    Bus.getDefault().unregister(this);
    super.onDestroy();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (intent.getBooleanExtra("WXPAY_SUCCESS", false)) {
      // 支付成功
      finish();
      initData();
    } else {

    }
  }
}
