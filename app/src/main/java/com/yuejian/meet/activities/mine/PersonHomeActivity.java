package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.AddRelationActivity;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.HomeActionAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ActionEntity;
import com.yuejian.meet.bean.NewHomeEntity;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.CircleImageView;
import com.yuejian.meet.widgets.InnerListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author :
 * @time : 2018/11/19 18:14
 * @desc :发现 我的 个人主页 个人资料
 * @version: V1.0
 * @update : 2018/11/19 18:14
 */

public class PersonHomeActivity extends BaseActivity {

    @Bind(R.id.user_photo)
    CircleImageView user_photo;
    @Bind(R.id.mine_sxe)
    ImageView mine_sxe;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.user_id)
    TextView user_id;
    @Bind(R.id.user_corp)
    TextView user_corp;
    @Bind(R.id.user_job)
    TextView user_job;
    @Bind(R.id.add_find)
    TextView add_find;
    @Bind(R.id.navbar_more)
    ImageView navbar_more;
    @Bind(R.id.txt_phone)
    TextView txt_phone;
    @Bind(R.id.txt_email)
    TextView txt_email;
    @Bind(R.id.txt_location)
    TextView txt_location;
    @Bind(R.id.phone_layout)
    LinearLayout phone_layout;
    @Bind(R.id.email_layout)
    LinearLayout email_layout;
    @Bind(R.id.location_layout)
    LinearLayout location_layout;
    @Bind(R.id.list_view)
    InnerListView listView;
    @Bind(R.id.message_home_layout)
    LinearLayout message_home_layout;

    NewHomeEntity entity;
    UserEntity userEntity;
    String customerId = "";
    HomeActionAdapter mAdapter;
    List<ActionEntity> listData = new ArrayList<>();
    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton mTitlebarImgBtnBack;
    @Bind(R.id.send_messag)
    LinearLayout mSendMessag;

    //    item_home_action_layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persion_hone);
        customerId = getIntent().getStringExtra("customer_id");
        initData();
    }

    public void initData() {
        navbar_more.setVisibility(customerId.equals(user.getCustomer_id()) ? View.VISIBLE : View.GONE);
        message_home_layout.setVisibility(customerId.equals(user.getCustomer_id()) ? View.GONE : View.VISIBLE);
        mAdapter = new HomeActionAdapter(listView, listData, R.layout.item_home_action_layout, this);
        listView.setAdapter(mAdapter);
        requstData();
    }

    @OnClick({R.id.navbar_more, R.id.send_messag, R.id.add_find, R.id.titlebar_imgBtn_back})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.navbar_more:
                startActivityIfNeeded(new Intent(this, EditHomeActivity.class), 110);
                break;
            case R.id.send_messag:
                ImUtils.toP2PCaht(this, customerId);
                break;
            case R.id.add_find:
                Intent intent = new Intent(this, AddRelationActivity.class);
                intent.putExtra("userName", userEntity.getSurname() + userEntity.getName());
                intent.putExtra("op_customer_id", userEntity.getCustomer_id());
                startActivity(intent);
                break;
            case R.id.titlebar_imgBtn_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            requstData();


        }
    }

    public void requstData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("my_customer_id", user.getCustomer_id());
        params.put("isAction", "true");
        apiImp.getUserInfoV3(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity = JSON.parseObject(data, NewHomeEntity.class);
                userEntity = JSON.parseObject(entity.getCustomer(), UserEntity.class);
                listData = JSON.parseArray(entity.getActionList(), ActionEntity.class);
                mAdapter.refresh(listData);
                setLayout();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    public void setLayout() {
        Glide.with(this).load(userEntity.getPhoto()).into(user_photo);
        mine_sxe.setSelected(userEntity.getSex().equals("1"));
        user_name.setText(userEntity.getSurname() + userEntity.getName());
        user_corp.setText(userEntity.getCompany_name());
        user_job.setText(userEntity.getJob());
        user_id.setText("约见号:" + userEntity.getCustomer_id() + "");
        phone_layout.setVisibility(StringUtils.isEmpty(userEntity.getMobile()) ? View.GONE : View.VISIBLE);
        if (!StringUtils.isEmpty(userEntity.getMobile())) {
            if (customerId.equals(user.getCustomer_id())) {
                txt_phone.setText(userEntity.getMobile());
            } else {
                String phone = userEntity.getMobile().substring(0, 2) + " **** *** " + userEntity.getMobile().substring(userEntity.getMobile().length() - 2);
                txt_phone.setText(phone);
            }
        }
        email_layout.setVisibility(StringUtils.isEmpty(userEntity.getEmail()) ? View.GONE : View.VISIBLE);
        txt_email.setText(userEntity.getEmail());
        location_layout.setVisibility(StringUtils.isEmpty(userEntity.getPosition()) ? View.GONE : View.VISIBLE);
        txt_location.setText(userEntity.getPosition());
        add_find.setVisibility(customerId.equals(user.getCustomer_id()) ? View.GONE : userEntity.getFriendStatus().equals("1") ? View.GONE : View.VISIBLE);
    }
}
