package com.yuejian.meet.activities.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.CityBean;
import com.yuejian.meet.bean.MessageDetailBean;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

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
        initData();
    }

    private void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("messageId", getIntent().getIntExtra("messageId",0));
        apiImp.getMessageDetail(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
               MessageDetailBean bean=new Gson().fromJson(data,MessageDetailBean.class);
               content.setText(bean.getData().getMsgRemark());
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(CustomerServiceDetails.this, errMsg);
            }
        });
    }
}
