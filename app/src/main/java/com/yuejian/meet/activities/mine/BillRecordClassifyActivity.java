package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.BillAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Bill;
import com.yuejian.meet.bean.DateHead;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.PinnedSectionListView;
import com.yuejian.meet.widgets.SwipeRefreshView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/21.
 */

public class BillRecordClassifyActivity extends BaseActivity {
    @Bind(R.id.pinned_list_view)
    PinnedSectionListView listView;
    @Bind(R.id.first_tag_btn)
    TextView firstTagBtn;
    @Bind(R.id.swipe_refresh_view)
    SwipeRefreshView swipeRefreshView;
    @Bind(R.id.second_tag_btn)
    TextView secondTagBtn;

    private BillAdapter adapter = null;
    private ArrayList<Bill> bills = null;
    private int billType = 0;
    private int pageIndex = 1;
    private int itemCount = 20;
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<Object> dataSource = new ArrayList<>();
    private final static int SELECT_TIME = 1000;

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
        setContentView(R.layout.activity_bill_reacord_classify);

        ImageView selectIcon = (ImageView) findViewById(R.id.xzsj);
        bills = new ArrayList<>();
        billType = getIntent().getIntExtra("billType", 0);
        startTime = getIntent().getLongExtra("startTime", 0);
        endTime = getIntent().getLongExtra("endTime", 0);

        String classify = getIntent().getStringExtra("tagString");
        if (startTime > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            String start = dateFormat.format(new Date(startTime));
            if (endTime == 0) {
                endTime = startTime + 86400000L * 31;
            }
            String end = dateFormat.format(new Date(endTime));
            String time = start + "--" + end;
            firstTagBtn.setText(time);
            firstTagBtn.setTag("time");
            selectIcon.setImageResource(R.mipmap.icon_fenlei);
            selectIcon.setTag("classify");
        } else if (StringUtils.isNotEmpty(classify)) {
            firstTagBtn.setText(classify);
            firstTagBtn.setTag("classify");
            selectIcon.setTag("time");
        }
        swipeRefreshView.setEnabled(false);
        swipeRefreshView.setItemCount(itemCount);
        swipeRefreshView.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_blue_bright, R.color.colorPrimaryDark,
                android.R.color.holo_orange_dark, android.R.color.holo_red_dark, android.R.color.holo_purple);

        swipeRefreshView.setOnLoadMoreListener(new SwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex++;
                        getBill(billType);
                    }
                }, 1000);
            }
        });
        adapter = new BillAdapter(this, dataSource);
        listView.setAdapter(adapter);
        getBill(billType);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = dataSource.get(position);
                if (obj instanceof Bill) {
                    Bill bill = (Bill) obj;
                    Intent intent = new Intent(getBaseContext(), BillDetailActivity.class);
                    String billJson = JSON.toJSONString(bill);
                    intent.putExtra("bill", billJson);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.fanhui, R.id.xzsj, R.id.first_tag_btn, R.id.second_tag_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                finish();
                break;
            case R.id.xzsj:
                if (v.getTag() != null) {
                    String tag = (String) v.getTag();
                    if ("classify".equals(tag)) {
                        showClassifyWindow();
                    } else {
                        startActivityForResult(new Intent(this, RecordSelectByTimeActivity.class), SELECT_TIME);
                    }
                }
                break;
            case R.id.first_tag_btn:
                if (v.getTag() != null) {
                    String tag = (String) v.getTag();
                    if ("classify".equals(tag)) {
                        showClassifyWindow();
                    } else {
                        startActivityForResult(new Intent(this, RecordSelectByTimeActivity.class), SELECT_TIME);
                    }
                }
                break;
            case R.id.second_tag_btn:
                String tag = (String) v.getTag();
                if ("time".equals(tag)) {
                    startActivityForResult(new Intent(this, RecordSelectByTimeActivity.class), SELECT_TIME);
                } else {
                    showClassifyWindow();
                }
                break;
        }
    }

    private int lastBillType = -1;

    private void getBill(final int billType) {
        if (lastBillType == -1) {
            lastBillType = billType;
        }
        final HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("bill_type", String.valueOf(billType));
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", String.valueOf(itemCount));
        if (startTime > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date(startTime);
            String start = dateFormat.format(date);
            params.put("start_date", start);
            if (endTime == 0) {
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(startTime);
                endTime = startTime + 86400000L * getMonthDay(instance.get(Calendar.MONTH) + 1);
            }
            String end = dateFormat.format(new Date(endTime));
            params.put("end_date", end);
        }
        apiImp.getBill(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Bill> temp = JSON.parseArray(data, Bill.class);
                if (temp != null && temp.size() > 0) {
                    if (lastBillType != billType) {
                        bills.clear();
                    }
                    lastBillType = billType;
                    dataSource.clear();
                    if (pageIndex == 1) {
                        bills.addAll(temp);
                        dataSource.addAll(0, handleBillData(bills));
                        swipeRefreshView.setRefreshing(false);
                    } else {
                        bills.addAll(temp);
                        dataSource.addAll(handleBillData(bills));
                    }
                } else {
                    if (pageIndex > 1) {
                        pageIndex--;
                    }
                    if (lastBillType != billType) {
                        bills.clear();
                        dataSource.clear();
                    }
                }
                swipeRefreshView.setLoading(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    private List handleBillData(List<Bill> bills) {
        List list = new ArrayList<>();
        int year = 0;
        int month = 0;
        if (dataSource.size() > 0) {
            Object o = dataSource.get(dataSource.size() - 1);
            if (o instanceof DateHead) {
                year = ((DateHead) o).year;
                month = ((DateHead) o).month;
            } else if (o instanceof Bill) {
                Bill bill = (Bill) o;
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(bill.create_time);
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
            }
        }
        for (Bill bill : bills) {
            calendar.setTimeInMillis(bill.create_time);
            int y = calendar.get(Calendar.YEAR);
            int m = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
            if (m != month || y != year) {
                //add
                DateHead head = new DateHead();
                head.year = y;
                head.month = m;
                head.day = day;
                list.add(head);
            }
            list.add(bill);
            year = y;
            month = m;
        }
        return list;
    }

    private PopupWindow classifyWindow = null;

    private void showClassifyWindow() {
        if (classifyWindow == null) {
            View contentView = View.inflate(this, R.layout.layout_grid_record_select, null);
            classifyWindow = new PopupWindow(contentView);
            contentView.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    classifyWindow.dismiss();
                    backgroundAlpha(1f);
                }
            });
        }
        classifyWindow.setWidth(DensityUtils.getScreenW(this));
        classifyWindow.setHeight(DensityUtils.dip2px(this, 270));
        classifyWindow.setAnimationStyle(R.style.PopupAnimation);
        classifyWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f6f6f6")));
        classifyWindow.setOutsideTouchable(false);
        classifyWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
    }

    public void onTagClick(View view) {
        try {
            if (view instanceof Button) {
                billType = Integer.valueOf((String) view.getTag());
                String tag = (String) firstTagBtn.getTag();
                if (tag != null) {
                    if ("classify".equals(tag)) {
                        firstTagBtn.setText(((Button) view).getText().toString());
                        firstTagBtn.setTag("classify");
                    } else {
                        secondTagBtn.setVisibility(View.VISIBLE);
                        secondTagBtn.setText(((Button) view).getText().toString());
                        secondTagBtn.setTag("classify");
                    }
                }

                getBill(billType);
                classifyWindow.dismiss();
                backgroundAlpha(1f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private long startTime = 0;
    private long endTime = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == SELECT_TIME) {
                startTime = data.getLongExtra("startTime", 0);
                endTime = data.getLongExtra("endTime", 0);
                if (startTime > 0) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                    String start = dateFormat.format(new Date(startTime));
                    if (endTime == 0) {
                        Calendar instance = Calendar.getInstance();
                        instance.setTimeInMillis(startTime);
                        endTime = startTime + 86400000L * getMonthDay(instance.get(Calendar.MONTH) + 1) - 1;
                    }
                    String end = dateFormat.format(new Date(endTime));
                    String tag = (String) firstTagBtn.getTag();
                    if ("time".equals(tag)) {
                        firstTagBtn.setText(start + "--" + end);
                    } else {
                        secondTagBtn.setVisibility(View.VISIBLE);
                        secondTagBtn.setText(start + "--" + end);
                        secondTagBtn.setTag("time");
                    }
                }
                pageIndex = 1;
                getBill(billType);
            }
        }
    }

    private int getMonthDay(int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 11:
                return 31;
            case 2:
                return 28;
        }
        return 30;
    }

}
