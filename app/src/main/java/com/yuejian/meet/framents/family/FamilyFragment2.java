package com.yuejian.meet.framents.family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.netease.nim.uikit.common.util.C;
import com.netease.nim.uikit.common.util.storage.StorageType;
import com.netease.nim.uikit.common.util.storage.StorageUtil;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.session.activity.CaptureVideoActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.clan.MainClanActivity;
import com.yuejian.meet.activities.common.ChooseIndustryActivity;
import com.yuejian.meet.activities.common.SelectMemberCityActivity;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.activities.home.ActionMessageActivity;
import com.yuejian.meet.activities.home.EnjoyActivity;
import com.yuejian.meet.activities.home.HistoryEventActivity;
import com.yuejian.meet.activities.home.ReleaseActionActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.search.MainSearchActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.adapters.ActionAdapter;
import com.yuejian.meet.adapters.MemberAdapter;
import com.yuejian.meet.adapters.MemberAdapter2;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.ActionAllEntity;
import com.yuejian.meet.bean.ActionEntity;
import com.yuejian.meet.bean.ActionUnreadMsgEntity;
import com.yuejian.meet.bean.BannerData;
import com.yuejian.meet.bean.FamilyMasterEntity;
import com.yuejian.meet.bean.MemberCntEntity;
import com.yuejian.meet.bean.MembersAllEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.bean.RegionEntity;
import com.yuejian.meet.bean.SigninDataEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.framents.ui.MemberFragmentHeaderUi;
import com.yuejian.meet.framents.ui.SiginWindow;
import com.yuejian.meet.ui.MainMoreUi;
import com.yuejian.meet.utils.DeviceUtils;
import com.yuejian.meet.utils.ImgUtils;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.TimeUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.DoubleSeekBar;
import com.yuejian.meet.widgets.InnerListView;
import com.yuejian.meet.widgets.MyObservableScrollView;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 家族
 */
/**
 * @author :
 * @time   : 2018/11/12 14:21
 * @desc   :
 * @version: V1.0
 * @update : 2018/11/12 14:21
 */

public class FamilyFragment2 extends BaseFragment implements MyObservableScrollView.OnObservableScrollViewScrollChanged ,SpringView.OnFreshListener{
    @Bind(R.id.more_all_alyout_bg)
    RelativeLayout more_all_alyout_bg;
    @Bind(R.id.search_all_layout_bg)
    RelativeLayout search_all_layout_bg;
    MyObservableScrollView secroll_view;
    @Bind(R.id.bars_bg)
    TextView bars_bg;
    @Bind(R.id.maia_layout_title_bar)
    RelativeLayout maia_layout_title_bar;
    Banner banner;
    @Bind(R.id.ll_fixedView)
    LinearLayout ll_fixedView;
    @Bind(R.id.ll_topView)
    LinearLayout ll_topView;
    @Bind(R.id.top_table)
    LinearLayout tv_topView;

    @Bind(R.id.member_list)
    InnerListView listView;

    @Bind(R.id.action_lsit)
    InnerListView action_lsit;
    @Bind(R.id.delow_screen1)
    ImageView delow_screen1;
    @Bind(R.id.delow_screen2)
    ImageView delow_screen2;
    @Bind(R.id.below_line1)
    View below_line1;
    @Bind(R.id.below_line2)
    View below_line2;
    @Bind(R.id.member_surname)
    TextView member_surname;
    @Bind(R.id.member_city)
    TextView member_city;
    @Bind(R.id.member_header_layout)
    LinearLayout member_header_layout;
    @Bind(R.id.select_list_composing)
    ImageView select_list_composing;
    @Bind(R.id.action_spring)
    SpringView springView;
    @Bind(R.id.action_top_header)
    LinearLayout action_top_header;
    @Bind(R.id.member_content_layout)
    LinearLayout member_content_layout;
    TextView member_list_load;
    View viewHeader,viewLoadMore,viewAction,actionFooterview,member_top_header;
    MemberFragmentHeaderUi memberFragmentHeaderUi;
    private MainMoreUi mainMoreUi;

    private View mPoupView = null,ActionpouqView=null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;
    //用来记录内层固定布局到屏幕顶部的距离
    private int mHeight;
    int table_sel=1;
    ViewGroup.LayoutParams params;
    ViewGroup.LayoutParams paramsFixe,paramsViewMemberHeader;
    MemberAdapter mAdapter;
    MemberAdapter2 mAdapterTwo;
    int selVisibleIndex;
    private int mTotalItemCount;//item总数
    private boolean mIsLoading = false;//是否正在加载
    List<MembersEntity> listData = new ArrayList<>();
    Intent intent;
    int height=0;

    View viewDailog,viewVideo;
    @Bind(R.id.rl_action_talk)
    LinearLayout rl_action_talk;
    RelativeLayout action_msg_layout;
    TextView txt_action_surname, txt_action_city,txt_action_all,txt_action_attention;
    @Bind(R.id.txt_action_collect_message)
    TextView txt_action_collect_message;
    @Bind(R.id.img_collect_header)
    ImageView img_collect_header;
    ActionAdapter actionAdapter;
    List<ActionEntity> actionListData = new ArrayList<>();
    ActionUnreadMsgEntity actionUnreadMsg = new ActionUnreadMsgEntity();
//    item_dialog_ligin_widdow_layout
    SiginWindow siginWindow;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_family_fragment2, container, false);
    }
    public void initView(){
        temporarySex=getString(R.string.surnames_all);
        surname = getString(R.string.surnames_all);
        province = getString(R.string.nationwide);
        memberSex = getString(R.string.surnames_all);
        jobs = getString(R.string.surnames_all);
        actionSurname=getString(R.string.surnames_all);
        actionProvince=getString(R.string.nationwide);
        action_type=getString(R.string.surnames_all);
        viewHeader = fragmentRootView.findViewById(R.id.member_sponsor);
        member_top_header = fragmentRootView.findViewById(R.id.member_top_header);
        viewAction = fragmentRootView.findViewById(R.id.action_layout);
        memberFragmentHeaderUi = new MemberFragmentHeaderUi(this, getContext(), viewHeader);
        select_list_composing.setSelected(isSelList);
        viewLoadMore = View.inflate(getContext(), R.layout.member_list_load_more, null);
        member_list_load = (TextView) viewLoadMore.findViewById(R.id.member_list_load);
        initactionView();

        springView.setHeader(new DefaultHeader(getContext()));
        springView.setFooter(new DefaultFooter(getContext()));
        springView.setListener(this);
        siginWindow=new SiginWindow(getActivity());
        if (user !=null){
            surname=user.getSurname();
            member_surname.setText(Utils.s2tOrT2s(surname+""));
        }
    }
    public void signin(){

//        siginWindow.showBottomPopupWindow(secroll_view);
//        siginWindow.setContext("您已连续签到"+11+"天，再接再励哦 本次修行值+10",1);
        if (user==null ||  TimeUtils.getDateDay(System.currentTimeMillis()).equals(PreferencesUtil.get(getContext(),PreferencesUtil.siginDate,""))){
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        apiImp.customerSignIn(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                PreferencesUtil.put(getContext(),PreferencesUtil.siginDate, TimeUtils.getDateDay(System.currentTimeMillis()));
                SigninDataEntity signinDataEntity=JSON.parseObject(data,SigninDataEntity.class);
                siginWindow.showBottomPopupWindow(secroll_view);
                siginWindow.setContext(getString(R.string.Continuous_sign_in)+signinDataEntity.getDays()+getString(R.string.Continuous_sign_in2),1);
            }

            @Override

            public void onFailed(String errCode, String errMsg, int id) {
                if (errMsg.contains("你今天已签到过了")){
                    PreferencesUtil.put(getContext(),PreferencesUtil.siginDate, TimeUtils.getDateDay(System.currentTimeMillis()));
                }
//                PreferencesUtil.put(getContext(),PreferencesUtil.siginDate, TimeUtils.getDateDay(System.currentTimeMillis()));
            }
        });
    }
    public void initactionView(){
        actionFooterview=View.inflate(getContext(), R.layout.item_action_dynamic_layout, null);
        actionFooterview.setVisibility(View.INVISIBLE);
        action_msg_layout = (RelativeLayout) fragmentRootView.findViewById(R.id.action_msg_layout);
        actionAdapter = new ActionAdapter(action_lsit, actionListData, R.layout.item_action_dynamic_layout, getActivity());
        action_lsit.setAdapter(actionAdapter);
//        action_lsit.addFooterView(actionFooterview);
//        action_lsit.addFooterView(viewLoadMore);
        actionAdapter.notifyDataSetChanged();

        requestActionData();
    }
    public static boolean ispause=false;

    @Override
    protected void initData() {
        super.initData();
        secroll_view= (MyObservableScrollView) fragmentRootView.findViewById(R.id.secroll_view);
        initView();
        params=ll_topView.getLayoutParams();
        paramsFixe=ll_fixedView.getLayoutParams();
        paramsViewMemberHeader=viewHeader.getLayoutParams();
        secroll_view.setOnObservableScrollViewScrollChanged(this);

        mAdapterTwo = new MemberAdapter2(getContext(), listData);
        mAdapter = new MemberAdapter(listView, listData, R.layout.item_member_fragment);
        listView.setAdapter(isSelList ? mAdapter : mAdapterTwo);
        listView.addFooterView(viewLoadMore);

        getRequstData();
        initBanner();

        signin();
//        secroll_view.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE :
//                        height++;
//                        break;
//                }
//
//                if (event.getAction() == MotionEvent.ACTION_UP &&  height > 0) {
//
//                    height = 0;
//                    View view = ((ScrollView) v).getChildAt(0);
//
//                    if (view.getMeasuredHeight() <= v.getScrollY() + v.getHeight()) {
//                        //　到达底部了
//                        mIsLoading = true;
////                        pageIndex += 1;
////                        getRequstData();
//                    }
//                }
//
//                return false;
//            }
//        });
    }
    public String getSurname() {
        return surname;
    }
    public String getDistrict() {
        return areaName;
    }

    private void initBanner() {
        banner = ((Banner) fragmentRootView.findViewById(R.id.banner));
        banner.setImageLoader(new GlideImageLoader());
        this.banner.isAutoPlay(true);
        this.banner.setBannerStyle(1);
        this.banner.setIndicatorGravity(6);
        banner.setDelayTime(4000);
        this.apiImp.findBanner(new HashMap<String, Object>(), this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(final String paramAnonymousString, int paramAnonymousInt) {
                final List<BannerData> bannerData = JSON.parseArray(paramAnonymousString, BannerData.class);
                ArrayList<String> images = new ArrayList<>();
                ArrayList<String> titles = new ArrayList<>();

                if (bannerData != null && !bannerData.isEmpty()) {
                    for (BannerData bd : bannerData) {
                        images.add(bd.banner_img);
                        titles.add(bd.banner_title);
                    }

                    banner.update(images, titles);
                    banner.setOnBannerListener(new OnBannerListener() {
                        public void OnBannerClick(int position) {
                            String str = bannerData.get(position).banner_link;
                            if (!StringUtil.isEmpty(str)){
                                Intent localIntent = new Intent(getActivity(), WebActivity.class);
                                localIntent.putExtra("No_Title", true);
                                localIntent.putExtra("url", str);
                                startActivity(localIntent);
                            }
                        }
                    });
                }
            }
        });
    }

    @OnClick({R.id.family_member,R.id.zuci_layout,R.id.nearby_clan_layout,R.id.family_action_layout,R.id.navbar_more,R.id.layout_search,
    R.id.name_layout,R.id.member_city,R.id.delow_screen1,R.id.select_list_composing,
    R.id.delow_screen2,R.id.rl_action_talk,R.id.layout_action_message,R.id.family_enjoy,R.id.srunname_origin})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.srunname_origin:
                if (AppConfig.userEntity == null) {
                    this.intent = new Intent(getActivity(), LoginActivity.class);
                }else {
                    startActivity(new Intent(getActivity(), HistoryEventActivity.class));
                }
                break;
            case R.id.family_enjoy:

                startActivity(new Intent(getContext(),EnjoyActivity.class));
                break;
            case R.id.layout_search:
                startActivity(new Intent(getContext(), MainSearchActivity.class));
                break;
            case R.id.navbar_more:
                if(mainMoreUi == null) {
                    mainMoreUi = new MainMoreUi(getActivity());
                }
                mainMoreUi.showBottomPopupWindow(fragmentRootView.findViewById(R.id.maia_layout_title_bar));
                break;
            case R.id.family_member:
                startActivity(new Intent(getContext(), MainClanActivity.class));
                break;
            case R.id.zuci_layout:
                if (AppConfig.userEntity == null) {
                    this.intent = new Intent(getActivity(), LoginActivity.class);
                }else {
                    this.intent = new Intent(getActivity(), WebActivity.class);
                    this.intent.putExtra("No_Title", true);
                    String url = UrlConstant.ExplainURL.STAR_ZPU + "?customer_id=" + AppConfig.CustomerId + "&surname=" + this.user.getSurname();
                    this.intent.putExtra("url", url);
                }
                startActivity(intent);
//                startActivity(new Intent(getContext(), ZuciMainActivity.class));
                break;
            case R.id.nearby_clan_layout:
                if (table_sel==1){
                    showBottomPopupWindow();
                    return;
                }
                action_top_header.setVisibility(View.GONE);
                member_top_header.setVisibility(View.VISIBLE);

                member_content_layout.setVisibility(View.VISIBLE);
                viewAction.setVisibility(View.GONE);
                action_lsit.setVisibility(View.GONE);
                table_sel=1;
                getmLayoutHeight();
                delow_screen2.setVisibility(View.INVISIBLE);
                below_line2.setVisibility(View.INVISIBLE);
                delow_screen1.setVisibility(View.VISIBLE);
                below_line1.setVisibility(View.VISIBLE);
                new Handler().postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        secroll_view.scrollTo(0,0);
                    }
                },5000);
                break;
            case R.id.family_action_layout:
                if (table_sel==2){
                    actionWindow();
                    return;
                }
                member_top_header.setVisibility(View.GONE);
                action_top_header.setVisibility(View.VISIBLE);
                member_content_layout.setVisibility(View.GONE);
                viewAction.setVisibility(View.VISIBLE);
                action_lsit.setVisibility(View.VISIBLE);
                table_sel=2;
                getmLayoutHeight();
                delow_screen1.setVisibility(View.INVISIBLE);
                below_line1.setVisibility(View.INVISIBLE);
                delow_screen2.setVisibility(View.VISIBLE);
                below_line2.setVisibility(View.VISIBLE);
                new Handler().postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        secroll_view.scrollTo(0,0);
                    }
                },8000);
//                if (actionListData.size()==0){
//                    requestActionData();
//                }
                break;
            case R.id.name_layout://姓氏
                intent = new Intent(getContext(), SurnameActivity.class);
                if (user != null) {
                    intent.putExtra("meSurname", user.getSurname());
                } else {
                    intent.putExtra("meSurname", getString(R.string.surnames_all));
                }
                startActivity(intent);
                break;
            case R.id.delow_screen1://宗亲成员选择
                showBottomPopupWindow();
                break;
            case R.id.delow_screen2://动态条件选择
                actionWindow();
                break;
            case R.id.member_city://城市
                if (AppConfig.city != null) {
                    intent = new Intent(getContext(), SelectMemberCityActivity.class);
                    if (StringUtils.isAutonomy(AppConfig.province)) {
                        intent.putExtra("city", AppConfig.province);
                    } else {
                        intent.putExtra("city", AppConfig.district);
                    }
                    intent.putExtra("isSovereignty", false);
                    startActivity(intent);
                }
                break;
            case R.id.member_choose_job://选择行业
                intent = new Intent(getContext(), ChooseIndustryActivity.class);
                startActivity(intent);
                break;
            case R.id.member_moer_confirm://确定
                jobs = txt_member_job.getText().toString().trim();
                age = tv_age_range.getText().toString().replace("-", ",");
                memberSex = temporarySex;
                pageIndex = 1;
                mPoupWindow.dismiss();
                getRequstData();
                break;
            case R.id.txt_member_sex_all://全部
                temporarySex = getString(R.string.surnames_all);
                txt_member_sex_women.setSelected(false);
                txt_member_sex_man.setSelected(false);
                txt_member_sex_all.setSelected(true);
                break;
            case R.id.txt_member_sex_man://男
                temporarySex = getString(R.string.reg_rb_man);
                txt_member_sex_women.setSelected(false);
                txt_member_sex_man.setSelected(true);
                txt_member_sex_all.setSelected(false);
                break;
            case R.id.txt_member_sex_women://女
                temporarySex = getString(R.string.reg_rb_wonman);
                txt_member_sex_women.setSelected(true);
                txt_member_sex_man.setSelected(false);
                txt_member_sex_all.setSelected(false);
                break;
            case R.id.select_list_composing://list排序
                isSelList = !isSelList;
                select_list_composing.setSelected(isSelList);
                if (!isSelList) {
                    listView.setAdapter(mAdapterTwo);
                    mAdapterTwo.notifyDataSetChanged();
                } else {
                    listView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                if (selVisibleIndex > 4)
                    listView.setSelection(isSelList ? selVisibleIndex : (selVisibleIndex / 2));
                break;
                ////动态------/////////////
            case R.id.rl_action_talk://说说
                if (user == null) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    showDialog();
                }
                break;
            case R.id.txt_action_dialog_issue://发布动态
                intent = new Intent(getContext(), ReleaseActionActivity.class);
                startActivityForResult(intent, 119);
                dialog.dismiss();
                break;
            case R.id.txt_action_dialog_video://视频
//                chooseVideoFromCamera();
                dialog.dismiss();
                selVideo();
                break;
            case R.id.txt_action_dialog_photograph_img://拍照
                dialog.dismiss();
                showSelector(false);
                break;
            case R.id.txt_action_dialog_select_img://选择本地照片
                dialog.dismiss();
                showSelector(true);
                break;
            case R.id.txt_action_dialog_shoot:///拍摄
                dialog.dismiss();
                chooseVideoFromCamera();
                break;
            case R.id.txt_action_dialog_locality:///本地选择视频
                dialog.dismiss();
                if (Build.VERSION.SDK_INT >= 19) {
                    new Utils().chooseVideoFromLocalKitKat(getActivity(), 113);
                } else {
                    new Utils().chooseVideoFromLocalBeforeKitKat(getActivity(), 113);
                }
                break;
            case R.id.close_member_dialog:
                mPoupWindow.dismiss();
                break;
            case R.id.close_action_dialog:
                mPoupWindow.dismiss();
                break;
            case R.id.sel_action_submit:///确定重新查询
                mPoupWindow.dismiss();
                actionPage=1;
                requestActionData();
                break;
            case R.id.txt_action_all:
                action_type=getString(R.string.txt_comment_all);
                txt_action_attention.setSelected(true);
                txt_action_all.setSelected(false);
                break;
            case R.id.txt_action_attention:
                action_type=getString(R.string.attention);
                txt_action_attention.setSelected(false);
                txt_action_all.setSelected(true);
                break;
            case R.id.action_choose_surname://姓氏
                intent = new Intent(getContext(), SurnameActivity.class);
                if (user != null)
                    intent.putExtra("meSurname", user.getSurname());
                else
                    intent.putExtra("meSurname", getString(R.string.txt_comment_all));
                startActivity(intent);
                break;
            case R.id.actoin_choose_city://城市
                if (AppConfig.city != null) {
                    intent = new Intent(getContext(), SelectMemberCityActivity.class);
                    if (StringUtils.isAutonomy(AppConfig.province)) {
                        intent.putExtra("city", AppConfig.province);
                    } else {
                        intent.putExtra("city", AppConfig.district);
                    }
                    startActivity(intent);
                }
                break;
            case R.id.layout_action_message:
                intent = new Intent(mContext, ActionMessageActivity.class);
                startActivityForResult(intent, 119);
                break;
        }
    }
    /**
     * 底部PopupWindow
     */
    private void showBottomPopupWindow() {
        if (mPoupView == null) {
            mInflater = LayoutInflater.from(getContext());
            mPoupView = mInflater.inflate(R.layout.activity_member_more_select, null);
            bindPopMenuEvent(mPoupView);
        }
            mPoupWindow = new PopupWindow(mPoupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            mPoupWindow.setAnimationStyle(R.style.PopupAnimation);
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
            ColorDrawable dw = new ColorDrawable(00000000);
            mPoupWindow.setBackgroundDrawable(dw);
        mPoupWindow.showAtLocation(secroll_view, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.7f);
    }
    TextView txt_member_job, tv_age_range, txt_member_sex_all, txt_member_sex_man, txt_member_sex_women;
    DoubleSeekBar dsb;
    String temporarySex="";
    /**
     * 实例化底部pop菜单项
     *
     * @param view
     */
    private void bindPopMenuEvent(View view) {
        RelativeLayout member_choose_job = (RelativeLayout) view.findViewById(R.id.member_choose_job);
        Button member_moer_confirm = (Button) view.findViewById(R.id.member_moer_confirm);
        txt_member_job = (TextView) view.findViewById(R.id.txt_member_job);
        tv_age_range = (TextView) view.findViewById(R.id.tv_age_range);
        txt_member_sex_all = (TextView) view.findViewById(R.id.txt_member_sex_all);
        txt_member_sex_man = (TextView) view.findViewById(R.id.txt_member_sex_man);
        txt_member_sex_women = (TextView) view.findViewById(R.id.txt_member_sex_women);
        if (getString(R.string.surnames_all).equals(memberSex)) {
            txt_member_sex_all.setSelected(true);
        } else if (getString(R.string.reg_rb_man).equals(memberSex)) {
            txt_member_sex_man.setSelected(true);
        } else {
            txt_member_sex_women.setSelected(true);
        }
        dsb = (DoubleSeekBar) view.findViewById(R.id.dsb);
        tv_age_range.setText(age.replace(",", "-"));
        txt_member_job.setText(jobs + " ");
        String[] stringAge = age.split(",");
        dsb.initProgress(Integer.parseInt(stringAge[0]), Integer.parseInt(stringAge[1]));
        member_choose_job.setOnClickListener(this);
        member_moer_confirm.setOnClickListener(this);
        view.findViewById(R.id.close_member_dialog).setOnClickListener(this);
        txt_member_sex_all.setOnClickListener(this);
        txt_member_sex_man.setOnClickListener(this);
        txt_member_sex_women.setOnClickListener(this);
        dsb.setOnSeekBarChangeListener(new DoubleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(DoubleSeekBar seekBar, int progressLow, int progressHigh) {
                tv_age_range.setText(progressLow + "-" + progressHigh);
            }

            @Override
            public void onProgressAfter() {
            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    /**
     * 动态条件PopupWindow
     */
    private void actionWindow() {
        if (ActionpouqView == null) {
            mInflater = LayoutInflater.from(getContext());
            ActionpouqView = mInflater.inflate(R.layout.layout_action_select_header, null);
            actionBindMenuEvent(ActionpouqView);
        }
            mPoupWindow = new PopupWindow(ActionpouqView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            mPoupWindow.setAnimationStyle(R.style.PopupAnimation);
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
            ColorDrawable dw = new ColorDrawable(00000000);
            mPoupWindow.setBackgroundDrawable(dw);
        mPoupWindow.showAtLocation(secroll_view, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.4f);
    }
    public void actionBindMenuEvent(View view){
        view.findViewById(R.id.action_choose_surname).setOnClickListener(this);
        view.findViewById(R.id.actoin_choose_city).setOnClickListener(this);
        view.findViewById(R.id.close_action_dialog).setOnClickListener(this);
        view.findViewById(R.id.sel_action_submit).setOnClickListener(this);
        txt_action_all = (TextView) view.findViewById(R.id.txt_action_all);
        txt_action_attention = (TextView) view.findViewById(R.id.txt_action_attention);
        txt_action_all.setOnClickListener(this);
        txt_action_attention.setOnClickListener(this);
        txt_action_attention.setSelected(true);
        txt_action_city = (TextView) view.findViewById(R.id.txt_action_city);
        txt_action_surname = (TextView) view.findViewById(R.id.txt_action_surname);
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        super.onBusCallback(event);
        if (ispause){
            return;
        }
        if (table_sel==1){
            if (event.getCallType() == BusEnum.LOGIN_UPDATE) {
                member_surname.setText(getString(R.string.surnames_all));
                member_city.setText(getString(R.string.nationwide));
                surname = user.getSurname();
                member_surname.setText(surname);
                province = getString(R.string.nationwide);
                pageIndex = 1;
                getRequstData();
            } else if (event.getCallType() == BusEnum.LOCATION_UPDATE) {
            } else if (event.getCallType() == BusEnum.SURNAME_UPDATE) {
                surname = event.getData();
                pageIndex = 1;
                member_surname.setText(surname);
                getRequstData();
            } else if (event.getCallType() == BusEnum.district) {
                isSelProvince = false;
                if (AppConfig.district.equals(event.getData())) {
                    province = AppConfig.province;
                    city = AppConfig.city;
                    district = AppConfig.district;
                    member_city.setText(district.equals("") ? city : district);
                    getAreaName(province,city,district);
                    return;
                } else if (StringUtils.isAutonomy(event.getData())) {
                    province = event.getData();
                    city = event.getData();
                    district = event.getData();
                } else if (getString(R.string.surnames_all).equals(event.getData())) {
                    province = getString(R.string.nationwide);
                    district = getString(R.string.nationwide);
                } else {
                    if (event.getStateType().equals("2")) {
                        city = event.getData();
                        district = "";
                    } else {
                        district = event.getData();
                    }
                }
                areaName = event.getAreaName();
                member_city.setText(district.equals("") ? city : district);
                pageIndex = 1;
                getRequstData();
            } else if (event.getCallType() == BusEnum.CITY) {
                isSelProvince = false;
                city = event.getData();
            } else if (event.getCallType() == BusEnum.PROVINCE) {
                province = event.getData();
                pageIndex = 1;
                if (event.getSelProvince()) {
                    isSelProvince = true;
                    areaName = province;
                    member_city.setText(province);
                    getRequstData();
                }
            } else if (event.getCallType() == BusEnum.JOBS) {
                if (txt_member_job != null)
                    txt_member_job.setText(event.getData() + " ");
            } else if (event.getCallType() == BusEnum.NETWOR_CONNECTION) {
                if (listData.size() == 0)
                    getRequstData();
            }
        }else {///动态
            if (event.getCallType() == BusEnum.SURNAME_UPDATE) {
                surname = event.getData();
                txt_action_surname.setText(surname);
                actionPage = 1;
//                requestData();
            } else if (event.getCallType() == BusEnum.district) {
                if (AppConfig.district.equals(event.getData())) {
                    actionProvince = AppConfig.province;
                    actionCity = AppConfig.city;
                    actionAreaName = AppConfig.district;
                    getAreaName(actionProvince,actionCity,actionAreaName);
                } else if (StringUtils.isAutonomy(event.getData())) {
                    actionProvince = event.getData();
                    actionCity = event.getData();
                    actionAreaName = event.getAreaName();
                } else if (getString(R.string.surnames_all).equals(event.getData())) {
                    actionProvince = getString(R.string.nationwide);
                    actionAreaName = getString(R.string.nationwide);
                } else {
                    if (event.getStateType().equals("2")) {
                        actionCity = event.getData();
                        actionAreaName = "";
                    } else {
                        actionAreaName = event.getAreaName();
                    }
                }
                areaName = event.getAreaName();
                txt_action_city.setText(StringUtil.isEmpty(actionAreaName) ? actionCity : actionAreaName);
                pageIndex = 1;
//                requestData();
            } else if (event.getCallType() == BusEnum.CITY) {
                actionCity = event.getData();
            } else if (event.getCallType() == BusEnum.PROVINCE) {
                actionProvince = event.getData();
            } else if (event.getCallType() == BusEnum.ACTION_UNREAD) {
                loadUnread();
            }else if (event.getCallType()==BusEnum.NETWOR_CONNECTION){
                if (listData.size()==0){
                    requestActionData();
                }
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasChanged) {
        super.onWindowFocusChanged(hasChanged);
        if(hasChanged){
            //获取HeaderView的高度，当滑动大于等于这个高度的时候，需要把topView移除当前布局，放入到外层布局
            mHeight=ll_topView.getTop()-maia_layout_title_bar.getBottom();
        }
    }

//    @Override
//    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//        if(y>=mHeight){
//            if(tv_topView.getParent()!=ll_fixedView){
////                params.height=DeviceUtils.dipToPX(getActivity(),45);
////                ll_topView.setLayoutParams(params);
//                getmLayoutHeight();
////                ll_topView.removeView(tv_topView);
//                ll_topView.removeAllViews();
//                ll_fixedView.addView(tv_topView);
//                ll_fixedView.addView(viewHeader);
//            }
//        }else {
//            if(tv_topView.getParent()!=ll_topView){
//                getmLayoutHeight();
////                ll_fixedView.removeView(tv_topView);
//                ll_fixedView.removeAllViews();
//                ll_topView.addView(tv_topView);
//                ll_topView.addView(viewHeader);
//            }
//        }
//    }
    public void getmLayoutHeight(){
//        if (false){
            int height=0;
            if (table_sel==1 && member_header_layout.getVisibility() == View.VISIBLE){
                height=DeviceUtils.dipToPX(getActivity(),45+40);
            }else if (table_sel==1){
                height=DeviceUtils.dipToPX(getActivity(),45+40);
            }else {
                height=DeviceUtils.dipToPX(getActivity(),45+48);
            }
            paramsFixe.height=height;
            ll_fixedView.setLayoutParams(paramsFixe);
            params.height=height;
            ll_topView.setLayoutParams(params);
//        }
    }

//    @Override
//    public void onScrollDistance(int distance) {
//
//    }

    @Override
    public void onRefresh() {
        if (table_sel==1){
            pageIndex=1;
            getRequstData();
        }else {
            actionPage=1;
            requestActionData();
        }
    }

    @Override
    public void onLoadmore() {
        if (table_sel==1){
            pageIndex+=1;
            getRequstData();
        }else {
            actionPage+=1;
            requestActionData();
        }
    }
    int action_y=0;
    int member_y=0;

    @Override
    public void onObservableScrollViewScrollChanged(int l, int y, int oldl, int oldt) {
        if (table_sel==1){
            member_y=y;
        }else {
            action_y=y;
        }
        if(y>=mHeight){
            if(tv_topView.getParent()!=ll_fixedView){
                bars_bg.setAlpha(1);
                search_all_layout_bg.setSelected(true);
                more_all_alyout_bg.setSelected(true);
//                params.height=DeviceUtils.dipToPX(getActivity(),45);
//                ll_topView.setLayoutParams(params);
                getmLayoutHeight();
//                ll_topView.removeView(tv_topView);
                ll_topView.removeAllViews();
                ll_fixedView.addView(tv_topView);
//                ll_fixedView.addView(viewHeader);
            }
        }else {
            if (y<=20){
                bars_bg.setAlpha(0);
            }else {
                bars_bg.setAlpha((float)y/(float) mHeight);
            }
            if(tv_topView.getParent()!=ll_topView){
                bars_bg.setAlpha(0);
                search_all_layout_bg.setSelected(false);
                more_all_alyout_bg.setSelected(false);
                getmLayoutHeight();
//                ll_fixedView.removeView(tv_topView);
                ll_fixedView.removeAllViews();
                ll_topView.addView(tv_topView);
//                ll_topView.addView(viewHeader);
            }
        }
    }

    private class GlideImageLoader extends ImageLoader {
        private GlideImageLoader() {
        }

        public void displayImage(Context paramContext, Object paramObject, ImageView paramImageView) {
            Glide.with(paramContext).load(paramObject).into(paramImageView);
        }

    }
    /////////宗亲会
    boolean isSelList = true;
    int pageIndex = 1;//第几页
    public String surname ;
    String province;
    String city = "";
    String memberSex ;
    String jobs ;
    String district = "";
    String areaName = "";
    String age = "0,100";
    boolean isRequst = false, isSelProvince = false;
    //请求数据
    private void getRequstData() {
        if ("".equals(province)) {
            return;
        }
        final Map<String, Object> params = new HashMap<>();
        params.put("pageItemCount", Constants.pageItemCount);
        params.put("pageIndex", pageIndex + "");
//        if (StringUtils.isAutonomy(province)) {
//            params.put("province", StringUtils.getTopPrticularly(province));
//        } else {
        if (isSelProvince || province.equals(getString(R.string.nationwide)))
            params.put("province", province);
        if (district.equals(getString(R.string.surnames_all)) || district.equals(getString(R.string.nationwide))) {
            params.put("area_name", "");
        } else {
//            if (!isSelProvince || surname.equals(getString(R.string.surnames_all)))
            params.put("area_name", areaName);
        }
//        }
        if (!memberSex.equals(getString(R.string.surnames_all))) {
            params.put("sex", memberSex.equals(getString(R.string.reg_rb_man)) ? "1" : "0");
        }
        params.put("industry", getString(R.string.surnames_all).equals(jobs) ? "" : jobs);
        params.put("surname", surname == null ? "" : surname);
        params.put("age_range", age);
        params.put("latitude", "0".equals(AppConfig.slatitude) ? "" : AppConfig.slatitude);
        params.put("longitude", "0".equals(AppConfig.slongitude) ? "" : AppConfig.slongitude);
        apiImp.getMember(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex == 1) {
                    listData.clear();
                }
                try {
                    final MembersAllEntity membersAllEntity = JSON.parseObject(data, MembersAllEntity.class);
                    List<MembersEntity> membersEntities = JSON.parseArray(membersAllEntity.getMembers(), MembersEntity.class);
                    if (membersEntities.size() < Integer.parseInt(Constants.pageItemCount)) {
                        member_list_load.setText(R.string.spring_text9);
                    } else {
                        member_list_load.setText(R.string.spring_text8);
                        mIsLoading = false;
                    }
                    listData.addAll(membersEntities);
                    if (pageIndex == 1) {
                        MemberCntEntity memberCntEntity = JSON.parseObject(membersAllEntity.getCnt(), MemberCntEntity.class);
                        String familyState = "2";
                        FamilyMasterEntity familyMasterEntity = null;
                        if (province.equals(getString(R.string.nationwide)) && !StringUtil.isEmpty(membersAllEntity.getSurnameMaster())) {
                            familyState = "1";
                            familyMasterEntity = JSON.parseObject(membersAllEntity.getSurnameMaster(), FamilyMasterEntity.class);
                        } else if (isSelProvince) {
                            familyState = "3";
                            familyMasterEntity = JSON.parseObject(membersAllEntity.getProvinceMaster(), FamilyMasterEntity.class);
                        } else {
                            familyMasterEntity = JSON.parseObject(membersAllEntity.getFamilyMaster(), FamilyMasterEntity.class);
                        }
                        memberFragmentHeaderUi.setFamilyMaster(familyMasterEntity, familyState);
                        memberFragmentHeaderUi.setMemberCnt(memberCntEntity);
                        memberFragmentHeaderUi.setReviewCnt(membersAllEntity.getFamilyMasterCount());
                    }




                    if (province.equals(getString(R.string.nationwide)) || surname.equals(getString(R.string.surnames_all))) {
                        if (!surname.equals(getString(R.string.surnames_all)) && !StringUtil.isEmpty(membersAllEntity.getSurnameMaster())) {
                            member_header_layout.setVisibility(View.VISIBLE);
                        } else {
                            member_header_layout.setVisibility(View.GONE);
                        }
                    } else {
                        if (isSelProvince && membersAllEntity.getProvinceMaster().length() < 10) {
                            member_header_layout.setVisibility(View.GONE);
                        } else {
                            member_header_layout.setVisibility(View.VISIBLE);
                        }
                    }
                    getmLayoutHeight();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (isSelList) {
                    mAdapter.refresh(listData);
                } else {
                    mAdapterTwo.refresh(listData);
                }
                if (pageIndex==1){
                    secroll_view.scrollTo(0,0);
                }
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
            }
        });
    }
    public void getAreaName(String province,final String city,String area) {
        Map<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("area", area);
        apiImp.getAreaName(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                RegionEntity entity = JSON.parseObject(data, RegionEntity.class);
                if (table_sel==1){
                    areaName = entity.getArea_name() == null ? "" : entity.getArea_name();
                    try {
                        district = areaName.substring(areaName.indexOf(" ") + 1, areaName.length());
                        member_city.setText(district.equals("") ? city : district);
                    } catch (Exception e) {
                    }
                    pageIndex = 1;
                    getRequstData();
                }else {
                    actionAreaName=entity.getArea_name() == null ? "" : entity.getArea_name();
                    district = actionAreaName.substring(actionAreaName.indexOf(" ") + 1, actionAreaName.length());
                    txt_action_city.setText(district.equals("") ? city : district);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case OPENPIC://相册选择照片返回
                    try {
                        List<PhotoInfo> photosInfo = PickerContract.getPhotos(data);
                        if (photosInfo.size() > 0) {
                            intent = new Intent(getContext(), ReleaseActionActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("photosList", (Serializable) photosInfo);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        ViewInject.shortToast(getContext(), "图片出错");
                    }
                    break;
                case OPENCAM://拍完照返回
                    intent = new Intent(getContext(), ReleaseActionActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("photo", outputPath);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 119);
                    break;
                case CROP_PIC:
                    break;
                case 114:
                    intent = new Intent(getContext(), ReleaseActionActivity.class);
                    bundle = new Bundle();
                    bundle.putString("videoUrl", videoFilePath);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 119);
                    break;
                case 113://选择本地视频
                    try {
                        videoFilePath = filePathFromIntent(data);
                        MediaPlayer player = new MediaPlayer();
                        try {
                            player.setDataSource(videoFilePath);  //recordingFilePath（）为音频文件的路径
                            player.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        double duration = player.getDuration();//获取音频的时间
                        if (duration >= (11 * 1000) && !videoFilePath.contains("WeiXin")) {
                            hintVideoDialog();
                        } else {
                            if (getFileSize(new File(videoFilePath)) > (1024 * 1024 * 6)) {
                                hintVideoDialog();
                            } else {
                                intent = new Intent(getContext(), ReleaseActionActivity.class);
                                bundle = new Bundle();
                                bundle.putString("videoUrl", videoFilePath);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 119);
                            }
                        }
                        player.release();//记得释放资源

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 119:
                    pageIndex = 1;
                    requestActionData();
                    break;
            }
        }
    }
    /**
     * 拍摄视频
     */
    protected void chooseVideoFromCamera() {
        if (!StorageUtil.hasEnoughSpaceForWrite(getContext(),
                StorageType.TYPE_VIDEO, true)) {
            return;
        }
        videoFilePath = StorageUtil.getWritePath(
                getContext(), StringUtil.get36UUID()
                        + C.FileSuffix.MP4, StorageType.TYPE_TEMP);

        // 启动视频录制
        CaptureVideoActivity.start(getActivity(), videoFilePath, 114);
    }
    private String videoFilePath;
    public void hintVideoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.hint);
        builder.setMessage(R.string.Video_can_t_be_big);
        builder.setPositiveButton(R.string.confirm, null);
        builder.show();
    }

    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }
    /**
     * 获取文件路径
     *
     * @param data intent数据
     * @return
     */
    private String filePathFromIntent(Intent data) {
        Uri uri = data.getData();
        try {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) {
                //miui 2.3 有可能为null
                return uri.getPath();
            } else {
                cursor.moveToFirst();
                return cursor.getString(cursor.getColumnIndex("_data")); // 文件路径
            }
        } catch (Exception e) {
            return null;
        }
    }
    AlertDialog dialog;
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        viewDailog = View.inflate(getContext(), R.layout.dialog_action_issue_layout, null);
        viewDailog.findViewById(R.id.txt_action_dialog_issue).setOnClickListener(this);
        viewDailog.findViewById(R.id.txt_action_dialog_video).setOnClickListener(this);
        viewDailog.findViewById(R.id.txt_action_dialog_photograph_img).setOnClickListener(this);
        viewDailog.findViewById(R.id.txt_action_dialog_select_img).setOnClickListener(this);
        builder.setView(viewDailog);
        dialog = builder.show();
        dialog.show();
    }

    //选择本地视频或拍摄
    public void selVideo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        viewVideo = View.inflate(getContext(), R.layout.dialog_action_video_sel_layout, null);
        viewVideo.findViewById(R.id.txt_action_dialog_shoot).setOnClickListener(this);
        viewVideo.findViewById(R.id.txt_action_dialog_locality).setOnClickListener(this);
        builder.setView(viewVideo);
        dialog = builder.show();
        dialog.show();
    }

    String outputPath = "";
    private static final int OPENPIC = 11;
    private static final int OPENCAM = 12;
    private static final int CROP_PIC = 13;
    /**
     * 打开图片选择器
     */
    private void showSelector(Boolean isOpen) {
        outputPath = ImgUtils.imgTempFile();
        int from = PickImageActivity.FROM_LOCAL;
        if (isOpen) {
            PickImageActivity.start(getActivity(), OPENPIC, from, outputPath, true,
                    9, true, false, 0, 0);//不截图
        } else {
            from = PickImageActivity.FROM_CAMERA;
            PickImageActivity.start(getActivity(), OPENCAM, from, outputPath, false, 1,
                    true, false, 0, 0);//不截图

        }
    }

    public String listLastId="0";
    public String actionSurname;
    public String actionProvince;
    public String actionCity="";
    public String actionAreaName="";
    public String action_type;
    public int actionPage=1;
    //获取动态数据
    public void requestActionData() {
        final Map<String, Object> params = new HashMap<>();
//        params.put("tab_type", "0");//0:全部, 1:关注, 2:推荐
        params.put("tab_type", action_type.equals(getString(R.string.surnames_all)) ? "0" : action_type.equals(getString(R.string.attention)) ? "1" : "2");//0:全部, 1:关注, 2:推荐
        params.put("surname", actionSurname);
        params.put("province", actionProvince);
        if (!actionProvince.equals(getString(R.string.nationwide))) {
            params.put("area_name", actionAreaName);
        }
        params.put("my_customer_id", user == null ? "" : user.getCustomer_id());
        params.put("pageIndex", actionPage + "");
        params.put("pageItemCount", "20");
        params.put("listLastId", actionPage==1?"0":listLastId);
        apiImp.getActionList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    if (actionPage == 1) {
                        actionListData.clear();
                    }

                    ActionAllEntity entity = JSON.parseObject(data, ActionAllEntity.class);
                    actionUnreadMsg = JSON.parseObject(entity.getUn_read_msg(), ActionUnreadMsgEntity.class);
                    List<ActionEntity> actionEntities = JSON.parseArray(entity.getAction_list(), ActionEntity.class);
                    if (actionEntities.isEmpty()) {
                        actionPage--;
                        actionPage = actionPage < 0 ? 1 : actionPage;
                    }

                    if(!actionListData.containsAll(actionEntities)) {
                        actionListData.addAll(actionEntities);
                        listLastId=actionEntities.get(actionEntities.size()-1).getAction_id();
                    }
                    actionAdapter.refresh(actionListData);
                    loadingReadMsg();
                    if (actionPage > 1) {
                        action_lsit.post(new Runnable() {
                            @Override
                            public void run() {
                                action_lsit.smoothScrollBy(120, 500);
                            }
                        });
                    }

                    if (springView != null) {
                        springView.onFinishFreshAndLoad();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    actionUnreadMsg = null;
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
            }
        });
    }
    /**
     * 加载未读红点数据
     */
    public void loadUnread() {
        if (StringUtil.isEmpty(AppConfig.CustomerId)) return;
        apiImp.getActioUNreadRemind(this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                actionUnreadMsg = JSON.parseObject(data, ActionUnreadMsgEntity.class);
                loadingReadMsg();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public void loadingReadMsg() {
        if (actionUnreadMsg == null || actionUnreadMsg.getCount() == null || actionUnreadMsg.getCount().equals("0")) {
            action_msg_layout.setVisibility(View.GONE);
        } else {
            action_msg_layout.setVisibility(View.VISIBLE);
//            if (listView.getHeaderViewsCount() == 0) {
//                listView.addHeaderView(viewHeader);
//            }
            txt_action_collect_message.setText(getString(R.string.ta_received) + actionUnreadMsg.getCount() + getString(R.string.one_message));
            Glide.with(getContext()).load(actionUnreadMsg.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into(img_collect_header);
        }
    }
}
