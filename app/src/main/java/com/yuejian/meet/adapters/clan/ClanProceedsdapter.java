package com.yuejian.meet.adapters.clan;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanProceedsInfoActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.ClanChargeEntity;

import java.util.List;

public class ClanProceedsdapter extends FKAdapter<ClanChargeEntity> {
    private Context context;
    private AdapterHolder mHelper;

    public ClanProceedsdapter(AbsListView paramAbsListView, List<ClanChargeEntity> paramList, int paramInt) {
        super(paramAbsListView, paramList, paramInt);
        this.context = paramAbsListView.getContext();
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, final ClanChargeEntity paramClanChargeEntity, int paramInt) {
        this.mHelper = paramAdapterHolder;
        paramAdapterHolder.setText(R.id.clan_info_charge_title, paramClanChargeEntity.getCharge_title());
        paramAdapterHolder.setText(R.id.clan_info_charge_content, paramClanChargeEntity.getCharge_info());
        paramAdapterHolder.setText(R.id.clan_info_charge_money, "金额：" + paramClanChargeEntity.getCharge_price());
        paramAdapterHolder.setText(R.id.clan_info_charge_time, "截止时间：" + paramClanChargeEntity.getCharge_endtime());
        paramAdapterHolder.setText(R.id.clan_info_charge_pay_cnt, paramClanChargeEntity.getCharge_pay_cnt() + "人已支付费用");
        this.mHelper.getView(R.id.clan_info_pay_button).setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Intent intent = new Intent(ClanProceedsdapter.this.context, ClanProceedsInfoActivity.class);
                intent.putExtra("chargeId", paramClanChargeEntity.getCharge_id());
                ClanProceedsdapter.this.context.startActivity(intent);
            }
        });
    }

    public void convert(AdapterHolder paramAdapterHolder, ClanChargeEntity paramClanChargeEntity, boolean paramBoolean, int paramInt) {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramClanChargeEntity, paramInt);
    }
}
