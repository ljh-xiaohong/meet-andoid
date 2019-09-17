package com.yuejian.meet.framents.family;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.ArticleDetailsActivity;
import com.yuejian.meet.activities.creation.VideoDetailsActivity;
import com.yuejian.meet.adapters.FamilyCircleRecommendListAdapter;
import com.yuejian.meet.adapters.FamilyCircleSameCityListAdapter;
import com.yuejian.meet.adapters.LifeAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.FamilySameCityEntity;
import com.yuejian.meet.bean.LifeEntity;
import com.yuejian.meet.bean.PositionInfo;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.ui.SpacesItemDecoration;
import com.yuejian.meet.utils.TextUtil;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author : g000gle
 * @time : 2019/5/23 09:50
 * @desc : 首页 - 家圈 - 生活Fragment
 */
public class FamilyCircleSameCityFragment extends BaseFragment
        implements SpringView.OnFreshListener, FamilyCircleSameCityListAdapter.OnListItemClickListener {

    @Bind(R.id.rv_family_circle_same_city_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.spring_family_same_city_list)
    SpringView mSpringView;
    @Bind(R.id.ll_family_same_city_list_empty)
    LinearLayout mEmptyList;

    private int mNextPageIndex = 1;
    private int pageCount = 10;
    private FamilyCircleSameCityListAdapter mSameCityListAdapter;
    private LifeAdapter adapter;

    private String province = "",
            city = "";

    private boolean firstLoad = true;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_family_circle_same_city, container, false);
    }

    @Override
    protected void initData() {
        super.initData();

        mSpringView.setFooter(new DefaultFooter(getContext()));
        mSpringView.setHeader(new DefaultHeader(getContext()));
        mSpringView.setListener(this);

//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        mSameCityListAdapter = new FamilyCircleSameCityListAdapter(getActivity(), this);
//        SpacesItemDecoration decoration = new SpacesItemDecoration(20);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.addItemDecoration(decoration);
//        mRecyclerView.setAdapter(mSameCityListAdapter);
        adapter = new LifeAdapter(mRecyclerView, getContext());

        loadDataFromNet(mNextPageIndex, pageCount);
//        initDatas("0", mNextPageIndex, pageCount, city);

    }

    private void initDatas(String type, int page, int count, String City) {
        if (TextUtils.isEmpty(City)) {
            getPosition(new DataIdCallback<PositionInfo>() {
                @Override
                public void onSuccess(PositionInfo data, int id) {
                    province = data.getProvince();
                    city = data.getCity();
//                    loadDataFromNet(type, page, count, city);
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }
            });
        }
    }

    private void loadDataFromNet(int page, int count) {
        Map<String, Object> map = new HashMap<>();
        map.put("longitude", AppConfig.slongitude);
        map.put("latitude", AppConfig.slatitude);
        map.put("pageIndex", page);
        map.put("pageItemCount", count);
        map.put("customerId", AppConfig.CustomerId);
        apiImp.getIcityFamilyCricleDo(map, this, new DataIdCallback<String>() {
            List<LifeEntity> lifeEntities;

            @Override
            public void onSuccess(String data, int id) {

                if (data != null) {
                    JSONObject jo = (JSONObject) JSON.parse(data);
                    String code = jo.getString("code");
                    if (code != null && code.equalsIgnoreCase("0")) {
                        lifeEntities = JSON.parseArray(jo.getString("data"), LifeEntity.class);
                        if (lifeEntities.size() > 0 && firstLoad) {
                            mEmptyList.setVisibility(View.GONE);
                            firstLoad = false;
                        }

                        if (page <= 1) {
                            //上拉最新
                            adapter.refresh(lifeEntities);

                        } else {
                            //下拉更多
                            adapter.Loadmore(lifeEntities);
                        }
                    }

                }
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }


            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {


            }
        });

    }

    private void loadDataFromNet(String type, int page, int count, String city) {

        Map<String, Object> map = new HashMap<>();
        map.put("customer_id", user.customer_id);
        map.put("city_name", city);
        map.put("pageIndex", String.valueOf(page));
        map.put("pageItemCount", String.valueOf(count));
        apiImp.getIcityFamilyCricleDo(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<FamilySameCityEntity> sameCityEntities = JSON.parseArray(data, FamilySameCityEntity.class);
                if (sameCityEntities.size() > 0 && firstLoad) {
                    mEmptyList.setVisibility(View.GONE);
                    firstLoad = false;
                }

                if (page <= 1) {
                    mSameCityListAdapter.refresh(sameCityEntities);
                } else {
                    mSameCityListAdapter.Loadmore(sameCityEntities);
                }

//                if (sameCityEntities.size() > 0) {
//                    mEmptyList.setVisibility(View.GONE);
//                } else {
//                    mEmptyList.setVisibility(View.VISIBLE);
//                }


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
        mNextPageIndex = 1;
        loadDataFromNet(mNextPageIndex, pageCount);
    }

    @Override
    public void onLoadmore() {
        loadDataFromNet(++mNextPageIndex, pageCount);
    }

    @Override
    public void onItemClick(int type, int id) {
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
}
