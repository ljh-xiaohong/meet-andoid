package com.yuejian.meet.activities.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : ljh
 * @time : 2019/9/10 18:51
 * @desc :
 */
public class CustomerServiceDetails extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.content)
    TextView content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_service_details);
        ButterKnife.bind(this);
        back.setOnClickListener(v -> finish());
        content.setText(getIntent().getStringExtra("content"));
    }
}
