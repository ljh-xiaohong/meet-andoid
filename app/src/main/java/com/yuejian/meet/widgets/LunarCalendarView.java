package com.yuejian.meet.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.yuejian.meet.R;

import java.util.ArrayList;

public class LunarCalendarView extends LinearLayout {
    private InnerGridView gridView = null;
    private Context mContext;
    private ArrayList<Integer> signDates = null;

    public LunarCalendarView(Context paramContext) {
        this(paramContext, null);
    }

    public LunarCalendarView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.mContext = paramContext;
        initView();
    }

    private void addGridView() {

        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, 0);
        localLayoutParams.weight = 1.0F;
        this.gridView = new InnerGridView(this.mContext);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) gridView.getLayoutParams();
//        params.width= RelativeLayout.LayoutParams.MATCH_PARENT;
        this.gridView.setNumColumns(7);
        this.gridView.setColumnWidth(40);
        this.gridView.setGravity(17);
        this.gridView.setSelector(new ColorDrawable(0));
        this.gridView.setVerticalSpacing(0);
        this.gridView.setHorizontalSpacing(0);
        addView(this.gridView, localLayoutParams);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        addView(View.inflate(this.mContext, R.layout.calen_calendar, null), new LinearLayout.LayoutParams(-1, -2));
        CalendarAdapter localCalendarAdapter = new CalendarAdapter(this.mContext, this.signDates);
        addGridView();
        this.gridView.setAdapter(localCalendarAdapter);
        setBackgroundColor(Color.parseColor("#ffffff"));
        setLayerType(1, null);
    }

    public void setSignDays(ArrayList<Integer> paramArrayList) {
        this.signDates = paramArrayList;
        this.gridView.setAdapter(new CalendarAdapter(this.mContext, this.signDates));
    }
}
