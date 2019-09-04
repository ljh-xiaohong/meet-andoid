package com.yuejian.meet.framents.family;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.SurnameRankFmAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.SurnameRankFmEntity;
import com.yuejian.meet.framents.base.BaseFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author :
 * @time : 2018/11/14 17:12
 * @desc : 功德排行 姓氏排行
 * @version: V1.0
 * @update : 2018/11/14 17:12
 */


public class SurnameRankFragment extends BaseFragment {


  @Bind(R.id.merit_ranking_fmt_myname)
  TextView mMeritRankingFmtMyname;


  @Bind(R.id.merit_ranking_act_tv_my_allmerit)
  TextView mMeritRankingActTvMyAllmerit;

  @Bind(R.id.surname_rank_act_tv_my_rank)
  TextView mSurnameRankActTvMyRank;

  @Bind(R.id.surname_rank_act_tv_my)
  TextView mSurnameRankActTvMy;

  @Bind(R.id.surname_ranking_act_rv)
  RecyclerView mRecyclerView;

  private SurnameRankFmAdapter mSurnameRankFmAdapter;

  @Override
  protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    return inflater.inflate(R.layout.fragment_surname_rank, container, false);
  }


  @Override
  protected void initData() {
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


    mRecyclerView.setLayoutManager(linearLayoutManager);

    mSurnameRankFmAdapter = new SurnameRankFmAdapter(getActivity(), null);

    mRecyclerView.setAdapter(mSurnameRankFmAdapter);


    getMerit();
  }

  @Override
  protected void initListener() {
    super.initListener();
  }

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

  private void getMerit() {
    Map<String, Object> map = new HashMap<>();
    map.put("surname", user.getSurname());
    apiImp.getSurnameRankTop10(map, this, new DataIdCallback<String>() {
      @Override
      public void onSuccess(String data, int id) {
        List<SurnameRankFmEntity> meritRankTOPEntities = JSON.parseArray(data, SurnameRankFmEntity.class);
        setData(meritRankTOPEntities);
      }

      @Override
      public void onFailed(String errCode, String errMsg, int id) {

      }
    });

  }

  private void setData(List<SurnameRankFmEntity> meritRankTOPEntities) {

    SurnameRankFmEntity surnameRankMy = meritRankTOPEntities.get(0);

    mMeritRankingFmtMyname.setText(surnameRankMy.getSurname());
    mMeritRankingActTvMyAllmerit.setText(String.valueOf(surnameRankMy.getPractice_total()));
    mSurnameRankActTvMyRank.setText(surnameRankMy.getRank() + "");
    mSurnameRankActTvMy.setText(surnameRankMy.getSurname() + "氏排名");


    mSurnameRankFmAdapter.setNewData(meritRankTOPEntities.subList(1, meritRankTOPEntities.size()));


  }


}
