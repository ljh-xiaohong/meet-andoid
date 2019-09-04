package com.yuejian.meet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NoticeInfoAdapter extends FKAdapter<String> {
    private Context context;
    List<String> list = new ArrayList();
    private AdapterHolder mHelper;

    public NoticeInfoAdapter(AbsListView paramAbsListView, List<String> paramList, int paramInt) {
        super(paramAbsListView, paramList, paramInt);
        this.context = paramAbsListView.getContext();
        this.list = paramList;
    }
    public void setLsit( List<String> paramList){
        this.list=paramList;
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, String paramString, final int paramInt)
    {
        this.mHelper = paramAdapterHolder;
        Glide.with(this.context).load(paramString).placeholder(R.mipmap.ic_default).into((ImageView)this.mHelper.getView(R.id.item_notice_info_photo));
        paramAdapterHolder.getConvertView().setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Utils.displayImages((Activity)NoticeInfoAdapter.this.mCxt, list, paramInt, null);
            }
        });
    }

    public void convert(AdapterHolder paramAdapterHolder, String paramString, boolean paramBoolean, int paramInt)
    {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramString, paramInt);
    }
}
