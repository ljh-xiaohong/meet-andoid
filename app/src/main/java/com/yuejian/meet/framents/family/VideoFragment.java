package com.yuejian.meet.framents.family;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.family.VideoActivity;
import com.yuejian.meet.adapters.CreationAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.CreationEntity;
import com.yuejian.meet.bean.VideoAndArticleBean;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.CommonUtil;
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

/**
 * @author : g000gle
 * @time : 2019/5/23 09:42
 * @desc : 搜索 视频
 */
public class VideoFragment extends BaseFragment
        implements SpringView.OnFreshListener{


    @Bind(R.id.rv_family_circle_follow_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.spring_family_follow_list)
    SpringView mSpringView;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout mEmptyList;
    private CreationAdapter adapter;

    private int mNextPageIndex = 1;
    private int pageCount = 20;
    private boolean firstLoad = true;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        adapter = new CreationAdapter(mRecyclerView, getContext(), 2,false);
        adapter.setOnItemClickListener((view, position) -> {
                VideoActivity.startActivity(mContext, creationEntities.get(position).getContentId() + "", AppConfig.CustomerId, Integer.parseInt(creationEntities.get(position).getCoverSizeType()) == 0 ? true : false);
        });
        mSpringView.setFooter(new DefaultFooter(getContext()));
        mSpringView.setHeader(new DefaultHeader(getContext()));
        mSpringView.setListener(this);
//        mSpringView.callFresh();
    }


    //加载数据
    List<CreationEntity> creationEntities = new ArrayList<>();
    VideoAndArticleBean mVideoAndArticleBean;

    private void loadDataFromNet(String type, String title) {
        if (dialog != null&&!dialog.isShowing)
            dialog.show(getActivity().getFragmentManager(), "");
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("title", title);
        map.put("type", type);
        map.put("pageIndex", String.valueOf(mNextPageIndex));
        map.put("pageItemCount", String.valueOf(pageCount));
        apiImp.getDoSearch(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                isUdate=true;
                if (mEmptyList==null)return;
                if (dialog != null)
                    dialog.dismiss();
                mVideoAndArticleBean = new Gson().fromJson(data, VideoAndArticleBean.class);
                if (mVideoAndArticleBean.getCode() != 0) {
                    ViewInject.shortToast(getActivity(), mVideoAndArticleBean.getMessage());
                    return;
                }
                for (int i=0;i<mVideoAndArticleBean.getData().size();i++){
                    CreationEntity entity = new CreationEntity();
                    entity.setLabelId(mVideoAndArticleBean.getData().get(i).getLabelId());
                    entity.setLabelName(mVideoAndArticleBean.getData().get(i).getLabelName());
                    entity.setContentTitle(mVideoAndArticleBean.getData().get(i).getTitle());
                    entity.setPhotoAndVideoUrl(mVideoAndArticleBean.getData().get(i).getCoverUrl());
                    entity.setFabulousNum(mVideoAndArticleBean.getData().get(i).getFabulousNum());
                    entity.setContentId(mVideoAndArticleBean.getData().get(i).getId());
                    entity.setPraise(mVideoAndArticleBean.getData().get(i).isIsPraise());
                    entity.setCoverSizeType(mVideoAndArticleBean.getData().get(i).getCoverSizeType());
                    creationEntities.add(entity);
                }
                if (creationEntities.size() > 0) {
                    mEmptyList.setVisibility(View.GONE);
                } else {
                    mEmptyList.setVisibility(View.VISIBLE);
                }
                if (mNextPageIndex <= 1) {
                    adapter.refresh(creationEntities);
                } else {
                    //下拉更多
                    if (mVideoAndArticleBean.getData().size() != pageCount) {
                        ViewInject.shortToast(getActivity(), "已经是最后一页");
                    } else {
                        adapter.Loadmore(creationEntities);
                    }
                }
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (CommonUtil.isNull(title)) return;
        creationEntities.clear();
        mNextPageIndex = 1;
        loadDataFromNet("4", title);
    }

    @Override
    public void onLoadmore() {
        if (CommonUtil.isNull(title)) return;
        ++mNextPageIndex;
        loadDataFromNet("4", title);
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

    String title = "";
    private boolean isUdate=true;
    public void update(String titles) {
        if (isUdate) {
            title = titles;
            creationEntities.clear();
            mNextPageIndex = 1;
            loadDataFromNet("4", titles);
        }
        isUdate=false;

    }
}
