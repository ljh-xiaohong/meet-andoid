package com.yuejian.meet.activities.clan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 邀请成功页面
 */
public class CreateClanSucceedActivity extends BaseActivity {
    String clanId;
    String clanName;
    @Bind(R.id.clang_img)
    ImageView clang_img;


    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_create_clan_succeed);
        this.clanId = getIntent().getStringExtra("clanId");
    }

    public void initData() {
        setTitleText(getString(R.string.application_approved));
        this.clang_img.setSelected(true);
    }

    @OnClick({R.id.add_ok, R.id.add_friend})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.add_friend:
//                Intent intent = new Intent(this, ClanInviteMemberActivity.class);
//                intent.putExtra("clanId", this.clanId);
//                intent.putExtra("clanName", this.clanName);
//                startActivity(intent);
                break;
            case R.id.add_ok:
                setResult(RESULT_OK, new Intent());
                finish();
                return;
        }

    }

}
