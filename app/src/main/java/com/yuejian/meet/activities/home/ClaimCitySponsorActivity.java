package com.yuejian.meet.activities.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SelectMemberCityActivity;
import com.yuejian.meet.activities.mine.VerifyCenterActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.ClaimCitySponsorAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.ClaimCitySponsorEntity;
import com.yuejian.meet.bean.ClaimPactEntity;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.bean.RegionEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 认领城市发起人
 */
public class ClaimCitySponsorActivity extends BaseActivity implements SpringView.OnFreshListener {

    @Bind(R.id.claim_spring)
    SpringView springView;
    @Bind(R.id.claim_list)
    ListView listView;
    @Bind(R.id.hint_layout)
    LinearLayout hint_layout;
    @Bind(R.id.claim_layout)
    LinearLayout claim_layout;
    ApiImp api = new ApiImp();
    List<ClaimCitySponsorEntity> listData = new ArrayList<>();
    ClaimCitySponsorAdapter mAdapter;
    ClaimPactEntity entity;
    int pageIndex = 1;
    Intent intent;
    LoadingDialogFragment dialog;
    int is_super=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_city_sponsor);
        dialog = LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        if (getIntent().hasExtra("is_super"))
            is_super=getIntent().getIntExtra("is_super",0);
        initData();
    }

    public void initData() {
        setTitleText(getString(R.string.To_claim_that));
        hint_layout.setVisibility(View.GONE);
        if (is_super>0){
            claim_layout.setVisibility(View.GONE);
        }
//        springView.setHeader(new DefaultHeader(this));
//        springView.setFooter(new DefaultFooter(this));
//        springView.setListener(this);
//        mAdapter=new ClaimCitySponsorAdapter(listView,listData,R.layout.item_claim_city_sponsor_list);
//        listView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
//        requstData();
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(UrlConstant.getWebUrl()+"protocol.html");
    }

    @OnClick({R.id.claim_explain, R.id.claim_participation})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.claim_explain://认领说明
                intent = new Intent(getApplication(), ClaimStatementActivity.class);
                intent.putExtra("isStatus", entity.getApply_status());
                startActivity(intent);
                finish();
                break;
            case R.id.claim_participation://参与认领
                if (StringUtil.isEmpty(AppConfig.AreaName)) {
                    intent = new Intent(mContext, SelectMemberCityActivity.class);
                    if (StringUtils.isAutonomy(AppConfig.province)) {
                        intent.putExtra("city", AppConfig.province);
                    } else {
                        intent.putExtra("city", AppConfig.district);
                    }
                    intent.putExtra("isSovereignty",true);
                    startActivity(intent);
                } else {
                    getUserVerify();
//                    claimIntent();
                }

                break;
        }
    }
    public void getAreaName() {
        if (dialog != null && !dialog.isShowing) {
            dialog.show(getFragmentManager(), "");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("province", AppConfig.province);
        params.put("city", AppConfig.city);
        params.put("area", AppConfig.district);
        api.getAreaName(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                RegionEntity entity = JSON.parseObject(data, RegionEntity.class);
                AppConfig.AreaName = entity.getArea_name() == null ? "" : entity.getArea_name();
                if (dialog!=null)
                    dialog.dismiss();
                getUserVerify();
//                claimIntent();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)
                    dialog.dismiss();
            }
        });
    }
    public void getUserVerify(){
        if (dialog!=null)
            dialog.show(getFragmentManager(),"");
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        new ApiImp().findMyInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Mine mine = JSON.parseObject(data, Mine.class);
                if (mine.getIs_idcard_certified().equals("1")){
                    claimIntent();
//                    Intent intent=new Intent(mContext, InCashActivity.class);
//                    intent.putExtra("isUpVip","1");
//                    startActivity(intent);
                }else {
                    if (dialog!=null)
                        dialog.dismiss();
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setTitle(R.string.hint);
                    builder.setMessage(R.string.cultural_inheritance3);
                    builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            intent=new Intent(getApplication(), VerifyCenterActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.show();
//                    startActivity(new Intent(this, VerifyIdCardActivity.class));
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)dialog.dismiss();
            }
        });
    }

    public void claimIntent() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("area_name", AppConfig.AreaName);
        api.isFamilyMaster(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                intent = new Intent(ClaimCitySponsorActivity.this, WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.apiUrl() +
                        "payment/index.html?customer_id=" + AppConfig.CustomerId
                        + "&surname=" + AppConfig.userEntity.surname + "&area_name=" + AppConfig.AreaName + "&crash=" + 1000);
                intent.putExtra(Constants.NO_TITLE_BAR, true);
                startActivityForResult(intent, 1000);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                AppConfig.AreaName="";
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        if (event.getCallType() == BusEnum.district) {
            if (event.getAreaName().equals(getString(R.string.nationwide))) {
                ViewInject.toast(mContext, R.string.cultural_inheritance4);
            }else if (event.getAreaName().equals(getString(R.string.location))){
                getAreaName();
            }else {
                AppConfig.AreaName = event.getAreaName();
                getUserVerify();
//                claimIntent();
            }
        }
        super.onBusCallback(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                finish();
            }
        }
    }

    private void requstData() {
        final Map<String, Object> params = new HashMap<>();
        params.put("family_id", AppConfig.family_id);
        params.put("area_name", AppConfig.AreaName);
        params.put("pageIndex", pageIndex + "");
        params.put("pageItemCount", Constants.pageItemCount);
        api.getFamilyMaster(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex == 1) {
                    listData.clear();
                }
                pageIndex++;
                entity = JSON.parseObject(data, ClaimPactEntity.class);
                listData.addAll(JSON.parseArray(entity.getApply_list(), ClaimCitySponsorEntity.class));
                mAdapter.refresh(listData);
                hint_layout.setVisibility(listData.size() > 0 ? View.GONE : View.VISIBLE);
                if (springView != null) springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null) springView.onFinishFreshAndLoad();
                finish();
            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        requstData();
    }

    @Override
    public void onLoadmore() {
        requstData();
    }
}
