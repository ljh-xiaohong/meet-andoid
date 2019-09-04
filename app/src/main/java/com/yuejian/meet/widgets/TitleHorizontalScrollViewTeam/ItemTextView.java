package com.yuejian.meet.widgets.TitleHorizontalScrollViewTeam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.yuejian.meet.R;

@SuppressLint("AppCompatCustomView")
public class ItemTextView extends TextView implements TitleHorizontalScrollView.Observer<View>, View.OnClickListener {

    private TitleHorizontalScrollView.Observable observable;

    private StateListDrawable drawableStates;
    private ColorStateList colorStateList;
    private boolean isCheck = false;

    public ItemTextView(Context context) {
        this(context, null);
    }

    public ItemTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        unClickState();
        this.setOnClickListener(this);
    }

    private void init() {

    }

    public void setObservable(TitleHorizontalScrollView.Observable observable) {
        this.observable = observable;
    }

    public void setClick(boolean isClick) {
        this.isCheck = isClick;
        if (this.isCheck) {
            ClickState();
        } else {
            unClickState();
        }

    }

    @Override
    public void onClick(View view) {
        if (isCheck) return;
        ClickState();
        if (null != observable) this.observable.notifyObservers(view);
    }

    private void setDrawbleState() {
        drawableStates = new StateListDrawable();
        drawableStates.addState(new int[]{-android.R.attr.state_checked}, this.getResources().getDrawable(android.R.drawable.ic_media_play));
        drawableStates.addState(new int[]{android.R.attr.state_checked}, this.getResources().getDrawable(android.R.drawable.ic_delete));
    }

    private void setColorState() {
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_checked}  // checked
        };//把两种状态一次性添加


        int[] colors = new int[]{
                Color.parseColor("#333333"),
                Color.parseColor("#AB3A34")
        };//把两种颜色一次性添加

        colorStateList = new ColorStateList(states, colors);
    }


    private void unClickState() {
        isCheck = false;
        this.setBackground(this.getResources().getDrawable(R.drawable.shape_mine_business_item_unclick));
        this.setTextColor(Color.parseColor("#333333"));
    }

    private void ClickState() {
        isCheck = true;
        this.setBackground(this.getResources().getDrawable(R.drawable.shape_mine_business_item_click));
        this.setTextColor(Color.parseColor("#AB3A34"));
    }

    @Override
    public void onUpdate(TitleHorizontalScrollView.Observable<View> observable, View view) {
        if (view == this) return;
        unClickState();
    }
}
