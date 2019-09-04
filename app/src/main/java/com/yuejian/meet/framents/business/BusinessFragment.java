package com.yuejian.meet.framents.business;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.umeng.socialize.utils.Log;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.business.BusinessPublishActivity;
import com.yuejian.meet.activities.business.TempShowingActivity;
import com.yuejian.meet.activities.common.SelectMemberCityActivity;
import com.yuejian.meet.activities.home.StoreWebActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.PositionInfo;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/05/11 14:10
 * @desc : 首页 - 商圈Fragment
 */
public class BusinessFragment extends BaseFragment {

    @Bind(R.id.ll_business_submit)
    LinearLayout mSubmitBtn;
    @Bind(R.id.ll_jinziyuan)
    LinearLayout mJinziyuanBtn;
    @Bind(R.id.ll_haoxiangmu)
    LinearLayout mHaoxiangmuBtn;
    @Bind(R.id.ll_yizhuanbao)
    LinearLayout mYizhuanbaoBtn;
    @Bind(R.id.ll_youpinggou)
    LinearLayout mYoupinggouBtn;
    @Bind(R.id.tl_business_tab)
    TabLayout mTabLayout;
    @Bind(R.id.ll_business_location)
    LinearLayout mBusinessLocationBtn;
    @Bind(R.id.tv_business_location_text)
    TextView mBusinessLocationTextView;
    @Bind(R.id.vp_business_content)
    ViewPager mViewPager;

    private ArrayList<Fragment> mFragmentList;

    private BusinessDemandFragment businessDemandFragment;

    private BusinessActivityFragment businessActivityFragment;

    private BusinessSquareFragment businessSquareFragment;

    private String city = "", province = "";

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_business, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(businessSquareFragment = new BusinessSquareFragment());
        mFragmentList.add(businessActivityFragment = new BusinessActivityFragment());
        mFragmentList.add(businessDemandFragment = new BusinessDemandFragment());

        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("广场");
        mTitleList.add("活动");
        mTitleList.add("需求");

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);

        mTabLayout.setupWithViewPager(mViewPager);

        //获取当前位置
        getPosition(new DataIdCallback<PositionInfo>() {
            @Override
            public void onSuccess(PositionInfo data, int id) {
                province = data.getProvince();
                city = data.getCity();
                mBusinessLocationTextView.setText(city);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @OnClick({R.id.ll_business_submit, R.id.ll_jinziyuan, R.id.ll_haoxiangmu, R.id.ll_yizhuanbao, R.id.ll_youpinggou, R.id.ll_business_location})
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {


            case R.id.ll_business_submit:
                Intent publishIntent = new Intent(getActivity(), WebActivity.class);
                String url = UrlConstant.apiUrl() + "release/release.html?customer_id=" + user.customer_id;
                publishIntent.putExtra("url", url);
                publishIntent.putExtra("No_Title", true);
                startActivityForResult(publishIntent, 38);
                break;
            case R.id.ll_jinziyuan:
                Intent intent1 = new Intent(getActivity(), WebActivity.class);
                String jzyUrl = UrlConstant.apiUrl() + "release/essential_resources_list.html?city=珠海市&customer_id=" + user.customer_id;
                intent1.putExtra("url", jzyUrl);
                intent1.putExtra("No_Title", true);
                startActivity(intent1);
                break;
            case R.id.ll_haoxiangmu:
                Intent intent2 = new Intent(getActivity(), WebActivity.class);
                String hxmUrl = UrlConstant.apiUrl() + "release/project_list.html?city=珠海市&customer_id=" + user.customer_id;
                intent2.putExtra("url", hxmUrl);
                intent2.putExtra("No_Title", true);
                startActivity(intent2);
                break;
            case R.id.ll_yizhuanbao:
//                Intent intent3 = new Intent(getActivity(), TempShowingActivity.class);
//                intent3.putExtra("title", "易赚宝");
//                startActivity(intent3);
                Intent intent3 = new Intent(getActivity(), WebActivity.class);
                String yzbUrl = UrlConstant.yiZhuanBao() + "?" + "userid="
                        + user.customer_id
                        + "&type=app";
                intent3.putExtra("url", yzbUrl);
                intent3.putExtra("No_Title", true);
                startActivity(intent3);
                break;
            case R.id.ll_youpinggou:
//                Intent intent4 = new Intent(getActivity(), TempShowingActivity.class);
//                intent4.putExtra("title", "优品购");
//                startActivity(intent4);
                Intent intent4 = new Intent(getActivity(), StoreWebActivity.class);
                String yogUrl = UrlConstant.youPinGou() + "?" + "userid="
                        + user.customer_id
                        + "&type=app";
                Log.e("taaap", yogUrl);
                intent4.putExtra("url", yogUrl);
                intent4.putExtra("tag","优品购");
                intent4.putExtra("title", "优品购");
                startActivity(intent4);
                break;

            case R.id.ll_business_location:
                Intent intent = new Intent(getContext(), SelectMemberCityActivity.class);
                if (StringUtils.isAutonomy(AppConfig.province)) {
                    intent.putExtra("city", AppConfig.province);
                } else {
                    intent.putExtra("city", AppConfig.district);
                }
                intent.putExtra("isSovereignty", false);
                startActivityForResult(intent, 11);

                break;

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            //todo 选择城市后，处理事件；
            case 11:
            case 38:

                businessDemandFragment.loadDataFromNet(1, 10, city);
                businessActivityFragment.loadDataFromNet(province, city);
                businessSquareFragment.loadDataFromNet(province, city);

                break;

        }


    }

    @Override
    public void onBusCallback(BusCallEntity event) {

        switch (event.getCallType()) {
            case CITY:
                city = event.getData();
                mBusinessLocationTextView.setText(city);
                break;

            case PROVINCE:
                province = event.getData();
                break;
        }

    }
}
