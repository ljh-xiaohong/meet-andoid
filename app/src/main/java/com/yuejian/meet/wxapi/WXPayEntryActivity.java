package com.yuejian.meet.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.applet.ExclusiveAppletActivity;
import com.yuejian.meet.activities.mine.InCashActivity;
import com.yuejian.meet.common.Constants;


/**
 * @author :
 * @time : 2018/11/12 14:33
 * @desc : 微信支付回调页
 * @version: V1.0
 * @update : 2018/11/12 14:33
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    private static final String APP_ID = Constants.WX_APP_ID;

    private TextView payResultTip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        payResultTip = (TextView) findViewById(R.id.pay_result_tip);
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                BusCallEntity busCallEntity = new BusCallEntity();
                busCallEntity.setCallType(BusEnum.payment_success);
                Bus.getDefault().post(busCallEntity);
//                2019.05.20屏蔽支付成功后跳转页面
//                if (ExclusiveAppletActivity.appletPal){
//                    BusCallEntity busCallEntity=new BusCallEntity();
//                    busCallEntity.setCallType(BusEnum.payment_success);
//                    Bus.getDefault().post(busCallEntity);
//                }else {
//                    Intent intent = new Intent(this, InCashActivity.class);
//                    intent.putExtra("WXPAY_SUCCESS", true);
//                    startActivity(intent);
//                }
            } else {
                if (resp.errCode == -2) {
                    Toast.makeText(this, "支付取消：用户取消支付", Toast.LENGTH_SHORT).show();

                } else if (resp.errCode == -1) {
                    Toast.makeText(this, "支付失败：错误码(" + resp.errCode + ")", Toast.LENGTH_SHORT).show();
                    Bus.getDefault().post(Constants.WX_PAY_RESULT_CODE_FAULT);
                }
            }
        }
        finish();
    }
}