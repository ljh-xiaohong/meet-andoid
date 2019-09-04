package com.yuejian.meet.activities.creation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.SuibiTagEntity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/05/22 15:03
 * @desc : 发布 - 编辑随笔 - 选择标签
 */
public class SuibiSelectTagActivity extends BaseActivity {

    @Bind(R.id.iv_select_tag_back_btn)
    ImageView mBackBtn;
    @Bind(R.id.select_tag_flow_layout)
    TagFlowLayout mFlowLayout;
    @Bind(R.id.btn_select_tag_submit_btn)
    Button mSubmitBtn;

    private List<SuibiTagEntity> mTags = new ArrayList<>();
    private String mTag = "";
    private int mTagId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suibi_select_tag);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mTagId = getIntent().getIntExtra("tagId", -1);
        Map<String, Object> param = new HashMap<>();
        apiImp.findLabel(param, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                mTags = JSON.parseArray(data, SuibiTagEntity.class);

                runOnUiThread(() -> {
                    mFlowLayout.setAdapter(new TagAdapter<SuibiTagEntity>(mTags) {
                        @Override
                        public View getView(FlowLayout flowLayout, int i, SuibiTagEntity suibiTagEntity) {
                            TextView tv = (TextView) mInflater.inflate(R.layout.item_tag_textview, mFlowLayout, false);
                            tv.setText(suibiTagEntity.getTitle());
                            return tv;
                        }
                    });
                });

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

        mFlowLayout.setOnTagClickListener((view, i, flowLayout) -> {
            mTag = mTags.get(i).getTitle();
            mTagId = mTags.get(i).getId();
            refreshLayout();
            return true;
        });
    }

    private void refreshLayout() {
        if (!TextUtils.isEmpty(mTag)) {
            mSubmitBtn.setEnabled(true);
        } else {
            mSubmitBtn.setEnabled(false);
        }
    }

    @OnClick({R.id.btn_select_tag_submit_btn, R.id.iv_select_tag_back_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_tag_submit_btn:
                backToEditSuibi(RESULT_OK); //修改并返回
                break;
            case R.id.iv_select_tag_back_btn:
                backToEditSuibi(RESULT_CANCELED); //放弃修改并返回
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            backToEditSuibi(RESULT_CANCELED); //放弃修改并返回
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToEditSuibi(RESULT_CANCELED); //放弃修改并返回
    }

    private void backToEditSuibi(int resultType) {
        Intent backIntent = new Intent();
        backIntent.putExtra("tag", mTag);
        backIntent.putExtra("tagId", mTagId);
        setResult(resultType, backIntent);
        finish();
    }
}
