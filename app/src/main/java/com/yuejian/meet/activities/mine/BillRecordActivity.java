package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.BillAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Bill;
import com.yuejian.meet.bean.DateHead;
import com.yuejian.meet.widgets.SwipeRefreshView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 账单明细
 * Created by zh02 on 2017/8/19.
 */

public class BillRecordActivity extends BaseActivity {
    @Bind(R.id.swipe_view)
    SwipeRefreshView swipeRefreshView;
    @Bind(R.id.bill_list)
    ListView billList;

    private BillAdapter adapter = null;
    private int pageIndex = 1;
    private int itemCount = 20;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_record);
        setTitleText(getString(R.string.mine_txt_wallet));
        findViewById(R.id.shuai_xuan).setVisibility(View.VISIBLE);
        adapter = new BillAdapter(this, dataSource);
        billList.setAdapter(adapter);
        swipeRefreshView.setEnabled(false);
        swipeRefreshView.setItemCount(itemCount);
        swipeRefreshView.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_blue_bright, R.color.colorPrimaryDark,
                android.R.color.holo_orange_dark, android.R.color.holo_red_dark, android.R.color.holo_purple);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        getBill();
                    }
                }, 1000);
            }
        });
        swipeRefreshView.setOnLoadMoreListener(new SwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipeRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex++;
                        getBill();
                    }
                }, 1000);
            }
        });
        getBill();

        billList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    @OnClick({R.id.shuai_xuan})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shuai_xuan:
                startActivity(new Intent(this, RecordSelectActivity.class));
                break;
        }
    }

    private List<Bill> bills = new ArrayList<>();
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<Object> dataSource = new ArrayList<>();

    private void getBill() {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("bill_type", String.valueOf(0));
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", String.valueOf(itemCount));
        apiImp.getBill(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Bill> temp = JSON.parseArray(data, Bill.class);
                if (temp != null && temp.size() > 0) {
                    dataSource.clear();
                    if (pageIndex == 1) {
                        bills.addAll(temp);
                        dataSource.addAll(0, handleBillData(bills));
                        swipeRefreshView.setRefreshing(false);
                    } else {
                        bills.addAll(temp);
                        dataSource.addAll(handleBillData(bills));
                        swipeRefreshView.setLoading(false);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    if (pageIndex > 1) {
                        pageIndex--;
                    }
                }
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
}
