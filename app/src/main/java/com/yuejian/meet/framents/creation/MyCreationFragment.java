package com.yuejian.meet.framents.creation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuejian.meet.R;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.widgets.TabLayoutHelper;

import java.util.ArrayList;
import java.util.List;

public class MyCreationFragment extends BaseFragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    MyFragmentPagerAdapter adapter;

    List<String> titles;
    List<Fragment> fragments;
    TabLayoutHelper mTabLayoutHelper;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_poster, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.fragment_poster_tablayout);
        viewPager = view.findViewById(R.id.fragment_poster_viewpager);

        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("模板");
        titles.add("视频");
        titles.add("文章");
        fragments.add(MyCreationListFragment.newInstance(3));
        fragments.add(MyCreationListFragment.newInstance(2));
        fragments.add(MyCreationListFragment.newInstance(1));
        adapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(titles.size());
        mTabLayoutHelper=new TabLayoutHelper(tabLayout,viewPager);
        mTabLayoutHelper.setAutoAdjustTabModeEnabled(true);
    }


}
