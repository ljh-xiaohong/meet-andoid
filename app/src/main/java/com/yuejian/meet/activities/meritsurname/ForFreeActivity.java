package com.yuejian.meet.activities.meritsurname;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

import butterknife.Bind;

/**
 * @author :
 * @time   : 2018/11/21 11:26
 * @desc   : 兑换放生
 * @version: V1.0
 * @update : 2018/11/21 11:26
 */

public class ForFreeActivity extends BaseActivity {
  @Bind(R.id.ib_back)
  ImageButton mIbBack;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_for_free);
    mIbBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

  }
}
