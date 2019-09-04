package com.yuejian.meet.activities.zuci;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.clan.ClanLocationActivity;
import com.yuejian.meet.activities.clan.ClanPhotoReleaseActivity;
import com.yuejian.meet.adapters.ReleaseActionAdapter2;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.BitmapLoader;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.InnerGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class CreateZuciActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    String area;
    String city;
    LoadingDialogFragment dialog;
    String lat;
    String lng;
    @Bind(R.id.mi_zuci_about)
    EditText mi_zuci_about;
    @Bind(R.id.mi_zuci_address)
    TextView mi_zuci_address;
    String province;
    @Bind(R.id.zuci_name)
    EditText zuci_name;
    @Bind(R.id.zuci_time)
    EditText zuci_time;

    List<PhotoInfo> photosList = new ArrayList();
    ReleaseActionAdapter2 mAdpter2;
    InnerGridView gridView;
    String outputPath = "";
    String imgUrlArr = "";
    int pudateCnt = 0;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_create_zuci);
        dialog = LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        inifData();
    }

    public void inifData() {
        setTitleText(getString(R.string.add_zuci));
        this.gridView = ((InnerGridView)findViewById(R.id.mi_clan_grid));
        this.mAdpter2 = new ReleaseActionAdapter2(this, this.photosList);
        this.gridView.setAdapter(this.mAdpter2);
        this.mAdpter2.notifyDataSetChanged();
        this.gridView.setOnItemClickListener(this);
    }

    public void onBusCallback(BusCallEntity paramBusCallEntity) {
        super.onBusCallback(paramBusCallEntity);
        if (paramBusCallEntity.getCallType() == BusEnum.CLAN_LOCATION) {
            PoiItem poiItem  = (PoiItem)paramBusCallEntity.getObject();
            String[] arrayOfString = poiItem.getLatLonPoint().toString().split(",");
            this.lng = arrayOfString[0];
            this.lat = arrayOfString[1];
            this.province = poiItem.getProvinceName();
            this.city = poiItem.getCityName();
            this.area = poiItem.getAdName();
            this.mi_zuci_address.setText(poiItem.getTitle());
        }
    }

    @OnClick({R.id.zuci_submit, R.id.mi_zuci_address_layout, R.id.mi_zuci_address})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.mi_zuci_address://选择地址
                break;
            case R.id.mi_zuci_address_layout:
                startActivityIfNeeded(new Intent(this, ClanLocationActivity.class), 100);
                return;
            case R.id.zuci_submit:
                pudateCnt=0;
                imgUrlArr="";
                if (StringUtil.isEmpty(this.lng)) {
                    ViewInject.toast(this, R.string.Select_the_detailed_address);
                    return;
                }
                if (StringUtil.isEmpty(this.zuci_name.getText().toString())) {
                    ViewInject.toast(this, R.string.input_zuci_name);
                    return;
                }
                if (StringUtil.isEmpty(this.zuci_time.getText().toString())) {
                    ViewInject.toast(this, R.string.input_year_of_completion);
                    return;
                }
                if (dialog != null)
                    dialog.show(getFragmentManager(),"");
//                psotAddZuci();
                if (photosList.size()>0){
                    for (int i=0 ;i<this.photosList.size();i++) {
                        updateUrl(((PhotoInfo) this.photosList.get(i)).getAbsolutePath());
                    }
                }else {
                    psotAddZuci();
                }
                return;
        }
    }

    public void updateUrl(String paramString) {
        String updateUrl;
        String bitmap2File=paramString;
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

        updateUserImg( bitmap2File,updateUrl);
    }
    public void updateUserImg(String paramString1, String paramString2) {
        if (!paramString1.equals("")) {
            new FeedsApiImpl().upLoadImageFileToOSS(paramString1, paramString2, this, new DataCallback<FeedsResourceBean>() {

                public void onSuccess(FeedsResourceBean paramAnonymousFeedsResourceBean) {
                    if (photosList.size() > 0) {
                        pudateCnt += 1;
                        if (photosList.size() == pudateCnt) {
                            psotAddZuci();
                        }
                    }
                }
                public void onFailed(String paramAnonymousString1, String paramAnonymousString2) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    ViewInject.shortToast(getApplication(), paramAnonymousString2);
                }
            });
        }
    }

    public void psotAddZuci() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("id", "");
        localHashMap.put("surname", this.user.getSurname());
        localHashMap.put("name", this.zuci_name.getText().toString());
        localHashMap.put("years", this.zuci_time.getText().toString());
        localHashMap.put("province", this.province);
        localHashMap.put("city", this.city);
        localHashMap.put("area", this.area);
        localHashMap.put("detailed_place", this.mi_zuci_address.getText().toString());
        localHashMap.put("introduce", this.mi_zuci_about.getText().toString());
        localHashMap.put("longitude", this.lat);
        localHashMap.put("latitude", this.lng);
        localHashMap.put("images", imgUrlArr);
        this.apiImp.ZuciSave(localHashMap, this, new DataIdCallback<String>() {

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ViewInject.toast(CreateZuciActivity.this.getApplication(), R.string.Creating_successful);
                CreateZuciActivity.this.finish();
            }
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 11://相册选择照片返回
                    try {
                        List<PhotoInfo> photosInfo = PickerContract.getPhotos(data);
                        if (photosInfo.size() > 0) {
                            photosList.addAll(photosInfo);
                            mAdpter2.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        ViewInject.shortToast(this, "图片出错");
                    }

                    break;
                case 12://拍完照返回
                    PhotoInfo entity = new PhotoInfo();
                    entity.setFilePath(outputPath);
                    entity.setAbsolutePath(outputPath);
                    photosList.add(entity);
                    mAdpter2.refresh(photosList);
                    break;
            }
        }
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
    public void onItemClick(AdapterView<?> adapterView, View view, int paramInt, long l) {
        if (paramInt == this.photosList.size()) {
            showSelector(Boolean.valueOf(true));
        }
    }
    private void showSelector(Boolean paramBoolean) {
        if (this.photosList.size() >= 9) {
            ViewInject.shortToast(getApplication(), R.string.txt_more_pics);
            return;
        }
        this.outputPath = ImgUtils.imgTempFile();
        if (paramBoolean.booleanValue()) {
            PickImageActivity.start(this, 11, 1, this.outputPath, true, 9 - this.photosList.size(), true, false, 0, 0);
            return;
        }
        PickImageActivity.start(this, 12, 2, this.outputPath, false, 1, true, false, 0, 0);
    }
}
