package com.yuejian.meet.ui;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.youth.banner.loader.ImageLoader;

/**
 * @author : g000gle
 * @time : 2019/6/4 15:30
 * @desc : 随笔发布预览轮播图加载器
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object o, ImageView imageView) {
        if (o instanceof PhotoInfo) {
            PhotoInfo photoInfo = (PhotoInfo) o;
            Glide.with(context).load(photoInfo.getAbsolutePath()).into(imageView);
        }
    }
}
