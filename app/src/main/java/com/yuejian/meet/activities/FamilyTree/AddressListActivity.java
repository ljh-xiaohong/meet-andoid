package com.yuejian.meet.activities.FamilyTree;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.api.utils.PreferencesMessage;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.message.GroupChatActivity;
import com.yuejian.meet.adapters.AddressListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.AddressListEntity;
import com.yuejian.meet.bean.BookJson;
import com.yuejian.meet.utils.CharacterParser;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 通讯录
 */
public class AddressListActivity extends BaseActivity implements TextView.OnEditorActionListener{
    @Bind(R.id.contact_slide_bar)
    SideBar sideBar;
    @Bind(R.id.et_search_all)
    CleanableEditText editText;
    @Bind(R.id.address_list)
    ListView listView;
    View viewHeader;
    AddressListAdapter mAdapter;
    private CharacterParser characterParser;
    PinyinComparator pinyinComparator;
    List<AddressListEntity> listData=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_address_list);
        initView();
        initData();
    }
    public void initView(){
        characterParser = CharacterParser.getInstance();
        pinyinComparator=new PinyinComparator();
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });
        editText.setOnEditorActionListener(this);
        mAdapter=new AddressListAdapter(listView,listData,R.layout.item_address_list_layout);
        listView.setAdapter(mAdapter);
        viewHeader=View.inflate(this,R.layout.item_address_list_top_layout,null);
//        listView.addHeaderView(viewHeader);
        mAdapter.notifyDataSetChanged();
        viewHeader.findViewById(R.id.add_new_friend).setOnClickListener(this);
        viewHeader.findViewById(R.id.grop_chat_layout).setOnClickListener(this);
        viewHeader.findViewById(R.id.blacklist_layout).setOnClickListener(this);

    }
    public void initData(){
        setTitleText("朋友");
        requstData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.add_new_friend:
                startActivityIfNeeded(new Intent(this,AddNewFriendActivity.class),20);
                break;
            case R.id.grop_chat_layout:
                if (PreferencesMessage.get(mContext,PreferencesMessage.family_id+ AppConfig.CustomerId,"0").equals("0")){
                    final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setMessage(R.string.Fill_in_the_location);
                    builder.setPositiveButton(R.string.To_fill_in, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            BusCallEntity busCallEntity=new BusCallEntity();
                            busCallEntity.setCallType(BusEnum.Bangding_Family);
                            Bus.getDefault().post(busCallEntity);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel,null);
                    builder.show();
                    return;
                }
                startActivity(new Intent(this, GroupChatActivity.class));
                break;
            case R.id.blacklist_layout:
                startActivityIfNeeded(new Intent(this,FriendBlacklistActivity.class),20);
                break;
        }
    }
    public void postBalck(String opCustomeerId){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("op_customer_id",opCustomeerId);
        params.put("is_black","1");
        apiImp.getBlack(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                requstData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void delFriend(String opCustomeerId){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("op_customer_id",opCustomeerId);
        apiImp.delFriend(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                requstData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    public void requstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("name",editText.getText().toString());
        apiImp.getMyFriend(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData= JSON.parseArray(data,AddressListEntity.class);
                listData=filledData(listData);
                Collections.sort(listData, pinyinComparator);
                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 20:
                    requstData();
                    break;
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            requstData();
            Utils.hideSystemKeyBoard(this,editText);
            return true;
        }
        return false;
    }
    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<AddressListEntity> filledData(List<AddressListEntity> date) {
        List<AddressListEntity> mSortList = new ArrayList<AddressListEntity>();
        for (int i = 0; i < date.size(); i++) {
            AddressListEntity contact = date.get(i);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(contact.getFullname());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                contact.setSortLetters(sortString.toUpperCase());
            } else {
                contact.setSortLetters("#");
            }
            mSortList.add(contact);
        }
        return mSortList;

    }
    private class PinyinComparator implements Comparator<AddressListEntity> {

        public int compare(AddressListEntity o1, AddressListEntity o2) {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            if (o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }
    }
}
