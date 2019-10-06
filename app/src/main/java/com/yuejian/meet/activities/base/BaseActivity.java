package com.yuejian.meet.activities.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.yuejian.meet.NoNetworkActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.PositionInfo;
import com.yuejian.meet.utils.AppManager;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.zhy.autolayout.AutoLayoutActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by zh03 on 2017/6/15.
 */

public abstract class BaseActivity extends AutoLayoutActivity implements View.OnClickListener {
    //获取权限会添加到这个map中
    protected static final String TAG = "SBaseActivity";
    protected Context mContext;
    public UserEntity user = null;
    public ApiImp apiImp = new ApiImp();
    public static String ACTIVITY_NAME = "";
    public PositionInfo position = new PositionInfo();
    protected WeakReference<BaseActivity> reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        reference = new WeakReference(this);
        AppManager.addActivity(this);
        if (Utils.isMIUI()) {
            Utils.MIUISetStatusBarLightMode(getWindow(), true);
        } else if (Utils.isFlyme()) {
            Utils.FlymeSetStatusBarLightMode(getWindow(), true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        user = AppConfig.userEntity;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        checkRequiredPermission(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        Bus.getDefault().register(this);
        initBackButton(true);
        setTitleText("");
    }

    @BusReceiver
    public void onStringEvent(String event) {
        if (event.equals(AppConfig.userKick)) {
            if (!getClass().getName().contains("MainActivity") && !getClass().getName().contains("AVChatActivity")) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = getTitleText();
        if (StringUtils.isNotEmpty(text)) {
            MobclickAgent.onPageStart(text);
        }
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * bus回调
     *
     * @param event
     */
    @BusReceiver
    public void onSomeEvent(BusCallEntity event) {
        if (event.getCallType() == BusEnum.LOGIN_UPDATE || event.getCallType() == BusEnum.LOGOUT) {
            user = AppConfig.userEntity;
        } else if (event.getCallType() == BusEnum.START_PAGE) {
            if (!getClass().getName().contains("MainActivity") && !getClass().getName().contains("AVChatActivity")) {
                finish();
            }
        } else if (event.getCallType() == BusEnum.Language) {
            recreate();
        }
        onBusCallback(event);
    }

    public void onBusCallback(BusCallEntity event) {
    }

    @Override
    public void onBackPressed() {
        if (Utils.isAnyWindowsIsShowing()) {
            Utils.dismissAnyWindows();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String text = getTitleText();
        if (StringUtils.isNotEmpty(text)) {
            MobclickAgent.onPageEnd(text);
        }
        ButterKnife.unbind(this);
        Bus.getDefault().unregister(this);
        AppManager.finishActivity(this);
        reference = null;
    }

    public void initBackButton(boolean isShow) {
        ImageButton back = (ImageButton) findViewById(R.id.titlebar_imgBtn_back);
        if (back != null) {
            if (isShow) {
                back.setVisibility(View.VISIBLE);

            } else {
                back.setVisibility(View.GONE);
            }
            if (getClass().getName().contains("ReleaseActionActivity")) return;
            if (getClass().getName().contains("UserNameSelectActivity")) return;
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NoNetworkActivity.timeClose = System.currentTimeMillis();
                    finish();
                }
            });
        }
    }

    public void setTitleText(String title) {
        TextView titleTv = (TextView) findViewById(R.id.txt_titlebar_title);
        if (titleTv != null) {
            if (StringUtils.isEmpty(title)) {
                titleTv.setVisibility(View.GONE);
                return;
            }
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText(title);
        }
    }

    public String getTitleText() {
        TextView titleTv = (TextView) findViewById(R.id.txt_titlebar_title);
        if (titleTv != null && titleTv.getVisibility() == View.VISIBLE) {
            String text = titleTv.getText().toString().trim();
            if (StringUtils.isNotEmpty(text)) {
                return text;
            }
        }
        return ACTIVITY_NAME;
    }

    //所需要申请的权限数组
    private static final String[] permissionsArray = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECORD_AUDIO};
    //还需申请的权限列表
    private List<String> permissionsList = new ArrayList<String>();
    //申请权限后的返回码
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private void checkRequiredPermission(final Activity activity) {
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        if (Manifest.permission.ACCESS_COARSE_LOCATION.equals(permissions[i])) {
                            Bus.getDefault().post(new BusCallEntity(null, null, null, "start_location"));
                        }
                    } else {
//                        Toast.makeText(this, "权限被拒绝： " + permissions[i], Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (checkIsLife()) return;
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null && fm.getFragments() != null && fm.getFragments().size() > 0) {
            for (Fragment frag : fm.getFragments()) {
                if (frag != null && frag.isVisible()) {
                    handleResult(frag, requestCode, resultCode, data);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("RestrictedApi")
    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onClick(View v) {
    }

    public void getPosition(DataIdCallback<PositionInfo> callback) {
        apiImp.getPosition(mContext, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                PositionInfo info = JSON.parseObject(data, PositionInfo.class);
                AppConfig.city = info.getCity();
                AppConfig.province = info.getProvince();
                if (null != callback) callback.onSuccess(info, id);

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (null != callback) callback.onFailed(errCode, errMsg, id);
            }
        });
    }

    /**
     * 检测是否还在栈内
     *
     * @return true：不在栈内，false：还在栈内
     */
    protected boolean checkIsLife() {
        return reference == null || reference.get() == null || reference.get().isFinishing();
    }

}
