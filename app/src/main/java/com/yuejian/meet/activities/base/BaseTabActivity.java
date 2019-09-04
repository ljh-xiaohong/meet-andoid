package com.yuejian.meet.activities.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuejian.meet.R;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/5/27 12:53
 * @desc : 带Tab + ViewPager的Activity
 */
public abstract class BaseTabActivity extends BaseActivity {

    @Bind(R.id.iv_base_tab_activity_back_btn)
    protected ImageView mBackBtn;
    @Bind(R.id.tv_base_tab_activity_title)
    protected TextView mTitleView;
    @Bind(R.id.tl_base_tab_activity_tab)
    protected TabLayout mTabLayout;
    @Bind(R.id.vp_base_tab_activity_content)
    protected ViewPager mViewPager;

    protected int mIndex = 0;
    protected List<Fragment> mFragmentList;
    protected List<String> mTabTitleList;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    protected int mPageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_tab);
        mIndex = getIntent().getIntExtra("index", 0);
        setTitleTxt();

        setFragmentList();
        setTabTitleList();
        setChildCount();

        checkChildData();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.vp_base_tab_activity_content);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tl_base_tab_activity_tab);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

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

    private void checkChildData() {
        if (mFragmentList == null || mTabTitleList == null ||
                mFragmentList.size() != mTabTitleList.size() ||
                mTabTitleList.size() != mPageCount ||
                mPageCount == 0) {
                    throw new RuntimeException("BaseTabActivity init error.");
                }
    }

    protected abstract void setTitleTxt();

    protected abstract void setFragmentList();

    protected abstract void setTabTitleList();

    protected abstract void setChildCount();

    @OnClick({R.id.iv_base_tab_activity_back_btn})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_base_tab_activity_back_btn:
                finish();
                break;
            default:
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
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
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
}
