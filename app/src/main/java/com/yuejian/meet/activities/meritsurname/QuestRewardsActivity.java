package com.yuejian.meet.activities.meritsurname;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.ShareCodeActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.QuestRewardsEntity;
import com.yuejian.meet.framents.find.ForHomePfracticeActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author :徐
 * @time : 2018/11/14 11:00
 * @desc : 每日任务
 *
 * @update : 2019/06/21 16:00
 */

public class QuestRewardsActivity extends BaseActivity {

    @Bind(R.id.ib_back)
    ImageButton mIbBack;


    @Bind(R.id.quest_rewards_act_tv_signin_go)
    TextView mQuestRewardsActTvSigninGo;

    @Bind(R.id.quest_rewards_act_tv_practice)
    TextView mQuestRewardsActTvPractice;
    @Bind(R.id.quest_rewards_act_tv_practice_go)
    TextView mQuestRewardsActTvPracticeGo;

    @Bind(R.id.quest_rewards_act_tv_addfd)
    TextView mQuestRewardsActTvAddfd;

    @Bind(R.id.quest_rewards_act_tv_addfd_go)
    TextView mQuestRewardsActTvAddfdGo;

    @Bind(R.id.quest_rewards_act_tv_recommend)
    TextView mQuestRewardsActTvRecommend;
    @Bind(R.id.quest_rewards_act_tv_recommend_go)
    TextView mQuestRewardsActTvRecommendGo;
    @Bind(R.id.qiandao_num)
    TextView mQiandaoNum;
    @Bind(R.id.xiuxing_num)
    TextView mXiuxingNum;
    @Bind(R.id.haoyou_num)
    TextView mHaoyouNum;
    @Bind(R.id.tuijian_num)
    TextView mTuijianNum;
    private QuestRewardsEntity mMQuestRewardsEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_rewards);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {


    }

    private void initData() {
        Map<String, Object> map = new HashMap<>();
        map.put("customer_id", user.getCustomer_id());
        apiImp.getQuestReward(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                mMQuestRewardsEntity = JSON.parseObject(data, QuestRewardsEntity.class);
                setData(mMQuestRewardsEntity);


            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

    }

    private void setData(QuestRewardsEntity questRewardsEntity) {

        //签到
        if (questRewardsEntity.getSigninMap().getIsGet() == 1) {
            // 已经领取奖励
            mQuestRewardsActTvSigninGo.setClickable(false);
            mQuestRewardsActTvSigninGo.setText("已签到");
            mQuestRewardsActTvSigninGo.setBackground(getResources().getDrawable(R.drawable.quest_rewards_act_tv_bg_huise));
        } else {
            if (questRewardsEntity.getSigninMap().getIsDo() == 1) {
                // 已完成任务 未领取奖励
                mQuestRewardsActTvSigninGo.setText("领取");
                mQuestRewardsActTvSigninGo.setBackground(getResources().getDrawable(R.drawable.quest_rewards_act_tv_bg));
            } else {
                mQuestRewardsActTvSigninGo.setText("去签到");
                mQuestRewardsActTvSigninGo.setBackground(getResources().getDrawable(R.drawable.quest_rewards_act_tv_bg));
            }

        }


        mQiandaoNum.setText("+" + questRewardsEntity.getSigninMap().getRewardsValue());


        // 修行
        if (questRewardsEntity.getEveryDayPracticeMap().getIsGet() == 1) {
            // 已经领取奖励
            mQuestRewardsActTvPracticeGo.setClickable(false);

            mQuestRewardsActTvPracticeGo.setBackground(getResources().getDrawable(R.drawable.quest_rewards_act_tv_bg_huise));
            mQuestRewardsActTvPracticeGo.setText("已修行");

        } else {
            if (questRewardsEntity.getEveryDayPracticeMap().getIsDo() == 1) {
                // 已修行 未领取奖励
                mQuestRewardsActTvPracticeGo.setBackground(getResources().getDrawable(R.drawable.quest_rewards_act_tv_bg));
                mQuestRewardsActTvPracticeGo.setText("领取");
            } else {
                //未修行
                mQuestRewardsActTvPracticeGo.setBackground(getResources().getDrawable(R.drawable.quest_rewards_act_tv_bg));
                mQuestRewardsActTvPracticeGo.setText("去修行");
            }
        }

        mQuestRewardsActTvPractice.setText("前往完成修行任意一项    " + questRewardsEntity.getEveryDayPracticeMap().getFinishNumber()
                + "/" + questRewardsEntity.getEveryDayPracticeMap().getTargetNumber());
        mXiuxingNum.setText("+" + questRewardsEntity.getEveryDayPracticeMap().getRewardsValue());


        // 添加好友
        if (questRewardsEntity.getAddFriendMap().getIsGet() == 1) {

            mQuestRewardsActTvAddfdGo.setClickable(false);

            mQuestRewardsActTvAddfdGo.setText("已添加");

            mQuestRewardsActTvAddfdGo.setBackground(getResources().getDrawable(R.drawable.quest_rewards_act_tv_bg_huise));
        } else {

            if (questRewardsEntity.getAddFriendMap().getIsDo() == 1) {
                // 已添加好友 未领取奖励
                mQuestRewardsActTvAddfdGo.setText("领取");
                mQuestRewardsActTvAddfdGo.setBackground(getResources().getDrawable(R.drawable.quest_rewards_act_tv_bg));
            } else {
                // 未完成任务
                mQuestRewardsActTvAddfdGo.setText("已添加");

                mQuestRewardsActTvAddfdGo.setBackground(getResources().getDrawable(R.drawable.quest_rewards_act_tv_bg));
            }
        }


        mQuestRewardsActTvAddfd.setText("添加" + questRewardsEntity.getAddFriendMap().getTargetNumber() + "名好友    " + questRewardsEntity.getAddFriendMap().getFinishNumber()
                + "/" + questRewardsEntity.getAddFriendMap().getTargetNumber());

        mHaoyouNum.setText("+" + questRewardsEntity.getAddFriendMap().getRewardsValue());


        // 推荐好友注册
        if (questRewardsEntity.getReferreMap().getIsGet() == 1) {
            mQuestRewardsActTvRecommendGo.setText("已推荐");
        } else {
            if (questRewardsEntity.getReferreMap().getIsDo() == 1) {
                mQuestRewardsActTvRecommendGo.setText("领取");
            }

        }


        mQuestRewardsActTvRecommend.setText("推荐好友注册" + questRewardsEntity.getReferreMap().getTargetNumber() + "人    " + questRewardsEntity.getReferreMap().getFinishNumber()
                + "/" + questRewardsEntity.getReferreMap().getTargetNumber());

        mTuijianNum.setText("+" + questRewardsEntity.getReferreMap().getRewardsValue());
    }

    private void initListener() {
        mQuestRewardsActTvRecommendGo.setOnClickListener(this);
        mQuestRewardsActTvAddfdGo.setOnClickListener(this);
        mQuestRewardsActTvPracticeGo.setOnClickListener(this);
        mQuestRewardsActTvSigninGo.setOnClickListener(this);
        mIbBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mMQuestRewardsEntity == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.quest_rewards_act_tv_signin_go:

                if (mMQuestRewardsEntity.getSigninMap() == null) {
                    return;
                }

                // 签到
                if (mMQuestRewardsEntity.getSigninMap().getIsGet() == 1) {
                    //已领取签到奖励
                    Toast.makeText(this, "已领取" + mMQuestRewardsEntity.getSigninMap().getRewardsValue() + "奖励", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (mMQuestRewardsEntity.getSigninMap().getIsDo() == 1) {
                        // 已签到 领取任务
                        postRenwu(1);

                    } else {
                        // 未签到
                        sign();

                    }

                }

                break;

            case R.id.quest_rewards_act_tv_practice_go:
                if (mMQuestRewardsEntity.getEveryDayPracticeMap() == null) {
                    return;
                }

                // 修行

                if (mMQuestRewardsEntity.getEveryDayPracticeMap().getIsGet() == 1) {
                    // 已领取任务

                    return;
                } else {

                    if (mMQuestRewardsEntity.getEveryDayPracticeMap().getIsDo() == 1) {
                        // 已做任务 没领取
                        postRenwu(2);
                    } else {
                        // 没修行 去修行
                        startActivity(new Intent(this, ForHomePfracticeActivity.class));

                    }
                }
                break;

            case R.id.quest_rewards_act_tv_addfd_go:
                if (mMQuestRewardsEntity.getAddFriendMap() == null) {
                    return;
                }

                // 添加好友
                if (mMQuestRewardsEntity.getAddFriendMap().getIsGet() == 1) {
                    //已领取添加好友 奖励
                    Toast.makeText(this, "已领取" + mMQuestRewardsEntity.getAddFriendMap().getRewardsValue() + "奖励", Toast.LENGTH_SHORT).show();

                    return;
                } else {

                    if (mMQuestRewardsEntity.getAddFriendMap().getIsDo() == 1) {
                        // 已做任务 没领取
                        postRenwu(3);
                    } else {
                        // 没添加好友 去添加 为家修行
                        startActivity(new Intent(this, AddSurnameOriginActivity.class));


                    }
                }


                break;


            case R.id.quest_rewards_act_tv_recommend_go:
                if (mMQuestRewardsEntity.getReferreMap() == null) {
                    return;
                }

                // 推荐好友注册

                if (mMQuestRewardsEntity.getReferreMap().getIsGet() == 1) {
                    //已领取推荐好友 奖励
                    startActivity(new Intent(this, ShareCodeActivity.class));

                } else {

                    if (mMQuestRewardsEntity.getReferreMap().getIsDo() == 1) {
                        // 已做任务 没领取
                        postRenwu(4);

                    } else {
                        // 没添加好友 去添加 为家修行
                        startActivity(new Intent(this, ShareCodeActivity.class));

                    }
                }

                break;

        }
    }

    private void postRenwu(final int renwu_type) {
        /*领取类型(1:签到, 2:每日修行, 3:添加好友, 4:推荐好友, 5:充值兑换)*/
        final Map<String, Object> map = new HashMap<>();
        map.put("customer_id", user.getCustomer_id());
        map.put("type", String.valueOf(renwu_type).toString());
        if (renwu_type == 1) {
            map.put("practice", mMQuestRewardsEntity.getSigninMap().getRewardsValue() + "");
        } else if (renwu_type == 2) {
            map.put("practice", mMQuestRewardsEntity.getEveryDayPracticeMap().getRewardsValue() + "");

        } else if (renwu_type == 3) {
            map.put("practice", mMQuestRewardsEntity.getAddFriendMap().getRewardsValue() + "");

        } else if (renwu_type == 4) {

            map.put("practice", mMQuestRewardsEntity.getReferreMap().getRewardsValue() + "");

        }
        // 领取奖励
        apiImp.postQuestReward(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (renwu_type == 1) {
                    Toast.makeText(QuestRewardsActivity.this, "已领取" + mMQuestRewardsEntity.getEveryDayPracticeMap().getRewardsValue() + "奖励", Toast.LENGTH_SHORT).show();

                } else if (renwu_type == 2) {
                    Toast.makeText(QuestRewardsActivity.this, "已领取" + mMQuestRewardsEntity.getEveryDayPracticeMap().getRewardsValue() + "奖励", Toast.LENGTH_SHORT).show();
                } else if (renwu_type == 3) {
                    Toast.makeText(QuestRewardsActivity.this, "已领取" + mMQuestRewardsEntity.getAddFriendMap().getRewardsValue() + "奖励", Toast.LENGTH_SHORT).show();
                }
                initData();


            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void sign() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        this.apiImp.customerSignIn(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                postRenwu(1);


            }
        });
    }
}
