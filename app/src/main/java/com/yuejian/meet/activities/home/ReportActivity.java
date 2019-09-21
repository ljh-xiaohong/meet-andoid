package com.yuejian.meet.activities.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReportActivity extends BaseActivity {


    @Bind(R.id.tv_counts)
    TextView tvCounts;
    @Bind(R.id.count_lay)
    LinearLayout countLay;
    @Bind(R.id.report_edit)
    EditText reportEdit;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.commit)
    TextView commit;
    private LoadingDialogFragment dialog;

    private String reportCustomerId, crId, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_activity);
        ButterKnife.bind(this);
        reportCustomerId = getIntent().getStringExtra("ReportActivity.reportCustomerId");
        crId = getIntent().getStringExtra("ReportActivity.crId");
        type = getIntent().getStringExtra("ReportActivity.type");
        dialog = LoadingDialogFragment.newInstance(getString(R.string.is_requesting));
        reportEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvCounts.setText(s.length() + "");
                if (s.length() > 200) {
                    tvCounts.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    tvCounts.setTextColor(getResources().getColor(R.color.black9));
                }
            }
        });
    }

    public ApiImp apiImp = new ApiImp();

    @OnClick({R.id.commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commit:
                commit();
                break;
        }
    }

    /**
     * @param context
     * @param reportCustomerId 被举报人id
     * @param crId             被举报内容信息id
     * @param type             被举报内容信息类型：1动态2文章3视频
     */
    public static void startActivityForResult(Context context, int requestCode, String reportCustomerId, String crId, String type) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra("ReportActivity.reportCustomerId", reportCustomerId);
        intent.putExtra("ReportActivity.crId", crId);
        intent.putExtra("ReportActivity.type", type);
        ((Activity) context).startActivityForResult(intent, requestCode);

    }

    private void commit() {
        if (dialog != null)
            dialog.show(getFragmentManager(), "");

        if (!CommonUtil.isNull(reportEdit.getText().toString())) {
            ViewInject.shortToast(ReportActivity.this, R.string.Please_enter_report);
            return;
        }
        if (!CommonUtil.isNull(phone.getText().toString())) {
            ViewInject.shortToast(ReportActivity.this, R.string.reg_hint_phone);
            return;
        }
        // customerId：用户id，reportDes:举报内容，mobile：手机号，reportCustomerId:被举报人id，crType：被举报内容信息类型：1动态2文章3视频，crId:被举报内容信息id
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("reportDes", reportEdit.getText().toString());
        map.put("mobile", phone.getText().toString());
        map.put("reportCustomerId", reportCustomerId);
        //1动态 2文章 3视频
        map.put("crType", type);
        map.put("crId", crId);
        apiImp.postDoReport(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog != null)
                    dialog.dismiss();
                ResultBean resultBean = new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(ReportActivity.this, resultBean.getMessage());
                if (resultBean.getCode() == 0) {
                    Intent i = new Intent();
                    setResult(7, i);
                    finish();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }


}
