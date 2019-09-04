package com.netease.nim.uikit.app.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.utils.PreferencesIm;
import com.netease.nim.uikit.api.utils.PreferencesMessage;
import com.netease.nim.uikit.api.utils.Utils;
import com.netease.nim.uikit.app.Adapter.GiftDetailAdapter2;
import com.netease.nim.uikit.app.Adapter.GiftPagerAdapter;
import com.netease.nim.uikit.app.Adapter.LineGridView;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.GiftAllEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * Created by zh03 on 2017/7/5.
 */

public class GiftDialogFragment extends DialogFragment implements View.OnClickListener {


    ViewPager mGiftVpagerContainer;
    ChatGiftIndicatorView mLlPointGroup;
    LinearLayout gift_ll_recharge;
    TextView gift_tv_czpay;
    TextView user_number_money;
    EditText giftCntTextView;
    ImageView gift_cnt_layout;
    private List<GridView> mGiftViews;
    Activity mContext;
    private List<List<GiftAllEntity>> mListGv;
    private GiftPagerAdapter giftPagerGvAdapter;
    private RelativeLayout main_content;
    private GiftAllEntity mCurrentGift;
    private OnSendGiftListener sendGiftLister;
    private int preclickPagerIndex = -1;
    private int preclickItemIndex = -1;
    private static final int PAGESIZE = 8;
    private View mPoupView = null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;
    private View gift_cnt_dialog = null;
    private int giftCnt = 1;
    private View father;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        Dialog dialog = new Dialog(mContext, R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.gif_dialog_layout);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.alpha = 0.8f;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        mGiftVpagerContainer = (ViewPager) dialog.findViewById(R.id.gift_vpager_container);
        gift_cnt_dialog = (View) dialog.findViewById(R.id.gift_cnt_dialog);
        mLlPointGroup = (ChatGiftIndicatorView) dialog.findViewById(R.id.ll_point_group);
        gift_ll_recharge = (LinearLayout) dialog.findViewById(R.id.gift_ll_recharge);
        gift_tv_czpay = (TextView) dialog.findViewById(R.id.gift_tv_czpay);
        user_number_money = (TextView) dialog.findViewById(R.id.user_number_money);
        gift_cnt_layout = (ImageView) dialog.findViewById(R.id.gift_cnt_layout);
        giftCntTextView = (EditText) dialog.findViewById(R.id.gift_cnt);
        user_number_money.setText(AppConfig.moneySun + "");
        gift_ll_recharge.setOnClickListener(recharge);
        gift_tv_czpay.setOnClickListener(payListener);
        gift_cnt_layout.setOnClickListener(giftCntListener);
        ButterKnife.bind(this, dialog); // Dialog即View
        return dialog;
    }

    /**
     * 底部PopupWindow
     */
    public PopupWindow showBottomPopupWindow(final Activity activity, View mLayout) {
        mContext = activity;
        mInflater = LayoutInflater.from(activity);
        mPoupView = mInflater.inflate(R.layout.gif_dialog_layout, null);
        bindPopMenuEvent(mPoupView);
        mPoupWindow = new PopupWindow(mPoupView, Utils.getScreenW(activity), Utils.dp2px(activity, 330));
        mPoupWindow.setTouchable(true);
        mPoupWindow.setFocusable(true);
        mPoupWindow.setOutsideTouchable(true);
        mPoupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        mPoupWindow.setBackgroundDrawable(dw);
        mPoupWindow.showAtLocation(mLayout, Gravity.BOTTOM, 0, 0);
        initData();
        mPoupView.setClickable(true);
        mPoupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPoupWindow.dismiss();
            }
        });
        return mPoupWindow;
    }

    private static final DisplayMetrics displayMetrics = new DisplayMetrics();

    /**
     * 实例化底部pop菜单项
     *
     * @param view
     */
    private void bindPopMenuEvent(View view) {
        mGiftVpagerContainer = (ViewPager) view.findViewById(R.id.gift_vpager_container);
        mLlPointGroup = (ChatGiftIndicatorView) view.findViewById(R.id.ll_point_group);
        gift_tv_czpay = (TextView) view.findViewById(R.id.gift_tv_czpay);
        gift_ll_recharge = (LinearLayout) view.findViewById(R.id.gift_ll_recharge);
        user_number_money = (TextView) view.findViewById(R.id.user_number_money);
        gift_cnt_layout = (ImageView) view.findViewById(R.id.gift_cnt_layout);
        main_content = (RelativeLayout) view.findViewById(R.id.main_content);
        user_number_money.setText(AppConfig.moneySun + "");
        father = view.findViewById(R.id.father);
        giftCntTextView = (EditText) view.findViewById(R.id.gift_cnt);
        gift_ll_recharge.setOnClickListener(recharge);
        gift_tv_czpay.setOnClickListener(payListener);
        gift_cnt_dialog = mPoupView.findViewById(R.id.gift_cnt_dialog);
        gift_cnt_layout.setOnClickListener(giftCntListener);

        TextView cnt1 = (TextView) gift_cnt_dialog.findViewById(R.id.cnt_1);
        TextView cnt10 = (TextView) gift_cnt_dialog.findViewById(R.id.cnt_10);
        TextView cnt66 = (TextView) gift_cnt_dialog.findViewById(R.id.cnt_66);
        TextView cnt99 = (TextView) gift_cnt_dialog.findViewById(R.id.cnt_99);
        TextView cnt188 = (TextView) gift_cnt_dialog.findViewById(R.id.cnt_188);
        TextView cnt520 = (TextView) gift_cnt_dialog.findViewById(R.id.cnt_520);
        TextView cnt1314 = (TextView) gift_cnt_dialog.findViewById(R.id.cnt_1314);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof TextView) {
                    TextView textView = (TextView) v;
                    giftCnt = Integer.valueOf(textView.getText().toString());
                    giftCntTextView.setText("" + giftCnt);
                    gift_cnt_dialog.setVisibility(View.GONE);
                }
            }
        };
        cnt1.setOnClickListener(clickListener);
        cnt10.setOnClickListener(clickListener);
        cnt66.setOnClickListener(clickListener);
        cnt99.setOnClickListener(clickListener);
        cnt188.setOnClickListener(clickListener);
        cnt520.setOnClickListener(clickListener);
        cnt1314.setOnClickListener(clickListener);


        final int tempheight = displayMetrics.heightPixels / 4;
        mContext.getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Rect rect = new Rect();
                mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                if(bottom != 0 && oldBottom != 0 && (rect.bottom >1800 || bottom - rect.bottom <= 0) && KeyStatus != STATUS_HIDE) {
                    mGiftVpagerContainer.setVisibility(View.VISIBLE);
                    KeyStatus = STATUS_HIDE;
                }else if(bottom!=0 && oldBottom!=0 && bottom - rect.bottom > tempheight &&rect.bottom<1810 && KeyStatus != STATUS_SHOW){
                    mGiftVpagerContainer.setVisibility(View.GONE);
                    KeyStatus = STATUS_SHOW;
                }
            }
        });
    }

    public static final int STATUS_SHOW = 201;
    public static final int STATUS_HIDE = 202;
    private int KeyStatus = 0;
    private int myBottom = 0;

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }


    @Override
    public void onResume() {
        super.onResume();
        DecimalFormat df = new DecimalFormat("0.00");
        user_number_money.setText(df.format(AppConfig.moneySun) + "");
    }

    //充值
    public View.OnClickListener recharge = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BusCallEntity busCallEntity = new BusCallEntity();
            busCallEntity.setCallType(BusEnum.In_CashActivity);
            Bus.getDefault().post(busCallEntity);
        }
    };
    /**
     * 选择礼物发送
     */
    public View.OnClickListener payListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (PreferencesMessage.get(mContext,PreferencesMessage.family_id+AppConfig.CustomerId,"0").equals("0")){
                final AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                builder.setMessage("您需要先填写所在地方可进行消费");
                builder.setPositiveButton("前往填写", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        BusCallEntity busCallEntity=new BusCallEntity();
                        busCallEntity.setCallType(BusEnum.Bangding_Family);
                        Bus.getDefault().post(busCallEntity);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.show();
                return;
            }
            if (sendGiftLister != null && mCurrentGift != null) {
                if (StringUtil.isEmpty(giftCntTextView.getText().toString())) {
                    Toast.makeText(mContext, "请填写礼物数量", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (giftCntTextView.getText().toString().equals("0")) {
                    Toast.makeText(mContext, "礼物数量不能为0", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCurrentGift.setCount(giftCntTextView.getText().toString());
                sendGiftLister.sendGift(mCurrentGift);
            } else {
                Toast.makeText(mContext, "请选择礼物", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //选择礼物数据
    public View.OnClickListener giftCntListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (gift_cnt_dialog != null) {
                gift_cnt_dialog.setVisibility(gift_cnt_dialog.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void initData() {
        mListGv = new ArrayList<>();
        int screenWidth = (int) (AppConfig.width);
        int spacing = Utils.dp2px(mContext, 1);
        int itemWidth = screenWidth / 5;
        int heighttv = Utils.dp2px(mContext, 33);
        int gvHeight = (screenWidth/2);
        List<GiftAllEntity> giftimg = JSON.parseArray(Utils.s2tOrT2s(PreferencesIm.get(mContext, PreferencesIm.gift_image, "")), GiftAllEntity.class);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        mGiftVpagerContainer.setLayoutParams(params);
        mGiftViews = new ArrayList<>();
        initPager(screenWidth, gvHeight, giftimg);
//        mLlPointGroup.initIndicator(mGiftViews.size());
        giftPagerGvAdapter = new GiftPagerAdapter(mGiftViews);
        mGiftVpagerContainer.setAdapter(giftPagerGvAdapter);
//        initListener();
    }

    private void initListener() {
        mGiftVpagerContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPagerPos = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mLlPointGroup.playByStartPointToNext(oldPagerPos, position);
                oldPagerPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPager(int screenWidth, int gvHeight, List<GiftAllEntity> giftimg) {
        List<GiftAllEntity> giftPrices = new ArrayList<>();
        // 遍历所有的表情的key
        int pagerindex = 0;
        for (GiftAllEntity giftBean : giftimg) {
            giftPrices.add(giftBean);
            if (giftPrices.size() == PAGESIZE) {
                mListGv.add(giftPrices);
                GridView gv = createGiftGridView(giftPrices, screenWidth, gvHeight, pagerindex);
                mGiftViews.add(gv);
//                // 添加完一组表情,重新创建一个表情名字集合
                giftPrices = new ArrayList<>();
                pagerindex++;
            }
        }
//         判断最后是否有不足8个表情的剩余情况
        if (giftPrices.size() > 0) {
            mListGv.add(giftPrices);
            GridView gv = createGiftGridView(giftPrices, screenWidth, gvHeight, pagerindex);
            mGiftViews.add(gv);
        }
    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createGiftGridView(List<GiftAllEntity> giftList, int gvWidth, int gvHeight, int pagerindex) {
        // 创建GridView
        LineGridView gv = new LineGridView(mContext);
//        设置点击背景透明
        gv.setSelector(R.drawable.selector_select_gift_bg);
        //设置4列
        gv.setNumColumns(PAGESIZE / 2);
        gv.setHorizontalSpacing(0);
        gv.setVerticalSpacing(0);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        GiftDetailAdapter2 adapter = new GiftDetailAdapter2(gv, mContext, giftList);
        gv.setAdapter(adapter);
        //设置全局点击事件
        gv.setOnItemClickListener(new ItemClickListener(pagerindex));
        return gv;
    }

    @Override
    public void onClick(View v) {
    }

    class ItemClickListener implements AdapterView.OnItemClickListener {
        private int pageindex;

        ItemClickListener(int pageindex) {
            this.pageindex = pageindex;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<GiftAllEntity> giftBeen = mListGv.get(pageindex);
            mCurrentGift = giftBeen.get(position);
            if (preclickPagerIndex != -1 && preclickItemIndex != -1) {//改变以前的
                mListGv.get(preclickPagerIndex).get(preclickItemIndex).isSelected = false;
                BaseAdapter adapter = (BaseAdapter) mGiftViews.get(preclickPagerIndex).getAdapter();
                adapter.notifyDataSetChanged();
            }
            mCurrentGift.isSelected = !mCurrentGift.isSelected;
            int clickPagerIndex = pageindex;

            BaseAdapter adapter = (BaseAdapter) mGiftViews.get(clickPagerIndex).getAdapter();
            adapter.notifyDataSetChanged();
            preclickPagerIndex = pageindex;
            preclickItemIndex = position;
        }
    }

    public void setOnSendGiftLister(OnSendGiftListener sendGiftLister) {
        this.sendGiftLister = sendGiftLister;
    }

    public interface OnSendGiftListener {
        void sendGift(GiftAllEntity giftBean);
    }
}
