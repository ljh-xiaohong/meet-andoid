package com.yuejian.meet.activities.search;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.ArticleInfoActivity;
import com.yuejian.meet.adapters.SearchTagHorizontalListAdapter;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.bean.HotSeacher;
import com.yuejian.meet.bean.Reward;
import com.yuejian.meet.utils.ClearEditText;
import com.yuejian.meet.utils.MyLinearLayoutManager;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.rockerhieu.emojicon.EmojiconEditText;

public class SearchActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @Bind(R.id.search_all)
    ImageView searchAll;
    @Bind(R.id.et_search_all)
    ClearEditText etSearchAll;
    @Bind(R.id.hot_flowlayout)
    TagFlowLayout hotFlowlayout;
    @Bind(R.id.clear_history)
    ImageView clearHistory;
    @Bind(R.id.history_flowlayout)
    TagFlowLayout historyFlowlayout;
    @Bind(R.id.tag_lay)
    LinearLayout tagLay;
    @Bind(R.id.tag_horizontal_list)
    RecyclerView tagHorizontalList;
    @Bind(R.id.tag_content_list)
    RecyclerView tagContentList;
    private TagAdapter<HotSeacher> mAdapter;
    private TagAdapter<HotSeacher> mAdapter1;
    private List<HotSeacher> hotData = new ArrayList<>();
    private List<HotSeacher> historyData = new ArrayList<>();
    private SearchTagHorizontalListAdapter searchtagHorizontalListAdapter;
    private List<HotSeacher> mDatas= new ArrayList<>();;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        HotSeacher hotSeacher = new HotSeacher();
        hotSeacher.setKeyword("华夏宗亲大会");
        hotData.add(hotSeacher);
        hotData.add(hotSeacher);
        hotData.add(hotSeacher);
        hotData.add(hotSeacher);
        historyData.add(hotSeacher);
        historyData.add(hotSeacher);
        historyData.add(hotSeacher);
        mDatas.add(hotSeacher);
        mDatas.add(hotSeacher);
        hotSeacher.setCompany(true);
        mDatas.add(hotSeacher);
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyData.clear();
                if (mAdapter1 != null) {
                    mAdapter1.notifyDataChanged();
                }
            }
        });
        hotFlowlayout.setAdapter(mAdapter = new TagAdapter<HotSeacher>(hotData) {
                    @Override
                    public View getView(FlowLayout parent, int position, HotSeacher s) {
                        View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.hotflowlayout_tv,
                                hotFlowlayout, false);
                        TextView tv = view.findViewById(R.id.tv);
                        tv.setText(s.getKeyword());
                        return view;
                    }

                    @Override
                    public boolean setSelected(int position, HotSeacher groupListBean) {

                        return super.setSelected(position, groupListBean);
                    }
                }
        );
        hotFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                etSearchAll.setText(hotData.get(position).getKeyword());
                etSearchAll.setSelection(hotData.get(position).getKeyword().length());
//                searching(hotData.get(position));
                return true;
            }
        });
        historyFlowlayout.setAdapter(mAdapter1 = new TagAdapter<HotSeacher>(historyData) {
                    @Override
                    public View getView(FlowLayout parent, int position, HotSeacher s) {
                        View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.hotflowlayout_tv,
                                historyFlowlayout, false);
                        TextView tv = view.findViewById(R.id.tv);
                        tv.setText(s.getKeyword());
                        return view;
                    }

                    @Override
                    public boolean setSelected(int position, HotSeacher groupListBean) {

                        return super.setSelected(position, groupListBean);
                    }
                }
        );
        historyFlowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                etSearchAll.setText(historyData.get(position).getKeyword());
                etSearchAll.setSelection(historyData.get(position).getKeyword().length());
//                searching(historyData.get(position));
                return true;
            }
        });
        MyLinearLayoutManager ms = new MyLinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        tagHorizontalList.setLayoutManager(ms);
        searchtagHorizontalListAdapter = new SearchTagHorizontalListAdapter(this, mDatas);
        tagHorizontalList.setAdapter(searchtagHorizontalListAdapter);
        MyLinearLayoutManager mss = new MyLinearLayoutManager(this);
        mss.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        tagContentList.setLayoutManager(mss);
        searchtagHorizontalListAdapter = new SearchTagHorizontalListAdapter(this, mDatas);
        tagContentList.setAdapter(searchtagHorizontalListAdapter);
    }

    public void hideView() {
        tagLay.setVisibility(View.GONE);
    }

    public void loadData(String paramString) {
        if (StringUtil.isEmpty(paramString)) {
            hideView();
            return;
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

}
