package com.yuejian.meet.framents.family;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.meritsurname.QuestRewardsActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.MeritRankTopAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.MeritRankTOPEntity;
import com.yuejian.meet.bean.PicEntity;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author :
 * @time : 2018/11/14 17:26
 * @desc : 功德排行 个人排行
 * @version: V1.0
 * @update : 2018/11/14 17:26
 */

public class MeritRankingFragment extends BaseFragment {


  @Bind(R.id.merit_ranking_act_iv_bg)
  ImageView mMeritRankingActIvBg;

  @Bind(R.id.merit_ranking_act_tv_my_rank)
  TextView mMeritRankingActTvMyRank;

  @Bind(R.id.merit_ranking_act_iv_icon)
  CircleImageView mMeritRankingActIvIcon;

  @Bind(R.id.merit_ranking_act_tv_name)
  TextView mMeritRankingActTvName;

  @Bind(R.id.merit_ranking_act_tv_get_merit)
  TextView mMeritRankingActTvGetMerit;

  @Bind(R.id.merit_ranking_act_tv_my_allmerit)
  TextView mMeritRankingActTvMyAllmerit;

  @Bind(R.id.merit_ranking_act_rv)
  RecyclerView mRecyclerView;


  private PicEntity mPicEntity;
  private MeritRankTopAdapter mMeritRankTopAdapter;


  @Override
  protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    return inflater.inflate(R.layout.fragment_merit_ranking, container, false);
  }


  @Override
  protected void initListener() {
    mMeritRankingActTvGetMerit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 任务奖励 获取功德
        startActivity(new Intent(getActivity(), QuestRewardsActivity.class));


      }
    });
    mMeritRankingActIvBg.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mPicEntity != null && !mPicEntity.getUrl().isEmpty()){
          Intent sintent = new Intent(getActivity(), WebActivity.class);
          sintent.putExtra("No_Title", true);
          sintent.putExtra("url", mPicEntity.getUrl());
          startActivity(sintent);
        }else {
          return;
        }

      }
    });
  }

  @Override
  protected void initData() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

    mRecyclerView.setLayoutManager(linearLayoutManager);

    mMeritRankTopAdapter = new MeritRankTopAdapter(getActivity(), null);

    mRecyclerView.setAdapter(mMeritRankTopAdapter);

    getMerit();

  }




/**
 * item_surname_rank_act_rv_my.xml 个人功德排行 rv
 */
  /**
   * 第一次对用户可见时会调用该方法
   */

  protected void onFirstUserVisible() {
    getMerit();

  }

  /**
   * 对用户可见时会调用该方法，除了第一次
   */
  public void onUserVisible() {
    getMerit();


  }

  public void getMerit() {
    // 获取数据
    Map<String, Object> map = new HashMap<>();
    map.put("customer_id", user.getCustomer_id());
    apiImp.getPic(new HashMap<String, Object>(), this, new DataIdCallback<String>() {
      @Override
      public void onSuccess(String data, int id) {
        mPicEntity = JSON.parseObject(data, PicEntity.class);

      }

      @Override
      public void onFailed(String errCode, String errMsg, int id) {

      }
    });

    apiImp.postMeritRank(map, this, new DataIdCallback<String>() {
      @Override
      public void onSuccess(String data, int id) {
        List<MeritRankTOPEntity> meritRankTOPEntities = JSON.parseArray(data, MeritRankTOPEntity.class);
        setData(meritRankTOPEntities);
      }

      @Override
      public void onFailed(String errCode, String errMsg, int id) {

      }
    });

  }

  private void setData(List<MeritRankTOPEntity> meritRankTOPEntities) {
    MeritRankTOPEntity meritRankTOP = meritRankTOPEntities.get(0);

    Glide.with(getActivity()).load(meritRankTOP.getIcon()).into(mMeritRankingActIvIcon);

    Glide.with(getActivity()).load(mPicEntity.getPic()).into(mMeritRankingActIvBg);

    mMeritRankingActTvName.setText(meritRankTOP.getName());

    mMeritRankingActTvMyRank.setText(meritRankTOP.getNo() + "");

    mMeritRankingActTvMyAllmerit.setText(meritRankTOP.getIntegral().substring(0,meritRankTOP.getIntegral().lastIndexOf(".")));


    mMeritRankTopAdapter.setNewData(meritRankTOPEntities.subList(1,meritRankTOPEntities.size()));

  }
}
