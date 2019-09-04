package com.yuejian.meet.framents.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.yuejian.meet.activities.mine.BuyAllVipPermissionActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.SingleVipDetailEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.PayResult;
import com.yuejian.meet.utils.TextUtil;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.WxPayOrderInfo;
import com.yuejian.meet.widgets.PaymentBottomDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * @author : g000gle
 * @time : 2019/5/18 16:20
 * @desc :
 */
public class JoinSingleVipDetailFragment extends BaseFragment {

    @Bind(R.id.iv_vip_permission_detail_icon)
    ImageView mVipDetailIconView;
    @Bind(R.id.tv_vip_permission_detail_title)
    TextView mVipDetailTitleView;
    @Bind(R.id.tv_vip_permission_detail_time)
    TextView mVipDetailTimeView;
    @Bind(R.id.tv_vip_permission_detail_content)
    TextView mVipDetailContentView;
    @Bind(R.id.tv_vip_permission_detail_once_price)
    TextView mVipDetailOncePriceView;
    @Bind(R.id.tv_vip_permission_detail_once_desc)
    TextView mVipDetailOnceDescView;
    @Bind(R.id.tv_vip_permission_detail_once_buy)
    TextView mVipDetailOnceBuyBtn;
    @Bind(R.id.tv_vip_permission_detail_month_price)
    TextView mVipDetailMonthPriceView;
    @Bind(R.id.tv_vip_permission_detail_month_desc)
    TextView mVipDetailMonthDescView;
    @Bind(R.id.tv_vip_permission_detail_month_buy)
    TextView mVipDetailMonthBuyBtn;
    @Bind(R.id.tv_vip_permission_detail_year_price)
    TextView mVipDetailYearPriceView;
    @Bind(R.id.tv_vip_permission_detail_year_desc)
    TextView mVipDetailYearDescView;
    @Bind(R.id.tv_vip_permission_detail_year_buy)
    TextView mVipDetailYearBuyBtn;
    @Bind(R.id.tv_pay_all_accpect)
    View accpect;
    private IWXAPI iwxapi;

    SingleVipDetailEntity mVipDetailEntity;

    private String pro_Name;

    public static JoinSingleVipDetailFragment newInstance(SingleVipDetailEntity entity) {
        Bundle args = new Bundle();
        args.putSerializable("entity", entity);
        JoinSingleVipDetailFragment fragment = new JoinSingleVipDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args == null) {
            Toast.makeText(getActivity(), "Arguments is NULL", Toast.LENGTH_LONG).show();
        } else {
            mVipDetailEntity = (SingleVipDetailEntity) args.getSerializable("entity");
        }




        initWxPayApi();
    }

    private void toAccpect() {
        Intent webActivity = new Intent(getContext(), WebActivity.class);
        webActivity.putExtra("url", UrlConstant.VIPACCEPCT());
        webActivity.putExtra("No_Title", true);
        startActivity(webActivity);
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_join_single_vip_detail, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
        refreshUI();
    }

    public void refreshData(SingleVipDetailEntity entity) {
        mVipDetailEntity = entity;
        refreshUI();
    }

    private void refreshUI() {
        accpect.setOnClickListener(view -> {
            toAccpect();
        });
        mVipDetailTitleView.setText(mVipDetailEntity.getVip_name());
        mVipDetailIconView.setImageDrawable(mContext.getResources().getDrawable(getLocalResourceID(mVipDetailEntity.getEnglish_name())));

        if (mVipDetailEntity.getRemaining_days() > 0) {
            mVipDetailTimeView.setText(String.format("剩余%s天", mVipDetailEntity.getRemaining_days()));
            mVipDetailOnceBuyBtn.setText("续费");
            mVipDetailMonthBuyBtn.setText("续费");
            mVipDetailYearBuyBtn.setText("续费");
        } else {
            mVipDetailTimeView.setText("未开通");
            mVipDetailOnceBuyBtn.setText("开通");
            mVipDetailMonthBuyBtn.setText("开通");
            mVipDetailYearBuyBtn.setText("开通");
        }
        mVipDetailContentView.setText(mVipDetailEntity.getVip_introduce());

        pro_Name = mVipDetailEntity.getVip_name();

        mVipDetailOncePriceView.setText(String.format("￥%s", mVipDetailEntity.getSingle_price()));
        mVipDetailOnceDescView.setText(String.format("1次%s会员", pro_Name.substring(2, pro_Name.length())));
        mVipDetailOnceBuyBtn.setOnClickListener(v -> showPaymentDialog(4, mVipDetailEntity.getId()));

        mVipDetailMonthPriceView.setText(String.format("￥%s", mVipDetailEntity.getMonthly_price()));
        mVipDetailMonthDescView.setText(String.format("一个月%s会员", pro_Name.substring(2, pro_Name.length())));
        mVipDetailMonthBuyBtn.setOnClickListener(v -> showPaymentDialog(3, mVipDetailEntity.getId()));

        mVipDetailYearPriceView.setText(String.format("￥%s", mVipDetailEntity.getYearly_price()));
        mVipDetailYearDescView.setText(String.format("一年%s会员", pro_Name.substring(2, pro_Name.length())));
        mVipDetailYearBuyBtn.setOnClickListener(v -> showPaymentDialog(2, mVipDetailEntity.getId()));

    }

    private int getLocalResourceID(String name) {
        switch (name) {
            case "faqi":
                return R.mipmap.icon_kt_faqi;
            case "fabu":
                return R.mipmap.icon_kt_fabu;
            case "youpin":
                return R.mipmap.icon_kt_youpingou;
            case "peizhi":
                return R.mipmap.icon_kt_peizhi;
            case "ziyuan":
                return R.mipmap.icon_kt_resource;
            case "xiangmu":
                return R.mipmap.icon_kt_xiangmu;
            case "yizhuanbao":
                return R.mipmap.icon_kt_yzb;
            case "poster":
                return R.mipmap.icon_kt_poster;
            case "ship":
                return R.mipmap.icon_kt_vid;
            case "xiangc":
                return R.mipmap.icon_kt_xiangc;
            default:
                return R.mipmap.icon_kt_poster;
        }
    }

    private void initWxPayApi() {
        iwxapi = WXAPIFactory.createWXAPI(getActivity(), Constants.WX_APP_ID);
    }

    /**
     * @param type        2: 单项年费VIP  3: 单项包月  4: 单项单次
     * @param introduceId
     */
    private void showPaymentDialog(int type, long introduceId) {
        View.OnClickListener zhifubaoPay = v -> doInCash(1, type, introduceId);
        View.OnClickListener wechatPay = v -> doInCash(2, type, introduceId);
        PaymentBottomDialog dialog = new PaymentBottomDialog(getActivity(), zhifubaoPay, wechatPay);
        dialog.show();
    }

    /**
     * 充值
     *
     * @param sourceType  1：支付宝  2:微信
     * @param introduceId 单项VIP购买类型
     */
    private void doInCash(final int sourceType, final int vipType, final long introduceId) {
        Map<String, Object> params = new HashMap<>();
        //约见ID
        params.put("customer_id", user.customer_id);
        //充值平台
        params.put("source_type", String.valueOf(sourceType));
        //VIP类型
        params.put("vip_type", String.valueOf(vipType));
        //类型ID
        params.put("introduce_id", String.valueOf(introduceId));
        //判断是否安装微信
        if (sourceType == 2) {
            if (!Utils.isWeixinAvilible(getActivity().getApplicationContext())) {
                Toast.makeText(getActivity(), R.string.casht_text7, Toast.LENGTH_SHORT).show();
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
                        PayTask task = new PayTask(getActivity());
                        Map<String, String> result = task.payV2(orderInfo, true);
                        getActivity().runOnUiThread(() -> {
                            PayResult payResult = new PayResult(result);
                            String resultStatus = payResult.getResultStatus();
                            if (TextUtils.equals(resultStatus, "9000")) {
                                Toast.makeText(getActivity(), R.string.payment_success, Toast.LENGTH_SHORT).show();
                                // TODO refreshUIData();
                                ((Activity) mContext).finish();
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
                LogUtil.e("BuySingleVip", "errCode = " + errCode + ", errMsg = " + errMsg);
            }
        });
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType() == BusEnum.payment_success) {

            ((Activity) getContext()).finish();
        }
    }


}
