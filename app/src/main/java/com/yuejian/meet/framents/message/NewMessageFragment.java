package com.yuejian.meet.framents.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.message.ContactActivity;
import com.yuejian.meet.activities.message.MessageSettingActivity;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.GetMessageBean;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.widgets.MessageTitleView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private NotificationMessageFragment mNotificationMessageFragment;
    private HundredSecretariesFragment mHundredSecretariesFragment;
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

    private void initView() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(mNotificationMessageFragment = new NotificationMessageFragment());
        mFragmentList.add(mHundredSecretariesFragment = new HundredSecretariesFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getFragmentManager(), mFragmentList);
        mContentPager.setAdapter(adapter);
        mContentPager.setOffscreenPageLimit(1);
        mContentPager.addOnPageChangeListener(this);
        setCurrentItem(0);
        mFamilyCircleTitleView.setOnTitleViewClickListener(this);
        mFamilyCircleTitleView.setImageBtnClick(view -> startActivity(new Intent(getActivity(), ContactActivity.class)),
                view -> initPopwindow(view));
        readPoint();

    }
    public void readPoint(){
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        apiImp.getMessage(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                GetMessageBean loginBean=new Gson().fromJson(data, GetMessageBean.class);
                mFamilyCircleTitleView.setPoint(loginBean.getData().isFirendFlag(),loginBean.getData().isReadFlag());
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
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
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置可以获取焦点
        window.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        window.update();
        window.showAsDropDown(view);
        TextView push = popupView.findViewById(R.id.push);
        TextView read = popupView.findViewById(R.id.read);
        push.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), MessageSettingActivity.class));
            window.dismiss();});
        read.setOnClickListener(v ->{
            readAll();
        });
    }
    public void readAll(){
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        apiImp.doSettingRead(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                GetMessageBean loginBean=new Gson().fromJson(data, GetMessageBean.class);
                Toast.makeText(getActivity(),loginBean.getMessage(),Toast.LENGTH_LONG).show();
                if (loginBean.getCode()==0) {
                    mFamilyCircleTitleView.setPoint(false, false);
                    BusCallEntity entity = new BusCallEntity();
                    entity.setCallType(BusEnum.NOT_POINT);
                    Bus.getDefault().post(entity);
                    window.dismiss();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
    private boolean isClick=false;
    @Override
    public void onTitleViewClick(int position) {
        switch (position) {
            case 0:
                isClick=true;
                setCurrentItem(0);
                break;
            case 1:
                isClick=true;
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
        if (position==0){
            mNotificationMessageFragment.update();
        }else {
            mHundredSecretariesFragment.update();
        }
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
                if (!isClick) {
                    setCurrentItem(0);
                }
                isClick=false;
                break;
            case 1:
                if (!isClick) {
                    setCurrentItem(1);
                }
                isClick=false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
