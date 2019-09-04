package com.yuejian.meet.activities.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

/**
 * Created by zh02 on 2017/9/1.
 */

public class GenealogyActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan);
        setTitleText("族谱");
    }

    @Override
    public void onClick(View v) {

    }
}
