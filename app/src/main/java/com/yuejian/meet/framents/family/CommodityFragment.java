package com.yuejian.meet.framents.family;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.ArticleDetailsActivity;
import com.yuejian.meet.activities.creation.VideoDetailsActivity;
import com.yuejian.meet.activities.find.ScannerActivity;
import com.yuejian.meet.activities.home.ReleaseActivity;
import com.yuejian.meet.activities.search.SearchActivity;
import com.yuejian.meet.adapters.FamilyCircleFollowListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.FamilyFollowEntity;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.ui.SingleLineItemDecoration;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author : g000gle
 * @time : 2019/5/23 09:42
 * @desc : 搜索 商品
 */
public class CommodityFragment extends BaseFragment
        implements SpringView.OnFreshListener, FamilyCircleFollowListAdapter.OnFollowListItemClickListener {

    @Bind(R.id.rv_family_circle_follow_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.spring_family_follow_list)
    SpringView mSpringView;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout mEmptyList;
    @Bind(R.id.search_all)
    ImageView searchAll;
    @Bind(R.id.sweep_code)
    LinearLayout sweepCode;
    @Bind(R.id.et_search_all)
    TextView etSearchAll;
    @Bind(R.id.btn_release)
    RelativeLayout btnRelease;

    private FamilyCircleFollowListAdapter mFollowListAdapter;

    private int mNextPageIndex = 1;
    private int pageCount = 20;
    private boolean firstLoad = true;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_family_circle_follow, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFollowListAdapter = new FamilyCircleFollowListAdapter(getActivity(), this, apiImp, getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mFollowListAdapter);
        mFollowListAdapter.setOnClickListener(new FamilyCircleFollowListAdapter.onClickListener() {
            @Override
            public void onClick(int position, boolean me) {
//                initPopwindow(position);
            }

            @Override
            public void onComment(int position) {

            }
        });
        mRecyclerView.addItemDecoration(new SingleLineItemDecoration(20));

        mSpringView.setFooter(new DefaultFooter(getContext()));
        mSpringView.setHeader(new DefaultHeader(getContext()));
        mSpringView.setListener(this);
        mSpringView.callFresh();
    }


    //加载数据
    List<FamilyFollowEntity.DataBean> followEntities =new ArrayList<>();
    FamilyFollowEntity followEntitie;
    private void loadDataFromNet(String type, int page, int count) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("pageIndex", String.valueOf(page));
        map.put("pageItemCount", String.valueOf(count));
        apiImp.getAttentionFamilyCricleDo(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                 followEntitie=new Gson().fromJson(data,FamilyFollowEntity.class);
                 if (followEntitie.getCode()!=0) {
                     ViewInject.shortToast(getActivity(),followEntitie.getMessage());
                     return;
                 }
                followEntities.addAll(followEntitie.getData());
                if (followEntities.size() > 0 && firstLoad) {
                    mEmptyList.setVisibility(View.GONE);
                    firstLoad = false;
                }
                if (page <= 1) {
                    //上拉最新
                    mFollowListAdapter.refresh(followEntities);
                } else {
                    //下拉更多
                    if (followEntitie.getData().size()!=pageCount){
                        ViewInject.shortToast(getActivity(),"已经是最后一页");
                    }else {
                        mFollowListAdapter.Loadmore(followEntities);
                    }
                }
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        followEntities.clear();
        mNextPageIndex = 0;
        loadDataFromNet("0", mNextPageIndex, pageCount);
    }

    @Override
    public void onLoadmore() {
        loadDataFromNet("0", ++mNextPageIndex, pageCount);
    }

    @Override
    public void onListItemClick(int type, int id) {
        //类型：1-随笔，2-文章，3-相册，4-视频
        Intent intent = null;
        if (type == 4) {
            intent = new Intent(getActivity(), VideoDetailsActivity.class);
        } else {
            intent = new Intent(getActivity(), ArticleDetailsActivity.class);
        }
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.search_all, R.id.sweep_code, R.id.et_search_all, R.id.btn_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_release:
                getActivity().startActivityForResult(new Intent(getActivity(), ReleaseActivity.class),1);
                break;
            case R.id.search_all:
            case R.id.et_search_all:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.sweep_code:
                getActivity().startActivity(new Intent(getActivity(), ScannerActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==7){
            followEntities.clear();
            mNextPageIndex = 0;
            loadDataFromNet("0", mNextPageIndex, pageCount);
        }
    }
}
