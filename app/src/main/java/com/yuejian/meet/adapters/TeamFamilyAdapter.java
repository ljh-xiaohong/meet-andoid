package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.Adapter.GiftDetailAdapter2;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.MembersEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 群---成员adpter
 */

public class TeamFamilyAdapter extends BaseAdapter {
    private Context context;
    List<MembersEntity> listData=new ArrayList<>();


    public TeamFamilyAdapter(Context context,List<MembersEntity> listData){
        this.context=context;
        this.listData=listData;
    }
    public void refresh(List<MembersEntity> listData){
        this.listData=listData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listData.size()+1;
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
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_group_family_member_layout, null);
            viewHolder.txt_family_name= (TextView) convertView.findViewById(R.id.txt_family_name);
            viewHolder.img_family_picture= (ImageView) convertView.findViewById(R.id.img_family_picture);
            viewHolder.city_sponsor= (ImageView) convertView.findViewById(R.id.city_sponsor);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.city_sponsor.setVisibility(View.GONE);
        if (position==(getCount()-1)){
            viewHolder.img_family_picture.setImageResource(R.mipmap.icon_shangchuan);
            viewHolder.txt_family_name.setText("");
        }else {
            MembersEntity item=listData.get(position);
            viewHolder.txt_family_name.setText(item.getSurname()+item.getName());
            Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into(viewHolder.img_family_picture);
            if (item.getIs_super()>0){
                viewHolder.city_sponsor.setVisibility(View.VISIBLE);
                Glide.with(context).load(item.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:item.is_super== FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).asBitmap().into(viewHolder.city_sponsor);
            }
//            if (item.getIs_family_master()>0){
//                viewHolder.city_sponsor.setVisibility(View.VISIBLE);
//            }
        }


        return convertView;
    }
    class ViewHolder {
        public TextView txt_family_name;
        public ImageView img_family_picture;
        public ImageView city_sponsor;
    }
}
