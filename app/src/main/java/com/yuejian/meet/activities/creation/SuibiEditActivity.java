package com.yuejian.meet.activities.creation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.EditSuibiImageListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.ui.HorizontalLineItemDecoration;
import com.yuejian.meet.utils.BitmapLoader;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/05/22 14:10
 * @desc : 发布 - 编辑随笔
 */
public class SuibiEditActivity extends BaseActivity implements EditSuibiImageListAdapter.OnEditSuibiImageListItemClickListener {

    @Bind(R.id.iv_edit_suibi_back_btn)
    ImageView mBackBtn;
    @Bind(R.id.lv_edit_suibi_image_list)
    RecyclerView mImageListView;
    @Bind(R.id.iv_edit_suibi_add_image)
    ImageView mAddImageBtn;
    @Bind(R.id.et_edit_suibi_title)
    EditText mEditTitleText;
    @Bind(R.id.et_edit_suibi_content)
    EditText mEditContentText;
    @Bind(R.id.tv_edit_suibi_tag_text)
    TextView mTagTextView;
    @Bind(R.id.cl_edit_suibi_select_tag)
    ConstraintLayout mSelectTagBtn;
    @Bind(R.id.sw_edit_suibi_allow_trans)
    Switch mAllowTransSwitch;
    @Bind(R.id.sw_edit_suibi_allow_comment)
    Switch mAllowCommentSwitch;
    @Bind(R.id.btn_edit_suibi_preview_btn)
    Button mPreviewBtn;
    @Bind(R.id.tv_edit_suibi_submit)
    TextView mSubmitView;

    private static final int SELECTTAG = 10;
    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    private static final int CROP_PIC = 13;

    private List<PhotoInfo> mPhotoList = new ArrayList<>();
    private String outputPath = "";
    private EditSuibiImageListAdapter mImageListAdapter;
    private LoadingDialogFragment mLoadingDialog;

    private Boolean isCompile = false;
    private String mTag = "";
    private int mTagId = -1;

    private JSONArray imgUrlJsonArray;
    private int pudateCnt = 0;
    private boolean mTitleBoolean;
    private boolean mContentBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suibi_edit);

        initEditTextListener();
        initData();
    }

    /**
     * 监听输入框内容变化
     */
    private void initEditTextListener() {
        MyTextChange myTextChange = new MyTextChange();
        mEditTitleText.addTextChangedListener(myTextChange);
        mEditContentText.addTextChangedListener(myTextChange);
    }

    private void initData() {
        mLoadingDialog = LoadingDialogFragment.newInstance(getString(R.string.Is_released));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mImageListAdapter = new EditSuibiImageListAdapter(this, mPhotoList, this);
        HorizontalLineItemDecoration itemDecoration = new HorizontalLineItemDecoration(20);
        mImageListView.setLayoutManager(layoutManager);
        mImageListView.setAdapter(mImageListAdapter);
        mImageListView.addItemDecoration(itemDecoration);

        checkSubmitBtn();
    }

    @OnClick({R.id.iv_edit_suibi_back_btn, R.id.iv_edit_suibi_add_image, R.id.cl_edit_suibi_select_tag,
            R.id.btn_edit_suibi_preview_btn, R.id.tv_edit_suibi_submit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_edit_suibi_back_btn:
                finish();
                break;
            case R.id.iv_edit_suibi_add_image:
                //打开图片选择器
                showSelector(true);
                break;
            case R.id.cl_edit_suibi_select_tag:
                Intent toSelectTagIntent = new Intent(this, SuibiSelectTagActivity.class);
                toSelectTagIntent.putExtra("tagId", mTagId);
                startActivityForResult(toSelectTagIntent, SELECTTAG);
                break;
            case R.id.btn_edit_suibi_preview_btn:
                Intent previewIntent = new Intent(this, SuibiPreviewActivity.class);
                previewIntent.putExtra("title", mEditTitleText.getText().toString());
                previewIntent.putExtra("content", mEditContentText.getText().toString());
                previewIntent.putExtra("label_id", String.valueOf(mTagId));
                previewIntent.putExtra("label_text", mTag);
                previewIntent.putExtra("photo_list", (Serializable) mPhotoList);
                previewIntent.putExtra("is_reprint", String.valueOf(mAllowTransSwitch.isChecked() ? 1 : 0));
                previewIntent.putExtra("is_comment", String.valueOf(mAllowCommentSwitch.isChecked() ? 1 : 0));
                startActivity(previewIntent);
                break;
            case R.id.tv_edit_suibi_submit:
                if (mPreviewBtn.isEnabled()) {
                    imgUrlJsonArray = new JSONArray();
                    if (mLoadingDialog != null)
                        mLoadingDialog.show(getFragmentManager(), "");
                    if (mPhotoList.size() > 0) { //上传图片
                        for (int i = 0; i < mPhotoList.size(); i++) {
                            updateUrl(i, mPhotoList.get(i).getAbsolutePath());
                        }
                    } else {
                        releaseSuibi();
                    }
                } else {
                    ViewInject.shortToast(getApplication(), "请填写完整信息");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 打开图片选择器
     */
    private void showSelector(Boolean isOpen) {
        if (mPhotoList.size() >= 9) {
            ViewInject.shortToast(getApplication(), "最多只能选择9张图片");
            return;
        }
        outputPath = ImgUtils.imgTempFile();
        int from = PickImageActivity.FROM_LOCAL;
        if (isOpen) {
            PickImageActivity.start(this, OPENPIC, from, outputPath, true,
                    9 - mPhotoList.size(), true, false, 0, 0);//不截图
        } else {
            from = PickImageActivity.FROM_CAMERA;
            PickImageActivity.start(this, OPENCAM, from, outputPath, false, 1,
                    true, false, 0, 0);//不截图
        }
    }

    /**
     * 删除图片
     * @param pos
     */
    public void deletePhotos(int pos) {
        mPhotoList.remove(pos);
        mImageListAdapter.refresh(mPhotoList);

        checkSubmitBtn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case OPENPIC: // 相册选择照片返回
                if (resultCode == RESULT_OK) {
                    try {
                        List<PhotoInfo> photosInfo = PickerContract.getPhotos(data);
                        if (photosInfo.size() > 0) {
                            mPhotoList.addAll(photosInfo);
                            mImageListAdapter.notifyDataSetChanged();
                        }
                        isCompile = true;
                    } catch (Exception e) {
                        ViewInject.shortToast(this, "图片出错");
                    }
                    mImageListView.scrollToPosition(mPhotoList.size());
                }
                break;
            case OPENCAM: // 相机拍照返回
                break;
            case SELECTTAG: // 选择Tag
                if (resultCode == RESULT_OK) {
                    mTag = data.getStringExtra("tag");
                    mTagId = data.getIntExtra("tagId", -1);
                    if (mTagId == -1) {
                        mTagTextView.setVisibility(View.INVISIBLE);
                    } else {
                        mTagTextView.setVisibility(View.VISIBLE);
                        mTagTextView.setText(mTag);
                    }
                }
                break;
        }

        checkSubmitBtn();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!TextUtils.isEmpty(mEditTitleText.getText().toString()) || !TextUtils.isEmpty(mEditContentText.getText().toString())) {
            // TODO 弹窗提示
        }
    }

    /**
     * 照片列表点击监听
     * @param type 0:添加  1:照片
     */
    @Override
    public void onImageItemClick(int type) {
        if (type == 0) {
            showSelector(true);
        }
    }

    /**
     * 检查“发布”和“预览”按钮的状态
     */
    private void checkSubmitBtn() {
        if (mTitleBoolean && mContentBoolean &&
                mPhotoList != null && mPhotoList.size() > 0 && mTagId != -1) {
            mPreviewBtn.setEnabled(true);
        } else {
            mPreviewBtn.setEnabled(false);
        }
    }

    /**
     * 更新已上传图片url串
     * @param index 顺序
     * @param url 本地图片路径
     */
    public void updateUrl(int index, String url) {
        String updateUrl = "";
        String bitmap2File = url;
        if (BitmapLoader.isHorizontal(bitmap2File)) {//横屏
            updateUrl = OssUtils.getTimeNmaeJpgHorizontal();
        } else {
            updateUrl = OssUtils.getTimeNmaeJpg();
        }
        if (!BitmapLoader.verifyPictureSize(bitmap2File)) {
            Bitmap bitmapFromFile = BitmapLoader.getBitmapFromFile(bitmap2File, 720, 1280);
            bitmap2File = BitmapLoader.saveMyBitmap(OssUtils.saveJpg(), bitmapFromFile, this);
        }

        JSONObject object = new JSONObject();
        object.put("index", index);
        object.put("url", OssUtils.getOssUploadingUrl(updateUrl));
        imgUrlJsonArray.add(object);
        updateUserImg(bitmap2File, updateUrl);
    }

    /**
     * 上传视频跟图片到oss
     */
    public void updateUserImg(String path, String ossUpdateUrl) {
        if (!path.equals("")) {
            new FeedsApiImpl().upLoadImageFileToOSS(path, ossUpdateUrl, this, new DataCallback<FeedsResourceBean>() {
                @Override
                public void onSuccess(FeedsResourceBean data) {
                    if (mPhotoList.size() > 0) {
                        pudateCnt += 1;
                        if (mPhotoList.size() == pudateCnt) {
                            releaseSuibi();
                        }
                    }
                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                    if (mLoadingDialog != null)
                        mLoadingDialog.dismiss();
                    ViewInject.shortToast(getApplication(), errMsg);
                }
            });
        }
    }

    /**
     * 发布随笔
     */
    private void releaseSuibi() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("title", mEditTitleText.getText().toString());
        params.put("content", mEditContentText.getText().toString());
        params.put("photo_and_video_url", imgUrlJsonArray.toJSONString());
        params.put("label_id", String.valueOf(mTagId));
        params.put("is_reprint", String.valueOf(mAllowTransSwitch.isChecked() ? 1 : 0));
        params.put("is_comment", String.valueOf(mAllowCommentSwitch.isChecked() ? 1 : 0));

        apiImp.insertEssay(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                ViewInject.shortToast(getApplicationContext(), R.string.release_success);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                ViewInject.shortToast(getApplicationContext(), "发布失败");
            }
        });
    }

    class MyTextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mTitleBoolean = mEditTitleText.getText().length() > 0;
            mContentBoolean = mEditContentText.getText().length() > 0;

            checkSubmitBtn();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
