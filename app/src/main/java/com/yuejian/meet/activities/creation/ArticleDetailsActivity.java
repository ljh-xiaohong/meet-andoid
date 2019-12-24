package com.yuejian.meet.activities.creation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nim.uikit.api.UrlApi;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.DiscussInnerAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ArticleContentEntity;
import com.yuejian.meet.bean.ReleaseContentEntity;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CircleImageView;
import com.yuejian.meet.widgets.DiscussView;
import com.yuejian.meet.widgets.MyObservableScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author : g000gle
 * @time : 2019/6/26 17:26
 * @desc : 文章详情界面
 */
public class ArticleDetailsActivity extends BaseActivity implements TextView.OnEditorActionListener, DiscussView.OnDiscussItemClickListener {

    @Bind(R.id.iv_edit_article_back_btn)
    ImageView mBackBtn;
    @Bind(R.id.civ_article_details_header)
    CircleImageView mHeaderView;
    @Bind(R.id.tv_article_details_name)
    TextView mNameView;
    @Bind(R.id.tv_article_details_date)
    TextView mDateView;
    @Bind(R.id.tv_article_details_comp)
    TextView mCompView;
    @Bind(R.id.tv_article_details_follow)
    TextView mFollowBtn;
    @Bind(R.id.ll_article_details_content_root)
    LinearLayout mContentRootView;
    @Bind(R.id.iv_article_details_ad_img)
    ImageView mAdImgView;
    @Bind(R.id.tv_article_details_ad_title)
    TextView mAdTitleView;
    @Bind(R.id.tv_article_details_ad_content)
    TextView mAdContentView;
    @Bind(R.id.bt_article_details_zan)
    View bt_zan;
    @Bind(R.id.bt_article_details_discuss)
    View bt_discuss;
    @Bind(R.id.bt_article_details_share)
    View bt_share;
    @Bind(R.id.ll_article_details_discuss_root)
    LinearLayout mDisussRootView;
    @Bind(R.id.tv_article_details_zan)
    TextView tv_Like;
    @Bind(R.id.ed_edit_discuss)
    EditText ed_discuss;
    @Bind(R.id.sv_srollview)
    MyObservableScrollView sv;
    Drawable leftIcon_like, leftIcon_unlike;

    private int mDetailsId;
    private int authorId;
    private boolean is_bind;
    private int is_praise;

    private static final int COMENT_COMENT = 0;
    private static final int REPLY_COMENT = 1;

    private String Reply_id = "";

    //用于判断是否是评论的回复；
    private int index = COMENT_COMENT;

    ReleaseContentEntity contentEntity;
    InputMethodManager imm;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mDetailsId = getIntent().getIntExtra("id", -1);
        initData();
    }


    private void initData() {
        ed_discuss.setFocusable(false);
        ed_discuss.setFocusableInTouchMode(false);
        ed_discuss.setOnEditorActionListener(this);
        leftIcon_like = getResources().getDrawable(R.mipmap.icon_pingjia_zan_sel);
//        leftIcon_like.setBounds(0, 0, leftIcon_like.getIntrinsicWidth(), leftIcon_like.getIntrinsicHeight());

        leftIcon_unlike = getResources().getDrawable(R.mipmap.icon_pingjia_zan_nor);
//        leftIcon_unlike.setBounds(0, 0, leftIcon_unlike.getIntrinsicWidth(), leftIcon_unlike.getIntrinsicHeight());
        ;
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("id", String.valueOf(mDetailsId));
        apiImp.getContentRelease(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                contentEntity = JSON.parseObject(data, ReleaseContentEntity.class);
                authorId = contentEntity.creation.customer_id;
                is_bind = contentEntity.creation.is_bind;
                is_praise = contentEntity.creation.is_praise;

                Glide.with(ArticleDetailsActivity.this).load(contentEntity.creation.photo).into(mHeaderView);
                mNameView.setText(contentEntity.creation.name);
                String time = TimeUtils.formatDateTime(new Date(contentEntity.creation.create_time));
                mDateView.setText(time);
                mCompView.setText(contentEntity.creation.job);
                mFollowBtn.setText(is_bind ? "取消关注" : "+ 关注");
                mFollowBtn.setVisibility(authorId == Integer.parseInt(user.customer_id) ? View.GONE : View.VISIBLE);
                mFollowBtn.setOnClickListener(v -> follow());
                tv_Like.setCompoundDrawablesWithIntrinsicBounds(is_praise == 1 ? leftIcon_like : leftIcon_unlike, null, null, null);
                int px = Utils.dp2px(getApplicationContext(), 12);

                TextView titleItemView = new TextView(ArticleDetailsActivity.this);
                LinearLayout.LayoutParams titleItemViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                titleItemViewParams.setMargins(px, px * 2, px, px);
                titleItemView.setText(contentEntity.creation.title);
                titleItemView.setTextColor(Color.parseColor("#333333"));
                titleItemView.setTextSize(21);
                mContentRootView.addView(titleItemView, titleItemViewParams);

                //  String content = contentEntity.creation.content.replace("\\", "");

                JSONArray ja = JSON.parseArray(contentEntity.creation.content);
                List<ArticleContentEntity> articleContentEntities = new ArrayList<>();
                if (ja != null && ja.size() > 0) {

                    for (int i = 0; i < ja.size(); i++) {
                        com.alibaba.fastjson.JSONObject jo = (com.alibaba.fastjson.JSONObject) ja.get(i);
                        ArticleContentEntity entity = new ArticleContentEntity();
                        entity.setIndex(jo.getInteger("index"));
                        entity.setType(jo.getString("type"));
                        entity.setContent(jo.getString("content"));
                        articleContentEntities.add(entity);
                    }
                }

                //  List<ArticleContentEntity> articleContentEntities = JSON.parseArray(content, ArticleContentEntity.class);
                for (int i = 0; i < articleContentEntities.size(); i++) {
                    ArticleContentEntity entity = articleContentEntities.get(i);

                    if (entity.type.equals("text")) {
                        TextView textItemView = new TextView(ArticleDetailsActivity.this);
                        LinearLayout.LayoutParams textItemViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        textItemViewParams.setMargins(px, px * 2, px, px);
                        textItemView.setText(entity.content);
                        textItemView.setTextColor(Color.parseColor("#333333"));
                        textItemView.setTextSize(13);
                        mContentRootView.addView(textItemView, textItemViewParams);
                    } else if (entity.type.equals("image")) {
                        ImageView imageItemView = new ImageView(ArticleDetailsActivity.this);
                        LinearLayout.LayoutParams imageItemViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        imageItemViewParams.setMargins(px, px + 5, px, px / 3);
                        Glide.with(mContext).load(entity.content).into(imageItemView);
                        mContentRootView.addView(imageItemView, imageItemViewParams);
                    } else if (entity.type.equals("video")) {
                        JCVideoPlayerStandard videoItemView = new JCVideoPlayerStandard(ArticleDetailsActivity.this);
                        LinearLayout.LayoutParams imageItemViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        imageItemViewParams.setMargins(px, px + 5, px, px / 3);
                        videoItemView.setUp(entity.content, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
                        mContentRootView.addView(videoItemView, imageItemViewParams);
                    }
                }

                //设置评论
                for (int i = 0; i < contentEntity.getComments().size(); i++) {

                    List<ReleaseContentEntity.Comments> comments = contentEntity.getComments();
                    DiscussView view = new DiscussView(mContext);
                    view.setAllInfo(comments.get(i).getName()
                            , comments.get(i).getArticle_comment_content()
                            , Long.valueOf(comments.get(i).getArticle_comment_time())
                            , comments.get(i).getPhoto()
                            , comments.get(i).getIs_praise().equals("0") ? false : true
                            , comments.get(i).getArticle_comment_praise_cnt()
                            , comments.get(i).getArticle_comment_id()
                    );

                    ListView lv = view.getListView();
                    view.setOnItemClickListener(ArticleDetailsActivity.this);
                    lv.setAdapter(new DiscussInnerAdapter(comments.get(i).getReply_list(), mContext));
                    mDisussRootView.addView(view);


                }

                Glide.with(ArticleDetailsActivity.this).load(contentEntity.advertising.image).into(mAdImgView);
                mAdTitleView.setText(contentEntity.advertising.title);
                mAdContentView.setText(contentEntity.advertising.content);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
        sv.setOnObservableScrollViewScrollChanged((l, t, oldl, oldt) -> {
            discuss(true);
        });

        ed_discuss.setOnEditorActionListener((v, actionId, event) -> {
            String s = ed_discuss.getText().toString();
            if (actionId == EditorInfo.IME_ACTION_SEND && !TextUtils.isEmpty(s)) {

                switch (index) {
                    case COMENT_COMENT:
                        sendComment(s);
                        break;

                    case REPLY_COMENT:
                        sendReplyComment(s);
                        break;
                }

            }
            index = COMENT_COMENT;
            return false;
        });


    }

    /**
     * 评论文章-评论评价
     *
     * @param reply
     */
    private void sendReplyComment(String reply) {
        Map<String, Object> map = new HashMap<>();
        map.put("article_comment_id", String.valueOf(Reply_id));
        map.put("customer_id", user.customer_id);
        map.put("article_comment_content", reply);

        apiImp.replyComent(map, mContext, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ViewInject.shortToast(mContext, "评论成功");
                ed_discuss.setText("");
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(mContext, "评论失败");
            }
        });
        Reply_id = "";
        discuss(false);
    }

    /**
     * 评论文章
     *
     * @param content
     */
    private void sendComment(String content) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", String.valueOf(mDetailsId));
        map.put("customer_id", user.customer_id);
        map.put("article_comment_content", content);
        apiImp.contentComent(map, mContext, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ViewInject.shortToast(mContext, "评论成功");
                ed_discuss.setText("");

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(mContext, "评论失败");
            }
        });
        discuss(false);
    }


    private void zan() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("articles_id", String.valueOf(mDetailsId));
        apiImp.praiseArticles(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    org.json.JSONObject result = new JSONObject(data);
                    is_praise = result.getInt("is_praise");
                    int praise_cnt = result.getInt("praise_cnt");
                    // tv_Like.setText(String.valueOf(praise_cnt));
                    // tv_Like.setCompoundDrawablesRelativeWithIntrinsicBounds(is_praise == 1 ? R.mipmap.icon_video_zan_sel : R.mipmap.icon_video_zan_nor, -1, -1, -1);

                    tv_Like.setCompoundDrawablesWithIntrinsicBounds(is_praise == 1 ? leftIcon_like : leftIcon_unlike, null, null, null);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void follow() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("op_customer_id", String.valueOf(authorId));
        params.put("bind_type", is_bind ? String.valueOf(2) : String.valueOf(1));
        apiImp.bindRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                is_bind = !is_bind;
                mFollowBtn.setText(is_bind ? "取消关注" : "+ 关注");
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    /**
     * 判断输入框处于什么状态
     *
     * @param isScowlling 判断是在外部点击事件
     */

    private void discuss(boolean isScowlling) {


        if (isScowlling) {

            if (ed_discuss.isFocused()) {
                ed_discuss.setFocusable(false);
                ed_discuss.setFocusableInTouchMode(false);
                imm.hideSoftInputFromWindow(ed_discuss.getWindowToken(), 0);
                ed_discuss.setVisibility(View.GONE);
            }


        } else {
            if (ed_discuss.isFocused()) {
                ed_discuss.setFocusable(false);
                ed_discuss.setFocusableInTouchMode(false);
                imm.hideSoftInputFromWindow(ed_discuss.getWindowToken(), 0);
                ed_discuss.setVisibility(View.GONE);
            } else {
                ed_discuss.setFocusable(true);
                ed_discuss.setFocusableInTouchMode(true);
                ed_discuss.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                ed_discuss.setVisibility(View.VISIBLE);
            }
        }


    }

    @OnClick({R.id.iv_edit_article_back_btn, R.id.bt_article_details_share, R.id.bt_article_details_zan, R.id.bt_article_details_discuss})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit_article_back_btn:
                finish();
                break;
            case R.id.bt_article_details_share:
                Glide.with(mContext).load("http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/2019062821201820186833.png").asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        String shareUrl = UrlApi.h5HttpUrl+"release/blank.html?type=2&id=" + contentEntity.getCreation().id;
                        Utils.umengShareByList((Activity) mContext, resource, "", "来自 约见·百家姓", shareUrl);
                    }
                });
                break;

            case R.id.bt_article_details_zan:
                zan();
                break;
            case R.id.bt_article_details_discuss:
                ed_discuss.setHint("评价文章");
                index = COMENT_COMENT;
                discuss(false);
                break;
        }
    }

    //监听输入栏，判断是输入到哪个评论里
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

        switch (actionId) {
            case EditorInfo.IME_ACTION_SEND:
                if (!TextUtils.isEmpty(textView.getText())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", String.valueOf(mDetailsId));
                    map.put("customer_id", user.customer_id);
                    map.put("article_comment_content", ed_discuss.getText().toString());
                    apiImp.contentComent(map, mContext, new DataIdCallback<String>() {
                        @Override
                        public void onSuccess(String data, int id) {
                            ViewInject.shortToast(mContext, "评论成功");
                            ed_discuss.setText("");
                            //  refreshData();
                        }

                        @Override
                        public void onFailed(String errCode, String errMsg, int id) {
                            ViewInject.shortToast(mContext, "评论失败");
                        }
                    });
                }
                break;
        }
        return true;
    }

    @Override
    public void DiscussViewClick(View v, String id, String name) {
        ed_discuss.setHint(String.format("回复 %s",name));
        index = REPLY_COMENT;
        Reply_id = id;
        discuss(false);
    }
}
