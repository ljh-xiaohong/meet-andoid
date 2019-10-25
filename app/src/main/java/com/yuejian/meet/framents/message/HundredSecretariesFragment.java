package com.yuejian.meet.framents.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.BaiJiaSourceBean;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.PersonalViewpager;
import com.yuejian.meet.widgets.SecretaryTitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 精准推送
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
    PersonalViewpager mViewPager;
    @Bind(R.id.statistics_img)
    ImageView statisticsImg;
    private FragmentPagerAdapter mAdapter;
    public ApiImp apiImp = new ApiImp();

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
    //获取统计图
    private void initImgData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        apiImp.getBaiJiaSource(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                BaiJiaSourceBean bean = new Gson().fromJson(data, BaiJiaSourceBean.class);
                if (bean.getCode() != 0) {
                    ViewInject.shortToast(getActivity(), bean.getMessage());
                    return;
                }
                if (!TextUtils.isEmpty(bean.getData().getImage())) {
                    Glide.with(getActivity()).load(bean.getData().getImage()).into(statisticsImg);
                }else {
                    Glide.with(getActivity()).load(R.mipmap.default_pic).into(statisticsImg);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(getActivity(), errMsg);
            }
        });
    }
    private void initDatas()
    {
        initImgData();
        mSecretaryTitleView.setOnTitleViewClickListener(this);
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
        setCurrentItem(1);
    }

    /**
     * 切换页面
     *
     * @param position 分类角标
     */
    private void setCurrentItem(int position) {
        mViewPager.setPostion(position);
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
