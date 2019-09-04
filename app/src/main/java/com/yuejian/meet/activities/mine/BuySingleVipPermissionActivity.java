package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.SingleVipDetailEntity;
import com.yuejian.meet.common.GlobalVipInfo;
import com.yuejian.meet.framents.mine.JoinSingleVipDetailFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/05/18 10:05
 * @desc : 我的 - 点亮VIP - 开通单项会员特权
 */
public class BuySingleVipPermissionActivity extends BaseActivity {

    @Bind(R.id.iv_buy_single_vip_back_btn)
    ImageView mBuySingleVipBackBtn;
    @Bind(R.id.tl_single_vip_permission_tab)
    TabLayout mTabLayout;
    @Bind(R.id.vp_single_vip_permission_content)
    ViewPager mViewPager;
    @Bind(R.id.cl_buy_all_vip_permission2)
    ConstraintLayout mBuyAllVipBtn;
    @Bind(R.id.tv_buy_all_vip_time2)
    TextView mBuyAllVipTimeView;
    @Bind(R.id.tv_buy_all_vip_price2)
    TextView mBuyAllVipPriceView;
    @Bind(R.id.tv_buy_all_vip_price_not2)
    TextView mBuyAllVipPriceNotView;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    protected List<Fragment> mFragmentList;
    protected List<String> mTabTitleList;
    protected int mPageCount;

    private GlobalVipInfo mGlobalVipInfo;
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_single_vip_permission);

        mGlobalVipInfo = GlobalVipInfo.getInstance(getApplicationContext());
        mIndex = getIntent().getIntExtra("index", 0);

        setFragmentList();
        setTabTitleList();
        setChildCount();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //全VIP权限剩余天数
        mBuyAllVipTimeView.setText(String.format("剩余%s天", mGlobalVipInfo.getPayAllVipTime()));
        mBuyAllVipTimeView.setVisibility(mGlobalVipInfo.getPayAllVipTime() > 0 ? View.VISIBLE : View.INVISIBLE);
        //全VIP权限优惠价格
        mBuyAllVipPriceView.setText(String.format("%s ￥%s/年", mGlobalVipInfo.getPayAllVipTime() > 0 ? "续费" : "开通", mGlobalVipInfo.getPayAllVipPrice()));
        //全VIP权限价格设置中划线
        mBuyAllVipPriceNotView.setText(String.format("￥%s", mGlobalVipInfo.getPayAllVipPriceNot()));
        mBuyAllVipPriceNotView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        mViewPager.setCurrentItem(mIndex);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setFragmentList() {
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < mGlobalVipInfo.getSingleVipDetailEntities().size(); i++) {
            SingleVipDetailEntity entity = mGlobalVipInfo.getSingleVipDetailEntities().get(i);
            JoinSingleVipDetailFragment fragment = JoinSingleVipDetailFragment.newInstance(entity);
            mFragmentList.add(fragment);
        }
    }

    private void setTabTitleList() {
        mTabTitleList = new ArrayList<>();
        for (int i = 0; i < mGlobalVipInfo.getSingleVipDetailEntities().size(); i++) {
            String title = mGlobalVipInfo.getSingleVipDetailEntities().get(i).getVip_name();
            mTabTitleList.add(title);
        }
    }

    private void setChildCount() {
        mPageCount = mGlobalVipInfo.getSingleVipDetailEntities().size();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateData();
    }

    @OnClick({R.id.cl_buy_all_vip_permission2, R.id.iv_buy_single_vip_back_btn, R.id.tv_pay_all_accpect})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_buy_single_vip_back_btn:
                finish();
                break;
            case R.id.cl_buy_all_vip_permission2:
                Intent toPayAllIntent = new Intent(this, BuyAllVipPermissionActivity.class);
                startActivity(toPayAllIntent);
                finish();
                break;
            case R.id.tv_pay_all_accpect:
                Intent webActivity = new Intent(this, WebActivity.class);
                webActivity.putExtra("url", UrlConstant.VIPACCEPCT());
                webActivity.putExtra("No_Title", true);
                startActivity(webActivity);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFragmentList != null)
            mFragmentList.clear();
        if (mTabTitleList != null)
            mTabTitleList.clear();
//        mSectionsPagerAdapter = null;
//        mViewPager.setAdapter(null);
//        mViewPager.clearOnPageChangeListeners();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitleList.get(position);
        }

        @Override
        public int getCount() {
            return mPageCount;
        }
    }

    private void updateData() {
        Map<String, Object> params = new HashMap<>();
        //约见ID
        params.put("customer_id", user.customer_id);
        apiImp.findIntroduceVo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(data);
                    org.json.JSONArray entityArray = jsonObject.getJSONArray("single_vip");
                    mGlobalVipInfo.setSingleVipDetailEntities(JSON.parseArray(entityArray.toString(), SingleVipDetailEntity.class));

                    org.json.JSONObject yearObject = jsonObject.getJSONObject("year_vip");
                    mGlobalVipInfo.setPayAllVipId(yearObject.getInt("id"));
                    //全VIP权限剩余天数
                    mGlobalVipInfo.setPayAllVipTime(yearObject.getInt("remaining_days"));
                    mBuyAllVipTimeView.setText(String.format("剩余%s天", mGlobalVipInfo.getPayAllVipTime()));
                    mBuyAllVipTimeView.setVisibility(mGlobalVipInfo.getPayAllVipTime() > 0 ? View.VISIBLE : View.INVISIBLE);
                    //全VIP权限优惠价格
                    mGlobalVipInfo.setPayAllVipPrice(yearObject.getInt("year_money"));
                    mBuyAllVipPriceView.setText(String.format("%s ￥%s/年", mGlobalVipInfo.getPayAllVipTime() > 0 ? "续费" : "开通", mGlobalVipInfo.getPayAllVipPrice()));
                    //全VIP权限价格
                    mGlobalVipInfo.setPayAllVipPriceNot(yearObject.getInt("yearly_price"));
                    mBuyAllVipPriceNotView.setText(String.format("￥%s", mGlobalVipInfo.getPayAllVipPriceNot()));


                    SingleVipDetailEntity entity = mGlobalVipInfo.getSingleVipDetailEntities().get(mIndex);
                    JoinSingleVipDetailFragment fragment = JoinSingleVipDetailFragment.newInstance(entity);
                    fragment.refreshData(entity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
