package com.yuejian.meet.activities.mine;

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
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.message.ContactFragment;
import com.yuejian.meet.framents.message.FriendsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 通讯录
 */
public class ContactActivity extends BaseActivity {

    @Bind(R.id.tab_contact_title)
    TabLayout tabLayout;
    @Bind(R.id.vp_contact)
    ViewPager viewPager;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitleText(getString(R.string.tab_rbtn_contact_list));
        ACTIVITY_NAME = getString(R.string.tab_rbtn_contact_list);
        String[] titles = {getString(R.string.friend), getString(R.string.attention), getString(R.string.bean_vermicelli), getString(R.string.blacklist)};
        FriendsFragment friendFragment = new FriendsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("relationType", Constants.GET_FRIENDS);
        friendFragment.setArguments(bundle);
        fragments.add(friendFragment);

        ContactFragment focusFragment = new ContactFragment();
        bundle = new Bundle();
        bundle.putInt("relationType", Constants.GET_FOCUS);
        focusFragment.setArguments(bundle);
        fragments.add(focusFragment);

        ContactFragment fansFragment = new ContactFragment();
        bundle = new Bundle();
        bundle.putInt("relationType", Constants.GET_FANS);
        fansFragment.setArguments(bundle);
        fragments.add(fansFragment);

        ContactFragment backListFragment = new ContactFragment();
        bundle = new Bundle();
        bundle.putInt("relationType", Constants.GET_BLACK_LIST);
        backListFragment.setArguments(bundle);
        fragments.add(backListFragment);

        viewPager.setAdapter(new TabFragmentPageAdapter(getSupportFragmentManager(), titles, fragments));
        tabLayout.setupWithViewPager(viewPager);
        if (getIntent().hasExtra("indexItem")) {
            viewPager.setCurrentItem(getIntent().getIntExtra("indexItem", 0));
        } else {
            viewPager.setCurrentItem(0);
        }

        viewPager.setOffscreenPageLimit(3);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_imgBtn_back:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class TabFragmentPageAdapter extends FragmentStatePagerAdapter {
        private String[] titles = new String[]{};
        private List<Fragment> fragments;

        TabFragmentPageAdapter(FragmentManager fm, String[] titles, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
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