package com.yuejian.meet.framents.creation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.DraftActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.BaseAdapter;
import com.yuejian.meet.adapters.CreationAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.CreationEntity;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.widgets.RecommendView;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCreationListFragment extends BaseFragment implements SpringView.OnFreshListener {

    private SpringView springView;

    private RecyclerView recyclerView;

    private int pageIndex = 1;

    private int pageItemCount = 10;

    private int type = -1;

    private List<CreationEntity> creationEntities;

    private CreationAdapter adapter;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_poster_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        springView = view.findViewById(R.id.fragment_poster_springview);
        recyclerView = view.findViewById(R.id.fragment_poster_recyclerview);
        springView.setListener(this);
        if (!getData()) return;
        adapter = new CreationAdapter(recyclerView, getContext(), type);
        setListener();
        getDataFromNet();
    }

    public static MyCreationListFragment newInstance(int type) {
        MyCreationListFragment fragment = new MyCreationListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    private boolean getData() {
        type = getArguments().getInt("type");
        return type != -1;
    }

    private void getDataFromNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("pageIndex", pageIndex);
        params.put("pageItemCount", pageItemCount);
        params.put("customerId", AppConfig.CustomerId);
        params.put("type", type);

        apiImp.getContentList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                JSONObject jo = JSON.parseObject(data);
                if (null == jo) return;
                if (!jo.getString("code").equals("0")) return;
                parseJson(jo.getString("data"));
                if (pageIndex == 1) {
                    setInit();
                    adapter.refresh(creationEntities);
                } else {
                    adapter.Loadmore(creationEntities);
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
            }
        });
    }

    /**
     * 第一个为草稿箱
     */
    private void setInit() {
        if (type == 1 || type == 2) {
            CreationEntity entity = new CreationEntity();
            entity.setDraftsId(type);
            if (creationEntities == null) creationEntities = new ArrayList<>();
            creationEntities.add(entity);
        }
    }

    private void parseJson(String data) {
        if (data == null || TextUtils.isEmpty(data)) return;
        JSONArray ja = JSON.parseArray(data);
        if (creationEntities == null) creationEntities = new ArrayList<>();
        for (int i = 0; i < ja.size(); i++) {
            JSONObject jo = (JSONObject) ja.get(i);
            CreationEntity entity = new CreationEntity();
            entity.setLabelId(jo.getString("labelId"));
            entity.setLabelName(jo.getString("labelName"));
            entity.setContentTitle(jo.getString("contentTitle"));
            entity.setContentId(jo.getInteger("contentId"));
            if (type == 3) {
                entity.setPreviewUrl(jo.getString("previewUrl"));
            } else {
                entity.setPhotoAndVideoUrl(jo.getString("photoAndVideoUrl"));
                entity.setFabulousNum(jo.getInteger("fabulousNum"));
                entity.setContent(jo.getString("content"));

            }
            creationEntities.add(entity);
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getDataFromNet();
    }

    @Override
    public void onLoadmore() {
        ++pageIndex;
        getDataFromNet();
    }

    private void setListener() {
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (type) {
                    //海报
                    case 3:
                        Intent intent = new Intent(mContext, WebActivity.class);
                        String hxmUrl = String.format("http://app2.yuejianchina.com/yuejian-app/canvas_haibao/personalPoset.html?customerId=%s&id=%s", AppConfig.CustomerId, creationEntities.get(position).getContentId());
                        intent.putExtra("url", hxmUrl);
                        intent.putExtra("No_Title", true);
                        startActivity(intent);
                        break;

                    case 1:
                    case 2:
                        if (position == 0) {
                            DraftActivity.startActivity(mContext, type);
                            return;
                        }
                        break;

                }
            }
        });
    }
}
