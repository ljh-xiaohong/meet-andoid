package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanMemberActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.ClanFindApprovaEntity;

import java.util.List;

public class ClanMasterMemberAdapter extends FKAdapter<ClanFindApprovaEntity> {
    private Context context;
    private AdapterHolder mHelper;
    int type;

    public ClanMasterMemberAdapter(AbsListView paramAbsListView, List<ClanFindApprovaEntity> paramList, int paramInt1, int paramInt2)
    {
        super(paramAbsListView, paramList, paramInt1);
        this.context = paramAbsListView.getContext();
        this.type = paramInt2;
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, final ClanFindApprovaEntity paramClanFindApprovaEntity, int paramInt)
    {
        this.mHelper = paramAdapterHolder;
        paramAdapterHolder.setText(R.id.item_clan_member_name, paramClanFindApprovaEntity.getSurname() + paramClanFindApprovaEntity.getName());
        paramAdapterHolder.setText(R.id.item_clan_member_age, " " + paramClanFindApprovaEntity.age);
        paramAdapterHolder.getView(R.id.item_clan_member_age).setSelected(paramClanFindApprovaEntity.getSex().equals("1")?true:false);
        if (this.type == 1 ) {
            paramAdapterHolder.getView(R.id.item_clan_member_remoe).setVisibility(View.VISIBLE);
        }else {
            paramAdapterHolder.getView(R.id.item_clan_member_remoe).setVisibility(View.GONE);
        }
        Glide.with(this.context).load(paramClanFindApprovaEntity.getPhoto()).placeholder(R.mipmap.ic_default).into((ImageView)this.mHelper.getView(R.id.item_clan_member_photo));
            paramAdapterHolder.getView(R.id.item_clan_member_remoe).setOnClickListener(new OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
                    ((ClanMemberActivity)ClanMasterMemberAdapter.this.mCxt).remoeMember(paramClanFindApprovaEntity);
                }
            });
            paramAdapterHolder.getConvertView().setOnClickListener(new OnClickListener()
            {
                public void onClick(View paramAnonymousView)
                {
//                    Intent intent = new Intent(ClanMasterMemberAdapter.this.context, WebActivity.class);
//                    intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + paramClanFindApprovaEntity.customer_id);
//                    ClanMasterMemberAdapter.this.context.startActivity(intent);
                }
            });
    }

    public void convert(AdapterHolder paramAdapterHolder, ClanFindApprovaEntity paramClanFindApprovaEntity, boolean paramBoolean, int paramInt)
    {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramClanFindApprovaEntity, paramInt);
    }
}
