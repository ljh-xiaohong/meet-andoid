package com.yuejian.meet.framents.business;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nim.uikit.api.UrlApi;
import com.netease.nim.uikit.app.AppConfig;
import com.umeng.socialize.utils.Log;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.BusinessDemandListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.BusinessDemandListEntity;
import com.yuejian.meet.bean.DemandPraiseEntity;
import com.yuejian.meet.bean.PositionInfo;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author : g000gle
 * @time : 2019/06/18 14:10
 * @desc : 首页 - 商圈需求Fragment
 */
public class BusinessDemandFragment extends BaseFragment implements SpringView.OnFreshListener, BusinessDemandListAdapter.OnItemClickListener {

    @Bind(R.id.rv_business_activity_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.spring_business_activity_list)
    SpringView mSpringView;
    @Bind(R.id.ll_business_activity_list_empty)
    LinearLayout mEmptyList;
    private BusinessDemandListAdapter mDemandListAdapter;
    private List<BusinessDemandListEntity> businessDemandListEntities;

    private DemandPraiseEntity praiseEntity;
    private DemandPraiseEntity followEntity;

    private String province = "", city = "";
    private int pageIndex = 1;


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_business_activity, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mDemandListAdapter = new BusinessDemandListAdapter();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mDemandListAdapter);
        mDemandListAdapter.setOnItemClickListener(this);
        initPosition();
    }

    public void initPosition() {
        if (province.isEmpty() || city.isEmpty()) {
            getPosition(new DataIdCallback<PositionInfo>() {
                @Override
                public void onSuccess(PositionInfo data, int id) {
                    loadDataFromNet(1, 20, data.getCity());
                }

                @Override
                public void onFailed(String errCode, String errMsg, int id) {

                }
            });
        }
    }

    public void loadDataFromNet(int page, int count, String city) {
        Map<String, Object> map = new HashMap<>();
        map.put("customer_id", user.customer_id);
        map.put("pageIndex", String.valueOf(page));
        map.put("pageItemCount", String.valueOf(count));
        map.put("city_name", city);
        Log.e("sfd56", city + "");
        apiImp.getDemandList(map, this, new DataIdCallback<String>() {
            @Override

            public void onSuccess(String data, int id) {
//                data = data.replaceAll("\\\"\\[\\{", "[{").replaceAll("\\}\\]\\\"", "}]").replaceAll("\\\\n", "").replaceAll("\\\\", "");
                data = StringUtils.skin(data);
                businessDemandListEntities = JSON.parseArray(data, BusinessDemandListEntity.class);
                if (businessDemandListEntities.size() > 0) {
                    mEmptyList.setVisibility(View.GONE);
                } else {
                    mEmptyList.setVisibility(View.VISIBLE);
                }
                mDemandListAdapter.refresh(businessDemandListEntities);

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
        pageIndex = 1;
        loadDataFromNet(pageIndex, 20, city);
    }

    @Override
    public void onLoadmore() {

        loadDataFromNet(++pageIndex, 20, city);
    }

    //下面3个全为item的点击事件
    //点击跳转
    @Override
    public void onItemClick(View view, int position, BusinessDemandListEntity entity) {
        Intent intent = new Intent(getContext(), WebActivity.class);
        intent.putExtra("url", UrlConstant.DEMANDDETAIL() + "?customer_id=" + user.customer_id + "&id=" + entity.id);
        intent.putExtra("No_Title", true);
        startActivity(intent);
    }

    //关注
    @Override
    public void onFollowClick(View view, int position, BusinessDemandListEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("op_customer_id", entity.customer_id + "");
        params.put("bind_type", entity.getIs_bind() ? "2" : "1");


        apiImp.bindRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (entity.getIs_bind()) {
                    ((TextView) view).setText("+ 关注");
                    entity.setIs_bind(false);
                } else {
                    ((TextView) view).setText("已关注");
                    entity.setIs_bind(true);
                }


            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

    }

    //点赞 View 为ImageView
    @Override
    public void onPraiseClick(View view, int position, BusinessDemandListEntity entity) {

        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("demand_id", entity.id + "");
        apiImp.postDemandPraise(params, getContext(), new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                praiseEntity = JSON.parseObject(data, DemandPraiseEntity.class);

                switch (praiseEntity.getIs_praise()) {
                    case "1":
                        //TODO 点赞成功
                        entity.setIs_praise(true);
                        ((ImageView) view).setImageResource(R.mipmap.icon_video_zan_sel);
                        break;


                    case "0":
                        // TODO 取消点赞
                        entity.setIs_praise(false);
                        ((ImageView) view).setImageResource(R.mipmap.icon_mine_zan_nor);

                        break;
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

    }


    @Override
    public void onPush(View view, int postion, BusinessDemandListEntity entity) {

        Glide.with(mContext).load("http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/2019062821201820186833.png").asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                String shareUrl = UrlApi.h5HttpUrl+"release/blank.html?type=4&id=" + entity.customer_id;
                Utils.umengShareByList(getActivity(), resource, "", "来自 约见·百家姓", shareUrl);
            }
        });
    }
}
