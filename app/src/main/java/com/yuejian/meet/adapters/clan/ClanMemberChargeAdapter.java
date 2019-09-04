package com.yuejian.meet.adapters.clan;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.utils.Utils;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanMemberChargeSelActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.ClanFindApprovaEntity;

import java.util.List;

public class ClanMemberChargeAdapter extends FKAdapter<ClanFindApprovaEntity> {
    List<ClanFindApprovaEntity> list;
    private AdapterHolder mHelper;
    private int selectCount = 0;

    public ClanMemberChargeAdapter(AbsListView paramAbsListView, List<ClanFindApprovaEntity> paramList, int paramInt)
    {
        super(paramAbsListView, paramList, paramInt);
        this.list = paramList;
    }

    private void initNearByData(final AdapterHolder holder, final ClanFindApprovaEntity item, int position)
    {
        boolean bool2 = true;
        this.mHelper = holder;
        holder.setText(R.id.member_age,item.getAge());
        holder.getView(R.id.member_age).setSelected(item.sex.equals("1")?true:false);
        holder.setText(R.id.text_member_name,item.getSurname()+item.getName());
        Glide.with(this.mCxt.getApplicationContext()).load(item.getPhoto()).override(Utils.dp2px(this.mCxt, 33.0F), Utils.dp2px(this.mCxt, 33.0F)).into((ImageView)this.mHelper.getView(R.id.img_member_icon));
        holder.getView(R.id.member_check_box).setSelected(item.getSelect());
        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.getView(R.id.contact_letter).setVisibility(View.VISIBLE);
            holder.setText(R.id.contact_letter,item.getSortLetters());
            holder.getView(R.id.itemt_wire).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.itemt_wire).setVisibility(View.VISIBLE);
            holder.getView(R.id.contact_letter).setVisibility(View.GONE);
        }
        this.mHelper.getView(R.id.member_check_box).setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                item.setSelect(!item.getSelect());
                ((CheckBox)holder.getView(R.id.member_check_box)).setChecked(item.getSelect());
                ((ClanMemberChargeSelActivity)ClanMemberChargeAdapter.this.mCxt).isSelectContact(item);
                return;
            }
        });
        holder.getConvertView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                item.setSelect(!item.getSelect());
                ((CheckBox)holder.getView(R.id.member_check_box)).setChecked(item.getSelect());
                ((ClanMemberChargeSelActivity)ClanMemberChargeAdapter.this.mCxt).isSelectContact(item);
                return;
            }
        });

    }

    public void convert(AdapterHolder paramAdapterHolder, ClanFindApprovaEntity paramClanFindApprovaEntity, boolean paramBoolean, int paramInt)
    {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramClanFindApprovaEntity, paramInt);
    }

    public int getPositionForSection(int paramInt)
    {
        int i = 0;
        while (i < getCount())
        {
            if (((ClanFindApprovaEntity)this.list.get(i)).getSortLetters().toUpperCase().charAt(0) == paramInt) {
                return i;
            }
            i += 1;
        }
        return -1;
    }

    public int getSectionForPosition(int paramInt)
    {
        return ((ClanFindApprovaEntity)this.list.get(paramInt)).getSortLetters().charAt(0);
    }

    public void setListDate(List<ClanFindApprovaEntity> paramList)
    {
        this.list = paramList;
    }
}
