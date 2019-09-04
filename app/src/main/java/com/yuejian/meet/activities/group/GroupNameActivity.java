package com.yuejian.meet.activities.group;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.team.activity.AdvancedTeamMemberInfoActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.team.TeamService;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class GroupNameActivity extends BaseActivity {
    @Bind(R.id.txt_titlebar_release)
    TextView release;
    @Bind(R.id.et_group_name)
    TextView groupName;
    String tId="",name;
    private LoadingDialogFragment dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name);
        initView();
    }
    private void initView(){
        tId=getIntent().getStringExtra("t_id");
        name=getIntent().getStringExtra("name");
        dialog=LoadingDialogFragment.newInstance("正在请求..");
        setTitleText("群名称");
        release.setText("确定");
        release.setVisibility(View.VISIBLE);
        groupName.setText(name);
    }

    @OnClick({R.id.txt_titlebar_release})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_titlebar_release://确定
                updateGroupName();
                break;
        }
    }

    public void updateGroupName(){
        if (StringUtil.isEmpty(groupName.getText().toString())){
            ViewInject.shortToast(mContext,"群名不能为空");
            return;
        }
        if (dialog!=null)
            dialog.show(getFragmentManager(),"");
        NIMClient.getService(TeamService.class).updateName(tId, groupName.getText().toString()).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                Map<String,Object> params=new HashMap<>();
                params.put("customer_id", AppConfig.CustomerId);
                params.put("t_id", tId);
                params.put("content", groupName.getText().toString());
                new ApiImp().getUpdateGroupName(params, this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        Intent intent=new Intent();
                        intent.putExtra("name",groupName.getText().toString());
                        setResult(RESULT_OK,intent);
                        ViewInject.shortToast(getApplicationContext(),"修改成功");
                        finish();
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                        ViewInject.shortToast(getApplicationContext(),"修改失败");
                        if (dialog!=null)
                            dialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailed(int code) {
                ViewInject.shortToast(getApplicationContext(),"修改失败");
                if (dialog!=null)
                    dialog.dismiss();
            }

            @Override
            public void onException(Throwable exception) {
                ViewInject.shortToast(getApplicationContext(),"修改失败");
                if (dialog!=null)
                    dialog.dismiss();
            }
        });

    }
}
