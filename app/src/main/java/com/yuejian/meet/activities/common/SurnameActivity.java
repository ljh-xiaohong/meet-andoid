package com.yuejian.meet.activities.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.SurnameAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.SurnameEntity;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.CleanableEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author :
 * @time   : 2018/11/19 17:08
 * @desc   : 选择姓氏
 * @version: V1.0
 * @update : 2018/11/19 17:08
 */

public class SurnameActivity extends BaseActivity implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener,CleanableEditText.ContentClear {

    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton back;
    @Bind(R.id.txt_titlebar_title)
    TextView title;
    @Bind(R.id.relation_service)
    TextView relation_service;
    @Bind(R.id.surname_gridview)
    GridView gridView;
//    @Bind(R.id.et_surname_search)
    CleanableEditText et_search;
    @Bind(R.id.user_service)
    RelativeLayout user_service;
    @Bind(R.id.ll_me_surname_layout)
    LinearLayout ll_me_surname_layout;
    @Bind(R.id.txt_me_name)
    TextView txt_me_name;
    @Bind(R.id.txt_all_surname)
    TextView txt_all_surname;
    SurnameAdapter mAdapter;
    List<SurnameEntity> listData = new ArrayList<>();
    List<SurnameEntity> listSearch = new ArrayList<>();
    String meSurname;
    ApiImp api = new ApiImp();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (AppConfig.isGeLiGuide){
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
        ACTIVITY_NAME = "选择姓氏";
        setContentView(R.layout.activity_surname);
        et_search= (CleanableEditText) findViewById(R.id.et_surname_search);
        et_search.setRegister(this);
        Utils.settingPutInt(this);
        Intent intent = getIntent();
        if (intent.hasExtra("meSurname")) {
            meSurname = intent.getStringExtra("meSurname");
            meSurname=Utils.s2tOrT2s(meSurname);
            if (meSurname.equals(getString(R.string.surnames_all))) {
                txt_all_surname.setVisibility(View.INVISIBLE);
            } else {
                txt_all_surname.setVisibility(View.VISIBLE);
            }
            ll_me_surname_layout.setVisibility(View.VISIBLE);
//            txt_me_name.setSelected(true);
            txt_me_name.setText(meSurname);
        }
        initView();
    }

    public void initView() {
        setTitleText(getString(R.string.All_the_family_name));
        relation_service.setSelected(true);
        back.setVisibility(View.VISIBLE);
        et_search.setOnEditorActionListener(this);
        gridView.setOnItemClickListener(this);
        mAdapter = new SurnameAdapter(gridView, listData, R.layout.item_surname_layout);
        gridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        getRequstData();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            search(v.getText().toString());
            hintKbTwo();
            return true;
        }
        return false;
    }

    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 搜索
     */
    public void search(String name) {
        listSearch.clear();
        user_service.setVisibility(View.GONE);
        if (name.equals("")) {
//            listData.addAll(allSurnames);
            mAdapter.refresh(listData);
            return;
        }
        for (SurnameEntity entity : listData) {
            if (entity.getSurname().contains(name))
                listSearch.add(entity);
        }
        if (listSearch.size() == 0) {
            user_service.setVisibility(View.VISIBLE);
        }
        mAdapter.refresh(listSearch);
    }

    /**
     * 获取姓氏
     */
    private List<SurnameEntity> allSurnames = new ArrayList<>();

    public void getRequstData() {
        api.getSurname(this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData = JSON.parseArray(data, SurnameEntity.class);
                allSurnames = listData;
                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        intent = new Intent();
        if (listSearch.size() == 0) {
            intent.putExtra("surname", listData.get(position).getSurname());
            intent.putExtra("surnameId", listData.get(position).getId());
            if (meSurname != null) {
                BusCallEntity entity = new BusCallEntity();
                entity.setCallType(BusEnum.SURNAME_UPDATE);
                entity.setData(listData.get(position).getSurname());
                Bus.getDefault().post(entity);
            }
        } else {
            intent.putExtra("surname", listSearch.get(position).getSurname());
            intent.putExtra("surnameId", listSearch.get(position).getId());
            if (meSurname != null) {
                BusCallEntity entity = new BusCallEntity();
                entity.setCallType(BusEnum.SURNAME_UPDATE);
                entity.setData(listSearch.get(position).getSurname());
                Bus.getDefault().post(entity);
            }
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (event.getCallType()==BusEnum.NETWOR_CONNECTION){
            if (listData.size()==0)
                getRequstData();
        }
    }

    @OnClick({R.id.surname_imgBtn_search, R.id.titlebar_imgBtn_back, R.id.relation_service, R.id.txt_me_name, R.id.txt_all_surname})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_imgBtn_back://返回
                finish();
                break;
            case R.id.surname_imgBtn_search://搜索
                search(et_search.getText().toString());
                break;
            case R.id.relation_service://联系客服
                try {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + "LkgTW4D_IAO6mlaiUzn5dUsgxEQ0sZMe"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.txt_me_name://我的姓
                BusCallEntity entity = new BusCallEntity();
                entity.setCallType(BusEnum.SURNAME_UPDATE);
                entity.setData(txt_me_name.getText().toString());
                Bus.getDefault().post(entity);
                Intent intent = new Intent();
                intent.putExtra("surname", txt_me_name.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.txt_all_surname://全部
                BusCallEntity entity2 = new BusCallEntity();
                entity2.setCallType(BusEnum.SURNAME_UPDATE);
                entity2.setData(getString(R.string.home_all_name));
                Bus.getDefault().post(entity2);
                intent = new Intent();
                intent.putExtra("surname", getString(R.string.home_all_name));
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void ClearText() {
        search("");
    }
}
