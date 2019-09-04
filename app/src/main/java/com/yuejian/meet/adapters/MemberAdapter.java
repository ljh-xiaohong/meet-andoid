package com.yuejian.meet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.bean.SurnameEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 成员adpter
 */

public class MemberAdapter extends FKAdapter<MembersEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public MemberAdapter(AbsListView view, List<MembersEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
    }

    public void convert(AdapterHolder helper, MembersEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(AdapterHolder helper, final MembersEntity item, final int position) {
        this.mHelper = helper;
        mHelper.getView(R.id.member_heaber_bg).setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        mHelper.setText(R.id.txt_member_name, item.getSurname() + item.getName());
        mHelper.setText(R.id.member_job, item.getCompany_name()+(StringUtils.isEmpty(item.getCompany_name())?"":"  ")+ item.getJob());
        mHelper.setText(R.id.other_job, item.getOther_job());
        mHelper.getView(R.id.other_job_layout).setVisibility(StringUtils.isEmpty(item.getOther_job())?View.GONE:View.VISIBLE);
        mHelper.setText(R.id.txt_member_age, " " + item.getAge());
        mHelper.getView(R.id.member_distance_layout).setVisibility("-1".equals(item.distance) ? View.GONE : View.VISIBLE);
        mHelper.setText(R.id.txt_member_distance, item.distance + "km");
        mHelper.getView(R.id.txt_member_area).setVisibility(StringUtils.isEmpty(item.family_area) ? View.GONE : View.VISIBLE);
        mHelper.setText(R.id.txt_member_area, item.family_area);
        mHelper.getView(R.id.txt_member_age).setSelected("1".equals(item.getSex()));
        mHelper.getView(R.id.img_member_phone).setSelected("1".equals(item.getIs_mobile_certified()));
        mHelper.getView(R.id.img_member_weixin).setSelected("1".equals(item.is_weixin_certified));
        mHelper.getView(R.id.img_member_identity).setSelected("1".equals(item.is_idcard_certified));
        mHelper.getView(R.id.img_member_enterprise).setSelected("1".equals(item.is_business_license_certified));
        Glide.with(context).load(item.getPhoto()).placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.img_member_header));
//        if (0 == item.is_family_master) {
//            mHelper.getView(R.id.faqr).setVisibility(View.GONE);
//        } else {
//            mHelper.getView(R.id.faqr).setVisibility(View.VISIBLE);
//        }

        ImageView fqr = mHelper.getView(R.id.faqr);
        if (0 == item.is_super) {
            fqr.setVisibility(View.GONE);
        } else {
            fqr.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.is_super == FqrEnum.city.getValue() ? R.mipmap.ic_shi : item.is_super == FqrEnum.province.getValue() ? R.mipmap.ic_sheng : R.mipmap.ic_guo).asBitmap().into(fqr);
        }
        mHelper.getView(R.id.itme_layout_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUitls.goToPersonHome(mCxt, item.customer_id);
            }
        });
    }
}
