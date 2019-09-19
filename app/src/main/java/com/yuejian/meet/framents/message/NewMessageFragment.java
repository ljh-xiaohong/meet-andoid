package com.yuejian.meet.framents.message;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.message.ContactActivity;
import com.yuejian.meet.activities.message.MessageSettingActivity;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.widgets.MessageTitleView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 信息
 */

/**
 * @author :
 * @time : 2018/11/29 16:29
 * @desc : 首页 消息
 * @version: V1.0
 * @update : 2018/11/29 16:29
 */


public class NewMessageFragment extends BaseFragment implements ViewPager.OnPageChangeListener, MessageTitleView.OnTitleViewClickListener {

        @Bind(R.id.family_circle_title_view)
    MessageTitleView mFamilyCircleTitleView;
        @Bind(R.id.vp_family_circle_content)
        ViewPager mContentPager;
//    @Bind(R.id.drawer_layout)
//    DrawerLayout mDrawerLayout;
//    @Bind(R.id.main_content)
//    FrameLayout mainContent;
    private NotificationMessageFragment mNotificationMessageFragment;
    private HundredSecretariesFragment mCommentZanFragment;
    private View view;// 需要返回的布局

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_new_message, container, false);
    }


    @Override
    protected void initData() {
        super.initData();
        initView();
    }

    // 布局管理器
    private FragmentManager fragManager;

    private void initView() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(mNotificationMessageFragment = new NotificationMessageFragment());
        mFragmentList.add(mCommentZanFragment = new HundredSecretariesFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), mFragmentList);
        mContentPager.setAdapter(adapter);
        mContentPager.setOffscreenPageLimit(1);
        mContentPager.addOnPageChangeListener(this);
        setCurrentItem(0);
        mFamilyCircleTitleView.setOnTitleViewClickListener(this);
        mFamilyCircleTitleView.setImageBtnClick(view -> startActivity(new Intent(getActivity(), ContactActivity.class)),
                view -> initPopwindow(view));
//        fragManager = getFragmentManager();
//        clickMenu(R.id.tv_title_one);
    }



    private View popupView;
    private PopupWindow window;

    private void initPopwindow(View view) {
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        popupView = inflater.inflate(R.layout.message_popupwindow, null);
        window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置动画
        window.setAnimationStyle(R.style.popup_window_anim);
        // 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置可以获取焦点
        window.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        window.update();
        int width = window.getWidth();
        int[] xy = new int[2];
        view.getLocationInWindow(xy);
        window.showAtLocation(view, Gravity.NO_GRAVITY,
                xy[0] + (width - view.getWidth()) / 2, xy[1] + 70);
        TextView push = popupView.findViewById(R.id.push);
        TextView read = popupView.findViewById(R.id.read);
        push.setOnClickListener(v -> startActivity(new Intent(getActivity(), MessageSettingActivity.class)));
        read.setOnClickListener(v -> window.dismiss());
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

    /**
     * 切换页面
     *
     * @param position 分类角标
     */
    private void setCurrentItem(int position) {
        mContentPager.setCurrentItem(position);
        mFamilyCircleTitleView.setSelectedTitle(position);
//        if (position == 1) {
//            //打开手势滑动
//            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//        } else {
//            //禁止手势滑动
//            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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
}
