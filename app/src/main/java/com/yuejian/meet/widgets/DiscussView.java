package com.yuejian.meet.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.utils.DateUtil;
import com.yuejian.meet.utils.TextUtil;
import com.yuejian.meet.utils.TimeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DiscussView extends LinearLayout implements View.OnClickListener {

    @Bind(R.id.item_discuss_image)
    ImageView head;
    @Bind(R.id.item_discuss_name)
    TextView tv_name;
    @Bind(R.id.item_discuss_time)
    TextView tv_time;
    @Bind(R.id.item_discuss_like)
    TextView tv_like;
    @Bind(R.id.item_discuss_content)
    TextView tv_contents;
    @Bind(R.id.item_discuss_contents)
    ListView lv_sons;

    private String ArticleId;

    private OnDiscussItemClickListener listener;

    public DiscussView(Context context) {
        this(context, null);
    }

    public DiscussView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscussView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void onClick(View view) {
        if (null != listener) {
            this.listener.DiscussViewClick(view, ArticleId, !TextUtils.isEmpty(tv_name.getText().toString()) ? tv_name.getText().toString() : "");
        }
    }

    public interface OnDiscussItemClickListener {
        void DiscussViewClick(View v, String ArticleId, String name);
    }

    public void setOnItemClickListener(OnDiscussItemClickListener listener) {
        this.listener = listener;
    }


    private void init() {
        View view = View.inflate(getContext(), R.layout.item_discuss_item, this);
        ButterKnife.bind(this, view);
        this.setOnClickListener(this);

    }

    public void setImageUrl(String url) {
        Glide.with(getContext()).load(url).into(head);
    }

    public void setContent(String content) {
        tv_contents.setText(TextUtil.notNull(content));
    }

    public void setTime(long seconds) {
        tv_time.setText(DateUtil.generateTimestamp(seconds));
    }

    public void setName(String name) {
        tv_name.setText(TextUtil.notNull(name));
    }

    public void setLike(boolean isLike, String count) {
        if (isLike) {
            tv_like.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.icon_pingjia_zan_sel), null, null, null);
        } else {
            tv_like.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.icon_pingjia_zan_nor), null, null, null);
        }
        tv_like.setText(count);
    }

    public void setArticleId(String ArticleId) {
        this.ArticleId = ArticleId;
    }

    /**
     * @param name      评论人 - 名字
     * @param content   评论人 - 评论内容
     * @param seconds   评论人 - 评论时间
     * @param url       评论人 - 图像
     * @param isLike    评论人 - 与本用户点赞关联
     * @param likeCount 评论人 - 点赞个数
     */
    public void setAllInfo(String name, String content, long seconds, String url, boolean isLike, String likeCount, String article_comment_id) {
        setName(name);
        setTime(seconds);
        setContent(content);
        setImageUrl(url);
        setLike(isLike, likeCount);
        setArticleId(article_comment_id);
    }

    public ListView getListView() {
        return this.lv_sons;
    }


}
