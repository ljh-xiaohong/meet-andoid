package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.ReleaseActionActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.utils.DensityUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 发布adpter
 */

public class ReleaseActionAdapter extends FKAdapter<PhotoInfo> {
    private AdapterHolder mHelper;
    private Context context;
    private int mWidth=0;


    public ReleaseActionAdapter(AbsListView view, List<PhotoInfo> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context=view.getContext();
        mWidth = (DensityUtils.getScreenW(mCxt) - DensityUtils.dip2px(mCxt, 84)) / 4;
    }
    public void convert(AdapterHolder helper, PhotoInfo item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, PhotoInfo item, final int position){
        this.mHelper=helper;
        ImageView select_img=helper.getView(R.id.img_dynamic_pic_item);
        select_img.getLayoutParams().width = mWidth;
        select_img.getLayoutParams().height = mWidth;
        Glide.with(context).load(item.getFilePath()).asBitmap().placeholder(R.mipmap.ic_default).into(select_img);
        mHelper.getView(R.id.release_img_del).setVisibility(View.VISIBLE);
        mHelper.getView(R.id.release_img_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ReleaseActionActivity)context).delPhotos(position);
            }
        });
    }
}
