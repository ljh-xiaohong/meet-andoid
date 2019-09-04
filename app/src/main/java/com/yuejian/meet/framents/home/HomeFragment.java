package com.yuejian.meet.framents.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ActionUnreadMsgEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.ImMesssageRedDot;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.CustomViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 家族
 */
public class HomeFragment extends BaseFragment {
    CustomViewPager viewpager;
    TabLayout table;
    private long lastPressTime;
    private boolean isClickOnce;
    private int selectedPage;
    private final static int DOUBLE_PRESS_INTERVAL = 2000;
    private CommFragmentPageAdapter mPageAdapter;
    private View view, tabRedView, tabGroupRedView, storeTab;
    private View search;
    private ImageView iv_tab_red, tabGroupRed, storeTabBadge;
    private TextView tab_aciton_item, tabGroupActoin, storeMessage;
    private MemberFragment memberFragment = new MemberFragment();///成员RE
    private ActionFragment actionFragment = new ActionFragment();//动态
    private GroupChatFragment groupChatFragment = new GroupChatFragment();//群聊
    private StoreFragment storeFragment = new StoreFragment();///商域
    private Fragment meetFragment = new Fragment();///约见

    private String[] titles;
    List<Fragment> mFragmentList = new ArrayList<>();


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        tabRedView = inflater.inflate(R.layout.custom_view_layout, container, false);
        tabGroupRedView = inflater.inflate(R.layout.custom_view_layout, container, false);
        storeTab = inflater.inflate(R.layout.custom_view_layout, container, false);
        return view;
    }

    @Override
    protected void initData() {
        table = (TabLayout) view.findViewById(R.id.tab_home_hot);
        viewpager = (CustomViewPager) view.findViewById(R.id.vp_home_food);
        iv_tab_red = (ImageView) tabRedView.findViewById(R.id.iv_tab_red);
        tabGroupRed = (ImageView) tabGroupRedView.findViewById(R.id.iv_tab_red);
        tab_aciton_item = (TextView) tabRedView.findViewById(R.id.tab_aciton_item);
        tabGroupActoin = (TextView) tabGroupRedView.findViewById(R.id.tab_aciton_item);
        storeMessage = (TextView) storeTab.findViewById(R.id.tab_aciton_item);
        storeTabBadge = (ImageView) storeTab.findViewById(R.id.iv_tab_red);
        storeMessage.setText("商域");
        tabGroupActoin.setText("群聊");
        viewpager.setPagingEnabled(true);
        titles = new String[]{"宗亲", "动态", "群聊", "商域", /*, "约见"*/};
        mFragmentList.add(memberFragment);
        mFragmentList.add(actionFragment);
        mFragmentList.add(groupChatFragment);
        boolean mallSwitch = PreferencesUtil.readBoolean(getActivity().getApplicationContext(), Constants.MALL_SWITCH);
        if (mallSwitch) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.URL, Constants.STORE_URL + "/index" + "?userid=" + (StringUtils.isEmpty(AppConfig.CustomerId) ? "" : AppConfig.CustomerId) + "&type=app");
            storeFragment.setArguments(bundle);
            mFragmentList.add(storeFragment);
            List<MainActivity.OnBackPressListener> onBackPressListeners = new ArrayList<MainActivity.OnBackPressListener>();
            onBackPressListeners.add(storeFragment);
            ((MainActivity) getActivity()).setBackPressListener(onBackPressListeners);
            Bus.getDefault().post(storeFragment);
        } else {
            titles = new String[]{"宗亲", "动态", "群聊"};
        }
        mPageAdapter = new CommFragmentPageAdapter(getChildFragmentManager());
        table.setTabMode(TabLayout.MODE_FIXED);
        viewpager.setAdapter(mPageAdapter);
        viewpager.setOffscreenPageLimit(1);
        table.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(0);
        search = view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MemberSearchActivity.class));
            }
        });
        view.findViewById(R.id.top_layout_tabs).setClickable(true);
        view.findViewById(R.id.top_layout_tabs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTopForRefreshing();
            }
        });
        if (table.getTabAt(1) != null) {
            table.getTabAt(1).setCustomView(tabRedView);
        }
        if (table.getTabAt(2) != null) {
            table.getTabAt(2).setCustomView(tabGroupRedView);
        }
        if (titles.length > 3) {
            if (table.getTabAt(3) != null) {
                table.getTabAt(3).setCustomView(storeTab);
            }
        }
        table.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isClickOnce = false;
                selectedPage = tab.getPosition();
                if (selectedPage == 1) {
                    iv_tab_red.setVisibility(View.GONE);
                    tab_aciton_item.setTextColor(Color.parseColor("#BA945A"));
                } else {
                    tab_aciton_item.setTextColor(Color.parseColor("#ffffff"));
                }
                if (selectedPage == 2) {
                    tabGroupRed.setVisibility(View.GONE);
                    tabGroupActoin.setTextColor(Color.parseColor("#BA945A"));
                } else {
                    tabGroupActoin.setTextColor(Color.parseColor("#ffffff"));
                }

                if (selectedPage == 3) {
                    storeTabBadge.setVisibility(View.GONE);
                    storeMessage.setTextColor(Color.parseColor("#BA945A"));
                } else {
                    storeMessage.setTextColor(Color.parseColor("#ffffff"));
                }

                notifyMainBadge();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                goToTopForRefreshing();
            }
        });
        loadActionRed();
        if (ImMesssageRedDot.getGroupUnreadCount() > 0 && selectedPage != 2) {
            tabGroupRed.setVisibility(View.VISIBLE);
            notifyMainBadge();
        }
    }

    /**
     * 动态红点提示
     */
    public void loadActionRed() {
        if (StringUtil.isEmpty(AppConfig.CustomerId) || selectedPage == 1) {
            iv_tab_red.setVisibility(View.GONE);
            notifyMainBadge();
            return;
        }
        apiImp.getActioUNreadRemind(this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ActionUnreadMsgEntity unreadMsgEntity = JSON.parseObject(data, ActionUnreadMsgEntity.class);
                if (!StringUtil.isEmpty(unreadMsgEntity.getCount())) {
                    if (Integer.parseInt(unreadMsgEntity.getCount()) > 0) {
                        iv_tab_red.setVisibility(View.VISIBLE);
                        notifyMainBadge();
                    }
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void goToTopForRefreshing() {
        long currentTime = System.currentTimeMillis();
        long interval = (currentTime - lastPressTime) / 1000L;
        if (isClickOnce && interval <= DOUBLE_PRESS_INTERVAL) {
            if (selectedPage == 1) {
                //action fresh
                Bus.getDefault().post("action_refresh");
            } else if (selectedPage == 0) {
                // member fresh
                Bus.getDefault().post("member_refresh");
            }
            isClickOnce = false;
        } else {
            isClickOnce = true;
            lastPressTime = currentTime;
        }
    }

    private class CommFragmentPageAdapter extends FragmentStatePagerAdapter {
        public CommFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            if (storeFragment == mFragmentList.get(pos)) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.URL, Constants.STORE_URL + "/index" + "?userid=" + (StringUtils.isEmpty(AppConfig.CustomerId) ? "" : AppConfig.CustomerId) + "&type=app");
                storeFragment.setArguments(bundle);
            }
            return mFragmentList.get(pos);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
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


    @Override
    public void onSomeEvent(BusCallEntity event) {
        super.onSomeEvent(event);
        if (BusEnum.LOGOUT == event.getCallType() || BusEnum.LOGIN_UPDATE == event.getCallType()) {
            if (user==null){
                iv_tab_red.setVisibility(View.GONE);
                tabGroupRed.setVisibility(View.GONE);
            }
            if (storeFragment != null) {
                String url = Constants.STORE_URL + "/index" + "?userid=" + (StringUtils.isEmpty(AppConfig.CustomerId) ? "" : AppConfig.CustomerId) + "&type=app";
                Bus.getDefault().post(url);
            }
            loadActionRed();
            if (BusEnum.LOGOUT == event.getCallType()) {
                tabGroupRed.setVisibility(View.GONE);
            }
            notifyMainBadge();
        } else if (event.getCallType() == BusEnum.ACTION_UNREAD) {
            loadActionRed();
        } else if (event.getCallType() == BusEnum.GROUP_UNREAD_COUNT) {
            if (ImMesssageRedDot.getGroupUnreadCount() > 0 ) {
                if (selectedPage != 2){
                    tabGroupRed.setVisibility(View.VISIBLE);
                    notifyMainBadge();
                }
            }else {
                tabGroupRed.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void receiverBus(String event) {
        if (AppConfig.userKick.equals(event)) {
            if (storeFragment != null) {
                String url = Constants.STORE_URL + "/index" + "?userid=" + (StringUtils.isEmpty(AppConfig.CustomerId) ? "" : AppConfig.CustomerId) + "&type=app";
                Bus.getDefault().post(url);
            }
        }

        if ("store_has_un_read_message".equals(event)) {
            storeTabBadge.setVisibility(View.VISIBLE);
            notifyMainBadge();
        }

        if ("ACTION_UNREAD".equals(event)) {
            loadActionRed();
        }
    }

    private void checkStoreMessage() {
        if (StringUtil.isEmpty(AppConfig.CustomerId)) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("type", String.valueOf(7));
        apiImp.getUnReadMsgCount(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(data);
                    int count = jsonObject.getInt("7");
                    storeTabBadge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                    notifyMainBadge();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                storeTabBadge.setVisibility(View.GONE);
                notifyMainBadge();
            }
        });
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
//        checkStoreMessage();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (selectedPage==1)
            notifyMainBadge();
    }

    private void notifyMainBadge() {

        if (AppConfig.userEntity != null
                && (storeTabBadge.getVisibility() == View.VISIBLE)
                || (iv_tab_red.getVisibility() == View.VISIBLE)
                || (tabGroupRed.getVisibility() == View.VISIBLE)) {
            Bus.getDefault().post("bai_jia_xing_badge_bling");
        } else {
            Bus.getDefault().post("bai_jia_xing_badge_dark");
        }
    }
}
