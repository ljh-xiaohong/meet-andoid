package com.yuejian.meet.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.SuibiEditActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : g000gle
 * @time : 2019/5/23 15:59
 * @desc : 发布随笔图片列表适配器
 */
public class EditSuibiImageListAdapter extends RecyclerView.Adapter<EditSuibiImageListAdapter.EditSuibiImageViewHolder> {

    private Activity mContext;
    private List<PhotoInfo> mPhotoInfoList;
    private OnEditSuibiImageListItemClickListener mItemClickListener;

    public EditSuibiImageListAdapter(Activity context, List<PhotoInfo> photoInfoList, OnEditSuibiImageListItemClickListener itemClickListener) {
        mContext = context;
        mPhotoInfoList = photoInfoList;
        mItemClickListener = itemClickListener;
    }

    @Override
    public EditSuibiImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_dynamic_img_item, parent, false);
        return new EditSuibiImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EditSuibiImageViewHolder holder, int position) {
        holder.mDeleteView.setVisibility(View.GONE);
        holder.mAddView.setVisibility(View.GONE);
        holder.mImageView.setVisibility(View.GONE);

        if (mPhotoInfoList.size() == position) {
            holder.mAddView.setVisibility(View.VISIBLE);
            holder.mImageView.setImageResource(R.mipmap.add_picture);
            holder.mAddView.setOnClickListener(v -> mItemClickListener.onImageItemClick(0));
        } else {
            holder.mImageView.setVisibility(View.VISIBLE);
            holder.mDeleteView.setVisibility(View.VISIBLE);
            try {
                Glide.with(mContext).load(mPhotoInfoList.get(position).getFilePath()).asBitmap().placeholder(R.mipmap.ic_default).into(holder.mImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 调用Activity删除方法
        holder.mDeleteView.setOnClickListener(v -> {
            if (mContext instanceof SuibiEditActivity) {
                ((SuibiEditActivity) mContext).deletePhotos(position);
            }
        });
        //照片预览
        holder.mImageView.setOnClickListener(v -> {
            List<String> list = new ArrayList<>();
            for (PhotoInfo info : mPhotoInfoList) {
                list.add(info.getFilePath());
            }
            com.yuejian.meet.utils.Utils.displayImages(mContext, list, position, null);
        });
    }

    /**
     * 不足9张照片时，加一个添加项
     * @return
     */
    @Override
    public int getItemCount() {
        if (mPhotoInfoList == null) return 0;
        return mPhotoInfoList.size() < 9 ? mPhotoInfoList.size() + 1 : 9;
    }

    /**
     * 刷新列表
     * @param photoInfoList 新数据
     */
    public void refresh(List<PhotoInfo> photoInfoList) {
        this.mPhotoInfoList = photoInfoList;
        notifyDataSetChanged();
    }

    public interface OnEditSuibiImageListItemClickListener {
        void onImageItemClick(int type);
    }

    class EditSuibiImageViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        ImageView mAddView;
        ImageView mDeleteView;

        public EditSuibiImageViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.img_dynamic_pic_item);
            mAddView = (ImageView) itemView.findViewById(R.id.img_dynamic_pic_sel);
            mDeleteView = (ImageView) itemView.findViewById(R.id.release_img_del);
        }
    }
}
