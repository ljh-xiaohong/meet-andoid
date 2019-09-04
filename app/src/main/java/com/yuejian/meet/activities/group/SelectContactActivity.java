package com.yuejian.meet.activities.group;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.app.myenum.ChatEnum;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ContactAdapter;
import com.yuejian.meet.adapters.SortContactAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Contact;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.CharacterParser;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.SideBar;
import com.yuejian.meet.widgets.SwipeRefreshView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class SelectContactActivity extends BaseActivity implements TextView.OnEditorActionListener,CleanableEditText.ContentClear{

    @Bind(R.id.txt_titlebar_release)
    TextView release;
    @Bind(R.id.contact_lv)
    ListView listView;
    @Bind(R.id.contact_rv)
    RecyclerView recyclerView;
    public CleanableEditText contactSearch;
    SideBar sideBar;
    PinyinComparator pinyinComparator;
    @Bind(R.id.contact_un_choose)
    TextView contact_un_choose;
    ContactAdapter contactAdapter;
    SortContactAdapter mAdapter;
    String tId="0";
    Boolean isFound=true;
    private CharacterParser characterParser;
    private List<Contact> contacts = new ArrayList<>();
    private List<Contact> selectContacts = new ArrayList<>();
    private LoadingDialogFragment dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_select_contact);
        sideBar=(SideBar) findViewById(R.id.contact_slide_bar);
        if (getIntent().hasExtra("tId"))
            tId=getIntent().getStringExtra("tId");
        if (getIntent().hasExtra("isFound"))
            isFound=getIntent().getBooleanExtra("isFound",true);

        setTitleText(isFound?getString(R.string.A_group_chat):getString(R.string.select_contact));
        initView();
    }
///FriendsFragment
    public void initView(){
        contactSearch= (CleanableEditText) findViewById(R.id.et_contact_search);
        release.setVisibility(View.VISIBLE);
        release.setText(R.string.confirm);
        dialog = LoadingDialogFragment.newInstance(getString(R.string.is_requesting));
        contactSearch.setOnEditorActionListener(this);
        pinyinComparator=new PinyinComparator();
        characterParser = CharacterParser.getInstance();
        contactAdapter=new ContactAdapter();
        mAdapter=new SortContactAdapter(listView,contacts,R.layout.item_manager_member);
        listView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactAdapter);
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
        getFriendsList();
        contactSearch.setRegister(this);
    }

    /**
     * 获取好友
     */
    private void getFriendsList() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        if (!tId.equals("0"))
            params.put("t_id", tId);
        params.put("keyword", contactSearch.getText().toString());
        apiImp.getGroupFriends(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Contact> contactList = JSON.parseArray(data, Contact.class);
                if (contactList != null && contactList.size() > 0) {
                    for (Contact info:selectContacts){
                        for (int i=0;i<contactList.size();i++){
                            if (info.getCustomer_id().equals(contactList.get(i).getCustomer_id())){
                                contactList.get(i).setSelect(true);
                                break;
                            }
                        }
                    }
                    contacts.clear();
                    contacts.addAll(filledData(contactList));
                    Collections.sort(contacts, pinyinComparator);
                    mAdapter.setListDate(contacts);
                    mAdapter.refresh(contacts);
                    // 根据a-z进行排序源数据
                }else {
                    contacts.clear();
                    mAdapter.refresh(contacts);
                }
//                adapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
    //选择或删除
    public void isSelectContact(Contact item){
        if (item.getSelect()){
            selectContacts.add(item);
        }else{
            for (int i=0;i<selectContacts.size();i++){
                if (selectContacts.get(i).getCustomer_id().equals(item.getCustomer_id())){
                    selectContacts.remove(i);
                    break;
                }
            }
        }
        contactAdapter.setListData(selectContacts);
        if (item.getSelect()){
            recyclerView.scrollToPosition(contactAdapter.getItemCount()-1);
        }
        if(selectContacts.size()>0){
            release.setText(getString(R.string.confirm)+"("+selectContacts.size()+")");
            contact_un_choose.setVisibility(View.GONE);
        }else {
            release.setText(R.string.confirm);
            contact_un_choose.setVisibility(View.VISIBLE);
        }
    }
    public void delectSelectData(Contact item){
        for (int i=0;i<selectContacts.size();i++){
            if (selectContacts.get(i).getCustomer_id().equals(item.getCustomer_id())){
                selectContacts.remove(i);
                break;
            }
        }
        contactAdapter.setListData(selectContacts);
        for (int n=0;n<contacts.size();n++){
            if (contacts.get(n).getCustomer_id()==item.getCustomer_id()){
                contacts.get(n).setSelect(false);
                break;
            }
        }
        mAdapter.setListDate(contacts);
        mAdapter.refresh(contacts);
        if(selectContacts.size()>0){
            release.setText(getString(R.string.confirm)+"("+selectContacts.size()+")");
            contact_un_choose.setVisibility(View.GONE);
        }else {
            release.setText(R.string.confirm);
            contact_un_choose.setVisibility(View.VISIBLE);
        }
    };
    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<Contact> filledData(List<Contact> date) {
        List<Contact> mSortList = new ArrayList<Contact>();
        for (int i = 0; i < date.size(); i++) {
            Contact contact = date.get(i);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(contact.getSurname());
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

    @Override
    public void ClearText() {
        getFriendsList();
    }

    private class PinyinComparator implements Comparator<Contact> {

        public int compare(Contact o1, Contact o2) {
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

    @OnClick({R.id.txt_titlebar_release,R.id.surname_imgBtn_search})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_titlebar_release:
                if (selectContacts.size()==0){
                    ViewInject.shortToast(this,R.string.select_contact_toast);
                    return;
                }else {
                    if (tId.equals("0"))
                        foundGroup();
                    else
                        IntoGRoup();
                }
                break;
            case R.id.surname_imgBtn_search:
                getFriendsList();
                break;
        }
    }

    /**
     * 邀请好友入群
     */
    public  void IntoGRoup(){

        if (dialog!=null)
            dialog.show(getFragmentManager(),"");
        List<String> selected=new ArrayList<>();
        for (Contact info:selectContacts){
            selected.add(info.getCustomer_id());
        }

        NIMClient.getService(TeamService.class).addMembers(tId, selected).setCallback(new RequestCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> param) {
                String members="";
                for (int i=0;i<selectContacts.size();i++){
                    if (members.equals("")){
                        members=selectContacts.get(i).getCustomer_id();
                    }else {
                        members=members+","+selectContacts.get(i).getCustomer_id();
                    }
                }
                Map<String,Object> params=new HashMap<>();
                params.put("customer_id",AppConfig.CustomerId);
                params.put("invited_ids",members);
                params.put("t_id",tId);
                apiImp.getIntoGroup(params, this, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        Intent intent=new Intent();
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

            @Override
            public void onFailed(int code) {
                ViewInject.toast(mContext,R.string.select_contact_toast1);
                if (dialog!=null)
                    dialog.dismiss();
            }

            @Override
            public void onException(Throwable exception) {
                ViewInject.toast(mContext,R.string.select_contact_toast1);
                exception.printStackTrace();
                if (dialog!=null)
                    dialog.dismiss();
            }
        });

    }

    /**
     * 创建群
     */
    public void foundGroup(){
        if (selectContacts.size()==1){
            ImUtils.toP2PCaht(mContext,selectContacts.get(0).getCustomer_id());
            finish();
            return;
        }
        if (dialog!=null)
            dialog.show(getFragmentManager(),"");
        String members="";
        for (int i=0;i<selectContacts.size();i++){
            if (members.equals("")){
                members=selectContacts.get(i).getCustomer_id();
            }else {
                members=members+","+selectContacts.get(i).getCustomer_id();
            }
        }
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",AppConfig.CustomerId);
        params.put("members",members);
        apiImp.getFoundGroup(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ImUtils.toAssemblyHall(mContext,data, ChatEnum.FoundGroup,"");
//                BusCallEntity busCallEntity=new BusCallEntity();
//                busCallEntity.setCallType(BusEnum.GROUP_UPDATE);
//                Bus.getDefault().post(busCallEntity);
                finish();
            }
            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)
                    dialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactSearch.unRegisyter();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            getFriendsList();
            Utils.hideSystemKeyBoard(this,contactSearch);
            return true;
        }
        return false;
    }
}
