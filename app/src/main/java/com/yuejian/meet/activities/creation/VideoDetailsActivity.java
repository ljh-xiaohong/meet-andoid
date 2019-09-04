package com.yuejian.meet.activities.creation;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.CommentEntity;
import com.yuejian.meet.bean.ReleaseContentEntity;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CircleImageView;
import com.yuejian.meet.widgets.CommentBottomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author : g000gle
 * @time : 2019/6/26 14:56
 * @desc : 视频详情界面
 */
public class VideoDetailsActivity extends BaseActivity {

    @Bind(R.id.vv_video_details_content)
    JCVideoPlayerStandard mVideoContent;
    @Bind(R.id.civ_video_details_header)
    CircleImageView mUserHeaderView;
    @Bind(R.id.tv_video_details_username)
    TextView mUsernameView;
    @Bind(R.id.tv_video_details_follow)
    TextView mFollowBtn;
    @Bind(R.id.tv_video_details_contents)
    TextView mContentView;
    @Bind(R.id.ll_video_details_sell)
    LinearLayout mSellBtn;
    @Bind(R.id.iv_video_details_zan_icon)
    ImageView mZanIcon;
    @Bind(R.id.tv_video_details_zan_count)
    TextView mZanCountView;
    @Bind(R.id.iv_video_details_comm_icon)
    ImageView mCommIcon;
    @Bind(R.id.tv_video_details_comm_count)
    TextView mCommCountView;
    @Bind(R.id.iv_video_details_share_icon)
    ImageView mShareIcon;
    @Bind(R.id.et_video_details_chat)
    EditText mChatEditView;
    @Bind(R.id.img_video_details_back)
    ImageView iv_video_details_zan_back;

    private int mDetailsId;
    private int authorId;
    private String customer_id;
    private boolean is_bind;
    private int is_praise;
    public String article_photo = "http://yuejian-app.oss-cn-shenzhen.aliyuncs.com/genealogy/2019062821201820186833.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        mDetailsId = getIntent().getIntExtra("id", -1);
        if (getIntent().hasExtra("customer_id")) {
            customer_id = getIntent().getStringExtra("customer_id");
        }
        initData();
    }

    @Override
    protected void onDestroy() {
        mVideoContent.release();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        JCVideoPlayer.releaseAllVideos();
        super.onPause();
    }

    private void initData() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("id", String.valueOf(mDetailsId));
        apiImp.getContentRelease(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ReleaseContentEntity contentEntity = JSON.parseObject(data, ReleaseContentEntity.class);
                authorId = contentEntity.creation.customer_id;
                mVideoContent.setUp(contentEntity.creation.content, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
                mVideoContent.startPlayLogic(); //自動播放
                Glide.with(VideoDetailsActivity.this).load(contentEntity.creation.photo).into(mUserHeaderView);
                mUsernameView.setText(contentEntity.creation.name);

                is_bind = contentEntity.creation.is_bind;
                mFollowBtn.setVisibility(authorId == Integer.parseInt(user.customer_id) ? View.GONE : View.VISIBLE);
                mFollowBtn.setOnClickListener(v -> follow());
                mFollowBtn.setText(is_bind ? "已关注" : "+ 关注");
                mFollowBtn.setBackgroundResource(is_bind ? R.drawable.bg_radiu_btn_dark_c06d66 : R.drawable.bg_radiu_btn_dark_red);

                mContentView.setText(contentEntity.creation.title);

                is_praise = contentEntity.creation.is_praise;
                mZanIcon.setImageResource(is_praise == 1 ? R.mipmap.icon_video_zan_sel : R.mipmap.icon_video_zan_nor);
                mZanIcon.setOnClickListener(v -> zan());
                mZanCountView.setText(String.valueOf(contentEntity.creation.fabulous_num));

                mCommIcon.setOnClickListener(v -> showComments());
                mCommCountView.setText(String.valueOf(contentEntity.creation.comment_num));
                mShareIcon.setOnClickListener(v -> {
                    Glide.with(mContext).load(article_photo).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            String title = contentEntity.creation.title;
                            String shareUrl = "http://app2.yuejianchina.com/yuejian-app/release/blank.html?type=4&id=" + contentEntity.creation.id;
                            Utils.umengShareByList(VideoDetailsActivity.this, resource, title, "来自 约见·百家姓", shareUrl);
                        }
                    });
                }); // TODO
                iv_video_details_zan_back.setOnClickListener(v -> finish());

                mUserHeaderView.setOnClickListener(view -> {
                    if (!TextUtils.isEmpty(customer_id)) {
                        AppUitls.goToPersonHome(mContext, customer_id);
                    }
                });
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });

        mChatEditView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String s = mChatEditView.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", String.valueOf(mDetailsId));
                    map.put("customer_id", user.customer_id);
                    map.put("article_comment_content", s);
                    apiImp.contentComent(map, VideoDetailsActivity.this, new DataIdCallback<String>() {
                        @Override
                        public void onSuccess(String data, int id) {
                            ViewInject.shortToast(VideoDetailsActivity.this, "评论成功");
                            mChatEditView.setText("");
                            refreshData();
                        }

                        @Override
                        public void onFailed(String errCode, String errMsg, int id) {
                            ViewInject.shortToast(VideoDetailsActivity.this, "评论失败");
                        }
                    });
                }
            }
            return false;
        });
    }

    private void showComments() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", String.valueOf(mDetailsId));
        map.put("my_customer_id", user.customer_id);
        map.put("order_index", String.valueOf(1));
        map.put("page", String.valueOf(1));
        apiImp.getContentComments(map, VideoDetailsActivity.this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<CommentEntity> commentEntities = JSON.parseArray(data, CommentEntity.class);
                if (commentEntities.size() > 0) {
                    CommentBottomDialog dialog = new CommentBottomDialog(VideoDetailsActivity.this, apiImp, commentEntities, mDetailsId, user.customer_id);
                    dialog.show();
                } else {
                    mChatEditView.setFocusable(true);
                    mChatEditView.setFocusableInTouchMode(true);
                    mChatEditView.requestFocus();
                    VideoDetailsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mChatEditView, 0);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });


    }

    public void refreshData() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("id", String.valueOf(mDetailsId));
        apiImp.getContentRelease(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ReleaseContentEntity contentEntity = JSON.parseObject(data, ReleaseContentEntity.class);
                is_bind = contentEntity.creation.is_bind;
                mFollowBtn.setText(is_bind ? "已关注" : "+ 关注");
                mFollowBtn.setBackgroundResource(is_bind ? R.drawable.bg_radiu_btn_dark_c06d66 : R.drawable.bg_radiu_btn_dark_red);
                is_praise = contentEntity.creation.is_praise;
                mZanIcon.setImageResource(is_praise == 1 ? R.mipmap.icon_video_zan_sel : R.mipmap.icon_video_zan_nor);
                mZanCountView.setText(String.valueOf(contentEntity.creation.fabulous_num));
                mCommCountView.setText(String.valueOf(contentEntity.creation.comment_num));
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
                mFollowBtn.setText(is_bind ? "已关注" : "+ 关注");
                mFollowBtn.setBackgroundResource(is_bind ? R.drawable.bg_radiu_btn_dark_c06d66 : R.drawable.bg_radiu_btn_dark_red);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void zan() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        params.put("articles_id", String.valueOf(mDetailsId));
        apiImp.praiseArticles(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    JSONObject result = new JSONObject(data);
                    is_praise = result.getInt("is_praise");
                    int praise_cnt = result.getInt("praise_cnt");
                    mZanCountView.setText(String.valueOf(praise_cnt));
                    mZanIcon.setImageResource(is_praise == 1 ? R.mipmap.icon_video_zan_sel : R.mipmap.icon_video_zan_nor);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }




}
