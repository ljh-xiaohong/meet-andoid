package com.yuejian.meet.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;

import java.util.List;

public class PictureLayout extends FrameLayout {

    private ImageView first, second, third, fourth;
    private TextView info;
    private ImageView[] imageViews;

    public PictureLayout(Context context) {
        this(context, null);
    }

    public PictureLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PictureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_pics, this);
        imageViews = new ImageView[4];

        first = this.findViewById(R.id.item_pics_first);
        imageViews[0] = first;
        second = this.findViewById(R.id.item_pics_second);
        imageViews[1] = second;
        third = this.findViewById(R.id.item_pics_third);
        imageViews[2] = third;
        fourth = this.findViewById(R.id.item_pics_fourth);
        imageViews[3] = fourth;

        info = this.findViewById(R.id.item_pics_info);
    }

    public void initInformations(List<String> urls, String information, Context context) {

        if (urls != null && urls.size() >= imageViews.length) {
            for (int i = 0; i < imageViews.length; i++) {
                Glide.with(context).load(urls.get(i)).into(imageViews[i]);
            }
        }
        info.setText(information);

    }


}
