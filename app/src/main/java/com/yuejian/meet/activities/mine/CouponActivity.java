package com.yuejian.meet.activities.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/18/018.
 */

public class CouponActivity extends BaseActivity implements SpringView.OnFreshListener {

    private SpringView swipeRefreshView = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        setTitleText("购物券");
        swipeRefreshView = (SpringView) findViewById(R.id.swipe_view);
        swipeRefreshView.setHeader(new DefaultHeader(this));
        swipeRefreshView.setFooter(new DefaultFooter(this));
    }


    @OnClick({R.id.look_out_of_date_coupon})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.look_out_of_date_coupon:
                break;
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadmore() {

    }
}
