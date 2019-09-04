package com.yuejian.meet.framents.mine;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuejian.meet.R;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.widgets.TitleHorizontalScrollViewTeam.ItemTextView;
import com.yuejian.meet.widgets.TitleHorizontalScrollViewTeam.TitleHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MineBusinessFragment extends BaseFragment {

    ViewPager mViewPager;
    //    TabLayout mTabLayout;
//    @Bind(R.id.fragment_inner_business_select)
//    TitleHorizontalScrollView mTitleHorizontalScrollView;

    List<ItemTextView> ItemTextViews = new ArrayList<>();

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_inner_business, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        mViewPager = (ViewPager) parentView.findViewById(R.id.fragment_inner_business_viewpager);
//        mTabLayout = (TabLayout) parentView.findViewById(R.id.fragment_inner_business_tab);


    }

    @Override
    protected void initData() {
        super.initData();

        ItemTextView activity = new ItemTextView(getContext());
        activity.setClick(true);
        activity.setText("活动");
        ItemTextViews.add(activity);
        ItemTextView needed = new ItemTextView(getContext());
        needed.setText("需求");
        ItemTextViews.add(needed);
        ItemTextView soure = new ItemTextView(getContext());
        soure.setText("资源");
        ItemTextViews.add(soure);
        ItemTextView project = new ItemTextView(getContext());
        project.setText("项目");
        ItemTextViews.add(project);
        ItemTextView yizhuanbao = new ItemTextView(getContext());
        yizhuanbao.setText("易赚宝");
        ItemTextViews.add(yizhuanbao);
        ItemTextView youpingou = new ItemTextView(getContext());
        youpingou.setText("优品购");
        ItemTextViews.add(youpingou);
//        mTitleHorizontalScrollView.setAdapter(new SelectAdapter());

//        ArrayList<String> mTitleList = new ArrayList<>();
//        mTitleList.add("活动");
//        mTitleList.add("需求");
//        mTitleList.add("资源");
//        mTitleList.add("项目");
//        mTitleList.add("易赚宝");
//        mTitleList.add("优品购");
//        List<Fragment> mFragmentList = new ArrayList<>();
//        mFragmentList.add(new MineInsideFragment());
//        mFragmentList.add(new MineInsideFragment());
//        mFragmentList.add(new MineInsideFragment());
//        mFragmentList.add(new MineInsideFragment());
//        mFragmentList.add(new MineInsideFragment());
//        mFragmentList.add(new MineInsideFragment());
//
//        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList, mTitleList);
//        mViewPager.setAdapter(adapter);
//        mTabLayout.setupWithViewPager(mViewPager);
    }


    class SelectAdapter extends TitleHorizontalScrollView.SelectAdapter {

        @Override
        public int getcount() {
            return ItemTextViews.size();
        }

        @Override
        public Object getItem(int i) {
            return ItemTextViews.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, ViewGroup viewGroup) {
            return ItemTextViews.get(i);
        }
    }
}
