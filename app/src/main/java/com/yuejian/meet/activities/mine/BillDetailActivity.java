package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.bean.Bill;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;

/**
 * 账单详情
 * Created by zh02 on 2017/8/22.
 */

public class BillDetailActivity extends BaseActivity {
    @Bind(R.id.bill_icon)
    ImageView billIcon;
    @Bind(R.id.bill_content)
    TextView billContent;
    @Bind(R.id.bill_account)
    TextView billAccount;
    @Bind(R.id.bill_explain)
    TextView billExplain;
    @Bind(R.id.bill_create_time)
    TextView billCreateTime;
    @Bind(R.id.bill_id)
    TextView billId;
    @Bind(R.id.bill_type)
    TextView billType;

    private Bill bill = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        setTitleText(getString(R.string.bill_title));
        String billJson = getIntent().getStringExtra("bill");
        bill = JSON.parseObject(billJson, Bill.class);
        if (bill == null) {
            finish();
            return;
        }
        if (StringUtils.isNotEmpty(bill.photo)) {
            Glide.with(this).load(bill.photo).into(billIcon);
        }
        billIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNotEmpty(bill.op_customer_id) && !"0".equals(bill.op_customer_id)) {
                    AppUitls.goToPersonHome(mContext, bill.op_customer_id);
                }
            }
        });
        billType.setText(bill.type_name);
        billExplain.setText(bill.content);
        billCreateTime.setText(new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault()).format(new Date(bill.create_time)));
        billId.setText(bill.id);
        billAccount.setText((bill.amount > 0 ? "+" : "") + bill.amount);

    }

    @Override
    public void onClick(View v) {

    }
}
