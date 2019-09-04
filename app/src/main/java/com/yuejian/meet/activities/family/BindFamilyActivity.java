package com.yuejian.meet.activities.family;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ContactAdapter;
import com.yuejian.meet.adapters.SortContactAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Contact;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.CharacterParser;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BindFamilyActivity extends BaseActivity implements TextView.OnEditorActionListener, CleanableEditText.ContentClear {
    private CharacterParser characterParser;
    String clanId = "0";
    ContactAdapter contactAdapter;
    public CleanableEditText contactSearch;
    private List<Contact> contacts = new ArrayList();
    private LoadingDialogFragment dialog;
    private String familyId;
    @Bind({R.id.contact_lv})
    ListView listView;
    SortContactAdapter mAdapter;
    PinyinComparator pinyinComparator;
    @Bind({R.id.txt_titlebar_send_moment})
    TextView release;
    private List<Contact> selectContacts = new ArrayList();
    SideBar sideBar;

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

    private void getFriendsList() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        localHashMap.put("keyword", this.contactSearch.getText().toString());
        this.apiImp.getGroupFriends(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                List<Contact> tmp = JSON.parseArray(paramAnonymousString, Contact.class);
                if ((tmp != null) && (tmp.size() > 0)) {
                    for (Contact contact : tmp) {
                        for (Contact selectContact : selectContacts) {
                            if (selectContact.customer_id.equals(contact.customer_id)) {
                                contact.setSelect(true);
                            }
                        }
                    }
                    contacts.clear();
                    contacts.addAll(filledData(tmp));
                    Collections.sort(contacts, pinyinComparator);
                    mAdapter.setListDate(contacts);
                    mAdapter.refresh(contacts);
                    return;
                }
                contacts.clear();
                mAdapter.refresh(contacts);
            }
        });
    }

    public void ClearText() {
        getFriendsList();
    }

    public void delectSelectData(Contact paramContact) {
        for (Contact contact : contacts) {
            if (contact.customer_id.equals(paramContact.customer_id)) {
                contact.setSelect(false);
            }
        }
        this.familyId = "";
        this.mAdapter.setListDate(this.contacts);
        this.mAdapter.refresh(this.contacts);
    }

    public void initView() {
        this.contactSearch = ((CleanableEditText) findViewById(R.id.et_contact_search));
        this.release.setVisibility(View.VISIBLE);
        this.release.setText("确定");
        this.dialog = LoadingDialogFragment.newInstance("正在请求");
        this.contactSearch.setOnEditorActionListener(this);
        this.pinyinComparator = new PinyinComparator();
        this.characterParser = CharacterParser.getInstance();
        this.contactAdapter = new ContactAdapter();
        this.mAdapter = new SortContactAdapter(this.listView, this.contacts, R.layout.item_manager_member);
        this.listView.setAdapter(this.mAdapter);
        new LinearLayoutManager(this).setOrientation(0);
        this.sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            public void onTouchingLetterChanged(String paramAnonymousString) {
                int i = mAdapter.getPositionForSection(paramAnonymousString.charAt(0));
                if (i != -1) {
                    listView.setSelection(i);
                }
            }
        });
        getFriendsList();
        this.contactSearch.setRegister(this);
    }

    public void isSelectContact(Contact paramContact) {
        //单选，先清除之前选中的选项
        this.familyId = "";
        for (Contact contact : contacts) {
            if (contact.customer_id.equals(paramContact.customer_id)) {
                if (paramContact.isSelect) {
                    this.familyId = paramContact.customer_id;
                    contact.setSelect(true);
                } else {
                    contact.setSelect(false);
                }
            } else {
                contact.setSelect(false);
            }
        }

        this.mAdapter.setListDate(this.contacts);
        this.mAdapter.refresh(this.contacts);
    }

    @OnClick({R.id.txt_titlebar_send_moment})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.txt_titlebar_send_moment:
                if (StringUtils.isEmpty(this.familyId)) {
                    ViewInject.shortToast(this, "请选择好友");
                    return;
                }
                Bus.getDefault().post("family_id_" + this.familyId);
                finish();
                break;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        getWindow().setSoftInputMode(32);
        setContentView(R.layout.activity_bind_famliy);
        this.sideBar = ((SideBar) findViewById(R.id.contact_slide_bar));
        if (getIntent().hasExtra("clanId")) {
            this.clanId = getIntent().getStringExtra("clanId");
        }
        setTitleText("关联家人");
        initBackButton(true);
        initView();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.contactSearch.unRegisyter();
    }

    public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent) {
        if ((paramInt == 4) || ((paramKeyEvent != null) && (paramKeyEvent.getKeyCode() == 66))) {
            getFriendsList();
            Utils.hideSystemKeyBoard(this, this.contactSearch);
            return true;
        }
        return false;
    }

    private class PinyinComparator
            implements Comparator<Contact> {
        private PinyinComparator() {
        }

        public int compare(Contact paramContact1, Contact paramContact2) {
            if (paramContact2.getSortLetters().equals("#")) {
                return -1;
            }
            if (paramContact1.getSortLetters().equals("#")) {
                return 1;
            }
            return paramContact1.getSortLetters().compareTo(paramContact2.getSortLetters());
        }
    }
}
