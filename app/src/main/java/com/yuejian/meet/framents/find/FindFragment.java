package com.yuejian.meet.framents.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.app.AppConfig;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.EnjoyActivity;
import com.yuejian.meet.activities.home.StoreWebActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.framents.base.BaseFragment;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author :
 * @time : 2018/11/10 9:25
 * @desc : 首页 发现主页模块
 * @version: V1.0
 * @update : 2018/11/10 9:25
 */


public class FindFragment extends BaseFragment {

  //  @Bind(R.id.txt_titlebar_title)
//  TextView title;
  @Bind(R.id.unread_msg_count)
  TextView msgCount;
  @Bind(R.id.find_fmt_for_home_pfractice)
  LinearLayout mFindFmtForHomePfractice;
  @Bind(R.id.find_fmt_ll_create_zong_will_enjoy)
  LinearLayout mFindFmtLlCreateZongWillEnjoy;
  @Bind(R.id.find_fmt_ll_optimization_shopping)
  LinearLayout mFindFmtLlOptimizationShopping;
  @Bind(R.id.find_fmt_ll_family_college)
  LinearLayout mFindFmtLlFamilyCollege;
  @Bind(R.id.find_fmt_ll_shopping_mall)
  LinearLayout mFindFmtLlShoppingMall;


  @Override
  protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    return inflater.inflate(R.layout.fragment_find, container, false);
  }

  @Override
  protected void initData() {


  }


  @OnClick({R.id.find_fmt_for_home_pfractice, R.id.find_fmt_ll_create_zong_will_enjoy, R.id.find_fmt_ll_optimization_shopping,
          R.id.find_fmt_ll_family_college, R.id.find_fmt_ll_shopping_mall,R.id.yimiaotao})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.find_fmt_for_home_pfractice:
        // 为家修行
        startActivity(new Intent(getActivity(), ForHomePfracticeActivity.class));
        break;

      case R.id.find_fmt_ll_create_zong_will_enjoy:
        // 创建宗享会


        startActivity(new Intent(getActivity(), EnjoyActivity.class));
        return;

      case R.id.find_fmt_ll_optimization_shopping:
          // 优选商城


        Intent sintent = new Intent(getActivity(), WebActivity.class);
        sintent.putExtra("No_Title", false);
        sintent.putExtra("tag","YIKE");
        sintent.putExtra("url", UrlConstant.ExplainURL.YIKE_SHANGCHENG + "?customerId=" + AppConfig.CustomerId);
        startActivity(sintent);

        break;

      case R.id.find_fmt_ll_family_college:

        // 家族学院

        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("No_Title", true);
        intent.putExtra("url", UrlConstant.ExplainURL.FAXIAN_SCHOOL + "?customer_id=" + AppConfig.CustomerId);
        startActivity(intent);

        break;

      case R.id.find_fmt_ll_shopping_mall:
        // 商城
        startActivity(new Intent(getActivity(), StoreWebActivity.class));
        break;

      case R.id.yimiaotao:
        String appId = "wx457b71ba948f85c0"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(getContext(), appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_25c911d20fca"; // 填小程序原始id
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
        break;

    }
  }


}
