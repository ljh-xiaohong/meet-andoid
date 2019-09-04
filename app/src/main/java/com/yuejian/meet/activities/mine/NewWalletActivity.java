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
import butterknife.ButterKnife;

/**
 * 钱包
 */

/**
 * @author :
 * @time : 2018/11/21 15:22
 * @desc :  发现 我的 我的钱包
 * @version: V1.0
 * @update : 2018/11/21 15:22
 */

public class NewWalletActivity extends BaseActivity {

  @Bind(R.id.qianbao)
  LinearLayout mQianbao;

  @Bind(R.id.fuyuan)
  LinearLayout mFuyuan;
  @Bind(R.id.jinyuan)
  LinearLayout mJinyuan;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_wallet);
    ButterKnife.bind(this);
    initView();
    initListener();
  }

  public void initView() {
    setTitleText("钱包");
  }

  private void initListener() {
    mQianbao.setOnClickListener(this);
    mFuyuan.setOnClickListener(this);
    mJinyuan.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.qianbao:
        // 钱包
        Intent sintent = new Intent(this, WebActivity.class);
        sintent.putExtra("No_Title", false);
        sintent.putExtra("url", UrlConstant.ExplainURL.YIKE_QIANBAO + "?customerId=" + AppConfig.CustomerId);
        startActivity(sintent);

        break;
     /* case R.id.gongde:
        //功德
        Intent gintent = new Intent(this, WebActivity.class);
        gintent.putExtra("No_Title", false);
        gintent.putExtra("url", UrlConstant.ExplainURL.YIKE_GONDE + "?customerId=" + AppConfig.CustomerId);
        startActivity(gintent);

        break;*/
      case R.id.fuyuan:
        // 福缘
        Intent fintent = new Intent(this, WebActivity.class);
        fintent.putExtra("No_Title", false);
        fintent.putExtra("url", UrlConstant.ExplainURL.YIKE_FUYUAN + "?customerId=" + AppConfig.CustomerId);
        startActivity(fintent);
        break;
      case R.id.jinyuan:
        // 金元
        Intent jintent = new Intent(this, WebActivity.class);
        jintent.putExtra("No_Title", false);
        jintent.putExtra("url", UrlConstant.ExplainURL.YIKE_JINYUAN + "?customerId=" + AppConfig.CustomerId);
        startActivity(jintent);
        break;

    }
    super.onClick(v);
  }
}
