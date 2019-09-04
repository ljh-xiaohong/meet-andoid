package com.yuejian.meet.activities.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.adapters.ArticleAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.framents.family.FamilyFragment2;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.InnerListView;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/29.
 */

public class HistoryEventActivity extends BaseActivity implements SpringView.OnFreshListener {
    @Bind(R.id.surname_select)
    TextView surnameSelect;
    @Bind(R.id.article_list)
    InnerListView listView;
    @Bind(R.id.surname_list)
    InnerListView surname_list;
    @Bind(R.id.spring_view)
    SpringView springView;
    @Bind(R.id.txt_history_event)
    TextView txt_history_event;
    @Bind(R.id.txt_directories)
    TextView txt_directories;
    @Bind(R.id.web_content)
    WebView webView;

    private final static int SURNAME_SELECT = 1110;
    private List<Article> dataSource = new ArrayList<>();
    private ArticleAdapter adapter = null;
    private int pageIndex = 1;
    private int wikipage = 1;
    int sel_type=1;
    String wiki_surname="全部";
    String history_surname="全部";


    private List<Article> listData = new ArrayList<>();
    private ArticleAdapter surnameAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_event);
//        setTitleText("资讯");
//        adapter = new ArticleAdapter(this, dataSource);
//        listView.setAdapter(adapter);
        springView.setListener(this);
        txt_history_event.setSelected(true);

        //名人录
        surnameAdapter=new ArticleAdapter(this,listData);
        surname_list.setAdapter(surnameAdapter);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        wiki_surname=user.getSurname();
        history_surname=user.getSurname();
        initWidget();
        getSurnName();
    }
    protected void initWidget() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setTextZoom(100);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

//        webView.setWebViewClient(webViewClient);// 新建浏览器客户端，不调用系统浏览器
//        webView.setWebChromeClient(webChromeClient);
//        loadUrl(orginalUrl);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (sel_type==1){
            getArticleList(history_surname);
//        }else {
            requstSurname(wiki_surname);
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FamilyFragment2.ispause=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FamilyFragment2.ispause=false;
    }

    /**
     * 名人录
     * @param surname
     */
    private void requstSurname(String surname) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("surname", surname);
        params.put("pageIndex", String.valueOf(wikipage));
        params.put("pageItemCount", String.valueOf(16));
        apiImp.getSurnameWiKiList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Article> articles = JSON.parseArray(data, Article.class);
                if (wikipage == 1) {
                    listData.clear();
                }
                if (articles == null || articles.isEmpty()) {
                    wikipage--;
                } else {
                    listData.addAll(articles);
                }
                surnameAdapter.notifyDataSetChanged();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

    }
    public void getSurnName(){
        Map<String,Object> params=new HashMap<>();
        params.put("surname",user.getSurname());
        apiImp.getSurNameSource(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    String url = new JSONObject(data).getString("url");
                    url= Utils.t2s(url);
                    webView.loadUrl(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    /**
     * 资讯
     * @param surname
     */
    private void getArticleList(String surname) {
        if (true)
            return;
        HashMap<String, Object> params = new HashMap<>();
        params.put("article_type", String.valueOf(3));
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

    @OnClick({R.id.surname_select,R.id.txt_directories,R.id.txt_history_event})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.surname_select:
                Intent intent = new Intent(this, SurnameActivity.class);
                if (user!=null){
                    intent.putExtra("meSurname",user.getSurname());
                }else {
                    intent.putExtra("meSurname","全部");
                }
                startActivityForResult(intent, SURNAME_SELECT);
                break;
            case R.id.txt_directories:
                sel_type=2;
                setlayoutType();
                break;
            case R.id.txt_history_event:
                sel_type=1;
                setlayoutType();
                break;
        }
    }
    public void setlayoutType(){
        if (sel_type==1){
            surnameSelect.setText(history_surname+" ");
//            listView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
            surname_list.setVisibility(View.GONE);
            txt_directories.setSelected(false);
            txt_history_event.setSelected(true);
        }else {
            surnameSelect.setText(wiki_surname+" ");
//            listView.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            surname_list.setVisibility(View.VISIBLE);
            txt_directories.setSelected(true);
            txt_history_event.setSelected(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == SURNAME_SELECT) {
                String surname = data.getExtras().getString("surname");
                surnameSelect.setText(surname + " ");
                if (sel_type==1){
                    history_surname=surname;
                    pageIndex = 1;
                    getArticleList(surname);
                }else {
                    wikipage=1;
                    wiki_surname=surname;
                    requstSurname(surname);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        if (sel_type==1){
            pageIndex = 1;
            getArticleList(surnameSelect.getText().toString().trim());
        }else {
            wikipage=1;
            requstSurname(surnameSelect.getText().toString().trim());
        }

    }

    @Override
    public void onLoadmore() {
        if (sel_type==1){
            pageIndex++;
            getArticleList(surnameSelect.getText().toString().trim());
        }else {
            wikipage++;
            requstSurname(surnameSelect.getText().toString().trim());
        }
    }
}
