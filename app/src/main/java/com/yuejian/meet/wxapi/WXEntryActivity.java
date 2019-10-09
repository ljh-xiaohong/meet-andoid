package com.yuejian.meet.wxapi;


import android.widget.Toast;

import com.mcxiaoke.bus.Bus;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.umeng.socialize.weixin.view.WXCallbackActivity;
import com.yuejian.meet.utils.ViewInject;


public class WXEntryActivity extends WXCallbackActivity {

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {

            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extraData = launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性

        } else if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0:
                    ViewInject.shortToast(this, "支付成功！");
                    break;
                case -2:
                    ViewInject.shortToast(this, "支付取消！");
                    break;
                case -1:
                    ViewInject.shortToast(this, "支付失败！");
                    break;
                default:
                    ViewInject.shortToast(this, "支付出错！");
                    break;
            }
        } else {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Bus.getDefault().post("wx_share_success");
            }
            super.onResp(resp);//一定要加super，实现我们的方法，否则不能回调
        }
    }
}
