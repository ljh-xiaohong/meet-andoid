package com.yuejian.meet.activities.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.aliyun.svideo.editor.util.Common;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.custom.view.RoundAngleImageView;
import com.yuejian.meet.bean.Image;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.GlideUtils;

import java.util.List;

/**
 * 图片选择设配器
 * Created by fs-ljh on 2017/10/20.
 */
public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

    private List<Image> titles;
    private Context context;
    private boolean isUpdate;

    private OnClickPic onClickPic;
    //接口回调
    public interface OnClickPic{
        public void clickPic(int position);
        public void delectPic(int position);
    }
    public void setClickPic(OnClickPic onClickPic) {
        this.onClickPic = onClickPic;
    }
    public PicAdapter(Context context, List<Image> titles, boolean isUpdate) {
        this.titles = titles;
        this.context = context;
        this.isUpdate = isUpdate;
    }

    @Override
    public int getItemCount() {
        return titles == null ? 0 : titles.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isUpdate) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.pic_item1, parent, false));
        }else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.pic_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (CommonUtil.isNull(titles.get(titles.size()-1).getPath())){
            if (CommonUtil.isNull(titles.get(position).getPath())) {
                GlideUtils.displayNative(holder.frist_strote_pic1_iv, R.mipmap.dongtai_add_picture);
                holder.frist_strote_pic1_delect_iv.setVisibility(View.GONE);
            } else {
                GlideUtils.display(holder.frist_strote_pic1_iv, titles.get(position).getPath());
                holder.frist_strote_pic1_delect_iv.setVisibility(View.VISIBLE);
            }
        }else {
            if (isUpdate){
                holder.frist_strote_pic1_delect_iv.setVisibility(View.VISIBLE);
                holder.frist_strote_pic1_iv.setVisibility(View.VISIBLE);
                if (CommonUtil.isNull(titles.get(position).getPath())) {
                    GlideUtils.displayNative(holder.frist_strote_pic1_iv, R.mipmap.dongtai_add_picture);
                    holder.frist_strote_pic1_delect_iv.setVisibility(View.GONE);
                } else {
                    GlideUtils.display(holder.frist_strote_pic1_iv, titles.get(position).getPath());
                }
            }else {
                holder.frist_strote_pic1_delect_iv.setVisibility(View.GONE);
                holder.frist_strote_pic1_iv1.setVisibility(View.VISIBLE);
                if (CommonUtil.isNull(titles.get(position).getPath())) {
                    GlideUtils.displayNative(holder.frist_strote_pic1_iv1, R.mipmap.dongtai_add_picture);
                } else {
                    GlideUtils.display(holder.frist_strote_pic1_iv1, titles.get(position).getPath());
                }
            }
        }
        holder.frist_strote_pic1_delect_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPic.delectPic(position);
            }
        });
        holder.frist_strote_pic1_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPic.clickPic(position);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout frist_strote_pic1_lay;
        ImageView frist_strote_pic1_iv;
        RoundAngleImageView frist_strote_pic1_iv1;
        ImageView frist_strote_pic1_delect_iv;
        public ViewHolder(View itemView) {
            super(itemView);
            frist_strote_pic1_lay = (LinearLayout) itemView.findViewById(R.id.frist_strote_pic1_lay);
            frist_strote_pic1_iv = (ImageView) itemView.findViewById(R.id.frist_strote_pic1_iv);
            frist_strote_pic1_iv1 = (RoundAngleImageView) itemView.findViewById(R.id.frist_strote_pic1_iv1);
            frist_strote_pic1_delect_iv = (ImageView) itemView.findViewById(R.id.frist_strote_pic1_delect_iv);
        }
        public void setVisibility(boolean isVisible){
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            if (isVisible){
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            }else{
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }

}
