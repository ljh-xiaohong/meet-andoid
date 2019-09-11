package com.yuejian.meet.activities.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.widget.LinearLayout;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.framents.family.VideoTemplate;
import com.yuejian.meet.widgets.CreationTitleView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreationActivity extends BaseActivity implements ViewPager.OnPageChangeListener, CreationTitleView.OnTitleViewClickListener {

    @Bind(R.id.family_circle_title_view)
    CreationTitleView familyCircleTitleView;
    @Bind(R.id.vp_family_circle_content)
    ViewPager vpFamilyCircleContent;
    @Bind(R.id.search_list)
    LinearLayout searchList;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.take)
    LinearLayout take;
    @Bind(R.id.article)
    LinearLayout article;
    @Bind(R.id.back)
    LinearLayout back;

    private VideoTemplate recommendFragment;
    private VideoTemplate followFragment;
    private VideoTemplate cityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_activity);
        ButterKnife.bind(this);
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(followFragment = new VideoTemplate());
        mFragmentList.add(recommendFragment = new VideoTemplate());
        mFragmentList.add(cityFragment = new VideoTemplate());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this.getSupportFragmentManager(), mFragmentList);
        vpFamilyCircleContent.setAdapter(adapter);
        vpFamilyCircleContent.setOffscreenPageLimit(2);
        vpFamilyCircleContent.addOnPageChangeListener(this);
        familyCircleTitleView.setOnTitleViewClickListener(this);
        back.setOnClickListener(v -> finish());
        setCurrentItem(0);

    }

    /**
     * 切换页面
     *
     * @param position 分类角标
     */
    private void setCurrentItem(int position) {
        vpFamilyCircleContent.setCurrentItem(position);
        familyCircleTitleView.setSelectedTitle(position);
        if (position == 1) {
            //打开手势滑动
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            //禁止手势滑动
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                setCurrentItem(0);

                break;
            case 1:
                setCurrentItem(1);
                break;
            case 2:
                setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTitleViewClick(int position) {
        switch (position) {
            case 0:
                setCurrentItem(0);
                followFragment.loadDataFromNet("0", 1, 10);
                break;
            case 1:
                setCurrentItem(1);
                followFragment.loadDataFromNet("0", 1, 10);
                break;
            case 2:
                setCurrentItem(2);
                followFragment.loadDataFromNet("0", 1, 10);
                break;
            case 3:
                followFragment.loadDataFromNet("0", 1, 10);
                break;
            case 4:
                followFragment.loadDataFromNet("0", 1, 10);
                break;
            default:
                break;
        }
    }
}
