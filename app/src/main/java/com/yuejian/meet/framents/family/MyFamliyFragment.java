package com.yuejian.meet.framents.family;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.meritsurname.AddSurnameOriginActivity;
import com.yuejian.meet.activities.meritsurname.MeritRankingActivity;
import com.yuejian.meet.activities.meritsurname.SurnameOriginActivity;
import com.yuejian.meet.activities.meritsurname.SurnameRankActivity;
import com.yuejian.meet.activities.message.NewActionActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.MyfamliyEntity;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.widgets.CustomToast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author :
 * @time : 2018/11/12 15:00
 * @desc : 首页 家族
 * @version: V1.0
 * @update : 2018/11/12 15:00
 */


public class MyFamliyFragment extends BaseFragment {

    @Bind(R.id.my_famliy_tv_surname_list)
    TextView mTvSurnameList;

    @Bind(R.id.my_famliy_tv_merit_list)
    TextView mTvMeritList;
    @Bind(R.id.my_famliy_tv_myname)
    TextView mTvMyname;
    @Bind(R.id.my_famliy_tv_influence_list)
    TextView mMTvInfluenceList;
    @Bind(R.id.my_famliy_tv_allpeople)
    TextView mTvAllpeople;
    @Bind(R.id.my_famliy_tv_people)
    TextView mTvPeople;

    @Bind(R.id.tv_wenhua)
    TextView mTvwenhua;

    @Bind(R.id.my_famliy_ll_surname_cultural)
    RelativeLayout mRlSurnameCultural;

    @Bind(R.id.my_famliy_ll_news)
    LinearLayout mLlNews;
    @Bind(R.id.my_famliy_ll_ethnic_surname)
    LinearLayout mLlEthnicSurname;
    @Bind(R.id.my_famliy_ll_family_tree)
    LinearLayout mLlFamilyTree;

    @Bind(R.id.my_famliy_ll__merit_rank)
    LinearLayout mLlMeritRank;

    @Bind(R.id.my_famliy_ll__surname_rank)
    LinearLayout mLlSurnameRank;


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.activity_my_famliy, container, false);
    }

    @Override
    protected void initData() {
        Map<String, Object> map = new HashMap<>();
        map.put("surname", user.getSurname());
        apiImp.getFamliyData(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                MyfamliyEntity myfamliyEntity = JSON.parseObject(data, MyfamliyEntity.class);
                mTvSurnameList.setText(myfamliyEntity.getRank() + "");
                mTvMeritList.setText(myfamliyEntity.getGd_position() + "");
                mTvMyname.setText(myfamliyEntity.getSurname() + "");
                mMTvInfluenceList.setText(myfamliyEntity.getYxl() + "");
                mTvAllpeople.setText(myfamliyEntity.getPerson_num() + "");
                mTvPeople.setText(myfamliyEntity.getSurname() + "姓入谱人数");
                mTvwenhua.setText(myfamliyEntity.getWenhua());
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });


    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @OnClick({R.id.my_famliy_ll_surname_cultural, R.id.my_famliy_ll_news, R.id.my_famliy_ll_ethnic_surname,
            R.id.my_famliy_ll_family_tree,R.id.my_famliy_ll__merit_rank,R.id.my_famliy_ll__surname_rank})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.my_famliy_ll_surname_cultural:
                // 姓氏文化 到姓氏起源
                startActivity(new Intent(getActivity(), SurnameOriginActivity.class));
                break;
            case R.id.my_famliy_ll_news:
                //新闻动态
                startActivity(new Intent(getActivity(), NewActionActivity.class));

                break;
            case R.id.my_famliy_ll_ethnic_surname:
                // 姓氏族人
                startActivity(new Intent(getActivity(), AddSurnameOriginActivity.class));


                break;  case R.id.my_famliy_ll__merit_rank:
                // 功德排行

                startActivity(new Intent(getActivity(), MeritRankingActivity.class));


                break;
            case R.id.my_famliy_ll__surname_rank:
                // 姓氏人数排行
                if (ImUtils.isLoginIm) {
                    startActivity(new Intent(getActivity(), SurnameRankActivity.class));
                } else {
                    Toast.makeText(mContext, "您尚未登陆", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.my_famliy_ll_family_tree:
                //我的族谱
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("No_Title", true);
                String url = UrlConstant.ExplainURL.STAR_ZPU + "?customer_id=" + AppConfig.CustomerId + "&surname=" + this.user.getSurname();
                intent.putExtra("url", url);
                startActivity(intent);
                break;
        }

    }
}
