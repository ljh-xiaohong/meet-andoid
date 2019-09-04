package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.BillAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Bill;
import com.yuejian.meet.bean.DateHead;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.PinnedSectionListView;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/18.
 */

public class RecordSelectActivity extends BaseActivity implements SpringView.OnFreshListener {
    @Bind(R.id.btn_yyetc)
    Button yyetcBtn;
    @Bind(R.id.bill_btns_grid_view)
    GridLayout gridLayout;
    @Bind(R.id.search_edit)
    EditText searchEdit;
    @Bind(R.id.search_list)
    PinnedSectionListView searchList;
    @Bind(R.id.spring_view)
    SpringView springView;

    private List<Bill> bills = new ArrayList<>();
    private ArrayList<Object> searchDataSource = new ArrayList<>();
    private int pageIndex = 1;
    private BillAdapter billAdapter = new BillAdapter(this, searchDataSource);

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
        setContentView(R.layout.activity_record_select);

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_NONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) && event != null) {
                    findViewById(R.id.search_icon).performClick();
                    return true;
                }
                return false;
            }
        });

        searchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    findViewById(R.id.select_content_layout).setVisibility(View.GONE);
                    findViewById(R.id.cancel_search).setVisibility(View.VISIBLE);
                    springView.setVisibility(View.VISIBLE);
                } else {
                    if (StringUtils.isEmpty(searchEdit.getText().toString())) {
                        findViewById(R.id.select_content_layout).setVisibility(View.VISIBLE);
                        findViewById(R.id.cancel_search).setVisibility(View.GONE);
                    }
                }
            }
        });
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = searchDataSource.get(position);
                if (obj instanceof Bill) {
                    Bill bill = (Bill) obj;
                    Intent intent = new Intent(getBaseContext(), BillDetailActivity.class);
                    String billJson = JSON.toJSONString(bill);
                    intent.putExtra("bill", billJson);
                    startActivity(intent);
                }
            }
        });
        searchList.setAdapter(billAdapter);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(this);
        getBill("");
    }

    @OnClick({R.id.fanhui, R.id.select_time, R.id.search_icon, R.id.cancel_search})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                finish();
                break;
            case R.id.select_time:
                Intent intent = new Intent(this, RecordSelectByTimeActivity.class);
                intent.putExtra("fromRecordSelect", true);
                startActivity(intent);
                break;
            case R.id.search_icon:
                getBill(searchEdit.getText().toString());
                searchEdit.clearFocus();
                Utils.hideSystemKeyBoard(this, searchEdit);
                break;
            case R.id.cancel_search:
                findViewById(R.id.select_content_layout).setVisibility(View.VISIBLE);
                v.setVisibility(View.GONE);
                searchEdit.clearFocus();
                Utils.hideSystemKeyBoard(this, searchEdit);
                springView.setVisibility(View.VISIBLE);
                break;
        }
    }


    //0:全部 1:充值记录, 2:提现记录, 3:收到礼物, 4:送出礼物, 5:视频收入, 6:视频消费, 7:营业额提成, 8:推荐收入, 9:购物消费, 10:店铺收入, 11:礼物退款
    private int btnTag = 0;
    private String btnTagString = "";

    public void onTagClick(View view) {
        try {
            if (view instanceof Button) {
                btnTag = Integer.valueOf((String) view.getTag());
                btnTagString = ((Button) view).getText().toString();
                Intent intent = new Intent(getBaseContext(), BillRecordClassifyActivity.class);
                intent.putExtra("billType", btnTag);
                intent.putExtra("tagString", btnTagString);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBill(String keyWord) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("bill_type", String.valueOf(0));
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", String.valueOf(200));
        params.put("query_word", keyWord);
        apiImp.getBill(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Bill> temp = JSON.parseArray(data, Bill.class);
                if (temp != null && temp.size() > 0) {
                    if (pageIndex == 1) {
                        if (bills.isEmpty() || !temp.get(0).id.equals(bills.get(0).id)) {
                            bills.addAll(0, temp);
                            searchDataSource.addAll(0, handleBillData(bills));
                        }
                    } else {
                        bills.addAll(temp);
                        searchDataSource.addAll(handleBillData(bills));
                    }
                } else {
                    if (pageIndex > 1) {
                        pageIndex--;
                    }
                }
                billAdapter.notifyDataSetChanged();
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
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
        if (searchDataSource.size() > 0) {
            Object o = searchDataSource.get(searchDataSource.size() - 1);
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
        Calendar calendar = Calendar.getInstance();
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

    @Override
    public void onRefresh() {
        if (springView.getVisibility() == View.VISIBLE) {
            pageIndex = 1;
            getBill(searchEdit.getText().toString());
        }
    }

    @Override
    public void onLoadmore() {
        if (springView.getVisibility() == View.VISIBLE) {
            pageIndex++;
            getBill(searchEdit.getText().toString());
        }
    }
}
