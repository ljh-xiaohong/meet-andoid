package com.yuejian.meet.framents.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.netease.nim.uikit.common.fragment.TabFragment;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.PrecisePushAdapter;
import com.yuejian.meet.adapters.PrecisePushContentAdapter;
import com.yuejian.meet.adapters.ServiceAdapter;
import com.yuejian.meet.widgets.SecretaryTitleView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : ljh
 * @time : 2019/9/8 11:10
 * @desc :
 */
public class HundredSecretariesFragment extends Fragment implements SecretaryTitleView.OnTitleViewClickListener, ViewPager.OnPageChangeListener {

    @Bind(R.id.id_stickynavlayout_topview)
    RelativeLayout idStickynavlayoutTopview;
    @Bind(R.id.id_stickynavlayout_indicator)
    SecretaryTitleView mSecretaryTitleView;
    @Bind(R.id.id_stickynavlayout_viewpager)
    ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;


    //是否可见
    public boolean isVisible = false;
    //是否初始化完成
    public boolean isInit = false;
    //是否已经加载过
    public boolean isLoadOver = false;

    //界面可见时再加载数据(该方法在onCreate()方法之前执行。)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisible = isVisibleToUser;
        setParam();
    }

    /**
     * 初始化一些参数，完成懒加载和数据只加载一次的效果
     * isInit = true：此Fragment初始化完成
     * isLoadOver = false：此Fragment没有加载过
     * isVisible = true：此Fragment可见
     */
    private void setParam() {
        if (isInit && !isLoadOver && isVisible) {
            //加载数据
        }
    }

    private View view;// 需要返回的布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {// 优化View减少View的创建次数
            view = inflater.inflate(R.layout.hundred_secretaries_fragment, null);
            isInit = true;
            setParam();
        }
        ButterKnife.bind(this, view);
        return view;
    }

    private void initDatas()
    {
        mSecretaryTitleView.setOnTitleViewClickListener(this);
        mSecretaryTitleView.setSelectedTitle(0);
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new ServiceFragment());
        mFragmentList.add(new PrecisePushFragment());
        mAdapter = new FragmentPagerAdapter(getFragmentManager())
        {
            @Override
            public int getCount()
            {
                return mFragmentList.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                return mFragmentList.get(position);
            }

        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
        setCurrentItem(0);
    }

    /**
     * 切换页面
     *
     * @param position 分类角标
     */
    private void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position);
        mSecretaryTitleView.setSelectedTitle(position);
    }

    @Override
    public void onTitleViewClick(int position) {
        switch (position) {
            case 0:
                setCurrentItem(0);
                break;
            case 1:
                setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mSecretaryTitleView.scroll(position, positionOffset);
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
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void update() {
        initDatas();
    }

}
