package com.yuejian.meet.framents.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.meritsurname.ForFreeActivity;
import com.yuejian.meet.activities.meritsurname.MeritRankingActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.MyMeritsEntity;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * @author :
 * @time : 2018/11/10 15:17
 * @desc : 发现 为家修行
 * @version: V1.0
 * @update : 2018/11/10 15:17
 */

public class ForHomePfracticeActivity extends BaseActivity {

  @Bind(R.id.for_home_act_ib_back)
  ImageButton mIbBack;
  /**
   * 功德数
   */
  @Bind(R.id.for_home_act_tv_allmerits)
  TextView mTvAllmerits;

  @Bind(R.id.for_home_act_tv_ranking_list)
  TextView mTvRankingList;

  @Bind(R.id.for_home_act_tv_release)
  TextView mTvRelease;

  @Bind(R.id.for_home_act_tv_recharge_merits)
  TextView mTvRechargeMerits;

  /**
   * 撞钟人数
   */
  @Bind(R.id.tv_bell_bash_people)
  TextView mTvBellBashPeople;

  @Bind(R.id.for_home_pfraactice_ll_bell_bash)
  RelativeLayout mLlBellBash;
  /**
   * 放生人数
   */
  @Bind(R.id.tv_release_people)
  TextView mTvReleasePeople;

  @Bind(R.id.for_home_pfraactice_ll_release)
  RelativeLayout mLlRelease;
  /**
   * 点灯人数
   */
  @Bind(R.id.tv_light_on_people)
  TextView mTvLightOnPeople;

  @Bind(R.id.for_home_pfraactice_ll_light_on)
  RelativeLayout mLlLightOn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_for_home_pfractice);
    initview();
    initData();
    initListener();

  }

  private void initview() {


  }

  @Override
  protected void onRestart() {
    initData();

    super.onRestart();
  }

  @Override
  protected void onPause() {
    initData();
    super.onPause();
  }

  private void initData() {

    Map<String, Object> params = new HashMap<>();
    params.put("customer_id", user.getCustomer_id());
    apiImp.getMeritsData(params, this, new DataIdCallback<String>() {
      @Override
      public void onSuccess(String data, int id) {
        MyMeritsEntity myMeritsEntity = JSON.parseObject(data, MyMeritsEntity.class);
        mTvAllmerits.setText(myMeritsEntity.getPractice_total() + "");

        mTvBellBashPeople.setText(myMeritsEntity.getStrikebull_cnt() + "人撞钟");

        mTvLightOnPeople.setText(myMeritsEntity.getDiandeng_cnt() + "人点灯");

        mTvReleasePeople.setText(myMeritsEntity.getFangsheng_cnt() + "人放生");

      }

      @Override
      public void onFailed(String errCode, String errMsg, int id) {

      }
    });
  }

  private void initListener() {

    mLlLightOn.setOnClickListener(this);
    mLlRelease.setOnClickListener(this);
    mLlBellBash.setOnClickListener(this);

    mTvRankingList.setOnClickListener(this);

    mTvRelease.setOnClickListener(this);
    mTvRechargeMerits.setOnClickListener(this);
    mIbBack.setOnClickListener(this);

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {

      case R.id.for_home_act_ib_back:
        // 返回
        finish();
        break;
      case R.id.for_home_act_tv_ranking_list:
        // 功德排行榜

        Intent intent = new Intent(this, MeritRankingActivity.class);
        startActivity(intent);

        break;
      case R.id.for_home_act_tv_release:

        // 兑换放生
        startActivity(new Intent(this, ForFreeActivity.class));


        break;
      case R.id.for_home_act_tv_recharge_merits:
        //充值功德
        startActivity(new Intent(this, RechargeMeritsActivity.class));
        break;
      case R.id.for_home_pfraactice_ll_bell_bash:
        //撞钟

        Intent zintent = new Intent(this, WebActivity.class);
        zintent.putExtra("No_Title", true);
        zintent.putExtra("url", UrlConstant.ExplainURL.ZHUANG_ZHONG + "?customer_id=" + AppConfig.CustomerId);
        startActivity(zintent);

        break;
      case R.id.for_home_pfraactice_ll_release:
        //放生
        Intent fintent = new Intent(this, WebActivity.class);
        fintent.putExtra("No_Title", true);
        fintent.putExtra("url", UrlConstant.ExplainURL.FANG_SHENG + "?customer_id=" + AppConfig.CustomerId);
        startActivity(fintent);


        break;
      case R.id.for_home_pfraactice_ll_light_on://点灯

        Intent dintent = new Intent(this, WebActivity.class);
        dintent.putExtra("No_Title", true);
        dintent.putExtra("url", UrlConstant.ExplainURL.DIAN_DENG + "?customer_id=" + AppConfig.CustomerId);
        startActivity(dintent);


        break;

    }
  }
}
