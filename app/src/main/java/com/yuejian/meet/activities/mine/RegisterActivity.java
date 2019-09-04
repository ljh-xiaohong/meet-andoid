package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SelectCityActivity;
import com.yuejian.meet.activities.gile.Register2Activity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.FCLoger;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.ViewInject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * 注册基本信息选择
 */
/**
 * @author :
 * @time   : 2018/11/23 14:53
 * @desc   : 选择性别
 * @version: V1.0
 * @update : 2018/11/23 14:53
 */

public class RegisterActivity extends BaseActivity {
    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton back;
    @Bind(R.id.txt_titlebar_title)
    TextView title;
    @Bind(R.id.bu_next)
    Button bu_next;
    @Bind(R.id.txt_basic_city)
    TextView city;
    @Bind(R.id.basic_age)
    TextView age;
    @Bind(R.id.basic_man)
    CheckBox basic_man;
    @Bind(R.id.basic_woman)
    CheckBox basic_woman;
    @Bind(R.id.user_header_img)
    ImageView user_header_img;
    @Bind(R.id.basic_info_main)
    LinearLayout mLayout;
    ApiImp aip = new ApiImp();
    private View mPoupView = null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;
    Intent intent;
    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    private static final int CROP_PIC = 13;
    private int mAge;
    private int mMonth;
    private int mDay;
    String name, surname;
    String sex = "";
    String mobile = "", openId = "", flag = "";
    String provincn_city_area_id = "", photo = "",areaCode="";
    private String phoneImei, phoneVersion, phoneModel;
    private LoadingDialogFragment dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_basic_info_select);
        initView();

    }

    public void initView() {
        setTitleText("基本信息");
        dialog = new LoadingDialogFragment().newInstance(getString(R.string.is_requesting));
        phoneImei = SystemTool.getPhoneIMEI(this);
        if (TextUtils.isEmpty(phoneImei)) {//权限
            phoneImei = "android_0";
        }
        phoneVersion = SystemTool.getSystemVersion();
        phoneModel = SystemTool.getSystemName().replaceAll(" +", "");
        intent = getIntent();
        name = intent.getStringExtra("name");
        surname = intent.getStringExtra("surname");
        if (intent.hasExtra("openId")) {
            openId = intent.getStringExtra("openId");
        }
        if (intent.hasExtra("flag")) {
            flag = intent.getStringExtra("flag");
        }
        if (intent.hasExtra("mobile")) {
            mobile = intent.getStringExtra("mobile");
        }
        if (intent.hasExtra("photo")) {
            photo = intent.getStringExtra("photo");
        }
        if (intent.hasExtra("areaCode"))
            areaCode=intent.getStringExtra("areaCode");
        back.setVisibility(View.VISIBLE);
//        basic_man.setSelected(true);
        Glide.with(mContext).load(photo).asBitmap().placeholder(R.mipmap.ic_default).into(user_header_img);
    }

    @OnClick({R.id.txt_basic_city, R.id.titlebar_imgBtn_back, R.id.basic_age, R.id.basic_man, R.id.basic_woman
            , R.id.user_header_img, R.id.bu_next,R.id.sex_woman_layout,R.id.sex_man_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_imgBtn_back://返回
                finish();
                break;
            case R.id.txt_basic_city://城市
                intent = new Intent(getApplication(), SelectCityActivity.class);
                startActivityIfNeeded(intent, 3);
                break;
            case R.id.basic_age://出生日期
                showDatePick();
                break;
            case R.id.basic_man://选择男
                sex = "男";
                basic_man.setSelected(true);
                basic_woman.setSelected(false);
                break;
            case R.id.sex_man_layout://选择男
                sex = "男";
                basic_man.setSelected(true);
                basic_woman.setSelected(false);
                break;
            case R.id.basic_woman://选择女
                sex = "女";
                basic_man.setSelected(false);
                basic_woman.setSelected(true);
                break;
            case R.id.sex_woman_layout://选择女
                sex = "女";
                basic_man.setSelected(false);
                basic_woman.setSelected(true);
                break;
            case R.id.bu_next://注册
//                updateUserImg();

//                if (dialog != null)
////                    dialog.show(getFragmentManager(), "");

                if (StringUtil.isEmpty(sex)){
                    ViewInject.toast(this,"请选择性别");
                    return;
                }
                intent=new Intent(this, Register2Activity.class);
                intent.putExtra("name",name);
                intent.putExtra("surname",surname);
                intent.putExtra("mobile",mobile);
                intent.putExtra("openId",openId);
                intent.putExtra("flag",flag);
                intent.putExtra("photo",photo);
                intent.putExtra("areaCode",areaCode);
                intent.putExtra("sex",sex);
                startActivity(intent);
                this.finish();
//                postRequst();
                break;
            case R.id.user_header_img://选择头像
                showBottomPopupWindow();
                break;
            case R.id.txt_dialog_photo://拍照
                showSelector(false);
                mPoupWindow.dismiss();
//                showBottomPopupWindow();
                break;
            case R.id.txt_dialog_album://选择图片
                showSelector(true);
                mPoupWindow.dismiss();
//                showBottomPopupWindow();
                break;
            case R.id.txt_dialog_cancel:
                mPoupWindow.dismiss();
                break;
        }
    }

    private static final int PORTRAIT_IMAGE_WIDTH = 720;
    String outputPath = "";

    /**
     * 打开图片选择器
     */
    private void showSelector(Boolean isOpen) {

        outputPath = ImgUtils.imgTempFile();
        int from = PickImageActivity.FROM_LOCAL;
        if (isOpen) {
            PickImageActivity.start(this, OPENPIC, from, outputPath, false, 1,
                    false, true, PORTRAIT_IMAGE_WIDTH, PORTRAIT_IMAGE_WIDTH);
        } else {
            from = PickImageActivity.FROM_CAMERA;
            PickImageActivity.start(this, OPENCAM, from, outputPath, false, 1,
                    false, true, PORTRAIT_IMAGE_WIDTH, PORTRAIT_IMAGE_WIDTH);
        }
    }

    /**
     * 上传头像到oss
     */
    public void updateUserImg() {
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        if (!outputPath.equals("")) {
            String updtaImgName = OssUtils.getTimeNmaeJpg();
            new FeedsApiImpl().upLoadImageFileToOSS(outputPath, updtaImgName, this, new DataCallback<FeedsResourceBean>() {
                @Override
                public void onSuccess(FeedsResourceBean data) {
                    photo = data.imageUrl;
                    postRequst();
                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                    if (dialog != null)
                        dialog.dismiss();
                    ViewInject.shortToast(getApplication(), errMsg);
                }
            });
        } else {
            postRequst();
        }
    }

    /**
     * 注册
     */
    public void postRequst() {
        if (StringUtil.isEmpty(sex)){
            ViewInject.toast(this,"请选择性别");
            return;
        }
//        if (StringUtil.isEmpty(provincn_city_area_id)){
//            ViewInject.toast(this,"请完成地区选择");
//            return;
//        }
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(mobile)) {
            params.put("mobile", mobile);
            params.put("area_code",areaCode);
        }
        params.put("surname", surname);
        params.put("name", name);
        params.put("sex", sex.equals("女") ? "0" : "1");
        params.put("birthday", age.getText().toString());
        if (StringUtils.isNotEmpty(photo)) {
            params.put("photo", photo);
        }

//        params.put("area_name", provincn_city_area_id);//地区选择先不用
//        params.put("provincn_city_area_id", provincn_city_area_id);
        params.put("phone_type", "1");
        params.put("phone_imei", phoneImei);
        params.put("phone_version", phoneVersion);
        params.put("phone_model", phoneModel);
        params.put("registration_id", JPushInterface.getRegistrationID(this));
        if (StringUtils.isNotEmpty(openId)) {
            params.put("openId", openId);//openId
        }
        if (StringUtils.isNotEmpty(flag)) {
            params.put("flag", flag);//flag表明openId是微信或QQ:1.weixin,2:QQ
        }
        String inviteCode = PreferencesUtil.get(getApplicationContext(), AppConfig.INVITE_CODE, "");
        if (StringUtils.isNotEmpty(inviteCode)) {
            params.put("op_invite_code", inviteCode);
        } else {
            Log.d("meet", "invite code is empty.");
        }
        aip.postRegister2(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                dialog.dismiss();
//                PreferencesUtil.put(getApplicationContext(), AppConfig.INVITE_CODE, "");
                updateCustomerData(data);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                dialog.dismiss();
                ViewInject.shortToast(getApplication(), errMsg);
            }
        });
    }

    /**
     * 弹出生日选择器
     */
    private void showDatePick() {
        DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        picker.setLabel(StringUtils.getResStr(this, R.string.pickerview_year), StringUtils.getResStr(this, R.string.pickerview_month), StringUtils.getResStr(this, R.string.pickerview_day));
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH) + 1;
        mDay = now.get(Calendar.DAY_OF_MONTH);
        FCLoger.debug("year:" + year + ",Month:" + mMonth + ",mDay:" + mDay);
        picker.setRangeStart(year - 90, mMonth, mDay);//开始范围
        picker.setRangeEnd(year - 12, mMonth, mDay);//结束范围
        if (mAge > 11 && mAge < 91) {
            int year1 = year - mAge;
            picker.setSelectedItem(year1, mMonth, mDay);
        } else {
            picker.setSelectedItem(1990, 1, 1);
        }
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                age.setText(year + "-" + month + "-" + day);
                if (age.getText().length() > 0 && city.getText().length() > 0)
                    bu_next.setSelected(true);
                bu_next.setEnabled(true);
            }
        });
        picker.show();
    }

    /**
     * 底部PopupWindow
     */
    private void showBottomPopupWindow() {
        if (mPoupView == null) {
            mInflater = LayoutInflater.from(this);
            mPoupView = mInflater.inflate(R.layout.dialog_edit_head_photo, null);
            bindPopMenuEvent(mPoupView);
            mPoupWindow = new PopupWindow(mPoupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPoupWindow.setAnimationStyle(R.style.PopupAnimation);
            mPoupWindow.setTouchable(true);
            mPoupWindow.setFocusable(true);
            mPoupWindow.setOutsideTouchable(true);
            mPoupWindow.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            mPoupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
            ColorDrawable dw = new ColorDrawable(0x90000000);
            mPoupWindow.setBackgroundDrawable(dw);
        }
        mPoupWindow.showAtLocation(mLayout, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
    }

    /**
     * 实例化底部pop菜单项
     *
     * @param view
     */
    private void bindPopMenuEvent(View view) {
        TextView txtToPhoto = (TextView) view.findViewById(R.id.txt_dialog_photo);
        TextView txtToAlbum = (TextView) view.findViewById(R.id.txt_dialog_album);
        TextView txtSavePic = (TextView) view.findViewById(R.id.txt_dialog_save_pic);
        TextView txtCancel = (TextView) view.findViewById(R.id.txt_dialog_cancel);
        txtToPhoto.setOnClickListener(this);
        txtSavePic.setOnClickListener(this);
        txtSavePic.setVisibility(View.GONE);
        txtToAlbum.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OPENPIC://相册选择照片返回
                if (resultCode == RESULT_OK) {
                    Glide.with(mContext).load(outputPath).asBitmap().placeholder(R.mipmap.ic_default).into(user_header_img);
                    return;
                }

                break;
            case OPENCAM://拍完照返回
                if (resultCode == RESULT_OK) {//outputPath
                    Glide.with(mContext).load(outputPath).asBitmap().placeholder(R.mipmap.ic_default).into(user_header_img);
                    return;
                }
                break;
            case CROP_PIC:
                if (resultCode != RESULT_OK) {
                    return;
                }
                break;

        }


    }

    @Override
    public void onSomeEvent(BusCallEntity event) {
        super.onSomeEvent(event);
        BusEnum callType = event.getCallType();
        if (callType == BusEnum.FINISH_SELECTCITY) {
            String place = city.getText().toString();
            provincn_city_area_id = event.getAreaName();
//            provincn_city_area_id = event.getId();
            city.setText(place + event.getData());
        } else if (callType == BusEnum.PROVINCE) {
            city.setText(event.getData());
        } else if (callType == BusEnum.CITY) {
            String place = city.getText().toString();
            if (place.contains("省")) {
                place += event.getData();
            } else {
                place = event.getData();
            }
            city.setText(place);
        }
        if (age.getText().length() > 0 && city.getText().length() > 0) {
            bu_next.setSelected(true);
            bu_next.setEnabled(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (!AppConfig.isGeLiGuide){
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 保存用户信息
     *
     * @param data
     */
    public void updateCustomerData(String data) {
        UserEntity userBean = JSON.parseObject(data, UserEntity.class);
        PreferencesUtil.put(getApplicationContext(), PreferencesUtil.KEY_USER_INFO, data);  //存储个人信息数据
        AppConfig.userEntity = userBean;
        AppConfig.CustomerId = userBean.getCustomer_id();
        AppConfig.UserSex = userBean.getSex();
        AppConfig.Token = userBean.getToken();
        if (AppConfig.isGeLiGuide){
            finish();
            return;
        }
        Intent intent = new Intent(getBaseContext(), PerfectUserDataActivity.class);
        intent.putExtra("finish_register", true);
        startActivity(intent);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                BusCallEntity entity = new BusCallEntity();
//                entity.setCallType(BusEnum.LOGIN_UPDATE);
//                Bus.getDefault().post(entity);
//            }
//        },500);
        finish();
    }

}
