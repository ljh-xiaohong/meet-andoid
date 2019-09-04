package com.yuejian.meet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

import java.util.Calendar;

import butterknife.Bind;

public class DateActivity extends Activity implements DatePicker.OnDateChangedListener, View.OnClickListener {

    DatePicker picker;

    private View confirm, cancl;

    private int year, month, day;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        confirm = this.findViewById(R.id.activity_date_confirm);
        cancl = this.findViewById(R.id.activity_date_cancl);
        this.picker = (DatePicker) findViewById(R.id.activity_date_picker);
        setListener();
    }

    private void setListener() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) ;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        picker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) , calendar.get(Calendar.DAY_OF_MONTH), this);
        this.cancl.setOnClickListener(this);
        this.confirm.setOnClickListener(this);

    }


    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {

        this.year = year;
        this.month = month;
        this.day = day;

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.activity_date_cancl:
                finish();
                break;

            case R.id.activity_date_confirm:
                Intent intent = new Intent();
                intent.putExtra("YEAR", year + "");
                intent.putExtra("MONTH", month + 1 + "");
                intent.putExtra("DAY", day + "");
                setResult(RESULT_OK, intent);
                finish();
                break;

        }


    }
}
