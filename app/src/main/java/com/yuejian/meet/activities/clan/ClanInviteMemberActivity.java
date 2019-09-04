package com.yuejian.meet.activities.clan;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ContactAdapter;
import com.yuejian.meet.adapters.SortContactAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Contact;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.CharacterParser;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class ClanInviteMemberActivity extends BaseActivity implements OnEditorActionListener, CleanableEditText.ContentClear {
    private CharacterParser characterParser;
    String clanId = "0";
    String clanName = "";
    ContactAdapter contactAdapter;
    public CleanableEditText contactSearch;
    @Bind(R.id.contact_un_choose)
    TextView contact_un_choose;
    private List<Contact> contacts = new ArrayList();
    private LoadingDialogFragment dialog;
    @Bind(R.id.contact_lv)
    ListView listView;
    SortContactAdapter mAdapter;
    PinyinComparator pinyinComparator;
    @Bind(R.id.contact_rv)
    RecyclerView recyclerView;
    @Bind(R.id.txt_titlebar_release)
    TextView release;
    private List<Contact> selectContacts = new ArrayList();
    SideBar sideBar;

    private List<Contact> filledData(List<Contact> paramList)
    {
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        if (i < paramList.size())
        {
            Contact localContact = (Contact)paramList.get(i);
            String str = this.characterParser.getSelling(localContact.getSurname()).substring(0, 1).toUpperCase();
            if (str.matches("[A-Z]")) {
                localContact.setSortLetters(str.toUpperCase());
            }
                localArrayList.add(localContact);
                i += 1;
                localContact.setSortLetters("#");
        }
        return localArrayList;
    }

    private void getFriendsList()
    {
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        localHashMap.put("surname", this.user.getSurname());
        localHashMap.put("relation_type", "2");
        this.apiImp.SurnameFrind(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                contacts = JSON.parseArray(paramAnonymousString, Contact.class);
                if ((paramAnonymousString != null) && (contacts.size() > 0))
                {
                    Iterator localIterator = ClanInviteMemberActivity.this.selectContacts.iterator();
                    label114:
                    while (localIterator.hasNext())
                    {
                        Contact localContact = (Contact)localIterator.next();
                        paramAnonymousInt = 0;
                        for (;;)
                        {
                            if (paramAnonymousInt >= contacts.size()) {
                                break label114;
                            }
                            if (localContact.getCustomer_id().equals(((Contact)contacts.get(paramAnonymousInt)).getCustomer_id()))
                            {
                                ((Contact)contacts.get(paramAnonymousInt)).setSelect(Boolean.valueOf(true));
                                break;
                            }
                            paramAnonymousInt += 1;
                        }
                    }
                    ClanInviteMemberActivity.this.contacts.clear();
                    ClanInviteMemberActivity.this.contacts.addAll(ClanInviteMemberActivity.this.filledData(contacts));
                    Collections.sort(ClanInviteMemberActivity.this.contacts, ClanInviteMemberActivity.this.pinyinComparator);
                    ClanInviteMemberActivity.this.mAdapter.setListDate(ClanInviteMemberActivity.this.contacts);
                    ClanInviteMemberActivity.this.mAdapter.refresh(ClanInviteMemberActivity.this.contacts);
                    return;
                }
                ClanInviteMemberActivity.this.contacts.clear();
                ClanInviteMemberActivity.this.mAdapter.refresh(ClanInviteMemberActivity.this.contacts);
            }
        });
    }

    public void ClearText()
    {
        getFriendsList();
    }

    public void delectSelectData(Contact paramContact)
    {
        int i = 0;
        if (i < this.selectContacts.size())
        {
            if (((Contact)this.selectContacts.get(i)).getCustomer_id().equals(paramContact.getCustomer_id())) {
                this.selectContacts.remove(i);
            }
        }
        else
        {
            this.contactAdapter.setListData(this.selectContacts);
            i = 0;
        }
            if (i < this.contacts.size())
            {
                if (((Contact)this.contacts.get(i)).getCustomer_id() == paramContact.getCustomer_id()) {
                    ((Contact)this.contacts.get(i)).setSelect(Boolean.valueOf(false));
                }
            }
            else
            {
                this.mAdapter.setListDate(this.contacts);
                this.mAdapter.refresh(this.contacts);
                if (this.selectContacts.size() <= 0) {
                }
                this.release.setText("确定(" + this.selectContacts.size() + ")");
                this.contact_un_choose.setVisibility(View.GONE);
                return;
            }
        this.release.setText("确定");
        this.contact_un_choose.setVisibility(View.VISIBLE);
    }

    public void foundGroup()
    {
        if (this.dialog != null) {
            this.dialog.show(getFragmentManager(), "");
        }
        String str = "";
        int i = 0;
        if (i < this.selectContacts.size())
        {
            if (str.equals("")) {}
            for (str = ((Contact)this.selectContacts.get(i)).getCustomer_id();; str = str + "," + ((Contact)this.selectContacts.get(i)).getCustomer_id())
            {
                i += 1;
                break;
            }
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", this.clanId);
        localHashMap.put("be_invite_customer_id", AppConfig.CustomerId);
        localHashMap.put("invite_customer_ids", str);
        this.apiImp.inviteClanMember(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt)
            {
                if (ClanInviteMemberActivity.this.dialog != null) {
                    ClanInviteMemberActivity.this.dialog.dismiss();
                }
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                ViewInject.toast(ClanInviteMemberActivity.this.getApplication(), "邀请成功");
                ClanInviteMemberActivity.this.finish();
            }
        });
    }

    public void initView()
    {
        this.contactSearch = ((CleanableEditText)findViewById(R.id.et_contact_search));
        this.release.setVisibility(View.VISIBLE);
        this.release.setText("确定");
        this.dialog = LoadingDialogFragment.newInstance("正在请求");
        this.contactSearch.setOnEditorActionListener(this);
        this.pinyinComparator = new PinyinComparator();
        this.characterParser = CharacterParser.getInstance();
        this.contactAdapter = new ContactAdapter();
        this.mAdapter = new SortContactAdapter(this.listView, this.contacts, R.layout.item_manager_member);
        this.listView.setAdapter(this.mAdapter);
        LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(this);
        localLinearLayoutManager.setOrientation(0);
        this.recyclerView.setLayoutManager(localLinearLayoutManager);
        this.recyclerView.setAdapter(this.contactAdapter);
        this.sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener()
        {
            public void onTouchingLetterChanged(String paramAnonymousString)
            {
                int i = ClanInviteMemberActivity.this.mAdapter.getPositionForSection(paramAnonymousString.charAt(0));
                if (i != -1) {
                    ClanInviteMemberActivity.this.listView.setSelection(i);
                }
            }
        });
        getFriendsList();
        this.contactSearch.setRegister(this);
    }

    public void isSelectContact(Contact paramContact)
    {
        if (paramContact.getSelect().booleanValue()) {
            this.selectContacts.add(paramContact);
        }
            this.contactAdapter.setListData(this.selectContacts);
            if (paramContact.getSelect().booleanValue()) {
                this.recyclerView.scrollToPosition(this.contactAdapter.getItemCount() - 1);
            }
            if (this.selectContacts.size() <= 0) {
            }
            this.release.setText("确定(" + this.selectContacts.size() + ")");
            this.contact_un_choose.setVisibility(View.GONE);
//                if (((Contact)this.selectContacts.get(i)).getCustomer_id().equals(paramContact.getCustomer_id()))
//                {
//                    this.selectContacts.remove(i);
//                }
        this.release.setText("确定");
        this.contact_un_choose.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.txt_titlebar_release, R.id.surname_imgBtn_search})
    public void onClick(View paramView)
    {
        switch (paramView.getId())
        {
            case R.id.surname_imgBtn_search:
                getFriendsList();
                return;
            case R.id.txt_titlebar_release:
                if (this.selectContacts.size() == 0)
                {
                    ViewInject.shortToast(this, "请选择好友");
                    return;
                }
                foundGroup();
                return;
        }
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        getWindow().setSoftInputMode(32);
        setContentView(R.layout.activity_clan_invite_member);
        this.sideBar = ((SideBar)findViewById(R.id.contact_slide_bar));
        if (getIntent().hasExtra("clanId")) {
            this.clanId = getIntent().getStringExtra("clanId");
        }
        setTitleText("选择成员");
        initView();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        this.contactSearch.unRegisyter();
    }

    public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
    {
        if ((paramInt == 4) || ((paramKeyEvent != null) && (paramKeyEvent.getKeyCode() == 66)))
        {
            getFriendsList();
            Utils.hideSystemKeyBoard(this, this.contactSearch);
            return true;
        }
        return false;
    }


    private class PinyinComparator
            implements Comparator<Contact>
    {
        private PinyinComparator() {}

        public int compare(Contact paramContact1, Contact paramContact2)
        {
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
