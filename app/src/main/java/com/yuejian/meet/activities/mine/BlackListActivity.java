package com.yuejian.meet.activities.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

/**
 * Created by zh02 on 2017/8/9.
 */

public class BlackListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_list);
        setTitleText("黑名单");
    }

    @Override
    public void onClick(View v) {

    }
}
