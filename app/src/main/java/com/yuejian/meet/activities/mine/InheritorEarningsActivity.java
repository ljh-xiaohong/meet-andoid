package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.bean.InheritorEarningEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.BitmapLoader;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.PayResult;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.utils.WxPayOrderInfo;
import com.yuejian.meet.widgets.CircleImageView;
import com.yuejian.meet.widgets.PaymentBottomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/6/20 15:09
 * @desc : 钱包 - 传承人收益
 */
public class InheritorEarningsActivity extends BaseActivity {

    @Bind(R.id.iv_inheritor_earning_back_btn)
    ImageView mBackView;
    @Bind(R.id.inheritor_earning_user_header_img)
    CircleImageView mHeadView;
    @Bind(R.id.tv_inheritor_earning_name)
    TextView mNameView;
    @Bind(R.id.tv_inheritor_earning_id)
    TextView mIdView;
    //=========================传承人收益界面 begin===========================
    @Bind(R.id.ll_inheritor_enable)
    LinearLayout mInheritorEnableRootView;  //传承人收益界面 root view
    @Bind(R.id.tv_inheritor_earning_total_inheritor_amt)
    TextView mInheritorAmt;
    @Bind(R.id.tv_inheritor_earning_inheritor_bal)
    TextView mInheritorBal;
    @Bind(R.id.cl_inheritor_earnings_inheritor_list)
    ConstraintLayout mInheritorList;
    @Bind(R.id.tv_ccr_tjzhqy_num)
    TextView mTjzhqyNum;
    @Bind(R.id.tv_ccr_tjccr_num)
    TextView mTjccrNum;
    @Bind(R.id.tv_ccr_yhgmqy_num)
    TextView mYhgmqyNum;
    @Bind(R.id.tv_ccr_zscpbzsl_num)
    TextView mZscpbzslNum;
    @Bind(R.id.tv_ccr_zscpb_num)
    TextView mZscpbNum;
    @Bind(R.id.tv_ccr_sycpb_num)
    TextView mSycpbNum;
    @Bind(R.id.ll_inheritor_earning_light_up)
    LinearLayout lightUpIcon;
    @Bind(R.id.tv_ccr_zhsy_lj_num)
    TextView mZhsy_lj;
    @Bind(R.id.tv_ccr_zhsy_br_num)
    TextView mZhsy_br;
    @Bind(R.id.tv_ccr_fwfsy_lj_num)
    TextView mFwfsy_lj;
    @Bind(R.id.tv_ccr_fwfsy_br_num)
    TextView mFwfsy_br;
    @Bind(R.id.tv_ccr_ccrtjsy_lj_br_num)
    TextView mCcrtjsy_lj;
    @Bind(R.id.tv_ccr_ccrtjsy_br_num)
    TextView mCcrtjsy_br;
    //==========================传承人收益界面 end==========================
    //====================================================
    @Bind(R.id.ll_inheritor_disable)
    LinearLayout mInheritorDisableRootView;  //申请传承人界面 root view
    private int mInheritorStats;
    @Bind(R.id.et_ccr_invitation_code)
    EditText mInvitationCodeEditView;
    @Bind(R.id.et_ccr_name)
    EditText mNameEditView;
    @Bind(R.id.et_ccr_phone)
    EditText mPhoneEditView;
    @Bind(R.id.et_ccr_location)
    EditText mLocationEditView;
    @Bind(R.id.et_ccr_idcard)
    EditText mIdcardEditView;
    @Bind(R.id.iv_ccr_idcard_front)
    ImageView mIdcardFrontView;
    @Bind(R.id.iv_ccr_idcard_obverse)
    ImageView mIdcardObverseView;
    @Bind(R.id.btn_ccr_submit)
    Button mSubmitBtn;
    @Bind(R.id.cb_ccr_license)
    CheckBox mLicenseCheckBox;
    @Bind(R.id.tv_ccr_license)
    TextView mLicenseBtn;
    //====================================================

    private LoadingDialogFragment mLoadingDialog;
    private String idcardFrontImageUrl = "";
    private String idcardObverseImageUrl = "";

    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inheritor_earnings);

        //传承人状态   0-审核未通过 1-审核通过 2-审核中 3-非传承人
        mInheritorStats = getIntent().getIntExtra("stats", 0);
        mInheritorEnableRootView.setVisibility(mInheritorStats == 1 ? View.VISIBLE : View.GONE);
        mInheritorDisableRootView.setVisibility(mInheritorStats == 1 ? View.GONE : View.VISIBLE);
        lightUpIcon.setVisibility(mInheritorStats == 1 ? View.VISIBLE : View.GONE);

        Glide.with(InheritorEarningsActivity.this).load(user.photo).into(mHeadView);
        mNameView.setText(String.format("%s%s", user.surname, user.name));
        mIdView.setText(String.format("约见号：%s", user.customer_id));

        if (mInheritorStats == 1) {
            loadInheritorDataFromNet();
        } else { //非传承人
            initSignUpLayout();
            mLoadingDialog = LoadingDialogFragment.newInstance("正在上传...");
            //初始化微信支付
            iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        }
    }

    private void initSignUpLayout() {
        mIdcardFrontView.setOnClickListener(v -> {
            String outputPath = ImgUtils.imgTempFile();
            int from = PickImageActivity.FROM_LOCAL;
            PickImageActivity.start(this, 100, from, outputPath, true, 1,
                    true, false, 0, 0);
        });

        mIdcardObverseView.setOnClickListener(v -> {
            String outputPath = ImgUtils.imgTempFile();
            int from = PickImageActivity.FROM_LOCAL;
            PickImageActivity.start(this, 200, from, outputPath, true, 1,
                    true, false, 0, 0);
        });

        mSubmitBtn.setOnClickListener(v -> checkDataAndSubmit());

        mLicenseCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
        });

        mLicenseBtn.setOnClickListener(v -> {

        });
    }

    @OnClick({R.id.iv_inheritor_earning_back_btn, R.id.cl_inheritor_earnings_inheritor_list,R.id.tv_ccr_license})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_inheritor_earning_back_btn:
                finish();
                break;

            case R.id.cl_inheritor_earnings_inheritor_list:
                Intent intent = new Intent(this, IncomeListActivity.class);
                intent.putExtra("customer_id", user.customer_id);
                startActivity(intent);
                break;

            case R.id.tv_ccr_license:
                Intent intent1=new Intent(this, WebActivity.class);
                String url = UrlConstant.apiUrl() + "agreement/Inheritor.html";
                intent1.putExtra("url", url);
                intent1.putExtra("No_Title", true);
                break;
        }
    }

    private void loadInheritorDataFromNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        apiImp.getMyInheritorEarnings(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                InheritorEarningEntity earningEntity = JSON.parseObject(data, InheritorEarningEntity.class);
                mInheritorAmt.setText(String.format("￥%s", earningEntity.total_inheritor_amt));
                mInheritorBal.setText(String.format("￥%s", earningEntity.inheritor_bal));
                mTjzhqyNum.setText(String.format("已推荐%s名", earningEntity.count_vip));
                mTjccrNum.setText(String.format("已推荐%s人", earningEntity.inheritor_count));
                mYhgmqyNum.setText(String.format("已获得%s次", earningEntity.all_earnings_count));
                mZhsy_lj.setText(String.format("￥%s", earningEntity.account_earnings_list.getAccount_earnings()));
                mZhsy_br.setText(String.format("￥%s", earningEntity.account_earnings_list.getAccount_earnings_today()));
                mFwfsy_lj.setText(String.format("￥%s", earningEntity.service_earnings_list.getService_earnings()));
                mFwfsy_br.setText(String.format("￥%s", earningEntity.service_earnings_list.getService_earnings_today()));
                mCcrtjsy_lj.setText(String.format("￥%s", earningEntity.inheritor_earnings_list.getInheritor_earnings()));
                mCcrtjsy_br.setText(String.format("￥%s", earningEntity.inheritor_earnings_list.getInheritor_earnings_today()));
                mZscpbzslNum.setText(String.valueOf(earningEntity.all_package));
                mZscpbNum.setText(String.valueOf(earningEntity.giving_package));
                mSycpbNum.setText(String.valueOf(earningEntity.residue_package));
                // TODO table data
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void checkDataAndSubmit() {
        String incitationCode = mInvitationCodeEditView.getText().toString();
        String name = mNameEditView.getText().toString();
        String phone = mPhoneEditView.getText().toString();
        String location = mLocationEditView.getText().toString();
        String idcard = mIdcardEditView.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(location)
                || TextUtils.isEmpty(idcard) || TextUtils.isEmpty(idcardFrontImageUrl)
                || TextUtils.isEmpty(idcardObverseImageUrl)) {
            ViewInject.shortToast(this, "请填写完整信息");
            return;
        }
        if (!mLicenseCheckBox.isChecked()) {
            ViewInject.shortToast(this, "请阅读并同意《用户充值协议》");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("name", name);
        params.put("phone", phone);
        params.put("address", location);
        params.put("id_no", idcard);
        params.put("id_card_1", idcardFrontImageUrl);
        params.put("id_card_2", idcardObverseImageUrl);
        params.put("referral_code", incitationCode);
        params.put("source_type", "0");
        apiImp.applyForInheritorDo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    JSONObject object = new JSONObject(data);
                    boolean inheritor = object.getBoolean("inheritor");
                    if (inheritor) {
                        //调起支付
                        showPaymentDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(InheritorEarningsActivity.this, "申请失败，请重试");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100: //选择身份证正面
                if (resultCode == RESULT_OK) {
                    try {
                        List<PhotoInfo> photosInfos = PickerContract.getPhotos(data);
                        if (photosInfos.size() > 0) {
                            PhotoInfo photo = photosInfos.get(0);
                            // 上传图片
                            if (mLoadingDialog != null)
                                mLoadingDialog.show(getFragmentManager(), "");
                            updateIdcardImageUrl(photo.getAbsolutePath(), 100);
                            Glide.with(mContext).load(photo.getAbsolutePath()).into(mIdcardFrontView);
                        }
                    } catch (Exception e) {
                        ViewInject.shortToast(this, "图片出错");
                    }
                }
                break;
            case 200: //选择身份证反面
                if (resultCode == RESULT_OK) {
                    try {
                        List<PhotoInfo> photosInfos = PickerContract.getPhotos(data);
                        if (photosInfos.size() > 0) {
                            PhotoInfo photo = photosInfos.get(0);
                            // 上传图片
                            if (mLoadingDialog != null)
                                mLoadingDialog.show(getFragmentManager(), "");
                            updateIdcardImageUrl(photo.getAbsolutePath(), 200);
                            Glide.with(mContext).load(photo.getAbsolutePath()).into(mIdcardObverseView);
                        }
                    } catch (Exception e) {
                        ViewInject.shortToast(this, "图片出错");
                    }
                }
                break;
        }
    }

    //上传身份证照片
    private void updateIdcardImageUrl(String path, int type) {
        String updateUrl = "";
        String bitmap2File = path;
        if (BitmapLoader.isHorizontal(bitmap2File)) {//横屏
            updateUrl = OssUtils.getTimeNmaeJpgHorizontal();
        } else {
            updateUrl = OssUtils.getTimeNmaeJpg();
        }
        if (!BitmapLoader.verifyPictureSize(bitmap2File)) {
            Bitmap bitmapFromFile = BitmapLoader.getBitmapFromFile(bitmap2File, 720, 1280);
            bitmap2File = BitmapLoader.saveMyBitmap(OssUtils.saveJpg(), bitmapFromFile, this);
        }

        if (type == 100) {
            idcardFrontImageUrl = OssUtils.getOssUploadingUrl(updateUrl);
        } else if (type == 200) {
            idcardObverseImageUrl = OssUtils.getOssUploadingUrl(updateUrl);
        }

        if (!bitmap2File.equals("")) {
            new FeedsApiImpl().upLoadImageFileToOSS(bitmap2File, updateUrl, this, new DataCallback<FeedsResourceBean>() {
                @Override
                public void onSuccess(FeedsResourceBean data) {
                    if (mLoadingDialog != null)
                        mLoadingDialog.dismiss();
                    ViewInject.shortToast(getApplication(), "上传完成");
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

    //支付弹窗
    private void showPaymentDialog() {
        View.OnClickListener zhifubaoPay = v -> doInCash(1);
        View.OnClickListener wechatPay = v -> doInCash(2);
        PaymentBottomDialog dialog = new PaymentBottomDialog(this, zhifubaoPay, wechatPay);
        dialog.show();
    }

    /**
     * 充值
     *
     * @param sourceType 1：支付宝  2:微信
     */
    private void doInCash(final int sourceType) {
        Map<String, Object> params = new HashMap<>();
        //约见ID
        params.put("customer_id", user.customer_id);
        //充值平台
        params.put("source_type", String.valueOf(sourceType));
        //VIP类型(传承人与VIP支付接口共用，传承人type = 5)
        params.put("vip_type", "5");
        //类型ID(传承人与VIP支付接口共用，传承人introduce_id = 0)
        params.put("introduce_id", String.valueOf(0));
        //判断是否安装微信
        if (sourceType == 2) {
            if (!Utils.isWeixinAvilible(getApplicationContext())) {
                Toast.makeText(this, R.string.casht_text7, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //调用支付api(传承人与VIP支付接口共用)
        apiImp.doInCashVip(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (sourceType == 1) {  //支付宝支付
                    final String orderInfo = data;
                    new Thread(() -> {
                        PayTask task = new PayTask(InheritorEarningsActivity.this);
                        Map<String, String> result = task.payV2(orderInfo, true);
                        runOnUiThread(() -> {
                            PayResult payResult = new PayResult(result);
                            String resultStatus = payResult.getResultStatus();
                            if (TextUtils.equals(resultStatus, "9000")) {
                                Toast.makeText(InheritorEarningsActivity.this, R.string.payment_success, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }).start();
                } else if (sourceType == 2) {  //微信支付
                    Log.d("wxPay", data);
                    final WxPayOrderInfo orderInfo = JSON.parseObject(data, WxPayOrderInfo.class);
                    PayReq request = new PayReq();
                    request.appId = Constants.WX_APP_ID;
                    request.partnerId = Constants.WX_PARTNER_ID;
                    request.prepayId = orderInfo.prepay_id;
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr = orderInfo.nonceStr;
                    request.timeStamp = orderInfo.timeStamp;
                    request.sign = orderInfo.paySign;
                    iwxapi.sendReq(request);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
