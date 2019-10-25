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
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.ArticleDetailsActivity;
import com.yuejian.meet.activities.creation.VideoDetailsActivity;
import com.yuejian.meet.activities.family.ActivityLabActivity;
import com.yuejian.meet.activities.family.ArticleActivity;
import com.yuejian.meet.activities.family.VideoActivity;
import com.yuejian.meet.activities.find.ScannerActivity;
import com.yuejian.meet.activities.search.SearchActivity;
import com.yuejian.meet.adapters.BaseAdapter;
import com.yuejian.meet.adapters.FamilyCircleRecommendListAdapter;
import com.yuejian.meet.adapters.RecommendListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.FamilyRecommendEntity;
import com.yuejian.meet.bean.RecommendEntity;
import com.yuejian.meet.framents.base.BaseFragment;
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
 * @time : 2019/05/09 15:20
 * @desc : 首页 - 家圈 - 推荐Fragment
 */
public class FamilyCircleRecommendFragment extends BaseFragment
        implements SpringView.OnFreshListener, FamilyCircleRecommendListAdapter.OnListItemClickListener {
    @Bind(R.id.rv_family_circle_recommend_list)
    RecyclerView mRecommendListView;
    @Bind(R.id.spring_family_recommend_list)
    SpringView mSpringView;
    @Bind(R.id.ll_family_recommend_list_empty)
    LinearLayout mEmptyList;
    @Bind(R.id.search_all)
    ImageView searchAll;
    @Bind(R.id.sweep_code)
    LinearLayout sweepCode;
    @Bind(R.id.et_search_all)
    TextView etSearchAll;

    private static final int CANCEL_DELECT_POSITION = 101;

    private int mNextPageIndex = 1;
    private int pageCount = 10;
    //    private FamilyCircleRecommendListAdapter mRecommendListAdapter;
    private RecommendListAdapter recommendListAdapter;
    private boolean firstLoad = true;
    private boolean is_recommend = true;
    Type type = Type.video;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_family_circle_recommend, container, false);
    }

    @Override
    protected void initData() {
        super.initData();

//        mSpringView.setFooter(new DefaultFooter(getContext()));
        mSpringView.setHeader(new DefaultHeader(getContext()));
        mSpringView.setListener(this);

        recommendListAdapter = new RecommendListAdapter(mRecommendListView, getContext());
        recommendListAdapter.setOnItemClickListener((view, position) -> {

            RecommendEntity item = recommendListAdapter.getData().get(position);
            switch (item.getType()) {
                //文章
                case 1:
                    ArticleActivity.startActivityForResult((Activity) mContext, item.getId() + "", AppConfig.CustomerId, position, CANCEL_DELECT_POSITION);
                    break;
                //视频
                case 2:
                    VideoActivity.startActivityForResult((Activity) mContext, item.getId() + "", AppConfig.CustomerId, position, CANCEL_DELECT_POSITION, item.getCoveSizeType() == 0 ? true : false);
                    break;
                //模板
                case 3:
//                    PosterDetailAcitivty.startActivity(mContext, item.getId(), AppConfig.CustomerId);
                    break;
                //活动
                case 4:
                    ActivityLabActivity.startActivity(mContext, item.getId() + "", AppConfig.CustomerId);
                    break;
            }

        });
        //底部更新
        recommendListAdapter.setOnBottomListener(new BaseAdapter.OnBottomListener() {
            @Override
            public void onBottom() {
                //到达底部自动加载
                onLoadmore();
            }

            @Override
            public boolean canScroll(List list) {
                //获取数据 如果数据被指定个数整除，则继续监听，不回调
                if (list != null && list.size() % pageCount == 0) return true;
                return false;
            }
        });

//        loadDataFromNet(mNextPageIndex, pageCount);
        mSpringView.callFresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CANCEL_DELECT_POSITION && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            if (position == -1) return;
            recommendListAdapter.getData().remove(position);
            recommendListAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.search_all, R.id.sweep_code, R.id.et_search_all})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_all:
            case R.id.et_search_all://搜索
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.sweep_code://扫码
                getActivity().startActivity(new Intent(getActivity(), ScannerActivity.class));
                break;
        }

    }


    public void loadDataFromNet(int page, int count) {
        Map<String, Object> map = new HashMap<>();
        //500102
        map.put("customerId", AppConfig.CustomerId);
        map.put("pageIndex", String.valueOf(page));
        map.put("pageItemCount", count + "");

        apiImp.getRecommendFamilyCricleDo(map, this, new DataIdCallback<String>() {

            List<RecommendEntity> recommendEntities;

            @Override
            public void onSuccess(String data, int id) {

                if (checkIsLife()) return;

                if (null != data) {


                    JSONObject jo = (JSONObject) JSON.parse(data);
                    if (jo.getInteger("code") == 0) {
                        recommendEntities = JSON.parseArray(jo.getString("data"), RecommendEntity.class);
                        if (recommendEntities.size() > 0 && firstLoad) {
                            mEmptyList.setVisibility(View.GONE);
                            firstLoad = false;
                        }

                        if (page <= 1) {
                            //上拉最新
                            recommendListAdapter.refresh(recommendEntities);

                        } else {
                            //下拉更多
                            recommendListAdapter.Loadmore(recommendEntities);
                        }
                        mNextPageIndex++;
                    }

                }

                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (checkIsLife()) return;
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();

                }
            }
        });
    }



    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (checkIsLife()) return;
        switch (event.getCallType()) {
            case FAMILY_RECOMMEND_ZAN:
//                mRecommendListAdapter.notifyDataSetChanged();
                break;

        }

    }

    @Override
    public void onRefresh() {
        if (checkIsLife()) return;
        mNextPageIndex = 1;
//        loadDataFromNet(type, mNextPageIndex, pageCount, is_recommend);
        loadDataFromNet(mNextPageIndex, pageCount);
    }

    @Override
    public void onLoadmore() {
        if (checkIsLife()) return;
//        loadDataFromNet(type, ++mNextPageIndex, pageCount, is_recommend);
        loadDataFromNet(mNextPageIndex, pageCount);
    }

    @Override
    public void onItemClick(FamilyRecommendEntity entity) {
        Intent intent;
        switch (type) {
            case article:
                intent = new Intent(getActivity(), ArticleDetailsActivity.class);
                intent.putExtra("id", Integer.valueOf(entity.getId()));
                startActivity(intent);

                break;

            case video:
                intent = new Intent(getActivity(), VideoDetailsActivity.class);
                intent.putExtra("id", Integer.valueOf(entity.getId()));
//                intent.putExtra("customer_id", entity.getCustomer_id());
                startActivity(intent);
                break;

        }

    }

    //获取文章列表
    private List<FamilyRecommendEntity> toEntity(String data) {

        List<FamilyRecommendEntity> entities = new ArrayList<>();

        data = data.replaceAll("null", "\"\"");

        JSONArray jsonArray = JSONArray.parseArray(data);
        for (Object o : jsonArray) {
            FamilyRecommendEntity entity = new FamilyRecommendEntity();
            List<FamilyRecommendEntity.Content> contents = new ArrayList<>();
            JSONObject ob = (JSONObject) JSONObject.parse(o.toString());
            entity.setComment_num(ob.get("comment_num").toString());
            entity.setCreate_time(ob.get("create_time").toString());
            entity.setArticle_comment_time(ob.get("article_comment_time").toString());
            entity.setIs_reprint(ob.get("is_reprint").toString());
            entity.setPhoto(ob.get("photo").toString());
            entity.setIs_praise(ob.get("is_praise").toString());
            entity.setTitle(ob.get("title").toString());
            entity.setType(ob.get("type").toString());
            entity.setVip_deploy_id(ob.get("vip_deploy_id").toString());
            entity.setIs_delete(ob.get("is_delete").toString());
            entity.setThird_photo(ob.get("third_photo").toString());
            entity.setArticle_comment_content(ob.get("article_comment_content").toString());
            entity.setMusic_url(ob.get("music_url").toString());
            entity.setIs_audit(ob.get("is_audit").toString());
            entity.setHeart_num(ob.get("heart_num").toString());
            entity.setVip_type(ob.get("vip_type").toString());
            entity.setIs_comment(ob.get("is_comment").toString());
            entity.setFabulous_num(ob.get("fabulous_num").toString());
            entity.setName(ob.get("name").toString());
            entity.setId(ob.get("id").toString());
            entity.setCustomer_id(ob.get("customer_id").toString());
            entity.setPhoto_and_video_url(ob.get("photo_and_video_url").toString());
            entity.setPraise_type(ob.get("praise_type").toString());

            if (ob.get("type").toString().equalsIgnoreCase("2")) {
                //文章
                JSONArray ja = JSONArray.parseArray(ob.get("content").toString());
                if (null != ja) {

                    for (Object oi : ja) {
                        JSONObject oib = (JSONObject) JSONObject.parse(oi.toString());
                        FamilyRecommendEntity.Content inner = new FamilyRecommendEntity.Content();
                        inner.setContent(oib.get("content").toString().replaceAll("\\\\n", ""));
                        inner.setIndex(oib.get("index").toString());
                        inner.setType(oib.get("type").toString());
                        contents.add(inner);

                    }
                    entity.setContents(contents);
                }

            } else if (ob.get("type").toString().equalsIgnoreCase("4")) {
                //VIDEO
                entity.setContent(ob.get("content").toString());
            }

            entities.add(entity);
        }


        return entities;
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

    public enum Type {
        all("0"), video("4"), article("2");
        private String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}