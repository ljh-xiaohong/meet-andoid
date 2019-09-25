package com.yuejian.meet.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.LinearLayout;


public class CheckLinearLayout extends LinearLayout {

    private OnClickListener listener;

    public CheckLinearLayout(Context context) {
        this(context, null);
    }

    public CheckLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        this.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < getChildCount(); i++) {
                    int position = i;
                    CheckBox view = (CheckBox) getChildAt(i);
                    if (view == null) return;
                    view.setOnClickListener((view1) -> {
                        if (listener == null) return;
                        listener.onCheckedChanged(CheckLinearLayout.this, view);
                    });

                }
            }
        });
    }


    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onCheckedChanged(CheckLinearLayout layout, CheckBox checkedId);
    }


    public void clear() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof CheckBox) {
                CheckBox item = (CheckBox) getChildAt(i);
                item.setChecked(false);
            }
        }
    }

    public String getLabId() {
        String laId = "";

        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof CheckBox) {
                CheckBox item = (CheckBox) getChildAt(i);
                if (!item.isChecked()) continue;
                laId += item.getTag().toString() + ",";
            }
        }

        if (!TextUtils.isEmpty(laId) && laId.contains(","))
            laId = laId.substring(0, laId.length() - 1);


        return laId;

    }


}
