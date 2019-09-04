package com.yuejian.meet.activities.clan;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.services.core.PoiItem;
import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.zuci.ZuciNearbyActivity;
import com.yuejian.meet.adapters.ReleaseActionAdapter2;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.bean.ClanMiAllEntity;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.BitmapLoader;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.InnerGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.OptionPicker.OnOptionPickListener;

/**
 * 建立宗亲会  编辑资料
 */
public class CreateMiClanActivity extends BaseActivity implements OnItemClickListener {
    private static final int OPENCAM = 12;
    private static final int OPENPIC = 11;
    private static final int SELECT_BG = 1005;
    private static final int SELECT_PHOTO = 1004;
    String area;
    private String bgPath = "";
    String city;
    String clanId;
    @Bind(R.id.mi_clan_binding_zuci)
    TextView mi_clan_binding_zuci;
    @Bind(R.id.create_clan_photo)
    ImageView create_clan_photo;
    LoadingDialogFragment dialog;
    Dialog dialogs;
    InnerGridView gridView;
    String imgUrlArr = "";
    Intent intent;
    String lat;
    String lng;
    ReleaseActionAdapter2 mAdpter2;
    @Bind(R.id.mi_clan_about)
    EditText mi_clan_about;
    @Bind(R.id.mi_clan_address)
    TextView mi_clan_address;
    @Bind(R.id.mi_clan_hall_name)
    TextView mi_clan_hall_name;
    @Bind(R.id.mi_clan_intrant_about)
    EditText mi_clan_intrant_about;
    @Bind(R.id.mi_clan_name)
    TextView mi_clan_name;
    @Bind(R.id.mi_clan_surname)
    TextView mi_clan_surname;
    @Bind(R.id.mi_clan_time)
    TextView mi_clan_time;
    String outputPath = "";
    private String photoPath = "";
    @Bind(R.id.photo_layout)
    LinearLayout photo_layout;
    List<PhotoInfo> photosList = new ArrayList();
    String province;
    int pudateCnt = 0;
    private int selectPhotoType = -1;
    @Bind(R.id.submit)
    Button submit;
    String titleImag = "";
    @Bind(R.id.txt_titlebar_save)
    TextView txt_titlebar_save;
    MoreClanEntity moreClanEntity;
    String hall_id="",hall_name="";

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_create_mi_clan);
        this.mi_clan_surname.setText(this.user.getSurname());
        initData();
        if (getIntent().hasExtra("clanId")) {
            this.clanId = getIntent().getStringExtra("clanId");
            requstData();
        }
        if (!StringUtil.isEmpty(this.clanId)) {
            this.submit.setVisibility(View.GONE);
//            this.submit.setBackgroundResource(2130838000);
            setTitleText(getString(R.string.Edit_data));
            return;
        }
        setTitleText(getString(R.string.careate_clan));
    }

    private void selectAlbums(Boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3) {
        outputPath=ImgUtils.imgTempFile();
        if (paramBoolean.booleanValue()) {
            PickImageActivity.start(this, 15, 1, outputPath, false, 1, false, true, paramInt2, paramInt3);
            return;
        }
        PickImageActivity.start(this, 16, 2, outputPath, false, 1, false, true, paramInt2, paramInt3);
    }

    private void showMoreSelection() {
        OptionPicker localOptionPicker = new OptionPicker(this, new String[] { getString(R.string.mobile_phone_photographing),getString(R.string.Choose_from_the_album)});
        localOptionPicker.setTitleText(R.string.hint);
        localOptionPicker.setTextSize(14);
        localOptionPicker.setOnOptionPickListener(new OnOptionPickListener() {
            public void onOptionPicked(int paramAnonymousInt, String paramAnonymousString) {
                paramAnonymousInt = DensityUtils.getScreenW(getBaseContext())-DensityUtils.dip2px(getBaseContext(),60);
                int i = paramAnonymousInt * 264 / 375;
                if (getString(R.string.mobile_phone_photographing).equals(paramAnonymousString)) {
                    if (selectPhotoType == 1004) {
                        selectAlbums(Boolean.valueOf(false),OPENPIC, paramAnonymousInt, i);
                        return;
                    }
                }
                if (getString(R.string.Choose_from_the_album).equals(paramAnonymousString)) {
                    paramAnonymousInt = DensityUtils.getScreenW(getBaseContext());
                    selectAlbums(true,selectPhotoType, 720, i);
                    return;
                }
//                if (selectPhotoType == 1004) {
//                    selectAlbums(Boolean.valueOf(true), selectPhotoType, 720, 720);
//                    return;
//                }
//                paramAnonymousInt = DensityUtils.getScreenW(getBaseContext());
//                selectAlbums(Boolean.valueOf(true),selectPhotoType, paramAnonymousInt, i);
            }
        });
        localOptionPicker.show();
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

    public void PostData() {
        if (this.dialog != null) {
            this.dialog.show(getFragmentManager(), "");
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_name", this.mi_clan_name.getText().toString());
        localHashMap.put("association_surname", this.user.getSurname());
        localHashMap.put("association_hall_name", this.mi_clan_hall_name.getText().toString());
        localHashMap.put("association_img", this.titleImag);
        localHashMap.put("association_province", this.province);
        localHashMap.put("association_city", this.city);
        localHashMap.put("association_area", this.area);
        localHashMap.put("association_address", this.mi_clan_address.getText().toString());
        localHashMap.put("association_lng", this.lng);
        localHashMap.put("association_lat", this.lat);
        localHashMap.put("association_found_date", this.mi_clan_time.getText().toString());
        localHashMap.put("association_about", this.mi_clan_about.getText().toString());
        localHashMap.put("association_intrant_about", this.mi_clan_intrant_about.getText().toString());
        localHashMap.put("customer_id", this.user.getCustomer_id());
        localHashMap.put("hall_id", hall_id);
        localHashMap.put("photos", this.imgUrlArr);
        this.apiImp.CreateClanAssciation(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                if (CreateMiClanActivity.this.dialog != null) {
                    CreateMiClanActivity.this.dialog.dismiss();
                }
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                intent = new Intent(CreateMiClanActivity.this.getApplication(), CreateClanSucceedActivity.class);
                intent.putExtra("clanId", paramAnonymousString);
                intent.putExtra("clanName", CreateMiClanActivity.this.mi_clan_name.getText().toString());
                startActivityForResult(CreateMiClanActivity.this.intent, 110);
                BusCallEntity busCallEntity = new BusCallEntity();
                busCallEntity.setCallType(BusEnum.CLAN_UPDATE);
                Bus.getDefault().post(busCallEntity);
                CreateMiClanActivity.this.finish();
            }
        });
    }

    public void delPhotos(int paramInt) {
        this.photosList.remove(paramInt);
        this.mAdpter2.refresh(this.photosList);
    }

    public void deleteClan() {
        Builder localBuilder = new Builder(this);
        localBuilder.setTitle(R.string.hint);
        localBuilder.setMessage(R.string.Family_album3);
        localBuilder.setNegativeButton(R.string.confirm, new OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();
                Map<String,Object> params = new HashMap();
                params.put("association_id", CreateMiClanActivity.this.clanId);
                CreateMiClanActivity.this.apiImp.clanDelete(params, this, new DataIdCallback<String>() {
                    public void onFailed(String paramAnonymous2String1, String paramAnonymous2String2, int paramAnonymous2Int) {}

                    public void onSuccess(String paramAnonymous2String, int paramAnonymous2Int) {
                        CreateMiClanActivity.this.setResult(RESULT_OK, new Intent());
                        BusCallEntity busCallEntity = new BusCallEntity();
                        busCallEntity.setCallType(BusEnum.CLAN_UPDATE);
                        Bus.getDefault().post(busCallEntity);
                        CreateMiClanActivity.this.finish();
                    }
                });
            }
        });
        localBuilder.setNeutralButton(R.string.cancel, null);
        localBuilder.show();
    }

    public void initData() {
        this.gridView = ((InnerGridView)findViewById(R.id.mi_clan_grid));
        this.dialog = LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        this.mAdpter2 = new ReleaseActionAdapter2(this, this.photosList);
        this.gridView.setAdapter(this.mAdpter2);
        this.mAdpter2.notifyDataSetChanged();
        this.gridView.setOnItemClickListener(this);
    }

    public void inputDialog(final String paramString1, String paramString2) {
        Builder localBuilder = new Builder(this);
        View localView = View.inflate(this, R.layout.dialog_mi_clan_input_layout, null);
        ((TextView)localView.findViewById(R.id.dialog_title)).setText(paramString1);
        final EditText localEditText = (EditText)localView.findViewById(R.id.dialog_content);
        localEditText.setHint(getString(R.string.please_enter)+ paramString1);
        localEditText.setText(paramString2);
        localBuilder.setView(localView);
        localView.findViewById(R.id.dialog_submit).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                CreateMiClanActivity.this.dialogs.dismiss();
                if (paramString1.equals(getString(R.string.Tong_he_tong))) {
                    CreateMiClanActivity.this.mi_clan_hall_name.setText(localEditText.getText().toString().trim());
                    return;
                }
                if (paramString1.equals(getString(R.string.date_of_establishment))) {
                    CreateMiClanActivity.this.mi_clan_time.setText(localEditText.getText().toString().trim());
                    return;
                }
                if (paramString1.equals(getString(R.string.Clan_name))){
                    CreateMiClanActivity.this.mi_clan_name.setText(localEditText.getText().toString().trim());
                }
            }
        });
        localView.findViewById(R.id.dialog_close).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                CreateMiClanActivity.this.dialogs.dismiss();
            }
        });
        this.dialogs = localBuilder.show();
        this.dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialogs.show();
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent data) {
        super.onActivityResult(paramInt1, paramInt2, data);
        if (paramInt2 == RESULT_OK) {
            switch (paramInt1){
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
                case 1005:
                    String[] arrayOfString = data.getStringExtra("LatLonPoint").split(",");
                    this.lng = arrayOfString[0];
                    this.lat = arrayOfString[1];
                    this.province = data.getStringExtra("ProvinceName");
                    this.city = data.getStringExtra("CityName");
                    this.mi_clan_address.setText(data.getStringExtra("locationTitle"));
                    break;
                case 15:
                    this.bgPath = outputPath;
                    Glide.with(this).load(bgPath).into(this.create_clan_photo);
                    this.selectPhotoType = -1;
                    break;
                case 16:
                    this.bgPath = outputPath;
                    Glide.with(this).load(bgPath).into(this.create_clan_photo);
                    this.selectPhotoType = -1;
                    break;
            }
        }
    }

    public void onBusCallback(BusCallEntity paramBusCallEntity) {
        super.onBusCallback(paramBusCallEntity);
        if (paramBusCallEntity.getCallType() == BusEnum.CLAN_LOCATION) {
            PoiItem poiItem = (PoiItem)paramBusCallEntity.getObject();
            String[] arrayOfString = poiItem.getLatLonPoint().toString().split(",");
            this.lat = arrayOfString[0];
            this.lng = arrayOfString[1];
            this.province = poiItem.getProvinceName();
            this.city = poiItem.getCityName();
            this.mi_clan_address.setText(poiItem.getTitle());
            this.area = poiItem.getAdName();
        }else if (paramBusCallEntity.getCallType() == BusEnum.ZUCI_SELECT){
            hall_id=paramBusCallEntity.getId();
            hall_name=paramBusCallEntity.getData();
            mi_clan_binding_zuci.setText(hall_name);
        }
    }

    @OnClick({R.id.create_clan_name_layout, R.id.mi_clan_hall_name_layout, R.id.mi_clan_time_layout, R.id.mi_clan_address_layout, R.id.submit, R.id.create_clan_bg_layout,
            R.id.txt_titlebar_save,R.id.crate_mi_mi_zuci_binding_layout})
    public void onClick(View paramView){
        switch (paramView.getId()){
            case R.id.crate_mi_mi_zuci_binding_layout://选择要绑定的祖祠
                intent = new Intent(this, ZuciNearbyActivity.class);
//                if (null == moreClanEntity){
                    intent.putExtra("bindingId",hall_id);
//                }else {
//                    intent.putExtra("bindingId",moreClanEntity.getHall_id());
//                }
                intent.putExtra("isBanin",true);
                startActivity(intent);
                break;
            case R.id.submit:
                if (!StringUtil.isEmpty(this.clanId)) {
                    deleteClan();
                    return;
                }
                if (StringUtil.isEmpty(this.bgPath)) {
                    ViewInject.toast(this, R.string.select_Clan_background);
                    return;
                }
                if (StringUtil.isEmpty(this.mi_clan_hall_name.getText().toString())) {
                    ViewInject.toast(this, R.string.input_Tong_he_tong);
                    return;
                }
                if (StringUtil.isEmpty(this.mi_clan_time.getText().toString())) {
                    ViewInject.toast(this, R.string.input_date_of_establishment);
                    return;
                }
                if (StringUtil.isEmpty(this.mi_clan_name.getText().toString())) {
                    ViewInject.toast(this, R.string.input_Clan_name);
                    return;
                }
                this.pudateCnt = 0;
                if (this.dialog != null) {
                    this.dialog.show(getFragmentManager(), "");
                }
                updateUrl(this.bgPath, Boolean.valueOf(true));
                int i = 0;
                while (i < this.photosList.size()) {
                    updateUrl(((PhotoInfo)this.photosList.get(i)).getAbsolutePath(), false);
                    i += 1;
                }
            case R.id.create_clan_bg_layout:
                this.selectPhotoType = 1004;
                showMoreSelection();
                return;
            case R.id.mi_clan_hall_name_layout:
                inputDialog(getString(R.string.Tong_he_tong), this.mi_clan_hall_name.getText().toString());
                return;
            case R.id.mi_clan_time_layout:
                inputDialog(getString(R.string.date_of_establishment), this.mi_clan_time.getText().toString());
                return;
            case R.id.create_clan_name_layout:
                inputDialog(getString(R.string.Clan_name), this.mi_clan_name.getText().toString());
                return;
            case R.id.mi_clan_address_layout:
                this.intent = new Intent(this, ClanLocationActivity.class);
                startActivityIfNeeded(this.intent, 100);
                return;
            case R.id.txt_titlebar_save:

                if (StringUtil.isEmpty(this.mi_clan_hall_name.getText().toString())) {
                    ViewInject.toast(this, R.string.input_Tong_he_tong);
                    return;
                }
                if (StringUtil.isEmpty(this.mi_clan_time.getText().toString())) {

                    ViewInject.toast(this, R.string.input_date_of_establishment);
                    return;
                }
                if (StringUtil.isEmpty(this.mi_clan_name.getText().toString())) {
                    ViewInject.toast(this, R.string.input_Clan_name);
                    return;
                }
                if (this.dialog != null) {
                    this.dialog.show(getFragmentManager(), "");
                }
                if (StringUtil.isEmpty(this.bgPath)) {
                    saveClan();
                    return;
                }
                updateUrl(this.bgPath, Boolean.valueOf(true));
                break;
        }
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        if (paramInt == this.photosList.size()) {
            showSelector(Boolean.valueOf(true));
        }
    }

    public void requstData() {
        this.txt_titlebar_save.setVisibility(View.VISIBLE);
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", this.clanId);
        localHashMap.put("customer_id", this.user.getCustomer_id());
        this.apiImp.findClanInfo(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                CreateMiClanActivity.this.finish();
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                if (CreateMiClanActivity.this.dialog != null) {
                    CreateMiClanActivity.this.dialog.dismiss();
                }
                ClanMiAllEntity clanMiAllEntity = JSON.parseObject(paramAnonymousString, ClanMiAllEntity.class);
                CreateMiClanActivity.this.setLayout(clanMiAllEntity);
            }
        });
    }

    public void saveClan() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", this.clanId);
        localHashMap.put("association_name", this.mi_clan_name.getText().toString());
        localHashMap.put("association_surname", this.user.getSurname());
        localHashMap.put("association_hall_name", this.mi_clan_hall_name.getText().toString());
        localHashMap.put("association_img", this.titleImag);
        localHashMap.put("association_address", this.mi_clan_address.getText().toString());
        localHashMap.put("association_found_date", this.mi_clan_time.getText().toString());
        localHashMap.put("association_province", this.province);
        localHashMap.put("association_city", this.city);
        localHashMap.put("association_area", this.area);
        localHashMap.put("association_address", this.mi_clan_address.getText().toString());
        localHashMap.put("association_lng", this.lng);
        localHashMap.put("association_lat", this.lat);
        localHashMap.put("association_about", this.mi_clan_about.getText().toString());
        localHashMap.put("association_intrant_about", this.mi_clan_intrant_about.getText().toString());
        localHashMap.put("hall_id", hall_id);
        this.apiImp.clanUpdate(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                if (CreateMiClanActivity.this.dialog != null) {
                    CreateMiClanActivity.this.dialog.dismiss();
                }
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ViewInject.toast(CreateMiClanActivity.this.getApplication(), R.string.modify_successfully);
                CreateMiClanActivity.this.setResult(RESULT_OK, new Intent());
                BusCallEntity busCallEntity = new BusCallEntity();
                busCallEntity.setCallType(BusEnum.CLAN_UPDATE);
                Bus.getDefault().post(busCallEntity);
                CreateMiClanActivity.this.finish();
            }
        });
    }

    public void setLayout(ClanMiAllEntity paramClanMiAllEntity) {
        this.photo_layout.setVisibility(View.GONE);
        this.submit.setText(R.string.Family_album4);
        this.submit.setTextColor(Color.parseColor("#333333"));
        moreClanEntity = JSON.parseObject(paramClanMiAllEntity.getClanAssociation(), MoreClanEntity.class);
        this.titleImag = moreClanEntity.getAssociation_img();
        this.mi_clan_name.setText(moreClanEntity.getAssociation_name());
        this.mi_clan_hall_name.setText(moreClanEntity.getAssociation_hall_name());
        this.mi_clan_address.setText(moreClanEntity.getAssociation_address());
        this.mi_clan_time.setText(moreClanEntity.getAssociation_found_date());
        this.mi_clan_about.setText(moreClanEntity.getAssociation_about());
        this.mi_clan_intrant_about.setText(moreClanEntity.getAssociation_intrant_about());
        this.province = moreClanEntity.getAssociation_province();
        if (!StringUtil.isEmpty(moreClanEntity.getHall_id())){
            hall_id=moreClanEntity.getHall_id();
            mi_clan_binding_zuci.setText(moreClanEntity.getHall_name());
        }
        this.city = moreClanEntity.getAssociation_city();
        this.area = moreClanEntity.getAssociation_area();
        this.lat = moreClanEntity.getAssociation_lat();
        this.lng = moreClanEntity.getAssociation_lng();
        Glide.with(this).load(this.titleImag).error(R.mipmap.ic_default).into(this.create_clan_photo);
    }

    public void updateUrl(String paramString, Boolean paramBoolean) {
        String updateUrl = "";
        String bitmap2File = paramString;
        if (BitmapLoader.isHorizontal(bitmap2File)) {//横屏
            updateUrl = OssUtils.getTimeNmaeJpgHorizontal();
        } else {
            updateUrl = OssUtils.getTimeNmaeJpg();
        }
        if (!BitmapLoader.verifyPictureSize(bitmap2File)) {
            Bitmap bitmapFromFile = BitmapLoader.getBitmapFromFile(bitmap2File, 720, 1280);
            bitmap2File = BitmapLoader.saveMyBitmap(OssUtils.saveJpg(), bitmapFromFile, this);
        }
        if (paramBoolean){
            this.titleImag = OssUtils.getOssUploadingUrl(updateUrl);
        }else {
            if (this.imgUrlArr.equals("")) {
                this.imgUrlArr += OssUtils.getOssUploadingUrl(updateUrl);
            }else {
                imgUrlArr = (this.imgUrlArr + "," + OssUtils.getOssUploadingUrl(updateUrl));
            }
        }
        updateUserImg(updateUrl, updateUrl);
    }

    public void updateUserImg(String paramString1, String paramString2) {
        if (!paramString1.equals("")) {
            new FeedsApiImpl().upLoadImageFileToOSS(paramString1, paramString2, this, new DataCallback<FeedsResourceBean>()
            {
                public void onFailed(String paramAnonymousString1, String paramAnonymousString2)
                {
                    if (CreateMiClanActivity.this.dialog != null) {
                        CreateMiClanActivity.this.dialog.dismiss();
                    }
                    ViewInject.shortToast(CreateMiClanActivity.this.getApplication(), paramAnonymousString2);
                }

                public void onSuccess(FeedsResourceBean paramAnonymousFeedsResourceBean) {
                    if (!StringUtil.isEmpty(CreateMiClanActivity.this.clanId)) {
                        CreateMiClanActivity.this.saveClan();
                    }
                    if (CreateMiClanActivity.this.photosList.size() + 1 != CreateMiClanActivity.this.pudateCnt){
                        pudateCnt += 1;

                    };
                    CreateMiClanActivity.this.PostData();
                }
            });
        }
    }
}
