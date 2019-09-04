package com.yuejian.meet.framents.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.FamilySourceActivity;
import com.yuejian.meet.activities.home.HistoryEventActivity;
import com.yuejian.meet.activities.mine.CreateArticleActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.ArticleAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.bean.CustomerInfo;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.XListView;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 文化
 */
public class CultureFragment extends BaseFragment {

    @Bind(R.id.article_list)
    XListView listView;
    @Bind(R.id.spring_view)
    SpringView springView;
    @Bind(R.id.fbwz)
    View fbwz;
    private View topLayout;

    private List<Article> dataSource = new ArrayList<>();
    private ArticleAdapter adapter = null;
    private int pageIndex = 1;
    private View banner = null;
    TextView surnameTitle;
    private CustomerInfo customerInfo;
    private long clickTime;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_culture, container, false);
    }

    @Override
    protected void initData() {
        findCustomerId(AppConfig.CustomerId);
        View headView = View.inflate(getContext(), R.layout.culture_head_layout, null);
        listView.addHeaderView(headView);
        headView.findViewById(R.id.zixun).setOnClickListener(this);
        headView.findViewById(R.id.genyuan).setOnClickListener(this);
        headView.findViewById(R.id.clan).setVisibility(View.VISIBLE);
        headView.findViewById(R.id.zupu).setVisibility(View.VISIBLE);
        headView.findViewById(R.id.clan).setOnClickListener(this);
        headView.findViewById(R.id.zupu).setOnClickListener(this);
        if (PreferencesUtil.readBoolean(getActivity().getApplicationContext(), Constants.UNDONE_SWITCH)) {
            headView.findViewById(R.id.jijin).setVisibility(View.VISIBLE);
            headView.findViewById(R.id.jijin).setOnClickListener(this);
        } else {
            headView.findViewById(R.id.jijin).setVisibility(View.INVISIBLE);
        }
        banner = headView.findViewById(R.id.banner);
        surnameTitle = (TextView) headView.findViewById(R.id.surname_title);
        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/JDJXINGKAI.ttf");
        surnameTitle.setTypeface(typeFace);
        adapter = new ArticleAdapter(getContext(), dataSource);
        listView.setAdapter(adapter);
        springView.setHeader(new DefaultHeader(getContext()));
        springView.setFooter(new DefaultFooter(getContext()));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getArticleList();
            }

            @Override
            public void onLoadmore() {
                pageIndex++;
                getArticleList();
            }
        });
        getArticleList();
        setSurnamTitle();
        topLayout = getRootView().findViewById(R.id.top_layout);
        topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - clickTime) < 2000) {
                    listView.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.smoothScrollToPosition(0);
                        }
                    });
                } else {
                    clickTime = currentTime;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user == null) {
            fbwz.setVisibility(View.GONE);
        } else {
            fbwz.setVisibility(View.VISIBLE);
        }
    }

    private void findCustomerId(final String customerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("latitude", String.valueOf(AppConfig.slatitude));
        params.put("longitude", String.valueOf(AppConfig.slongitude));
        if (user != null) {
            params.put("my_customer_id", user.customer_id);
        }
        apiImp.findCustomerInfo(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                customerInfo = JSON.parseObject(data, CustomerInfo.class);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });

    }

    private void getSurNameSource(String surname) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("surname", surname);
        apiImp.getSurNameSource(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    String url = new JSONObject(data).getString("url");
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra(Constants.URL, url);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @OnClick({R.id.fbwz, /*R.id.genyuan, R.id.shijian*/})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fbwz:
                showMenuListDialog();
                break;
            case R.id.genyuan:
                startActivity(new Intent(getContext(), FamilySourceActivity.class));
                break;
            case R.id.zixun:
                startActivity(new Intent(getContext(), HistoryEventActivity.class));
                break;
            case R.id.clan:
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                if (customerInfo == null) {
                    findCustomerId(AppConfig.CustomerId);
                    return;
                }
                Intent intent = new Intent(getContext(), WebActivity.class);
                String url = UrlConstant.apiUrl() + "clanRelatives/index.html?"
                        + "customer_id=" + customerInfo.customer_id
                        + "&surname=" + customerInfo.surname
                        + "&area_name=" + customerInfo.full_area_name;
                url = url.replace("https", "http");
                intent.putExtra(Constants.URL, url);
                intent.putExtra(Constants.NO_TITLE_BAR, true);
                startActivity(intent);
                break;
            case R.id.zupu:
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                if (customerInfo == null) {
                    findCustomerId(AppConfig.CustomerId);
                    return;
                }
                intent = new Intent(getContext(), WebActivity.class);
                url = UrlConstant.apiUrl() + "zupu/index.html"
                        + "?customer_id=" + customerInfo.customer_id
                        + "&surname=" + customerInfo.surname
                        + "&area_name=" + customerInfo.full_area_name;
                url = url.replace("https", "http");
                intent.putExtra(Constants.URL, url);
                intent.putExtra(Constants.NO_TITLE_BAR, true);
                startActivity(intent);
                break;
            case R.id.jijin:
                if (customerInfo == null) {
                    findCustomerId(AppConfig.CustomerId);
                    return;
                }
                if (customerInfo == null) {
                    findCustomerId(AppConfig.CustomerId);
                    return;
                }
                intent = new Intent(getContext(), WebActivity.class);
                url = UrlConstant.apiUrl() + "api/fund/fundIndex?my_customer_id=" + (customerInfo == null || StringUtils.isEmpty(customerInfo.customer_id) ? "" : customerInfo.customer_id);
                url = url.replace("https", "http");
                intent.putExtra(Constants.URL, url);
                startActivity(intent);
                break;
        }
    }

    private void showMenuListDialog() {
        View inflate = View.inflate(getContext(), R.layout.layout_essay_menu_list, null);
        final AlertDialog dialog = new AlertDialog
                .Builder(getContext())
                .setView(inflate).create();
        TextView mrl = (TextView) inflate.findViewById(R.id.mrl);
        mrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(getContext(), CreateArticleActivity.class);
                i.putExtra("create_type", 2);
                startActivity(i);
            }
        });
        TextView xmqy = (TextView) inflate.findViewById(R.id.xmqy);
        xmqy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(getContext(), CreateArticleActivity.class);
                i.putExtra("create_type", 1);
                startActivity(i);
            }
        });
        TextView sj = (TextView) inflate.findViewById(R.id.sj);
        sj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(getContext(), CreateArticleActivity.class);
                i.putExtra("create_type", 3);
                startActivity(i);
            }
        });
        dialog.show();
    }

    private void getArticleList() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("article_type", String.valueOf(0));
        params.put("surname", "全部");
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", String.valueOf(16));
        if (AppConfig.userEntity != null && StringUtils.isNotEmpty(AppConfig.userEntity.surname)) {
            params.put("surname", AppConfig.userEntity.surname);
        }
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

    @Override
    public void receiverBus(String event) {
        super.receiverBus(event);
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        setSurnamTitle();
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (BusEnum.LOGIN_UPDATE == event.getCallType() || BusEnum.LOGOUT == event.getCallType()) {
            findCustomerId(AppConfig.CustomerId);
        }
    }

    @Override
    protected void onFirstUserVisible() {
        setSurnamTitle();
    }

    private void setSurnamTitle() {
        if (user == null) {
            surnameTitle.setText("百家姓");
            surnameTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtils.dip2px(getContext(), 45));
            banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://wapbaike.baidu.com/item/百家姓";
                    Intent intent = new Intent(getContext(), WebActivity.class);
                    intent.putExtra(Constants.URL, url);
                    startActivity(intent);
                }
            });
        } else {
            if (user.surname.length() == 2) {
                surnameTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtils.dip2px(getContext(), 51));
            } else {
                surnameTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, DensityUtils.dip2px(getContext(), 59));
            }
            surnameTitle.setText(user.surname);
            banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSurNameSource(user.surname);
                }
            });
        }
    }
}
