package com.yuejian.meet.adapters.clan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.zuci.ZuciCollectionActivity;
import com.yuejian.meet.activities.zuci.ZuciInfoActivity;
import com.yuejian.meet.activities.zuci.ZuciNearbyActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.BugTypeEntity;
import com.yuejian.meet.bean.ZuciCollectionEntity;

import java.util.List;

//import com.yuejian.meet.activities.family.ClanSearchActivity;

public class ZuciCollectionAdapter extends FKAdapter<ZuciCollectionEntity> {
    private Context context;
    private AdapterHolder mHelper;
    private String bandingId;
    private Boolean isBandinZuci=false;

    public ZuciCollectionAdapter(AbsListView paramAbsListView, List<ZuciCollectionEntity> paramList, int paramInt)
    {
        super(paramAbsListView, paramList, paramInt);
        this.context = paramAbsListView.getContext();
    }
    public void setBandinId(String id,Boolean isBandinZuci){
        bandingId=id;
        this.isBandinZuci=isBandinZuci;
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, final ZuciCollectionEntity item, final int paramInt)
    {
        boolean bool2 = true;
        this.mHelper = paramAdapterHolder;
        paramAdapterHolder.setText(R.id.item_collcst_name, item.getName());
        if (null==item.getIs_collection()){
            mHelper.setText(R.id.item_zuci_collection,"已收藏");
        }else {
            mHelper.setText(R.id.item_zuci_collection,item.getIs_collection().equals("0")?"收藏":"已收藏");
        }
        if (isBandinZuci) {
            mHelper.getView(R.id.item_collect_top).setVisibility(View.GONE);
            mHelper.getView(R.id.item_zuci_collection).setVisibility(View.GONE);
            mHelper.getView(R.id.banding_zuci).setVisibility(View.VISIBLE);
            if (StringUtil.isEmpty(item.getClan_hall_id()) || StringUtil.isEmpty(bandingId)){
                mHelper.getView(R.id.banding_zuci).setSelected(false);
            }else {
                mHelper.getView(R.id.banding_zuci).setSelected(bandingId.equals(item.getClan_hall_id())?true:false);
            }
        }

        paramAdapterHolder.setText(R.id.item_collcst_loation, item.getDistance() + "km");
        mHelper.getView(R.id.item_collcst_loation).setVisibility(item.getDistance().equals("-1")?View.GONE:View.VISIBLE);
        mHelper.setText(R.id.item_collect_top,item.getIs_top().equals("0")?"置顶首页":"已置顶");
        Glide.with(this.context).load(item.getFirst_figure()).asBitmap().into((ImageView)paramAdapterHolder.getView(R.id.item_collect_photo));
        this.mHelper.getView(R.id.item_collect_top).setOnClickListener(new OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if ((ZuciCollectionAdapter.this.mCxt instanceof ZuciCollectionActivity)) {
                    ((ZuciCollectionActivity)ZuciCollectionAdapter.this.mCxt).postTop(paramInt);
                }
                if ((ZuciCollectionAdapter.this.mCxt instanceof ZuciNearbyActivity)) {
                    ((ZuciNearbyActivity)ZuciCollectionAdapter.this.mCxt).postTop(paramInt);
                    return;
                }
//                    if (ZuciCollectionAdapter.this.mCxt instanceof ClanSearchActivity){
//                        ((ClanSearchActivity)ZuciCollectionAdapter.this.mCxt).postTop(paramInt);
//                    }

            }
        });
        mHelper.getView(R.id.item_zuci_collection).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ZuciCollectionAdapter.this.mCxt instanceof ZuciCollectionActivity)) {
                    ((ZuciCollectionActivity)ZuciCollectionAdapter.this.mCxt).postCollection(paramInt);
                }
                if ((ZuciCollectionAdapter.this.mCxt instanceof ZuciNearbyActivity)) {
                    ((ZuciNearbyActivity)ZuciCollectionAdapter.this.mCxt).postCollection(paramInt);
                    return;
                }

            }
        });
        this.mHelper.getConvertView().setOnClickListener(new OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Intent localIntent = new Intent(ZuciCollectionAdapter.this.mCxt, ZuciInfoActivity.class);
//                if (StringUtil.isEmpty(item.getClan_hall_id())) {
                    localIntent.putExtra("id", item.getId());
                    localIntent.putExtra("zuciName", item.getName());
                    ZuciCollectionAdapter.this.mCxt.startActivity(localIntent);
//                }
            }
        });
        mHelper.getView(R.id.banding_zuci).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("");
                builder.setMessage("确定绑定此祖祠到宗会吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BusCallEntity entity=new BusCallEntity();
                        entity.setData(item.getName());
                        entity.setId(item.getId());
                        entity.setCallType(BusEnum.ZUCI_SELECT);
                        Bus.getDefault().post(entity);
                        ((Activity)context).finish();
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();

            }
        });
    }

    public void convert(AdapterHolder paramAdapterHolder, ZuciCollectionEntity paramZuciCollectionEntity, boolean paramBoolean, int paramInt)
    {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramZuciCollectionEntity, paramInt);
    }
}
