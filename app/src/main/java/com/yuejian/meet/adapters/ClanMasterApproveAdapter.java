package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanMasterApproveActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.ClanFindApprovaEntity;

import java.util.List;

public class ClanMasterApproveAdapter
        extends FKAdapter<ClanFindApprovaEntity>
{
    private Context context;
    private AdapterHolder mHelper;

    public ClanMasterApproveAdapter(AbsListView paramAbsListView, List<ClanFindApprovaEntity> paramList, int paramInt)
    {
        super(paramAbsListView, paramList, paramInt);
        this.context = paramAbsListView.getContext();
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, final ClanFindApprovaEntity paramClanFindApprovaEntity, int paramInt)
    {
        this.mHelper = paramAdapterHolder;
        paramAdapterHolder.setText(R.id.item_approve_name, paramClanFindApprovaEntity.getSurname() + paramClanFindApprovaEntity.getName());
        paramAdapterHolder.setText(R.id.item_approve_age, " " + paramClanFindApprovaEntity.age);
        mHelper.getView(R.id.item_approve_age).setSelected(paramClanFindApprovaEntity.getSex().equals("1")?true:false);
        Glide.with(this.context).load(paramClanFindApprovaEntity.getPhoto()).placeholder(R.mipmap.ic_default).into((ImageView)this.mHelper.getView(R.id.item_approve_photo));
        paramAdapterHolder.getView(R.id.item_no_pass).setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                ((ClanMasterApproveActivity)ClanMasterApproveAdapter.this.mCxt).approvaNoPass(paramClanFindApprovaEntity.getMember_id());
            }
        });
        paramAdapterHolder.getView(R.id.item_pass).setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                ((ClanMasterApproveActivity)ClanMasterApproveAdapter.this.mCxt).approvaPass(paramClanFindApprovaEntity.getMember_id());
            }
        });
    }

    public void convert(AdapterHolder paramAdapterHolder, ClanFindApprovaEntity paramClanFindApprovaEntity, boolean paramBoolean, int paramInt)
    {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramClanFindApprovaEntity, paramInt);
    }
}
