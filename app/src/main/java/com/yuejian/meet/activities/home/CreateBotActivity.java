package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.ChooseIndustryActivity;
import com.yuejian.meet.activities.mine.ChooseJobActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.BotEntity;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CleanableEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发布项目，编辑项目
 */
public class CreateBotActivity extends BaseActivity {

    @Bind(R.id.bot_photo)
    ImageView bot_photo;
    @Bind(R.id.bot_layout)
    View bot_layout;
    @Bind(R.id.bot_title)
    CleanableEditText bot_title;
    @Bind(R.id.bot_site)
    CleanableEditText bot_site;
    @Bind(R.id.tet_job)
    TextView tet_job;
    @Bind(R.id.bot_abstract)
    EditText bot_abstract;
    @Bind(R.id.rg_sel_1)
    CheckBox rg_sel_1;
    @Bind(R.id.rg_sel_2)
    CheckBox rg_sel_2;
    @Bind(R.id.rg_sel_3)
    CheckBox rg_sel_3;
    @Bind(R.id.launch_bot)
    TextView launch_bot;

    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    private static final int CROP_PIC = 13;
    private static final int PORTRAIT_IMAGE_WIDTH = 720;
    String outputPath = "";
    LoadingDialogFragment dialog;
    String id;
    BotEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bot);
        dialog=LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        if (getIntent().hasExtra("id")){
            id=getIntent().getStringExtra("id");
        }
        if (!StringUtils.isEmpty(id)){
            launch_bot.setText(R.string.dialog_text_save);
            setTitleText(getString(R.string.bot_txt_7));
            getRequstData();
        }else {
            setTitleText(getString(R.string.bot_txt_8));
        }
    }

    private View mPoupView = null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;

    @OnClick({R.id.bot_photo,R.id.launch_bot,R.id.job_layout})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.job_layout:
                startActivityIfNeeded(new Intent(getBaseContext(), ChooseIndustryActivity.class),114);
                break;
            case R.id.bot_photo:
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
            case R.id.launch_bot:
                if (StringUtils.isEmpty(bot_title.getText().toString())){
                    ViewInject.toast(this,R.string.bot_txt_9);
                    return;
                }
                if (StringUtils.isEmpty(tet_job.getText().toString())){
                    ViewInject.toast(this,R.string.bot_txt_10);
                    return;
                }
                if (StringUtils.isEmpty(bot_site.getText().toString())){
                    ViewInject.toast(this,R.string.bot_txt_11);
                    return;
                }

                if (StringUtils.isEmpty(getCh())){
                    ViewInject.toast(this,R.string.bot_txt_12);
                    return;
                }
                if (StringUtils.isEmpty(bot_abstract.getText().toString())){
                    ViewInject.toast(this,R.string.bot_txt_13);
                    return;
                }
                if (StringUtils.isEmpty(outputPath)){
                    postBotData();
                }else {
                    updateUserImg();
                }
                break;
        }
    }
    public void getRequstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("id",id);
        params.put("op_customer_id",user.getCustomer_id());
        params.put("customer_id",user.getCustomer_id());
        apiImp.getBotMy(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity= JSON.parseObject(data,BotEntity.class);
                setLayout();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void setLayout(){
        photo=entity.getLogo();
        if (!StringUtils.isEmpty(photo)){
            Glide.with(this).load(photo).into(bot_photo);
        }
        bot_title.setText(entity.getTitle());
        tet_job.setText(entity.getIndustry());
        bot_site.setText(entity.getCompany_address());
        bot_abstract.setText(entity.getIntro());
        if (entity.getFacing_problems().contains(getString(R.string.bot_txt_14))){
            rg_sel_1.setChecked(true);
        }
        if (entity.getFacing_problems().contains(getString(R.string.bot_txt_15))){
            rg_sel_2.setChecked(true);
        }
        if (entity.getFacing_problems().contains(getString(R.string.bot_txt_16))){
            rg_sel_3.setChecked(true);
        }
    }
    public void postBotData(){
        if (!StringUtils.isEmpty(id)){
            updataeDat();
        }else {

            Map<String,Object> params=new HashMap<>();
            params.put("customer_id",user.getCustomer_id());
            if (StringUtils.isNotEmpty(photo))
                params.put("logo",photo);
            params.put("title",bot_title.getText().toString());
            params.put("industry",tet_job.getText().toString());
            params.put("company_address",bot_site.getText().toString());
            params.put("facing_problems",getCh());
            params.put("intro",bot_abstract.getText().toString());
            apiImp.addBot(params, this, new DataIdCallback<String>() {
                @Override
                public void onSuccess(String data, int id) {
                    setResult(RESULT_OK);
                    finish();
                    startActivity(new Intent(getBaseContext(),BotSucceedActivity.class));
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {
                    if (dialog!=null)
                        dialog.dismiss();
                }
            });
        }
    }
    public void updataeDat(){
        Map<String,Object> params=new HashMap<>();
        params.put("id",id);
        params.put("customer_id",user.getCustomer_id());
        if (StringUtils.isNotEmpty(photo))
            params.put("logo",photo);
        params.put("title",bot_title.getText().toString());
        params.put("industry",tet_job.getText().toString());
        params.put("company_address",bot_site.getText().toString());
        params.put("facing_problems",getCh());
        params.put("intro",bot_abstract.getText().toString());
        apiImp.updateBot(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
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
                    postBotData();
                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                    if (dialog != null)
                        dialog.dismiss();
                    ViewInject.shortToast(getApplication(), errMsg);
                }
            });
        } else {
            postBotData();
        }
    }

    public String getCh(){
        String company="";
        if (rg_sel_1.isChecked()){
            company=rg_sel_1.getText().toString();
        }
        if (rg_sel_2.isChecked()){
            if (StringUtils.isEmpty(company)){
                company=rg_sel_2.getText().toString();
            }else {
                company+=","+rg_sel_2.getText().toString();
            }
        }
        if (rg_sel_3.isChecked()){
            if (StringUtils.isEmpty(company))
                company=rg_sel_3.getText().toString();
            else
                company+=","+rg_sel_3.getText().toString();
        }
        return company;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case OPENPIC://相册选择照片返回
                if (resultCode == RESULT_OK) {
                    Glide.with(mContext).load(outputPath).asBitmap().placeholder(R.mipmap.ic_default).into(bot_photo);
                    return;
                }
                break;
            case OPENCAM://拍完照返回
                if (resultCode == RESULT_OK) {//outputPath
                    Glide.with(mContext).load(outputPath).asBitmap().placeholder(R.mipmap.ic_default).into(bot_photo);
                    return;
                }
                break;
            case 114:
//                tet_job.setText(data.getStringExtra("job"));
                break;
        }
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType() == BusEnum.JOBS){
            tet_job.setText(event.getData());
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
        mPoupWindow.showAtLocation(bot_layout, Gravity.BOTTOM, 0, 0);
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
}
