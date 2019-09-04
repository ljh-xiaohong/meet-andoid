package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.InviteJoinGroupActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.GroupSameSurnameEntity;
import com.yuejian.meet.bean.MembersEntity;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 邀请入群聊天adpter
 */

public class InviteJoinGroupAdapter extends FKAdapter<GroupSameSurnameEntity> {
    private AdapterHolder mHelper;
    private Context context;


    public InviteJoinGroupAdapter(AbsListView view, List<GroupSameSurnameEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
    }
    public void convert(AdapterHolder helper, GroupSameSurnameEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(final AdapterHolder helper, final GroupSameSurnameEntity item, final int position){
        this.mHelper=helper;
        ((CheckBox)mHelper.getView(R.id.check_invite_sel)).setChecked(item.getChoose());
        mHelper.setText(R.id.txt_nivite_content,item.getSurname()+item.getName());
        mHelper.setText(R.id.txt_intive_sex," "+item.getAge());
        mHelper.getView(R.id.txt_intive_sex).setSelected(item.getSex().equals("1")?true:false);
        ImageView imageView=(ImageView) mHelper.getView(R.id.invite_join_fqr);
        imageView.setVisibility(View.GONE);
        if (item.getIs_praise()>0){
            imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.getIs_praise()== FqrEnum.city.getValue()?R.mipmap.ic_shi:item.getIs_praise()==FqrEnum.province.getValue()?R.mipmap.ic_guo:R.mipmap.ic_sheng).asBitmap().into(imageView);
        }
        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.img_nivite_head));
        mHelper.getView(R.id.check_invite_sel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setChoose(!item.getChoose());
                ((CheckBox)helper.getView(R.id.check_invite_sel)).setChecked(item.getChoose());
                ((InviteJoinGroupActivity)mCxt).selChcked();
            }
        });
    }
}
