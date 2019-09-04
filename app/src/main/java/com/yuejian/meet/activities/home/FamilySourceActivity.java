package com.yuejian.meet.activities.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.framents.home.EssayFragment;
import com.yuejian.meet.framents.home.FamousPersonFragment;
import com.yuejian.meet.framents.home.SourceFragment;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by zh02 on 2017/8/29.
 */

public class FamilySourceActivity extends BaseActivity {

    @Bind(R.id.tab)
    TabLayout tabLayout;
    @Bind(R.id.vp)
    ViewPager viewPager;

    private String[] titles = {"起源", "名人录"};
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mrl);
        setTitleText("根源");
        fragments.add(new SourceFragment());
        fragments.add(new FamousPersonFragment());
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new CommFragmentPageAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {

    }

    private class CommFragmentPageAdapter extends FragmentStatePagerAdapter {
        public CommFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return fragments.get(pos);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
