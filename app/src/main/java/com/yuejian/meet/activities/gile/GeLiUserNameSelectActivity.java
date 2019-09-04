package com.yuejian.meet.activities.gile;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.activities.mine.GeLiLoginActivity;
import com.yuejian.meet.activities.mine.RegisterActivity;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.utils.Utils;

import butterknife.Bind;
import butterknife.OnClick;

import static android.provider.Settings.Global.putInt;

/**
 * 选择性
 */
public class GeLiUserNameSelectActivity extends BaseActivity {
    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton back;
    @Bind(R.id.txt_titlebar_title)
    TextView title;
    @Bind(R.id.txt_name)
    TextView name;
    @Bind(R.id.bt_surname)
    TextView surname;
    @Bind(R.id.bu_next)
    Button bu_next;
    @Bind(R.id.geli_user_name_return)
    TextView geli_user_name_return;
    Intent intent;
    String surnameId="";
    String mobile="",openId="",flag="",photo="",nationCode="";

    ApiImp api=new ApiImp();

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geli_user_name_select);
        Utils.settingPutInt(this);
        initView();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public void initView(){
        setTitleText("用户名");
        geli_user_name_return.setText("<<上一步");
        bu_next.setEnabled(false);
        intent=getIntent();
        if (intent.hasExtra("mobile"))
            mobile=intent.getStringExtra("mobile");
        if (intent.hasExtra("openId"))
            openId=intent.getStringExtra("openId");
        if (intent.hasExtra("flag"))
            flag=intent.getStringExtra("flag");
        if (intent.hasExtra("photo"))
            photo=intent.getStringExtra("photo");
        if (intent.hasExtra("nationCode"))
            nationCode=intent.getStringExtra("nationCode");
        back.setVisibility(View.VISIBLE);
        name.addTextChangedListener(new TextWatcherImpl());
    }

    @OnClick({R.id.titlebar_imgBtn_back,R.id.bu_next,R.id.bt_surname,R.id.geli_user_name_return})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titlebar_imgBtn_back:///返回
                finish();
                break;
            case R.id.bt_surname:///选择姓
                intent=new Intent(getApplication(), SurnameActivity.class);
                startActivityIfNeeded(intent,3);
                break;
            case R.id.bu_next:///下一步
                intent=new Intent(getApplication(),GeLiRegisterActivity.class);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("surname",surname.getText().toString());
                intent.putExtra("mobile",mobile);
                intent.putExtra("openId",openId);
                intent.putExtra("flag",flag);
                intent.putExtra("photo",photo);
                intent.putExtra("nationCode",nationCode);
                startActivity(intent);
                finish();
                break;
            case R.id.geli_user_name_return:
                startActivity(new Intent(getApplication(), GeLiLoginActivity.class));
                finish();
                break;
        }
    }
    //输入监听
    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            if (surname.getText().toString().length()>=1&&name.getText().toString().length()>=1){
                bu_next.setSelected(true);
                bu_next.setEnabled(true);
            }else {
                bu_next.setSelected(false);
                bu_next.setEnabled(false);
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==3){
                surname.setText(data.getExtras().getString("surname"));
                surnameId=data.getExtras().getString("surnameId");
            }
            if (surname.getText().toString().length()>=1&&name.getText().toString().length()>=1){
                bu_next.setSelected(true);
                bu_next.setEnabled(true);
            }else {
                bu_next.setSelected(false);
                bu_next.setEnabled(false);
            }
        }
    }
}
