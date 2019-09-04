package com.yuejian.meet.activities.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.PicturePreviewActivity;
import com.yuejian.meet.activities.home.SelectImageActivity;
import com.yuejian.meet.bean.Image;
import com.yuejian.meet.recyclerview.CommonViewHolder;
import java.util.List;

/**
 * Description:
 * Data：9/4/2018-3:14 PM
 *
 * @author yanzhiwen
 */
public class ImageAdapter extends RecyclerView.Adapter<CommonViewHolder> {
    private Context mContext;
    private onSelectImageCountListener mSelectImageCountListener;
    private List<Image> mSelectImages;
    private List<Image> images;
    private int max=0;
    public ImageAdapter(Context context, List<Image> images, List<Image> selectedImages) {
        this.mContext = context;
        this.mSelectImages = selectedImages;
        this.images = images;
    }

    public void setSelectImageCountListener(onSelectImageCountListener selectImageCountListener) {
        mSelectImageCountListener = selectImageCountListener;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_list_image, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        if (!TextUtils.isEmpty(images.get(position).getPath())) {
            final ImageView chb_selected = holder.getView(R.id.iv_selected);
            final View maskView = holder.getView(R.id.mask);
            ImageView iv_image = holder.getView(R.id.iv_image);
            Glide.with(mContext).load(images.get(position).getPath()).into(iv_image);
            iv_image.setOnClickListener(v -> {
                mContext.startActivity(PicturePreviewActivity.newIntent(mContext, images.get(position).getPath()));
            });
            chb_selected.setOnClickListener(v -> {
                max=0;
                for (int i=0;i<mSelectImages.size();i++){
                    if (mSelectImages.get(i).isTake()){
                        max=max+1;
                    }
                }
                if(mSelectImages.size()>5){
                    if (!images.get(position).isSelect()) {
                        Toast.makeText(mContext, "图片最多" + (6 - max) + "张", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (images.get(position).isSelect()) {
                    images.get(position).setSelect(false);
                    mSelectImages.remove(images.get(position));
                    chb_selected.setSelected(false);
                    images.get(position).setSelect(false);
                    maskView.setVisibility(images.get(position).isSelect() ? View.VISIBLE : View.GONE);
                } else if (mSelectImages.size() < SelectImageActivity.MAX_SIZE) {
                    images.get(position).setSelect(true);
                    mSelectImages.add(images.get(position));
                    chb_selected.setSelected(true);
                    images.get(position).setSelect(true);
                    maskView.setVisibility(images.get(position).isSelect() ? View.VISIBLE : View.GONE);
                }
                if (mSelectImageCountListener != null) {
                    mSelectImageCountListener.onSelectImageCount(mSelectImages.size());
                    mSelectImageCountListener.onSelectImageList(mSelectImages);
                }
            });
            chb_selected.setSelected(images.get(position).isSelect());
            if (images.get(position).isSelect()) {
                maskView.setVisibility(View.VISIBLE);
            }else {
                maskView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public interface onSelectImageCountListener {
        void onSelectImageCount(int count);

        void onSelectImageList(List<Image> images);
    }
}
