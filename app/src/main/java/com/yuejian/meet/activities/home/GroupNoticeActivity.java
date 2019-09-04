package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.dialogs.LoadingDialogFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 群公告
 */
public class GroupNoticeActivity extends BaseActivity {
    @Bind(R.id.txt_titlebar_release)
    TextView txt_titlebar_release;
    @Bind(R.id.notice_hint)
    TextView notice_hint;
    @Bind(R.id.notice_cotent)
    TextView notice_cotent;
    LoadingDialogFragment dialog;
    ApiImp api=new ApiImp();
    Boolean isOwner=false;
    String t_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice);
        t_id=getIntent().getStringExtra("t_id");
        isOwner=getIntent().getBooleanExtra("isOwner",false);
        notice_cotent.setText(getIntent().getStringExtra("content"));
        initData();
    }
    public void initData(){
        dialog=new LoadingDialogFragment().newInstance("正在提交");
        txt_titlebar_release.setText("确定");
        setTitleText("群公告");
        if (!isOwner){
            notice_cotent.setFocusable(false);
//            notice_cotent.setEnabled(false);
        }else {
            txt_titlebar_release.setVisibility(View.VISIBLE);
            notice_hint.setVisibility(View.GONE);
        }
    }
    public void postData(){
        if (dialog!=null)
            dialog.show(getFragmentManager(),"");
        Map<String,Object> params=new HashMap<>();
        params.put("t_id",t_id);
        params.put("group_notice",notice_cotent.getText().toString());
        api.updateNotice(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog!=null)
                    dialog.dismiss();
                Intent intent=new Intent();
                intent.putExtra("content",notice_cotent.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)
                    dialog.dismiss();
            }
        });
    }


    @OnClick({R.id.txt_titlebar_release})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_titlebar_release:
                    if (isOwner){
                        postData();
                    }
                break;
        }
    }
}
