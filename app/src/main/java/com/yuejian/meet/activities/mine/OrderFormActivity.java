package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.http.UrlConstant;

import butterknife.Bind;



/**
 * @author :
 * @time : 2018/11/21 15:20
 * @desc :  发现 我的 我的订单
 * @version: V1.0
 * @update : 2018/11/21 15:20
 */

public class OrderFormActivity extends BaseActivity {

  @Bind(R.id.fukuan)
  LinearLayout mFukuan;
  @Bind(R.id.daifahuo)
  LinearLayout mDaifahuo;
  @Bind(R.id.daishouhuo)
  LinearLayout mDaishouhuo;
  @Bind(R.id.daipingjia)
  LinearLayout mDaipingjia;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_form);

    initData();
    initListener();
  }

  public void initData() {
    setTitleText("我的订单");
  }

  private void initListener() {
    mFukuan.setOnClickListener(this);
    mDaifahuo.setOnClickListener(this);
    mDaishouhuo.setOnClickListener(this);
    mDaipingjia.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fukuan:
        // 待付款
        Intent gintent = new Intent(this, WebActivity.class);
        gintent.putExtra("No_Title", false);
        gintent.putExtra("url", UrlConstant.ExplainURL.YIKE_SHOUHUO + "?customerId=" + AppConfig.CustomerId + "&tabFlag=" + 2);
        startActivity(gintent);
        break;
      case R.id.daifahuo:
        // 待发货
        Intent fintent = new Intent(this, WebActivity.class);
        fintent.putExtra("No_Title", false);
        fintent.putExtra("url", UrlConstant.ExplainURL.YIKE_SHOUHUO + "?customerId=" + AppConfig.CustomerId + "&tabFlag=" + 3);
        startActivity(fintent);
        break;
      case R.id.daishouhuo:
        // 待收货
        Intent sintent = new Intent(this, WebActivity.class);
        sintent.putExtra("No_Title", false);
        sintent.putExtra("url", UrlConstant.ExplainURL.YIKE_SHOUHUO + "?customerId=" + AppConfig.CustomerId + "&tabFlag=" + 4);
        startActivity(sintent);
        break;
      case R.id.daipingjia:
        // 待评价
        Intent pintent = new Intent(this, WebActivity.class);
        pintent.putExtra("No_Title", false);
        pintent.putExtra("url", UrlConstant.ExplainURL.YIKE_SHOUHUO + "?customerId=" + AppConfig.CustomerId + "&tabFlag=" + 5);
        startActivity(pintent);
        break;


    }
    super.onClick(v);
  }
}
