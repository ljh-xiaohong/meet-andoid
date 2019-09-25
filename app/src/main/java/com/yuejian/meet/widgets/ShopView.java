package com.yuejian.meet.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuejian.meet.R;

public class ShopView extends LinearLayout {

    public ImageView img, click_img;
    public TextView tv_title, tv_discount, tv_fullPrice;


    public ShopView(Context context) {
        this(context, null);
    }

    public ShopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.widget_shop_view, this);
        init();
    }

    private void init() {
        img = findViewById(R.id.widget_shop_img);
        click_img = findViewById(R.id.widget_shop_click);
        tv_title = findViewById(R.id.widget_shop_title);
        tv_discount = findViewById(R.id.widget_shop_discount);
        tv_fullPrice = findViewById(R.id.widget_shop_price_full);
    }


}
