package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.ClaimCitySponsorEntity;
import com.yuejian.meet.bean.MembersEntity;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 城市发起人认领adpter
 */

public class ClaimCitySponsorAdapter extends FKAdapter<ClaimCitySponsorEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public ClaimCitySponsorAdapter(AbsListView view, List<ClaimCitySponsorEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
    }
    public void convert(AdapterHolder helper, ClaimCitySponsorEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, ClaimCitySponsorEntity item, final int position){
        this.mHelper=helper;
        mHelper.setText(R.id.txt_claim_name,item.getSurname()+item.getName());
        mHelper.setText(R.id.txt_claim_job,item.getJob());
        mHelper.setText(R.id.txt_claim_age," "+item.getAge());
        mHelper.getView(R.id.txt_claim_audit).setSelected(item.getApply_status().equals("0")?false:true);
        mHelper.setText(R.id.txt_claim_audit,item.getApply_status().equals("0")?"申请中":"申请未通过");
        mHelper.getView(R.id.txt_claim_age).setSelected(item.getSex().equals("1")?true:false);
        mHelper.getView(R.id.img_claim_phone).setSelected(item.getIs_mobile_certified().endsWith("1")?true:false);
        mHelper.getView(R.id.img_claim_weixin).setSelected(item.is_weixin_certified.endsWith("1")?true:false);
        mHelper.getView(R.id.img_claim_identity).setSelected(item.is_idcard_certified.endsWith("1")?true:false);
        mHelper.getView(R.id.img_claim_enterprise).setSelected(item.is_business_license_certified.endsWith("1")?true:false);
        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.img_lam_photo));
    }
}
