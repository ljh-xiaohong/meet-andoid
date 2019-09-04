package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SelectCityActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 个人主页编辑
 */
/**
 * @author :
 * @time   : 2018/11/22 11:55
 * @desc   : 基本资料 修改
 * @version: V1.0
 * @update : 2018/11/22 11:55
 */

public class EditHomeActivity extends BaseActivity{
    @Bind(R.id.txt_titlebar_save)
    TextView txt_titlebar_save;
    @Bind(R.id.user_suname)
    TextView user_suname;
    @Bind(R.id.user_name)
    EditText user_name;
    @Bind(R.id.company_name)
    EditText company_name;
    @Bind(R.id.user_job)
    EditText user_job;
    @Bind(R.id.user_phone)
    EditText user_phone;
    @Bind(R.id.user_email)
    EditText user_email;
    @Bind(R.id.origin_place)
    TextView origin_place;
    @Bind(R.id.user_location)
    TextView user_location;
    @Bind(R.id.user_sxe)
    ImageView user_sxe;
    @Bind(R.id.user_photo)
    CircleImageView user_photo;

    UserEntity userEntity;
    String district,province,city,areaName;
    int type=1;
    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    private static final int CROP_PIC = 13;
    private static final int PORTRAIT_IMAGE_WIDTH = 720;
    String outputPath = "";
    private PopupWindow mPoupWindow = null;
    protected LayoutInflater mInflater;
    private View mPoupView = null;
    private LoadingDialogFragment dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_home);
        setTitleText("基本资料");
        txt_titlebar_save.setVisibility(View.VISIBLE);
        dialog=LoadingDialogFragment.newInstance("正在操作..");
        requstData();
    }

    @OnClick({R.id.select_origin_layout,R.id.select_location_layout,R.id.user_photo,R.id.txt_titlebar_save})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.select_origin_layout:
                type=1;
                startActivity(new Intent(this, SelectCityActivity.class));
                break;
            case R.id.select_location_layout:
                type=2;
                startActivity(new Intent(this, SelectCityActivity.class));
                break;
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
            case R.id.txt_titlebar_save:
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

    public void requstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        apiImp.getUserBaseInfoV3(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                userEntity= JSON.parseObject(data,UserEntity.class);
                setLayout();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void setLayout(){
        photo=userEntity.getPhoto();
        Glide.with(this).load(userEntity.getPhoto()).into(user_photo);
        user_suname.setText(userEntity.getSurname());
        user_name.setText(userEntity.getName());
        company_name.setText(userEntity.getCompany_name());
        user_job.setText(userEntity.getJob());
        user_phone.setText(userEntity.getMobile());
        user_email.setText(userEntity.getEmail());
        origin_place.setText(userEntity.getOrigin_place());
        user_location.setText(userEntity.getHome());
        user_sxe.setSelected(userEntity.getSex().equals("1"));
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
        mPoupWindow.showAtLocation(findViewById(R.id.add_layout), Gravity.BOTTOM, 0, 0);
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
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType() == BusEnum.district) {
            if (AppConfig.district.equals(event.getData())) {
                province = AppConfig.province;
                city = AppConfig.city;
                district = AppConfig.district;
                return;
            } else if (getString(R.string.surnames_all).equals(event.getData())) {
                province = getString(R.string.nationwide);
                district = getString(R.string.nationwide);
            } else {
                if (event.getStateType().equals("2")) {
                    city = event.getData();
                    district = "";
                } else {
                    district = event.getData();
                }
            }
            areaName = event.getAreaName();
            if (type==1){
                origin_place.setText(areaName);
            }else {
                user_location.setText(areaName);
//                user_location.setText(district.equals("") ? city : district);
            }
        }else if (event.getCallType() == BusEnum.FINISH_SELECTCITY){
            areaName = event.getAreaName();
            if (type==1){
                origin_place.setText(areaName);
            }else {
                user_location.setText(areaName);
//                user_location.setText(district.equals("") ? city : district);
            }
        }
    }
    String photo;
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
    public void postRequst(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("name",user_name.getText().toString());
        params.put("company",company_name.getText().toString());
        params.put("email",user_email.getText().toString());
        params.put("origin_place",origin_place.getText().toString());
        params.put("home",user_location.getText().toString());
        params.put("job",user_job.getText().toString());
        params.put("photo",photo);
        apiImp.updataUserInfoData(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }
}
