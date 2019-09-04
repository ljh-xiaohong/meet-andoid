package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.utils.Utils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/16.
 */

public class EditActivity extends BaseActivity {

    @Bind(R.id.edit)
    EditText editText;
    @Bind(R.id.type_count)
    TextView countTv;

    private int countTotal = 30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setTitleText("个性签名");
        findViewById(R.id.user_info_edit_save).setVisibility(View.VISIBLE);
        countTotal = getIntent().getIntExtra("type_count", 30);
        String signature = getIntent().getStringExtra("signature");
        if (signature != null) {
            editText.setText(signature);
            countTv.setText("" + (30 - signature.length()));
        }
        editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setSingleLine(false);
        editText.setHorizontallyScrolling(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                countTv.setText("" + (30 - length));
            }
        });
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});

    }

    @OnClick({R.id.user_info_edit_save})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_info_edit_save:
                Intent intent = new Intent();
                intent.putExtra("edit_content", editText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
