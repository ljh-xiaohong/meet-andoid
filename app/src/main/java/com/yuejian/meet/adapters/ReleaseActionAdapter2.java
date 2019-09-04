package com.yuejian.meet.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.ClanNoticeReleaseActivity;
import com.yuejian.meet.activities.clan.ClanPhotoReleaseActivity;
import com.yuejian.meet.activities.clan.CreateMiClanActivity;
import com.yuejian.meet.activities.home.ReleaseActionActivity;
import com.yuejian.meet.activities.mine.UserFeedbackActivity;
import com.yuejian.meet.activities.zuci.CreateZuciActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 发布adpter
 */

public class ReleaseActionAdapter2 extends BaseAdapter{
    private AdapterHolder mHelper;
    public List<PhotoInfo> listDatas;
    public LayoutInflater inflater;
    private Activity context;
    private int mWidth=0;


    public ReleaseActionAdapter2(Activity context, List<PhotoInfo> mDatas) {
        this.inflater=LayoutInflater.from(context);
        this.context=context;
        this.listDatas=mDatas;
        mWidth = (DensityUtils.getScreenW(context) - DensityUtils.dip2px(context, 84)) / 4;
    }
    public void refresh(List<PhotoInfo> mDatas){
        this.listDatas=mDatas;
        notifyDataSetChanged();
    }
    public void convert(AdapterHolder helper, PhotoInfo item, boolean isScrolling, int position) {
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
                if (context instanceof ReleaseActionActivity){
                    ((ReleaseActionActivity)context).delPhotos(position);
                }else if(context instanceof CreateZuciActivity){
                    ((CreateZuciActivity)context).delPhotos(position);
                }else if(context instanceof CreateMiClanActivity){
                    ((CreateMiClanActivity)context).delPhotos(position);
                }else if (context instanceof UserFeedbackActivity){
                    ((UserFeedbackActivity)context).delPhotos(position);
                }else if (context instanceof ClanNoticeReleaseActivity){
                    ((ClanNoticeReleaseActivity)context).delPhotos(position);
                }else if (context instanceof ClanPhotoReleaseActivity){
                    ((ClanPhotoReleaseActivity)context).delPhotos(position);
                }

            }
        });
    }

    @Override
    public int getCount() {
        return listDatas.size()<9?listDatas.size()+1:9;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView=inflater.inflate(R.layout.layout_dynamic_img_item,null);
            holder = new ViewHolder();
            holder.mImageView= (ImageView) convertView.findViewById(R.id.img_dynamic_pic_item);
            holder.release_img_del= (ImageView) convertView.findViewById(R.id.release_img_del);
            holder.img_dynamic_pic_sel= (ImageView) convertView.findViewById(R.id.img_dynamic_pic_sel);
            holder.mImageView.getLayoutParams().width=mWidth;
            holder.mImageView.getLayoutParams().height=mWidth;
            holder.img_dynamic_pic_sel.getLayoutParams().width=mWidth;
            holder.img_dynamic_pic_sel.getLayoutParams().height=mWidth;
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
            holder.mImageView.setImageBitmap(null);
        }
        holder.release_img_del.setVisibility(View.GONE);
        holder.img_dynamic_pic_sel.setVisibility(View.GONE);
        holder.mImageView.setVisibility(View.GONE);
         if (listDatas.size()==position){
             holder.img_dynamic_pic_sel.setVisibility(View.VISIBLE);
             holder.mImageView.setImageResource(R.mipmap.icon_shangchuan);
        }else {
             holder.mImageView.setVisibility(View.VISIBLE);
             holder.release_img_del.setVisibility(View.VISIBLE);
             try{
                 Glide.with(context).load(listDatas.get(position).getFilePath()).asBitmap().placeholder(R.mipmap.ic_default).into(holder.mImageView);
             }catch (Exception e){
                 e.printStackTrace();
             }
         }
        holder.release_img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof ReleaseActionActivity){
                    ((ReleaseActionActivity)context).delPhotos(position);
                }else if(context instanceof CreateZuciActivity){
                    ((CreateZuciActivity)context).delPhotos(position);
                }else if(context instanceof CreateMiClanActivity){
                    ((CreateMiClanActivity)context).delPhotos(position);
                }else if (context instanceof UserFeedbackActivity){
                    ((UserFeedbackActivity)context).delPhotos(position);
                }else if (context instanceof ClanNoticeReleaseActivity){
                    ((ClanNoticeReleaseActivity)context).delPhotos(position);
                }else if (context instanceof ClanPhotoReleaseActivity){
                    ((ClanPhotoReleaseActivity)context).delPhotos(position);
                }
//                if (context instanceof ReleaseActionActivity)
//                    ((ReleaseActionActivity)context).delPhotos(position);
//                else if (context instanceof UserFeedbackActivity){
//                    ((UserFeedbackActivity)context).delPhotos(position);
//                }
            }
        });
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list=new ArrayList<String>();
                for (PhotoInfo info:listDatas){
                    list.add(info.getFilePath());
                }
                com.yuejian.meet.utils.Utils.displayImages(context, list, position, null);
            }
        });
        return convertView;
    }
    public static class ViewHolder {
        ImageView mImageView;
        ImageView release_img_del;
        ImageView img_dynamic_pic_sel;
    }
}
