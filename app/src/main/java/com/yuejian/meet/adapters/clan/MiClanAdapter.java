package com.yuejian.meet.adapters.clan;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanInfoActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.MoreClanEntity;

import java.util.List;

//import com.yuejian.meet.activities.clan.ClanInfoActivity;

public class MiClanAdapter extends FKAdapter<MoreClanEntity> {
    List<MoreClanEntity> list;
    private AdapterHolder mHelper;
    private int selectCount = 0;

    public MiClanAdapter(AbsListView paramAbsListView, List<MoreClanEntity> paramList, int paramInt) {
        super(paramAbsListView, paramList, paramInt);
        this.list = paramList;
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, final MoreClanEntity paramMoreClanEntity, int paramInt) {
        this.mHelper = paramAdapterHolder;
        String localObject;
        mHelper.getView(R.id.item_association_status).setVisibility(paramMoreClanEntity.getAssociation_status().equals("0")?View.VISIBLE:View.GONE);
        if (paramInt == getPositionForSection(getSectionForPosition(paramInt)))
        {
            paramAdapterHolder.getView(R.id.item_clan_title).setVisibility(View.VISIBLE);
            if (paramMoreClanEntity.getIs_MiClan().equals("N")){
                localObject = "我加入的宗亲会";
                paramAdapterHolder.setText(R.id.txt_mi_clan_title, localObject);
            }
        }

        paramAdapterHolder.setText(R.id.item_img_more_clan_name, paramMoreClanEntity.getAssociation_name());
        paramAdapterHolder.setText(R.id.item_img_more_clan_count, paramMoreClanEntity.getAssociation_cnt() + "人");
        mHelper.setText(R.id.item_img_more_clan_loation,paramMoreClanEntity.getKm());
        mHelper.getView(R.id.item_img_more_clan_loation).setVisibility(paramMoreClanEntity.getKm().equals("-1")?View.GONE:View.VISIBLE);
        Glide.with(this.mCxt).load(paramMoreClanEntity.getAssociation_img()).asBitmap().into((ImageView)paramAdapterHolder.getView(R.id.item_img_more_clan_photo));
        paramAdapterHolder.getConvertView().setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Intent intent = new Intent(MiClanAdapter.this.mCxt, ClanInfoActivity.class);
                intent.putExtra("clanEntity", paramMoreClanEntity);
                MiClanAdapter.this.mCxt.startActivity(intent);
            }
        });
//            paramAdapterHolder.getView(2131821941).setVisibility(View.GONE);
    }

    public void convert(AdapterHolder paramAdapterHolder, MoreClanEntity paramMoreClanEntity, boolean paramBoolean, int paramInt) {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramMoreClanEntity, paramInt);
    }

    public int getPositionForSection(int paramInt) {
        int i = 0;
        while (i < getCount()) {
            if (((MoreClanEntity)this.list.get(i)).getIs_MiClan().toUpperCase().charAt(0) == paramInt) {
                return i;
            }
            i += 1;
        }
        return -1;
    }

    public int getSectionForPosition(int paramInt) {
        return ((MoreClanEntity)this.list.get(paramInt)).getIs_MiClan().charAt(0);
    }

    public void setListDate(List<MoreClanEntity> paramList)
    {
        this.list = paramList;
    }
}
