package com.yuejian.meet.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.api.NetApi;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.netease.nim.uikit.app.widgets.GiftDialogFragment;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.ActionInfoActivity;
import com.yuejian.meet.activities.home.ActionRewardActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.web.VideoViewActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.ActionEntity;
import com.yuejian.meet.bean.DynamicPrivatePicBean;
import com.yuejian.meet.bean.PraiseState;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.InnerGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh03 on 2017/6/13.
 * 动态adpter
 */

public class ActionAdapter extends FKAdapter<ActionEntity> {
    private AdapterHolder mHelper;
    private Activity context;
    private ApiImp api = new ApiImp();
    private View view;
    private ColorDrawable placeHolder = null;
    private long touchTime;
    private long holdTime;

    public ActionAdapter(AbsListView view, List<ActionEntity> mDatas, int itemLayoutId, Activity activity) {
        super(view, mDatas, itemLayoutId);
        this.context = activity;
        this.view = view;
        addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (copySelectionWindow != null && copySelectionWindow.isShowing()) {
                    copySelectionWindow.dismiss();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    public void convert(AdapterHolder helper, ActionEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        placeHolder = new ColorDrawable(Color.parseColor("#e8e8e8"));
        initNearByData(helper, item, position);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initNearByData(final AdapterHolder helper, final ActionEntity item, final int position) {
        this.mHelper = helper;
        mHelper.setText(R.id.txt_action_name, item.getSurname() + item.getName());
        mHelper.setText(R.id.txt_action_city, item.getFamily_area_name());
        if (item.getSex().equals("1")) {
            mHelper.getView(R.id.txt_action_age).setSelected(true);
        } else {
            mHelper.getView(R.id.txt_action_age).setSelected(false);
        }
        mHelper.setText(R.id.txt_action_age, " " + item.getAge());
        mHelper.setText(R.id.txt_action_job, item.getJob());
        mHelper.setText(R.id.txt_action_time, StringUtils.friendlyTime(item.getCreate_time()));

        ImageView fqr = mHelper.getView(R.id.img_action_sponsor);
        fqr.setVisibility(View.GONE);
        if (item.getIs_super()>0){
            fqr.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:item.is_super== FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).asBitmap().into(fqr);
        }
//        if ("0".equals(item.is_super) || StringUtils.isEmpty(item.is_super)) {
//        } else if ("1".equals(item.is_super)) {
//            fqr.setVisibility(View.VISIBLE);
//            fqr.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_faqr_nationwide));
//        } else if ("2".equals(item.is_super)) {
//            fqr.setVisibility(View.VISIBLE);
//            fqr.setImageDrawable(context.getResources().getDrawable(R.mipmap.faqr));
//        }
        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.img_action_header));
//        mHelper.getView(R.id.txt_gift_cnt).setVisibility(StringUtils.isEmpty(item.getGift_cnt()) ? View.GONE : item.getGift_cnt().equals("0") ? View.GONE : View.VISIBLE);
        mHelper.setText(R.id.txt_gift_cnt, " "+context.getString(R.string.action_info_text_10) + item.getGift_cnt() + context.getString(R.string.action_info_text_8));
        mHelper.setText(R.id.txt_action_location, " " + (item.getCity() + "·" + item.getArea()));
        mHelper.getView(R.id.txt_action_location).setVisibility(StringUtils.isEmpty(item.area) ? View.GONE : View.VISIBLE);
        mHelper.getView(R.id.location_mark).setVisibility(StringUtils.isEmpty(item.area) ? View.GONE : View.VISIBLE);
        mHelper.setText(R.id.action_comment_count, " " + item.getComment_cnt());
        mHelper.setText(R.id.action_like_praise_count, " " + item.getPraise_cnt());
        mHelper.getView(R.id.action_like_praise_count).setSelected(!(StringUtils.isEmpty(item.getIs_praise()) || "0".equals(item.getIs_praise())));
        mHelper.setText(R.id.txt_send_gift, " " + item.getGift_cnt());

        mHelper.getView(R.id.ly_like_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    ImUtils.hintLogin(context);
                    return;
                }
                Map<String, Object> params = new HashMap<>();
                params.put("action_id", item.action_id);
                params.put("customer_id", AppConfig.CustomerId);
                api.actionPraise(params, mCxt, new DataIdCallback<String>() {
                    @Override
                    public void onSuccess(String data, int id) {
                        PraiseState state = JSON.parseObject(data, PraiseState.class);
                        if (state != null) {
                            helper.getView(R.id.action_like_praise_count).setSelected("1".equals(state.is_praise));
                            item.setPraise_cnt(state.praise_cnt);
                            item.setIs_praise(state.is_praise);
                            helper.setText(R.id.action_like_praise_count, item.getPraise_cnt());
//                            mHelper.setText(R.id.txt_gift_cnt, " "+context.getString(R.string.action_info_text_10) + (Integer.valueOf(item.getGift_cnt()) + 1) + context.getString(R.string.action_info_text_8));
                        }
                    }

                    @Override
                    public void onFailed(String errCode, String errMsg, int id) {
                    }
                });
            }
        });
        mHelper.getView(R.id.ly_action_meet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImUtils.toP2PCaht(context, item.getCustomer_id());
            }
        });
        mHelper.getView(R.id.ly_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    ImUtils.hintLogin(context);
                    return;
                }
                Intent intent = new Intent(context, ActionInfoActivity.class);
                intent.putExtra("action_id", item.getAction_id());
                context.startActivity(intent);
            }
        });
        sendActionGift(helper, item, position);
        //处理图片列表
        handlePicList(helper, item, position);
        mHelper.setClickListener(R.id.img_action_header, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUitls.goToPersonHome(mCxt, item.customer_id);
            }
        });

        mHelper.setClickListener(R.id.action_root_layout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    ImUtils.hintLogin(context);
                    return;
                }
                Intent intent = new Intent(context, ActionInfoActivity.class);
                intent.putExtra("action_id", item.getAction_id());
                context.startActivity(intent);
            }
        });

        final TextView content = mHelper.getView(R.id.txt_action_content);

        if (StringUtils.isNotEmpty(item.action_title)) {
            content.setVisibility(View.VISIBLE);
            mHelper.setText(R.id.txt_action_content, item.action_title);
        } else {
            content.setVisibility(View.GONE);
        }

        final CheckBox showUpCheck = mHelper.getView(R.id.show_all_content);
        content.post(new Runnable() {
            @Override
            public void run() {
                if (content.getLineCount() > 6) {
                    showUpCheck.setVisibility(View.VISIBLE);
                } else {
                    showUpCheck.setVisibility(View.GONE);
                }
            }
        });

        content.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                                showUpCopySelection(content, event.getRawX(), event.getRawY());
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

        showUpCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showUpCheck.isChecked()) {
                    showUpCheck.setText(R.string.action_text_1);
                    content.setMaxLines(Integer.MAX_VALUE);
                    content.postInvalidate();
                } else {
                    showUpCheck.setText(R.string.action_text_2);
                    content.setMaxLines(6);
                    content.postInvalidate();
                }
            }
        });

        mHelper.setClickListener(R.id.txt_gift_cnt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCxt, ActionRewardActivity.class);
                intent.putExtra("is_praise", false);
                intent.putExtra("actionId", item.action_id);
                intent.putExtra("customer_id", item.customer_id);
                mCxt.startActivity(intent);
            }
        });
    }

    private GiftDialogFragment editNameDialog;

    //礼物
    public void sendActionGift(final AdapterHolder helper, final ActionEntity item, final int position) {
        helper.setText(R.id.txt_send_gift, " " + item.getGift_cnt());
        mHelper.getView(R.id.ly_send_gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AppConfig.CustomerId)) {
                    context.startActivity(new Intent(context, LoginActivity.class));
                    return;
                }
                if (editNameDialog == null) {
                    editNameDialog = new GiftDialogFragment();
                }
                final PopupWindow mPoupWindow = editNameDialog.showBottomPopupWindow(context, view);
                editNameDialog.setOnSendGiftLister(new GiftDialogFragment.OnSendGiftListener() {
                    @Override
                    public void sendGift(final GiftAllEntity giftBean) {
                        if (mPoupWindow != null)
                            mPoupWindow.dismiss();
                        Map<String, String> params = new HashMap<>();
                        params.put("customer_id", AppConfig.CustomerId);
                        params.put("op_customer_id", item.getCustomer_id());
                        params.put("gift_id", giftBean.getId());
                        params.put("gift_count", giftBean.getCount());
                        params.put("gift_expense_type", "1");//送礼类型(1.动态礼物,2.文章礼物,3.私聊礼物,4.视频礼物)
                        params.put("object_id", item.getAction_id());
                        new NetApi().sendGift(params, this, new DataCallback<String>() {
                            @Override
                            public void onSuccess(String data) {
                                item.setGift_cnt((Integer.parseInt(giftBean.getCount()) + Integer.parseInt(item.getGift_cnt())) + "");
                                helper.setText(R.id.txt_send_gift, " " + item.getGift_cnt());
                            }

                            @Override
                            public void onFailed(String errCode, String errMsg) {
                                Toast.makeText(view.getContext(), errMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }

    /**
     * 图片处理Adapter
     *
     * @param helper
     * @param item
     * @param itemPosition
     */
    private void handlePicList(AdapterHolder helper, final ActionEntity item, final int itemPosition) {
        InnerGridView innerGridView = helper.getView(R.id.action_photo_list);
        innerGridView.setVisibility(View.VISIBLE);
        final List<DynamicPrivatePicBean> actionPhoto = new ArrayList<>();
        String[] action_photo = item.getAction_url().split(",");
        for (int i = 0; i < action_photo.length; i++) {
            if (StringUtils.isNotEmpty(action_photo[i])) {
                actionPhoto.add(new DynamicPrivatePicBean(action_photo[i], "1"));
            }
        }
        mHelper.getView(R.id.video_centre_img).setVisibility(View.GONE);
        innerGridView.setVisibility(View.GONE);
        mHelper.getView(R.id.img_action_one_img).setVisibility(View.GONE);
        if (actionPhoto.size() == 0) {
            return;
        }
        if (actionPhoto.size() == 1) {
            final String url_img = actionPhoto.get(0).actionPhoto;
            ImageView one_img = mHelper.getView(R.id.img_action_one_img);
            one_img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mHelper.getView(R.id.img_action_one_img).setVisibility(View.VISIBLE);
            if ("2".equals(item.url_type)) {
                if (url_img.contains("_hengping")) {
                    one_img.getLayoutParams().height = DensityUtils.dip2px(mCxt, 100);
                    one_img.getLayoutParams().width = DensityUtils.dip2px(mCxt, 178);
                } else {
                    one_img.getLayoutParams().height = DensityUtils.dip2px(mCxt, 178);
                    one_img.getLayoutParams().width = DensityUtils.dip2px(mCxt, 100);
                }
                mHelper.getView(R.id.video_centre_img).setVisibility(View.VISIBLE);
                Glide.with(context).load(item.getVideo_first_url()).placeholder(placeHolder).error(R.mipmap.load_error).into(one_img);
            } else {
                if (url_img.contains("_hengping")) {
                    one_img.getLayoutParams().height = DensityUtils.dip2px(mCxt, 140);
                    one_img.getLayoutParams().width = DensityUtils.dip2px(mCxt, 222);
                } else {
                    one_img.getLayoutParams().height = DensityUtils.dip2px(mCxt, 222);
                    one_img.getLayoutParams().width = DensityUtils.dip2px(mCxt, 178);
                }
                Glide.with(context).load(url_img).placeholder(placeHolder).error(R.mipmap.load_error).into(one_img);
            }

            mHelper.setClickListener(R.id.img_action_one_img, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtils.isNotEmpty(item.url_type) && "2".equals(item.url_type)) {
                        Intent intent = new Intent(context, VideoViewActivity.class);
                        intent.putExtra(Constants.VIDEO_URL, item.action_url);
                        context.startActivity(intent);
                    } else {
                        List<String> list = new ArrayList<String>();
                        list.add(url_img);
                        com.yuejian.meet.utils.Utils.displayImages(context, list, 0, null);
                    }
                }
            });

        } else {
            innerGridView.setVisibility(View.VISIBLE);
            int itemWidth = (DensityUtils.getScreenW(mCxt) - DensityUtils.dip2px(mCxt, 34)) / 3;
            innerGridView.setColumnWidth(itemWidth);
            if (actionPhoto.size() == 4) {
                innerGridView.setNumColumns(2);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) innerGridView.getLayoutParams();
                params.width = 2 * itemWidth + DensityUtils.dip2px(mCxt, 4);
                params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                innerGridView.setLayoutParams(params);
                innerGridView.setPadding(6, 6, 6, 6);
            } else {
                innerGridView.setNumColumns(3);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) innerGridView.getLayoutParams();
                params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                innerGridView.setLayoutParams(params);
            }
            GvImgAdapter gvImgAdapter = new GvImgAdapter(innerGridView, actionPhoto, R.layout.layout_dynamic_img_item);
            final List<DynamicPrivatePicBean> tmps = new ArrayList<>();
            tmps.addAll(actionPhoto);
            innerGridView.setAdapter(gvImgAdapter);
            gvImgAdapter.refresh(actionPhoto);
            innerGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    List<String> list = new ArrayList<String>();
                    for (DynamicPrivatePicBean bean : tmps) {
                        list.add(bean.actionPhoto);
                    }
                    com.yuejian.meet.utils.Utils.displayImages(context, list, position, null);
                }
            });
            innerGridView.setOnTouchBlankPositionListener(new InnerGridView.OnTouchBlankPositionListener() {
                @Override
                public void onTouchBlank(MotionEvent event) {
                    if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                        ImUtils.hintLogin(context);
                        return;
                    }
                    Intent intent = new Intent(context, ActionInfoActivity.class);
                    intent.putExtra("action_id", item.getAction_id());
                    context.startActivity(intent);
                }
            });
        }
    }

    private PopupWindow copySelectionWindow = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showUpCopySelection(final TextView target, final float x, final float y) {
        copySelectionWindow = null;
        final int width = DensityUtils.dip2px(mCxt, 80);
        final int height = DensityUtils.dip2px(mCxt, 40);
        copySelectionWindow = new PopupWindow(width, height);
        LinearLayout layout = (LinearLayout) View.inflate(mCxt, R.layout.copy_selection_layout, null);
        TextView copySelect = (TextView) layout.findViewById(R.id.copy);
        copySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = target.getText().toString();
                Utils.copyText(mCxt, text);
                target.setBackgroundColor(Color.WHITE);
                touchTime = 0;
                copySelectionWindow.dismiss();
            }
        });
        copySelectionWindow.setContentView(layout);
        copySelectionWindow.setElevation(30f);
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

    public static class GvImgAdapter extends FKAdapter<DynamicPrivatePicBean> {
        private int mWidth = 0;
        private ColorDrawable placeHolder = new ColorDrawable(Color.parseColor("#e8e8e8"));

        public GvImgAdapter(AbsListView view, List<DynamicPrivatePicBean> mDatas, int itemLayoutId) {
            super(view, mDatas, itemLayoutId);
            mWidth = (DensityUtils.getScreenW(mCxt) - DensityUtils.dip2px(mCxt, 34)) / 3;
        }

        @Override
        public void convert(AdapterHolder helper, DynamicPrivatePicBean item, boolean isScrolling, int position) {
            super.convert(helper, item, isScrolling, position);
            helper.getView(R.id.img_dynamic_pic_item).getLayoutParams().width = mWidth;
            helper.getView(R.id.img_dynamic_pic_item).getLayoutParams().height = mWidth;
            Glide.with(mCxt).load(item.actionPhoto).asBitmap()
                    .centerCrop()
                    .placeholder(placeHolder)
                    .error(R.mipmap.load_error)
                    .into((ImageView) helper.getView(R.id.img_dynamic_pic_item));
        }
    }
}
