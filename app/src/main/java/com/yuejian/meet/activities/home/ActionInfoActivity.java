package com.yuejian.meet.activities.home;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.aztec.AztecDetectorResult;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.api.utils.UtilsIm;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.ExpenseEntity;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.entity.MonyEntity;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.netease.nim.uikit.app.widgets.GiftDialogFragment;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.web.VideoViewActivity;
import com.yuejian.meet.adapters.ActionAdapter;
import com.yuejian.meet.adapters.ActionInfoAdapter;
import com.yuejian.meet.adapters.ActionInfoREwardHeaderAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.ActionCntsEntity;
import com.yuejian.meet.bean.ActionInfo;
import com.yuejian.meet.bean.CommentAllEntity;
import com.yuejian.meet.bean.CommentsEntity;
import com.yuejian.meet.bean.DelActionInfoEntity;
import com.yuejian.meet.bean.DynamicPrivatePicBean;
import com.yuejian.meet.bean.PraiseState;
import com.yuejian.meet.bean.ReplyListEntity;
import com.yuejian.meet.bean.RewardListEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.GlideCircleTransform;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.InnerGridView;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class ActionInfoActivity extends BaseActivity implements SpringView.OnFreshListener {

    @Bind(R.id.actoin_info_layout)
    FrameLayout actoin_info_layout;
    @Bind(R.id.action_info_spring)
    SpringView springView;
    @Bind(R.id.action_info_list)
    ListView listView;
    View viewHeader;
    @Bind(R.id.msg_send)
    Button msg_send;
    @Bind(R.id.msg_content)
    EditText msg_content;
    @Bind(R.id.comment_layout)
    View commentLayout;
    @Bind(R.id.txt_titlebar_cancel)
    TextView deleteAction;
    private long holdTime;
    ImageView img_action_header, img_action_one_img, video_centre_img;
    TextView txt_action_name, txt_action_city, txt_action_age, txt_action_job, txt_action_time, action_comment_count, action_like_praise_count, txt_send_gift, txt_action_content, txt_comment_cnt, txt_replys_sort, gift_cnt;
    GridView actioninfo_header_gridveiw;
    RelativeLayout action_info_header_lf_layout;
    LinearLayout likePraise, ly_comment;
    LinearLayout reward_layout;
    TextView txt_gift_cnt = null;
    TextView txt_action_location;
    LinearLayout ly_action_meet;
    RelativeLayout photo_layout;
    LinearLayout ly_send_gift;
    TextView dialog_del, dialog_reply;
    InnerGridView action_photo_list;
    ActionInfo actionInfo = new ActionInfo();
    ActionInfoAdapter mAdapter;
    ApiImp api = new ApiImp();
    List<CommentsEntity> listData = new ArrayList<>();
    String action_id = "1", comment_id = "1", costomerId = "", commentName = "", content = "";
    Boolean isAction = true, isDelParent;
    int pageIndex = 1;
    int index = 0;
    int maxLen = 500;
    CommentAllEntity commentAllEntity = new CommentAllEntity();
    List<RewardListEntity> rewardListEntities = new ArrayList<>();
    ActionInfoREwardHeaderAdapter actioninfoAdapter;
    LoadingDialogFragment dialog;
    Activity activity;
    private View mPoupView = null, mHintView = null;
    private PopupWindow mPoupWindow = null, mHintWindow = null;
    private int orderIndex = 1;
    public String listLastId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY_NAME = "动态详情";
        setContentView(R.layout.activity_actioln_info);
        dialog = LoadingDialogFragment.newInstance(getString(R.string.in_operation));
        activity = this;
        Intent intent = getIntent();
        if (intent.hasExtra("action_id")) {
            action_id = intent.getStringExtra("action_id");
        }
        comment_id = action_id;
        initData();
    }

    public void initData() {
        viewHeader = View.inflate(this, R.layout.item_action_info_header_layout, null);
        initViewHeader();
        setTitleText(getString(R.string.notice_info_text_1));
        springView.setFooter(new DefaultFooter(this));
        springView.setHeader(new DefaultHeader(this));
        springView.setListener(this);
        msg_send.setSelected(true);
        listView.addHeaderView(viewHeader);
        mAdapter = new ActionInfoAdapter(listView, listData, R.layout.item_action_info_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        getActionInfo();
        getMomments();
        msg_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if (length > maxLen) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, maxLen);
                    msg_content.setText(newStr);
                    editable = msg_content.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
            }
        });
    }

    public void initViewHeader() {
        img_action_header = (ImageView) viewHeader.findViewById(R.id.img_action_header);
        img_action_header.setOnClickListener(this);
        video_centre_img = (ImageView) viewHeader.findViewById(R.id.video_centre_img);
        txt_gift_cnt = (TextView) viewHeader.findViewById(R.id.txt_gift_cnt);
        txt_action_location = (TextView) viewHeader.findViewById(R.id.txt_action_location);
        img_action_one_img = (ImageView) viewHeader.findViewById(R.id.img_action_one_img);
        actioninfo_header_gridveiw = (GridView) viewHeader.findViewById(R.id.actioninfo_header_gridveiw);
        reward_layout = (LinearLayout) viewHeader.findViewById(R.id.reward_layout);
        action_photo_list = (InnerGridView) viewHeader.findViewById(R.id.action_photo_list);
        txt_action_name = (TextView) viewHeader.findViewById(R.id.txt_action_name);
        txt_action_city = (TextView) viewHeader.findViewById(R.id.txt_action_city);
        txt_action_age = (TextView) viewHeader.findViewById(R.id.txt_action_age);
        txt_action_job = (TextView) viewHeader.findViewById(R.id.txt_action_job);
        txt_action_time = (TextView) viewHeader.findViewById(R.id.txt_action_time);
        action_comment_count = (TextView) viewHeader.findViewById(R.id.action_comment_count);
        action_like_praise_count = (TextView) viewHeader.findViewById(R.id.action_like_praise_count);
        txt_send_gift = (TextView) viewHeader.findViewById(R.id.txt_send_gift);
        txt_action_content = (TextView) viewHeader.findViewById(R.id.txt_action_content);
        txt_comment_cnt = (TextView) viewHeader.findViewById(R.id.txt_comment_cnt);
        txt_replys_sort = (TextView) viewHeader.findViewById(R.id.txt_replys_sort);
        gift_cnt = (TextView) viewHeader.findViewById(R.id.gift_cnt);
        ly_send_gift = (LinearLayout) viewHeader.findViewById(R.id.ly_send_gift);
        photo_layout = (RelativeLayout) viewHeader.findViewById(R.id.photo_layout);
        ly_action_meet = (LinearLayout) viewHeader.findViewById(R.id.ly_action_meet);
        action_info_header_lf_layout = (RelativeLayout) viewHeader.findViewById(R.id.action_info_header_lf_layout);
        likePraise = (LinearLayout) viewHeader.findViewById(R.id.ly_like_praise);
        ly_comment = (LinearLayout) viewHeader.findViewById(R.id.ly_comment);
        action_comment_count.setOnClickListener(this);
        txt_replys_sort.setOnClickListener(this);
        action_like_praise_count.setOnClickListener(this);
        ly_send_gift.setOnClickListener(this);
        gift_cnt.setOnClickListener(this);
        reward_layout.setOnClickListener(this);
        likePraise.setOnClickListener(this);
        ly_comment.setOnClickListener(this);
        actioninfoAdapter = new ActionInfoREwardHeaderAdapter(actioninfo_header_gridveiw, rewardListEntities, R.layout.item_action_info_header_reward_img_layout);
        actioninfo_header_gridveiw.setAdapter(actioninfoAdapter);
        actioninfoAdapter.notifyDataSetChanged();
        if (user == null) {
            commentLayout.setVisibility(View.GONE);
        }
        View headerLayout = viewHeader.findViewById(R.id.header_layout);
        headerLayout.setClickable(true);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionInfo == null) return;
                AppUitls.goToPersonHome(mContext, actionInfo.customer_id);
            }
        });
    }

    //获取评论
    public void getMomments() {
        final Map<String, Object> params = new HashMap<>();
        params.put("action_id", action_id);
        params.put("my_customer_id", user == null ? "" : user.getCustomer_id());
        params.put("order_index", String.valueOf(orderIndex));//排序(0:热度(默认), 1:时间)
        params.put("pageIndex", pageIndex + "");
        params.put("pageItemCount", Constants.pageItemCount);
        params.put("listLastId", pageIndex == 1 ? "0" : listLastId);
        api.comments(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex == 1) {
                    listData.clear();
                }
                commentAllEntity = JSON.parseObject(data, CommentAllEntity.class);
                listData.addAll(JSON.parseArray(commentAllEntity.getComment_list(), CommentsEntity.class));
                mAdapter.refresh(listData);
                if (listData.size() > 0) {
                    listLastId = listData.get(listData.size() - 1).getComment_id();
                }
                pageIndex += 1;
                txt_comment_cnt.setText(getString(R.string.action_info_text_7)+" " + commentAllEntity.getComment_cnt());
                if (springView != null)
                    springView.onFinishFreshAndLoad();

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null)
                    springView.onFinishFreshAndLoad();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            listData.remove(index);
            mAdapter.refresh(listData);
            getActionInfo();
            comment_id = action_id;
            msg_content.setHint(R.string.notice_info_text_2);
            isAction = true;
        } else {
            if (requestCode == 12) {
                if (index >= 0)
                    getOneComment();
            }
        }
    }

    /**
     * 获取单条动态评论
     */
    public void getOneComment() {
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", comment_id);
        params.put("my_customer_id", AppConfig.CustomerId);
        api.oneComment(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData.set(index, JSON.parseObject(data, CommentsEntity.class));
                mAdapter.refresh(listData);
                comment_id = action_id;
                msg_content.setHint(R.string.notice_info_text_2);
                isAction = true;
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                comment_id = action_id;
                msg_content.setHint(R.string.notice_info_text_2);
                isAction = true;
            }
        });
    }

    //获取单条动态修信息
    public void getActionInfo() {
        Map<String, Object> params = new HashMap<>();
        params.put("action_id", action_id);
        params.put("my_customer_id", user == null ? "" : user.getCustomer_id());
        api.actionOne(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                actionInfo = JSON.parseObject(data, ActionInfo.class);
                loadListHeaderData(actionInfo);

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                finish();
            }
        });
    }

    //评论刷新
    public void updateHeaderUi(ActionCntsEntity entity) {
        if (entity == null) return;
        String giftCnt = StringUtils.isEmpty(entity.getGift_cnt()) ? "0" : entity.getGift_cnt();
//        gift_cnt.setText(" 共" + giftCnt + "次打赏");
//        txt_gift_cnt.setText(" "+getString(R.string.action_info_text_10) + entity.getGift_cnt() + getString(R.string.action_info_text_8));
        String commentCnt = StringUtils.isEmpty(entity.getComment_cnt()) ? "0" : entity.getComment_cnt();
        txt_comment_cnt.setText(getString(R.string.action_info_text_7) + commentCnt);
        String praiseCnt = StringUtils.isEmpty(entity.getPraise_cnt()) ? "0" : entity.getPraise_cnt();
        action_like_praise_count.setText(" " + praiseCnt);
        if (!giftCnt.equals("0")) action_info_header_lf_layout.setVisibility(View.VISIBLE);
    }

    //更新list头部信息
    public void loadListHeaderData(final ActionInfo actionInfo) {
        final List<DynamicPrivatePicBean> actionPhoto = new ArrayList<>();
        Glide.with(this).load(actionInfo.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into(img_action_header);
        txt_action_name.setText(actionInfo.getSurname() + actionInfo.getName());
        txt_action_city.setText(actionInfo.getFamily_area_name());
        txt_action_age.setSelected(!actionInfo.getSex().equals("0"));
        txt_action_age.setText(" " + actionInfo.getAge());
        txt_action_job.setText(actionInfo.getJob());
        txt_action_time.setText(StringUtils.friendlyTime(actionInfo.getCreate_time()));
        action_comment_count.setText(" " + actionInfo.getComment_cnt());
        txt_comment_cnt.setText(getString(R.string.action_info_text_7)+" " + actionInfo.getComment_cnt());
        action_like_praise_count.setSelected(!actionInfo.getIs_praise().equals("0"));
        action_like_praise_count.setText(" " + actionInfo.getPraise_cnt());
        txt_send_gift.setText(" " + actionInfo.getGift_cnt());
        gift_cnt.setText(" "+getString(R.string.action_info_text_9) + actionInfo.getReward_cnt() + "人");
//        txt_gift_cnt.setText(" "+getString(R.string.action_info_text_9) + actionInfo.getGift_cnt() + getString(R.string.action_info_text_8));
        txt_action_content.setText(actionInfo.getAction_title());

        txt_action_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        holdTime = System.currentTimeMillis() - touchTime;
                        if (copySelectionWindow == null || !copySelectionWindow.isShowing()) {
                            if (holdTime > 300) {
                                showUpCopySelection(txt_action_content, event.getRawX(), event.getRawY());
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        holdTime = 0L;
                        return false;
                }
                return true;
            }
        });

        String gift_cont = StringUtil.isEmpty(actionInfo.getGift_cnt()) ? "0" : actionInfo.getGift_cnt();
        action_info_header_lf_layout.setVisibility(StringUtils.isEmpty(actionInfo.area) ? gift_cont.equals("0") ? View.GONE : View.VISIBLE : View.VISIBLE);
        txt_action_location.setVisibility(StringUtils.isEmpty(actionInfo.area) ? View.GONE : View.VISIBLE);
        txt_action_location.setText(actionInfo.getCity() + "·" + actionInfo.area);
        deleteAction.setText(R.string.delete);
        if (user != null && actionInfo.customer_id.equals(user.customer_id)) {
            deleteAction.setVisibility(View.VISIBLE);
        } else {
            deleteAction.setVisibility(View.GONE);
        }

        ImageView fqr = (ImageView) viewHeader.findViewById(R.id.img_action_sponsor);
        fqr.setVisibility(View.GONE);
        if (actionInfo.getIs_super() > 0) {
            fqr.setVisibility(View.VISIBLE);
            Glide.with(getApplication()).load(actionInfo.is_super == FqrEnum.city.getValue() ? R.mipmap.ic_shi : actionInfo.is_super == FqrEnum.province.getValue() ? R.mipmap.ic_sheng : R.mipmap.ic_guo).asBitmap().into(fqr);
        }
//        if ("0".equals(actionInfo.is_super)) {
//            fqr.setVisibility(View.GONE);
//        } else if ("1".equals(actionInfo.is_super)) {
//            fqr.setVisibility(View.VISIBLE);
//            fqr.setImageDrawable(getResources().getDrawable(R.mipmap.ic_faqr_nationwide));
//        } else if ("2".equals(actionInfo.is_super)) {
//            fqr.setVisibility(View.VISIBLE);
//            fqr.setImageDrawable(getResources().getDrawable(R.mipmap.faqr));
//        }

        actionPhoto.clear();
        String[] action_photo = actionInfo.getAction_url().split(",");
        for (int i = 0; i < action_photo.length; i++) {
            if (!action_photo[i].equals(""))
                actionPhoto.add(new DynamicPrivatePicBean(action_photo[i], "1"));
        }
        action_photo_list.setVisibility(View.GONE);
        photo_layout.setVisibility(View.GONE);
        if (actionPhoto.size() > 0) {
            if (actionPhoto.size() == 1) {
                photo_layout.setVisibility(View.VISIBLE);
                img_action_one_img.setVisibility(View.VISIBLE);
                ImageView one_img = img_action_one_img;
                one_img.setScaleType(ImageView.ScaleType.CENTER);
                final String url_img = actionPhoto.get(0).actionPhoto;
                if (actionInfo.getUrl_type().equals("2")) {
                    if (url_img.contains("_hengping")) {
                        one_img.getLayoutParams().height = (int) (DensityUtils.getScreenW(this) * 0.27);
                        one_img.getLayoutParams().width = (int) (DensityUtils.getScreenW(this) * 0.45);
                    } else {
                        one_img.getLayoutParams().height = (int) (DensityUtils.getScreenW(this) * 0.45);
                        one_img.getLayoutParams().width = (int) (DensityUtils.getScreenW(this) * 0.27);
                    }
                    video_centre_img.setVisibility(View.VISIBLE);
                    Glide.with(this).load(actionInfo.getVideo_first_url()).asBitmap().placeholder(new ColorDrawable(Color.parseColor("#e8e8e8"))).into(one_img);
                } else {
                    if (url_img.contains("_hengping")) {
                        one_img.getLayoutParams().height = (int) (DensityUtils.getScreenW(this) * 0.41);
                        one_img.getLayoutParams().width = (int) (DensityUtils.getScreenH(this) * 0.33);
                    } else {
                        one_img.getLayoutParams().height = (int) (DensityUtils.getScreenH(this) * 0.31);
                        one_img.getLayoutParams().width = (int) (DensityUtils.getScreenW(this) * 0.45);
                    }
                    Glide.with(this).load(url_img).asBitmap().placeholder(R.mipmap.ic_default).into(one_img);
                }
                img_action_one_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtils.isNotEmpty(actionInfo.url_type) && "2".equals(actionInfo.url_type)) {
                            Intent intent = new Intent(getApplication(), VideoViewActivity.class);
                            intent.putExtra(Constants.VIDEO_URL, actionInfo.action_url);
                            startActivity(intent);
                        } else {
                            List<String> list = new ArrayList<String>();
                            list.add(url_img);
                            com.yuejian.meet.utils.Utils.displayImages(activity, list, 0, null);
                        }
                    }
                });
            } else {
                action_photo_list.setVisibility(View.VISIBLE);

                int itemWidth = (DensityUtils.getScreenW(this) - DensityUtils.dip2px(this, 34)) / 3;
                action_photo_list.setColumnWidth(itemWidth);
                if (actionPhoto.size() == 4) {
                    action_photo_list.setNumColumns(2);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) action_photo_list.getLayoutParams();
                    params.width = 2 * itemWidth + DensityUtils.dip2px(this, 4);
                    params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    action_photo_list.setLayoutParams(params);
                    action_photo_list.setPadding(6, 6, 6, 6);
                } else {
                    action_photo_list.setNumColumns(3);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) action_photo_list.getLayoutParams();
                    params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    action_photo_list.setLayoutParams(params);
                }

                ActionAdapter.GvImgAdapter gvImgAdapter = new ActionAdapter.GvImgAdapter(action_photo_list, actionPhoto, R.layout.layout_dynamic_img_item);
                action_photo_list.setAdapter(gvImgAdapter);
                gvImgAdapter.refresh(actionPhoto);
                action_photo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        List<String> list = new ArrayList<String>();
                        for (DynamicPrivatePicBean bean : actionPhoto) {
                            list.add(bean.actionPhoto);
                        }
                        com.yuejian.meet.utils.Utils.displayImages(activity, list, position, null);
                    }
                });
            }
        }
        ly_action_meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImUtils.toP2PCaht(mContext, actionInfo.customer_id);
            }
        });


        //获取点赞头像列表

        List<ActionInfo.PraiseInfo> praiseInfos = null;
        if (actionInfo.praise_list != null && actionInfo.praise_list.size() > 0) {
            viewHeader.findViewById(R.id.praise_list_layout).setVisibility(View.VISIBLE);
            if (actionInfo.praise_list.size() > 7) {
                praiseInfos = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    praiseInfos.add(actionInfo.praise_list.get(i));
                }
            } else {
                praiseInfos = actionInfo.praise_list;
            }
            loadPraiseList(praiseInfos);
        } else {
            viewHeader.findViewById(R.id.praise_list_layout).setVisibility(View.GONE);
        }

        rewardListEntities = JSON.parseArray(actionInfo.getReward_list(), RewardListEntity.class);
//        reward_layout.setVisibility(View.VISIBLE);
        List<RewardListEntity> temps = new ArrayList<>();
        if (rewardListEntities != null && rewardListEntities.size() > 0) {
            if (rewardListEntities.size() > 8) {
                for (int i = 0; i < 8; i++) {
                    temps.add(rewardListEntities.get(i));
                }
                loadGiftList(temps);
            } else {
                loadGiftList(rewardListEntities);
            }
        } else {
            reward_layout.setVisibility(View.GONE);
        }
    }

    public void setSelAdapter(String comment_id, int index) {
        this.index = index;
        this.comment_id = comment_id;
    }

    public String replyCommentId;

    /**
     * 底部PopupWindow
     */
    public void showBottomPopupWindow(String content, String costomerId, String comment_id, Boolean isAction, String commentName, int index) {
        this.content = content;
        this.costomerId = costomerId;
        this.index = index;
//        this.isAction = false;
        this.isDelParent = isAction;
//        this.comment_id = comment_id;
        this.replyCommentId = comment_id;
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
        dialog_del.setVisibility(View.VISIBLE);
        dialog_reply.setVisibility(View.VISIBLE);
        if (costomerId.equals(AppConfig.CustomerId)) {
            dialog_reply.setVisibility(View.GONE);
        } else {
            if (!isAction || !actionInfo.getCustomer_id().equals(AppConfig.CustomerId)) {
                dialog_del.setVisibility(View.GONE);
            }
        }
        mPoupWindow.showAtLocation(actoin_info_layout, Gravity.BOTTOM, 0, 0);
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
        mHintWindow.showAtLocation(actoin_info_layout, Gravity.BOTTOM, 0, 0);
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

    ///评论
    public void commentMsg(String comment_id, Boolean isAction, String commentName, int index) {
        this.index = index;
        this.isAction = isAction;
        this.comment_id = comment_id;
        msg_content.setText("");
        msg_content.setHint(commentName == null ? getString(R.string.notice_info_text_2) : commentName.equals("") ? getString(R.string.notice_info_text_2) : "@" + commentName);
        msg_content.setFocusable(true);
        msg_content.setFocusableInTouchMode(true);
        msg_content.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(msg_content, 0);
    }

    @OnClick({R.id.msg_send, R.id.txt_titlebar_cancel})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.msg_send://发送
                Utils.hintKbTwo((Activity) mContext);
                if (user == null) {
                    ViewInject.shortToast(this, R.string.action_info_text_6);
                    return;
                }
                if (isAction) {
                    comment();
                } else {
                    repleComment();
                }
                break;
            case R.id.action_comment_count://动态评论
                commentMsg(action_id, true, "", 0);
                break;
            case R.id.ly_comment://动态评论
                commentMsg(action_id, true, "", 0);
                break;
            case R.id.action_like_praise_count://点赞
                Praise();
                break;
            case R.id.ly_like_praise://点赞
                Praise();
                break;
            case R.id.txt_replys_sort://排序
                if (getString(R.string.action_info_text_4).equals(txt_replys_sort.getText().toString())) {
                    txt_replys_sort.setText(R.string.action_info_text_5);
                    orderIndex = 0;
                } else {
                    txt_replys_sort.setText(R.string.action_info_text_4);
                    orderIndex = 1;
                }
                pageIndex = 1;
                getMomments();
                break;
            case R.id.ly_send_gift://打赏
                sendActionGift();
                break;
            case R.id.img_action_header:
                if (actionInfo == null) return;
                AppUitls.goToPersonHome(mContext, actionInfo.customer_id);
                break;
            case R.id.gift_cnt://跳转到礼物详情
                AppUitls.goToPersonHome(mContext, actionInfo.customer_id);
                break;
            case R.id.reward_layout://跳转到礼物详情
                AppUitls.goToPersonHome(mContext, actionInfo.customer_id);
                break;
            case R.id.txt_titlebar_cancel:
                showDeleteActionDialog();
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
                ViewInject.toast(getApplicationContext(), R.string.action_info_text_3);
                break;
            case R.id.dialog_reply://回复评论动态
                mPoupWindow.dismiss();
                comment_id = replyCommentId;
                this.isAction = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        commentMsg(comment_id, isAction, commentName, index);
                    }
                }, 500);
                break;
            case R.id.dialog_del_commetn://删除评论
                mHintWindow.dismiss();
                comment_id = replyCommentId;
                delComment();
                break;
            case R.id.dialog_hint_cancel://关闭提示
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
        params.put("comment_id", comment_id);
        params.put("customer_id", AppConfig.CustomerId);
        api.postDelComment(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (dialog != null)
                    dialog.dismiss();
                if (isDelParent) {
                    listData.remove(index);
                } else {
                    DelActionInfoEntity delActionInfoEntity = JSON.parseObject(data, DelActionInfoEntity.class);
                    listData.get(index).setReply_cnt(delActionInfoEntity.getReply_cnt());
                    listData.get(index).setReply_list(delActionInfoEntity.getTop2Replys());
                }
                mAdapter.refresh(listData);
                comment_id = action_id;
                isAction = true;
                getActionInfo();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    GiftDialogFragment editNameDialog;

    //礼物
    public void sendActionGift() {
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (editNameDialog == null) {
            editNameDialog = new GiftDialogFragment();
        }
        final PopupWindow mPoupWindow = editNameDialog.showBottomPopupWindow(this, listView);
        editNameDialog.setOnSendGiftLister(new GiftDialogFragment.OnSendGiftListener() {
            @Override
            public void sendGift(final GiftAllEntity giftBean) {
                if (mPoupWindow != null)
                    mPoupWindow.dismiss();
                Map<String, String> params = new HashMap<>();
                params.put("customer_id", AppConfig.CustomerId);
                params.put("op_customer_id", actionInfo.getCustomer_id());
                params.put("gift_id", giftBean.getId());
                params.put("gift_count", giftBean.getCount());
                params.put("gift_expense_type", "1");//送礼类型(1.动态礼物,2.文章礼物,3.私聊礼物,4.视频礼物)
                params.put("object_id", action_id);
                new NetApi().sendGift(params, this, new DataCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ExpenseEntity expenseEntity = JSON.parseObject(data, ExpenseEntity.class);
                        MonyEntity monyEntity = JSON.parseObject(expenseEntity.getBal(), MonyEntity.class);
                        UtilsIm.setMyMoney(getApplication(), monyEntity);
                        actionInfo.setGift_cnt((Integer.parseInt(giftBean.getCount()) + Integer.parseInt(actionInfo.getGift_cnt())) + "");
                        txt_send_gift.setText(" " + actionInfo.getGift_cnt());
                        getActionInfo();
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg) {
                    }
                });
            }
        });
    }

    //点赞
    public void Praise() {
        Map<String, Object> params = new HashMap<>();
        params.put("action_id", action_id);
        params.put("customer_id", AppConfig.CustomerId);
        api.actionPraise(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                PraiseState state = JSON.parseObject(data, PraiseState.class);
                if (state != null) {
                    action_like_praise_count.setSelected("1".equals(state.is_praise));
                    action_like_praise_count.setText(state.praise_cnt);
                    getActionInfo();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    //动态评论
    public void comment() {
        String content = msg_content.getText().toString();
        if (StringUtil.isEmpty(content)) {
            ViewInject.shortToast(this, R.string.action_info_text_2);
            return;
        } else if (content.length() > 500) {
            return;
        }
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        msg_content.setText("");
        findViewById(R.id.msg_send).setEnabled(false);
        Map<String, Object> params = new HashMap<>();
        params.put("action_id", comment_id);
        params.put("customer_id", user == null ? "" : user.getCustomer_id());
        params.put("comment_content", content);
        api.postSendComment(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                findViewById(R.id.msg_send).setEnabled(true);
                comment_id = action_id;
                msg_content.setHint(R.string.notice_info_text_2);
                isAction = true;
                CommentsEntity entity = JSON.parseObject(data, CommentsEntity.class);
                listData.add(0, entity);
                mAdapter.refresh(listData);
                listView.setSelection(0);
                updateHeaderUi(JSON.parseObject(entity.getAction_cnts(), ActionCntsEntity.class));
                getActionInfo();
                if (dialog != null)
                    dialog.dismiss();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                findViewById(R.id.msg_send).setEnabled(true);
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    ///回复
    public void repleComment() {
        String content = msg_content.getText().toString();
        if ("".equals(content)) {
            ViewInject.shortToast(this, R.string.action_info_text_2);
            return;
        }
        if (dialog != null)
            dialog.show(getFragmentManager(), "");
        msg_content.setText("");
        findViewById(R.id.msg_send).setEnabled(false);
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", comment_id);
        params.put("customer_id", user == null ? "" : user.getCustomer_id());
        params.put("comment_content", content);
        api.replyAction(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Gson gson = new Gson();
                findViewById(R.id.msg_send).setEnabled(true);
                Utils.hintKbTwo((Activity) mContext);
                List<ReplyListEntity> mList = JSON.parseArray(listData.get(index).getReply_list(), ReplyListEntity.class);
                ReplyListEntity entity = JSON.parseObject(data, ReplyListEntity.class);
                if (mList.size() < 2) {
                    mList.add(0, entity);
                } else {
                    mList.remove(1);
                    mList.add(0, entity);
                }
                listData.get(index).setReply_list(gson.toJson(mList));
                listData.get(index).setReply_cnt(StringUtil.isEmpty(listData.get(index).getReply_cnt()) ? "1" : (Integer.parseInt(listData.get(index).getReply_cnt()) + 1) + "");
                mAdapter.refresh(listData);
                comment_id = action_id;
                msg_content.setHint(R.string.notice_info_text_2);
                isAction = true;

                if (dialog != null)
                    dialog.dismiss();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                findViewById(R.id.msg_send).setEnabled(true);
                if (dialog != null)
                    dialog.dismiss();
            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getMomments();
        getActionInfo();
    }

    @Override
    public void onLoadmore() {
        getMomments();
    }

    private void showDeleteActionDialog() {
        Utils.showNoTitleDialog(ActionInfoActivity.this, getString(R.string.action_info_text_1), getString(R.string.confirm), "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(action_id) || user == null || StringUtils.isEmpty(user.customer_id)) {
                    return;
                }
                deleteAction(action_id, user.customer_id);
            }
        }, null);
    }

    /**
     * 删除动态
     *
     * @param actionId
     * @param customerId
     */
    private void deleteAction(final String actionId, String customerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("action_id", actionId);
        params.put("customer_id", customerId);
        api.deleteAction(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Intent intent = new Intent();
                intent.putExtra("del_action", actionId);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    private void loadPraiseList(List<ActionInfo.PraiseInfo> praiseInfos) {
        if (viewHeader == null) {
            return;
        }
        LinearLayout praiseLayout = (LinearLayout) viewHeader.findViewById(R.id.praise_list_layout);
        praiseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionInfoActivity.this, ActionRewardActivity.class);
                intent.putExtra("is_praise", true);
                intent.putExtra("actionId", action_id);
                intent.putExtra("customer_id", actionInfo.customer_id);
                startActivity(intent);
            }
        });
        GridLayout gridLayout = (GridLayout) praiseLayout.findViewById(R.id.praise_header_grid);
        gridLayout.removeAllViews();
        for (final ActionInfo.PraiseInfo info : praiseInfos) {
            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ImageView imageView = new ImageView(this);
            int size = DensityUtils.dip2px(this, 32);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            int margin = DensityUtils.dip2px(this, 4);
            layoutParams.setMargins(margin, 0, 0, 0);
            layout.addView(imageView, layoutParams);
            gridLayout.addView(layout);
            Glide.with(getApplicationContext()).load(info.photo).asBitmap().transform(new GlideCircleTransform(this)).placeholder(R.mipmap.ic_default).into(imageView);
        }
        TextView cnt = (TextView) praiseLayout.findViewById(R.id.praise_cnt);
        cnt.setText(getString(R.string.action_info_text_9) + actionInfo.praise_cnt + "人");
    }

    private void loadGiftList(List<RewardListEntity> entities) {
        if (viewHeader == null) {
            return;
        }
        LinearLayout reward_layout = (LinearLayout) viewHeader.findViewById(R.id.reward_layout);
        reward_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActionInfoActivity.this, ActionRewardActivity.class);
                intent.putExtra("is_praise", false);
                intent.putExtra("actionId", action_id);
                intent.putExtra("customer_id", actionInfo.customer_id);
                startActivity(intent);
            }
        });
        GridLayout gridLayout = (GridLayout) reward_layout.findViewById(R.id.gift_header_grid);
        gridLayout.removeAllViews();
        for (RewardListEntity info : entities) {
            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ImageView imageView = new ImageView(this);
            int size = DensityUtils.dip2px(this, 32);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            int margin = DensityUtils.dip2px(this, 4);
            layoutParams.setMargins(margin, 0, 0, 0);
            layout.addView(imageView, layoutParams);
            gridLayout.addView(layout);
            Glide.with(getApplicationContext()).load(info.photo).asBitmap().transform(new GlideCircleTransform(this)).placeholder(R.mipmap.ic_default).into(imageView);
        }
    }

    private PopupWindow copySelectionWindow = null;
    private long touchTime = 0L;

    private void showUpCopySelection(final TextView target, final float x, final float y) {
        copySelectionWindow = null;
        final int width = DensityUtils.dip2px(this, 80);
        final int height = DensityUtils.dip2px(this, 40);
        copySelectionWindow = new PopupWindow(width, height);
        LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.copy_selection_layout, null);
        TextView copySelect = (TextView) layout.findViewById(R.id.copy);
        copySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = target.getText().toString();
                Utils.copyText(ActionInfoActivity.this, text);
                target.setBackgroundColor(Color.WHITE);
                touchTime = 0;
                copySelectionWindow.dismiss();
            }
        });
        copySelectionWindow.setContentView(layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            copySelectionWindow.setElevation(30f);
        }
        copySelectionWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        copySelectionWindow.setOutsideTouchable(true);
        target.setBackgroundColor(Color.parseColor("#DCDCDC"));
        target.postDelayed(new Runnable() {
            @Override
            public void run() {
                View parent = (View) target.getParent();
                int targetX = (int) (x - width);
                int targetY = (int) (y - height);
                copySelectionWindow.showAtLocation(parent, Gravity.TOP | Gravity.LEFT, targetX, targetY);
            }
        }, 200);

        copySelectionWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                target.setBackgroundColor(Color.WHITE);
            }
        });
    }
}
