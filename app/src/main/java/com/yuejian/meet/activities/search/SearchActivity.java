package com.yuejian.meet.activities.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.HotBean;
import com.yuejian.meet.bean.HotSeacher;
import com.yuejian.meet.framents.family.ArticleFragment;
import com.yuejian.meet.framents.family.CommodityFragment;
import com.yuejian.meet.framents.family.FriendFragment;
import com.yuejian.meet.framents.family.ProjectFragment;
import com.yuejian.meet.framents.family.VideoFragment;
import com.yuejian.meet.utils.ClearEditText;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.widgets.SearchTitleView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements TextView.OnEditorActionListener, ViewPager.OnPageChangeListener, SearchTitleView.OnTitleViewClickListener {

    @Bind(com.yuejian.meet.R.id.search_all)
    ImageView searchAll;
    @Bind(com.yuejian.meet.R.id.et_search_all)
    ClearEditText etSearchAll;
    @Bind(com.yuejian.meet.R.id.hot_flowlayout)
    TagFlowLayout hotFlowlayout;
    @Bind(com.yuejian.meet.R.id.clear_history)
    ImageView clearHistory;
    @Bind(com.yuejian.meet.R.id.history_flowlayout)
    TagFlowLayout historyFlowlayout;
    @Bind(com.yuejian.meet.R.id.tag_lay)
    LinearLayout tagLay;
    @Bind(com.yuejian.meet.R.id.cencel)
    TextView cencel;
    @Bind(com.yuejian.meet.R.id.family_circle_title_view)
    SearchTitleView familyCircleTitleView;
    @Bind(com.yuejian.meet.R.id.vp_family_circle_content)
    ViewPager vpFamilyCircleContent;
    @Bind(com.yuejian.meet.R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(com.yuejian.meet.R.id.search_list)
    LinearLayout searchList;
    private TagAdapter<HotSeacher> mAdapter;
    private TagAdapter<HotSeacher> mAdapter1;
    private List<HotSeacher> hotData = new ArrayList<>();
    private List<HotSeacher> historyData = new ArrayList<>();
    private int positions=0;
    private boolean isFirst=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.yuejian.meet.R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        initHotData();
    }

    private void initHotData() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", "3");
        apiImp.getKeywordOrlabelByType(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                HotBean hotBean=new Gson().fromJson(data,HotBean.class);
                hotData.addAll(hotBean.getData());
                mAdapter.notifyDataChanged();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private FriendFragment mFriendFragment;
    private VideoFragment mVideoFragment;
    private ArticleFragment mArticleFragment;
    private CommodityFragment mCommodityFragment;
    private ProjectFragment mProjectFragment;

    public void initView() {
        ArrayList<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(mFriendFragment = new FriendFragment());
        mFragmentList.add(mVideoFragment = new VideoFragment());
        mFragmentList.add(mArticleFragment = new ArticleFragment());
        mFragmentList.add(mCommodityFragment = new CommodityFragment());
        mFragmentList.add(mProjectFragment = new ProjectFragment());
        setCurrentItem(0);
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this.getSupportFragmentManager(), mFragmentList);
        vpFamilyCircleContent.setAdapter(adapter);
        vpFamilyCircleContent.setOffscreenPageLimit(1);
        vpFamilyCircleContent.addOnPageChangeListener(this);
        familyCircleTitleView.setOnTitleViewClickListener(this);
//        HotSeacher hotSeacher = new HotSeacher();
//        hotSeacher.setTitle("华夏宗亲大会");
//        hotData.add(hotSeacher);
//        HotSeacher hotSeacher1 = new HotSeacher();
//        hotSeacher1.setTitle("宗亲大会");
//        hotData.add(hotSeacher1);
//        HotSeacher hotSeacher2 = new HotSeacher();
//        hotSeacher2.setTitle("姓氏大全");
//        hotData.add(hotSeacher2);
//        HotSeacher hotSeacher3 = new HotSeacher();
//        hotSeacher3.setTitle("# 姓氏文化知多少");
//        hotData.add(hotSeacher3);
//        HotSeacher hotSeacher4 = new HotSeacher();
//        hotSeacher4.setTitle("# 秀秀我的宗亲祠堂");
//        hotData.add(hotSeacher4);
        historyData.addAll(DadanPreference.getInstance(this).getDataList("historyData", HotSeacher.class));

        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyData.clear();
                DadanPreference.getInstance(SearchActivity.this).setDataList("historyData", historyData);
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
                        TextView tv = view.findViewById(com.yuejian.meet.R.id.tv);
                        tv.setText(s.getTitle());
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
                etSearchAll.setText(hotData.get(position).getTitle());
                etSearchAll.setSelection(hotData.get(position).getTitle().length());
                searching(hotData.get(position));
                return true;
            }
        });
        historyFlowlayout.setAdapter(mAdapter1 = new TagAdapter<HotSeacher>(historyData) {
                    @Override
                    public View getView(FlowLayout parent, int position, HotSeacher s) {
                        View view = LayoutInflater.from(SearchActivity.this).inflate(com.yuejian.meet.R.layout.hotflowlayout_tv,
                                historyFlowlayout, false);
                        TextView tv = view.findViewById(com.yuejian.meet.R.id.tv);
                        tv.setText(s.getTitle());
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
                etSearchAll.setText(historyData.get(position).getTitle());
                etSearchAll.setSelection(historyData.get(position).getTitle().length());
                searching(historyData.get(position));
                return true;
            }
        });
        etSearchAll.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if (CommonUtil.isNull(s.toString())){
                   showView();
               }
            }
        });
        cencel.setOnClickListener(v -> finish());
        etSearchAll.setOnEditorActionListener(this);
    }
    private void searching(HotSeacher keyword) {
        isFirst=false;
        mCurrentSearch=keyword;
        hideView();
        if (CommonUtil.isNull(etSearchAll.getText().toString())){
            return;
        }
        if(positions==0){
            mFriendFragment.update(etSearchAll.getText().toString());
        }else  if(positions==1){
            mVideoFragment.update(etSearchAll.getText().toString());
        }else if(positions==2){
            mArticleFragment.update(etSearchAll.getText().toString());
        }else if(positions==3){
            mCommodityFragment.update(etSearchAll.getText().toString());
        }else{
            mProjectFragment.update(etSearchAll.getText().toString());
        }

        for (int i = 0; i < historyData.size(); i++) {
            if (historyData.get(i).getTitle().equals(keyword.getTitle())) {
                historyData.remove(i);
            }
        }
        if (!CommonUtil.isNull(keyword.getTitle())) {
            HotSeacher hotSeacher = new HotSeacher();
            hotSeacher.setCompany(keyword.isCompany());
            hotSeacher.setTitle(keyword.getTitle());
            historyData.add(0, hotSeacher);
            DadanPreference.getInstance(this).setDataList("historyData", historyData);
            if (mAdapter1 != null) {
                mAdapter1.notifyDataChanged();
            }
        }
    }
    /**
     * 切换页面
     *
     * @param position 分类角标
     */
    private void setCurrentItem(int position) {
        if (!isFirst){
            searching(mCurrentSearch);
        }
        vpFamilyCircleContent.setCurrentItem(position);
        familyCircleTitleView.setSelectedTitle(position);
        if (position == 1) {
            //打开手势滑动
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            //禁止手势滑动
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void hideView() {
        tagLay.setVisibility(View.GONE);
        searchList.setVisibility(View.VISIBLE);
    }
    public void showView() {
        tagLay.setVisibility(View.VISIBLE);
        searchList.setVisibility(View.GONE);
    }
    private  HotSeacher mCurrentSearch;
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            HotSeacher hotSeacher = new HotSeacher();
            hotSeacher.setCompany(false);
            hotSeacher.setTitle(v.getText().toString());
            searching(hotSeacher);
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
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                positions=0;
                setCurrentItem(0);
                break;
            case 1:
                positions=1;
                setCurrentItem(1);
                break;
            case 2:
                positions=2;
                setCurrentItem(2);
                break;
            case 3:
                positions=3;
                setCurrentItem(3);
                break;
            case 4:
                positions=4;
                setCurrentItem(4);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onTitleViewClick(int position) {
        switch (position) {
            case 0:
                positions=0;
                setCurrentItem(0);
                break;
            case 1:
                positions=1;
                setCurrentItem(1);
                break;
            case 2:
                positions=2;
                setCurrentItem(2);
                break;
            case 3:
                positions=3;
                setCurrentItem(3);
                break;
            case 4:
                positions=4;
                setCurrentItem(4);
                break;
            default:
                break;
        }
    }
}
