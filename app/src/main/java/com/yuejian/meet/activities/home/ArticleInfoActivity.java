package com.yuejian.meet.activities.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.UrlApi;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.ExpenseEntity;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.netease.nim.uikit.app.widgets.GiftDialogFragment;
import com.netease.nim.uikit.common.ui.imageview.CircleImageView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.adapters.ArticleCommentAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Article;
import com.yuejian.meet.bean.CommentsEntity;
import com.yuejian.meet.bean.DelActionInfoEntity;
import com.yuejian.meet.bean.PraiseState;
import com.yuejian.meet.bean.Reward;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.ArticleCommentFactory;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.ObservableScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/25.
 */

public class ArticleInfoActivity extends BaseActivity {
    @Bind(R.id.comment_list)
    ListView listView;
    @Bind(R.id.web_content)
    WebView webView;
    @Bind(R.id.author_name)
    TextView authorName;
    @Bind(R.id.author_icon)
    ImageView authorIcon;
    @Bind(R.id.reward_cnt)
    TextView rewardCnt;
    @Bind(R.id.reward_icon_layout)
    LinearLayout rewordIconsLayout;
    @Bind(R.id.create_time)
    TextView createTime;
    @Bind(R.id.comment_cnt)
    TextView commentCnt;
    @Bind(R.id.msg_send)
    Button msg_send;
    @Bind(R.id.msg_content)
    EditText msg_content;
    @Bind(R.id.article_scroll_view)
    ObservableScrollView scrollView;
    @Bind(R.id.like_cnt)
    TextView likeCnt;
    @Bind(R.id.add_focus)
    Button addFocus;
    @Bind(R.id.faqr)
    ImageView faqr;
    @Bind(R.id.article_info_layout)
    LinearLayout article_info_layout;
    TextView dialog_del, dialog_reply;

    LinearLayout commentLayout = null;

    String action_id = "1", comment_id = "1", costomerId = "", commentName = "", content = "";
    Boolean isAction = true;
    private View mPoupView = null, mHintView = null;
    private PopupWindow mPoupWindow = null, mHintWindow = null;
    LoadingDialogFragment dialog;

    public final static String JS_TO_LOAD_IMAGE = "<script></script>";
    private Article article = null;
    private ArticleCommentAdapter adapter = null;
    private List<CommentsEntity> dataSource = new ArrayList<>();
    private int pageIndex = 1;
    private int orderIndex = 0;
    private String listLastId = "0";
    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;

    private static String[] TITLES = {"看看自己姓氏的由来",
            "这些姓氏是怎么来的，你知道吗？",
            "百家姓：你知道每个姓氏代表着什么吗?",
            "你知道自己姓氏的祖先来自哪里吗？",
            "姓东郭什么的都弱爆了，这几个姓氏你听过吗？",
            "你的祖上当过皇帝？看看中国古代皇帝姓氏",
            "华裔女演员改姓氏，背后原因让人吃惊！",
            "中国姓氏趣味多，东西南北、上下左右皆为姓",
            "姓氏溯源，你姓氏的变迁知多少？",
            "从姓氏中倾听你家族的故事"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        dialog = LoadingDialogFragment.newInstance("正在操作..");
        article = (Article) getIntent().getSerializableExtra("article");
        if (article == null) finish();
        MobclickAgent.onPageStart(article.article_title);
        adapter = new ArticleCommentAdapter(listView, dataSource, R.layout.item_action_info_layout);
        listView.setAdapter(adapter);
        msg_send.setSelected(true);
        commentLayout = (LinearLayout) findViewById(R.id.comment_layout);
        scrollView.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//                if (scrollView.getScrollY() == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
//                    isScrolledToTop = true;
//                    isScrolledToBottom = false;
//                } else if (
//                        scrollView.getScrollY()
//                                + scrollView.getHeight()
//                                - scrollView.getPaddingTop()
//                                - scrollView.getPaddingBottom()
//                                == scrollView.getChildAt(0).getHeight()) {
//                    // 小心踩坑2: 这里不能是 >=
//                    // 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
//                    isScrolledToBottom = true;
//                    isScrolledToTop = false;
//                } else {
//                    isScrolledToTop = false;
//                    isScrolledToBottom = false;
//                }
//                scrollView.isNeedToIntercept(false);
//                if (isScrolledToTop) {
//                    //do when it top
//                } else if (isScrolledToBottom) {
//                    //do when it bottom
//                    scrollView.isNeedToIntercept(false);
//                }
            }

            @Override
            public void onScrollDistance(int distance) {
                if (distance == scrollView.getTotalScrollDistance()) {
                    scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                }
            }
        });
        msg_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (totalItemCount == firstVisibleItem + visibleItemCount + 1) {
//                    scrollView.isNeedToIntercept(false);
//                }
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    scrollView.isNeedToIntercept(false);
                }
            }
        });
        initWebView();
    }

    private void getArticle(String aId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("article_id", aId);
        if (user != null) {
            params.put("my_customer_id", user.customer_id);
        }
        apiImp.getArticle(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                article = JSON.parseObject(data, Article.class);
                getArticleCommentsList();
                initView();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

    }

    private void initView() {

        faqr.setVisibility(View.INVISIBLE);
        if (article.getIs_super() > 0) {
            faqr.setVisibility(View.VISIBLE);
            Glide.with(getApplication()).load(article.is_super == FqrEnum.city.getValue() ? R.mipmap.ic_shi : article.is_super == FqrEnum.province.getValue() ? R.mipmap.ic_sheng : R.mipmap.ic_guo).asBitmap().into(faqr);
        }
        Glide.with(this).load(article.photo).asBitmap().placeholder(R.mipmap.ic_default).into(authorIcon);
        authorName.setText(article.c_surname + article.c_name);
        createTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(Long.valueOf(article.create_time))));
        if ("1".equals(article.is_praise)) {
            likeCnt.setSelected(true);
        }

        likeCnt.setText(" " + article.article_praise_cnt);

        rewordIconsLayout.removeAllViews();
        if (article.reward_list == null || article.reward_list.isEmpty()) {
            ((View) rewordIconsLayout.getParent()).setVisibility(View.GONE);
        } else {
            ((View) rewordIconsLayout.getParent()).setVisibility(View.VISIBLE);
            for (Reward reward : article.reward_list) {
                if (rewordIconsLayout.getChildCount() > 6) {
                    break;
                }
                CircleImageView imageView = new CircleImageView(this);
                imageView.setBorderColor(Color.TRANSPARENT);
                imageView.setBorderWidth(1);
                int size = DensityUtils.dip2px(this, 30);
                int margin = DensityUtils.dip2px(this, 2);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
                layoutParams.setMargins(0, 0, margin, 0);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                rewordIconsLayout.addView(imageView, layoutParams);
                Glide.with(this).load(reward.photo).into(imageView);
            }
        }

        rewardCnt.setText("等 " + article.reward_list.size() + " 人 >");
        addFocus.setVisibility(user == null ? View.GONE : View.VISIBLE);

        if ("1".equals(article.relation_type) || "2".equals(article.relation_type)) {
            addFocus.setSelected(true);
            addFocus.setText("已关注");
        } else if ("0".equals(article.relation_type)) {
            addFocus.setSelected(true);
            addFocus.setText("加关注");
            addFocus.setSelected(false);
        } else if ("3".equals(article.relation_type)) {
            addFocus.setVisibility(View.GONE);
        }

        commentCnt.setText("评论 " + article.article_comment_cnt);
        findViewById(R.id.send_msg_layout).setVisibility(user == null ? View.GONE : View.VISIBLE);
        msg_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    msg_content.setText("");
                    msg_content.setHint("说点什么吧...");
                    isComment = true;
                }
            }
        });

        int visible = "0".equals(article.article_status) ? View.GONE : View.VISIBLE;
        findViewById(R.id.share_more).setVisibility(visible);
        findViewById(R.id.weixin_icon).setVisibility(visible);
        findViewById(R.id.pyq_icon).setVisibility(visible);
    }

    private void getArticleCommentsList() {
        if (article == null) return;
        HashMap<String, Object> params = new HashMap<>();
        params.put("article_id", article.article_id);
        if (user != null) {
            params.put("my_customer_id", user.customer_id);
        }
        params.put("order_index", String.valueOf(orderIndex));
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", String.valueOf(200));
        params.put("listLastId", pageIndex == 1 ? "0" : listLastId);
        apiImp.getArticleCommentsList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    if (pageIndex == 1) {
                        dataSource.clear();
                        commentLayout.removeAllViews();
                    }
                    JSONObject dataJson = new JSONObject(data);
                    String commentList = dataJson.getString("comment_list");
                    List<CommentsEntity> commentsEntities = JSON.parseArray(commentList, CommentsEntity.class);
                    if (commentsEntities == null || commentsEntities.isEmpty()) {
//                        pageIndex--;
                        if (commentsEntities == null) {
                            commentsEntities = new ArrayList<CommentsEntity>();
                        }
                    } else {
//                        listLastId = commentsEntities.get(commentsEntities.size() - 1).getArticle_comment_id();
                    }
                    dataSource.addAll(commentsEntities);
//                    adapter.refresh(dataSource);
//                    Utils.setListViewHeightBasedOnChildren(getBaseContext(), listView, 0);
                    for (CommentsEntity entity : commentsEntities) {
                        commentLayout.addView(ArticleCommentFactory.createItem(ArticleInfoActivity.this, entity, dataSource.indexOf(entity)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private boolean isComment = true;

    @OnClick({R.id.fanhui, R.id.msg_send, R.id.author_layout, R.id.share_more, R.id.add_focus, R.id.like_cnt})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                finish();
                break;
            case R.id.msg_send:
                if (isComment) {
                    comment();
                } else {
                    reply();
                }
                break;
            case R.id.author_layout:
                if (user == null) {
                    return;
                }
                AppUitls.goToPersonHome(mContext, article.customer_id);
                break;
            case R.id.share_more:
                Glide.with(getBaseContext()).load(article.article_photo).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        String title = "";
                        if ("1".equals(article.article_type)) {
                            title = TITLES[(int) (Math.random() * 9)];
                        } else {
                            title = article.article_title;
                        }
                        String shareUrl = UrlApi.h5HttpUrl+"article_share/iframe.html?url=http://app2.yuejianchina.com/yuejian-app/article.html?article_id=" + article.article_id;
                        Utils.umengShareByList(ArticleInfoActivity.this, resource, title, "来自 约见·百家姓 的文章", shareUrl);
                    }
                });
                break;
            case R.id.add_focus:
                focusCustomer(user.customer_id, article.customer_id, Constants.ADD_FOCUS);
                break;
            case R.id.like_cnt:
                praiseArticle(article.article_id);
                break;
            case R.id.dialog_cancel://关闭dialog
                mPoupWindow.dismiss();
                break;
            case R.id.dialog_del://删除评论动态
                mPoupWindow.dismiss();
                showHintWindow();
                break;
            case R.id.dialog_copy://复制评论动态
                mPoupWindow.dismiss();
                // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(content);
                ViewInject.toast(getApplicationContext(), "复制成功");
                break;
            case R.id.dialog_reply://回复评论动态
                mPoupWindow.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        commentMsg(comment_id, isAction, commentName, index);
                    }
                }, 500);
                break;
            case R.id.dialog_del_commetn://删除评论
                mHintWindow.dismiss();
                delComment();
                break;
            case R.id.dialog_hint_cancel://关闭提示
                mHintWindow.dismiss();
                break;
        }
    }

    public void onShare(View v) {
        if ("0".equals(article.article_status)) {
            return;
        }
        switch (v.getId()) {
            case R.id.weixin_icon:
                Glide.with(getBaseContext()).load(article.article_photo).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        String title = "";
                        if ("1".equals(article.article_type)) {
                            title = TITLES[(int) (Math.random() * 9)];
                        } else {
                            title = article.article_title;
                        }
                        String shareUrl = UrlApi.h5HttpUrl+"article_share/iframe.html?url=http://app2.yuejianchina.com/yuejian-app/article.html?article_id=" + article.article_id;
                        Utils.umengShareForPhatForm(
                                SHARE_MEDIA.WEIXIN,
                                ArticleInfoActivity.this, resource,
                                title, "来自 约见·百家姓 的文章",
                                shareUrl);
                    }
                });
                break;
            case R.id.pyq_icon:
                Glide.with(getBaseContext()).load(article.article_photo).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        String title = "";
                        if ("1".equals(article.article_type)) {
                            title = TITLES[(int) (Math.random() * 9)];
                        } else {
                            title = article.article_title;
                        }
                        String shareUrl = UrlApi.h5HttpUrl+"article_share/iframe.html?url=http://app2.yuejianchina.com/yuejian-app/article.html?article_id=" + article.article_id;
                        Utils.umengShareForPhatForm(
                                SHARE_MEDIA.WEIXIN_CIRCLE,
                                ArticleInfoActivity.this, resource,
                                title, "来自 约见·百家姓 的文章",
                                shareUrl);
                    }
                });
                break;
            default:
                break;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("article", article.article_title);
        MobclickAgent.onEvent(this, "ArticleShare", map);
    }

    public void onSortByHot(View view) {
        TextView textView = (TextView) view;

        if (orderIndex == 1) {
            orderIndex = 0;
            textView.setText("按时间");
        } else {
            orderIndex = 1;
            textView.setText("按热度");
        }
        pageIndex = 1;
        getArticleCommentsList();
    }

    public void onReward(View view) {
        Utils.hintKbTwo(this);
        sendGift();
        HashMap<String, String> map = new HashMap<>();
        map.put("article", article.article_title);
        MobclickAgent.onEvent(this, "ArticleReward", map);
    }

    public void goToRewardList(View view) {
//        Intent intent = new Intent(this, RewardListActivity.class);
//        intent.putExtra("articleId", article.article_id);
//        intent.putExtra("customer_id", article.customer_id);
//        startActivity(intent);
    }

    @SuppressLint("AddJavascriptInterface")
    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setBlockNetworkImage(false);
        settings.setAllowFileAccess(true);
        settings.setSupportMultipleWindows(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new MyWebClient());// 新建浏览器客户端，不调用系统浏览器
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress >= 100) {
                }
            }
        });
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        webView.loadUrl(article.article_url);
        webView.loadUrl("javascript:alert(injectedObject.toString())");
        loadingDialog.show(getFragmentManager(), "");
        loadingDialog.setCancelable(false);
    }

    private LoadingDialogFragment loadingDialog = LoadingDialogFragment.newInstance("正在加载...");

    public class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("img.html?img=")) {
                String src = Utils.getValueByName(url, "img");
                ArrayList<String> urls = new ArrayList<>();
                urls.add(src);
                Utils.displayImages(ArticleInfoActivity.this, urls, 0, null);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loadingDialog.dismiss();
            pickWebImage();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            loadingDialog.dismiss();
        }
    }

    private void pickWebImage() {
        if (webView == null) {
            return;
        }
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.text = function starup(){" +
                "for(var i=0;i<document.getElementsByTagName(\"img\").length;i++){\ndocument.getElementsByTagName(\"img\")[i].addEventListener(\"click\",function(){window.location.href=\"img.html?img=\"+this.src;});}" +
                "};";
        js += "document.body.appendChild(newscript);";
        webView.loadUrl("javascript:" + js);
        webView.loadUrl("javascript:window.starup()");
        webView.loadUrl("javascript:window.java_obj.pickImage(document.getElementsByTagName('html')[0].innerHTML)");
    }

    /**
     * 底部PopupWindow
     */
    public void showBottomPopupWindow(String content, String costomerId, String comment_id, Boolean isAction, String commentName, int index) {
        this.content = content;
        this.costomerId = costomerId;
        this.index = index;
        this.isAction = isAction;
        this.comment_id = comment_id;
        this.commentName = commentName;

        if (mPoupView == null) {
            mPoupView = View.inflate(this, R.layout.action_dialog_layout, null);
            bindPopMenuEvent(mPoupView);
            mPoupWindow = new PopupWindow(mPoupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPoupWindow.setAnimationStyle(R.style.PopupAnimation);
            mPoupWindow.setTouchable(true);
            mPoupWindow.setFocusable(true);
            mPoupWindow.setOutsideTouchable(true);
            mPoupWindow.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            mPoupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
            ColorDrawable dw = new ColorDrawable(0x90000000);
            mPoupWindow.setBackgroundDrawable(dw);
        }
        if (costomerId.equals(AppConfig.CustomerId)) {
            dialog_reply.setVisibility(View.GONE);
            dialog_del.setVisibility(View.VISIBLE);
        } else {
            dialog_del.setVisibility(View.GONE);
            dialog_reply.setVisibility(View.VISIBLE);
        }
        mPoupWindow.showAtLocation(article_info_layout, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
    }

    /**
     * 提示删除
     */
    public void showHintWindow() {
        if (mHintView == null) {
            mHintView = View.inflate(this, R.layout.action_dialog_del_hint_layout, null);
            mHintWindow = new PopupWindow(mHintView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mHintWindow.setAnimationStyle(R.style.PopupAnimation);
            mHintWindow.setTouchable(true);
            mHintWindow.setFocusable(true);
            mHintWindow.setOutsideTouchable(true);
            mHintWindow.setTouchInterceptor(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            mHintWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
            ColorDrawable dw = new ColorDrawable(0x90000000);
            mHintWindow.setBackgroundDrawable(dw);
            mHintView.findViewById(R.id.dialog_del_commetn).setOnClickListener(this);
            mHintView.findViewById(R.id.dialog_hint_cancel).setOnClickListener(this);
        }
        mHintWindow.showAtLocation(article_info_layout, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
    }

    /**
     * 实例化底部pop菜单项
     *
     * @param view
     */
    private void bindPopMenuEvent(View view) {
        view.findViewById(R.id.dialog_cancel).setOnClickListener(this);
        dialog_del = (TextView) view.findViewById(R.id.dialog_del);
        view.findViewById(R.id.dialog_copy).setOnClickListener(this);
        dialog_reply = (TextView) view.findViewById(R.id.dialog_reply);
        dialog_reply.setOnClickListener(this);
        dialog_del.setOnClickListener(this);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 删除文章评论
     */
    public void delComment() {
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", comment_id);
        params.put("customer_id", AppConfig.CustomerId);
        apiImp.postArticleDelComment(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog != null)
                    dialog.dismiss();
                getArticle(article.article_id);
                isComment = true;
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    private int index;
    private String msgId;

    public void commentMsg(String id, Boolean isAction, String commentName, int index) {
        if (user == null) {
            return;
        }
        msgId = id;
        this.index = index;
        isComment = isAction;
        msg_content.setText("");
        msg_content.setHint(StringUtils.isEmpty(commentName) ? "评论" : "回复@" + commentName);
        msg_content.requestFocus();
        Utils.showKB(msg_content, this);
    }

    public void comment() {
        if (user == null) return;
        String comment = msg_content.getText().toString();
        if (StringUtils.isEmpty(comment)) {
            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (comment.length() > 5000) {
            Toast.makeText(mContext, "评论内容不得超过500字", Toast.LENGTH_SHORT).show();
            return;
        }
        findViewById(R.id.msg_send).setEnabled(false);
        Map<String, Object> params = new HashMap<>();
        params.put("article_id", article.article_id);
        params.put("customer_id", user.customer_id);
        params.put("comment_content", comment);
        findViewById(R.id.msg_send).setEnabled(false);
        apiImp.commentArticle(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                msg_content.setText("");
                Utils.hintKbTwo((Activity) mContext);
                getArticle(article.article_id);
                isComment = true;
                findViewById(R.id.msg_send).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.msg_send).setEnabled(true);
                    }
                }, 1000);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                findViewById(R.id.msg_send).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.msg_send).setEnabled(true);
                    }
                }, 1000);
            }
        });
    }

    public void reply() {
        String content = msg_content.getText().toString();
        if (StringUtils.isEmpty(content)) {
            Toast.makeText(mContext, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (content.length() > 5000) {
            Toast.makeText(mContext, "回复内容不得超过500字", Toast.LENGTH_SHORT).show();
            return;
        }
        findViewById(R.id.msg_send).setEnabled(false);
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", msgId);
        params.put("customer_id", user.customer_id);
        params.put("comment_content", content);
        apiImp.replyArticle(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Log.d("reply", data);
                Utils.hideSystemKeyBoard(getBaseContext(), msg_content);
                getArticle(article.article_id);
                listView.setSelection(index);
                isComment = true;
                msg_content.setText("");
                msg_content.setHint("说点什么吧...");
                findViewById(R.id.msg_send).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.msg_send).setEnabled(true);
                    }
                }, 1000);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                findViewById(R.id.msg_send).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.msg_send).setEnabled(true);
                    }
                }, 1000);
            }
        });
    }

    private void focusCustomer(String customerId, String opCustomerId, int bindType) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("op_customer_id", opCustomerId);
        params.put("bind_type", String.valueOf(bindType));
        apiImp.bindRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                addFocus.setSelected(true);
                addFocus.setText("已关注");
                addFocus.setEnabled(true);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    private void praiseArticle(String articleId) {
        if (user == null) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("article_id", articleId);
        params.put("customer_id", user.customer_id);
        apiImp.getArticlePraise(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                PraiseState state = JSON.parseObject(data, PraiseState.class);
                if (state != null) {
                    likeCnt.setSelected("1".equals(state.is_praise));
                    likeCnt.setText(state.praise_cnt);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        article = (Article) getIntent().getSerializableExtra("article");
        if (article == null) finish();
        getArticle(article.article_id);
        getArticleCommentsList();
    }

    private GiftDialogFragment editNameDialog = null;

    private void sendGift() {
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        if (editNameDialog == null) {
            editNameDialog = new GiftDialogFragment();
        }
        final PopupWindow popupWindow = editNameDialog.showBottomPopupWindow(this, getWindow().getDecorView());
        editNameDialog.setOnSendGiftLister(new GiftDialogFragment.OnSendGiftListener() {
            @Override
            public void sendGift(final GiftAllEntity giftBean) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", user.customer_id);
                params.put("op_customer_id", article.customer_id);
                params.put("gift_id", giftBean.getId());
                params.put("gift_count", giftBean.getCount());
                params.put("gift_expense_type", "2");//送礼类型(1.动态礼物,2.文章礼物,3.私聊礼物,4.视频礼物)
                params.put("object_id", article.article_id);
                new NetApi().sendGift(params, this, new DataCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ExpenseEntity expenseEntity = JSON.parseObject(data, ExpenseEntity.class);
                        MonyEntity monyEntity = JSON.parseObject(expenseEntity.getBal(), MonyEntity.class);
                        UtilsIm.setMyMoney(getApplication(), monyEntity);
                        getArticle(article.article_id);
                    }

                    @Override
                    public void onSuccess(String data, int id) {

                    }

                    @Override
                    public void onFailed(String errCode, String errMsg) {
                        Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {
            pageIndex = 1;
        }
    }

    @Override
    protected void onDestroy() {
        if (editNameDialog != null && editNameDialog.isVisible()) {
            editNameDialog.dismiss();
        }
        if (webView != null) {
            webView.getSettings().setBuiltInZoomControls(false);
            webView.setVisibility(View.GONE);
            long timeout = ViewConfiguration.getZoomControlsTimeout();//timeout ==3000
            webView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (webView != null) {
                        webView.destroy();
                    }
                }
            }, timeout);
        }
        super.onDestroy();
    }

    private final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void pickImage(String src) {
            Log.d("html", src);
        }
    }
}
