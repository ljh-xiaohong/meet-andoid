package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.creation.ArticleEditActivity;
import com.yuejian.meet.activities.creation.VideoLoadActivity;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.framents.creation.MyCreationFragment;
import com.yuejian.meet.framents.creation.PosterFragment;
import com.yuejian.meet.framents.family.VideoTemplate;
import com.yuejian.meet.widgets.CreationTitleView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private PosterFragment recommendFragment;
    private VideoTemplate followFragment;
    private MyCreationFragment cityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_activity);
        ButterKnife.bind(this);
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(followFragment = new VideoTemplate());
        mFragmentList.add(recommendFragment = new PosterFragment());
        mFragmentList.add(cityFragment = new MyCreationFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this.getSupportFragmentManager(), mFragmentList);
        vpFamilyCircleContent.setAdapter(adapter);
        vpFamilyCircleContent.setOffscreenPageLimit(2);
        vpFamilyCircleContent.addOnPageChangeListener(this);
        familyCircleTitleView.setOnTitleViewClickListener(this);
        back.setOnClickListener(v -> finish());
        setCurrentItem(0);

    }

    @OnClick({R.id.article, R.id.take})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.article:
                startActivity(new Intent(mContext, ArticleEditActivity.class));
                break;

            case R.id.take:
//                startActivity(new Intent(mContext, VideoLoadActivity.class));
                VideoLoadActivity.startRecord(mContext,"");
                break;
        }
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
