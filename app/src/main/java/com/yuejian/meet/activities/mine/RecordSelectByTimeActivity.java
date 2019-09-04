package com.yuejian.meet.activities.mine;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/19.
 */

public class RecordSelectByTimeActivity extends BaseActivity {
    @Bind(R.id.start_time_edit)
    EditText startTimeEdit;
    @Bind(R.id.end_time_edit)
    EditText endTimeEdit;
    @Bind(R.id.month_select)
    RadioButton monthSelect;
    @Bind(R.id.day_select)
    RadioButton daySelect;

    private TimePickerView.Type type;
    private SimpleDateFormat dateFormat = null;
    private EditText currentEd;
    private int SELECTED_COLOR = Color.parseColor("#3A9BDC");
    private int UNSELECTED_COLOR = Color.parseColor("#B5B5B5");

    private boolean isFromRecordSelect = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.isMIUI()) {
            Utils.MIUISetStatusBarLightMode(getWindow(), true);
        } else if (Utils.isFlyme()) {
            Utils.FlymeSetStatusBarLightMode(getWindow(), true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }
        setContentView(R.layout.activity_record_select_by_time);
        isFromRecordSelect = getIntent().getBooleanExtra("fromRecordSelect", false);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        startTimeEdit.setHint(R.string.record_text_2);
        endTimeEdit.setHint(R.string.record_text_3);
        startTimeEdit.setInputType(InputType.TYPE_NULL);
        endTimeEdit.setInputType(InputType.TYPE_NULL);
        startTimeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    currentEd = startTimeEdit;
                    startTimeEdit.setTextColor(SELECTED_COLOR);
                    endTimeEdit.setTextColor(UNSELECTED_COLOR);
                    showTimePicker(type);
                    findViewById(R.id.start_view).setBackgroundColor(SELECTED_COLOR);
                    findViewById(R.id.end_view).setBackgroundColor(UNSELECTED_COLOR);
                }
            }
        });


        endTimeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    currentEd = endTimeEdit;
                    endTimeEdit.setTextColor(SELECTED_COLOR);
                    startTimeEdit.setTextColor(UNSELECTED_COLOR);
                    showTimePicker(type);
                    findViewById(R.id.end_view).setBackgroundColor(SELECTED_COLOR);
                    findViewById(R.id.start_view).setBackgroundColor(UNSELECTED_COLOR);
                }
            }
        });

        monthSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = TimePickerView.Type.YEAR_MONTH;
                    findViewById(R.id.zhi).setVisibility(View.GONE);
                    findViewById(R.id.end_layout).setVisibility(View.GONE);
                } else {
                    type = TimePickerView.Type.YEAR_MONTH_DAY;
                    findViewById(R.id.zhi).setVisibility(View.VISIBLE);
                    findViewById(R.id.end_layout).setVisibility(View.VISIBLE);
                }
            }
        });

        monthSelect.setChecked(true);
    }


    @OnClick({R.id.fanhui, R.id.del, R.id.user_info_edit_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                finish();
                break;
            case R.id.del:
                startTimeEdit.setText("");
                startTimeEdit.clearFocus();
                endTimeEdit.setText("");
                endTimeEdit.clearFocus();
                break;
            case R.id.user_info_edit_save:
                if (startTimeEdit.getTag() == null) {
                    finish();
                    return;
                }
                long startTime = (long) startTimeEdit.getTag();
                Intent intent = new Intent();
                intent.putExtra("startTime", startTime);
                if (endTimeEdit.getTag() != null && !monthSelect.isChecked()) {
                    long endTime = (long) endTimeEdit.getTag();
                    if(endTime < startTime){
                        Toast.makeText(mContext, R.string.record_text_1, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    intent.putExtra("endTime", endTime);
                }
                if (isFromRecordSelect) {
                    intent.setComponent(new ComponentName(this, BillRecordClassifyActivity.class));
                    startActivity(intent);
                } else {
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
        }
    }

    private void showTimePicker(final TimePickerView.Type type) {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (type == TimePickerView.Type.YEAR_MONTH) {
                    dateFormat.applyPattern("yyyy/MM");
                } else {
                    dateFormat.applyPattern("yyyy/MM/dd");
                }

                String dateString = dateFormat.format(date);
                currentEd.setText(dateString);
                currentEd.setTag(date.getTime());
            }
        })
                .setType(type)
                .setCancelText(getString(R.string.cancel))
                .setSubmitText(getString(R.string.confirm))
                .setContentSize(20)
                .setTitleSize(20)
                .setOutSideCancelable(true)
                .isCyclic(true)
                .setTextColorCenter(Color.BLACK)
                .setTitleColor(Color.BLACK)
                .setSubmitColor(Color.BLUE)
                .setCancelColor(Color.BLUE)
                .isCenterLabel(false)
                .isDialog(false)
                .build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }
}
