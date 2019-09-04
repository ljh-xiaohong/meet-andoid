package com.yuejian.meet.activities.ui;

import android.app.Activity;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.netease.nim.uikit.app.AppConfig;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yuejian.meet.activities.clan.ClanInfoActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.utils.PayResult;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.WxPayOrderInfo;

import java.util.HashMap;
import java.util.Map;

public class ClanPayUi
{
    private static final String APP_ID = "wx457b71ba948f85c0";
    private static final String PARTNER_ID = "1486212932";
    ApiImp apiImp = new ApiImp();
    public Activity context;
    private Handler handler = new Handler(new Callback()
    {
        public boolean handleMessage(Message paramAnonymousMessage)
        {
            if (paramAnonymousMessage.what == 1)
            {
                if (TextUtils.equals(new PayResult((Map)paramAnonymousMessage.obj).getResultStatus(), "9000")) {
                    ((ClanInfoActivity)ClanPayUi.this.context).payGold();
                }
            }
            else {
                return false;
            }
            Toast.makeText(ClanPayUi.this.context, "支付失败", Toast.LENGTH_SHORT).show();
            return false;
        }
    });
    private IWXAPI iwxapi;

    public ClanPayUi(Activity paramActivity)
    {
        this.context = paramActivity;
        initWxPayApi();
    }

    private void initWxPayApi()
    {
        this.iwxapi = WXAPIFactory.createWXAPI(this.context, "wx457b71ba948f85c0");
    }

    public void doInCash(final int paramInt1, int paramInt2,String mini)
    {
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        if (paramInt1 > 2)
        {
            Toast.makeText(this.context, "暂不支付银联方式支付", Toast.LENGTH_SHORT).show();
            return;
        }
        if (paramInt1 < 1)
        {
            Toast.makeText(this.context, "请选择充值方式", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isEmpty(mini)){
            localHashMap.put("mini",mini);
        }
        localHashMap.put("source_type", String.valueOf(paramInt1));
        localHashMap.put("cny", String.valueOf(paramInt2));
        if ((paramInt1 == 2) && (!Utils.isWeixinAvilible(this.context)))
        {
            Toast.makeText(this.context, "请先安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        this.apiImp.doInCash(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(final String paramAnonymousString, int paramAnonymousInt)
            {
                if (paramInt1 == 1) {
                    new Thread(new Runnable()
                    {
                        public void run()
                        {
                            Map localMap = new PayTask(ClanPayUi.this.context).payV2(paramAnonymousString, true);
                            Message localMessage = new Message();
                            localMessage.what = 1;
                            localMessage.obj = localMap;
                            ClanPayUi.this.handler.sendMessage(localMessage);
                        }
                    }).start();
                }
                while (paramInt1 != 2) {
                    return;
                }
                Log.d("wxPay", paramAnonymousString);
                WxPayOrderInfo wxPayOrderInfo = (WxPayOrderInfo) JSON.parseObject(paramAnonymousString, WxPayOrderInfo.class);
                PayReq localPayReq = new PayReq();
                localPayReq.appId = "wx457b71ba948f85c0";
                localPayReq.partnerId = "1486212932";
                localPayReq.prepayId = wxPayOrderInfo.prepay_id;
                localPayReq.packageValue = "Sign=WXPay";
                localPayReq.nonceStr = wxPayOrderInfo.nonceStr;
                localPayReq.timeStamp = wxPayOrderInfo.timeStamp;
                localPayReq.sign = wxPayOrderInfo.paySign;
                ClanPayUi.this.iwxapi.sendReq(localPayReq);
            }
        });
    }
}
