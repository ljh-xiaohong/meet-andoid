package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 提现
 * Created by zh02 on 2017/7/18.
 */

public class OutCashActivity extends BaseActivity {

    @Bind(R.id.out_cash_name)
    EditText nameEd;
    @Bind(R.id.alipay_way_tab)
    LinearLayout alipayWayLy;
    @Bind(R.id.unionpay_way_tab)
    LinearLayout unionPayWayLy;
    @Bind(R.id.alipay_way)
    LinearLayout alipayLayout;
    @Bind(R.id.bank_way)
    LinearLayout bankLayout;
    @Bind(R.id.out_cash_alipay_account)
    EditText alipayAccoutEd;
    @Bind(R.id.out_cash_bank_card_account)
    EditText bankCardNumEd;
    @Bind(R.id.out_cash_cost_ed)
    EditText goldEd;
    @Bind(R.id.out_cash_gain_bal)
    TextView gainBal;
    @Bind(R.id.out_cash_bank_name)
    EditText bankNameTextView;
    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton back;
    @Bind(R.id.txt_titlebar_title)
    TextView title;

    private Mine myInfo;
    private String bankName = "";
    private int targetType = 2;
    private int gold = 0;

    private final static int PASSWORD = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_cash);
        myInfo = (Mine) getIntent().getSerializableExtra("mine");
        if (myInfo == null) {
            finish();
        }
        initialView();
    }

    private void initialView() {
        alipayLayout.setClickable(true);
        bankLayout.setClickable(true);
        back.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        title.setText(R.string.out_cash_title);
        alipayWayLy.setSelected(true);
        unionPayWayLy.setSelected(false);
        alipayLayout.setVisibility(View.VISIBLE);
        bankLayout.setVisibility(View.GONE);
        targetType = 2;
        float allBal = Float.valueOf(myInfo.gains_bal);
        gainBal.setText(getString(R.string.out_cash_text)+" " + (allBal / 100f) + " 元,");
    }

    @OnClick({
            R.id.alipay_way_tab,
            R.id.unionpay_way_tab,
            R.id.take_all_money,
            R.id.titlebar_imgBtn_back, R.id.out_cash_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alipay_way_tab:
                alipayWayLy.setSelected(true);
                unionPayWayLy.setSelected(false);
                alipayLayout.setVisibility(View.VISIBLE);
                bankLayout.setVisibility(View.GONE);
                targetType = 2;
                break;
            case R.id.unionpay_way_tab:
                alipayWayLy.setSelected(false);
                unionPayWayLy.setSelected(true);
                alipayLayout.setVisibility(View.GONE);
                bankLayout.setVisibility(View.VISIBLE);
                targetType = 1;
                break;
            case R.id.take_all_money:
                float allBal = Float.valueOf(myInfo.gains_bal);
                goldEd.setText(String.valueOf(allBal / 100f));
                break;
            case R.id.titlebar_imgBtn_back:
                finish();
                break;
            case R.id.out_cash_rule:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(Constants.URL, UrlConstant.ExplainURL.OUTCASH);
                startActivity(intent);
                break;
        }
    }

    private Map<String, Object> params = null;

    private void applyOutCash(String password) {
        params = new HashMap<>();
        params.put("customer_id", myInfo.customer_id);
        params.put("target_type", String.valueOf(targetType));
        params.put("password", Utils.md5(password));

        String goldString = goldEd.getText().toString();
        if (StringUtils.isEmpty(goldString)) {
            Toast.makeText(mContext, R.string.out_cash_toast, Toast.LENGTH_SHORT).show();
            return;
        } else if (!StringUtils.isNumber(goldString)) {
            Toast.makeText(mContext, R.string.out_cash_toast1, Toast.LENGTH_SHORT).show();
            return;
        }
        gold = Integer.valueOf(goldString);
        Double gainBal = Double.valueOf(myInfo.gains_bal) / 100;
        if (gainBal < gold) {
            Toast.makeText(this, R.string.out_cash_toast2, Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("gold", String.valueOf(gold * 100));
        String name = nameEd.getText().toString();
        if (StringUtils.isEmpty(name)) {
            Toast.makeText(mContext, R.string.out_cash_toast3, Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("target_name", name);

        if (2 == targetType) {
            String alipayAccount = alipayAccoutEd.getText().toString();
            if (StringUtils.isEmpty(alipayAccoutEd.getText().toString())) {
                Toast.makeText(this, R.string.out_cash_toast4, Toast.LENGTH_SHORT).show();
                return;
            }
            params.put("alipay_acc", alipayAccount);
        } else if (1 == targetType) {
            String bankCardNum = bankCardNumEd.getText().toString().trim();
            if (StringUtils.isEmpty(bankCardNum)) {
                Toast.makeText(mContext, R.string.out_cash_toast5, Toast.LENGTH_SHORT).show();
                return;
            }
            params.put("target_acc", bankCardNum);
            bankName = bankNameTextView.getText().toString();
            if (StringUtils.isNotEmpty(bankName)) {
                params.put("target_bank", bankName);
            } else {
                Toast.makeText(mContext, R.string.out_cash_toast6, Toast.LENGTH_SHORT).show();
                return;
            }
            EditText brachName = (EditText) findViewById(R.id.out_cash_brach_bank_name);
            String bName = brachName.getText().toString();
            if(StringUtils.isNotEmpty(bName)) {
                params.put("target_bank_branch", bName);
            } else {
                Toast.makeText(mContext, R.string.out_cash_toast7, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        apiImp.applyOutCash(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == PASSWORD) {
                String password = data.getStringExtra("password");
                applyOutCash(password);
            }
        }
    }

    public void onSubmit(View view) {
        Intent intent = new Intent(getBaseContext(), ModifyOutCashPWDActivity.class);
        intent.putExtra("mine", myInfo);
        intent.putExtra("isOutCash", true);
        startActivityForResult(intent, PASSWORD);
    }
}
