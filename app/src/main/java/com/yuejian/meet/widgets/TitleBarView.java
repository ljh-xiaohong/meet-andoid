package com.yuejian.meet.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuejian.meet.R;

public class TitleBarView extends LinearLayout {

    private TextView tv_TitleName;
    private ImageView img_BackIcomn;


    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.widget_titlebar, this);
        tv_TitleName = (TextView) this.findViewById(R.id.widget_title_tv);
        img_BackIcomn = (ImageView) this.findViewById(R.id.widget_title_img);

        setAttributes(getContext(), attrs, defStyleAttr);
        setListener();
    }

    private void setAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TitleBarView, defStyleAttr, defStyleAttr);
        for (int i = 0; i < a.getIndexCount(); i++) {
            switch (a.getIndex(i)) {
                case R.styleable.TitleBarView_information:
                    tv_TitleName.setText(a.getString(a.getIndex(i)));
                    break;
            }
        }
        a.recycle();

    }

    private void setListener() {
        img_BackIcomn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });
    }

}
