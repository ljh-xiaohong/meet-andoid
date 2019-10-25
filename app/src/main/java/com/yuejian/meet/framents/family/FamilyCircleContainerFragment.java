package com.yuejian.meet.framents.family;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.search.MainSearchActivity;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.DadanPreference;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @author : g000gle
 * @time : 2019/05/09 15:00
 * @desc : 首页 - 家圈Fragment
 */
public class FamilyCircleContainerFragment extends BaseFragment{


    @Bind(R.id.tab)
    SlidingTabLayout tab;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private FamilyCircleFollowFragment followFragment;
    private FamilyCircleRecommendFragment recommendFragment;
    private FamilyCircleSameCityFragment cityFragment;
    MyViewPagerAdapter mAdapter;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_family_circle_container, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);


        mFragments.add(followFragment = new FamilyCircleFollowFragment());
        mFragments.add(recommendFragment = new FamilyCircleRecommendFragment());
        mFragments.add(cityFragment = new FamilyCircleSameCityFragment());
        mAdapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewpager.setAdapter(mAdapter);
        tab.setViewPager(viewpager, TAB_TITLES, getActivity(), mFragments);
        tab.setCurrentTab(1);

    }

    //Tab 文字
    private final String[] TAB_TITLES = new String[]{"动态", "推荐", "生活"};
    //Fragment 数组
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    /**
     * @description: ViewPager 适配器
     */
    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
