package com.yuejian.meet.adapters.clan;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.ZuciFootprintsEntity;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.StringUtils;

import java.util.List;

public class ZuciFootprintsAdapter extends FKAdapter<ZuciFootprintsEntity> {
    private Context context;
    private AdapterHolder mHelper;

    public ZuciFootprintsAdapter(AbsListView paramAbsListView, List<ZuciFootprintsEntity> paramList, int paramInt) {
        super(paramAbsListView, paramList, paramInt);
        this.context = paramAbsListView.getContext();
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, final ZuciFootprintsEntity paramZuciFootprintsEntity, int paramInt) {
        boolean bool = false;
        this.mHelper = paramAdapterHolder;
        paramAdapterHolder.setText(R.id.footprints_name, paramZuciFootprintsEntity.getSurname() + paramZuciFootprintsEntity.getName());
        mHelper.getView(R.id.item_footprints_age).setSelected(paramZuciFootprintsEntity.getSex().equals("0")?false:true);
        paramAdapterHolder.setText(R.id.item_footprints_age, " " + paramZuciFootprintsEntity.getAge());
        View localView = paramAdapterHolder.getView(R.id.item_footprints_chat);
        localView.setVisibility(View.VISIBLE);
        if (paramZuciFootprintsEntity.getCustomer_id().equals(AppConfig.CustomerId)) {
            localView.setVisibility(View.INVISIBLE);
        }
        paramAdapterHolder.setText(R.id.item_footprints_tiem, " " + StringUtils.friendlyTime(new StringBuilder().append(paramZuciFootprintsEntity.getCreate_time()).append("").toString()));
        Glide.with(this.context).load(paramZuciFootprintsEntity.getPhoto()).asBitmap().error(R.mipmap.ic_default).into((ImageView)paramAdapterHolder.getView(R.id.footprints_photo));
        paramAdapterHolder.getView(R.id.item_footprints_chat).setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                ImUtils.toP2PCaht(ZuciFootprintsAdapter.this.mCxt, paramZuciFootprintsEntity.getCustomer_id());
            }
        });
    }

    public void convert(AdapterHolder paramAdapterHolder, ZuciFootprintsEntity paramZuciFootprintsEntity, boolean paramBoolean, int paramInt)
    {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramZuciFootprintsEntity, paramInt);
    }
}
