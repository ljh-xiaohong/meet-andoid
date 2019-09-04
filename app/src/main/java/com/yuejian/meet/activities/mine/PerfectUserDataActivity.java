package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.FamilyTree.AddRelationActivity2;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.TagTypSelAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.InnerGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 完善资料
 */
public class PerfectUserDataActivity extends BaseActivity{
    @Bind(R.id.ed_company_name)
    EditText ed_company_name;
    @Bind(R.id.ed_job)
    EditText ed_job;
    @Bind(R.id.ed_other_job)
    EditText ed_other_job;
    @Bind(R.id.specialty_grid)
    InnerGridView gridView;

    Intent intent;
    List<String> listData=new ArrayList<>();
    TagTypSelAdapter mAdapter;
    boolean noRegister=false;
//    item_tag_type_sel_layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_user_data);
        noRegister=getIntent().getBooleanExtra("noRegister",false);
        initData();
    }
    public void initData(){
        ImUtils.loginIm();//登录im
        setTitleText("完善资料");
        mAdapter=new TagTypSelAdapter(gridView,listData,R.layout.item_tag_type_sel_layout);
        gridView.setAdapter(mAdapter);
        getRequstData();
    }

    @OnClick({R.id.next,R.id.bu_register})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.bu_register:
                postUserData();
                break;
            case R.id.next:
                intentToMain();
                break;
        }
    }
    public void getRequstData(){
        Map<String,Object> params=new HashMap<>();
        apiImp.getTag(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData= JSON.parseArray(data,String.class);
                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void postUserData(){
        if (StringUtils.isEmpty(ed_company_name.getText().toString())){
            ViewInject.toast(this,"请填写公司名称");
            return;
        }
        if (StringUtils.isEmpty(ed_job.getText().toString())){
            ViewInject.toast(this,"请填写您的职位");
            return;
        }
        if (StringUtils.isEmpty(ed_other_job.getText().toString())){
            ViewInject.toast(this,"请填写您的其他职务");
            return;
        }
        if (StringUtils.isEmpty(mAdapter.getSelTag())){
            ViewInject.toast(this,"请选择擅长领域");
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("name",user.getName());
        params.put("job",ed_job.getText().toString());
        params.put("other_job",ed_other_job.getText().toString());
        params.put("specialty",mAdapter.getSelTag());
        params.put("company_name",ed_company_name.getText().toString());
        apiImp.updateCustomerInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                intentToMain();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void intentToMain(){
        if (noRegister){
            finish();
            return;
        }
        String inviteCode = PreferencesUtil.get(getApplicationContext(), AppConfig.INVITE_CODE, "");
        if (StringUtils.isNotEmpty(inviteCode)) {
            intent = new Intent(getBaseContext(), AddRelationActivity2.class);
        }else {
            intent = new Intent(getBaseContext(), MainActivity.class);
        }
        intent.putExtra("finish_register", true);
        startActivity(intent);
        finish();
    }
}
