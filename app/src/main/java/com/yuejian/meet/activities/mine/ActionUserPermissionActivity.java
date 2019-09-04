package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * 允许查看动态的用户
 * Created by zh02 on 2017/8/9.
 */

public class ActionUserPermissionActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_user_permission);
        permissionType = getIntent().getStringExtra("permissionType");
        setTitleText(getString(R.string.Allow_viewing_of_dynamic));

        findViewById(R.id.all_people_check).setVisibility(View.INVISIBLE);
        findViewById(R.id.only_self_check).setVisibility(View.INVISIBLE);
        findViewById(R.id.just_friends_check).setVisibility(View.INVISIBLE);
        if ("0".equals(permissionType)) {
            findViewById(R.id.all_people_check).setVisibility(View.VISIBLE);
            findViewById(R.id.only_self_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.just_friends_check).setVisibility(View.INVISIBLE);
        } else if ("1".equals(permissionType)) {
            findViewById(R.id.all_people_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.only_self_check).setVisibility(View.VISIBLE);
            findViewById(R.id.just_friends_check).setVisibility(View.INVISIBLE);
        } else if ("2".equals(permissionType)) {
            findViewById(R.id.all_people_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.only_self_check).setVisibility(View.INVISIBLE);
            findViewById(R.id.just_friends_check).setVisibility(View.VISIBLE);
        }
    }


    private String permissionType = "";

    @OnClick({R.id.all_people, R.id.only_self, R.id.just_friends})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_people:
                permissionType = "0";
                findViewById(R.id.all_people_check).setVisibility(View.VISIBLE);
                findViewById(R.id.only_self_check).setVisibility(View.INVISIBLE);
                findViewById(R.id.just_friends_check).setVisibility(View.INVISIBLE);
                setSeePerson(permissionType);
                finish();
                break;
            case R.id.only_self:
                permissionType = "1";
                findViewById(R.id.all_people_check).setVisibility(View.INVISIBLE);
                findViewById(R.id.only_self_check).setVisibility(View.VISIBLE);
                findViewById(R.id.just_friends_check).setVisibility(View.INVISIBLE);
                setSeePerson(permissionType);
                finish();
                break;
            case R.id.just_friends:
                permissionType = "2";
                findViewById(R.id.all_people_check).setVisibility(View.INVISIBLE);
                findViewById(R.id.only_self_check).setVisibility(View.INVISIBLE);
                findViewById(R.id.just_friends_check).setVisibility(View.VISIBLE);
                setSeePerson(permissionType);
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("permissionType", permissionType);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    private void setSeePerson(String type) {
        if (user == null) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("see_person", type);
        apiImp.setSeePerson(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
}
