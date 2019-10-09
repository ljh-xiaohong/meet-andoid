package com.yuejian.meet.activities.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CircleImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

public class InputActivity extends FragmentActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {
    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    private boolean hasClick;

    private Context mContext;

    private ApiImp api = new ApiImp();

    private String crId = "";

    private String replyCommentId = "";

    private String hint;

    private int limit = 200;

    InputMethodManager imm;

    @Bind(R.id.content)
    EditText content;

    @Bind(R.id.dismiss)
    View dismiss;

    @Bind(R.id.emojicons)
    FrameLayout emojicons;

    @Bind(R.id.emoji)
    ImageView emoji;

    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.head_img)
    CircleImageView head;

    private WeakReference<InputActivity> reference;


//    public static void startActivity(Activity activity, String crId, String replyCommentId) {
//        Intent intent = new Intent(activity, InputActivity.class);
//        intent.putExtra("InputActivity.crId", crId);
//        intent.putExtra("InputActivity.id", replyCommentId);
//        activity.startActivity(intent);
//    }

    public static void startActivityForResult(Activity activity, String crId, String replyCommentId, String hint, int requestCode) {
        Intent intent = new Intent(activity, InputActivity.class);
        intent.putExtra("InputActivity.crId", crId);
        intent.putExtra("InputActivity.id", replyCommentId);
        intent.putExtra("InputActivity.hint", hint);
        activity.startActivityForResult(intent, requestCode);
    }

    private boolean intData() {
        crId = getIntent().getStringExtra("InputActivity.crId");
        replyCommentId = getIntent().getStringExtra("InputActivity.id");
        hint = getIntent().getStringExtra("InputActivity.hint");
        return crId != null && replyCommentId != null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        if (!intData()) return;
        reference = new WeakReference<>(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mContext = this;
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

        if (!TextUtils.isEmpty(AppConfig.photo)) {
            Glide.with(this).load(AppConfig.photo).into(head);
        }

        content.setOnClickListener(v -> {
            emojicons.setVisibility(View.GONE);
            hasClick = false;
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (content.getText().toString().length() > limit) {
                    content.setText(content.getText().toString().substring(0, limit));
                    content.setSelection(content.length());
                    ViewInject.shortToast(mContext, "字数限制，不超：" + limit);
                }
            }
        });
        setEmojiconFragment(false);

        if (!TextUtils.isEmpty(hint)) content.setHint(String.format("回复：%s", hint));

        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        imm.showSoftInput(content, 0); //强制隐藏键盘

        dismiss.setOnClickListener(view -> {
            Intent i = new Intent();
            if (!TextUtils.isEmpty(content.getText().toString())) {
                i.putExtra("InputActivity.cancel", content.getText().toString());
            }
            setResult(RESULT_CANCELED, i);
            finish();
        });

        content.setOnClickListener(v -> {
            emojicons.setVisibility(View.GONE);
            hasClick = false;
        });

        tvSend.setOnClickListener(v -> {
            send();
        });
        emoji.setOnClickListener(v -> {
            if (hasClick) {
                emojicons.setVisibility(View.GONE);
                imm.showSoftInput(content, 0); //强制隐藏键盘
                hasClick = false;
            } else {
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0); //强制隐藏键盘
                emojicons.setVisibility(View.VISIBLE);
                hasClick = true;
            }
        });


    }


    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();

    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
                .commit();
    }

    @Override
    public void finish() {
        super.finish();
        //finish时调用退出动画
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    private void send() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        params.put("crId", crId);
        params.put("commentContent", content.getText().toString());
        params.put("replyCommentId", replyCommentId);
        api.contentComent(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (reference.get() == null || reference.get().isFinishing()) return;
                content.setText("");
                content.setHint("留下你的评论吧~");
                emojicons.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(content.getWindowToken(), 0); //强制隐藏键盘
                hasClick = false;
                Toast.makeText(mContext, "操作成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (reference.get() == null || reference.get().isFinishing()) return;
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(content, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(content);
    }

    @Override
    public void onBackPressed() {
        if (hasClick) {
            emojicons.setVisibility(View.GONE);
            imm.showSoftInput(content, 0); //强制隐藏键盘
            hasClick = false;
        } else {
            super.onBackPressed();
        }
    }
}
