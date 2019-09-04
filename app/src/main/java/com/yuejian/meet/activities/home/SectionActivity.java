package com.yuejian.meet.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.adapters.ArticleAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/25.
 */

public class SectionActivity extends BaseActivity {

    @Bind(R.id.surname_select)
    TextView surnameSelect;
    @Bind(R.id.article_list)
    ListView listView;
    private int articleType = -1;

    private final static int SURNAME_SELECT = 1110;
    private List<Article> dataSource = new ArrayList<>();
    private ArticleAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        setTitleText(getIntent().getStringExtra("articleTitle"));
        articleType = getIntent().getIntExtra("article_type", -1);
        adapter = new ArticleAdapter(this, dataSource);
        listView.setAdapter(adapter);
        getArticleList("全部");
    }

    private void getArticleList(String surname) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("article_type", String.valueOf(articleType));
        params.put("surname", surname);
        apiImp.getArticleList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Article> articles = JSON.parseArray(data, Article.class);
                dataSource.clear();
                dataSource.addAll(articles);
                adapter.notifyDataSetChanged();
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
                startActivityForResult(new Intent(this, SurnameActivity.class), SURNAME_SELECT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == SURNAME_SELECT) {
                String surname = data.getExtras().getString("surname");
                surnameSelect.setText(surname + " ");
                getArticleList(surname);
            }
        }
    }
}
