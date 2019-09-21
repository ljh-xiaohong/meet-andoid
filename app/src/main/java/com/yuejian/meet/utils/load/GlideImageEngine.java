package com.yuejian.meet.utils.load;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.maning.imagebrowserlibrary.ImageEngine;

public class GlideImageEngine implements ImageEngine {
    @Override
    public void loadImage(Context context, String url, ImageView imageView, View progressView) {
        Glide.with(context)
                .load(url)
                .fitCenter()
                .into(imageView);
    }
}
