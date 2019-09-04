package com.yuejian.meet.activities.family;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.widgets.GiftDialogFragment;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.VideoViewActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.AVComment;
import com.yuejian.meet.bean.AVData;
import com.yuejian.meet.bean.PraiseState;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.MediaPlayerController;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HarbourActivity extends BaseActivity implements SpringView.OnFreshListener {
    private static final int GET_PROGRESS = 1;
    private AVCommentAdapter adapter = null;
    private CheckBox avCollection;
    private List<AVData> avDataList;
    private AVListAdapter avListAdapter = null;
    private String commentCustomerId = null;
    private List<AVComment> commentList = new ArrayList();
    private String copyContent;
    private View copySelection = null;
    private int currentPlayingIndex = 0;
    private TextView currentTimeTv = null;
    private View deleteSelection = null;
    private String fatherCommentId = null;
    private GiftDialogFragment giftDialogFragment;
    private PopupWindow giftWindow;

    private static final String DEFUALT_HINT = "说点什么吧...";

    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message paramAnonymousMessage) {
            try{
                if ((paramAnonymousMessage.what == 1) && (seekBar != null) && (mediaPlayer != null) && (mediaPlayer.isPlaying())) {
                    int i = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(i);
                    i = Math.round(i / 1000);
                    String time = String.format(Locale.getDefault(), "%02d:%02d", i / 60, i % 60);
                    currentTimeTv.setText(time);
                    handler.sendEmptyMessageDelayed(1, 1000L);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });
    private View header = null;
    private TextView introduction;
    private CheckBox likeCheck;
    private TextView likeCnt;
    private PopupWindow mPoupWindow;
    private MediaPlayer mediaPlayer = null;
    private MediaPlayerController mediaPlayerController = null;
    private int page = 1;
    private ImageView play;
    private String relyCommentId = null;
    private PopupWindow avPlayListSheet = null;
    private View replySelection = null;
    private TextView rewordCnt;
    private SeekBar seekBar = null;
    private EditText sendContentEd;
    private TextView source;
    private SpringView springView;
    private TextView title;

    private void avCommentPraise(final AVComment paramAVComment, final TextView paramTextView) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("av_comment_id", paramAVComment.av_comment_id);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.avCommentPraise(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                PraiseState praiseState = JSON.parseObject(data, PraiseState.class);
                paramTextView.setText(String.valueOf(" " + praiseState.praise_cnt));
                paramTextView.setSelected("1".equals(praiseState.is_praise));
                paramAVComment.praise_status = praiseState.is_praise;
                paramAVComment.av_comment_praise_cnt = praiseState.praise_cnt;
            }
        });
    }

    private void collection() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("type", String.valueOf(3));
        localHashMap.put("relation_id", this.avDataList.get(this.currentPlayingIndex).av_id);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.collection(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                avCollection.setChecked("1".equals(paramAnonymousString));
            }
        });
    }

    private void commentAv() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("av_id", avDataList.get(this.currentPlayingIndex).av_id);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        localHashMap.put("comment_content", this.sendContentEd.getText().toString());
        this.apiImp.commentAv(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                onRefresh();
            }
        });
        this.sendContentEd.setText("");
        Utils.hideSystemKeyBoard(this, this.sendContentEd);
        this.sendContentEd.setHint(DEFUALT_HINT);
    }

    private void deleteAvComment() {
        DataIdCallback local23 = new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                fatherCommentId = null;
                onRefresh();
            }
        };
        if (this.fatherCommentId == null) {
            HashMap<String, Object> params = new HashMap();
            params.put("av_comment_id", this.relyCommentId);
            this.apiImp.deleteAvComment(params, this, local23);
        } else {
            HashMap params = new HashMap();
            params.put("av_comment_id", this.relyCommentId);
            params.put("av_id", this.fatherCommentId);
            this.apiImp.deleteAvReplyComment(params, this, local23);
        }
    }

    private void findAVComment() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("av_id", this.avDataList.get(this.currentPlayingIndex).av_id);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        localHashMap.put("pageIndex", String.valueOf(this.page));
        this.apiImp.findAvComment(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                try {
                    List<AVComment> comment_list = JSON.parseArray(JSON.parseObject(data).getString("comment_list"), AVComment.class);
                    if (page == 1) {
                        commentList.clear();
                    } else {
                        if ((comment_list == null) || (comment_list.isEmpty())) {
                            page--;
                            if (page < 1) {
                                page = 1;
                            }
                        }
                    }
                    commentList.addAll(comment_list);
                    adapter.notifyDataSetChanged();
                    springView.onFinishFreshAndLoad();
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        });
    }

    private void findAvById(int paramInt) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("av_id", this.avDataList.get(paramInt).av_id);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        localHashMap.put("page", String.valueOf(this.page));
        this.apiImp.findAvById(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String error, String paramAnonymousString2, int paramAnonymousInt) {
                AVData avData = avDataList.get(currentPlayingIndex);
                introduction.setText(avData.av_depict);
                Glide.with(HarbourActivity.this).load(avData.av_img).into((ImageView) header.findViewById(R.id.cover));
                title.setText(avData.av_title);
                source.setText(String.valueOf("作者：" + avData.av_author));
                likeCnt.setText(avData.av_praise_cnt);
                rewordCnt.setText(avData.av_expense_cnt);
                seekBar.setProgress(0);
                likeCheck.setChecked("1".equals(avData.is_praise));
                avCollection.setChecked("1".equals(avData.is_collection));
                onRefresh();
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                AVData avData = JSON.parseObject(data, AVData.class);
                introduction.setText(avData.av_depict);
                Glide.with(HarbourActivity.this).load(avData.av_img).into((ImageView) header.findViewById(R.id.cover));
                title.setText(avData.av_title);
                source.setText(String.valueOf("作者：" + avData.av_author));
                likeCnt.setText(avData.av_praise_cnt);
                rewordCnt.setText(avData.av_expense_cnt);
                seekBar.setProgress(0);
                likeCheck.setChecked("1".equals(avData.is_praise));
                avCollection.setChecked("1".equals(avData.is_collection));
                onRefresh();
            }
        });
    }

    private void initHeader() {
        this.header = View.inflate(this, R.layout.harbour_header, null);
        this.introduction = ((TextView) this.header.findViewById(R.id.introduction));
        this.title = ((TextView) this.header.findViewById(R.id.title));
        this.source = ((TextView) this.header.findViewById(R.id.source));
        this.likeCnt = ((TextView) this.header.findViewById(R.id.like_cnt));
        this.rewordCnt = ((TextView) this.header.findViewById(R.id.reward_cnt));
        this.likeCheck = ((CheckBox) this.header.findViewById(R.id.like_check));
        this.likeCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                likeCheck();
            }
        });
        this.avCollection = ((CheckBox) this.header.findViewById(R.id.av_collection));
        this.avCollection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                collection();
            }
        });
        this.header.findViewById(R.id.reward).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                sendGift();
            }
        });
        this.header.findViewById(R.id.bofangku).setOnClickListener(this);
    }

    private void initMediaController() {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer paramAnonymousMediaPlayer) {
                next();
            }
        });
        this.header.findViewById(R.id.pre).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                pre();
            }
        });
        this.header.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                next();
            }
        });
        this.play = ((ImageView) this.header.findViewById(R.id.play));
        this.play.setImageResource(R.mipmap.bofang);
        this.play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                play();
            }
        });
        try {
            this.mediaPlayer.setDataSource(this.avDataList.get(this.currentPlayingIndex).av_url);
            this.mediaPlayer.prepare();
            this.mediaPlayerController = new MediaPlayerController(this, this.mediaPlayer);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private void initPlayer() {
        try {
            initMediaController();
            this.seekBar = ((SeekBar) this.header.findViewById(R.id.seek_bar_progress));
            this.currentTimeTv = ((TextView) this.header.findViewById(R.id.currentTime));
            this.header.findViewById(R.id.bofangku).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    showBottomList();
                }
            });
            this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean) {
                }

                public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar) {
                }

                public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar) {
                    int i = paramAnonymousSeekBar.getProgress();
                    mediaPlayer.seekTo(i);
                }
            });
            this.header.findViewById(R.id.fenxiang).setOnClickListener(this);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    private void initSendMessageLayout() {
        this.sendContentEd = ((EditText) findViewById(R.id.msg_content));
        this.sendContentEd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                sendContentEd.setHint(DEFUALT_HINT);
            }
        });
    }

    private void likeCheck() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("av_id", (this.avDataList.get(this.currentPlayingIndex).av_id));
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.avPraise(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                PraiseState praiseState = JSON.parseObject(paramAnonymousString, PraiseState.class);
                likeCnt.setText(praiseState.praise_cnt);
                likeCheck.setChecked("1".equals(praiseState.is_praise));
            }
        });
    }

    private void next() {
        if ((this.avDataList != null) && (this.currentPlayingIndex < this.avDataList.size() - 1)) {
            currentPlayingIndex++;
        }
        try {
            this.mediaPlayer.pause();
            this.mediaPlayer.reset();
            MediaPlayer localMediaPlayer = this.mediaPlayer;
            List localList = this.avDataList;
            int i = this.currentPlayingIndex + 1;
            this.currentPlayingIndex = i;
            localMediaPlayer.setDataSource(((AVData) localList.get(i)).av_url);
            this.mediaPlayer.prepare();
            play();
            updateAVInfo(this.currentPlayingIndex);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    private void play() {
        if ((this.avDataList == null) || (this.avDataList.isEmpty())) {
            return;
        }
        if (this.mediaPlayer.isPlaying()) {
            this.play.setImageResource(R.mipmap.bofang);
            this.mediaPlayer.pause();
            this.handler.removeMessages(1);
            return;
        }
        if ("1".equals(this.avDataList.get(this.currentPlayingIndex).av_type)) {
            Intent localIntent = new Intent(this, VideoViewActivity.class);
            localIntent.putExtra("video_url", (this.avDataList.get(this.currentPlayingIndex).av_url));
            startActivity(localIntent);
            this.play.setImageResource(R.mipmap.bofang);
            return;
        }
        this.play.setImageResource(R.mipmap.zanting);
        this.mediaPlayerController.startMediaPlayer();
        this.seekBar.setMax(this.mediaPlayer.getDuration());
        this.handler.sendEmptyMessageDelayed(1, 1000L);
    }

    private void pre() {
        if ((this.avDataList != null) && (this.currentPlayingIndex > 0)) {
            currentPlayingIndex--;
        }
        try {
            this.mediaPlayer.pause();
            this.mediaPlayer.reset();
            MediaPlayer localMediaPlayer = this.mediaPlayer;
            List localList = this.avDataList;
            int i = this.currentPlayingIndex - 1;
            this.currentPlayingIndex = i;
            localMediaPlayer.setDataSource(((AVData) localList.get(i)).av_url);
            this.mediaPlayer.prepare();
            play();
            updateAVInfo(this.currentPlayingIndex);
            return;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    private void relyComment() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("comment_id", this.relyCommentId);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        localHashMap.put("comment_content", this.sendContentEd.getText().toString());
        this.apiImp.replyComment(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                onRefresh();
            }
        });
        Utils.hideSystemKeyBoard(this, this.sendContentEd);
        this.sendContentEd.setText("");
        this.sendContentEd.setHint(DEFUALT_HINT);
    }

    private void showBottomList() {
        if (this.avPlayListSheet == null) {
            this.avPlayListSheet = new PopupWindow(this);
            View localView = View.inflate(this, R.layout.bottom_sheet_list, null);
            localView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    avPlayListSheet.dismiss();
                }
            });
            ListView localListView = (ListView) localView.findViewById(R.id.bottom_sheet_list);
            this.avListAdapter = new AVListAdapter(this, this.avDataList);
            localListView.setAdapter(this.avListAdapter);
            localListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                    try {
                        currentPlayingIndex = paramAnonymousInt;
                        mediaPlayer.pause();
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(avDataList.get(currentPlayingIndex).av_url);
                        mediaPlayer.prepare();
                        play();
                        updateAVInfo(currentPlayingIndex);
                        avPlayListSheet.dismiss();
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                }
            });
            this.avPlayListSheet.setWidth(ScreenUtils.getScreenWidth(this));
            this.avPlayListSheet.setHeight(ScreenUtils.getScreenHeight(this) - ScreenUtil.getStatusBarHeight(this));
            this.avPlayListSheet.setAnimationStyle(R.style.popmenu_animation);
            this.avPlayListSheet.setContentView(localView);
            this.avPlayListSheet.setBackgroundDrawable(new ColorDrawable());
            this.avPlayListSheet.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    ScreenUtils.setScreenAlpha(HarbourActivity.this, 1.0F);
                }
            });
        }
        ScreenUtils.setScreenAlpha(this, 0.4F);
        this.avListAdapter.setCurrentPlayingIndex(this.currentPlayingIndex);
        this.avPlayListSheet.showAtLocation(getWindow().getDecorView(), 80, 0, 0);
    }

    private void showBottomPopupWindow() {
        if (AppUitls.isLogin()) {
            return;
        }
        if (this.mPoupWindow == null) {
            View contentView = View.inflate(this, R.layout.action_dialog_layout, null);
            this.mPoupWindow = new PopupWindow(contentView, -1, -2);
            this.mPoupWindow.setAnimationStyle(R.style.popmenu_animation);
            this.mPoupWindow.setTouchable(true);
            this.mPoupWindow.setFocusable(true);
            this.mPoupWindow.setOutsideTouchable(true);
            this.mPoupWindow.setTouchInterceptor(new View.OnTouchListener() {
                public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                    return false;
                }
            });
            this.mPoupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    ScreenUtils.setScreenAlpha(HarbourActivity.this, 1.0F);
                }
            });
            ColorDrawable localColorDrawable = new ColorDrawable(Color.parseColor("#A1000000"));
            this.mPoupWindow.setBackgroundDrawable(localColorDrawable);
            this.deleteSelection = contentView.findViewById(R.id.dialog_del);
            this.deleteSelection.setOnClickListener(this);
            this.copySelection = contentView.findViewById(R.id.dialog_copy);
            this.copySelection.setOnClickListener(this);
            this.replySelection = contentView.findViewById(R.id.dialog_reply);
            this.replySelection.setOnClickListener(this);
            contentView.findViewById(R.id.dialog_cancel).setOnClickListener(this);
        }
        deleteSelection.setVisibility(View.GONE);
        if (AppConfig.CustomerId.equals(commentCustomerId)) {
            deleteSelection.setVisibility(View.VISIBLE);
            replySelection.setVisibility(View.GONE);
        }
        ScreenUtils.setScreenAlpha(this, 0.7F);
        this.mPoupWindow.showAtLocation(getWindow().getDecorView(), 80, 0, 0);
    }

    private void updateAVInfo(int paramInt) {
        findAvById(paramInt);
    }

    public void onBackPressed() {
        if ((this.avPlayListSheet != null) && (this.avPlayListSheet.isShowing())) {
            this.avPlayListSheet.dismiss();
            return;
        }
        if ((this.giftWindow != null) && (this.giftWindow.isShowing())) {
            this.giftWindow.dismiss();
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.msg_send})
    public void onClick(final View paramView) {
        switch (paramView.getId()) {
            case R.id.dialog_reply:
                if (!AppUitls.isLogin()) {
                    break;
                }
                if (this.sendContentEd.getHint().toString().contains(DEFUALT_HINT)) {
                    commentAv();
                } else {
                    relyComment();
                }
                break;
            case R.id.dialog_cancel:
                this.mPoupWindow.dismiss();
                break;
            case R.id.msg_send:
                this.mPoupWindow.dismiss();
                this.sendContentEd.findFocus();
                this.sendContentEd.postDelayed(new Runnable() {
                    public void run() {
                        Utils.showKB(sendContentEd, HarbourActivity.this);
                    }
                }, 300L);
                break;
            case R.id.dialog_copy:
                this.mPoupWindow.dismiss();
                Utils.copyText(this, this.copyContent);
                break;
            case R.id.dialog_del:
                this.mPoupWindow.dismiss();
                deleteAvComment();
                break;
            case R.id.fenxiang:
                final AVData avData = this.avDataList.get(this.currentPlayingIndex);
                Glide.with(this.mContext).load(avData.av_img).asBitmap().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap bitmap, String s, Target<Bitmap> target, boolean b, boolean b1) {
                        int random = (int) (Math.random() * 8.0D);
                        if (random == 8) {
                            random = 7;
                        }
                        String title = new String[]{
                                "我的港湾聆听邀请函！！！",
                                "最治愈人心的声音，送给你！",
                                "每天音悦分享【耳朵都要怀孕】",
                                "我有故事，你要听吗？",
                                "嘈杂的生活里，你需要让心灵静下来。",
                                "给你的心一首歌的时间，静下来聆听！",
                                "心灵港湾|我这里有你喜欢听得故事。",
                                "嘘！点开静静地听就好。"}[random];
                        String dec = "名称: " + avData.av_title + " 作者: " + avData.av_author + " 读者: " + avData.av_reader;
                        String shareUrl = UrlConstant.getWebUrl() + "gw_share/index.html?av_id=" + avData.av_id + "&inviteCode=" + (AppConfig.userEntity != null ? AppConfig.userEntity.invite_code : "");
                        Utils.umengShareByList(HarbourActivity.this, bitmap, title, dec, shareUrl);
                        return false;
                    }
                }).preload();

                break;
        }
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        getWindow().setSoftInputMode(32);
        setContentView(R.layout.activity_harbour);
        initBackButton(true);
        setTitleText("港湾");
        String json = getIntent().getStringExtra("avJson");
        if (StringUtils.isEmpty(json)) {
            finish();
            return;
        }
        this.currentPlayingIndex = getIntent().getIntExtra("index", 0);
        this.avDataList = JSON.parseArray(json, AVData.class);
        this.springView = ((SpringView) findViewById(R.id.spring_view));
        this.springView.setHeader(new DefaultHeader(this));
        this.springView.setFooter(new DefaultFooter(this));
        this.springView.setListener(this);
        ListView listView = (ListView) findViewById(R.id.harbour_list);
        initHeader();
        listView.addHeaderView(this.header);
        this.adapter = new AVCommentAdapter();
        listView.setAdapter(this.adapter);
        initPlayer();
        updateAVInfo(this.currentPlayingIndex);
        initSendMessageLayout();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mediaPlayer != null) {
            this.mediaPlayerController.stopMediaPlayer();
        }
    }

    public void onLoadmore() {
        this.page += 1;
        findAVComment();
    }

    public void onRefresh() {
        this.page = 1;
        findAVComment();
    }

    protected void onStop() {
        super.onStop();
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        Utils.hideSystemKeyBoard(this, this.sendContentEd);
        this.sendContentEd.setText("");
        this.sendContentEd.setHint(DEFUALT_HINT);
        this.sendContentEd.clearFocus();
        return super.onTouchEvent(paramMotionEvent);
    }

    public void sendGift() {
        if (this.giftDialogFragment == null) {
            this.giftDialogFragment = new GiftDialogFragment();
        }
        this.giftWindow = this.giftDialogFragment.showBottomPopupWindow(this, getWindow().getDecorView());
        this.giftWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                ScreenUtils.setScreenAlpha(HarbourActivity.this, 1.0F);
            }
        });
        ScreenUtils.setScreenAlpha(this, 0.7F);
        this.giftDialogFragment.setOnSendGiftLister(new GiftDialogFragment.OnSendGiftListener() {
            public void sendGift(GiftAllEntity paramAnonymousGiftAllEntity) {
                if (giftWindow != null) {
                    giftWindow.dismiss();
                    giftWindow = null;
                }
                HashMap<String, String> localHashMap = new HashMap<>();
                localHashMap.put("customer_id", AppConfig.CustomerId);
                localHashMap.put("op_customer_id", "10001");
                localHashMap.put("gift_id", paramAnonymousGiftAllEntity.getId());
                localHashMap.put("gift_count", paramAnonymousGiftAllEntity.getCount());
                localHashMap.put("gift_expense_type", "15");
                localHashMap.put("object_id", avDataList.get(currentPlayingIndex).av_id);
                new NetApi().sendGift(localHashMap, this, new DataCallback<String>() {
                    public void onFailed(String paramAnonymous2String1, String paramAnonymous2String2) {
                    }

                    public void onSuccess(String paramAnonymous2String) {
                        findAvById(currentPlayingIndex);
                    }
                });
            }
        });
    }

    public class AVCommentAdapter extends BaseAdapter {

        private TextView getTextView(AVComment.Rely rely) {
            TextView textView = new TextView(getBaseContext());
            String name = rely.getSurname() + rely.getName();
            String opName = rely.getOp_surname() + rely.getOp_name();
            textView.setTextColor(Color.parseColor("#232323"));
            if (StringUtils.isNotEmpty(opName)) {
                SpannableString ss = new SpannableString(name + " 回复 " + opName + ":" + rely.av_comment_content);
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#2398db")), 0, name.length(), 33);
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#2398db")), name.length() + 4, name.length() + 4 + opName.length(), 33);
                textView.setText(ss);
            } else {
                SpannableString ss = new SpannableString(name + ":" + rely.av_comment_content);
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#2398db")), 0, name.length(), 33);
                textView.setText(ss);
            }
            return textView;
        }

        public int getCount() {
            return commentList.size();
        }

        public Object getItem(int paramInt) {
            return commentList.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, final ViewGroup paramViewGroup) {
            ViewHolder holder = null;
            if (paramView == null) {
                holder = new ViewHolder();
                paramView = View.inflate(getBaseContext(), R.layout.item_action_info_layout, null);
                holder.photo = ((ImageView) paramView.findViewById(R.id.img_action_header));
                holder.name = ((TextView) paramView.findViewById(R.id.txt_action_name));
                holder.age = ((TextView) paramView.findViewById(R.id.txt_action_age));
                holder.address = ((TextView) paramView.findViewById(R.id.txt_action_city));
                holder.content = ((TextView) paramView.findViewById(R.id.txt_item_actioninfo_content));
                holder.relyLayout = ((LinearLayout) paramView.findViewById(R.id.rely_layout));
                holder.relyCnt = ((TextView) paramView.findViewById(R.id.rely_cnt));
                holder.likeCnt = ((TextView) paramView.findViewById(R.id.actioninfo_like_praise_count));
                holder.relyIt = ((ImageView) paramView.findViewById(R.id.actioninfo_message));
                holder.time = ((TextView) paramView.findViewById(R.id.txt_action_time));
                holder.job = ((TextView) paramView.findViewById(R.id.txt_action_job));
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }
            final AVComment avComment = commentList.get(paramInt);

            if (avComment.rely_list != null) {
                for (AVComment.Rely rely : avComment.rely_list) {
                    rely.father_comment_id = avComment.av_comment_id;
                }
            }

            holder.name.setText(String.valueOf(avComment.surname + avComment.name));
            holder.content.setText(avComment.av_comment_content);
            holder.address.setText(avComment.family_area);
            holder.relyLayout.removeAllViews();
            ((ViewGroup) holder.relyLayout.getParent()).setVisibility(View.VISIBLE);
            holder.relyLayout.setVisibility(View.VISIBLE);
            if ((avComment.rely_list == null) || (avComment.rely_list.isEmpty())) {
                ((ViewGroup) holder.relyLayout.getParent()).setVisibility(View.GONE);
            }

            final ViewHolder finalHolder = holder;
            holder.likeCnt.setText(String.valueOf(" " + avComment.av_comment_praise_cnt));
            holder.likeCnt.setSelected("1".equals(avComment.praise_status));
            holder.likeCnt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    avCommentPraise(avComment, finalHolder.likeCnt);
                }
            });

            Utils.setAgeAndSexView(getBaseContext(), holder.age, avComment.sex, avComment.age);
            Glide.with(getBaseContext()).load(avComment.photo).into(holder.photo);
            holder.relyIt.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    commentCustomerId = avComment.customer_id;
                    sendContentEd.setText("");
                    sendContentEd.setHint("回复 " + avComment.surname + avComment.name + " :");
                    copyContent = avComment.av_comment_content;
                    fatherCommentId = avComment.av_comment_id;
                    showBottomPopupWindow();
                }
            });
            paramView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    commentCustomerId = avComment.customer_id;
                    sendContentEd.setText("");
                    sendContentEd.setHint("回复 " + avComment.surname + avComment.name + " :");
                    copyContent = avComment.av_comment_content;
                    fatherCommentId = avComment.av_comment_id;
                    showBottomPopupWindow();
                }
            });

            holder.time.setText(StringUtils.friendlyTime(avComment.av_comment_createtime));
            holder.photo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Intent intent = new Intent(getBaseContext(), WebActivity.class);
                    intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + avComment.customer_id);
                    startActivity(intent);
                }
            });
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            paramInt = Utils.dp2px(getBaseContext(), 2.0F);
            holder.relyLayout.setPadding(paramInt, paramInt, paramInt, paramInt);

            for (final AVComment.Rely rely : avComment.rely_list) {
                TextView localTextView = getTextView(rely);
                localTextView.setTextSize(0, Utils.dp2px(getBaseContext(), 12.0F));
                localTextView.setBackground(getResources().getDrawable(R.drawable.selector_bottom_bar_bg));
                localTextView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        commentCustomerId = rely.customer_id;
                        sendContentEd.setText("");
                        sendContentEd.setHint("回复 " + rely.surname + rely.name + " :");
                        fatherCommentId = rely.father_comment_id;
                        copyContent = rely.av_comment_content;
                        showBottomPopupWindow();
                    }
                });
                if (avComment.rely_list.indexOf(rely) > 1) {
                    localTextView.setVisibility(View.GONE);
                }
                holder.relyLayout.addView(localTextView, layoutParams);
            }

            if (Integer.valueOf(avComment.av_reply_cnt) > 2) {
                holder.relyCnt.setVisibility(View.VISIBLE);
                holder.relyCnt.setText(String.valueOf("共" + avComment.av_reply_cnt + "等回复"));
                holder.relyCnt.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        if (finalHolder.relyLayout.getChildCount() > 1) {
                            View view = finalHolder.relyLayout.getChildAt(2);
                            if (view.getVisibility() == View.VISIBLE) {
                                for (int i = 2; i < finalHolder.relyLayout.getChildCount(); i++) {
                                    View child = finalHolder.relyLayout.getChildAt(i);
                                    child.setVisibility(View.VISIBLE);
                                }
                            } else {
                                for (int i = 2; i < finalHolder.relyLayout.getChildCount(); i++) {
                                    View child = finalHolder.relyLayout.getChildAt(i);
                                    child.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                });
            } else {
                holder.relyCnt.setVisibility(View.GONE);
            }

            return paramView;
        }

        class ViewHolder {
            TextView address;
            TextView age;
            TextView content;
            TextView job;
            TextView likeCnt;
            TextView name;
            ImageView photo;
            TextView relyCnt;
            ImageView relyIt;
            LinearLayout relyLayout;
            TextView time;
        }
    }
}
