package com.yuejian.meet.framents.creation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.poster.PosterDetailAcitivty;
import com.yuejian.meet.adapters.PosterListLabelAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.PosterModelEntity;
import com.yuejian.meet.bean.TypeEntity;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PosterListFragment extends BaseFragment {

    private TypeEntity type;

    private SpringView springView;

    private RecyclerView recyclerView;

    private boolean isLabel = false;

    private int pageIndex = 0;

    private int pageItemCount = 10;


    private PosterListLabelAdapter posterListLabelAdapter;


    private List<PosterModelEntity> posterModelEntities;


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {


        return inflater.inflate(R.layout.fragment_poster_list, null);
    }

    @SuppressLint("ValidFragment")
    private PosterListFragment() {
        super();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        springView = view.findViewById(R.id.fragment_poster_springview);
        recyclerView = view.findViewById(R.id.fragment_poster_recyclerview);
        type = (TypeEntity) getArguments().getSerializable("label");
        isLabel = getArguments().getBoolean("isLabel", isLabel);
        setListener(isLabel);
        getDataFromNet();


    }

    private void setListener(boolean isLabel) {

        posterListLabelAdapter = new PosterListLabelAdapter(recyclerView, getContext());
        posterListLabelAdapter.setOnItemClickListener(new PosterListLabelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PosterModelEntity item = posterModelEntities.get(position);

                PosterDetailAcitivty.startActivity(getActivity(), item.getId(), AppConfig.CustomerId);

            }
        });


    }

    public static PosterListFragment newInstance(TypeEntity type, boolean isLabel) {
        PosterListFragment fragment = new PosterListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("label", type);
        bundle.putBoolean("isLabel", isLabel);
        fragment.setArguments(bundle);
        return fragment;
    }


    private void getDataFromNet() {
        if (isLabel) {
            PosterModel(1, 10);
        } else {
            RecommendPoster(1, 10);
        }
    }

    /**
     * 推荐列表
     */
    private void RecommendPoster(int pageIndex, int pageItemCount) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageIndex", pageIndex);
        params.put("pageItemCount", pageItemCount);
        apiImp.getAppPostersModelDo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {

                if (data != null) {

                    JSONObject jo = (JSONObject) JSON.parse(data);
                    if (jo == null) return;

                    String code = jo.getString("code");
                    if (code == null || !code.equalsIgnoreCase("0")) return;

                    posterModelEntities = JSON.parseArray(jo.getString("data"), PosterModelEntity.class);

                    posterListLabelAdapter.refresh(posterModelEntities);

                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });


    }

//    /**
//     * 解析 推荐 数据
//     */
//    private List<RecommendPosterEntity> parseRecommendJson(String data) {
//        List<RecommendPosterEntity> entities = new ArrayList<>();
//
//        JSONArray jo = JSON.parseArray(data);
//
//        if (null != jo) {
//            for (int i = 0; i < jo.size(); i++) {
//                RecommendPosterEntity entity = new RecommendPosterEntity();
//                JSONObject jsin = (JSONObject) jo.get(i);
//                JSONArray ja = jsin.getJSONArray("contentLabelList");
//                if (ja != null) {
//                    List<RecommendPosterEntity.ContentLabelList> contentLabelLists = JSON.parseArray(jsin.getString("contentLabelList"), RecommendPosterEntity.ContentLabelList.class);
//                    entity.setContentLabelList(contentLabelLists);
//                }
//                entity.setCreateTime(jsin.getInteger("createTime"));
//                entity.setId(jsin.getInteger("id"));
//                entity.setIsDelete(jsin.getInteger("isDelete"));
//                entity.setLabelId(jsin.getString("labelId"));
//                entity.setPostersJson(jsin.getString("postersJson"));
//                entity.setPostersPrice(jsin.getDouble("postersPrice"));
//                entity.setPostersTitle(jsin.getString("postersTitle"));
//                entity.setPreviewUrl(jsin.getString("previewUrl"));
//                entity.setRecFlag(jsin.getInteger("recFlag"));
//                entity.setUpdateTime(jsin.getInteger("updateTime"));
//                entity.setUsenumFalse(jsin.getInteger("usenumFalse"));
//                entity.setUsenumTrue(jsin.getInteger("usenumTrue"));
//                entities.add(entity);
//            }
//
//
//        }
//
//        return entities;
//
//    }

//    /**
//     * 解析 海报ID 数据
//     *
//     * @param data
//     * @return
//     */
//    private PosterModelEntity parsePosterJson(String data) {
//        PosterModelEntity posterModelEntities = new PosterModelEntity();
//        JSONObject ja = (JSONObject) JSON.parse(data);
//        if (null != ja) {
//
//            if (ja.getJSONArray("postersModelList") != null) {
//                List<PosterModelEntity.PostersModelList> lists = JSON.parseArray(ja.getString("postersModelList"), PosterModelEntity.PostersModelList.class);
//                posterModelEntities.setPostersModelList(lists);
//            }
//            posterModelEntities.setCoverUrl(ja.getString("coverUrl"));
//            posterModelEntities.setCreateTime(ja.getInteger("createTime"));
//            posterModelEntities.setDelete(ja.getBoolean("delete"));
//            posterModelEntities.setEndTime(ja.getInteger("endTime"));
//            posterModelEntities.setId(ja.getInteger("id"));
//            posterModelEntities.setJoinnumFalse(ja.getInteger("joinnumFalse"));
//            posterModelEntities.setJoinnumTrue(ja.getInteger("joinnumTrue"));
//            posterModelEntities.setSort(ja.getInteger("sort"));
//            posterModelEntities.setStartTime(ja.getInteger("startTime"));
//            posterModelEntities.setTitle(ja.getString("title"));
//            posterModelEntities.setType(ja.getInteger("type"));
//
//
//        }
//
//        return posterModelEntities;
//
//    }


    /**
     * 海报列表
     */
    private void PosterModel(int pageIndex, int pageItemCount) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", type.getId());
        params.put("pageIndex", pageIndex);
        params.put("pageItemCount", pageItemCount);
        apiImp.getAppPostersModelLabel(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (data == null) return;
                JSONObject jo = (JSONObject) JSON.parse(data);
                if (jo == null) return;
                String code = jo.getString("code");
                if (code == null || !code.equalsIgnoreCase("0")) return;


                posterModelEntities = JSON.parseArray(jo.getString("data"), PosterModelEntity.class);

                posterListLabelAdapter.refresh(posterModelEntities);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }


}
