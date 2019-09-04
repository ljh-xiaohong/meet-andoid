package com.yuejian.meet.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.utils.TextUtil;


public class LikerItemView extends LinearLayout {

    private TextView num, name, time;
    private ImageView head;

    public LikerItemView(Context context) {
        this(context, null);
    }

    public LikerItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikerItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_business_activity_like, this);
        num = (TextView) this.findViewById(R.id.item_business_like_no);
        name = (TextView) this.findViewById(R.id.item_business_like_name);
        time = (TextView) this.findViewById(R.id.item_business_like_time);
        head = (ImageView) this.findViewById(R.id.item_business_like_head);
    }

    public void setItemInfo(String num, String name, String time, String url) {

        this.num.setText(TextUtil.notNull(num));
        this.name.setText(TextUtil.notNull(name));
        this.time.setText(TextUtil.notNull(time));
        Glide.with(getContext()).load(url).into(this.head);

    }

}
