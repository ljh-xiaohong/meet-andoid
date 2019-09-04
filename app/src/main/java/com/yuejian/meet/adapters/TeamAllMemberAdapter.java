package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.MembersEntity;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 群 所有成员-adpter
 */

public class TeamAllMemberAdapter extends FKAdapter<MembersEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public TeamAllMemberAdapter(AbsListView view, List<MembersEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
    }
    public void convert(AdapterHolder helper, MembersEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, MembersEntity item, final int position){
        this.mHelper=helper;
        mHelper.setText(R.id.txt_family_name,item.getSurname()+item.getName());
        ImageView imageView=(ImageView) mHelper.getView(R.id.city_sponsor);
        imageView.setVisibility(View.GONE);
        if (item.getIs_super()>0){
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:item.is_super== FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).asBitmap().into(imageView);
        }
        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.img_family_picture));
    }
}
