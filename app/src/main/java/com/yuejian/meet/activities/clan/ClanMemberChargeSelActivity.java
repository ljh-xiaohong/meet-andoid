package com.yuejian.meet.activities.clan;

import android.content.Intent;
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
import com.yuejian.meet.adapters.clan.ClanMemberChargeAdapter;
import com.yuejian.meet.adapters.clan.ClanMemberchargeSelAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ClanFindApprovaEntity;
import com.yuejian.meet.bean.ClanMiAllEntity;
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

public class ClanMemberChargeSelActivity extends BaseActivity implements OnEditorActionListener, CleanableEditText.ContentClear {
    private CharacterParser characterParser;
    String clanId = "0";
    ClanMemberchargeSelAdapter contactAdapter;
    public CleanableEditText contactSearch;
    @Bind(R.id.contact_un_choose)
    TextView contact_un_choose;
    private List<ClanFindApprovaEntity> contacts = new ArrayList();
    String customer_id;
    private LoadingDialogFragment dialog;
    @Bind(R.id.contact_lv)
    ListView listView;
    ClanMemberChargeAdapter mAdapter;
    PinyinComparator pinyinComparator;
    @Bind(R.id.contact_rv)
    RecyclerView recyclerView;
    @Bind(R.id.txt_titlebar_release)
    TextView release;
    private List<ClanFindApprovaEntity> selectContacts = new ArrayList();
    SideBar sideBar;

    private List<ClanFindApprovaEntity> filledData(List<ClanFindApprovaEntity> paramList) {
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        if (i < paramList.size()) {
            ClanFindApprovaEntity localClanFindApprovaEntity = (ClanFindApprovaEntity)paramList.get(i);
            String str = this.characterParser.getSelling(localClanFindApprovaEntity.getSurname()).substring(0, 1).toUpperCase();
            if (str.matches("[A-Z]")) {
                localClanFindApprovaEntity.setSortLetters(str.toUpperCase());
            }
                localArrayList.add(localClanFindApprovaEntity);
                localClanFindApprovaEntity.setSortLetters("#");
            }
        return localArrayList;
    }

    private void getFriendsList() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("association_id", this.clanId);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.clanMember(localHashMap, this, new DataIdCallback<String>()
        {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {}

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt)
            {
                Object localObject = (ClanMiAllEntity) JSON.parseObject(paramAnonymousString, ClanMiAllEntity.class);
                List<ClanFindApprovaEntity>  clanFindList= new ArrayList();
                clanFindList.add(JSON.parseObject(((ClanMiAllEntity)localObject).getCustomer(), ClanFindApprovaEntity.class));
                clanFindList.addAll(JSON.parseArray(((ClanMiAllEntity)localObject).getMemberList(), ClanFindApprovaEntity.class));
                if ((clanFindList != null) && (clanFindList.size() > 0))
                {
                    localObject = ClanMemberChargeSelActivity.this.selectContacts.iterator();
                    label157:
                    while (((Iterator)localObject).hasNext())
                    {
                        ClanFindApprovaEntity localClanFindApprovaEntity = (ClanFindApprovaEntity)((Iterator)localObject).next();
                        paramAnonymousInt = 0;
                        for (;;)
                        {
                            if (paramAnonymousInt >= clanFindList.size()) {
                                break label157;
                            }
                            if (localClanFindApprovaEntity.getCustomer_id().equals(((ClanFindApprovaEntity)clanFindList.get(paramAnonymousInt)).getCustomer_id()))
                            {
                                ((ClanFindApprovaEntity)clanFindList.get(paramAnonymousInt)).setSelect(Boolean.valueOf(true));
                                break;
                            }
                            paramAnonymousInt += 1;
                        }
                    }
                    ClanMemberChargeSelActivity.this.contacts.clear();
                    ClanMemberChargeSelActivity.this.contacts.addAll(ClanMemberChargeSelActivity.this.filledData(clanFindList));
                    Collections.sort(ClanMemberChargeSelActivity.this.contacts, ClanMemberChargeSelActivity.this.pinyinComparator);
                    ClanMemberChargeSelActivity.this.mAdapter.setListDate(ClanMemberChargeSelActivity.this.contacts);
                    ClanMemberChargeSelActivity.this.postSearch();
                    return;
                }
                ClanMemberChargeSelActivity.this.contacts.clear();
                ClanMemberChargeSelActivity.this.mAdapter.refresh(ClanMemberChargeSelActivity.this.contacts);
            }
        });
    }

    public void ClearText()
    {
        postSearch();
    }

    public void delectSelectData(ClanFindApprovaEntity item) {
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
            release.setText("确定("+selectContacts.size()+")");
            contact_un_choose.setVisibility(View.GONE);
        }else {
            release.setText("确定");
            contact_un_choose.setVisibility(View.VISIBLE);
        }

    }

    public void foundGroup() {
        String str = "";
        int i = 0;
        if (i < this.selectContacts.size()) {
            if (str.equals("")) {}
            for (str = ((ClanFindApprovaEntity)this.selectContacts.get(i)).getCustomer_id();; str = str + "," + ((ClanFindApprovaEntity)this.selectContacts.get(i)).getCustomer_id())
            {
                i += 1;
                break;
            }
        }
        Intent localIntent = new Intent();
        localIntent.putExtra("memberArray", str);
        localIntent.putExtra("memberCnt", this.selectContacts.size() + "");
        setResult(-1, localIntent);
        finish();
    }

    public void initView() {
        this.contactSearch = ((CleanableEditText)findViewById(R.id.et_contact_search));
        this.release.setVisibility(View.VISIBLE);
        this.release.setText("确定");
        this.dialog = LoadingDialogFragment.newInstance("正在请求");
        this.contactSearch.setOnEditorActionListener(this);
        this.pinyinComparator = new PinyinComparator();
        this.characterParser = CharacterParser.getInstance();
        this.contactAdapter = new ClanMemberchargeSelAdapter();
        this.mAdapter = new ClanMemberChargeAdapter(this.listView, this.contacts, R.layout.item_manager_member);
        this.listView.setAdapter(this.mAdapter);
        LinearLayoutManager localLinearLayoutManager = new LinearLayoutManager(this);
        localLinearLayoutManager.setOrientation(0);
        this.recyclerView.setLayoutManager(localLinearLayoutManager);
        this.recyclerView.setAdapter(this.contactAdapter);
        this.sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener()
        {
            public void onTouchingLetterChanged(String paramAnonymousString)
            {
                int i = ClanMemberChargeSelActivity.this.mAdapter.getPositionForSection(paramAnonymousString.charAt(0));
                if (i != -1) {
                    ClanMemberChargeSelActivity.this.listView.setSelection(i);
                }
            }
        });
        getFriendsList();
        this.contactSearch.setRegister(this);
    }

    public void isSelectContact(ClanFindApprovaEntity item) {
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
            release.setText("确定("+selectContacts.size()+")");
            contact_un_choose.setVisibility(View.GONE);
        }else {
            release.setText("确定");
            contact_un_choose.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.txt_titlebar_release, R.id.surname_imgBtn_search})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.surname_imgBtn_search:
                postSearch();
                break;
            case R.id.txt_titlebar_release:
                if (this.selectContacts.size() == 0)
                {
                    ViewInject.shortToast(this, "请选择好友");
                    return;
                }
                foundGroup();
                break;
        }
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        getWindow().setSoftInputMode(32);
        setContentView(R.layout.activity_clan_member_charge_sel);
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
            postSearch();
            Utils.hideSystemKeyBoard(this, this.contactSearch);
            return true;
        }
        return false;
    }

    public void postSearch()
    {
        if (this.contactSearch == null) {
            return;
        }
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        while (i < this.contacts.size())
        {
            if ((((ClanFindApprovaEntity)this.contacts.get(i)).getSurname() + ((ClanFindApprovaEntity)this.contacts.get(i)).getName()).contains(this.contactSearch.getText().toString())) {
                localArrayList.add(this.contacts.get(i));
            }
            i += 1;
        }
        this.mAdapter.refresh(localArrayList);
    }

    private class PinyinComparator
            implements Comparator<ClanFindApprovaEntity>
    {
        private PinyinComparator() {}

        public int compare(ClanFindApprovaEntity paramClanFindApprovaEntity1, ClanFindApprovaEntity paramClanFindApprovaEntity2)
        {
            if (paramClanFindApprovaEntity2.getSortLetters().equals("#")) {
                return -1;
            }
            if (paramClanFindApprovaEntity1.getSortLetters().equals("#")) {
                return 1;
            }
            return paramClanFindApprovaEntity1.getSortLetters().compareTo(paramClanFindApprovaEntity2.getSortLetters());
        }
    }
}
