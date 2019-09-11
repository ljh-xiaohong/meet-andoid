package com.yuejian.meet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuejian.meet.activities.FamilyTree.AddressListActivity;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.clan.ClanMemberActivity;
import com.yuejian.meet.activities.common.SelectFamilyCityActivity;
import com.yuejian.meet.activities.group.GroupUserinfo;
import com.yuejian.meet.activities.home.AdvancedTeamInfoActivity;
import com.yuejian.meet.activities.home.CreationActivity;
import com.yuejian.meet.activities.home.InviteJoinGroupActivity;
import com.yuejian.meet.activities.mine.BangDingWeChatActivity;
import com.yuejian.meet.activities.mine.InCashActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.search.MainSearchActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.bean.ActionUnreadMsgEntity;
import com.yuejian.meet.bean.mine2Entity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.framents.business.BusinessFragment;
import com.yuejian.meet.framents.creation.CreationFragment;
import com.yuejian.meet.framents.family.FamilyCircleContainerFragment;
import com.yuejian.meet.framents.find.FindFragment;
import com.yuejian.meet.framents.message.MessageFragment;
import com.yuejian.meet.framents.mine.MineFragment;
import com.yuejian.meet.ui.MainMoreUi;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.ImMesssageRedDot;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.utils.tinkerutil.SampleApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements AMapLocationListener {
    @Bind(R.id.rbtn_home)
    View rbtn_home;
    @Bind(R.id.rbtn_msg)
    View rbtn_message;
    @Bind(R.id.rbtn_creation)
    View rbtn_creation;
    @Bind(R.id.rbtn_business)
    View rbtn_business;
    @Bind(R.id.rbtn_mine)
    View rbtn_me;
    @Bind(R.id.img_msg_tip)
    TextView img_msg_tip;
    @Bind(R.id.address_list)
    ImageView address_list;
    @Bind(R.id.maia_layout_title_bar)
    RelativeLayout maia_layout_title_bar;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private FragmentManager mFragmentManager;
    private BaseFragment currentFragment;
    //    private HomeFragment homeFragment = new HomeFragment();
    private MessageFragment messageFragment = new MessageFragment();
    private CreationFragment creationFragment = new CreationFragment();
    private FindFragment findWebFragment = new FindFragment();
    private BusinessFragment businessFragment = new BusinessFragment();
    private MineFragment mineFragment = new MineFragment();
    //    private CultureFragment cultureFragment = new CultureFragment();
    private FamilyCircleContainerFragment familyFragment;
    //    private FamilyFragment familyFragment = new FamilyFragment();
    private Intent intent;
    private MainMoreUi mainMoreUi;
    private mine2Entity mine2;
    private TextView baiJiaXingBadge = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setlangage(this);
        getPosition(null);
//        goToGuidePage();
        AppConfig.isAppOpen = true;
        AppConfig.isGeLiGuide = false;
        ACTIVITY_NAME = "首页";
        setContentView(R.layout.activity_main_new);
        initView();
        SampleApplicationContext.application = getApplication();
        SampleApplicationContext.context = this;
        initDisplay();
        Utils.versionUpdate(this);
        onParseIntent(getIntent());
        quitGroup();
        if (AppConfig.isGeliPhone) {
            ImUtils.loginIm();//登录im
        }
        if (!CommonUtil.isNull(AppConfig.CustomerId)) {
            DadanPreference.getInstance(this).setBoolean("isLogin",true);
        }
    }

    private void goToGuidePage() {
        if (AppConfig.userEntity != null) {
            //1.5.0版本新老用户登录都能看到引导页面，往后的版本只有新用户才能看到
            try {
                String rewordIds = PreferencesUtil.get(MyApplication.context, Constants.HAVE_SHOW_UP_GUIDE, "");
                if (!rewordIds.contains(AppConfig.CustomerId)) {
                    int compare = Utils.compareVersion(Utils.getVersionName(this), "1.5.0");
                    if (compare == 0 || (compare == 1 && getIntent().getBooleanExtra("finish_register", false))) {
                        Intent intent = new Intent(this, SplashNewActivity.class);
                        startActivity(intent);
//                        finish();
                    }
                }
            } catch (ClassCastException e) {
                PreferencesUtil.remove(MyApplication.context, Constants.HAVE_SHOW_UP_GUIDE);
                PreferencesUtil.put(MyApplication.context, Constants.HAVE_SHOW_UP_GUIDE, "");
            }
            if (getIntent().getBooleanExtra("phone_register", false)) {
                startActivity(new Intent(this, BangDingWeChatActivity.class));
            }
        }
    }

    private void initDisplay() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) SampleApplicationContext.application.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        MyApplication.width = metrics.widthPixels;
        AppConfig.width = metrics.widthPixels;
        MyApplication.height = metrics.heightPixels;
        MyApplication.minEdg = MyApplication.width > MyApplication.height ? MyApplication.height : MyApplication.width;
        if (MyApplication.width > MyApplication.height) {// 保证宽度比高度小，如果宽度大则两值互换
            MyApplication.width += MyApplication.height;
            MyApplication.height = MyApplication.width - MyApplication.height;
            MyApplication.width -= MyApplication.height;
        }
        MyApplication.density = metrics.density;
        MyApplication.densityDpi = metrics.densityDpi;
        intent = getIntent();
        if (intent.hasExtra("meetPatams")) {
            String next = intent.getStringExtra("meetPatams");
            selectNext(next);
        }
    }

    public void selectNext(String next) {
        if (next.equals("1")) {
            setSelectBut(R.id.rlayout_one_to_one);
        } else if (next.equals("2")) {
            setSelectBut(R.id.rlayout_business);
        } else if (next.equals("3")) {
            setSelectBut(R.id.rlayout_creation);
        } else if (next.equals("4")) {
            setSelectBut(R.id.rlayout_msg);
        } else if (next.equals("5")) {
            setSelectBut(R.id.rlayout_mine);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ImUtils.isLoginIm) {
            ImUtils.loginIm();
        }
//        getCouponUnRead();
    }

    //处理回调
    @BusReceiver
    public void onStringEvent(String event) {
//        if (event.contains(AppConfig.Toasshow)) {
//            if (user != null) {
//                ViewInject.shortToast(this, Utils.s2tOrT2s(event.replace(AppConfig.Toasshow, "")));
//            }
//        } else if (event.equals(AppConfig.userKick)) {
//            user = null;
//            Dialog dialog = new Dialog(this);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.hint);
//            builder.setMessage(R.string.main_no_longer_showing_hint_name);
//            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    intent = new Intent(mContext, LoginActivity.class);
//                    intent.putExtra("mine_login", true);
//                    startActivity(intent);
//                    dialog.dismiss();
//                }
//            });
//            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//            dialog = builder.show();
//            dialog.show();
//            if (currentFragment instanceof MessageFragment)
//                setSelectBut(R.id.rlayout_one_to_one);
//        } else if (event.equals(AppConfig.user_freeze)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(R.string.hint);
//            builder.setMessage("您帐号被冻结");
//            builder.setPositiveButton(R.string.confirm, null);
//            builder.show();
//        } else if ("patch_download_success".equals(event)) {
//            Utils.showNoTitleDialog(this, Utils.s2tOrT2s("升级补丁版本,马上安装"), Utils.s2tOrT2s("安装"), "", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    System.exit(0);
//                }
//            }, null);
//        } else if ("bai_jia_xing_badge_bling".equals(event) || "ACTION_UNREAD".equals(event)) {
//            loadRedDot();
//        } else if ("bai_jia_xing_badge_dark".equals(event)) {
//            baiJiaXingBadge.setVisibility(View.GONE);
//        }
    }


    public void initView() {
        // TODO: 2018/11/16   徐 家族改版

        familyFragment = new FamilyCircleContainerFragment();
        initLocationMap();
        startLocation();
        mFragmentManager = this.getSupportFragmentManager();
        setSelectBut(R.id.rlayout_one_to_one);
        img_msg_tip = (TextView) findViewById(R.id.img_msg_tip);
        ImMesssageRedDot.register(new ImMesssageRedDot.RedDotInterFace() {
            @Override
            public void redDotCount(String count) {
                if (img_msg_tip != null) {
                    img_msg_tip.setVisibility(count.equals("0") ? View.GONE : View.VISIBLE);
                    img_msg_tip.setText(Integer.parseInt(count) > 99 ? "99+" : count);
                }
            }
        });
        baiJiaXingBadge = (TextView) findViewById(R.id.baijiaxing_badge);
        ImMesssageRedDot.CallMessageRigister();//群聊红点消息
    }

    public void setTitleText(String text) {
        TextView titleTv = (TextView) findViewById(R.id.title);
        titleTv.setText(text);
    }

    public void changeFragment(BaseFragment targetFragment) {
        this.changeFragment(R.id.main_content, targetFragment);
    }

    public void changeFragment(int resview, BaseFragment targetFragment) {
        if (!targetFragment.equals(this.currentFragment)) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            if (!targetFragment.isAdded()) {
                transaction.add(resview, targetFragment, targetFragment.getClass().getSimpleName());
            }
            if (targetFragment.isHidden()) {
                transaction.show(targetFragment);
                targetFragment.onUserVisible();
            }
            if (this.currentFragment != null && this.currentFragment.isVisible()) {
                transaction.hide(this.currentFragment);
                currentFragment.onUserInvisible();
            }
            this.currentFragment = targetFragment;
            transaction.commitAllowingStateLoss();
        }
        if (mineFragment != targetFragment) {
//            getCouponUnRead();
        }
    }

    @OnClick({R.id.rlayout_one_to_one, R.id.rlayout_msg, R.id.rlayout_business, R.id.rlayout_creation,
            R.id.rlayout_mine, R.id.baijiaxing, R.id.layout_search, R.id.navbar_more, R.id.address_list})
    @Override
    public void onClick(View v) {
        setSelectBut(v.getId());
    }

    /**
     * 选择的下标
     */
    public void setSelectBut(int vId) {
        if (vId == R.id.navbar_more || vId == R.id.layout_search || vId == R.id.rlayout_creation) {

        } else {
            rbtn_home.setSelected(false);
            rbtn_message.setSelected(false);
            rbtn_creation.setSelected(false);
            rbtn_business.setSelected(false);
            rbtn_me.setSelected(false);
            findViewById(R.id.baijiaxing_text).setSelected(false);
        }
        switch (vId) {
            case R.id.address_list:
                startActivity(new Intent(this, AddressListActivity.class));
                break;
            case R.id.rlayout_one_to_one:///
                rbtn_home.setSelected(true);
                changeFragment(familyFragment);
                setTitleText(getString(R.string.The_family_dynamic));
                maia_layout_title_bar.setVisibility(View.GONE);
                address_list.setVisibility(View.GONE);
                break;
            case R.id.rlayout_msg:
                if (!DadanPreference.getInstance(this).getBoolean("isLogin")){
                    Intent intent= new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                maia_layout_title_bar.setVisibility(View.GONE);
                address_list.setVisibility(View.VISIBLE);
//                if (user == null) {
//                    Intent intent = new Intent(getApplication(), LoginActivity.class);
//                    intent.putExtra("mine_login", true);
//                    startActivity(intent);
//                } else {
                    rbtn_message.setSelected(true);
                    changeFragment(messageFragment);
                    setTitleText(getString(R.string.tab_rbtn_msg));
//                }
                break;
            case R.id.rlayout_creation:
                if (!DadanPreference.getInstance(this).getBoolean("isLogin")){
                    Intent intent= new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                startActivity(new Intent(this, CreationActivity.class));
//                rbtn_creation.setSelected(true);
//                changeFragment(creationFragment);
//                setTitleText("开始创作");
//                maia_layout_title_bar.setVisibility(View.GONE);
//                address_list.setVisibility(View.GONE);
                break;
            case R.id.rlayout_business:
                if (!DadanPreference.getInstance(this).getBoolean("isLogin")){
                    Intent intent= new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                rbtn_business.setSelected(true);
                changeFragment(businessFragment);
                setTitleText("商圈");
                maia_layout_title_bar.setVisibility(View.GONE);
                address_list.setVisibility(View.GONE);

//                address_list.setVisibility(View.GONE);
//                try {
//                    JChineseConvertor jChineseConvertor = JChineseConvertor.getInstance();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                maia_layout_title_bar.setVisibility(View.GONE);
//                rbtn_business.setSelected(true);
//                changeFragment(findWebFragment);
//                setTitleText(getString(R.string.txt_discover));
                break;

            case R.id.rlayout_mine:
                if (!DadanPreference.getInstance(this).getBoolean("isLogin")){
                    Intent intent= new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                maia_layout_title_bar.setVisibility(View.GONE);
                rbtn_me.setSelected(true);
                changeFragment(mineFragment);

                setTitleText(getString(R.string.main_mine_name));
                backPressListeners = new ArrayList<>();
                backPressListeners.add(mineFragment);
                setBackPressListener(backPressListeners);
                View mineBagde = findViewById(R.id.img_mine);
                if (mineBagde.getVisibility() == View.VISIBLE) {
                    mineBagde.setVisibility(View.GONE);
                }
                break;
            case R.id.baijiaxing:
//                findViewById(R.id.baijiaxing_text).setSelected(true);
//                changeFragment(homeFragment);
                break;
            case R.id.navbar_more:
                if (mainMoreUi == null) {
                    mainMoreUi = new MainMoreUi(this);
                }
                mainMoreUi.showBottomPopupWindow(findViewById(R.id.maia_layout_title_bar));
                break;
            case R.id.layout_search:
                intent = new Intent(this, MainSearchActivity.class);
                startActivity(intent);
                break;
        }
//        startActivity(new Intent(this, PhoneCodeActivity.class));
    }


    ///跳转到绝对应的聊天页面
    private void onParseIntent(Intent intent) {
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            ArrayList<IMMessage> listMessage = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            if (listMessage == null || listMessage.size() > 1) {
                return;
            }
            final IMMessage message = listMessage.get(0);
            switch (message.getSessionType()) {
                case P2P:
                    setSelectBut(R.id.rlayout_msg);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ImUtils.toP2PCaht(mContext, message.getSessionId());
                        }
                    }, 100);
                    break;
                case Team:
                    setSelectBut(R.id.baijiaxing);
                    Map<String, Object> pushPayload = new HashMap<>();
                    pushPayload = message.getPushPayload();
                    if (pushPayload == null || !pushPayload.containsKey("sub_type")) {
                        return;
                    }

                    if (Integer.parseInt(pushPayload.get("sub_type").toString()) == ChatEnum.shop.getValue()) {
                        ImUtils.toAssemblyHall(this, message.getSessionId(), ChatEnum.shop, pushPayload.get("leader_id").toString());
                    } else if (Integer.parseInt(pushPayload.get("sub_type").toString()) == ChatEnum.FoundGroup.getValue()) {
                        ImUtils.toAssemblyHall(this, message.getSessionId(), ChatEnum.FoundGroup, "0");
                    } else if (Integer.parseInt(pushPayload.get("sub_type").toString()) == ChatEnum.CLANGROUP.getValue()) {
                        ImUtils.toGroupChat(this, message.getSessionId(), ChatEnum.CLANGROUP, "0", Boolean.valueOf(true));
                    } else {
                        Map<String, Object> params = new HashMap<>();
                        params.put("customer_id", AppConfig.CustomerId);
                        params.put("t_id", message.getSessionId());
                        new ApiImp().intoGroup(params, this, new DataIdCallback<String>() {
                            @Override
                            public void onSuccess(String data, int id) {
                                ImUtils.toTeamSession(mContext, message.getSessionId(), data);
                            }

                            @Override
                            public void onFailed(String errCode, String errMsg, int id) {
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 初始化定位参数信息
     */
    private void initLocationMap() {
        try {
            locationClient = new AMapLocationClient(getApplicationContext());
            locationOption = new AMapLocationClientOption();
            locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            locationClient.setLocationListener(this);
            locationOption.setOnceLocation(true);
            locationOption.setNeedAddress(true);
            locationOption.setGpsFirst(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startLocation() {
        locationClient.setLocationOption(locationOption);
        locationClient.startLocation();
        Log.d("location", "startLocation");
    }

    private void stopLocation() {
        locationClient.stopLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                AppConfig.slatitude = amapLocation.getLatitude() + "";//获取纬度
                AppConfig.slongitude = amapLocation.getLongitude() + "";//获取经度
                AppConfig.city = amapLocation.getCity();//城市信息
                AppConfig.province = amapLocation.getProvince();//省
                AppConfig.district = amapLocation.getDistrict();//区
                PreferencesUtil.put(this, PreferencesUtil.LATITUDE, amapLocation.getLatitude() + "");
                PreferencesUtil.put(this, PreferencesUtil.LONGITUDE, amapLocation.getLongitude() + "");
                PreferencesUtil.put(this, PreferencesUtil.CITY, amapLocation.getCity() + "");
                PreferencesUtil.put(this, PreferencesUtil.DISTRICT, amapLocation.getDistrict() + "");
                PreferencesUtil.put(this, PreferencesUtil.PROVINCE, amapLocation.getProvince() + "");
                BusCallEntity bugEntity = new BusCallEntity();
                bugEntity.setCallType(BusEnum.LOCATION_UPDATE);
                Bus.getDefault().post(bugEntity);
                updatePosition();
            }
        } else {
            ViewInject.shortToast(this, "请打开GPS开关，才能定位你当前位置哦~");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        quitGroup();
        return super.onKeyDown(keyCode, event);
    }

    public void quitGroup() {
        new NetApi().quitGroup(this, new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
            }

            @Override
            public void onFailed(String errCode, String errMsg) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Utils.isAnyWindowsIsShowing()) {
            Utils.dismissAnyWindows();
        } else if (!currentFragment.onBackPressed()) {
            boolean isBack = false;
            if (backPressListeners != null) {
                for (OnBackPressListener listener : backPressListeners) {
                    isBack = listener.onBackPress();
                    if (isBack) break;
                }
                if (!isBack) {
                    twiceCheckCloseApp();
                }
            } else {
                twiceCheckCloseApp();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        familyFragment.onWindowFocusChanged(hasFocus);
    }

    private long firstTap = 0;

    private void twiceCheckCloseApp() {
        long nowTap = System.currentTimeMillis();
        if (nowTap - firstTap <= 2000) {
            moveTaskToBack(true);
        } else {
            Toast.makeText(mContext, "再按一次返回键退出约见百家姓", Toast.LENGTH_SHORT).show();
            firstTap = nowTap;
        }
    }

    private List<OnBackPressListener> backPressListeners = null;

    public void setBackPressListener(List<OnBackPressListener> backPressListeners) {
        if (this.backPressListeners == null) {
            this.backPressListeners = new ArrayList<>();
        }
        this.backPressListeners.addAll(backPressListeners);
    }

    public interface OnBackPressListener {
        boolean onBackPress();
    }

    @Override
    public void onSomeEvent(BusCallEntity event) {
        super.onSomeEvent(event);
        if ("start_location".equals(event.data)) {
            startLocation();
        }
        if (event.getCallType() == BusEnum.LOGIN_UPDATE) {
            ImUtils.loginIm();//登录im
            updatePosition();
            goToGuidePage();
        } else if (event.getCallType() == BusEnum.INVITE_JOIN_GROUP) {
            Intent intent = new Intent(this, InviteJoinGroupActivity.class);
            intent.putExtra("tid", event.getData());
            startActivity(intent);


        } else if (event.getCallType() == BusEnum.GROUP_USERINFO) {//群资料
            if (ChatEnum.defaults == AppConfig.chatEnum) {
                intent = new Intent(this, AdvancedTeamInfoActivity.class);
            } else if (ChatEnum.CLANGROUP == AppConfig.chatEnum) {
                intent = new Intent(this, ClanMemberActivity.class);
            } else {
                intent = new Intent(this, GroupUserinfo.class);
            }
            intent.putExtra("t_id", event.getData());
            startActivity(intent);
        } else if (event.getCallType() == BusEnum.OPEN_USERINFO) {//打开用户资料
            AppUitls.goToPersonHome(this, event.getData());
        } else if (event.getCallType() == BusEnum.In_CashActivity) {//打开支付页面
            startActivity(new Intent(this, InCashActivity.class));
        } else if (event.getCallType() == BusEnum.IM_LOGIN) {//网易登录成功
        } else if (event.getCallType() == BusEnum.LOGOUT) {//退出登录
            img_msg_tip.setVisibility(View.GONE);
            ImMesssageRedDot.CallMessageRigister();//群聊红点消息
            baiJiaXingBadge.setVisibility(View.GONE);
        } else if (event.getCallType() == BusEnum.START_PAGE) {//落地页
            selectNext(event.getData());
        } else if (event.getCallType() == BusEnum.INTEN_CHAT) {
            onParseIntent(event.getIntent());
        } else if (event.getCallType() == BusEnum.Message_RECEIVER) {
            String count = event.getData();
            int a = Integer.valueOf(StringUtils.isEmpty(count) ? "0" : count);
            img_msg_tip.setVisibility(a <= 0 ? View.GONE : View.VISIBLE);
            img_msg_tip.setText(count);
        } else if (event.getCallType() == BusEnum.MESSAGE_RECEIVER_DELETE) {
            String count = event.getData();
            String textCount = img_msg_tip.getText().toString().trim();
            int a = Integer.valueOf(StringUtils.isEmpty(count) ? "0" : count);
            int b = Integer.valueOf(StringUtil.isEmpty(textCount) ? "0" : textCount);
            if (a <= b) {
                a = b - a;
            }
            img_msg_tip.setVisibility(a <= 0 ? View.GONE : View.VISIBLE);
            img_msg_tip.setText(count);
        } else if (event.getCallType() == BusEnum.ACTION_UNREAD) {
            baiJiaXingBadge.setVisibility(View.VISIBLE);
        } else if (event.getCallType() == BusEnum.GROUP_UNREAD_COUNT) {
            if (ImMesssageRedDot.getGroupUnreadCount() > 0)
                baiJiaXingBadge.setVisibility(View.VISIBLE);
        } else if (event.getCallType() == BusEnum.Bangding_Family) {
            startActivity(new Intent(this, SelectFamilyCityActivity.class));
        }
    }

    public void loadRedDot() {
        if (user == null) {
            baiJiaXingBadge.setVisibility(View.GONE);
            return;
        }
        apiImp.getActioUNreadRemind(this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ActionUnreadMsgEntity unreadMsgEntity = JSON.parseObject(data, ActionUnreadMsgEntity.class);
                if (!StringUtil.isEmpty(unreadMsgEntity.getCount())) {
                    if (Integer.parseInt(unreadMsgEntity.getCount()) > 0 || ImMesssageRedDot.getGroupUnreadCount() > 0) {
                        baiJiaXingBadge.setVisibility(View.VISIBLE);
                    } else {
                        baiJiaXingBadge.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    //更新位置
    public void updatePosition() {
        if (user == null || AppConfig.slongitude.equals("0")) return;
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("longitude", AppConfig.slongitude);
        params.put("latitude", AppConfig.slatitude);
        params.put("province", AppConfig.province);
        params.put("city", AppConfig.city);
        params.put("area", AppConfig.district);
        new ApiImp().updatePosition(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }

    private void getCouponUnRead() {
        if (StringUtils.isEmpty(AppConfig.CustomerId)) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        apiImp.getCouponUnReadCnt(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                int cnt = Integer.valueOf(data);
                if (cnt > 0) {
                    findViewById(R.id.img_mine).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.img_mine).setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }


}
