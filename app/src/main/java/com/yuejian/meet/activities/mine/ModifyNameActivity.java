package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/17.
 */

public class ModifyNameActivity extends BaseActivity {

    @Bind(R.id.name_edit)
    EditText nameEdit;
    @Bind(R.id.surname)
    TextView surName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);
        setTitleText("姓名");
        findViewById(R.id.user_info_edit_save).setVisibility(View.VISIBLE);
        String surname = getIntent().getStringExtra("surname");
        String name = getIntent().getStringExtra("name");
        surName.setText(surname);
        nameEdit.setText(name);

        nameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

    }

    @OnClick({R.id.user_info_edit_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_edit_save:
                if (nameEdit.getText().length() > 10) {
                    Toast.makeText(mContext, "名字不能超过10个字", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("name_edit", nameEdit.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }
}
