package com.yuejian.meet.framents.business;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.BusinessActivityListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.BusinessActivityListEntity;
import com.yuejian.meet.bean.PositionInfo;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author : g000gle
 * @time : 2019/06/18 10:10
 * @desc : 首页 - 商圈活动Fragment
 */
public class BusinessActivityFragment extends BaseFragment
        implements SpringView.OnFreshListener, BusinessActivityListAdapter.OnItemClickListener {

    @Bind(R.id.rv_business_activity_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.spring_business_activity_list)
    SpringView mSpringView;
    @Bind(R.id.ll_business_activity_list_empty)
    LinearLayout mEmptyList;
    private BusinessActivityListAdapter mActivityListAdapter;

    private List<BusinessActivityListEntity> businessActivityListEntities = new ArrayList<>();

    private String province = "", city = "";

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_business_activity, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mActivityListAdapter = new BusinessActivityListAdapter(getActivity(), this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mActivityListAdapter);
        initPosition();
    }

    //首次获取地理位置，后获取活动列表
    public void initPosition() {
        if (province.isEmpty() || city.isEmpty()) {
            getPosition(new DataIdCallback<PositionInfo>() {
                @Override
                public void onSuccess(PositionInfo data, int id) {
                    loadDataFromNet(data.getProvince(), data.getCity());
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }
            });
        }
    }

    //获取活动列表
    public void loadDataFromNet(String province, String city) {
        this.province = province;
        this.city = city;
        Map<String, Object> map = new HashMap<>();
        map.put("customer_id", user.customer_id);
        map.put("province", province);
        map.put("city", city);
        apiImp.getActivityListDo(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {

//                businessActivityListEntities = JSON.parseArray(StringUtils.skin(data), BusinessActivityListEntity.class);
                businessActivityListEntities = ParseJson(data);
                if (businessActivityListEntities.size() > 0) {
                    mEmptyList.setVisibility(View.GONE);
                } else {
                    mEmptyList.setVisibility(View.VISIBLE);
                }
                mActivityListAdapter.refresh(businessActivityListEntities);

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
        //  loadDataFromNet(null, null);
    }

    @Override
    public void onLoadmore() {

    }

    public List<BusinessActivityListEntity> ParseJson(String data) {

        JSONArray ja = JSON.parseArray(data);

        if (null != ja || ja.size() > 0) {
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                BusinessActivityListEntity entity = new BusinessActivityListEntity();
                entity.setJoin_way(jo.getString("join_way"));
                entity.setCreate_time(jo.getString("create_time"));
                entity.setCity(jo.getString("city"));
                entity.setPhoto(jo.getString("photo"));
                entity.setUserName(jo.getString("userName"));
                entity.setContent(jo.getString("content"));
                entity.setApply_num_yet(jo.getString("apply_num_yet"));
                entity.setActivity_state(jo.getString("activity_state"));
                entity.setRead_num(jo.getString("read_num"));
                entity.setProvince(jo.getString("province"));
                entity.setDistrict(jo.getString("district"));
                entity.setCompany_name(jo.getString("company_name"));
                entity.setIs_concern(jo.getString("is_concern"));
                entity.setId(jo.getString("id"));
                entity.setCustomer_id(jo.getString("customer_id"));
                entity.setIs_pass(jo.getString("is_pass"));
                entity.setHeadline(jo.getString("headline"));
                String url = jo.getString("resources_url");
                List<BusinessActivityListEntity.Resources_url> urls = JSON.parseArray(url, BusinessActivityListEntity.Resources_url.class);
                entity.setResources_url(urls);
                String join = jo.getString("joinList");
                List<BusinessActivityListEntity.JoinList> joins = JSON.parseArray(join, BusinessActivityListEntity.JoinList.class);
                entity.setJoinList(joins);
                businessActivityListEntities.add(entity);

            }
        }

        return businessActivityListEntities;
    }


    //关注
    @Override
    public void onFollowClick(View view, int position) {

        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("op_customer_id", businessActivityListEntities.get(position).getCustomer_id() + "");
        params.put("bind_type", businessActivityListEntities.get(position).getIs_concern().equals("0") ? "1" : "2");
        apiImp.bindRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (!businessActivityListEntities.get(position).getIs_concern().equals("0")) {
                    ((TextView) view).setText("+ 关注");
                    businessActivityListEntities.get(position).setIs_concern("0");
                } else {
                    ((TextView) view).setText("已关注");

                    businessActivityListEntities.get(position).setIs_concern("1");
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    //框款点击
    @Override
    public void onItemClick(View view, int position) {
        Intent activityIntent = new Intent(getActivity(), WebActivity.class);
        String params = String.format("?customer_id=%s&&id=%s", user.customer_id, businessActivityListEntities.get(position).getId());
        String url = UrlConstant.apiUrl() + "release/details-of-activities.html" + params;
        activityIntent.putExtra("url", url);
        activityIntent.putExtra("No_Title", true);
        startActivity(activityIntent);
    }

    //
    @Override
    public void onJoinClick(View view, int position) {


    }
}
