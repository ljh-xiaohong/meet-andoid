package com.yuejian.meet.activities.meritsurname;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.SurnameRankAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.SuranameRankEntity;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;


/**
 * @author :
 * @time : 2018/11/13 16:03
 * @desc : 姓氏排行
 * @version: V1.0
 * @update : 2018/11/13 16:03
 */

public class SurnameRankActivity extends BaseActivity implements SpringView.OnFreshListener {


  @Bind(R.id.surname_rank_act_ib_back)
  ImageButton mSurnameRankActIbBack;

  @Bind(R.id.surname_rank_act_tv_rank)
  TextView mSurnameRankActTvRank;

  @Bind(R.id.surname_rank_act_rvlist)
  RecyclerView mRecyclerView;

  @Bind(R.id.surname_rank_act_sv)
  SpringView mSpringView;

  private int page_Index = 1;

  private SurnameRankAdapter mSurnameRankAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_surname_rank);
    initView();
    initData();
    initListener();
  }

  private void initView() {

    mSpringView.setHeader(new DefaultHeader(this));
    mSpringView.setFooter(new DefaultFooter(this));
    // 加载完成
    mSpringView.setListener(this);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    mRecyclerView.setLayoutManager(linearLayoutManager);

    mSurnameRankAdapter = new SurnameRankAdapter(this, null);
    mRecyclerView.setAdapter(mSurnameRankAdapter);

  }

  private void initData() {
    Map<String, Object> map = new HashMap<>();
    map.put("surname", user.getSurname());
    map.put("pageIndex", page_Index + "");

    apiImp.getSurnameRank(map, this, new DataIdCallback<String>() {
      @Override
      public void onSuccess(String data, int id) {
        SuranameRankEntity suranameRankEntity = JSON.parseObject(data, SuranameRankEntity.class);
        mSurnameRankActTvRank.setText(suranameRankEntity.getCustomerSurname().getRank() + "");
        mSpringView.onFinishFreshAndLoad();
        setData(suranameRankEntity);


      }

      @Override
      public void onFailed(String errCode, String errMsg, int id) {
        mSpringView.onFinishFreshAndLoad();


      }
    });

  }

  private void setData(SuranameRankEntity suranameRankEntity) {
    if (page_Index == 1) {
      if (suranameRankEntity.getSurnameList() == null || suranameRankEntity.getSurnameList().isEmpty()) {
        page_Index = 1;
      } else {
        mSurnameRankAdapter.setNewData(suranameRankEntity.getSurnameList());
      }
    } else {
      if (suranameRankEntity.getSurnameList() == null || suranameRankEntity.getSurnameList().isEmpty()) {
        page_Index--;
      } else {
        mSurnameRankAdapter.addData(suranameRankEntity.getSurnameList());
      }
    }


  }

  private void initListener() {
    mSurnameRankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        // 姓氏起源
        startActivity(new Intent(SurnameRankActivity.this, SurnameOriginActivity.class));
      }
    });
    mSurnameRankActIbBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }


  @Override
  public void onRefresh() {
    page_Index = 1;
    initData();
  }

  @Override
  public void onLoadmore() {
    page_Index++;
    initData();
  }
}
