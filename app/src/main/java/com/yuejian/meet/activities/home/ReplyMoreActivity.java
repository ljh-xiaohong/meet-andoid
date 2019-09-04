package com.yuejian.meet.activities.home;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ReplyMoreAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.CommentsEntity;
import com.yuejian.meet.bean.DelActionInfoEntity;
import com.yuejian.meet.bean.PraiseState;
import com.yuejian.meet.bean.ReplyListEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 评论回复所有
 */
public class ReplyMoreActivity extends BaseActivity implements SpringView.OnFreshListener {
    @Bind(R.id.reply_more_spring)
    SpringView springView;
    @Bind(R.id.reply_more_list)
    ListView listView;
    ImageView img_action_header;
    @Bind(R.id.reply_msg_send)
    Button msg_send;
    @Bind(R.id.reply_msg_content)
    EditText msg_content;

    TextView dialog_del, dialog_reply;
    ImageView img_action_sponsor;

    TextView txt_action_name, txt_action_city, txt_action_age, txt_action_job, txt_action_time, txt_item_actioninfo_content, actioninfo_like_praise_count;
    ImageView actioninfo_message;
    LinearLayout acton_meg_layout;
    ReplyMoreAdapter mAdapter;
    List<ReplyListEntity> listData = new ArrayList<>();
    CommentsEntity commentsEntity;
    View viewHeader;
    String titlName;
    int pageIndex = 1;
    String action_id = "1";
    String comment_id;
    Boolean isAction=false;

    private View mPoupView = null, mHintView = null;
    private PopupWindow mPoupWindow = null, mHintWindow = null;
    public String commentName, content = "";
    LoadingDialogFragment dialog;

    ApiImp api = new ApiImp();
    private boolean isCommentMe = true;
    public String replyId = "";
    public String listLastId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_more);
        dialog = LoadingDialogFragment.newInstance("正在操作..");
        Intent intent = getIntent();
        Bundle mBundle = intent.getExtras();
        titlName = mBundle.getString("titlName");
        action_id = mBundle.getString("action_id");
        commentsEntity = (CommentsEntity) mBundle.getSerializable("commentsEntity");
        if (commentsEntity == null) finish();
        if (commentsEntity.isArticleComment) {
            replyId = commentsEntity.article_comment_id;
        } else {
            replyId = commentsEntity.comment_id;
        }
        isCommentMe = true;
        initData();
    }

    public void initData() {
        viewHeader = View.inflate(this, R.layout.item_action_info_layout, null);
        initViewHeader();
        msg_send.setSelected(true);
        setTitleText(titlName + "条回复");
        springView.setFooter(new DefaultFooter(this));
        springView.setHeader(new DefaultHeader(this));
        springView.setListener(this);
        listView.addHeaderView(viewHeader);
        viewHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requestInput(true, "评论", commentsEntity.comment_id);
                showBottomPopupWindow(commentsEntity.getComment_content(), commentsEntity.getCustomer_id(), StringUtils.isNotEmpty(commentsEntity.getComment_id()) ? commentsEntity.getComment_id() : commentsEntity.getArticle_comment_id(), true, "");
            }
        });
        mAdapter = new ReplyMoreAdapter(listView, listData, R.layout.item_reply_more_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        getRequestData();
        loadHeaderData();
    }

    public void initViewHeader() {
        img_action_header = (ImageView) viewHeader.findViewById(R.id.img_action_header);
        txt_action_name = (TextView) viewHeader.findViewById(R.id.txt_action_name);
        txt_action_city = (TextView) viewHeader.findViewById(R.id.txt_action_city);
        txt_action_age = (TextView) viewHeader.findViewById(R.id.txt_action_age);
        txt_action_job = (TextView) viewHeader.findViewById(R.id.txt_action_job);
        txt_action_time = (TextView) viewHeader.findViewById(R.id.txt_action_time);
        txt_item_actioninfo_content = (TextView) viewHeader.findViewById(R.id.txt_item_actioninfo_content);
        actioninfo_like_praise_count = (TextView) viewHeader.findViewById(R.id.actioninfo_like_praise_count);
        actioninfo_message = (ImageView) viewHeader.findViewById(R.id.actioninfo_message);
        acton_meg_layout = (LinearLayout) viewHeader.findViewById(R.id.acton_meg_layout);
        img_action_sponsor= (ImageView) viewHeader.findViewById(R.id.img_action_sponsor);
        img_action_sponsor.setVisibility(View.GONE);
        acton_meg_layout.setVisibility(View.GONE);
        actioninfo_message.setOnClickListener(this);
        actioninfo_like_praise_count.setOnClickListener(this);
    }

    public void loadHeaderData() {
        if (commentsEntity == null) return;
        txt_action_name.setText(commentsEntity.getSurname() + commentsEntity.getName());
        txt_action_city.setText(commentsEntity.getFamily_area());
        txt_action_age.setSelected(!commentsEntity.getSex().equals("0"));
        txt_action_age.setText(" " + commentsEntity.getAge());
        txt_action_job.setText(commentsEntity.getJob());
        if (commentsEntity.isArticleComment) {
            actioninfo_like_praise_count.setSelected(!commentsEntity.article_comment_praise_cnt.equals("0"));
            actioninfo_like_praise_count.setText(" " + commentsEntity.article_comment_praise_cnt);
            txt_item_actioninfo_content.setText(commentsEntity.article_comment_content);
            txt_action_time.setText(StringUtils.friendlyTime(commentsEntity.article_comment_time));
        } else {
            actioninfo_like_praise_count.setSelected(!commentsEntity.getIs_praise().equals("0"));
            actioninfo_like_praise_count.setText(" " + commentsEntity.comment_praise_cnt);
            txt_item_actioninfo_content.setText(commentsEntity.getComment_content());
            txt_action_time.setText(StringUtils.friendlyTime(commentsEntity.getComment_time()));
        }
        if (commentsEntity.getIs_super()>0){
            img_action_sponsor.setVisibility(View.VISIBLE);
            Glide.with(getApplication()).load(commentsEntity.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:commentsEntity.getIs_super()==FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).into(img_action_sponsor);
        }
        Glide.with(this).load(commentsEntity.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into(img_action_header);
    }

    public void getRequestData() {
        final Map<String, Object> params = new HashMap<>();
        params.put("comment_id", action_id);
        params.put("my_customer_id", user == null ? "" : user.getCustomer_id());
        params.put("pageIndex", pageIndex + "");
        params.put("pageItemCount", Constants.pageItemCount);
        params.put("listLastId", pageIndex == 1 ? "0" : listLastId);
        DataIdCallback<String> dataIdCallback = new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex == 1) {
                    listData.clear();
                }
                List<ReplyListEntity> replyListEntities = JSON.parseArray(data, ReplyListEntity.class);
                if (replyListEntities == null || replyListEntities.isEmpty()) {
                    pageIndex--;
                } else {
                    if (commentsEntity.isArticleComment) {
                        for (ReplyListEntity entity : replyListEntities) {
                            entity.isArticleReply = true;
                        }
                    }
                    listLastId = replyListEntities.get(replyListEntities.size() - 1).getComment_id();
                    listData.addAll(replyListEntities);
                    mAdapter.refresh(listData);
                }
                setTitleText("共" + listData.size() + "条回复");
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null)
                    springView.onFinishFreshAndLoad();
            }
        };
        if (commentsEntity.isArticleComment) {
            params.put("article_comment_id", action_id);
            api.getArticleCommentReplys(params, this, dataIdCallback);
        } else {
            api.getReplys(params, this, dataIdCallback);
        }
    }

    @OnClick({R.id.reply_msg_send})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actioninfo_message:
                msg_content.setHint("说点什么吧...");
                msg_content.setFocusable(true);
                msg_content.setFocusableInTouchMode(true);
                msg_content.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(msg_content, 0);
                break;
            case R.id.reply_msg_send://发送
                if (isCommentMe) {
                    comment();
                } else {
                    replyComment(replyId);
                }
                break;
            case R.id.actioninfo_like_praise_count://点赞
                praise();
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
                isCommentMe = isAction;
                this.replyId = comment_id;
                mPoupWindow.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        requestInput(isCommentMe, commentName, replyId);
//                        commentMsg(comment_id,isAction,commentName,index);
                    }
                }, 500);
                break;
            case R.id.dialog_del_commetn://删除评论
                mHintWindow.dismiss();
                isCommentMe=isAction;
                this.replyId = comment_id;
                delComment();
                break;
            case R.id.dialog_hint_cancel://关闭提示
                isCommentMe = true;
                replyId = StringUtil.isEmpty(commentsEntity.comment_id)?commentsEntity.article_comment_id:commentsEntity.comment_id;
                mHintWindow.dismiss();
                break;
        }
    }

    /**
     * 删除动态评论
     */
    public void delComment() {
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", replyId);
        params.put("customer_id", AppConfig.CustomerId);
        DataIdCallback dataIdCallback = new DataIdCallback() {
            @Override
            public void onSuccess(Object data, int id) {
                if (dialog != null)
                    dialog.dismiss();
                replyId= StringUtil.isEmpty(commentsEntity.comment_id)?commentsEntity.article_comment_id:commentsEntity.comment_id;
                if (isCommentMe) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    getRequestData();
                    isCommentMe = true;
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
            }
        };
        if (commentsEntity.isArticleComment) {
            api.postArticleDelComment(params, this, dataIdCallback);
        } else {
            api.postDelComment(params, this, dataIdCallback);
        }

    }

    /**
     * 底部PopupWindow
     */
    public void showBottomPopupWindow(String content, String costomerId, String comment_id, Boolean isAction, String commentName) {
//        isCommentMe = isAction;
//        this.replyId = comment_id;
        this.comment_id=comment_id;
        this.isAction=isAction;
        this.commentName = commentName;
        this.content = content;
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
        mPoupWindow.showAtLocation(springView, Gravity.BOTTOM, 0, 0);
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
        mHintWindow.showAtLocation(springView, Gravity.BOTTOM, 0, 0);
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

    public void comment() {
        if (msg_content.getText().toString().equals("")) {
            ViewInject.shortToast(this, "内容不能为空");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", commentsEntity.isArticleComment ? commentsEntity.article_comment_id : commentsEntity.comment_id);
        params.put("customer_id", StringUtils.isEmpty(AppConfig.CustomerId) ? "" : AppConfig.CustomerId);
        params.put("comment_content", msg_content.getText().toString());
        DataIdCallback<String> dataIdCallback = new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                msg_content.setText("");
                Utils.hintKbTwo((Activity) mContext);
                msg_content.setHint("说点什么吧...");
                listData.add(0, JSON.parseObject(data, ReplyListEntity.class));
                if (commentsEntity.isArticleComment)
                    listData.get(0).isArticleReply=true;
                mAdapter.refresh(listData);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        };
        if (commentsEntity.isArticleComment) {
            api.replyArticle(params, this, dataIdCallback);
        } else {
            api.replyAction(params, this, dataIdCallback);
        }
    }

    public void praise() {
        if (user == null) {
            return;
        }
        //点赞
        Map<String, Object> params = new HashMap<>();
        DataIdCallback<String> dataIdCallback = new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                PraiseState state = JSON.parseObject(data, PraiseState.class);
                if (state != null) {
                    commentsEntity.setComment_praise_cnt(state.praise_cnt);
                    actioninfo_like_praise_count.setText(" " + commentsEntity.getComment_praise_cnt());
                    commentsEntity.setIs_praise(state.is_praise);
                    actioninfo_like_praise_count.setSelected("1".equals(state.is_praise));
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        };
        if (!commentsEntity.isArticleComment) {
            params.put("comment_id", commentsEntity.comment_id);
            params.put("customer_id", AppConfig.CustomerId);
            api.commentPraise(params, this, dataIdCallback);
        } else {
            params.put("article_comment_id", commentsEntity.article_comment_id);
            params.put("customer_id", AppConfig.CustomerId);
            apiImp.getArticleCommentReplyPraise(params, this, dataIdCallback);
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getRequestData();
    }

    @Override
    public void onLoadmore() {
        pageIndex++;
        getRequestData();
    }

    private void replyComment(String replyIds) {
        String content = msg_content.getText().toString();
        if (StringUtils.isEmpty(content)) {
            ViewInject.shortToast(this, "内容不能为空");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", replyIds);
        params.put("customer_id", user.customer_id);
        params.put("comment_content", msg_content.getText().toString());
        DataIdCallback<String> dataIdCallback = new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                msg_content.setText("");
                Utils.hintKbTwo(ReplyMoreActivity.this);
                isCommentMe = true;
                replyId = commentsEntity.comment_id;
                msg_content.setHint("说点什么吧...");
                getRequestData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        };
        if (commentsEntity.isArticleComment) {
            apiImp.replyArticle(params, this, dataIdCallback);
        } else {
            apiImp.replyAction(params, this, dataIdCallback);
        }
    }

    public void requestInput(boolean isComment, String hint, String replyId) {
        this.replyId = replyId;
        isCommentMe = isComment;
        msg_content.setHint(hint);
        msg_content.setText("");
        msg_content.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(msg_content, 0);
    }

}
