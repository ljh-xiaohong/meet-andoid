package com.yuejian.meet.framents.creation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.TypeEntity;
import com.yuejian.meet.framents.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PosterFragment extends BaseFragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    MyFragmentPagerAdapter adapter;

    List<String> titles;
    List<Fragment> fragments;


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_poster, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.fragment_poster_tablayout);
        viewPager = view.findViewById(R.id.fragment_poster_viewpager);

        fragments = new ArrayList<>();
        fragments.add(PosterListFragment.newInstance(null, false));
        titles = new ArrayList<>();
        titles.add("推荐");
        getDataFromNet();

    }

    private void getDataFromNet() {

        Map<String, Object> params = new HashMap<>();
        params.put("type", 3);

        apiImp.getContentLabel(params, this, new DataIdCallback<String>() {

            List<TypeEntity> typeEntities;

            @Override
            public void onSuccess(String data, int id) {

                if (checkIsLife()) return;
                if (data != null) {
                    JSONObject jo = (JSONObject) JSON.parse(data);
                    String code = jo.getString("code");
                    if (code != null && code.equalsIgnoreCase("0")) {


                        typeEntities = JSON.parseArray(jo.getString("data"), TypeEntity.class);
                        if (typeEntities != null && typeEntities.size() > 0) {

                            for (TypeEntity entity : typeEntities) {
                                titles.add(entity.getTitle());
                                fragments.add(PosterListFragment.newInstance(entity, true));
                            }

                            adapter = new MyFragmentPagerAdapter(getFragmentManager(), fragments, titles);
                            viewPager.setAdapter(adapter);
                            tabLayout.setupWithViewPager(viewPager);
                            viewPager.setOffscreenPageLimit(typeEntities.size());

                        }


                    }
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {


            }
        });
    }


}
