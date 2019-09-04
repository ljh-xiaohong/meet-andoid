package com.yuejian.meet.activities.gile;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.AddRelationActivity2;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * 注册页面
 */

public class Register2Activity extends BaseActivity {

    @Bind(R.id.basic_info_main)
    LinearLayout mLayout;
    @Bind(R.id.user_photo)
    ImageView user_photo;

    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    private static final int CROP_PIC = 13;
    private static final int PORTRAIT_IMAGE_WIDTH = 720;
    String outputPath = "";
    private PopupWindow mPoupWindow = null;
    protected LayoutInflater mInflater;
    private View mPoupView = null;
    private LoadingDialogFragment dialog;
    Intent intent;

    String name, surname;
    String sex = "";
    String mobile = "", openId = "", flag = "";
    String provincn_city_area_id = "", photo = "",areaCode="";
    private String phoneImei, phoneVersion, phoneModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        dialog = new LoadingDialogFragment().newInstance(getString(R.string.is_requesting));
        initData();
    }
    public void initData(){
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
        if (intent.hasExtra("sex"))
            sex=intent.getStringExtra("sex");
        if (!StringUtils.isEmpty(photo))
            Glide.with(this).load(photo).into(user_photo);
    }

    @OnClick({R.id.user_photo,R.id.bu_next})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.user_photo:
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
            case R.id.txt_dialog_cancel://选择图片
                mPoupWindow.dismiss();
                break;
            case R.id.bu_next:
                if (StringUtils.isEmpty(outputPath) && StringUtils.isEmpty(photo)){
                    ViewInject.toast(this,"请选择头像");
                    return;
                }
                if (!StringUtils.isEmpty(outputPath)){
                    updateUserImg();
                }else {
                    postRequst();
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OPENPIC://相册选择照片返回
                if (resultCode == RESULT_OK) {
                    Glide.with(mContext).load(outputPath).asBitmap().placeholder(R.mipmap.ic_default).into(user_photo);
                    return;
                }else {
                    outputPath="";
                }
                break;
            case OPENCAM://拍完照返回
                if (resultCode == RESULT_OK) {//outputPath
                    Glide.with(mContext).load(outputPath).asBitmap().placeholder(R.mipmap.ic_default).into(user_photo);
                    return;
                }else {
                    outputPath="";
                }
                break;
            case CROP_PIC:
                if (resultCode != RESULT_OK) {
                    return;
                }
                break;
        }
    }

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
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(mobile)) {
            params.put("mobile", mobile);
            params.put("area_code",areaCode);
        }
        params.put("surname", surname);
        params.put("name", name);
        params.put("sex", sex.equals("女") ? "0" : "1");
        params.put("birthday", "1980-01-01");
        if (StringUtils.isNotEmpty(photo)) {
            params.put("photo", photo);
        }
        params.put("phone_type", "1");
        params.put("phone_imei", phoneImei);
        params.put("phone_version", phoneVersion);
        params.put("phone_model", phoneModel);
        params.put("registration_id", JPushInterface.getRegistrationID(this));
        if (openId != null && StringUtils.isNotEmpty(openId)) {
            params.put("openId", openId);//openId
        } else {
            params.put("openId", "null");//openId
        }
        if (flag != null && StringUtils.isNotEmpty(flag) && !flag.contains("null")) {
            params.put("flag", flag);//flag表明openId是微信或QQ:1.weixin,2:QQ
        } else {
            params.put("flag", "0");
        }
        String inviteCode = PreferencesUtil.get(getApplicationContext(), AppConfig.INVITE_CODE, "");
        if (StringUtils.isNotEmpty(inviteCode)) {
            params.put("op_invite_code", inviteCode);
        } else {
            params.put("op_invite_code", "inviteCode");
            Log.d("meet", "invite code is empty.");
        }
        apiImp.postRegister2(params, this, new DataIdCallback<String>() {
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
        String inviteCode = PreferencesUtil.get(getApplicationContext(), AppConfig.INVITE_CODE, "");
        if (StringUtils.isNotEmpty(inviteCode)) {
            intent = new Intent(getBaseContext(), AddRelationActivity2.class);
        }else {
            intent=new Intent(mContext,MainActivity.class);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BusCallEntity entity = new BusCallEntity();
                    entity.setCallType(BusEnum.LOGIN_UPDATE);
                    Bus.getDefault().post(entity);
                }
            },500);
        }
        startActivity(intent);
//        Intent intent = new Intent(getBaseContext(), PerfectUserDataActivity.class);
//        intent.putExtra("finish_register", true);
//        startActivity(intent);

        this.finish();
    }
}
