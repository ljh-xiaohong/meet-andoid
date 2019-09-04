package com.yuejian.meet.activities.meritsurname;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.framents.family.MeritRankingFragment;
import com.yuejian.meet.framents.family.SurnameRankFragment;

import butterknife.Bind;

/**
 * @author :
 * @time : 2018/11/13 10:43
 * @desc : 功德排行 主页
 * @version: V1.0
 * @update : 2018/11/13 10:43
 */

public class


MeritRankingActivity extends BaseActivity {

  @Bind(R.id.ib_back)
  ImageButton mIbBack;

  @Bind(R.id.merit_ranking_act_tv_nom)
  TextView mTvNom;

  @Bind(R.id.merit_ranking_act_view)
  View mNomView;

  @Bind(R.id.merit_ranking_act_ll_nom_selected)
  LinearLayout mLlNomSelected;

  @Bind(R.id.merit_ranking_act_tv_surname)
  TextView mTvSurname;

  @Bind(R.id.merit_ranking_act_view_surname)
  View mViewSurname;

  @Bind(R.id.merit_ranking_act_ll_surname_selected)
  LinearLayout mLlSurnameSelected;

  @Bind(R.id.fm)
  FrameLayout mFm;
  private MeritRankingFragment mMeritRankingFragment;
  private SurnameRankFragment mSurnameRankFragment;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_merit_ranking);
    initView();

    initData();

    initListener();

  }

  private void initView() {
    mTvNom.setTextColor(getResources().getColor(R.color.color_232323));
    mNomView.setVisibility(View.VISIBLE);

    mTvSurname.setTextColor(getResources().getColor(R.color.color_grey_999999));
    mViewSurname.setVisibility(View.GONE);

    mMeritRankingFragment = new MeritRankingFragment();
    mSurnameRankFragment = new SurnameRankFragment();

    FragmentManager mSupportFragmentManager = getSupportFragmentManager();
    FragmentTransaction mFragmentTransaction = mSupportFragmentManager.beginTransaction();
    mFragmentTransaction.add(R.id.fm, mMeritRankingFragment);

    mFragmentTransaction.show(mMeritRankingFragment);

    mFragmentTransaction.commit();

  }

  private void initData() {

  }

  private void initListener() {
    mIbBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    mLlNomSelected.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentManager   mSupportFragmentManager = getSupportFragmentManager();

        FragmentTransaction mFragmentTransaction = mSupportFragmentManager.beginTransaction();

        mFragmentTransaction.hide(mSurnameRankFragment);


        if (!mMeritRankingFragment.isAdded()) {

          mFragmentTransaction.add(R.id.fm, mMeritRankingFragment).show(mMeritRankingFragment);
        } else {
          mFragmentTransaction.show(mMeritRankingFragment);
        }
        mTvNom.setTextColor(getResources().getColor(R.color.color_232323));
        mNomView.setVisibility(View.VISIBLE);

        mTvSurname.setTextColor(getResources().getColor(R.color.color_grey_999999));
        mViewSurname.setVisibility(View.GONE);

        mFragmentTransaction.commit();


      }
    });

    mLlSurnameSelected.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FragmentManager   mSupportFragmentManager = getSupportFragmentManager();

        FragmentTransaction mFragmentTransaction = mSupportFragmentManager.beginTransaction();

        mFragmentTransaction.hide(mMeritRankingFragment);
        if (!mSurnameRankFragment.isAdded()) {
          mFragmentTransaction.add(R.id.fm, mSurnameRankFragment).show(mSurnameRankFragment);
        } else {
          mFragmentTransaction.show(mSurnameRankFragment);
        }
        mTvNom.setTextColor(getResources().getColor(R.color.color_grey_999999));
        mNomView.setVisibility(View.GONE);

        mTvSurname.setTextColor(getResources().getColor(R.color.color_232323));
        mViewSurname.setVisibility(View.VISIBLE);

        mFragmentTransaction.commit();


      }
    });
  }

}
