package com.yuejian.meet.activities.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;

import java.util.HashMap;

import butterknife.OnClick;

/**允许查看动态的范围
 * Created by zh02 on 2017/9/1.
 */

public class PrivacyRangeActivity extends BaseActivity {

    private String range = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_range);
        this.range = getIntent().getStringExtra("range");
        setTitleText(getString(R.string.Allow_viewing_of_dynamic2));
        initRangeCheck();
    }

    private void initRangeCheck() {
        findViewById(R.id.all_days_check).setVisibility(View.INVISIBLE);
        findViewById(R.id.seven_days_check).setVisibility(View.INVISIBLE);
        findViewById(R.id.thirty_days_check).setVisibility(View.INVISIBLE);
        findViewById(R.id.half_year_check).setVisibility(View.INVISIBLE);
        if ("0".equals(range)) {
            findViewById(R.id.all_days_check).setVisibility(View.VISIBLE);
            findViewById(R.id.seven_days_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.thirty_days_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.half_year_check).setVisibility(View.INVISIBLE);
        } else if ("1".equals(range)) {
            findViewById(R.id.all_days_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.seven_days_check).setVisibility(View.VISIBLE);
            findViewById(R.id.thirty_days_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.half_year_check).setVisibility(View.INVISIBLE);
        } else if ("2".equals(range)) {
            findViewById(R.id.all_days_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.seven_days_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.thirty_days_check).setVisibility(View.VISIBLE);
            findViewById(R.id.half_year_check).setVisibility(View.INVISIBLE);
        } else if ("3".equals(range)) {
            findViewById(R.id.all_days_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.seven_days_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.thirty_days_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.half_year_check).setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.all_days_layout, R.id.seven_days_layout, R.id.thirty_days_layout, R.id.half_year_layout})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_days_layout:
                range = "0";
                break;
            case R.id.seven_days_layout:
                range = "1";
                break;
            case R.id.thirty_days_layout:
                range = "2";
                break;
            case R.id.half_year_layout:
                range = "3";
                break;
        }
        setPrivacyRange();
    }

    private void setPrivacyRange() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("see_range", range);
        apiImp.setPrivacyRange(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                initRangeCheck();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
