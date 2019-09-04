package com.yuejian.meet.activities.search;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.clan.ClanInfoActivity;
import com.yuejian.meet.activities.home.ArticleInfoActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.activities.zuci.ZuciInfoActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.bean.GroupSeedEntity;
import com.yuejian.meet.bean.MainSearchEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.bean.MoreClanEntity;
import com.yuejian.meet.bean.ZuciEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.InnerListView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainSearchActivity extends BaseActivity implements TextView.OnEditorActionListener, CleanableEditText.ContentClear {
    CleanableEditText et_search;
    private InnerListView search_member_list;
    private InnerListView search_group_lsit;
    private InnerListView search_article_list;
    private InnerListView search_zhongqinhu_list;
    private InnerListView search_zuci_list;
    @Bind(R.id.layout_search_title)
    LinearLayout layout_search_title;
    @Bind(R.id.layout_search_member)
    LinearLayout layout_search_member;
    @Bind(R.id.layout_search_group)
    LinearLayout layout_search_group;
    @Bind(R.id.layout_search_article)
    LinearLayout layout_search_article;
    @Bind(R.id.layout_search_zhongqinhu)
    LinearLayout layout_search_zhongqinhu;
    @Bind(R.id.layout_search_zuci)
    LinearLayout layout_search_zuci;
    @Bind(R.id.search_back)
    View back;
    List<MembersEntity> membersList;
    List<ZuciEntity> zuciList;
    List<MoreClanEntity> clanList;
    List<Article> articleList;
    List<GroupSeedEntity> groupList;
    MemberAdapter memberAdapter;
    ZuciAdapter zuciAdapter;
    ClanAdapter clanAdapter;
    ArticleAdapter articleAdapter;
    GroupAdapter groupAdapter;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        initView();
    }

    public void initView() {
        et_search = (CleanableEditText) findViewById(R.id.et_search_all);
        et_search.setRegister(this);
        et_search.setOnEditorActionListener(this);
        search_zuci_list = (InnerListView) findViewById(R.id.search_zuci_list);
        search_zhongqinhu_list = (InnerListView) findViewById(R.id.search_zhongqinhu_list);
        search_article_list = (InnerListView) findViewById(R.id.search_article_list);
        search_group_lsit = (InnerListView) findViewById(R.id.search_group_lsit);
        search_member_list = (InnerListView) findViewById(R.id.search_member_list);
        memberAdapter = new MemberAdapter(search_member_list, membersList, R.layout.item_search_clan_layout);
        search_member_list.setAdapter(memberAdapter);
        clanAdapter = new ClanAdapter(search_zhongqinhu_list, clanList, R.layout.item_search_zongqinhui_layout);
        search_zhongqinhu_list.setAdapter(clanAdapter);
        zuciAdapter = new ZuciAdapter(search_zuci_list, zuciList, R.layout.item_search_zuci_layout);
        search_zuci_list.setAdapter(zuciAdapter);
        articleAdapter = new ArticleAdapter(search_article_list, articleList, R.layout.item_search_article_layout);
        search_article_list.setAdapter(articleAdapter);
        groupAdapter = new GroupAdapter(search_group_lsit, groupList, R.layout.item_search_chat_group_layout);
        search_group_lsit.setAdapter(groupAdapter);
        hideView();
    }

    public void hideView() {
        layout_search_member.setVisibility(View.GONE);
        layout_search_zuci.setVisibility(View.GONE);
        layout_search_zhongqinhu.setVisibility(View.GONE);
        layout_search_article.setVisibility(View.GONE);
        layout_search_group.setVisibility(View.GONE);
        layout_search_title.setVisibility(View.VISIBLE);
    }

    public void loadData(String paramString) {
        if (StringUtil.isEmpty(paramString)) {
            hideView();
            return;
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("key_words", paramString);
        localHashMap.put("longitude", AppConfig.slongitude);
        localHashMap.put("latitude", AppConfig.slatitude);
//        localHashMap.put("type", "" + this.type);
        localHashMap.put("pageIndex", "1");
        localHashMap.put("pageItemCount", "3");
        this.apiImp.getSerch(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                MainSearchEntity mainSearchEntity = (MainSearchEntity) JSON.parseObject(data, MainSearchEntity.class);
                membersList = JSON.parseArray(mainSearchEntity.getCustomers(), MembersEntity.class);
                zuciList = JSON.parseArray(mainSearchEntity.getClan_halls(), ZuciEntity.class);
                clanList = JSON.parseArray(mainSearchEntity.getClan_associations(), MoreClanEntity.class);
                articleList = JSON.parseArray(mainSearchEntity.getArticles(), Article.class);
                groupList = JSON.parseArray(mainSearchEntity.getChat_groups(), GroupSeedEntity.class);
                layout_search_member.setVisibility(View.GONE);
                layout_search_zuci.setVisibility(View.GONE);
                layout_search_zhongqinhu.setVisibility(View.GONE);
                layout_search_article.setVisibility(View.GONE);
                layout_search_group.setVisibility(View.GONE);

                if (membersList.size() > 0) {
                    layout_search_member.setVisibility(View.VISIBLE);
                    memberAdapter.refresh(membersList);
                }
                if (zuciList.size() > 0) {
                    layout_search_zuci.setVisibility(View.VISIBLE);
                    zuciAdapter.refresh(zuciList);
                }
                if (clanList.size() > 0) {
                    layout_search_zhongqinhu.setVisibility(View.VISIBLE);
                    clanAdapter.refresh(clanList);
                }
                if (articleList.size() > 0) {
                    layout_search_article.setVisibility(View.VISIBLE);
                    articleAdapter.refresh(articleList);
                }
                if (groupList.size() > 0) {
                    layout_search_group.setVisibility(View.VISIBLE);
                    groupAdapter.refresh(groupList);
                }
                if (membersList.size() == 0 && zuciList.size() == 0 && clanList.size() == 0 && articleList.size() == 0 && groupList.size() == 0) {
                    layout_search_title.setVisibility(View.VISIBLE);
                } else {
                    layout_search_title.setVisibility(View.GONE);
                }

            }
        });
    }

    @OnClick({R.id.more_member, R.id.more_group, R.id.more_article, R.id.more_zhongqinhu, R.id.more_zuci, R.id.search_all,
            R.id.txt_member, R.id.txt_zuci, R.id.txt_zqh, R.id.txt_group, R.id.txt_article, R.id.search_back})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.more_member:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 1);
                this.intent.putExtra("content", et_search.getText().toString());
                startActivity(this.intent);
                break;
            case R.id.more_group:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 4);
                this.intent.putExtra("content", et_search.getText().toString());
                startActivity(this.intent);
                break;
            case R.id.more_article:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 5);
                this.intent.putExtra("content", et_search.getText().toString());
                startActivity(this.intent);
                break;
            case R.id.more_zhongqinhu:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 3);
                this.intent.putExtra("content", et_search.getText().toString());
                startActivity(this.intent);
                break;
            case R.id.more_zuci:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 2);
                this.intent.putExtra("content", et_search.getText().toString());
                startActivity(this.intent);
                break;
            case R.id.search_all:
                loadData(et_search.getText().toString());
                break;
            case R.id.txt_member:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 1);
                this.intent.putExtra("content", et_search.getText().toString());
                startActivity(this.intent);
                break;
            case R.id.txt_zuci:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 2);
                this.intent.putExtra("content", et_search.getText().toString());
                startActivity(this.intent);
                break;
            case R.id.txt_zqh:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 3);
                this.intent.putExtra("content", et_search.getText().toString());
                startActivity(this.intent);
                break;
            case R.id.txt_group:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 4);
                this.intent.putExtra("content", et_search.getText().toString());
                startActivity(this.intent);
                break;
            case R.id.txt_article:
                this.intent = new Intent(this, MoreSearchActivity.class);
                this.intent.putExtra("type", 5);
                this.intent.putExtra("content", et_search.getText().toString());
                startActivity(this.intent);
                break;

            case R.id.search_back:
                this.finish();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            loadData(v.getText().toString());
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

    @Override
    public void ClearText() {
        loadData(et_search.getText().toString());
    }

    public class MemberAdapter extends FKAdapter<MembersEntity> {

        public MemberAdapter(AbsListView view, List<MembersEntity> mDatas, int itemLayoutId) {
            super(view, mDatas, itemLayoutId);
        }

        public void convert(AdapterHolder helper, final MembersEntity item, boolean isScrolling, int position) {
            convert(helper, getItem(position), isScrolling);
            helper.setText(R.id.search_clan_name, item.getSurname() + item.getName());
            helper.setText(R.id.txt_search_clan_age, item.getAge());
            helper.getView(R.id.txt_search_clan_age).setSelected(item.getSex().equals("1") ? true : false);
            Glide.with(mContext).load(item.getPhoto()).into((ImageView) helper.getView(R.id.search_clan_picture));
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCxt, WebActivity.class);
                    intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + item.customer_id);
                    mCxt.startActivity(intent);
                }
            });
        }
    }

    public class ZuciAdapter extends FKAdapter<ZuciEntity> {

        public ZuciAdapter(AbsListView view, List<ZuciEntity> mDatas, int itemLayoutId) {
            super(view, mDatas, itemLayoutId);
        }

        public void convert(AdapterHolder helper, final ZuciEntity item, boolean isScrolling, int position) {
            convert(helper, getItem(position), isScrolling);
            helper.setText(R.id.search_zuci_name, item.getName());
            helper.setText(R.id.search_zuci_location, item.getProvince() + item.city + item.getArea());
            Glide.with(mContext).load(item.getFirst_figure()).into((ImageView) helper.getView(R.id.search_zuci_photo));
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(mContext, ZuciInfoActivity.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("zuciName", item.getName());
                    mCxt.startActivity(intent);
                }
            });
        }
    }

    public class ClanAdapter extends FKAdapter<MoreClanEntity> {

        public ClanAdapter(AbsListView view, List<MoreClanEntity> mDatas, int itemLayoutId) {
            super(view, mDatas, itemLayoutId);
        }

        public void convert(AdapterHolder helper, final MoreClanEntity item, final boolean isScrolling, int position) {
            convert(helper, getItem(position), isScrolling);
            helper.setText(R.id.search_clan_name, item.getAssociation_name());
            helper.setText(R.id.clan_km, item.getDistance());
            helper.getView(R.id.clan_km).setVisibility(item.getDistance().equals("-1") ? View.GONE : View.VISIBLE);
            helper.setText(R.id.clan_ren_cont, item.getAssociation_cnt() + "人");
            Glide.with(mContext).load(item.getAssociation_img()).into((ImageView) helper.getView(R.id.search_clan_picture));
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(mContext, ClanInfoActivity.class);
                    intent.putExtra("clanEntity", item);
                    mCxt.startActivity(intent);
                }
            });
        }
    }

    public class ArticleAdapter extends FKAdapter<Article> {

        public ArticleAdapter(AbsListView view, List<Article> mDatas, int itemLayoutId) {
            super(view, mDatas, itemLayoutId);
        }

        public void convert(AdapterHolder helper, final Article item, boolean isScrolling, int position) {
            convert(helper, getItem(position), isScrolling);
            helper.setText(R.id.search_article_name, item.getArticle_title());
            Glide.with(mContext).load(item.getArticle_photo()).into((ImageView) helper.getView(R.id.search_article_photo));
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent localIntent = new Intent(mContext, ArticleInfoActivity.class);
                    localIntent.putExtra("article", item);
                    mCxt.startActivity(localIntent);
                }
            });
        }
    }

    public class GroupAdapter extends FKAdapter<GroupSeedEntity> {

        public GroupAdapter(AbsListView view, List<GroupSeedEntity> mDatas, int itemLayoutId) {
            super(view, mDatas, itemLayoutId);
        }

        public void convert(AdapterHolder helper, final GroupSeedEntity item, boolean isScrolling, int position) {
            convert(helper, getItem(position), isScrolling);
            helper.setText(R.id.search_group_name, item.getGroup_name());
            Glide.with(mContext).load(item.getGroup_photo()).into((ImageView) helper.getView(R.id.search_group_photo));
            helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImUtils.toTeamSession(mContext, item.getGroup_id(), "0");
                }
            });
        }
    }
}
