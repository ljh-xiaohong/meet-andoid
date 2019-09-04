package com.yuejian.meet.widgets.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.utils.ZoomOutPageTransformer;
import com.yuejian.meet.widgets.PinchImageView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerGallery extends ViewPager {
    private Context mContext;
    private ViewGroup.LayoutParams mLayoutParams;
    private GalleryOnClickListener mGalleryOnClickListener;

    public ViewPagerGallery(Context context) {
        this(context, null);
    }

    public ViewPagerGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    private void init() {
        int pagerWidth = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 5.0f / 5.0f);
        mLayoutParams = this.getLayoutParams();
        if (mLayoutParams == null) {
            mLayoutParams = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            mLayoutParams.width = pagerWidth;
        }
        this.setLayoutParams(mLayoutParams);
        this.setPageMargin(-50);
        this.setPageTransformer(false, new ZoomOutPageTransformer());
        this.setClipChildren(false);
        this.setFitsSystemWindows(true);
        final ViewGroup parent = (ViewGroup) this.getParent();
        if (parent != null) {
            parent.setClipChildren(false);
            parent.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return dispatchTouchEvent(event);
                }
            });
        }

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i < getChildCount(); i++){
                    View view = getChildAt(i);
                    if (view instanceof PinchImageView) {
                        final PinchImageView imageView = (PinchImageView) view;
                        imageView.reset();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setImgUrls(List<String> imgUrls, int position) {
        if (imgUrls == null)
            throw new RuntimeException("Fuck, imgurls is null");
        List<View> views = new ArrayList<>();
        for (int i = 0; i < imgUrls.size(); i++) {
            String url = imgUrls.get(i);
            final PinchImageView imageView = new PinchImageView(mContext);
            Glide.with(mContext.getApplicationContext()).load(url).asBitmap().placeholder(R.mipmap.loading).error(R.mipmap.load_error).into(imageView);
            final int finalI = i;
            if (mGalleryOnClickListener != null) {
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGalleryOnClickListener.onClick(finalI);
                    }
                });
            }
            imageView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longClickListener != null) {
                        longClickListener.onLongClick(v);
                    }
                    return false;
                }
            });
            views.add(imageView);
        }
        this.setAdapter(new ViewPagerGalleryAdapter(views));
        init();
        if (position < imgUrls.size()) {
            this.setCurrentItem(position);
        }
        this.setOffscreenPageLimit(views.size());
    }

    public void setImgBitmaps(List<Bitmap> imgBitmaps, int position) {
        if (imgBitmaps == null)
            throw new RuntimeException("Fuck, imgResources is null");
        List<View> views = new ArrayList<>();
        for (int i = 0; i < imgBitmaps.size(); i++) {
            Bitmap bitmap = imgBitmaps.get(i);
            final PinchImageView imageView = new PinchImageView(mContext);
            imageView.setImageBitmap(bitmap);
            if (mGalleryOnClickListener != null) {
                final int finalI = i;
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGalleryOnClickListener.onClick(finalI);
                    }
                });
            }
            views.add(imageView);
        }
        this.setAdapter(new ViewPagerGalleryAdapter(views));
        init();
        if (position < imgBitmaps.size()) {
            this.setCurrentItem(position);
        }
        this.setOffscreenPageLimit(views.size());
    }


    public void setImgResources(List<Integer> imgResources, int position) {
        if (imgResources == null)
            throw new RuntimeException("Fuck, imgResources is null");
        List<View> views = new ArrayList<>();
        for (int i = 0; i < imgResources.size(); i++) {
            int id = imgResources.get(i);
            final PinchImageView imageView = new PinchImageView(mContext);
            imageView.setImageResource(id);
            if (mGalleryOnClickListener != null) {
                final int finalI = i;
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGalleryOnClickListener.onClick(finalI);
                    }
                });
            }
            views.add(imageView);
        }
        this.setAdapter(new ViewPagerGalleryAdapter(views));
        init();
        if (position < imgResources.size()) {
            this.setCurrentItem(position);
        }
        this.setOffscreenPageLimit(views.size());
    }

    public void setOnClickListener(GalleryOnClickListener galleryOnClickListener) {
        this.mGalleryOnClickListener = galleryOnClickListener;
    }

    public interface GalleryOnClickListener {
        void onClick(int position);
    }
    public interface  GalleryDelOnClickListener{
        abstract void onBack(int paramInt, ViewPagerGallery.GalleryOnClickListener paramGalleryOnClickListener);

        void onClick(int position);
    }

    private GalleryOnLongClickListener longClickListener;

    public void setGalleryOnLongClickListener(GalleryOnLongClickListener listener) {
        longClickListener = listener;
    }

    public interface GalleryOnLongClickListener {
        void onLongClick(View view);
    }
}