package com.yuejian.meet.adapters.clan;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.ClanPhotoArrlyEntity;
import com.yuejian.meet.bean.ClanPhotoEntity;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.InnerGridView;
import com.yuejian.meet.widgets.gallery.ViewPagerGallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ClanPhotoAdapter extends FKAdapter<ClanPhotoArrlyEntity> {
    private Context context;
    private AdapterHolder mHelper;
    public String year;

    public ClanPhotoAdapter(AbsListView paramAbsListView, List<ClanPhotoArrlyEntity> paramList, int paramInt) {
        super(paramAbsListView, paramList, paramInt);
        this.context = paramAbsListView.getContext();
        this.year = TimeUtil.getFormatDatetimeYear(System.currentTimeMillis());
    }

    private void initNearByData(AdapterHolder paramAdapterHolder, ClanPhotoArrlyEntity paramClanPhotoArrlyEntity, int paramInt) {
        this.mHelper = paramAdapterHolder;
        String datatiem=null;
        if (paramClanPhotoArrlyEntity.getYear().equals(this.year)) {
            datatiem= paramClanPhotoArrlyEntity.getMonth() + "月";
        }else {
            datatiem=paramClanPhotoArrlyEntity.getYear()+"年"+paramClanPhotoArrlyEntity.getMonth() + "月";
        }
        paramAdapterHolder.setText(R.id.date,datatiem);
        InnerGridView gridView = (InnerGridView)paramAdapterHolder.getView(R.id.photo_grid);
        ClanPhotoGrild clanPhotoGrild = new ClanPhotoGrild(gridView,paramClanPhotoArrlyEntity.getListPhoto(), R.layout.item_notice_info_img_layout);
        gridView.setAdapter(clanPhotoGrild);
        clanPhotoGrild.refresh(paramClanPhotoArrlyEntity.getListPhoto());
    }

    public void convert(AdapterHolder paramAdapterHolder, ClanPhotoArrlyEntity paramClanPhotoArrlyEntity, boolean paramBoolean, int paramInt) {
        convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
        initNearByData(paramAdapterHolder, paramClanPhotoArrlyEntity, paramInt);
    }

    class ClanPhotoGrild extends FKAdapter<ClanPhotoEntity> {
        List<ClanPhotoEntity> tmps;

        public ClanPhotoGrild(AbsListView view,List<ClanPhotoEntity> paramList, int paramInt) {
            super(view,paramList, paramInt);
            this.tmps = paramList;
        }

        public void convert(AdapterHolder paramAdapterHolder, ClanPhotoEntity paramClanPhotoEntity, boolean paramBoolean, final int paramInt) {
            convert(paramAdapterHolder, getItem(paramInt), paramBoolean);
            Glide.with(this.mCxt).load(paramClanPhotoEntity.getPhoto_img()).into((ImageView)paramAdapterHolder.getView(R.id.item_notice_info_photo));
            paramAdapterHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    List<String> list = new ArrayList();
                    Iterator localIterator = ClanPhotoAdapter.ClanPhotoGrild.this.tmps.iterator();
                    while (localIterator.hasNext()) {
                        list.add(((ClanPhotoEntity)localIterator.next()).getPhoto_img());
                    }
                    Utils.displayImagesClan((Activity)mCxt, list, paramInt, null, new ViewPagerGallery.GalleryDelOnClickListener() {
                        public void onBack(final int paramAnonymous2Int, final ViewPagerGallery.GalleryOnClickListener paramAnonymous2GalleryOnClickListener) {
                            final Builder localBuilder = new Builder(ClanPhotoAdapter.ClanPhotoGrild.this.mCxt);
                            localBuilder.setTitle("提示");
                            localBuilder.setMessage("确定删除此照片");
                            localBuilder.setNeutralButton("取消", null);
                            localBuilder.setNegativeButton("确定", new OnClickListener() {
                                public void onClick(DialogInterface paramAnonymous3DialogInterface, int paramAnonymous3Int) {
                                    paramAnonymous3DialogInterface.dismiss();
                                    Map<String,Object> params = new HashMap();
                                    params.put("photo_id", ((ClanPhotoEntity)ClanPhotoAdapter.ClanPhotoGrild.this.tmps.get(paramAnonymous2Int)).getPhoto_id());
                                    new ApiImp().clanPhotoDelete(params, this, new DataIdCallback<String>() {
                                        public void onFailed(String paramAnonymous4String1, String paramAnonymous4String2, int paramAnonymous4Int) {}

                                        public void onSuccess(String paramAnonymous4String, int paramAnonymous4Int) {
                                            ClanPhotoAdapter.ClanPhotoGrild.this.refresh(ClanPhotoAdapter.ClanPhotoGrild.this.tmps);
                                        }
                                    });
                                }
                            });
                            localBuilder.show();
                        }

                        public void onClick(int paramAnonymous2Int) {}
                    });
                }
            });
        }
    }
}
