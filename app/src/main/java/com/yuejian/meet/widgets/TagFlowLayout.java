package com.yuejian.meet.widgets;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import com.zhy.view.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class TagFlowLayout extends FlowLayout implements View.OnClickListener {

    private OnItemClickListener listener;

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagFlowLayout(Context context) {
        super(context);
    }

    /**
     * 清除点击ID
     *
     * @param exceptID
     */
    public void clearClick(int exceptID) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).getId() == exceptID) continue;
            if (!(getChildAt(i) instanceof CheckBox)) continue;

            CheckBox item = (CheckBox) getChildAt(i);
            if (item.isChecked()) item.setChecked(false);
        }
    }

    /**
     * addview时，把ID，setTag（）
     *
     * @param child
     */
    @Override
    public void addView(View child) {
        super.addView(child);
        child.setOnClickListener(this);
    }

    /**
     * 获取已选中的ID
     *
     * @return
     */
    private List<View> getClickViews() {
        List<View> items = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            if (!(getChildAt(i) instanceof CheckBox)) continue;
            CheckBox item = (CheckBox) getChildAt(i);
            if (item.isChecked()) items.add(item);
        }

        return items;
    }

    @Override
    public void onClick(View view) {
        if (listener == null) return;
        listener.clickItem(view);
    }

    /**
     * 获取标签id
     *
     * @return
     */
    public String getLabId() {
        String laId = "";

        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof CheckBox) {
                CheckBox item = (CheckBox) getChildAt(i);
                if (!item.isChecked()) continue;
                if (item.getTag() == null) continue;
                laId += item.getTag().toString() + ",";
            }
        }

        if (!TextUtils.isEmpty(laId) && laId.contains(","))
            laId = laId.substring(0, laId.length() - 1);


        return laId;
    }

    public interface OnItemClickListener {

        void clickItem(View view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
