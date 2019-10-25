package com.yuejian.meet.activities.clan;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.ui.ClanPayUi;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ClanChargeEntity;
import com.yuejian.meet.bean.ClanMiAllEntity;
import com.yuejian.meet.bean.ClanPhotosEntity;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class ClanInfoActivity extends BaseActivity {
    ClanChargeEntity clanChargeEntity;
    ClanPayUi clanPayUi;
    List<ClanPhotosEntity> clanPhotosEntity = new ArrayList();
    @Bind(R.id.clan_info_about)
    TextView clan_info_about;
    @Bind(R.id.clan_info_add_but)
    Button clan_info_add_but;
    @Bind(R.id.clan_info_address)
    TextView clan_info_address;
    @Bind(R.id.clan_info_charge_content)
    TextView clan_info_charge_content;
    @Bind(R.id.clan_info_charge_layout)
    LinearLayout clan_info_charge_layout;
    @Bind(R.id.clan_info_charge_money)
    TextView clan_info_charge_money;
    @Bind(R.id.clan_info_charge_pay_cnt)
    TextView clan_info_charge_pay_cnt;
    @Bind(R.id.clan_info_charge_time)
    TextView clan_info_charge_time;
    @Bind(R.id.clan_info_charge_title)
    TextView clan_info_charge_title;
    @Bind(R.id.clan_info_customer_cnt)
    TextView clan_info_customer_cnt;
    @Bind(R.id.clan_info_found_date)
    TextView clan_info_found_date;
    @Bind(R.id.clan_info_hall_name)
    TextView clan_info_hall_name;
    @Bind(R.id.clan_info_pay_button)
    Button clan_info_pay_button;
    @Bind(R.id.clan_info_photo_layout)
    LinearLayout clan_info_photo_layout;
    @Bind(R.id.clan_info_photos1)
    ImageView clan_info_photos1;
    @Bind(R.id.clan_info_photos2)
    ImageView clan_info_photos2;
    @Bind(R.id.clan_info_photos3)
    ImageView clan_info_photos3;
    @Bind(R.id.clan_info_shengpi)
    LinearLayout clan_info_shengpi;
    @Bind(R.id.clan_info_surname)
    TextView clan_info_surname;
    @Bind(R.id.clan_info_title_photo)
    ImageView clan_info_title_photo;
    @Bind(R.id.clan_name)
    TextView clan_name;
    LoadingDialogFragment dialog;
    Dialog dialogs;
    Intent intent;
    @Bind(R.id.layout_clan_info_control)
    LinearLayout layout_clan_info_control;
    @Bind(R.id.clan_info_houkuan)
    LinearLayout clan_info_houkuan;
    List<String> list;
    MoreClanEntity moreClanEntity;
    int type;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_clan_info);
        this.moreClanEntity = ((MoreClanEntity)getIntent().getSerializableExtra("clanEntity"));
        this.dialog = LoadingDialogFragment.newInstance("正在加载..");
        if (user==null){
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        initData();
    }

    public void dialogPay() {
        if (this.clan_info_pay_button.getText().toString().equals("已支付")) {
            return;
        }
        if (this.clanPayUi == null) {
            this.clanPayUi = new ClanPayUi(this);
        }
        Builder localBuilder = new Builder(this);
        View localView = View.inflate(this, R.layout.dialog_clan_pay_layout, null);
        ((TextView)localView.findViewById(R.id.money)).setText("余额：" + (int)(AppConfig.moneySun.doubleValue() / 100.0D));
        ((LinearLayout)localView.findViewById(R.id.jinbi)).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                ClanInfoActivity.this.dialogs.dismiss();
                ClanInfoActivity.this.payGold();
            }
        });
        localView.findViewById(R.id.weixin).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                ClanInfoActivity.this.dialogs.dismiss();
                AppConfig.isClanWeiXinPay = Boolean.valueOf(true);
                ClanInfoActivity.this.clanPayUi.doInCash(2, Integer.parseInt(ClanInfoActivity.this.clanChargeEntity.getCharge_price()),null);
            }
        });
        localView.findViewById(R.id.zhifubao).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                ClanInfoActivity.this.dialogs.dismiss();
                ClanInfoActivity.this.clanPayUi.doInCash(1, Integer.parseInt(ClanInfoActivity.this.clanChargeEntity.getCharge_price()),null);
            }
        });
        localBuilder.setView(localView);
        this.dialogs = localBuilder.show();
        this.dialogs.show();
    }

    public void displDialog(String paramString1, String paramString2) {
        Builder localBuilder = new Builder(this);
        String str = paramString1;
        if (StringUtil.isEmpty(paramString1)) {
            str = "提示";
        }
        localBuilder.setTitle(str);
        localBuilder.setMessage(paramString2);
        localBuilder.setNegativeButton("确定", null);
        localBuilder.show();
    }

    public void initData() {
        setTitleText(this.moreClanEntity.getAssociation_name());
        Glide.with(this).load(this.moreClanEntity.getAssociation_img()).error(R.mipmap.ic_default).into(this.clan_info_title_photo);
        requstData();
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
    }

    public void onBusCallback(BusCallEntity paramBusCallEntity) {
        super.onBusCallback(paramBusCallEntity);
        if (paramBusCallEntity.getCallType() == BusEnum.CLAN_WEIXIN_PAY) {
            payGold();
        }
        while (paramBusCallEntity.getCallType() != BusEnum.CLAN_UPDATE) {
            return;
        }
        requstData();
    }

    @OnClick({ R.id.clan_info_kuangjia, R.id.clan_info_guangchang, R.id.clan_info_gonggao_layout,
            R.id.clan_info_shengpi, R.id.clan_info_houkuan, R.id.caln_info_ibianji, R.id.clan_info_pay_button
            ,R.id.clan_info_photos1, R.id.clan_info_photos2, R.id.clan_info_photos3, R.id.clan_info_photo_all, R.id.clan_info_add_but, R.id.about_unfold, R.id.clan_info_customer_cnt})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.clan_info_gonggao_layout:
//                if (this.moreClanEntity.getCustomer_type() == 0) {
//                    displDialog(null, "成为宗亲会成员，方可查看宗亲会公告。");
//                    return;
//                }
//                this.intent = new Intent(this, ClanNoticeActivity.class);
//                this.intent.putExtra("clanId", this.moreClanEntity.getAssociation_id());
//                startActivity(this.intent);
                return;
            case R.id.clan_info_pay_button:
                dialogPay();
                return;
            case R.id.caln_info_ibianji:
                this.intent = new Intent(this, CreateMiClanActivity.class);
                this.intent.putExtra("clanId", this.moreClanEntity.getAssociation_id());
                startActivityForResult(this.intent, 113);
                return;
            case R.id.clan_info_houkuan:
                return;
            case R.id.clan_info_shengpi:
                this.intent = new Intent(this, ClanMasterApproveActivity.class);
                this.intent.putExtra("clanId", this.moreClanEntity.getAssociation_id());
                startActivityForResult(this.intent, 113);
                return;
            case R.id.clan_info_guangchang:
                if (this.moreClanEntity.getCustomer_type() != 0) {
                    ImUtils.toGroupChat(this, this.moreClanEntity.getWy_team_id(), ChatEnum.CLANGROUP, "0", Boolean.valueOf(true));
                    return;
                }
                displDialog(null, "成为宗亲会成员，方可进入宗亲会聊天广场。");
                return;
            case R.id.about_unfold://简介
                paramView = LayoutInflater.from(this).inflate(R.layout.dialog_clan_synopsis_layout, null);
                ((TextView)paramView.findViewById(R.id.dialog_content)).setText(this.moreClanEntity.getAssociation_about());
                Builder localBuilder = new Builder(this);
                localBuilder.setView(paramView);
                localBuilder.show();
                return;
            case R.id.clan_info_add_but:
                if (this.user.getSurname().equals(this.moreClanEntity.getAssociation_surname())) {
                    requstAddClan();
                }
                ViewInject.toast(this, "只能加入同姓宗亲会哦");
                return;
            case R.id.clan_info_customer_cnt:
                this.intent = new Intent(this, ClanMemberActivity.class);
                this.intent.putExtra("type", this.type);
                this.intent.putExtra("customer_id", this.moreClanEntity.getCustomer_id());
                this.intent.putExtra("clanId", this.moreClanEntity.getAssociation_id());
                this.intent.putExtra("clanName", this.moreClanEntity.getAssociation_name());
                startActivityForResult(this.intent, 113);
                return;
            case R.id.clan_info_photo_all:
                this.intent = new Intent(this, ClanPhotoActivity.class);
                this.intent.putExtra("clanId", this.moreClanEntity.getAssociation_id());
                this.intent.putExtra("type", this.moreClanEntity.getCustomer_type());
                startActivity(this.intent);
                return;
            case R.id.clan_info_photos3:
                this.list = new ArrayList();
                this.list.add(this.moreClanEntity.getAssociation_img());
                Utils.displayImages(this, this.list, 0, null);
                return;
            case R.id.clan_info_photos1:
                this.list = new ArrayList();
                for (int i=0;i < this.clanPhotosEntity.size();i++)
                {
                    this.list.add(((ClanPhotosEntity)this.clanPhotosEntity.get(i)).getPhoto_img());
                }
                Utils.displayImages(this, this.list, 0, null);
                return;
            case R.id.clan_info_photos2:
                this.list = new ArrayList();
                for (int i=0;i < this.clanPhotosEntity.size();i++) {
                    this.list.add(((ClanPhotosEntity)this.clanPhotosEntity.get(i)).getPhoto_img());
                }
                Utils.displayImages(this, this.list, 1, null);
                return;
        }
    }


    protected void onDestroy() {
        super.onDestroy();
        AppConfig.isClanWeiXinPay = Boolean.valueOf(false);
    }

    public void payGold() {
        if (this.dialog != null) {
            this.dialog.show(getFragmentManager(), "");
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("charge_id", this.clanChargeEntity.getCharge_id());
        localHashMap.put("customer_id", this.user.getCustomer_id());
        this.apiImp.clanPayment(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                if (ClanInfoActivity.this.dialog != null) {
                    ClanInfoActivity.this.dialog.dismiss();
                }
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                if (ClanInfoActivity.this.dialog != null) {
                    ClanInfoActivity.this.dialog.dismiss();
                }
                ViewInject.toast(ClanInfoActivity.this.getApplication(), "支付成功");
            }
        });
    }

    public void requstAddClan() {
        Builder localBuilder = new Builder(this);
        localBuilder.setTitle("入会要求");
        localBuilder.setMessage(this.moreClanEntity.getAssociation_intrant_about());
        localBuilder.setNegativeButton("确定", new OnClickListener()
        {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
                paramAnonymousDialogInterface.dismiss();
                if (ClanInfoActivity.this.dialog != null) {
                    ClanInfoActivity.this.dialog.show(ClanInfoActivity.this.getFragmentManager(), "");
                }
                Map<String,Object> params = new HashMap();
                params.put("association_id", ClanInfoActivity.this.moreClanEntity.getAssociation_id());
                params.put("invite_customer_id", AppConfig.CustomerId);
                params.put("be_invite_customer_id", ClanInfoActivity.this.moreClanEntity.getCustomer_id());
                ClanInfoActivity.this.apiImp.addClanMember(params, this, new DataIdCallback<String>() {
                    public void onFailed(String paramAnonymous2String1, String paramAnonymous2String2, int paramAnonymous2Int) {
                        if (ClanInfoActivity.this.dialog != null) {
                            ClanInfoActivity.this.dialog.dismiss();
                        }
                    }

                    public void onSuccess(String paramAnonymous2String, int paramAnonymous2Int) {
                        ViewInject.toast(ClanInfoActivity.this.getApplication(), "提交成功");
                        if (ClanInfoActivity.this.dialog != null) {
                            ClanInfoActivity.this.dialog.dismiss();
                        }
                    }
                });
            }
        });
        localBuilder.setNeutralButton("取消", null);
        localBuilder.show();
    }

    public void requstData() {
        if (this.dialog != null) {
            this.dialog.show(getFragmentManager(), "");
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", this.moreClanEntity.getAssociation_id());
        localHashMap.put("customer_id", this.user.getCustomer_id());
        this.apiImp.findClanInfo(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt)
            {
                ClanInfoActivity.this.finish();
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                if (ClanInfoActivity.this.dialog != null) {
                    ClanInfoActivity.this.dialog.dismiss();
                }
                ClanMiAllEntity clanMiAllEntity = (JSON.parseObject(paramAnonymousString, ClanMiAllEntity.class));
                ClanInfoActivity.this.setLayout(clanMiAllEntity);
            }
        });
    }

    public void setLayout(ClanMiAllEntity paramClanMiAllEntity) {
        if (!StringUtil.isEmpty(paramClanMiAllEntity.getPhotos())) {
            this.clanPhotosEntity = JSON.parseArray(paramClanMiAllEntity.getPhotos(), ClanPhotosEntity.class);
        }
        this.moreClanEntity = ((MoreClanEntity) JSON.parseObject(paramClanMiAllEntity.getClanAssociation(), MoreClanEntity.class));
        this.clan_info_surname.setText("姓氏：" + this.moreClanEntity.getAssociation_surname());
        this.clan_info_hall_name.setText("堂号：" + this.moreClanEntity.getAssociation_hall_name());
        this.clan_info_hall_name.setText("堂号：" + this.moreClanEntity.getAssociation_hall_name());
        this.clan_info_found_date.setText("成立时间：" + this.moreClanEntity.getAssociation_found_date());
        this.clan_info_address.setText("详细地址：" + this.moreClanEntity.getAssociation_province() + this.moreClanEntity.getAssociation_city() + this.moreClanEntity.getAssociation_area());
        this.clan_name.setText(this.moreClanEntity.getAssociation_name());
        this.clan_info_about.setText(this.moreClanEntity.getAssociation_about());
        this.clan_info_customer_cnt.setText("已加入" + this.moreClanEntity.getAssociation_cnt() + "位成员");
        Glide.with(this).load(this.moreClanEntity.getAssociation_img()).error(R.mipmap.ic_default).into(this.clan_info_title_photo);
        this.layout_clan_info_control.setVisibility(View.GONE);
        this.clan_info_shengpi.setVisibility(View.INVISIBLE);
        this.clan_info_houkuan.setVisibility(View.INVISIBLE);
        this.type = this.moreClanEntity.getCustomer_type();
        this.clan_info_add_but.setVisibility(View.GONE);
        if (this.moreClanEntity.getCustomer_type() == 0) {
            this.clan_info_add_but.setVisibility(View.VISIBLE);
        }
        this.clan_info_photos1.setVisibility(View.INVISIBLE);
        this.clan_info_photos2.setVisibility(View.INVISIBLE);
        this.clan_info_photos3.setVisibility(View.INVISIBLE);
        if (this.clanPhotosEntity.size() >= 1) {
            this.clan_info_photos1.setVisibility(View.VISIBLE);
            Glide.with(this).load(((ClanPhotosEntity)this.clanPhotosEntity.get(0)).getPhoto_img()).into(this.clan_info_photos1);
        }else {
            this.clan_info_photo_layout.setVisibility(View.GONE);
        }
        if (this.clanPhotosEntity.size() >= 2) {
            this.clan_info_photos2.setVisibility(View.VISIBLE);
            Glide.with(this).load(((ClanPhotosEntity)this.clanPhotosEntity.get(1)).getPhoto_img()).into(this.clan_info_photos2);
        }
        if (this.clanPhotosEntity.size() >= 3) {
            this.clan_info_photos3.setVisibility(View.VISIBLE);
            Glide.with(this).load(((ClanPhotosEntity)this.clanPhotosEntity.get(2)).getPhoto_img()).into(this.clan_info_photos3);
        }
        if (moreClanEntity.getCustomer_type() == 1){
            this.layout_clan_info_control.setVisibility(View.VISIBLE);
            this.clan_info_shengpi.setVisibility(View.VISIBLE);
            this.clan_info_houkuan.setVisibility(View.VISIBLE);
        }
//            int i = this.clan_info_about.getLayout().getEllipsisCount(this.clan_info_about.getLineCount() - 1);
//            this.clan_info_about.getLayout().getEllipsisCount(this.clan_info_about.getLineCount() - 1);
//            if (i <= 0) {
//            }
//            this.clan_info_about.setMaxHeight(getResources().getDisplayMetrics().heightPixels);
//            this.clan_info_about.setEllipsize(TextUtils.TruncateAt.END);
//            ((TextView)findViewById(R.id.about_unfold)).setText("收起");
//            if (this.moreClanEntity.getCustomer_type() != 1) {
//            }
            if (!StringUtil.isEmpty(paramClanMiAllEntity.getCharge())){
                this.clanChargeEntity = ((ClanChargeEntity) JSON.parseObject(paramClanMiAllEntity.getCharge(), ClanChargeEntity.class));
                if (!this.clanChargeEntity.getCharge_status().equals("2")) {
                    this.clan_info_charge_layout.setVisibility(View.VISIBLE);
                    this.clan_info_charge_title.setText(this.clanChargeEntity.getCharge_title());
                    this.clan_info_charge_content.setText(this.clanChargeEntity.getCharge_info());
                    this.clan_info_charge_money.setText("金额：" + this.clanChargeEntity.getCharge_price());
                    this.clan_info_charge_time.setText("截止时间：" + this.clanChargeEntity.getCharge_endtime());
                    this.clan_info_charge_pay_cnt.setText(this.clanChargeEntity.getCharge_pay_cnt() + "人已支付费用");
                    if (this.clanChargeEntity.getIs_pay() == 1) {
                        this.clan_info_pay_button.setText("已支付");
                    }
                }
            }else {
                this.clan_info_charge_layout.setVisibility(View.GONE);
            }
        ((TextView)findViewById(R.id.about_unfold)).setText("显示全部");
//        this.clan_info_about.setEllipsize(null);
    }
}
