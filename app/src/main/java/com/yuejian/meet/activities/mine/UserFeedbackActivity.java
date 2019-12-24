package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.BugTypSelAdapter;
import com.yuejian.meet.adapters.ReleaseActionAdapter2;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.BugTypeEntity;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.BitmapLoader;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.InnerGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 用户反馈
 */
public class UserFeedbackActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    InnerGridView gridView;
    InnerGridView bugGridView;
    @Bind(R.id.feedback_content)
    EditText feedback_content;
    @Bind(R.id.user_name)
    EditText user_name;
    @Bind(R.id.user_phone)
    EditText user_phone;
    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    private static final int CROP_PIC = 13;
    LoadingDialogFragment dialog;

    List<BugTypeEntity> listBugType=new ArrayList<>();
    BugTypeEntity bugTypeEntity;
    List<PhotoInfo> photosList = new ArrayList<>();
    ReleaseActionAdapter2 mAdpter2;
    BugTypSelAdapter bugTypSelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        initView();
    }
    public void initView(){
        dialog=LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        setTitleText(getString(R.string.user_feedback_title));
        bugTypeEntity=new BugTypeEntity(getString(R.string.user_feedback_bug1),1,true);
        listBugType.add(new BugTypeEntity(getString(R.string.user_feedback_bug1),1,true));
        listBugType.add(new BugTypeEntity(getString(R.string.user_feedback_bug2),2,false));
        listBugType.add(new BugTypeEntity(getString(R.string.user_feedback_bug3),3,false));
        listBugType.add(new BugTypeEntity(getString(R.string.user_feedback_bug4),4,false));
        gridView= (InnerGridView) findViewById(R.id.photo_grid);
        bugGridView= (InnerGridView) findViewById(R.id.bug_type);
        mAdpter2=new ReleaseActionAdapter2(this, photosList);
        gridView.setAdapter(mAdpter2);
        mAdpter2.notifyDataSetChanged();
        gridView.setOnItemClickListener(this);
        bugTypSelAdapter=new BugTypSelAdapter(bugGridView,listBugType,R.layout.item_bug_type_sel_layout);
        bugGridView.setAdapter(bugTypSelAdapter);
        bugTypSelAdapter.notifyDataSetChanged();
        user_name.setText(PreferencesUtil.get(this,user.getCustomer_id()+PreferencesUtil.bug_userName,""));
        user_phone.setText(PreferencesUtil.get(this,user.getCustomer_id()+PreferencesUtil.bug_userPhone,""));
    }

    @OnClick({R.id.feedback_submit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.feedback_submit:
                if (StringUtil.isEmpty(feedback_content.getText().toString())){
                    ViewInject.toast(this,R.string.user_feedback_hint);
                    return;
                }
                if (dialog!=null)
                    dialog.show(getFragmentManager(),"");
                if (photosList.size() > 0) {//上传图片
                    for (int i = 0; i < photosList.size(); i++) {
                        updateUrl(photosList.get(i).getAbsolutePath());
                    }
                }else {
                    sutmitFeedback();

                }

                break;
        }
    }

    /**
     * 选择bug类型
     * @param entity
     * @param position
     */
    public void selBugType(BugTypeEntity entity,int position){
        bugTypeEntity=entity;
        for (int i=0;i<listBugType.size();i++){
            listBugType.get(i).setSelect(false);
        }
        listBugType.get(position).setSelect(true);
        bugTypSelAdapter.refresh(listBugType);
    }
    /**
     * 删除选择图片
     *
     * @param index
     */
    public void delPhotos(int index) {
        photosList.remove(index);
        mAdpter2.refresh(photosList);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == photosList.size()) {
            showSelector(true);
        }
    }

    String outputPath = "";
    /**
     * 打开图片选择器
     */
    private void showSelector(Boolean isOpen) {
        if (photosList.size() >= 9) {
            ViewInject.shortToast(getApplication(), R.string.photo_at_most);
            return;
        }
        outputPath = ImgUtils.imgTempFile();
        int from = PickImageActivity.FROM_LOCAL;
        if (isOpen) {
            PickImageActivity.start(this, OPENPIC, from, outputPath, true,
                    9 - photosList.size(), true, false, 0, 0);//不截图
        } else {
            from = PickImageActivity.FROM_CAMERA;
            PickImageActivity.start(this, OPENCAM, from, outputPath, false, 1,
                    true, false, 0, 0);//不截图
        }
    }
    String imgUrlArr = "";
    int pudateCnt = 0;
    //上传动态图片
    public void updateUrl(String rul) {
        String updateUrl = "";
        String bitmap2File = rul;
        if (BitmapLoader.isHorizontal(bitmap2File)) {//横屏
            updateUrl = OssUtils.getTimeNmaeJpgHorizontal();
        } else {
            updateUrl = OssUtils.getTimeNmaeJpg();
        }
        if (!BitmapLoader.verifyPictureSize(bitmap2File)) {
            Bitmap bitmapFromFile = BitmapLoader.getBitmapFromFile(bitmap2File, 720, 1280);
            bitmap2File = BitmapLoader.saveMyBitmap(OssUtils.saveJpg(), bitmapFromFile, this);
        }
        if (!imgUrlArr.equals("")) {
            imgUrlArr = imgUrlArr + "," + OssUtils.getOssUploadingUrl(updateUrl);
        } else {
            imgUrlArr += OssUtils.getOssUploadingUrl(updateUrl);
        }
        updateUserImg(bitmap2File, updateUrl);
    }
    /**
     * 上传视频跟图片到oss
     */
    public void updateUserImg(String Path, String ossUpdateUrl) {
        if (!Path.equals("")) {
            new FeedsApiImpl().upLoadImageFileToOSS(Path, ossUpdateUrl, this, new DataCallback<FeedsResourceBean>() {
                @Override
                public void onSuccess(FeedsResourceBean data) {
                    if (photosList.size() > 0) {
                        pudateCnt += 1;
                        if (photosList.size() == pudateCnt) {
                            sutmitFeedback();
                        }
                    }
                }

                @Override
                public void onSuccess(FeedsResourceBean data, int id) {

                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                    if (dialog != null)
                        dialog.dismiss();
                    ViewInject.shortToast(getApplication(), errMsg);
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }
            });
        }
    }

    /**
     * 提交反馈到服务器
     */
    public void sutmitFeedback(){
        PreferencesUtil.put(this,user.getCustomer_id()+PreferencesUtil.bug_userName,user_name.getText().toString());
        PreferencesUtil.put(this,user.getCustomer_id()+PreferencesUtil.bug_userPhone,user_phone.getText().toString());
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("feedback_question",feedback_content.getText().toString());
        params.put("feedback_imgs",imgUrlArr);
        params.put("feedback_type",""+bugTypeEntity.getType());
        params.put("feedback_tel",user_phone.getText().toString());
        params.put("customer_name",user_name.getText().toString());
        apiImp.SaveCustomerFeedack(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ViewInject.toast(getApplication(),R.string.user_feedback_hint2);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case OPENPIC:
                        if (resultCode == RESULT_OK) {
                            try {
                                List<PhotoInfo> photosInfo = PickerContract.getPhotos(data);
                                if (photosInfo.size() > 0) {
                                    photosList.addAll(photosInfo);
                                    mAdpter2.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                ViewInject.shortToast(this, "图片出错");
                            }
                            return;
                        }

                    break;
                case OPENCAM://拍完照返回
                    if (resultCode == RESULT_OK) {//outputPath
                        PhotoInfo entity = new PhotoInfo();
                        entity.setFilePath(outputPath);
                        entity.setAbsolutePath(outputPath);
                        photosList.add(entity);
                        mAdpter2.refresh(photosList);
                        return;
                    }
                    break;

            }
        }
    }
}
