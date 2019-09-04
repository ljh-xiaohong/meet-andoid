package com.yuejian.meet.widgets;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yuejian.meet.R;

import java.util.ArrayList;
import java.util.Calendar;

import io.github.xhinliang.lunarcalendar.LunarCalendar;

public class CalendarAdapter extends BaseAdapter {
    private Calendar calendar;
    private Context context;
    private int[] dates = null;
    private ArrayList<Integer> signDates = null;

    public CalendarAdapter(Context paramContext, ArrayList<Integer> paramArrayList) {
        this.context = paramContext;
        this.calendar = Calendar.getInstance();
        this.dates = new int[(this.calendar.getMaximum(Calendar.WEEK_OF_MONTH) - 1) * 7];
        int i = 0;
        while (i < this.dates.length) {
            this.dates[i] = -1;
            i += 1;
        }
        this.calendar = Calendar.getInstance();
        i = 1;
        while (i <= this.calendar.getMaximum(Calendar.DATE)) {
            this.calendar.set(Calendar.DATE, i);
            int j = this.calendar.get(Calendar.WEEK_OF_MONTH);
            int k = this.calendar.get(Calendar.DAY_OF_WEEK);
            if (((j - 1) * 7 + k - 1)<dates.length)
                this.dates[((j - 1) * 7 + k - 1)] = i;
            i += 1;
        }
        this.signDates = paramArrayList;
    }

    public int getCount() {
        return this.dates.length;
    }

    public Object getItem(int paramInt) {
        return Integer.valueOf(this.dates[paramInt]);
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        if (paramView == null) {
            paramView = LayoutInflater.from(this.context).inflate(R.layout.sign_lunar_calendar_item, paramViewGroup, false);
        }
        TextView dateView = (TextView) paramView.findViewById(R.id.date);
        int date = dates[paramInt];
        if (date != -1 && !(paramInt<5 && date==31)) {
            if (signDates != null && signDates.contains(date)) {
                dateView.setSelected(true);
            } else {
                dateView.setSelected(false);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, date);
            dateView.setText(String.valueOf(date));
            TextView lunarDateView = (TextView) paramView.findViewById(R.id.lunar_date);
            try{
                LunarCalendar lunarCalender = LunarCalendar.getInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, date);
                lunarDateView.setText(lunarCalender.getLunarDay());
            }catch (Exception e) {
                e.printStackTrace();
                Log.d("calendar", "date = " + date);
            }
        } else {
            paramView.findViewById(R.id.content_date).setVisibility(View.INVISIBLE);
        }
        return paramView;
    }
}
