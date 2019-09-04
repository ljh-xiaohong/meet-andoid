package com.yuejian.meet.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.VideoDetailsActivity;
import com.yuejian.meet.adapters.CommentListViewAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.CommentEntity;
import com.yuejian.meet.bean.ReleaseContentEntity;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * @author : g000gle
 * @time : 2019/5/18 12:59
 * @desc : 底部评论dialog
 */
public class CommentBottomDialog {

    private final Dialog mBottomDialog;
    private Context context;
    private CommentListViewAdapter adapter;
private TextView countView;

    public CommentBottomDialog(Context context, ApiImp apiImp, List<CommentEntity> commentEntities, int detailsId, String customerId) {
        mBottomDialog = new Dialog(context, R.style.BottomDialog);
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_comment, null);

        countView = (TextView) contentView.findViewById(R.id.tv_comment_dialog_count);
        ImageView closeBtn = (ImageView) contentView.findViewById(R.id.iv_close_comment_dialog);
        RecyclerView commentListView = (RecyclerView) contentView.findViewById(R.id.rv_comment_dialog_list);
        EditText editText = (EditText) contentView.findViewById(R.id.et_comment_dialog_edit);
        Button sendBtn = (Button) contentView.findViewById(R.id.btn_comment_dialog_send);

        closeBtn.setOnClickListener(v -> mBottomDialog.dismiss());
        countView.setText(String.format("共%s条评论", commentEntities.size()));

        commentListView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new CommentListViewAdapter(context);
        commentListView.setAdapter(adapter);
        adapter.refresh(commentEntities);

        sendBtn.setOnClickListener(v -> {
            String s = editText.getText().toString();
            if (!TextUtils.isEmpty(s)) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", String.valueOf(detailsId));
                map.put("customer_id", customerId);
                map.put("article_comment_content", s);
                apiImp.contentComent(map, context, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        ViewInject.shortToast(context, "评论成功");
                        editText.setText("");
                        // TODO refreshData();
                        refreshData(apiImp, String.valueOf(detailsId), customerId);
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                        ViewInject.shortToast(context, "评论失败");
                    }
                });
            }
        });

        mBottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomDialog.setCanceledOnTouchOutside(true);
        mBottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    private void refreshData(ApiImp apiImp, String mDetailsId, String customerId) {

        Map<String, Object> map = new HashMap<>();
        map.put("id", String.valueOf(mDetailsId));
        map.put("customer_id", customerId);
        map.put("order_index", String.valueOf(1));
//        map.put("page", String.valueOf(1));
        apiImp.getContentComments(map, context, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<CommentEntity> commentEntities = JSON.parseArray(data, CommentEntity.class);
                if (commentEntities.size() > 0) {
                    adapter.refresh(commentEntities);
                    countView.setText(String.format("共%s条评论", commentEntities.size()));
                    ((VideoDetailsActivity) context).refreshData();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    public void show() {
        if (mBottomDialog != null) {
            mBottomDialog.show();
        } else {
            throw new NullPointerException("CommentBottomDialog should init first.");
        }
    }
}
