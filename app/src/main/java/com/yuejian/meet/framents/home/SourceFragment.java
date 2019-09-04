package com.yuejian.meet.framents.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.adapters.ArticleAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.framents.base.BaseFragment;
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

public class SourceFragment extends BaseFragment implements SpringView.OnFreshListener {

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
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_source, container, false);
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
        getArticleList(surnameSelect.getText().toString().trim());
    }

    private void getArticleList(String surname) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("article_type", String.valueOf(1));
        params.put("surname", surname);
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

    @OnClick({R.id.surname_select})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.surname_select:
                Intent intent = new Intent(getContext(), SurnameActivity.class);
                if (user!=null){
                    intent.putExtra("meSurname",user.getSurname());
                }else {
                    intent.putExtra("meSurname","全部");
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
