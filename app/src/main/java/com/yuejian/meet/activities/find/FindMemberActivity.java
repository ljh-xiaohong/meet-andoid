package com.yuejian.meet.activities.find;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.UpgradeInfoTipActivity;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ClanMemberAdapter;
import com.yuejian.meet.adapters.NearMemberAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.bean.BookJson;
import com.yuejian.meet.bean.FindClanEntity;
import com.yuejian.meet.bean.FindMemberEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.InnerListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发现宗亲
 */
public class FindMemberActivity extends BaseActivity implements AMapLocationListener {
    private InnerListView clanListView, nearListView;
    @Bind(R.id.layout_near)
    LinearLayout layoutNear;
    @Bind(R.id.layout_clan)
    LinearLayout layoutClan;
    @Bind(R.id.find_clan_cb)
    CheckBox find_clan_cb;
    @Bind(R.id.find_near_cb)
    CheckBox find_near_cb;
    @Bind(R.id.txt_find_attention)
    TextView txt_find_attention;
    @Bind(R.id.find_view_wire)
    View find_view_wire;
    /**
     * 获取库Phone表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER = 1;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    List<BookJson> bookJsonsList = new ArrayList<>();
    List<FindMemberEntity> clanList = new ArrayList<>();//通讯录
    List<FindMemberEntity> nearList = new ArrayList<>();//附近的人
    ClanMemberAdapter clanMemberAdapter;
    NearMemberAdapter nearMemberAdapter;
    LoadingDialogFragment dialog;
    ApiImp api = new ApiImp();
    String ids = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_member);
        initData();
        initLocationMap();
        getPhoneContacts();
        setTitleText("发现宗亲");
        initBackButton(false);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public void initData() {
        dialog = LoadingDialogFragment.newInstance("正在操作..");
        clanListView = (InnerListView) findViewById(R.id.find_clan_li);
        nearListView = (InnerListView) findViewById(R.id.find_near_list);
        clanMemberAdapter = new ClanMemberAdapter(clanListView, clanList, R.layout.item_find_clan_layout);
        clanListView.setAdapter(clanMemberAdapter);
        clanMemberAdapter.notifyDataSetChanged();
        nearMemberAdapter = new NearMemberAdapter(nearListView, nearList, R.layout.item_find_near_layout);
        nearListView.setAdapter(nearMemberAdapter);
        nearMemberAdapter.notifyDataSetChanged();
        if (dialog!=null){
            dialog.show(getFragmentManager(),"");
        }
    }

    // 获取手机联系人
    private void getPhoneContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            getFindDclan();
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1100);
        } else {
            ContentResolver resolver = mContext.getContentResolver();
            // 获取手机联系人
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PHONES_PROJECTION, null, null, null);
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    BookJson bookJson = new BookJson();
                    bookJson.setByname(phoneCursor.getString(PHONES_DISPLAY_NAME));
                    bookJson.setMobile(phoneCursor.getString(PHONES_NUMBER));
                    bookJsonsList.add(bookJson);
                }
                ;
                phoneCursor.close();
            }

            Map<String, Object> params = new HashMap<>();
            JSONArray bookJson = (JSONArray) JSON.toJSON(bookJsonsList);
            String bookString = bookJson.toJSONString();
            params.put("customer_id", AppConfig.CustomerId);
            params.put("book_json", bookString);
            api.postSavePhoneBook(params, this, new DataIdCallback<String>() {
                @Override
                public void onSuccess(String data, int id) {
                    getFindDclan();
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {
                    getFindDclan();
                }
            });

        }
    }

    /**
     * 加载数据
     */
    public void getFindDclan() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("longitude", AppConfig.slongitude);
        params.put("latitude", AppConfig.slatitude);
        params.put("province", AppConfig.province);
        api.getFinDclan(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                FindClanEntity findClanEntity = JSON.parseObject(data, FindClanEntity.class);
                clanList = JSON.parseArray(findClanEntity.getClan_list(), FindMemberEntity.class);
                nearList = JSON.parseArray(findClanEntity.getNear_list(), FindMemberEntity.class);
                if (clanList.size() > 0) {
                    clanMemberAdapter.refresh(clanList);
                } else {
                    layoutClan.setVisibility(View.GONE);
                    find_view_wire.setVisibility(View.GONE);
                }
                if (nearList.size() > 0) {
                    nearMemberAdapter.refresh(nearList);
                }
                if (clanList.size() == 0 && nearList.size() == 0) {
                    Intent intent = new Intent(mContext, UpgradeInfoTipActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (dialog!=null){
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.toast(getApplicationContext(), errMsg);
                if (dialog!=null){
                    dialog.dismiss();
                }
            }
        });
    }

    @OnClick({R.id.layout_near, R.id.layout_clan, R.id.find_clan_cb, R.id.find_near_cb, R.id.find_attention})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_near:
                find_near_cb.setChecked(!find_near_cb.isChecked());
                setCBNear();
                break;
            case R.id.layout_clan:
                find_clan_cb.setChecked(!find_clan_cb.isChecked());
                setCBClan();
                break;
            case R.id.find_clan_cb:
                setCBClan();
                break;
            case R.id.find_near_cb:
                setCBNear();
                break;
            case R.id.find_attention://提交要关注的人
                postAttention();
                break;
        }
    }

    ///提交要关注的人
    public void postAttention() {
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        ids = "";
        for (FindMemberEntity userInfo : clanList) {
            if (userInfo.getSelect()) {
                if (StringUtil.isEmpty(ids)) {
                    ids = userInfo.getCustomer_id();
                } else {
                    ids = ids + "," + userInfo.getCustomer_id();
                }
            }
        }
        for (FindMemberEntity userInfo : nearList) {
            if (userInfo.getSelect()) {
                if (StringUtil.isEmpty(ids)) {
                    ids = userInfo.getCustomer_id();
                } else {
                    ids = ids + "," + userInfo.getCustomer_id();
                }
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("op_customer_ids", ids);
        api.postAttentionBatch(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Intent intent = new Intent(mContext, UpgradeInfoTipActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.toast(getApplicationContext(), errMsg);
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    ///选择附近宗亲
    public void setCBNear() {
        Boolean selNearCb = find_near_cb.isChecked();
        for (int i = 0; i < nearList.size(); i++) {
            nearList.get(i).setSelect(selNearCb);
        }
        nearMemberAdapter.refresh(nearList);
//        isUnSelAll();
    }

    //选择联系人
    public void setCBClan() {
        Boolean selClanCb = find_clan_cb.isChecked();
        for (int i = 0; i < clanList.size(); i++) {
            clanList.get(i).setSelect(selClanCb);
        }
        clanMemberAdapter.refresh(clanList);
//        isUnSelAll();
    }

    //是否全部都没有选择
    public void isUnSelAll() {
        ids = "";
        boolean isAllSelectclan=false,isAllSelectNear=false;
        for (FindMemberEntity userInfo : clanList) {
            if (userInfo.getSelect()) {
                isAllSelectclan=true;
                break;
            }
        }
        for (FindMemberEntity userInfo : nearList) {
            if (userInfo.getSelect()) {
                isAllSelectNear=true;
            }
        }
        find_near_cb.setChecked(isAllSelectNear);
        find_clan_cb.setChecked(isAllSelectclan);
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
        startLocation();
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
            }
        } else {
            ViewInject.shortToast(this, "请打开GPS开关，才能定位你当前位置哦~");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1100) {
            boolean agree = true;
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    agree = false;
                    break;
                }
            }

            if (agree) {
                getPhoneContacts();
            }
        }
    }
}
