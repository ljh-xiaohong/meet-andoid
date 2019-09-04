package com.yuejian.meet.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 宗亲---成员adpter
 */

public class MemberAdapter2 extends BaseAdapter {
    private Context context;
    List<MembersEntity> listData=new ArrayList<>();
    int screenW=0;


    public MemberAdapter2(Context context, List<MembersEntity> listData){
        this.context=context;
        this.listData=listData;
        this.screenW= DensityUtils.getScreenW(context);
    }
    public void refresh(List<MembersEntity> listData){
        this.listData=listData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listData!=null?(listData.size()+1)/2:0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_member_fragment_two, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MembersEntity memberOne=listData.get(position*2);
        viewHolder.member_heaber_two_bg.setVisibility(position==0?View.VISIBLE:View.GONE);
        viewHolder.item_photo_one.getLayoutParams().height=((screenW/2)-DensityUtils.dip2px(context, (float) 19.5));
        Glide.with(context).load(memberOne.getPhoto()).asBitmap().thumbnail(0.3f).into(viewHolder.item_photo_one);
        viewHolder.txt_member_name_one.setText(memberOne.getSurname()+memberOne.getName());
        viewHolder.txt_member_age_one.setText(" "+memberOne.getAge());
        viewHolder.txt_member_age_one.setSelected(memberOne.getSex().equals("1"));
        viewHolder.member_job_one.setText(memberOne.getJob());
        viewHolder.img_member_phone_one.setSelected("1".equals(memberOne.getIs_mobile_certified()));
        viewHolder.img_member_weixin_one.setSelected("1".equals(memberOne.getIs_weixin_certified()));
        viewHolder.img_member_identity_one.setSelected("1".equals(memberOne.getIs_idcard_certified()));
        viewHolder.img_member_enterprise_one.setSelected("1".equals(memberOne.getIs_business_license_certified()));
        viewHolder.member_distance_layout_one.setVisibility("-1".equals(memberOne.distance) ? View.GONE : View.VISIBLE);
        viewHolder.txt_member_distance_one.setText(memberOne.getDistance()+"km");
//        viewHolder.item_member_faqr_one.setVisibility(memberOne.getIs_super().equals("2")?View.VISIBLE:View.GONE);
        viewHolder.faqr_global_one.setVisibility(View.GONE);
        if (memberOne.getIs_super()>0){
            viewHolder.faqr_global_one.setVisibility(View.VISIBLE);
            Glide.with(context).load(memberOne.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:memberOne.is_super== FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).asBitmap().into(viewHolder.faqr_global_one);
        }

        if ((listData.size()-1)>=((position*2)+1)){
            viewHolder.item_layout_two.setVisibility(View.VISIBLE);
            final MembersEntity memberTwo=listData.get(position*2+1);
            viewHolder.item_photo_two.getLayoutParams().height=((screenW/2)-DensityUtils.dip2px(context, (float) 19.5));
            Glide.with(context).load(memberTwo.getPhoto()).asBitmap().thumbnail(0.3f).into(viewHolder.item_photo_two);
            viewHolder.txt_member_name_two.setText(memberTwo.getSurname()+memberTwo.getName());
            viewHolder.txt_member_age_two.setText(" "+memberTwo.getAge());
            viewHolder.txt_member_age_two.setSelected(memberTwo.getSex().equals("1"));
            viewHolder.member_job_two.setText(memberTwo.getJob());
            viewHolder.img_member_phone_two.setSelected("1".equals(memberTwo.getIs_mobile_certified()));
            viewHolder.img_member_weixin_two.setSelected("1".equals(memberTwo.getIs_weixin_certified()));
            viewHolder.img_member_identity_two.setSelected("1".equals(memberTwo.getIs_idcard_certified()));
            viewHolder.img_member_enterprise_two.setSelected("1".equals(memberTwo.getIs_business_license_certified()));
            viewHolder.member_distance_layout_two.setVisibility("-1".equals(memberTwo.distance) ? View.GONE : View.VISIBLE);
            viewHolder.txt_member_distance_two.setText(memberTwo.getDistance()+"km");
//            viewHolder.item_member_faqr_two.setVisibility(memberTwo.getIs_super().equals("2")?View.VISIBLE:View.GONE);
            viewHolder.faqr_global_two.setVisibility(View.GONE);
            if (memberTwo.getIs_super()>0){
                viewHolder.faqr_global_two.setVisibility(View.VISIBLE);
                Glide.with(context).load(memberTwo.is_super.equals(FqrEnum.city.getValue()) ?R.mipmap.ic_shi: memberTwo.is_super.equals(FqrEnum.province.getValue()) ?R.mipmap.ic_sheng:R.mipmap.ic_guo).asBitmap().into(viewHolder.faqr_global_two);
            }
            viewHolder.item_layout_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUitls.goToPersonHome(context, memberTwo.customer_id);

                }
            });
        }else {
            viewHolder.item_layout_two.setVisibility(View.INVISIBLE);
        }
        viewHolder.item_layout_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUitls.goToPersonHome(context, memberOne.customer_id);
            }
        });
        return convertView;
    }
    class ViewHolder {
        public LinearLayout member_heaber_two_bg;

        public CardView item_layout_one;
        public ImageView item_photo_one;
        public TextView txt_member_name_one;
        public TextView txt_member_age_one;
        public TextView member_job_one;
        public ImageView img_member_phone_one;
        public ImageView img_member_weixin_one;
        public ImageView img_member_identity_one;
        public ImageView img_member_enterprise_one;
        public LinearLayout member_distance_layout_one;
        public TextView txt_member_distance_one;
        public TextView item_member_faqr_one;
        public ImageView faqr_global_one;

        public CardView item_layout_two;
        public ImageView item_photo_two;
        public TextView txt_member_name_two;
        public TextView txt_member_age_two;
        public TextView member_job_two;
        public ImageView img_member_phone_two;
        public ImageView img_member_weixin_two;
        public ImageView img_member_identity_two;
        public ImageView img_member_enterprise_two;
        public LinearLayout member_distance_layout_two;
        public TextView txt_member_distance_two;
        public TextView item_member_faqr_two;
        public ImageView faqr_global_two;
        ViewHolder(View view){
            member_heaber_two_bg= (LinearLayout) view.findViewById(R.id.member_heaber_two_bg);

            item_layout_one= (CardView) view.findViewById(R.id.item_layout_one);
            item_photo_one= (ImageView) view.findViewById(R.id.item_photo_one);
            txt_member_name_one= (TextView) view.findViewById(R.id.txt_member_name_one);
            txt_member_age_one= (TextView) view.findViewById(R.id.txt_member_age_one);
            member_job_one= (TextView) view.findViewById(R.id.member_job_one);
            img_member_phone_one= (ImageView) view.findViewById(R.id.img_member_phone_one);
            img_member_weixin_one= (ImageView) view.findViewById(R.id.img_member_weixin_one);
            img_member_identity_one= (ImageView) view.findViewById(R.id.img_member_identity_one);
            img_member_enterprise_one= (ImageView) view.findViewById(R.id.img_member_enterprise_one);
            member_distance_layout_one= (LinearLayout) view.findViewById(R.id.member_distance_layout_one);
            txt_member_distance_one= (TextView) view.findViewById(R.id.txt_member_distance_one);
            item_member_faqr_one= (TextView) view.findViewById(R.id.item_member_faqr_one);
            faqr_global_one= (ImageView) view.findViewById(R.id.faqr_global_one);

            item_layout_two= (CardView) view.findViewById(R.id.item_layout_two);
            item_photo_two= (ImageView) view.findViewById(R.id.item_photo_two);
            txt_member_name_two= (TextView) view.findViewById(R.id.txt_member_name_two);
            txt_member_age_two= (TextView) view.findViewById(R.id.txt_member_age_two);
            member_job_two= (TextView) view.findViewById(R.id.member_job_two);
            img_member_phone_two= (ImageView) view.findViewById(R.id.img_member_phone_two);
            img_member_weixin_two= (ImageView) view.findViewById(R.id.img_member_weixin_two);
            img_member_identity_two= (ImageView) view.findViewById(R.id.img_member_identity_two);
            img_member_enterprise_two= (ImageView) view.findViewById(R.id.img_member_enterprise_two);
            member_distance_layout_two= (LinearLayout) view.findViewById(R.id.member_distance_layout_two);
            txt_member_distance_two= (TextView) view.findViewById(R.id.txt_member_distance_one);
            item_member_faqr_two= (TextView) view.findViewById(R.id.item_member_faqr_two);
            faqr_global_two= (ImageView) view.findViewById(R.id.faqr_global_two);
        }

    }
}
