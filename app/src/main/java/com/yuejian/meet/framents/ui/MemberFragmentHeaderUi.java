package com.yuejian.meet.framents.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.ClaimCitySponsorActivity;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.FamilyMasterEntity;
import com.yuejian.meet.bean.MemberCntEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.family.FamilyFragment2;
import com.yuejian.meet.framents.home.MemberFragment;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;

/**
 * 成员UI
 * Created by zh03 on 2017/8/10.
 */
public class MemberFragmentHeaderUi {
    View view;
    Context context;
    TextView txt_member_review;//发起人在认证人数
    TextView member_cnt;//成员数
    TextView member_authenticate_cnt;//认证成员数
    TextView txt_member_certified;//去认领
    LinearLayout ll_member_sponsor_nor;//未认领
    LinearLayout ll_member_sponsor;//认领
    ImageView img_member_claim;//未认证头像

    ImageView img_member_sponsor;//认证头像
    ImageView img_member_header;//头像
    TextView txt_member_name;//名字
    TextView txt_member_age;//性别年龄
    TextView member_job;//职业
    ImageView img_member_phone;//手机认证
    ImageView img_member_weixin;//微信认证
    ImageView img_member_identity;//身份认证
    ImageView img_member_enterprise;//企业认证
    TextView txt_member_distance;//距离
    TextView member_other_job;
    LinearLayout other_job_layout;
    FamilyFragment2 member;
    MemberCntEntity memberCntEntity;

    public MemberFragmentHeaderUi(final FamilyFragment2 member, final Context context, View view) {
        this.member = member;
        this.view = view;
        this.context = context;
        ll_member_sponsor_nor = (LinearLayout) view.findViewById(R.id.ll_member_sponsor_nor);
        txt_member_review = (TextView) view.findViewById(R.id.txt_member_review);
        member_cnt = (TextView) view.findViewById(R.id.member_cnt);
        member_authenticate_cnt = (TextView) view.findViewById(R.id.member_authenticate_cnt);
        txt_member_certified = (TextView) view.findViewById(R.id.txt_member_certified);
        img_member_claim = (ImageView) view.findViewById(R.id.img_member_claim);

        ll_member_sponsor = (LinearLayout) view.findViewById(R.id.ll_member_sponsor);
        img_member_sponsor = (ImageView) view.findViewById(R.id.img_member_sponsor);
        img_member_header = (ImageView) view.findViewById(R.id.img_member_header);
        txt_member_name = (TextView) view.findViewById(R.id.txt_member_name);
        txt_member_age = (TextView) view.findViewById(R.id.txt_member_age);
        member_job = (TextView) view.findViewById(R.id.member_job);
        img_member_phone = (ImageView) view.findViewById(R.id.img_member_phone);
        img_member_weixin = (ImageView) view.findViewById(R.id.img_member_weixin);
        img_member_identity = (ImageView) view.findViewById(R.id.img_member_identity);
        img_member_enterprise = (ImageView) view.findViewById(R.id.img_member_enterprise);
        txt_member_distance = (TextView) view.findViewById(R.id.txt_member_distance);
        member_other_job = (TextView) view.findViewById(R.id.member_other_job);
        other_job_layout = (LinearLayout) view.findViewById(R.id.other_job_layout);
        txt_member_certified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppConfig.userEntity == null) {
                    ImUtils.hintLogin(context);
                    return;
                }
                if (!member.getSurname().equals(AppConfig.userEntity.getSurname())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("提示");
//                    if (!member.getSurname().equals(AppConfig.userEntity.getSurname())) {
                    builder.setMessage("您的姓氏或所属家族与当前家族不符");
//                    } else {
//                        builder.setMessage("不可以认领城市姓氏发起人职位");
//                    }
                    builder.setPositiveButton("确定", null);
                    builder.show();
                    return;
                }
                AppConfig.family_id = "";
                AppConfig.AreaName = member.getDistrict();
                context.startActivity(new Intent(context, ClaimCitySponsorActivity.class));
            }
        });
        img_member_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familyMasterEntity.getDistance() != null) {
                    AppUitls.goToPersonHome(context, familyMasterEntity.customer_id);
                }
            }
        });

        view.setClickable(true);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familyMasterEntity.getDistance() != null) {
                    AppUitls.goToPersonHome(context, familyMasterEntity.customer_id);
                }
            }
        });
    }

    FamilyMasterEntity familyMasterEntity = new FamilyMasterEntity();

    //发起人
    public void setFamilyMaster(FamilyMasterEntity familyMasterEntity, String familyState) {
        this.familyMasterEntity = familyMasterEntity;
        if (StringUtils.isEmpty(familyMasterEntity.getDistance())) {
            ll_member_sponsor_nor.setVisibility(View.VISIBLE);
            img_member_claim.setVisibility(View.VISIBLE);
            ll_member_sponsor.setVisibility(View.GONE);
            img_member_sponsor.setVisibility(View.GONE);
            img_member_header.setImageResource(R.mipmap.ic_default);
        } else {
            ll_member_sponsor_nor.setVisibility(View.GONE);
            img_member_claim.setVisibility(View.GONE);
            ll_member_sponsor.setVisibility(View.VISIBLE);
            img_member_sponsor.setVisibility(View.VISIBLE);
            txt_member_distance.setText(familyMasterEntity.getDistance() + "km");
            txt_member_name.setText(familyMasterEntity.getSurname() + familyMasterEntity.getName());
            txt_member_age.setSelected("1".equals(familyMasterEntity.getSex()));
            txt_member_age.setText(" " + familyMasterEntity.getAge());
            member_job.setText(familyMasterEntity.getCompany_name()+(StringUtils.isEmpty(familyMasterEntity.getCompany_name())?"":"  ")+familyMasterEntity.getJob());
            img_member_phone.setSelected(!"-1".equals(familyMasterEntity.getIs_mobile_certified()));
            img_member_weixin.setSelected(!"-1".equals(familyMasterEntity.getIs_weixin_certified()));
            img_member_identity.setSelected(!"-1".equals(familyMasterEntity.getIs_idcard_certified()));
            img_member_enterprise.setSelected(!"-1".equals(familyMasterEntity.getIs_business_license_certified()));
            member_other_job.setText(familyMasterEntity.getOther_job());
            other_job_layout.setVisibility(StringUtils.isEmpty(familyMasterEntity.getOther_job())?View.GONE:View.VISIBLE);
            Glide.with(context).load(familyMasterEntity.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into(img_member_header);
            if (familyState.equals("1")) {
                img_member_sponsor.setImageResource(R.mipmap.ic_guo);
            } else if (familyState.equals("3")) {
                img_member_sponsor.setImageResource(R.mipmap.ic_sheng);
            } else {
                img_member_sponsor.setImageResource(R.mipmap.ic_shi);
            }
        }
    }

    //发起人认证数
    public void setReviewCnt(String cnt) {
        txt_member_review.setText(cnt + "人在审核中");
    }

    ///成员数
    public void setMemberCnt(MemberCntEntity memberCntEntity) {
        this.memberCntEntity = memberCntEntity;
//        if (AppConfig.userEntity!=null && memberCntEntity.getFamily_id()!=null){
//            if (memberCntEntity.getFamily_id().equals(AppConfig.userEntity.getFamily_id())){
//                txt_member_certified.setVisibility(View.VISIBLE);
//            }else {
//                txt_member_certified.setVisibility(View.GONE);
//            }
//        }else {
//            txt_member_certified.setVisibility(View.GONE);
//        }
        member_cnt.setText(memberCntEntity.getMember_cnt() == null ? "0" : memberCntEntity.getMember_cnt());
        member_authenticate_cnt.setText(memberCntEntity.getCertified_member_cnt() == null ? "0" : memberCntEntity.getCertified_member_cnt());
    }
}
