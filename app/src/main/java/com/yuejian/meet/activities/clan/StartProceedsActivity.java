package com.yuejian.meet.activities.clan;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.FCLoger;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DatePicker.OnYearMonthDayPickListener;

public class StartProceedsActivity extends BaseActivity {
    @Bind(R.id.charge_info)
    EditText charge_info;
    @Bind(R.id.charge_price)
    EditText charge_price;
    @Bind(R.id.charge_title)
    EditText charge_title;
    String clanId;
    LoadingDialogFragment dialog;
    private int mDay;
    private int mMonth;
    String memberArray;
    @Bind(R.id.member_sel_cnt)
    TextView member_sel_cnt;
    String memeberCnt;
    @Bind(R.id.priceeds_time)
    TextView tiem;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_start_proceeds);
        this.clanId = getIntent().getStringExtra("clanId");
        this.dialog = LoadingDialogFragment.newInstance("正在操作..");
        initData();
    }

    private void showDatePick() {
        DatePicker localDatePicker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        localDatePicker.setLabel(StringUtils.getResStr(this, R.string.pickerview_year), StringUtils.getResStr(this, R.string.pickerview_month), StringUtils.getResStr(this, R.string.pickerview_day));
        Calendar localCalendar = Calendar.getInstance();
        int i = localCalendar.get(Calendar.YEAR);
        this.mMonth = (localCalendar.get(Calendar.MONTH) + 1);
        this.mDay = localCalendar.get(Calendar.DAY_OF_MONTH);
        FCLoger.debug("year:" + i + ",Month:" + this.mMonth + ",mDay:" + this.mDay);
        localDatePicker.setRangeStart(i, this.mMonth, this.mDay + 1);
        localDatePicker.setRangeEnd(i + 12, this.mMonth, this.mDay);
        localDatePicker.setSelectedItem(i, this.mMonth, this.mDay);
        localDatePicker.setOnDatePickListener(new OnYearMonthDayPickListener()
        {
            public void onDatePicked(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3)
            {
                StartProceedsActivity.this.tiem.setText(paramAnonymousString1 + "-" + paramAnonymousString2 + "-" + paramAnonymousString3);
            }
        });
        localDatePicker.show();
    }

    public void initData() {
        setTitleText("发起收款");
        this.charge_title.setOnEditorActionListener(new OnEditorActionListener()
        {
            public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
            {
                return paramAnonymousKeyEvent.getKeyCode() == 66;
            }
        });
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if ((paramInt2 == -1) && (paramIntent != null)) {
            this.memberArray = paramIntent.getStringExtra("memberArray");
            this.memeberCnt = paramIntent.getStringExtra("memberCnt");
            this.member_sel_cnt.setText("已选择" + this.memeberCnt + "人");
        }
    }

    @OnClick({R.id.layout_proceeds_tiem, R.id.proeccds_member_sel, R.id.bu_submit})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.bu_submit:
                postData();
                return;
            case R.id.layout_proceeds_tiem:
                showDatePick();
                return;
            case R.id.proeccds_member_sel:
                Intent intent = new Intent(this, ClanMemberChargeSelActivity.class);
                intent.putExtra("clanId", this.clanId);
                startActivityIfNeeded(intent, 11);
                return;
        }
    }

    public void postData() {
        if (StringUtil.isEmpty(this.charge_title.getText().toString())) {
            ViewInject.toast(this, "请输入收款名称");
            return;
        }
        if (StringUtil.isEmpty(this.charge_info.getText().toString())) {
            ViewInject.toast(this, "请输入收款原因");
            return;
        }
        if (StringUtil.isEmpty(this.charge_price.getText().toString())) {
            ViewInject.toast(this, "请输入收款金额");
            return;
        }
        if (StringUtil.isEmpty(this.tiem.getText().toString())) {
            ViewInject.toast(this, "请选择结束时间");
            return;
        }
        if (StringUtil.isEmpty(this.memberArray)) {
            ViewInject.toast(this, "请选择收款范围");
            return;
        }
        if (Integer.parseInt(this.charge_price.getText().toString()) > 200) {
            ViewInject.toast(this, "发起收款人均金额限额为200");
            return;
        }
        if (this.dialog != null) {
            this.dialog.show(getFragmentManager(), "");
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("charge_title", this.charge_title.getText().toString());
        localHashMap.put("charge_info", this.charge_info.getText().toString());
        localHashMap.put("charge_price", this.charge_price.getText().toString());
        localHashMap.put("charge_endtime", this.tiem.getText().toString());
        localHashMap.put("invite_customer_ids", this.memberArray);
        localHashMap.put("association_id", this.clanId);
        localHashMap.put("customer_id", this.user.getCustomer_id());
        this.apiImp.clanMemberCharge(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                if (StartProceedsActivity.this.dialog != null) {
                    StartProceedsActivity.this.dialog.dismiss();
                }
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                StartProceedsActivity.this.setResult(-1, new Intent());
                StartProceedsActivity.this.finish();
            }
        });
    }
}
