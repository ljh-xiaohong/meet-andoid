package com.yuejian.meet.activities.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.MoreSearcAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.MainSearchEntity;
import com.yuejian.meet.bean.MoreSearchEntity;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MoreSearchActivity extends BaseActivity implements OnEditorActionListener, CleanableEditText.ContentClear, SpringView.OnFreshListener {
    String content;
    CleanableEditText et_search_all;
    List<MoreSearchEntity> listDate = new ArrayList();
    MoreSearcAdapter mAdapter;
    RecyclerView more_recycler;
    int pageIndex = 1;
    @Bind(R.id.search_sv)
    SpringView search_sv;
    int type;
    String hintName;
    public String bindingId;
    public Boolean isBanin = false;
    Intent intent;

    private void hintKbTwo() {
        @SuppressLint("WrongConstant") InputMethodManager localInputMethodManager = (InputMethodManager) getSystemService("input_method");
        if ((localInputMethodManager.isActive()) && (getCurrentFocus() != null) && (getCurrentFocus().getWindowToken() != null)) {
            localInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 2);
        }
    }

    public void ClearText() {
        this.listDate.clear();
        this.mAdapter.refresh(this.listDate);
    }

    public void loadData(String paramString) {
        if (StringUtil.isEmpty(paramString)) {
            return;
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("key_words", paramString);
        localHashMap.put("longitude", AppConfig.slongitude);
        localHashMap.put("latitude", AppConfig.slatitude);
        localHashMap.put("type", "" + this.type);
        localHashMap.put("pageIndex", "" + this.pageIndex);
        localHashMap.put("pageItemCount", "20");
        this.apiImp.getSerch(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                MainSearchEntity mainSearchEntity = (MainSearchEntity) JSON.parseObject(data, MainSearchEntity.class);
                if (MoreSearchActivity.this.pageIndex == 1) {
                    MoreSearchActivity.this.listDate.clear();
                }
                MoreSearchActivity.this.listDate.addAll(JSON.parseArray(mainSearchEntity.getCustomers(), MoreSearchEntity.class));
                MoreSearchActivity.this.listDate.addAll(JSON.parseArray(mainSearchEntity.getClan_halls(), MoreSearchEntity.class));
                MoreSearchActivity.this.listDate.addAll(JSON.parseArray(mainSearchEntity.getArticles(), MoreSearchEntity.class));
                MoreSearchActivity.this.listDate.addAll(JSON.parseArray(mainSearchEntity.getChat_groups(), MoreSearchEntity.class));
                MoreSearchActivity.this.listDate.addAll(JSON.parseArray(mainSearchEntity.getClan_associations(), MoreSearchEntity.class));
//                if ((MoreSearchActivity.this.listDate.size() > 0) && (MoreSearchActivity.this.pageIndex == 1))
//                {
//                    paramAnonymousString = new MoreSearchEntity();
//                    paramAnonymousString.setType(1);
//                    MoreSearchActivity.this.listDate.add(0, paramAnonymousString);
//                }
                MoreSearchActivity.this.mAdapter.refresh(MoreSearchActivity.this.listDate);
            }
        });
    }

    @OnClick({R.id.search_all, R.id.search_back})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.search_all:
                loadData(this.et_search_all.getText().toString());
                break;
            case R.id.search_back:
                finish();
                break;
        }
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_more_search);
        this.search_sv.setFooter(new DefaultFooter(this));
        this.search_sv.setHeader(new DefaultHeader(this));
        this.search_sv.setListener(this);
        this.type = getIntent().getIntExtra("type", 1);
        this.content = getIntent().getStringExtra("content");
        this.et_search_all = ((CleanableEditText) findViewById(R.id.et_search_all));
        this.more_recycler = ((RecyclerView) findViewById(R.id.more_recycler));
        DividerItemDecoration decoration = new DividerItemDecoration(this, 1);

        intent = getIntent();
        if (intent.hasExtra("bindingId")) {
            bindingId = getIntent().getStringExtra("bindingId");
            isBanin = getIntent().getBooleanExtra("isBanin", false);
        }
//        decoration.setDrawable(ContextCompat.getDrawable(this, 2130837657));
        this.more_recycler.addItemDecoration(decoration);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        this.more_recycler.setLayoutManager(layoutManager);
        this.mAdapter = new MoreSearcAdapter(this, this.listDate, this.type);
        this.more_recycler.setAdapter(this.mAdapter);
        mAdapter.setZuciType(bindingId, isBanin);
        this.mAdapter.notifyDataSetChanged();
        this.et_search_all.setRegister(this);
        this.et_search_all.setOnEditorActionListener(this);
        CleanableEditText localCleanableEditText = this.et_search_all;

        this.et_search_all.setText(this.content);
        if (!StringUtil.isEmpty(this.content)) {
            loadData(this.content);
        }
        if (this.type == 1) {
            hintName = getString(R.string.clan_relatives);
        }else if (this.type == 2) {
            hintName = getString(R.string.Ancestral_shrine);
        } else if (this.type == 3) {
            hintName = getString(R.string.Clansmen_association);
        } else if (this.type == 4) {
            hintName = getString(R.string.group_chat);
        } else {
            hintName = getString(R.string.article);
        }
        localCleanableEditText.setHint(hintName);
    }

    public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent) {
        if ((paramInt == 4) || ((paramKeyEvent != null) && (paramKeyEvent.getKeyCode() == 66))) {
            loadData(paramTextView.getText().toString());
            hintKbTwo();
            return true;
        }
        return false;
    }

    public void onLoadmore() {
        this.pageIndex += 1;
        loadData(this.et_search_all.getText().toString());
    }

    public void onRefresh() {
        this.pageIndex = 1;
        loadData(this.et_search_all.getText().toString());
    }
}
