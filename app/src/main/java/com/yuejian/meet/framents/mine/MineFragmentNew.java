package com.yuejian.meet.framents.mine;


import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.ArticleDetailsActivity;
import com.yuejian.meet.activities.creation.VideoDetailsActivity;
import com.yuejian.meet.activities.meritsurname.QuestRewardsActivity;
import com.yuejian.meet.activities.mine.EditHomeActivity;
import com.yuejian.meet.activities.mine.InheritorEarningsActivity;
import com.yuejian.meet.activities.mine.LightUpActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.mine.MyWalletActivity;
import com.yuejian.meet.activities.mine.SettingActivity;
import com.yuejian.meet.activities.mine.ShareCodeActivity;
import com.yuejian.meet.adapters.MyCreationListAdapter;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.bean.MineCreationEntity;
import com.yuejian.meet.bean.NotificationMsgBean;
import com.yuejian.meet.bean.mine2Entity;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.framents.ui.SiginWindow;
import com.yuejian.meet.ui.MainMoreUi;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import butterknife.Bind;
import butterknife.OnClick;

public class MineFragmentNew extends BaseFragment implements MainActivity.OnBackPressListener, MyCreationListAdapter.OnCreationListListener {
//    TextView title;

    TextView userName;

    TextView postText;

    TextView minePosition;

    LinearLayout dailyTaskBtn;

    ImageView userHeaderIcon;

    ImageView faqr;

    TextView faqrText;

    ImageView mineViewIcon;

    ImageView mShareQrCodeBtn;

    LinearLayout mWalletBtn;

    LinearLayout mLightUpVipBtn;

    ImageView mMineSettingBtn;

    TabLayout mTabLayout;

    ViewPager mViewPager;

    LinearLayout mEmptyList;

    TextView user_id;

    private NotificationMsgBean msgBean = new NotificationMsgBean();
    private MineFragmentBC mineFragmentBC;
    private MainMoreUi mainMoreUi;

    PropertyValuesHolder transXHolder = PropertyValuesHolder.ofFloat("translationX", 0, 0);
    PropertyValuesHolder transYHolder = PropertyValuesHolder.ofFloat("translationY", 0, 400);
    PropertyValuesHolder scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 1, 0.3f);
    PropertyValuesHolder scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 1, 0.3f);
    Timer timer = null;

    private Mine mine;
    private mine2Entity mine2;
    UserEntity userEntity;
    private Bitmap mineBitmap;
    SiginWindow siginWindow;
    private MyCreationListAdapter mMyCreationListAdapter;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_mine_new, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
//        title = (TextView)parentView.findViewById(R.id.txt_titlebar_title);
        userName = (TextView) parentView.findViewById(R.id.mine_user_name);
        postText = (TextView) parentView.findViewById(R.id.tv_mine_post_text);
        minePosition = (TextView) parentView.findViewById(R.id.tv_mine_position);
        dailyTaskBtn = (LinearLayout) parentView.findViewById(R.id.ll_mine_daily_task);
        userHeaderIcon = (ImageView) parentView.findViewById(R.id.mine_user_header_img);
        faqr = (ImageView) parentView.findViewById(R.id.mine_faqr);
        faqrText = (TextView) parentView.findViewById(R.id.mine_faqr_text);
        mineViewIcon = (ImageView) parentView.findViewById(R.id.iv_mine_vip_icon);
        mShareQrCodeBtn = (ImageView) parentView.findViewById(R.id.iv_share_qr_code);
        mWalletBtn = (LinearLayout) parentView.findViewById(R.id.ll_mine_wallet);
        mLightUpVipBtn = (LinearLayout) parentView.findViewById(R.id.ll_light_up_vip_btn);
        mMineSettingBtn = (ImageView) parentView.findViewById(R.id.iv_mine_setting_btn);
        mTabLayout = (TabLayout) parentView.findViewById(R.id.tl_mine_tab);
        mViewPager = (ViewPager) parentView.findViewById(R.id.mine_viewpager);
        mEmptyList = (LinearLayout) parentView.findViewById(R.id.ll_mine_creation_list_empty);
        user_id = (TextView) parentView.findViewById(R.id.mine_user_id);

        siginWindow = new SiginWindow(getActivity());
        user_id.setText(String.format(getContext().getResources().getString(R.string.text_mine_num) + ": %s", user.customer_id));

        ArrayList<String> mTitleList = new ArrayList<>();
        mTitleList.add("我的创作");
        mTitleList.add("我的商务");
        mTitleList.add("我的动态");

        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(new MineInsideFragment());
        mFragmentList.add(new MineBusinessFragment());
        mFragmentList.add(new MineInsideFragment());

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);


        if (user == null) {
            initView();
        } else {
            findMyInfo(user.customer_id);
            getCouponUnRead();
            getMyCreation(user.customer_id);
        }

    }


    // 获取我的创作
    private void getMyCreation(String customerId) {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("customer_id", customerId);
//        apiImp.getMineFamilyCricleDo(params, this, new DataIdCallback<String>() {
//            @Override
//            public void onSuccess(String data, int id) {
//                List<MineCreationEntity> mineCreationEntities = JSON.parseArray(data, MineCreationEntity.class);
//                if (mineCreationEntities.size() > 0) {
//                    mEmptyList.setVisibility(View.GONE);
//                } else {
//                    mEmptyList.setVisibility(View.VISIBLE);
//                }
//                updateMineCreationList(mineCreationEntities);
//            }
//
//            @Override
//            public void onFailed(String errCode, String errMsg, int id) {
//
//            }
//        });
    }

    private void updateMineCreationList(List<MineCreationEntity> mineCreationEntities) {
        mMyCreationListAdapter.refresh(mineCreationEntities);
    }

//    @Override
//    public void onUserVisible() {
//        super.onUserVisible();
//        Log.d("mine", "user visible");
//        if (AppConfig.userEntity == null) {
//            initView();
//        } else {
//            findMyInfo(user.customer_id);
//            getMyCreation(user.customer_id);
//        }
//    }

    private void findMyInfo(String customerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        if (mine2 != null) {
            //System.out.println("传承人1:"+mine2.inheritorFlag);
        }
        apiImp.getFindMyData(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                mine2 = JSON.parseObject(data, mine2Entity.class);
                //System.out.println("传承人2:"+mine2.inheritorFlag);
                userEntity = JSON.parseObject(mine2.getCustomer(), UserEntity.class);
                updateMineData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private void updateMineData() {
        if (mine2 != null) {
            fillDataToView();
        } else {
            initView();
        }
    }

    private void fillDataToView() {
        userName.setText(userEntity.getName());
        postText.setText(TextUtils.isEmpty(userEntity.job) ? "暂无名片" : userEntity.job);
        minePosition.setText(TextUtils.isEmpty(userEntity.address) ? "暂无地址" : userEntity.address);
        Glide.with(getContext()).load(userEntity.photo).asBitmap().placeholder(R.mipmap.ic_default).into(userHeaderIcon);
        //System.out.println("传承人:"+mine2.inheritorFlag);
        // 0-审核未通过 1-审核通过 2-审核中 3-非传承人
        switch (mine2.inheritorFlag) {
            case 1:
                faqr.setImageResource(R.mipmap.button_ccr_sel);
                faqrText.setText("传承人 >");
                faqrText.setTextColor(Color.parseColor("#AA8564"));
                faqr.setOnClickListener(v -> {
                    Intent gotoInheritorActy = new Intent(getActivity(), InheritorEarningsActivity.class);
                    gotoInheritorActy.putExtra("stats", mine2.inheritorFlag);
                    startActivity(gotoInheritorActy);
                });

                break;
            case 2:
                faqr.setImageResource(R.mipmap.button_ccr_nor);
                faqrText.setText("审核中...");
                faqrText.setTextColor(Color.parseColor("#507DAF"));
                break;
            case 0:
            case 3:
            default:
                faqr.setImageResource(R.mipmap.button_ccr_nor);
                faqrText.setText("传承人 >");
                faqrText.setTextColor(Color.parseColor("#979797"));
                faqr.setOnClickListener(v -> {
                    Intent gotoInheritorActy = new Intent(getActivity(), InheritorEarningsActivity.class);
                    gotoInheritorActy.putExtra("stats", mine2.inheritorFlag);
                    startActivity(gotoInheritorActy);
                });
                break;
        }


        if (mine2.isYear) {
            mineViewIcon.setImageResource(R.mipmap.icon_vip_all);
        } else {
            mineViewIcon.setImageResource(R.mipmap.icon_vip_part);
        }

    }


    private void initView() {
        userHeaderIcon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_default));
        userName.setText("");
        faqr.setVisibility(View.INVISIBLE);
        popupQrCodeWindow = null;
      /*  if(user != null){
            findMyInfo(user.customer_id);
        }*/

        //底部TabLayout和RecyclerView
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @OnClick({R.id.mine_user_header_img, R.id.mine_user_name, R.id.iv_share_qr_code,
            R.id.ll_mine_wallet, R.id.ll_light_up_vip_btn, R.id.iv_mine_setting_btn, R.id.ll_mine_daily_task})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_user_header_img:
                startActivity(new Intent(getActivity(), EditHomeActivity.class));
                break;
            case R.id.ll_mine_daily_task: // 每日任务
                startActivity(new Intent(getContext(), QuestRewardsActivity.class));
                break;
            case R.id.mine_user_name: //点击名字打开个人名片
                if (user == null) {
                    Intent nameIntent = new Intent(getContext(), LoginActivity.class);
                    nameIntent.putExtra("mine_login", true);
                    startActivity(nameIntent);
                } else {
                    AppUitls.goToPersonHome(mContext, user.customer_id);
                }
                break;
            case R.id.iv_share_qr_code:  //递拜帖
                if (user != null) {
                    startActivity(new Intent(getContext(), ShareCodeActivity.class));
                } else {
                    Toast.makeText(mContext, R.string.Please_log_in, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_mine_wallet: //钱包
                if (user == null) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    Intent intent = new Intent(getContext(), MyWalletActivity.class);
                    intent.putExtra("mine", mine);
                    startActivity(intent);
                }
                break;
            case R.id.ll_light_up_vip_btn:  //点亮VIP
                startActivity(new Intent(getContext(), LightUpActivity.class));
                break;
            case R.id.iv_mine_setting_btn:  //标题栏 - 设置
                Intent toSettingIntent = new Intent(getActivity(), SettingActivity.class);
                toSettingIntent.putExtra("mine", mine);
                startActivity(toSettingIntent);
                break;
        }
    }

    private void setVideoPrice(final String price) {
//        if (price.length() > 1 && price.startsWith("0")) {
//            Toast.makeText(mContext, "请输入正确价格", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        HashMap<String, String> params = new HashMap<>();
//        params.put("customer_id", mine.customer_id);
//        params.put("price", price);
//        new ApiImp().setVideoPrice(params, this, new DataIdCallback<String>() {
//            @Override
//            public void onSuccess(String data, int id) {
//                videoPriceEdit.setText(price);
//            }
//
//            @Override
//            public void onFailed(String errCode, String errMsg, int id) {
//                String price = StringUtils.isEmpty(mine.video_price) ? "0" : mine.video_price;
//                videoPriceEdit.setText(price);
//            }
//        });
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        if (event.getCallType() == BusEnum.LOGIN_UPDATE || event.getCallType() == BusEnum.LOGOUT) {
            if (StringUtils.isNotEmpty(AppConfig.CustomerId)) {
                findMyInfo(user.customer_id);
                getMyCreation(user.customer_id);
            } else {
                initView();
            }
        }
    }

    private PopupWindow popupQrCodeWindow;

    private void popupQrCode() {
        if (mine2 == null) return;
        if (popupQrCodeWindow == null) {
            int width = DensityUtils.getScreenW(getContext());
            int height = DensityUtils.getScreenH(getContext());
            popupQrCodeWindow = new PopupWindow(width, height);
            ViewGroup contentView = (ViewGroup) View.inflate(getContext(), R.layout.layout_qr_code_window, null);
            Glide.with(getContext()).load(userEntity.getPhoto()).into((ImageView) contentView.findViewById(R.id.qr_code_customer_photo));
            final TextView name = (TextView) contentView.findViewById(R.id.qr_code_customer_name);
            name.setText(userEntity.getName());
            TextView id = (TextView) contentView.findViewById(R.id.qr_code_customer_id);
            id.setText(getString(R.string.project_name) + user.customer_id);
            Button copy = (Button) contentView.findViewById(R.id.copy_qr_link);
            Button share = (Button) contentView.findViewById(R.id.share_qr_code);
            Button invite = (Button) contentView.findViewById(R.id.invite_originate);
//            if (mine.is_family_master != 0) {
//                invite.setVisibility(View.VISIBLE);
//                invite.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getContext(), InviteOriginateActivity.class);
//                        intent.putExtra("mine", mine);
//                        startActivity(intent);
//                    }
//                });
//            }
            ImageView close = (ImageView) contentView.findViewById(R.id.qr_window_close);
            ImageView qrCodeImg = (ImageView) contentView.findViewById(R.id.qr_code_img);
            copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.copyText(getContext(), UrlConstant.ExplainURL.QRCODE_SHARE + "?customer_id="
                            + (user != null ? user.customer_id : "")
                            + (user != null ? "&inviteCode=" + user.invite_code : ""));
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mineBitmap == null) {
                        mineBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo);
                    }
                    Utils.umengShareByList(getActivity(),
                            mineBitmap,
                            getString(R.string.Friend_request_family),
                            getString(R.string.Family_the_clan_slogan),
                            UrlConstant.ExplainURL.QRCODE_SHARE + "?customer_id="
                                    + (user != null ? user.customer_id : "")
                                    + (user != null ? "&inviteCode=" + user.invite_code : ""));
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupQrCodeWindow.dismiss();
                }
            });
            int size = (int) getResources().getDimension(R.dimen.px_300);
            Bitmap qrCode = Utils.generateBitmap(UrlConstant.ExplainURL.QRCODE_SHARE + "?customer_id=" + user.customer_id + "&inviteCode=" + user.invite_code, size, size);
            qrCodeImg.setImageBitmap(qrCode);
            popupQrCodeWindow.setContentView(contentView);
        }
        popupQrCodeWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public boolean onBackPress() {
        if (popupQrCodeWindow != null && popupQrCodeWindow.isShowing()) {
            popupQrCodeWindow.dismiss();
            popupQrCodeWindow = null;
            return true;
        }
        if (videoPriceSettingWindow != null && videoPriceSettingWindow.isShowing()) {
            videoPriceSettingWindow.dismiss();
            videoPriceSettingWindow = null;
            return true;
        }
        return false;
    }

//    @Override
//    protected void onFirstUserVisible() {
//        super.onFirstUserVisible();
//        Log.d("mine", "first user visible");
//        Log.d("mine", "first" + AppConfig.province + "--" + AppConfig.city);
//        if (user == null) {
//            initView();
//        } else {
//            findMyInfo(user.customer_id);
//            getCouponUnRead();
////            fillDataToView();
//            getMyCreation(user.customer_id);
//        }
//    }

    private PopupWindow videoPriceSettingWindow;

    private void showVideoPriceSettingWindow(final Activity activity) {
//        if (videoPriceSettingWindow == null) {
//            videoPriceSettingWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//            View contentView = View.inflate(activity, R.layout.video_price_window, null);
//            videoPriceSettingWindow.setContentView(contentView);
//            videoPriceSettingWindow.setFocusable(true);
//            videoPriceSettingWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//            contentView.findViewById(R.id.close_dialog).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    videoPriceSettingWindow.dismiss();
//                }
//            });
//            final EditText videoEdit = (EditText) contentView.findViewById(R.id.video_price_edit);
//            videoEdit.setText(videoPriceEdit.getText().toString());
//            videoEdit.setSelection(videoEdit.getText().length());
//            contentView.findViewById(R.id.queding).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String price = videoEdit.getText().toString();
//                    if (StringUtils.isNumber(price)) {
//                        setVideoPrice(price);
//                    }
//                    videoPriceSettingWindow.dismiss();
//                }
//            });
//            Utils.backgroundAlpha(activity, 0.7f);
//            videoPriceSettingWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//            videoPriceSettingWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    Utils.backgroundAlpha(activity, 1f);
//                    videoPriceSettingWindow = null;
//                }
//            });
//        }
    }

    private void getCouponUnRead() {
//        if (StringUtils.isEmpty(AppConfig.CustomerId)) {
//            return;
//        }
//        HashMap<String, String> params = new HashMap<>();
//        params.put("customer_id", AppConfig.CustomerId);
//        apiImp.getCouponUnReadCnt(params, this, new DataIdCallback<String>() {
//            @Override
//            public void onSuccess(String data, int id) {
//                int cnt = Integer.valueOf(data);
//                if (cnt > 0) {
//                    couponUnReadCnt.setVisibility(View.VISIBLE);
//                    couponUnReadCnt.setText(data);
//                } else {
//                    couponUnReadCnt.setText("");
//                    couponUnReadCnt.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailed(String errCode, String errMsg, int id) {
//
//            }
//        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {

            findMyInfo(user.getCustomer_id());
            getMyCreation(user.getCustomer_id());
        }
    }

    @Override
    public void onListItemClick(int type, int id) {
        //类型：1-随笔，2-文章，3-相册，4-视频
        Intent intent = null;
        if (type == 4) {
            intent = new Intent(getActivity(), VideoDetailsActivity.class);
        } else {
            intent = new Intent(getActivity(), ArticleDetailsActivity.class);
        }
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
