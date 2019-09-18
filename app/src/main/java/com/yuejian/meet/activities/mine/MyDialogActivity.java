package com.yuejian.meet.activities.mine;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.CommentListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.CommentBean;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * @author : ljh
 * @time : 2019/9/10 11:42
 * @desc :
 */
public class MyDialogActivity extends FragmentActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;
    @Bind(R.id.comment_list)
    RecyclerView commentList;
    @Bind(R.id.shop_img)
    CircleImageView shopImg;
    @Bind(R.id.content)
    EditText content;
    @Bind(R.id.emoji)
    ImageView emoji;
    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.comment_edit_lay)
    RelativeLayout commentEditLay;
    @Bind(R.id.emojicons)
    FrameLayout emojicons;
    @Bind(R.id.dismiss)
    View dismiss;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.cencel)
    ImageView cencel;
    @Bind(R.id.window)
    LinearLayout window;
    private CommentListAdapter commentAdapter;
    private List<CommentBean.DataBean> mcommentData = new ArrayList<CommentBean.DataBean>();
    private boolean hasClick;
    private ApiImp api = new ApiImp();
    private String replyCommentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydialog);
        ButterKnife.bind(this);
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();
        this.overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
//        //设置布局在底部
        getWindow().setGravity(Gravity.BOTTOM);
        //设置布局填充满宽度
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);
        emoji.setOnClickListener(v -> {
            if (hasClick) {
                emojicons.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(content, 0); //强制隐藏键盘
                hasClick = false;
            } else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0); //强制隐藏键盘
                emojicons.setVisibility(View.VISIBLE);
                hasClick = true;
            }
        });
        commentAdapter = new CommentListAdapter(this, mcommentData);
        commentList.setLayoutManager(new LinearLayoutManager(this));
        commentList.setAdapter(commentAdapter);
        commentAdapter.setChange(new CommentListAdapter.OnChange() {
            @Override
            public void click(int postion) {
                replyCommentId=mcommentData.get(postion).getReplyCommentId();
                content.setHint("回复" + mcommentData.get(postion).getName());
            }
        });
        dismiss.setOnClickListener(v -> finish());
        content.setOnClickListener(v -> {
            emojicons.setVisibility(View.GONE);
            hasClick = false;
        });
        setEmojiconFragment(false);
        if (!TextUtils.isEmpty(AppConfig.photo)) {
            Glide.with(this).load(AppConfig.photo).into(shopImg);
        }
        initData();
        tvSend.setOnClickListener(v -> send());
        cencel.setOnClickListener(v -> finish());
    }

    private void send() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        params.put("crId", getIntent().getStringExtra("crId"));
        params.put("commentContent", content.getText().toString());
        params.put("replyCommentId", replyCommentId);
        api.contentComent(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                content.setText("");
                content.setHint("留下你的评论吧~");
                emojicons.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0); //强制隐藏键盘
                hasClick = false;
                mcommentData.clear();
                initData();
                Toast.makeText(MyDialogActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(MyDialogActivity.this, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int mNextPageIndex = 1;
    private int pageCount = 20;

    private void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("myCustomerId", AppConfig.CustomerId);
        params.put("crId", getIntent().getStringExtra("crId"));
        params.put("pageIndex", String.valueOf(mNextPageIndex));
        params.put("pageItemCount", String.valueOf(pageCount));
        api.getContentComments(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                CommentBean commentBean = new Gson().fromJson(data, CommentBean.class);
                mcommentData.addAll(commentBean.getData());
                count.setText("共"+mcommentData.size()+"条评论");
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                Toast.makeText(MyDialogActivity.this, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //重写finish方法
    @Override
    public void finish() {
        Intent i = new Intent();
        setResult(7, i);
        super.finish();
        //finish时调用退出动画
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(content, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(content);
    }

    @Override
    public void onBackPressed() {
        if (hasClick) {
            emojicons.setVisibility(View.GONE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(content, 0); //强制隐藏键盘
            hasClick = false;
        } else {
            super.onBackPressed();
        }
    }
}
