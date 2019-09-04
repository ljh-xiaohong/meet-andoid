package com.yuejian.meet.framents.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.ArticleAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.widgets.SwipeRefreshView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zh02 on 2017/8/24.
 */


//审核中
public class EssayExamineFragment extends BaseFragment {
    @Bind(R.id.swipe_refresh_view)
    SwipeRefreshView swipeRefreshView;
    @Bind(R.id.essay_list)
    ListView essayList;

    private List<Article> dataSource = new ArrayList<>();
    private ArticleAdapter articleAdapter = null;
    private int pageIndex = 1;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_essay, container, false);
    }

    @Override
    protected void initData() {
        super.initData();
        articleAdapter = new ArticleAdapter(getContext(), dataSource);
        essayList.setAdapter(articleAdapter);
        swipeRefreshView.setRefreshing(true);
        swipeRefreshView.setItemCount(16);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getDeliveredArticles();
            }
        });

        swipeRefreshView.setOnLoadMoreListener(new SwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageIndex++;
                getDeliveredArticles();
            }
        });

//        essayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Article article = dataSource.get(position);
//                Intent intent = new Intent(getContext(), WebActivity.class);
//                intent.putExtra(Constants.URL, article.article_url);
//                startActivity(intent);
//            }
//        });
        getDeliveredArticles();
    }

    private void getDeliveredArticles() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("article_status", String.valueOf(0));
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", String.valueOf(16));
        apiImp.getMyArticle(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Article> articles = JSON.parseArray(data, Article.class);
                if (pageIndex == 1) {
                    dataSource.clear();
                    swipeRefreshView.setRefreshing(false);
                } else {
                    swipeRefreshView.setLoading(false);
                }
                if (articles != null) {
                    dataSource.addAll(articles);
                }
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        pageIndex = 1;
        getDeliveredArticles();
    }
}
