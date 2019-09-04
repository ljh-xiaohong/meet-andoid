package com.yuejian.meet.activities.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.adapters.ArticleAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/29/029.
 */

public class SurnameWikiActivity extends BaseActivity implements SpringView.OnFreshListener {
    @Bind(R.id.surname_select)
    TextView surnameSelect;
    @Bind(R.id.article_list)
    ListView listView;
    @Bind(R.id.spring_view)
    SpringView springView;

    private final static int SURNAME_SELECT = 1110;
    private List<Article> dataSource = new ArrayList<>();
    private ArticleAdapter adapter = null;
    private int pageIndex = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_event);
        setTitleText("姓氏百科");
        adapter = new ArticleAdapter(this, dataSource);
        listView.setAdapter(adapter);
        springView.setListener(this);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        surnameSelect.setText(AppConfig.userEntity == null || StringUtil.isEmpty(AppConfig.userEntity.surname) ? "全部" : AppConfig.userEntity.surname);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getArticleList(surnameSelect.getText().toString().trim());
    }

    private void getArticleList(String surname) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("surname", surname);
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", String.valueOf(16));
        apiImp.getSurnameWiKiList(params, this, new DataIdCallback<String>() {
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

    @OnClick({R.id.surname_select})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.surname_select:
                Intent intent = new Intent(this, SurnameActivity.class);
                if (user != null) {
                    intent.putExtra("meSurname", user.getSurname());
                } else {
                    intent.putExtra("meSurname", "全部");
                }
                startActivityForResult(intent, SURNAME_SELECT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == SURNAME_SELECT) {
                String surname = data.getExtras().getString("surname");
                surnameSelect.setText(surname + " ");
                pageIndex = 1;
                getArticleList(surname);
            }
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getArticleList(surnameSelect.getText().toString().trim());

    }

    @Override
    public void onLoadmore() {
        pageIndex++;
        getArticleList(surnameSelect.getText().toString().trim());
    }
}
