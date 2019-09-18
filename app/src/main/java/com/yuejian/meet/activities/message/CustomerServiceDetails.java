package com.yuejian.meet.activities.message;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;


import com.yuejian.meet.R;

import butterknife.ButterKnife;

/**
 * @author : ljh
 * @time : 2019/9/10 18:51
 * @desc :
 */
public class CustomerServiceDetails extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_service_details);
        ButterKnife.bind(this);

    }
}
