package com.yuejian.meet.framents.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.adapters.ArticleAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/29.
 */

public class FamousPersonFragment extends BaseFragment implements SpringView.OnFreshListener {
    @Bind(R.id.surname_select)
    TextView surnameSelect;
    @Bind(R.id.age_select)
    TextView ageSelect;
    @Bind(R.id.article_list)
    ListView listView;
    @Bind(R.id.spring_view)
    SpringView springView;

    private final static int SURNAME_SELECT = 1110;
    private List<Article> dataSource = new ArrayList<>();
    private ArticleAdapter adapter = null;
    private int age = 0;
    private int pageIndex = 1;
    private String surname = "全部";

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_mrl, container, false);
    }

    @Override
    protected void initData() {
        adapter = new ArticleAdapter(getContext(), dataSource);
        listView.setAdapter(adapter);
        springView.setListener(this);
        springView.setHeader(new DefaultHeader(getContext()));
        springView.setFooter(new DefaultFooter(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        getArticleList(surname, age);
    }

    private void getArticleList(String surname, int age) {
        final HashMap<String, Object> params = new HashMap<>();
        params.put("article_type", String.valueOf(2));
        params.put("surname", surname);
        params.put("article_age", String.valueOf(age));
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", String.valueOf(16));
        apiImp.getArticleList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Article> articles = JSON.parseArray(data, Article.class);
                if (pageIndex == 1) {
                    dataSource.clear();
                }
                if (articles == null || articles.isEmpty()) {
                    pageIndex--;
                } else {
                    dataSource.addAll(articles);
                }
                adapter.notifyDataSetChanged();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

    }

    @OnClick({R.id.surname_select, R.id.age_select})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.surname_select:
                Intent intent = new Intent(getContext(), SurnameActivity.class);
                if (user != null) {
                    intent.putExtra("meSurname", user.getSurname());
                } else {
                    intent.putExtra("meSurname", surname);
                }
                startActivityForResult(intent, SURNAME_SELECT);
                break;
            case R.id.age_select:
                showPullDownAge();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == SURNAME_SELECT) {
                surname = data.getExtras().getString("surname");
                surnameSelect.setText(surname + " ");
                pageIndex = 1;
                getArticleList(surname, age);
            }
        }
    }

    private PopupWindow pullDownWindow;

    private void showPullDownAge() {
        if (pullDownWindow == null) {
            pullDownWindow = new PopupWindow(getContext());
            pullDownWindow.setWidth(DensityUtils.dip2px(getContext(), 80));
            pullDownWindow.setHeight(DensityUtils.dip2px(getContext(), 120));
            View contentView = View.inflate(getContext(), R.layout.item_famous_person_age_pull_down, null);
            TextView allAge = (TextView) contentView.findViewById(R.id.all_age);
            TextView currentAge = (TextView) contentView.findViewById(R.id.current_age);
            TextView modernAge = (TextView) contentView.findViewById(R.id.modern_age);
            TextView ancientAge = (TextView) contentView.findViewById(R.id.ancient_age);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView view = (TextView) v;
                    String tag = (String) view.getTag();
                    if (tag != null) {
                        try {
                            age = Integer.valueOf(tag);
                            ageSelect.setText(view.getText().toString() + " ");
                            pageIndex = 1;
                            getArticleList(surnameSelect.getText().toString().trim(), age);
                            pullDownWindow.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            allAge.setOnClickListener(listener);
            currentAge.setOnClickListener(listener);
            modernAge.setOnClickListener(listener);
            ancientAge.setOnClickListener(listener);
            pullDownWindow.setContentView(contentView);
            pullDownWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pullDownWindow.setOutsideTouchable(true);
            pullDownWindow.setAnimationStyle(R.style.popmenu_animation);
        }
        if (pullDownWindow.isShowing()) {
            pullDownWindow.dismiss();
        } else {
            pullDownWindow.showAsDropDown(ageSelect);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pullDownWindow != null) {
            pullDownWindow.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getArticleList(surnameSelect.getText().toString().trim(), age);

    }

    @Override
    public void onLoadmore() {
        pageIndex++;
        getArticleList(surnameSelect.getText().toString().trim(), age);
    }
}
