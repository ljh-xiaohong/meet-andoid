package com.yuejian.meet.widgets.gallery;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.yuejian.meet.widgets.PinchImageView;

import java.util.List;

public class ViewPagerGalleryAdapter extends PagerAdapter {
    private List<View> mViews;

    public ViewPagerGalleryAdapter(List<View> views) {
        mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mViews.get(position);
        int childCount = container.getChildCount() - 1;
        if (childCount < position) {
            position = childCount;
        }
        container.addView(view, position);
        if(view instanceof PinchImageView){
            PinchImageView imageView = (PinchImageView) view;
//            imageView.reset();
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mViews.get(position);
        if(view instanceof PinchImageView){
            PinchImageView imageView = (PinchImageView) view;
//            imageView.reset();
        }
        container.removeView(view);
    }
}