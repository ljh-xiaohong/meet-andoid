package com.yuejian.meet.activities.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.StoreWebActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.bean.MyWallet;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 钱包
 * Created by zh02 on 2017/8/18.
 * update 2019.06.18
 */

public class MyWalletActivity extends BaseActivity {

    @Bind(R.id.total_bal)
    TextView totalBal;
    @Bind(R.id.recharge_bal)
    TextView rechargeBal;
    @Bind(R.id.gains_bal)
    TextView gainsBal;
    @Bind(R.id.inheritor_bal)
    TextView inheritorBal;
    @Bind(R.id.coupon_unread_cnt)
    TextView couponUnReadCnt;
    @Bind(R.id.ll_my_wallet_vip_earnings)
    LinearLayout myWalletVipEarnings;
    @Bind(R.id.ll_my_wallet_my_inheritor_earnings)
    LinearLayout myWalletMyInheritorEarnings;

    private Mine mine = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        setTitleText("钱包");
        mine = (Mine) getIntent().getSerializableExtra("mine");
//        TextView gwjf = (TextView) findViewById(R.id.jf_tap);
//        String text = gwjf.getText().toString();
//        Spannable spannable = new SpannableString(text);
//        spannable.setSpan(
//                new ForegroundColorSpan(Color.parseColor("#19A1E9")),
//                text.indexOf("("), text.lastIndexOf(")") + 1,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        gwjf.setText(spannable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            findMyInfo(user.customer_id);
            getMyWallet(user.customer_id);
        }
        getCouponUnRead();
    }

    private MyWallet wallet = null;

    private void getMyWallet(String customerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        apiImp.getMyWallet(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                wallet = JSON.parseObject(data, MyWallet.class);
                if (wallet != null) {
                    totalBal.setText(String.format("￥%s", wallet.total_bal));
                    gainsBal.setText(String.format("￥%s", wallet.gains_bal));
                    rechargeBal.setText(wallet.recharge_bal);
                    inheritorBal.setText(String.format("￥%s",wallet.inheritor_bal));
                } else {
                    totalBal.setText(String.format("￥%s", 0));
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void findMyInfo(String customerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        new ApiImp().findMyInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Log.d("data", data);
                mine = JSON.parseObject(data, Mine.class);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick({R.id.zdmx, R.id.go_to_out_cash, R.id.in_cash, R.id.out_cash, R.id.helper, R.id.gwq,
            R.id.ll_my_wallet_vip_earnings, R.id.ll_my_wallet_my_inheritor_earnings})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zdmx:
                startActivity(new Intent(this, BillRecordActivity.class));
                break;
            case R.id.go_to_out_cash:
                if (!"1".equals(mine.is_mobile_certified)) {
                    Toast.makeText(mContext, R.string.my_wallet_toast, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("1".equals(mine.is_out_cash_pw)) {
                    Intent intent = new Intent(this, UpdateOutCashPwdActivity.class);
                    intent.putExtra("mine", mine);
                    startActivity(intent);
                } else if ("0".equals(mine.is_out_cash_pw)) {
                    Intent intent = new Intent(this, ModifyOutCashPWDActivity.class);
                    intent.putExtra("mine", mine);
                    startActivity(intent);
                    Toast.makeText(mContext, R.string.my_wallet_toast2, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.in_cash:
                startActivity(new Intent(this, InCashActivity.class));
                break;
            case R.id.out_cash:
                if (mine == null) {
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                    return;
                }
                if (wallet == null || 1000 > Double.valueOf(wallet.gains_bal).longValue()) {
                    Toast.makeText(mContext, R.string.my_wallet_toast3, Toast.LENGTH_SHORT).show();
                } else if ("0".equals(mine.is_out_cash_pw)) {
                    if (!"1".equals(mine.is_mobile_certified)) {
                        Toast.makeText(mContext, R.string.my_wallet_toast4, Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(getBaseContext(), R.string.my_wallet_toast5, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, ModifyOutCashPWDActivity.class);
                        intent.putExtra("mine", mine);
                        startActivity(intent);
                    }
                } else if (!"1".equals(mine.is_idcard_certified)) {
                    Toast.makeText(mContext, R.string.my_wallet_toast6, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, VerifyIdCardActivity.class));
                } else {
                    Intent intent = new Intent(this, OutCashActivity.class);
                    intent.putExtra("mine", mine);
                    startActivity(intent);
                }
                break;
            case R.id.helper:
                startActivity(new Intent(this, WalletHelperActivity.class));
                break;
            case R.id.gwq:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.COUPON_URL + "?customer_id=" + AppConfig.CustomerId);
                intent.putExtra(Constants.NO_TITLE_BAR, true);
                startActivity(intent);
                couponUnReadCnt.setVisibility(View.GONE);
                break;
//            case R.id.jf_tap:
//                intent = new Intent(this, StoreWebActivity.class);
//                intent.putExtra(Constants.URL, Constants.STORE_URL + "/store?storetype=1&storeid=2&userId=" + AppConfig.CustomerId + "&type=app");
//                startActivity(intent);
//                break;
            case R.id.ll_my_wallet_vip_earnings:  //VIP会员收益
                if (wallet.isYear) {
                    startActivity(new Intent(MyWalletActivity.this, VipEarningsActivity.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("您还不是年费VIP，是否前往开通？");
                    builder.setPositiveButton(R.string.ok, (dialog, id) -> startActivity(new Intent(MyWalletActivity.this, LightUpActivity.class)));
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.ll_my_wallet_my_inheritor_earnings:  //传承人收益
                Intent gotoInheritorActy = new Intent(MyWalletActivity.this, InheritorEarningsActivity.class);
                gotoInheritorActy.putExtra("stats", wallet.inheritorFlag);
                startActivity(gotoInheritorActy);
                break;
        }
    }

    private void getCouponUnRead() {
        if (StringUtils.isEmpty(AppConfig.CustomerId)) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        apiImp.getCouponUnReadCnt(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                int cnt = Integer.valueOf(data);
                if (cnt > 0) {
                    couponUnReadCnt.setVisibility(View.VISIBLE);
                    couponUnReadCnt.setText(data);
                } else {
                    couponUnReadCnt.setText("");
                    couponUnReadCnt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
