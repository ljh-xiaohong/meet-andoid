package com.yuejian.meet.activities.mine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.NewUserEntity;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.DateActivity;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.utils.DialogUtils;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.SystemTool;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择性
 */
public class UserNameSelectActivity extends BaseActivity {

    @Bind(R.id.activity_user_name_select_new_mlayout)
    RelativeLayout mLayout;

    @Bind(R.id.acitivity_user_name_select_new_family_name)
    EditText tv_family_name;

    @Bind(R.id.acitivity_user_name_select_new_given_name)
    EditText tv_given_name;

    @Bind(R.id.acitivity_user_name_referrer)
    TextView acitivity_user_name_referrer;

    @Bind(R.id.acitivity_user_name_select_new_gender)
    RadioGroup rg_gender;

    @Bind(R.id.acitivity_user_name_select_new_next)
    Button bt_next;

    @Bind(R.id.activity_user_name_select_new_img)
    ImageView img_head;

    @Bind(R.id.activity_user_name_select_rb_man)
    RadioButton man;

    @Bind(R.id.activity_user_name_select_rb_woman)
    RadioButton woman;

    Intent intent;
    String surnameId = "";
    String mobile = "", openId = "", flag = "", photo = "", areaCode = "", phone_imei = "", phone_type = "", phone_version = "", phone_model = "", sex = "男";
    @Bind(R.id.back)
    ImageView back;


    private TextWatcherImpl twi;

    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    private static final int CROP_PIC = 13;
    LoadingDialogFragment dialog;

    private View mPoupView = null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;

    ApiImp api = new ApiImp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_user_name_select);
        setContentView(R.layout.activity_user_name_select_new);
        ButterKnife.bind(this);
        // initView();
        initViews();
        setListeners();
    }


    //初始化数据中的参数
    private void initViews() {
        dialog = LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        intent = getIntent();
        if (intent.hasExtra("mobile"))
            mobile = intent.getStringExtra("mobile");
        if (intent.hasExtra("openId"))
            openId = intent.getStringExtra("openId");
        if (intent.hasExtra("flag"))
            flag = intent.getStringExtra("flag");
        if (intent.hasExtra("photo")) {
            photo = intent.getStringExtra("photo");
            //   Glide.with(this).load(photo).into(user_header_img);
        }
        if (intent.hasExtra("areaCode"))
            areaCode = intent.getStringExtra("areaCode");


        if (intent.hasExtra("phone_imei")) {
            phone_imei = intent.getStringExtra("phone_imei");
        } else {
            phone_imei = SystemTool.getPhoneIMEI(this);
        }
        if (TextUtils.isEmpty(phone_imei)) {//权限
            phone_imei = "android_0";
        }

        if (intent.hasExtra("phone_type")) {
            phone_type = intent.getStringExtra("phone_type");
        } else {
            phone_type = "1";
        }

        if (intent.hasExtra("phone_version")) {
            phone_version = intent.getStringExtra("phone_version");
        } else {
            phone_version = SystemTool.getSystemVersion();
        }

        if (intent.hasExtra("phone_model")) {
            phone_model = intent.getStringExtra("phone_model");
        } else {
            phone_model = SystemTool.getSystemName().replaceAll(" +", "");
        }


        twi = new TextWatcherImpl();
        tv_given_name.addTextChangedListener(twi);
        tv_family_name.addTextChangedListener(twi);
    }


    /**
     * 注册
     */
    public void postRequst() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        if (StringUtils.isNotEmpty(photo)) {
            params.put("photo", photo);
        }
        if (CommonUtil.isNull(tv_family_name.getText().toString())){
            ViewInject.shortToast(this, R.string.toast_xing_not_data);
            return;
        }
        if (CommonUtil.isNull(tv_given_name.getText().toString())){
            ViewInject.shortToast(this, R.string.toast_name_not_data);
            return;
        }
        params.put("referralMobile", acitivity_user_name_referrer.getText().toString());
        params.put("surname", tv_family_name.getText().toString());
        params.put("name", tv_given_name.getText().toString());


        //最后判断是否有图片，有图片上存图片后在注册，没有图片直接注册
        if (!outputPath.equals("")) {
            uploadUserImg(params);
        } else {
            if (dialog != null)
                dialog.show(getFragmentManager(), "");
            postRegister(params);
        }


    }

    /**
     * 数据
     */
    private void postRegister(Map params) {
        apiImp.postRegister2(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                dialog.dismiss();
                NewUserEntity loginBean=new Gson().fromJson(data, NewUserEntity.class);
                if (loginBean.getData()!=null){
                    AppConfig.newUerEntity= loginBean;
                    updateCustomerData(loginBean.getData().toString());
                }
                if (loginBean.getCode() == 0) {
                    DadanPreference.getInstance(UserNameSelectActivity.this).setBoolean("isLogin",true);
                    intent = new Intent(mContext, GifActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    if(loginBean.getCode()==19985){
                        Dialog dialog = DialogUtils.createOneBtnDialog(UserNameSelectActivity.this, "", "未找到您推荐人\n" +
                                "请确认推荐人手机号","确定");
                        dialog.show();
                        return;
                    }
                    ViewInject.shortToast(getApplication(), loginBean.getMessage());
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                dialog.dismiss();
                ViewInject.shortToast(getApplication(), errMsg);
            }
        });
    }


    /**
     * 上传图片到oss 完成
     */
    private void uploadUserImg(Map params) {
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        //如果存在头像，先上存到oss

        String updtaImgName = OssUtils.getTimeNmaeJpg();
        new FeedsApiImpl().upLoadImageFileToOSS(outputPath, updtaImgName, this, new DataCallback<FeedsResourceBean>() {
            @Override
            public void onSuccess(FeedsResourceBean data) {
                photo = data.imageUrl;
                params.put("photo", photo);
                apiImp.postRegister2(params, this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        dialog.dismiss();
                        NewUserEntity loginBean=new Gson().fromJson(data, NewUserEntity.class);
                        if (loginBean.getData()!=null){
                            AppConfig.newUerEntity= loginBean;
                            updateCustomerData(loginBean.getData().toString());
                        }
                        if (loginBean.getCode() == 0) {
                            DadanPreference.getInstance(UserNameSelectActivity.this).setBoolean("isLogin",true);
                            intent = new Intent(mContext, GifActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            if(loginBean.getCode()==19985){
                                Dialog dialog = DialogUtils.createOneBtnDialog(UserNameSelectActivity.this, "", "未找到您推荐人\n" +
                                        "请确认推荐人手机号","确定");
                                dialog.show();
                                return;
                            }
                            ViewInject.shortToast(getApplication(), loginBean.getMessage());
                        }
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                        dialog.dismiss();
                        ViewInject.shortToast(getApplication(), errMsg);
                    }
                });

            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                if (dialog != null)
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
        UserEntity entity =new Gson().fromJson(data, UserEntity.class);
        PreferencesUtil.put(getApplicationContext(), PreferencesUtil.KEY_USER_INFO, data);  //存储个人信息数据
        AppConfig.userEntity = entity;
        if (!entity.getCustomer_id().equals("0")){
            AppConfig.CustomerId = entity.getCustomer_id();
        }else {
            AppConfig.CustomerId = entity.getCustomerId();
        }
        DadanPreference.getInstance(this).setString("CustomerId",AppConfig.CustomerId);
        DadanPreference.getInstance(this).setString("photo",entity.getPhoto());
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.acitivity_user_name_select_new_family_name, R.id.acitivity_user_name_select_new_given_name,
            R.id.acitivity_user_name_referrer, R.id.acitivity_user_name_select_new_gender, R.id.acitivity_user_name_select_new_next,
            R.id.activity_user_name_select_new_img})
    public void onClick(View v) {
        switch (v.getId()) {
            //图片
            case R.id.activity_user_name_select_new_img:
                showBottomPopupWindow();
                break;

            // 姓
            case R.id.acitivity_user_name_select_new_family_name:
//                intent = new Intent(getApplication(), SurnameActivity.class);
//                startActivityIfNeeded(intent, 3);
                break;
            //名
            case R.id.acitivity_user_name_select_new_given_name:
                break;
            //生日
            case R.id.acitivity_user_name_referrer:
                break;
            //性别
            case R.id.acitivity_user_name_select_new_gender:
                break;

            //下一步
            case R.id.acitivity_user_name_select_new_next:
                postRequst();
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


        }
    }

    /**
     * 初始化监听事件  完成
     */
    private void setListeners() {
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.activity_user_name_select_rb_man:
                        sex = man.getText().toString().trim();
                        break;

                    case R.id.activity_user_name_select_rb_woman:
                        sex = woman.getText().toString().trim();
                        break;
                }
            }
        });
        back.setOnClickListener(v -> onBackPressed());
    }

    public void backDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.Return_to_the_home_page);
        builder.setNegativeButton(R.string.cancel, null);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backDialog();
    }

    private static final int PORTRAIT_IMAGE_WIDTH = 720;
    String outputPath = "";


    /**
     * 打开图片/拍照选择器（完成）
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

                    Toast.makeText(UserNameSelectActivity.this, data.imageUrl, Toast.LENGTH_LONG).show();
                    photo = data.imageUrl;
                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                    if (dialog != null)
                        dialog.dismiss();
                    ViewInject.shortToast(getApplication(), errMsg);
                }
            });
        } else {
            itnetnRegister();
        }
    }


    /**
     * 注册
     */
    public void itnetnRegister() {
        intent = new Intent(getApplication(), RegisterActivity.class);
        intent.putExtra("name", tv_given_name.getText().toString());
        intent.putExtra("surname", tv_family_name.getText().toString());
        intent.putExtra("mobile", mobile);
        intent.putExtra("openId", openId);
        intent.putExtra("flag", flag);
        intent.putExtra("photo", photo);
        intent.putExtra("areaCode", areaCode);
        startActivity(intent);
        finish();
    }

    //输入监听 完成
    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            if (tv_family_name.getText().toString().length() >= 1
                    && tv_given_name.getText().toString().length() >= 1) {
                bt_next.setSelected(true);
                bt_next.setEnabled(true);
                bt_next.setBackgroundResource(R.drawable.bg_resg_grey_press_btn);
                //   bt_next.setAlpha(1);
            } else {
                bt_next.setSelected(false);
                bt_next.setEnabled(false);
                bt_next.setBackgroundResource(R.drawable.bg_resg_grey_unpress_btn);
                //  bt_next.setAlpha(0.6f);
            }
            //  full_name.setText(surname.getText().toString() + name.getText().toString());
//            full_name_layout.setVisibility(View.VISIBLE);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

    }

    //监听返回的activityresult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

//            //出生日期
//            if (requestCode == 114) {
//                tv_birthday.setText("" + data.getStringExtra("YEAR") + "-" + data.getStringExtra("MONTH") + "-" + data.getStringExtra("DAY"));
//
//            }

            //姓氏返回
            if (requestCode == 3) {
                tv_family_name.setText(data.getExtras().getString("surname"));
                surnameId = data.getExtras().getString("surnameId");
                if (tv_family_name.getText().toString().length() >= 1 && tv_given_name.getText().toString().length() >= 1) {
//                    bu_next.setSelected(true);
//                    bu_next.setEnabled(true);
//                    bu_next.setAlpha(1);
                } else {
//                    bu_next.setSelected(false);
//                    bu_next.setEnabled(false);
//                    bu_next.setAlpha(0.6f);
                }
//                requstCount(data.getExtras().getString("surname"));
//                full_name.setText(surname.getText().toString() + name.getText().toString());
            }

            //照片或图片返回
            switch (requestCode) {
                case OPENPIC://相册选择照片返回
                    if (resultCode == RESULT_OK) {
                        Glide.with(mContext).load(outputPath).asBitmap().placeholder(R.mipmap.ic_default).into(img_head);
                        return;
                    }

                    break;
                case OPENCAM://拍完照返回
                    if (resultCode == RESULT_OK) {//outputPath
                        Glide.with(mContext).load(outputPath).asBitmap().placeholder(R.mipmap.ic_default).into(img_head);
                        return;
                    }
                    break;
            }

        }
    }

//    public void requstCount(String surname) {
//        Map<String, String> params = new HashMap<>();
//        params.put("surname", surname);
//        apiImp.getSurnameCount(params, this, new DataIdCallback<String>() {
//            @Override
//            public void onSuccess(String data, int id) {
//                member_number.setText(data);
//                member_number_layout.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onFailed(String errCode, String errMsg, int id) {
//
//            }
//        });
//    }

    //完成

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

    //完成

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

    //完成

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


    //完成（样式调整）
    public void showDatePickerDialog(Context context) {
        Intent intent = new Intent(context, DateActivity.class);
        startActivityForResult(intent, 114);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            DatePickerDialog dialog = new DatePickerDialog(context);
//            dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//
//                    tv_birthday.setText("" + datePicker.getYear() + "-" + datePicker.getMonth() + "-" + datePicker.getDayOfMonth());
//
//                }
//            });
//            dialog.show();
//        }


    }

    private void simpleDate() {


    }


}

