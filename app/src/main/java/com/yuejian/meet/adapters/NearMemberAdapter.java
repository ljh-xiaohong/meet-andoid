package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.find.FindMemberActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.FindMemberEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.utils.StringUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 附近宗亲成员adpter
 */

public class NearMemberAdapter extends FKAdapter<FindMemberEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public NearMemberAdapter(AbsListView view, List<FindMemberEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
    }

    public void convert(AdapterHolder helper, FindMemberEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(AdapterHolder helper, final FindMemberEntity item, final int position) {
        if (0 == item.is_family_master) {
            helper.getView(R.id.find_near_faqr).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.find_near_faqr).setVisibility(View.VISIBLE);
        }
        helper.setText(R.id.tex_company_name,item.getCompany_name()+(StringUtils.isEmpty(item.getCompany_name())?"":"  ")+item.getJob());
        helper.setText(R.id.txt_find_near_member_name,item.getSurname()+item.getName());
        helper.setText(R.id.txt_find_clan_postion,item.getDistance()+"km");
        final CheckBox checkBox=(CheckBox)helper.getView(R.id.cb_find_near_sel);
        checkBox.setChecked(item.getSelect());
        Glide.with(mCxt).load(item.getPhoto()).asBitmap().thumbnail(0.1f).into((ImageView) helper.getView(R.id.img_find_near_member_header));
        helper.getView(R.id.item_near_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
                item.setSelect(checkBox.isChecked());
                ((FindMemberActivity)mCxt).isUnSelAll();
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setSelect(checkBox.isChecked());
                ((FindMemberActivity)mCxt).isUnSelAll();
            }
        });
    }
}
