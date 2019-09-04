package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.FCLoger;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.MyRadioGroup;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

/**
 * 发起会议
 */
public class CreateEnjoyActivity extends BaseActivity {
    @Bind(R.id.enjoy_title)
    EditText enjoy_title;
    @Bind(R.id.tet_time)
    TextView tet_time;
    @Bind(R.id.enjoy_address)
    EditText enjoy_address;
    @Bind(R.id.rg1)
    MyRadioGroup rg1;
    @Bind(R.id.rg_son)
    RadioGroup rg_son;
    @Bind(R.id.rg_sel_son)
    RadioGroup rg_sel_son;
    @Bind(R.id.check_layout)
    LinearLayout check_layout;
    @Bind(R.id.enjoy_cost1)
    EditText enjoy_cost1;
    @Bind(R.id.enjoy_cost2)
    EditText enjoy_cost2;
    @Bind(R.id.enjoy_cost3)
    EditText enjoy_cost3;
    @Bind(R.id.user_name)
    EditText user_name;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.rg_sel)
    RadioGroup rg_sel;
    @Bind(R.id.cb_1)
    CheckBox cb_1;
    @Bind(R.id.cb_2)
    CheckBox cb_2;
    @Bind(R.id.cb_3)
    CheckBox cb_3;
    LoadingDialogFragment dialog;
    String Actor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_enjoy);
        setTitleText(getString(R.string.enjoy_create_text_1));
        Actor=getString(R.string.enjoy_create_text_6);
        initView();
        initData();
    }
    public void initData(){
        dialog=LoadingDialogFragment.newInstance(getString(R.string.is_submitted));
    }
    public void selRadio(String typeName){
        rg_son.setVisibility(View.GONE);
        check_layout.setVisibility(View.GONE);
        rg_sel_son.setVisibility(View.GONE);
        if (typeName.equals(getString(R.string.enjoy_create_text_6))){
            rg_son.setVisibility(View.VISIBLE);
        }
        if (typeName.equals(getString(R.string.enjoy_create_text_7))){
            check_layout.setVisibility(View.VISIBLE);
        }
        if (typeName.equals(getString(R.string.enjoy_create_text_8))){
            rg_sel_son.setVisibility(View.VISIBLE);
        }
    }
    public void initView(){
        rg_sel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rg_sel_1:
                        Actor=getString(R.string.enjoy_create_text_6);
                        break;
                    case R.id.rg_sel_2:
                        Actor=getString(R.string.enjoy_create_text_7);
                        break;
                    case R.id.rg_sel_3:
                        Actor=getString(R.string.enjoy_create_text_8);
//                        RadioButton rb = (RadioButton)findViewById(rg_sel_son.getCheckedRadioButtonId());
//                        ViewInject.toast(getBaseContext(),rb.getText().toString());
                        break;
                }
                selRadio(Actor);
            }
        });

        enjoy_cost1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!StringUtil.isEmpty(enjoy_cost1.getText().toString())){
                        int unber=Integer.parseInt(enjoy_cost1.getText().toString());
                        if (unber<10){
                            enjoy_cost1.setText("10");
                        }else if (unber>30){
                            enjoy_cost1.setText("30");
                        }
                    }
                }
            }
        });
        enjoy_cost2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!StringUtil.isEmpty(enjoy_cost2.getText().toString())){
                        int unber=Integer.parseInt(enjoy_cost2.getText().toString());
                        if (unber<10){
                            enjoy_cost2.setText("10");
                        }else if (unber>20){
                            enjoy_cost2.setText("20");
                        }
                    }
                }
            }
        });
        enjoy_cost3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!StringUtil.isEmpty(enjoy_cost3.getText().toString())){
                        int unber=Integer.parseInt(enjoy_cost3.getText().toString());
                        if (unber<3){
                            enjoy_cost3.setText("3");
                        }else if (unber>9){
                            enjoy_cost3.setText("9");
                        }
                    }
                }
            }
        });
    }

    @OnClick({R.id.time_layout,R.id.launch_enjoy})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.time_layout:
                showDatePick();
                break;
            case R.id.launch_enjoy:
                postData();
                break;
        }
    }
    public String getWay(){
        String stringArray=Actor;
        if (Actor.equals(getString(R.string.enjoy_create_text_6))){
            RadioButton rb1 = (RadioButton)findViewById(rg_son.getCheckedRadioButtonId());
            stringArray+=","+rb1.getText().toString();
        }
        if (Actor.equals(getString(R.string.enjoy_create_text_7))){
            if (cb_1.isChecked()){
                stringArray+=","+cb_1.getText().toString();
            }
            if (cb_2.isChecked()){
                stringArray+=","+cb_2.getText().toString();
            }
            if (cb_3.isChecked()){
                stringArray+=","+cb_3.getText().toString();
            }
            if (stringArray.equals(getString(R.string.enjoy_create_text_7))){
                return "";
            }
        }
        if (Actor.equals(getString(R.string.enjoy_create_text_8))){
            RadioButton rb1 = (RadioButton)findViewById(rg_sel_son.getCheckedRadioButtonId());
            stringArray+=","+rb1.getText().toString();
        }

        return stringArray;
    }

    public void postData(){
        if (StringUtils.isEmpty(enjoy_title.getText().toString())){
            ViewInject.toast(this,R.string.enjoy_create_text_4);
            return;
        }
        if (StringUtils.isEmpty(getWay())){
            ViewInject.toast(this,R.string.enjoy_succed_5);
            return;
        }
        if (StringUtils.isEmpty(tet_time.getText().toString())){
            ViewInject.toast(this,R.string.enjoy_create_text_27);
            return;
        }
        if (StringUtils.isEmpty(enjoy_address.getText().toString())){
            ViewInject.toast(this,R.string.enjoy_create_text_19);
            return;
        }
        if (StringUtils.isEmpty(enjoy_cost1.getText().toString())){
            ViewInject.toast(this,R.string.enjoy_create_text_28);
            return;
        }
        if (StringUtils.isEmpty(enjoy_cost2.getText().toString())){
            ViewInject.toast(this,R.string.enjoy_create_text_29);
            return;
        }
        if (StringUtils.isEmpty(enjoy_cost3.getText().toString())){
            ViewInject.toast(this,R.string.enjoy_create_text_30);
            return;
        }

        if (StringUtils.isEmpty(phone.getText().toString())){
            ViewInject.toast(this,R.string.enjoy_create_text_26);
            return;
        }
        if (StringUtils.isEmpty(user_name.getText().toString())){
            ViewInject.toast(this,R.string.text_user_feedback6);
            return;
        }
        if (null!=null)
            dialog.show(getFragmentManager(),"");
        RadioButton rb1 = (RadioButton)findViewById(rg1.getCheckedRadioButtonId());
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("title", enjoy_title.getText().toString());
        params.put("participation_way", getWay());//参与方式
        params.put("meeting_time", tet_time.getText().toString());//预计会议时间
        params.put("address", enjoy_address.getText().toString());//会议地点
        params.put("meeting_number_type", rb1.getTag().toString());//会议人数(0: 50-100, 1: 101-300, 2: 301-500, 4: 800-1000)
//        params.put("meeting_number_type", rb1.getText().toString().equals("50-100")?"0": rb1.getText().toString().equals("101-300")?"1":rb1.getText().toString().equals("301-500")?"2":"3");//会议人数(0: 50-100, 1: 101-300, 2: 301-500, 4: 800-1000)
        params.put("broker_commission", getBroker());//合作机制-居间人提成
        params.put("city_inheritor_commission", getTerrace());//合作机制-市传承使者提成
        params.put("platform_commission", getPatform());//合作机制-平台提成
        params.put("publisher", user_name.getText().toString());//发布人姓名
        params.put("phone", phone.getText().toString());//发布人电话
        apiImp.saveEnjoy(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                finish();
                startActivity(new Intent(getBaseContext(),EnjoySucceedActivity.class));
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (null!=null)
                    dialog.dismiss();
            }
        });
    }
    public String getPatform(){
        int unber=Integer.parseInt(enjoy_cost3.getText().toString());
        if (unber<3){
            enjoy_cost3.setText("3");
        }else if (unber>9){
            enjoy_cost3.setText("9");
        }
        return enjoy_cost3.getText().toString();
    }
    public String getTerrace(){
        int unber=Integer.parseInt(enjoy_cost2.getText().toString());
        if (unber<10){
            enjoy_cost2.setText("10");
        }else if (unber>20){
            enjoy_cost2.setText("20");
        }
        return enjoy_cost3.getText().toString();
    }
    public String getBroker(){
        int unber=Integer.parseInt(enjoy_cost1.getText().toString());
        if (unber<10){
            enjoy_cost1.setText("10");
        }else if (unber>30){
            enjoy_cost1.setText("30");
        }
        return enjoy_cost1.getText().toString();
    }
    private int mMonth;
    private int mDay;
    /**
     * 弹出生日选择器
     */
    private void showDatePick() {
        DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        picker.setLabel(StringUtils.getResStr(this, R.string.pickerview_year), StringUtils.getResStr(this, R.string.pickerview_month), StringUtils.getResStr(this, R.string.pickerview_day));
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH) + 1;
        mDay = now.get(Calendar.DAY_OF_MONTH);
        FCLoger.debug("year:" + year + ",Month:" + mMonth + ",mDay:" + mDay);
        picker.setRangeStart(year, mMonth, mDay);//开始范围
        picker.setRangeEnd(year + 5, mMonth, mDay);//结束范围
//        if (mAge > 11 && mAge < 91) {
//            int year1 = year - mAge;
//            picker.setSelectedItem(year1, mMonth, mDay);
//        } else {
            picker.setSelectedItem(year, mMonth, mDay);
//        }
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tet_time.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }

}
