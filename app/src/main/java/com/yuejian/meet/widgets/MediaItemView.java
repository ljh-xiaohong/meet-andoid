package com.yuejian.meet.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author : g000gle
 * @time : 2019/5/28 10:46
 * @desc : 创作 - 编辑文章 - 文字、图片、视频ItemView
 * 因为需要传递类型参数，此View只能在Java代码中创建，不支持在xml中直接使用
 */
public class MediaItemView extends LinearLayout {

    public static final int MEDIA_TEXT = 1;
    public static final int MEDIA_IMAGE = 2;
    public static final int MEDIA_VIDEO = 3;

    @Bind(R.id.et_media_item_edit_content)
    EditText mEditContentView;
    @Bind(R.id.iv_media_item_image_content)
    ImageView mImageContentView;
    @Bind(R.id.vv_media_item_video_content)
    JCVideoPlayerStandard mVideoContentView;
    @Bind(R.id.ll_media_item_remove_btn)
    LinearLayout mRemoveBtn;
    @Bind(R.id.iv_media_item_added_type_icon)
    ImageView mAddedTypeIconView;
    @Bind(R.id.tv_media_item_added_type_name)
    TextView mAddedTypeNameView;
    @Bind(R.id.ll_media_item_replace_btn)
    LinearLayout mReplaceBtn;
    @Bind(R.id.iv_media_item_replace_icon)
    ImageView mReplaceBtnIcon;
    @Bind(R.id.iv_media_item_play_video_btn)
    ImageView mPlayVideoView;

    private Context mContext;
    private int mMediaType;
    private int mItemId;
    private OnViewClickListener mOnViewClickListener;
    private String mPicLocalPath;

    public MediaItemView(Context context, int mediaType, int itemId, OnViewClickListener listener) {
        this(context, null, mediaType, itemId, listener);
    }

    public MediaItemView(Context context, @Nullable AttributeSet attrs, int mediaType, int itemId, OnViewClickListener listener) {
        this(context, attrs, 0, mediaType, itemId, listener);
    }

    public MediaItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int mediaType, int itemId, OnViewClickListener listener) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mMediaType = mediaType;
        mItemId = itemId;
        mOnViewClickListener = listener;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_media_item_view, this, true);
        ButterKnife.bind(this, view);

        mEditContentView.setVisibility(mMediaType == MEDIA_TEXT ? VISIBLE : GONE);
        mImageContentView.setVisibility(mMediaType == MEDIA_IMAGE ? VISIBLE : GONE);
        mVideoContentView.setVisibility(mMediaType == MEDIA_VIDEO ? VISIBLE : GONE);


        mRemoveBtn.setOnClickListener(v -> mOnViewClickListener.onRemoveViewClick(MediaItemView.this));

        mAddedTypeIconView.setVisibility(GONE);
        mAddedTypeNameView.setVisibility(GONE);
        mReplaceBtn.setVisibility(mMediaType == MEDIA_TEXT ? GONE : VISIBLE);
        mReplaceBtnIcon.setVisibility(mMediaType == MEDIA_TEXT ? GONE : VISIBLE);
        this.setTag(mItemId);
        if (mMediaType == MEDIA_IMAGE) {
            // mAddedTypeIconView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_article_edit_addpic));
            // mAddedTypeNameView.setText("添加图片");
            mReplaceBtn.setOnClickListener(v -> mOnViewClickListener.onReplaceViewClick(MediaItemView.this));
            mReplaceBtnIcon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_article_edit_replace_pic));
        } else if (mMediaType == MEDIA_VIDEO) {
            // mAddedTypeIconView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_article_edit_addvid));
            // mAddedTypeNameView.setText("添加视频");
            mReplaceBtn.setOnClickListener(v -> mOnViewClickListener.onReplaceViewClick(MediaItemView.this));
            mReplaceBtnIcon.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_article_edit_replace_vid));
        }

        mPlayVideoView.setVisibility(GONE);
    }

    /**
     * 获取媒体类型
     */
    public int getMediaType() {
        return mMediaType;
    }

    /**
     * 获取下标
     * @return
     */
    public int getmItemId(){return  mItemId;};

    /**
     * 获取文本内容
     */
    public String getEditText() {
        return mEditContentView.getText().toString();
    }

    /**
     * 获取本地图片地址
     */
    public String getPicLocalPath() {
        return mPicLocalPath;
    }

    public void setImageContentView(String picLocalPath) {
        mPicLocalPath = picLocalPath;
        Glide.with(mContext).load(picLocalPath).into(mImageContentView);
    }

    public void setVideoContentView(String videoLocalPath) {
        mVideoContentView.setUp(videoLocalPath, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
    }

    public interface OnViewClickListener {
        void onRemoveViewClick(View v);

        void onReplaceViewClick(View v);
    }
}
