package com.yuejian.meet.framents.family;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.video.common.utils.FastClickUtil;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.family.ArticleActivity;
import com.yuejian.meet.activities.family.VideoVerticalActivity;
import com.yuejian.meet.activities.find.ScannerActivity;
import com.yuejian.meet.activities.home.ReleaseActivity;
import com.yuejian.meet.activities.search.SearchActivity;
import com.yuejian.meet.adapters.LifeAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.LifeEntity;
import com.yuejian.meet.framents.base.BaseFragment;
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

import static android.app.Activity.RESULT_OK;

/**
 * @author : g000gle
 * @time : 2019/5/23 09:50
 * @desc : 首页 - 家圈 - 生活Fragment
 */
public class FamilyCircleSameCityFragment extends BaseFragment
        implements SpringView.OnFreshListener {

    @Bind(R.id.rv_family_circle_same_city_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.spring_family_same_city_list)
    SpringView mSpringView;
    @Bind(R.id.ll_family_same_city_list_empty)
    LinearLayout mEmptyList;
    @Bind(R.id.btn_release)
    RelativeLayout btnRelease;
    @Bind(R.id.search_all)
    ImageView searchAll;
    @Bind(R.id.sweep_code)
    LinearLayout sweepCode;
    @Bind(R.id.et_search_all)
    TextView etSearchAll;

    private int mNextPageIndex = 1;
    private int pageCount = 10;
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
        mSpringView.callFresh();
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        mSameCityListAdapter = new FamilyCircleSameCityListAdapter(getActivity(), this);
//        SpacesItemDecoration decoration = new SpacesItemDecoration(20);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.addItemDecoration(decoration);
//        mRecyclerView.setAdapter(mSameCityListAdapter);
        adapter = new LifeAdapter(mRecyclerView, getContext());
        adapter.setOnItemClickListener((view, position) -> {
            if (FastClickUtil.isFastClick()) {
                return;
            }
            LifeEntity item = adapter.getData().get(position);
            switch (item.getType()) {
                //文章
                case 2:
                    ArticleActivity.startActivityForResult((Activity) mContext, item.getId() + "", AppConfig.CustomerId, position, CANCEL_DELECT_POSITION);
                    break;
                //视频
                case 4:
                    VideoVerticalActivity.startActivityForResult((Activity) mContext, item.getId() + "", AppConfig.CustomerId, CANCEL_NOTINTERET, item.getCoveSizeType() == 0 ? true : false);
                    break;
            }
        });
//        loadDataFromNet(mNextPageIndex, pageCount);

    }

    List<LifeEntity> lifeEntities = new ArrayList<>();

    private void loadDataFromNet(int page, int count) {
        Map<String, Object> map = new HashMap<>();
        //若获取不到经纬度则默认
        if (AppConfig.slongitude.equals("0")||AppConfig.slatitude.equals("0")){
            AppConfig.slongitude="113.517696";
            AppConfig.slatitude="22.260701";
        }
        map.put("longitude", AppConfig.slongitude);
        map.put("latitude", AppConfig.slatitude);
        map.put("pageIndex", page);
        map.put("pageItemCount", count);
        map.put("customerId", AppConfig.CustomerId);
        apiImp.getIcityFamilyCricleDo(map, this, new DataIdCallback<String>() {
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

    @Override
    public void onRefresh() {
        mNextPageIndex = 1;
        loadDataFromNet(mNextPageIndex, pageCount);
    }

    @Override
    public void onLoadmore() {
        loadDataFromNet(++mNextPageIndex, pageCount);
    }

    private static final int CANCEL_DELECT_POSITION = 101;

    private static final int CANCEL_NOTINTERET = 102;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case CANCEL_DELECT_POSITION:
                if (data == null) return;
                int position = data.getIntExtra("position", -1);
                if (position == -1) return;
                if (adapter == null) {
                    return;
                } else {
                    adapter.getData().remove(position);
                    adapter.notifyDataSetChanged();
                }
                break;
            case CANCEL_NOTINTERET:
                onRefresh();
                break;

        }

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

    @OnClick({R.id.search_all, R.id.sweep_code, R.id.et_search_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_all:
            case R.id.et_search_all:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.sweep_code:
                getActivity().startActivity(new Intent(getActivity(), ScannerActivity.class));
                break;
        }
    }
}
