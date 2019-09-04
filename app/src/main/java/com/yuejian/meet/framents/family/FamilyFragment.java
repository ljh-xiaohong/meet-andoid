package com.yuejian.meet.framents.family;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.taobao.library.BaseBannerAdapter;
import com.taobao.library.VerticalBannerView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yuejian.meet.R;
//import com.yuejian.meet.activities.clan.MainClanActivity;
import com.yuejian.meet.activities.clan.MainClanActivity;
import com.yuejian.meet.activities.family.AVListAdapter;
import com.yuejian.meet.activities.family.AddMemberActivity;
import com.yuejian.meet.activities.family.EveryDaySignActivity;
//import com.yuejian.meet.activities.family.FamilyMemberActivity;
import com.yuejian.meet.activities.family.FamilyMemberActivity;
import com.yuejian.meet.activities.family.HarbourActivity;
import com.yuejian.meet.activities.family.HistoryBulletinActivity;
import com.yuejian.meet.activities.family.PutSomethingActivity;
import com.yuejian.meet.activities.home.StoreWebActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.web.VideoViewActivity;
import com.yuejian.meet.activities.web.WebActivity;
//import com.yuejian.meet.activities.zuci.ZuciMainActivity;
//import com.yuejian.meet.activities.zuci.ZuciNearbyActivity;
import com.yuejian.meet.activities.zuci.ZuciMainActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.AVData;
import com.yuejian.meet.bean.BannerData;
import com.yuejian.meet.bean.HistoryToday;
import com.yuejian.meet.bean.NearPerson;
import com.yuejian.meet.bean.Relative;
import com.yuejian.meet.bean.SignDay;
import com.yuejian.meet.bean.ClanHall;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.MediaPlayerController;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.BigMoneyTree;
import com.yuejian.meet.widgets.BigTanTanWindow;
import com.yuejian.meet.widgets.BindRelativeDialog;
import com.yuejian.meet.widgets.ShakeGiftPopWindow;
import com.zhy.http.okhttp.callback.StringCallback;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

import org.json.JSONObject;

public class FamilyFragment extends BaseFragment {
    private static int GET_CONTACT = 1100;
    private static final int GET_PROGRESS = 1;
    private static int WINDOW_HEIGHT;
    private List<AVData> avDataList;
    private AVListAdapter avListAdapter = null;
    private Banner banner;
    private BigMoneyTree bigMoneyTree = null;
    private BigTanTanWindow bigNearPeopleWindow = null;
    private ViewPager clanHallViewpager = null;
    private List<ClanHall> clanHalls = new ArrayList();
    private int currentPlayingIndex = 0;
    private TextView currentTime = null;
    private ArrayList<SignDay> days = null;
    private ArrayList<Fragment> fragments;
    private int guideTap = 0;
    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message paramAnonymousMessage) {
            if ((paramAnonymousMessage.what == 1) && (seekBar != null) && (mediaPlayer != null) && (mediaPlayer.isPlaying())) {
                int i = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(i);
                i = Math.round(i / 1000);
                String time = String.format(Locale.getDefault(), "%02d:%02d", i / 60, i % 60);
                currentTime.setText(time);
                handler.sendEmptyMessageDelayed(1, 1000L);
            }
            return false;
        }
    });
    private List<HistoryToday> historyTodays = new ArrayList();
    private MediaPlayer mediaPlayer = null;
    private MediaPlayerController mediaPlayerController = null;
    private List<Relative> myRelativesList = new ArrayList();
    private ImageView play;
    private TextView playTitle = null;
    private TextView playerAuthor = null;
    private ImageView playerBg = null;
    private View playerView = null;
    private PopupWindow popupWindow = null;
    private PopupWindow ptBottomSheet = null;
    private BindRelativeDialog relativeDialog = null;
    private int relativePosition = 0;
    private RelativesAdapter relativesAdapter = null;
    private SwipeRefreshLayout rootView;
    private SeekBar seekBar = null;
    private ShakeGiftPopWindow shakeGiftPopWindow = null;
    private PopupWindow signWindow = null;
    private TextView totalTime = null;
    private VerticalBannerView vBanner;
    private TextView like_clan_hall_cnt,collect_clan_hall_cnt;

    private void bindRelative(String paramString1, String paramString2) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("my_customer_id", AppConfig.CustomerId);
        params.put("relative_id", paramString2);
        params.put("op_customer_id", paramString1);
        this.apiImp.bindRelative(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                if (relativeDialog != null) {
                    relativeDialog.dismiss();
                }
                myRelatives();
            }
        });
    }

    private void clanHallTopReword() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("my_customer_id", AppConfig.CustomerId);
        this.apiImp.clanHallTopRecord(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                clanHalls = JSON.parseArray(paramAnonymousString, ClanHall.class);
                initClanHallView();
            }
        });
    }

    private void clanLike(String paramString) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("id", paramString);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.zuciLike(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ((CheckBox) rootView.findViewById(R.id.like_clan_hall)).setChecked("1".equals(paramAnonymousString));
                clanHalls.get(clanHallViewpager.getCurrentItem()).is_thumb = paramAnonymousString;
                like_clan_hall_cnt.setText("1".equals(paramAnonymousString)?(Integer.parseInt(like_clan_hall_cnt.getText().toString())+1)+"":(Integer.parseInt(like_clan_hall_cnt.getText().toString())-1)+"");
            }
        });
    }

    private void collectClanHall(String paramString) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("type", "1");
        localHashMap.put("relation_id", paramString);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.collection(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                ((CheckBox) rootView.findViewById(R.id.collect_clan_hall)).setChecked("1".equals(paramAnonymousString));
                clanHalls.get(clanHallViewpager.getCurrentItem()).is_collection = paramAnonymousString;
                collect_clan_hall_cnt.setText("1".equals(paramAnonymousString)?(Integer.parseInt(collect_clan_hall_cnt.getText().toString())+1)+"":(Integer.parseInt(collect_clan_hall_cnt.getText().toString())-1)+"");
            }
        });
    }

    private void findAv() {
        this.apiImp.findAv(new HashMap<String, Object>(), this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                avDataList = JSON.parseArray(paramAnonymousString, AVData.class);
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(avDataList.get(0).av_url);
                        mediaPlayer.prepare();
                        updatePlayerInfo(0);
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                }
            }
        });
    }

    private String[] getPhoneContacts(Uri paramUri) {
        String[] arrayOfString = new String[2];
        Object localObject = getActivity().getContentResolver();
        Cursor query = ((ContentResolver) localObject).query(paramUri, null, null, null, null);
        if (query != null) {
            query.moveToFirst();
            arrayOfString[0] = query.getString(query.getColumnIndex("display_name"));
            String str = query.getString(query.getColumnIndex("_id"));
            localObject = ((ContentResolver) localObject).query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id=" + str, null, null);
            if (localObject != null) {
                ((Cursor) localObject).moveToFirst();
                arrayOfString[1] = ((Cursor) localObject).getString(((Cursor) localObject).getColumnIndex("data1"));
            }
            ((Cursor) localObject).close();
            query.close();
            return arrayOfString;
        }
        return null;
    }

    private void initBanner() {
        this.banner = ((Banner) this.rootView.findViewById(R.id.banner));
        this.banner.setImageLoader(new GlideImageLoader());
        this.banner.isAutoPlay(true);
        this.banner.setBannerStyle(1);
        this.banner.setIndicatorGravity(6);
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
                            Intent localIntent = new Intent(getActivity(), WebActivity.class);
                            localIntent.putExtra("No_Title", true);
                            localIntent.putExtra("url", str);
                            startActivity(localIntent);
                        }
                    });
                }
            }
        });
    }

    private void initClanHallView() {
        if ((this.clanHalls == null) || (this.clanHalls.isEmpty())) {
            this.rootView.findViewById(R.id.clan_halls_layout).setVisibility(View.GONE);
        } else {
            final ArrayList<String> images = new ArrayList<>();
            for (ClanHall hall : clanHalls) {
                images.add(hall.first_figure);
            }
            this.rootView.findViewById(R.id.clan_halls_layout).setVisibility(View.VISIBLE);
            if (this.clanHallViewpager == null) {
                this.clanHallViewpager = ((ViewPager) this.rootView.findViewById(R.id.vp_clan_hall));
            }
            this.clanHallViewpager.setAdapter(null);
            this.clanHallViewpager.removeAllViews();
            this.clanHallViewpager.setAdapter(new PagerAdapter() {
                public void destroyItem(ViewGroup paramAnonymousViewGroup, int paramAnonymousInt, Object paramAnonymousObject) {
                    paramAnonymousObject = paramAnonymousViewGroup.getChildAt(paramAnonymousInt);
                    if (paramAnonymousObject != null) {
                        paramAnonymousViewGroup.removeView((View) paramAnonymousObject);
                    }
                }

                public int getCount() {
                    return images.size();
                }

                public Object instantiateItem(ViewGroup paramAnonymousViewGroup, int position) {
                    ImageView localImageView = new ImageView(getContext());
                    localImageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                    localImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    paramAnonymousViewGroup.addView(localImageView, 0);
                    Glide.with(getActivity()).load(images.get(position)).into(localImageView);
                    return localImageView;
                }

                public boolean isViewFromObject(View paramAnonymousView, Object paramAnonymousObject) {
                    return paramAnonymousView == paramAnonymousObject;
                }
            });
        }
        final TextView hallName = (TextView) this.rootView.findViewById(R.id.hall_name);
        final TextView hallAddress = (TextView) this.rootView.findViewById(R.id.hall_address);
        final CheckBox likeClanHallCheck = (CheckBox) this.rootView.findViewById(R.id.like_clan_hall);
        final CheckBox collectionClanHallCheck = (CheckBox) this.rootView.findViewById(R.id.collect_clan_hall);
        like_clan_hall_cnt= (TextView) rootView.findViewById(R.id.like_clan_hall_cnt);
        collect_clan_hall_cnt= (TextView) rootView.findViewById(R.id.collect_clan_hall_cnt);

        ClanHall localClanHall = this.clanHalls.get(0);
        hallName.setText(localClanHall.getName());
        hallAddress.setText(String.valueOf(localClanHall.province + localClanHall.city + localClanHall.detailed_place));
        likeClanHallCheck.setChecked("1".equals(localClanHall.is_thumb));
        collectionClanHallCheck.setChecked("1".equals(localClanHall.is_collection));
        like_clan_hall_cnt.setText(localClanHall.getThumb_cnt()+"");
        collect_clan_hall_cnt.setText(localClanHall.getCollection_cnt()+"");
        this.clanHallViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int paramAnonymousInt) {
                super.onPageSelected(paramAnonymousInt);
                ClanHall localClanHall = clanHalls.get(paramAnonymousInt);
                hallName.setText(localClanHall.getName());
                hallAddress.setText(String.valueOf(localClanHall.province + localClanHall.city + localClanHall.detailed_place));
                likeClanHallCheck.setChecked("1".equals(localClanHall.is_thumb));
                collectionClanHallCheck.setChecked("1".equals(localClanHall.is_collection));
                like_clan_hall_cnt.setText(localClanHall.getThumb_cnt()+"");
                collect_clan_hall_cnt.setText(localClanHall.getCollection_cnt()+"");
            }
        });
    }

    private void initGiftCenter() {
        this.rootView.findViewById(R.id.yaoyiyao).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (shakeGiftPopWindow == null) {
                    shakeGiftPopWindow = new ShakeGiftPopWindow(getActivity());
                }
                shakeGiftPopWindow.show();
                rootView.findViewById(R.id.yaoyiyao).setEnabled(false);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        rootView.findViewById(R.id.yaoyiyao).setEnabled(true);
                    }
                }, 3000L);
            }
        });
        this.rootView.findViewById(R.id.fangrujingxi).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Bus.getDefault().post("put_something");
            }
        });
        this.rootView.findViewById(R.id.tree).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (bigMoneyTree == null) {
                    bigMoneyTree = new BigMoneyTree(getActivity());
                }
                bigMoneyTree.show();
            }
        });
    }

    private void initGuide() {
        this.guideTap = 0;
        final PopupWindow localPopupWindow = new PopupWindow(getContext());
        FrameLayout localFrameLayout = new FrameLayout(getContext());
        final ImageView localImageView = new ImageView(getContext());
        FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(ScreenUtils.getScreenWidth(getActivity()), ScreenUtils.getScreenHeight(getActivity()) - Utils.dp2px(getActivity(), 4.0F));
        localImageView.setImageResource(R.mipmap.zczq);
        localImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        localImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (guideTap == 0) {
                    guideTap = 1;
                    localImageView.setImageResource(R.mipmap.jiaren);
                    return;
                }
                localPopupWindow.dismiss();
            }
        });
        localFrameLayout.addView(localImageView, localLayoutParams);
        localPopupWindow.setBackgroundDrawable(new ColorDrawable());
        localPopupWindow.setContentView(localFrameLayout);
        localPopupWindow.showAtLocation(this.rootView, 51, 0, 0);
    }

    private void initHistoryToday(Calendar paramCalendar) {
        this.vBanner = ((VerticalBannerView) this.rootView.findViewById(R.id.ver_banner));
        this.apiImp.getHistoryToday(paramCalendar.get(Calendar.MONTH) + 1, paramCalendar.get(Calendar.DATE), new StringCallback() {
            public void onError(Call paramAnonymousCall, Exception paramAnonymousException, int paramAnonymousInt) {
            }

            public void onResponse(String paramAnonymousString, int paramAnonymousInt) {
                try {
                    paramAnonymousString = new JSONObject(paramAnonymousString).getString("result");
                    historyTodays = JSON.parseArray(paramAnonymousString, HistoryToday.class);
                    if ((historyTodays == null) || (historyTodays.isEmpty())) {
                        vBanner.setVisibility(View.GONE);
                        return;
                    }
                    vBanner.setAdapter(new BaseBannerAdapter<HistoryToday>(historyTodays) {
                        public View getView(VerticalBannerView paramAnonymous2VerticalBannerView) {
                            return View.inflate(getActivity(), R.layout.view_history_today, null);
                        }

                        public void setItem(View paramAnonymous2View, HistoryToday paramAnonymous2HistoryToday) {
                            ((TextView) paramAnonymous2View.findViewById(R.id.history_date)).setText(new StringBuffer(paramAnonymous2HistoryToday.year + "." + paramAnonymous2HistoryToday.month + "." + paramAnonymous2HistoryToday.day));
                            ((TextView) paramAnonymous2View.findViewById(R.id.history_title)).setText(paramAnonymous2HistoryToday.title);
                            paramAnonymous2View.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View paramAnonymous3View) {
                                    Intent intent = new Intent(getActivity(), HistoryBulletinActivity.class);
                                    intent.putExtra("dataSource", JSON.toJSONString(historyTodays));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                    vBanner.start();
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        });
    }

    private void initLunarCalendarView(ArrayList<SignDay> paramArrayList) {
        Calendar localCalendar = Calendar.getInstance();
        this.fragments = new ArrayList();
        ViewPager localViewPager = (ViewPager) this.rootView.findViewById(R.id.vp_canlander);
        int k = localCalendar.get(Calendar.DATE);
        int m = localCalendar.getMaximum(Calendar.DATE);
        int n = localCalendar.get(Calendar.MONTH);
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        for (int i = 0; i < 7; i++) {
            localCalendar = Calendar.getInstance();
            CalenderCardFragment calenderCardFragment = new CalenderCardFragment();
            Bundle bundle = new Bundle();
            if (k + i > m) {
                localCalendar.set(localCalendar.get(Calendar.YEAR), n + 1, k + i - m);
            } else {
                localCalendar.set(Calendar.DATE, k + i);
            }
            bundle.putLong("date", localCalendar.getTimeInMillis());
            if (paramArrayList != null) {
                Date date = new Date(localCalendar.getTimeInMillis());
                for (SignDay signDay : paramArrayList) {
                    if (signDay.signin_date.equals(localSimpleDateFormat.format(date))) {
                        bundle.putBoolean("isSigned", true);
                    }
                }
            }
            calenderCardFragment.setArguments(bundle);
            fragments.add(calenderCardFragment);
        }

        localViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            public int getCount() {
                return fragments.size();
            }

            public Fragment getItem(int paramAnonymousInt) {
                return fragments.get(paramAnonymousInt);
            }

            public Object instantiateItem(ViewGroup paramAnonymousViewGroup, int paramAnonymousInt) {
                return super.instantiateItem(paramAnonymousViewGroup, paramAnonymousInt);
            }
        });
        localViewPager.setCurrentItem(0);
        localViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageScrollStateChanged(int paramAnonymousInt) {
                super.onPageScrollStateChanged(paramAnonymousInt);
                if (1 == paramAnonymousInt) {
                    rootView.setEnabled(false);
                    return;
                }
                rootView.setEnabled(true);
            }
        });
    }

    private void initLunerDate() {
        if (StringUtils.isEmpty(AppConfig.CustomerId)) {
            initLunarCalendarView(null);
            return;
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        localHashMap.put("date", new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date()));
        this.apiImp.findCustomerSignIn(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                initLunarCalendarView(null);
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                List<SignDay> signDays = JSON.parseArray(paramAnonymousString, SignDay.class);
                days = new ArrayList<>();
                days.addAll(signDays);
                initLunarCalendarView(days);
            }
        });
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
        this.playerView.findViewById(R.id.pre).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                pre();
            }
        });
        this.playerView.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                next();
            }
        });
        this.play = ((ImageView) this.playerView.findViewById(R.id.play));
        this.play.setImageResource(R.mipmap.bofang);
        this.play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                play();
            }
        });
    }

    private void initMyFamilyRelatives() {
        this.myRelativesList.clear();
        if (StringUtils.isNotEmpty(AppConfig.CustomerId)) {
            Relative relative = new Relative();
            relative.id = "-2";
            this.myRelativesList.add(0, relative);
        }
        Relative relative = new Relative();
        relative.id = "-1";
        this.myRelativesList.add(relative);
        RecyclerView recyclerView = (RecyclerView) this.rootView.findViewById(R.id.family_list);
        this.relativesAdapter = new RelativesAdapter(getActivity(), this.myRelativesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        recyclerView.setAdapter(this.relativesAdapter);
        myRelatives();
    }

    private void initPlayer() {
        try {
            this.playerView = this.rootView.findViewById(R.id.player);
            initMediaController();
            this.playerBg = ((ImageView) this.playerView.findViewById(R.id.player_bg));
            this.playTitle = ((TextView) this.playerView.findViewById(R.id.play_title));
            this.seekBar = ((SeekBar) this.playerView.findViewById(R.id.seek_bar_progress));
            this.currentTime = ((TextView) this.playerView.findViewById(R.id.currentTime));
            this.totalTime = ((TextView) this.playerView.findViewById(R.id.total_time));
            this.playerAuthor = ((TextView) this.playerView.findViewById(R.id.player_author));
            this.playerView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    if ((avDataList == null) || (avDataList.isEmpty())) {
                        return;
                    }
                    Intent intent = new Intent(getActivity(), HarbourActivity.class);
                    intent.putExtra("avJson", JSON.toJSONString(avDataList));
                    intent.putExtra("index", currentPlayingIndex);
                    startActivity(intent);
                }
            });
            this.playerView.findViewById(R.id.bofangku).setOnClickListener(new View.OnClickListener() {
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
                    if (mediaPlayer != null) {
                        mediaPlayer.seekTo(i);
                    }
                }
            });
            this.playerView.findViewById(R.id.fenxiang).setOnClickListener(new View.OnClickListener() {
                public void onClick(final View paramAnonymousView) {
                    final AVData avData = avDataList.get(currentPlayingIndex);
                    Glide.with(getActivity()).load(avData.av_img).asBitmap().listener(new RequestListener<String, Bitmap>() {
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
                            String shareUrl = UrlConstant.apiUrl() + "gw_share/index.html?av_id=" + avData.av_id + "&inviteCode=" + (AppConfig.userEntity != null ? AppConfig.userEntity.invite_code : "");
                            Utils.umengShareByList(getActivity(), bitmap, title, dec, shareUrl);
                            return false;
                        }
                    }).preload();
                }
            });
            setupBlurView();
            findAv();
            this.mediaPlayerController = new MediaPlayerController(getActivity(), this.mediaPlayer);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    private void initRandomSearch() {
        NearPersonCardSearch nearPersonCardSearch = new NearPersonCardSearch();
        FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
        localFragmentTransaction.add(R.id.random_search_content, nearPersonCardSearch);
        localFragmentTransaction.commit();
    }

    private void myRelatives() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.myRelatives(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                List<Relative> relatives = JSON.parseArray(paramAnonymousString, Relative.class);
                myRelativesList.clear();
                if (StringUtils.isNotEmpty(AppConfig.CustomerId)) {
                    Relative localRelative = new Relative();
                    localRelative.id = "-2";
                    myRelativesList.add(0, localRelative);
                }
                myRelativesList.addAll(relatives);
                Relative relative = new Relative();
                relative.id = "-1";
                myRelativesList.add(relative);
                relativesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void next() {
        if ((this.avDataList != null) && (this.currentPlayingIndex < this.avDataList.size() - 1)) {
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
            updatePlayerInfo(this.currentPlayingIndex);
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
            Intent localIntent = new Intent(getActivity(), VideoViewActivity.class);
            localIntent.putExtra("video_url", this.avDataList.get(this.currentPlayingIndex).av_url);
            startActivity(localIntent);
            return;
        }
        this.play.setImageResource(R.mipmap.zanting);
        mediaPlayer.start();
        this.seekBar.setMax(this.mediaPlayer.getDuration());
        this.handler.sendEmptyMessageDelayed(1, 1000L);
    }

    private void popupSignWindow() {
        if (this.signWindow == null) {
            this.signWindow = new PopupWindow(getActivity());
            this.signWindow.setWidth(ScreenUtils.getScreenWidth(getActivity()));
            this.signWindow.setHeight(WINDOW_HEIGHT);
            this.signWindow.setBackgroundDrawable(new ColorDrawable());
            View localView = View.inflate(getActivity(), R.layout.layout_sign_window, null);
            localView.findViewById(R.id.go_to_detail).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Bus.getDefault().post("sign_every_day");
                    signWindow.dismiss();
                }
            });
            this.signWindow.setContentView(localView);
        }
        this.signWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                ScreenUtils.setScreenAlpha(getActivity(), 1.0F);
            }
        });
        ScreenUtils.setScreenAlpha(getActivity(), 0.4F);
        this.signWindow.showAtLocation(getActivity().getWindow().getDecorView(), 80, 0, 0);
    }

    private void pre() {
        if ((this.avDataList != null) && (this.currentPlayingIndex > 0)) {
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
            updatePlayerInfo(this.currentPlayingIndex);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    private void setupBlurView() {
        ((BlurView) this.playerView.findViewById(R.id.blurView)).setupWith((ViewGroup) this.playerView).windowBackground(this.play.getBackground()).blurAlgorithm(new RenderScriptBlur(getActivity())).blurRadius(2.0F);
    }

    private void showBottomList() {
        if (this.popupWindow == null) {
            this.popupWindow = new PopupWindow(getActivity());
            View localView = View.inflate(getActivity(), R.layout.bottom_sheet_list, null);
            localView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    popupWindow.dismiss();
                }
            });
            ListView localListView = (ListView) localView.findViewById(R.id.bottom_sheet_list);
            this.avListAdapter = new AVListAdapter(getActivity(), this.avDataList);
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
                        updatePlayerInfo(currentPlayingIndex);
                        popupWindow.dismiss();
                    } catch (IOException error) {
                        error.printStackTrace();
                    }
                }
            });
            this.popupWindow.setWidth(ScreenUtils.getScreenWidth(getActivity()));
            this.popupWindow.setHeight(ScreenUtils.getScreenHeight(getActivity()) - ScreenUtil.getStatusBarHeight(getActivity()));
            this.popupWindow.setAnimationStyle(R.style.popmenu_animation);
            this.popupWindow.setContentView(localView);
            this.popupWindow.setBackgroundDrawable(new ColorDrawable());
            this.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    ScreenUtils.setScreenAlpha(getActivity(), 1.0F);
                }
            });
        }
        ScreenUtils.setScreenAlpha(getActivity(), 0.4F);
        this.avListAdapter.setCurrentPlayingIndex(this.currentPlayingIndex);
        this.popupWindow.showAtLocation(this.rootView, 80, 0, 0);
    }

    private void showPutThingBottomSheet() {
        if (this.ptBottomSheet == null) {
            this.ptBottomSheet = new PopupWindow();
            this.ptBottomSheet.setWidth(ScreenUtils.getScreenWidth(getActivity()));
            this.ptBottomSheet.setHeight(WINDOW_HEIGHT);
            View localView = View.inflate(getActivity(), R.layout.xlh_bottom_sheet, null);
            this.ptBottomSheet.setContentView(localView);
            this.ptBottomSheet.setBackgroundDrawable(new ColorDrawable());
            this.ptBottomSheet.setOutsideTouchable(false);
            localView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    ptBottomSheet.dismiss();
                }
            });
            localView.findViewById(R.id.xinlihua).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Intent intent = new Intent(getActivity(), PutSomethingActivity.class);
                    intent.putExtra("title", "心里话");
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
            });
            localView.findViewById(R.id.jinbi).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Intent intent = new Intent(getActivity(), PutSomethingActivity.class);
                    intent.putExtra("title", "金币");
                    intent.putExtra("type", 2);
                    startActivity(intent);
                }
            });
            localView.findViewById(R.id.liwu).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Intent intent = new Intent(getActivity(), PutSomethingActivity.class);
                    intent.putExtra("title", "礼物");
                    intent.putExtra("type", 3);
                    startActivity(intent);
                }
            });
        }
        this.ptBottomSheet.showAtLocation(getActivity().getWindow().getDecorView(), 81, 0, 0);
        this.ptBottomSheet.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                ScreenUtils.setScreenAlpha(getActivity(), 1.0F);
            }
        });
    }

    private void updatePlayerInfo(int paramInt) {
        AVData localAVData = this.avDataList.get(paramInt);
        this.playTitle.setText(localAVData.av_title);
        Glide.with(getActivity()).load(localAVData.av_img).asBitmap().into(this.playerBg);
        this.seekBar.setProgress(0);
        this.playerAuthor.setText(new StringBuffer("作者: " + localAVData.av_author));
        this.totalTime.setText(localAVData.av_time);
    }

    private void zoomUpNearPeopleSearch(List<NearPerson> paramList) {
        if (this.bigNearPeopleWindow == null) {
            this.bigNearPeopleWindow = new BigTanTanWindow(getActivity());
        }
        this.bigNearPeopleWindow.setData(paramList);
        this.bigNearPeopleWindow.show();
    }

    protected View inflaterView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        this.rootView = ((SwipeRefreshLayout) paramLayoutInflater.inflate(R.layout.fragment_family, null));
        this.rootView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                initData();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        rootView.setRefreshing(false);
                    }
                }, 1000L);
            }
        });
        this.rootView.setEnabled(true);
        return this.rootView;
    }

    protected void initData() {
        super.initData();
        WINDOW_HEIGHT = ScreenUtils.getScreenHeight(getActivity()) - Utils.dp2px(getActivity(), 44);
        initBanner();
        final Object localObject = Calendar.getInstance();
        initLunerDate();
        initHistoryToday((Calendar) localObject);
        initPlayer();
        initRandomSearch();
        initGiftCenter();
        initMyFamilyRelatives();
        clanHallTopReword();
        final ScrollView scrollView = (ScrollView) this.rootView.findViewById(R.id.scroll_view);
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });
    }

    public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if ((paramInt1 == GET_CONTACT) && (paramIntent != null)) {
            String[] phoneContacts = getPhoneContacts(paramIntent.getData());
            if ((phoneContacts != null) && (phoneContacts.length >= 2)) {
                String phoneContact = phoneContacts[1];
                paramIntent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + phoneContact));
                paramIntent.putExtra("sms_body", "嗨！朋友，我在玩的这个APP挺不错的，邀你进来一起咱有事没事聊聊天" + UrlConstant.ExplainURL.QRCODE_SHARE2 + "?inviteCode=" + AppConfig.userEntity.invite_code);
                startActivity(paramIntent);
            }
        }
    }

    public boolean onBackPressed() {
        if ((this.shakeGiftPopWindow != null) && (this.shakeGiftPopWindow.isShowing())) {
            this.shakeGiftPopWindow.dismiss();
        } else if ((this.popupWindow != null) && (this.popupWindow.isShowing())) {
            this.popupWindow.dismiss();
        } else if ((this.ptBottomSheet != null) && (this.ptBottomSheet.isShowing())) {
            this.ptBottomSheet.dismiss();
        } else if ((this.relativeDialog != null) && (this.relativeDialog.isShowing())) {
            this.relativeDialog.dismiss();
        } else if ((this.signWindow != null) && (this.signWindow.isShowing())) {
            this.signWindow.dismiss();
        } else if ((this.bigNearPeopleWindow != null) && (this.bigNearPeopleWindow.isShowing())) {
            this.bigNearPeopleWindow.dismiss();
        } else if (this.bigMoneyTree != null) {
            this.bigMoneyTree.dismiss();
        } else {
            return super.onBackPressed();
        }
        return true;
    }

    public void onBusCallback(BusCallEntity paramBusCallEntity) {
        if ((paramBusCallEntity.getCallType() == BusEnum.LOGIN_UPDATE) || (paramBusCallEntity.getCallType() == BusEnum.LOGOUT)) {
            initData();
        }
    }

    @OnClick({R.id.like_clan_hall,
            R.id.collect_clan_hall,
            R.id.ic_shangyu,
            R.id.zuci_layout,
            R.id.family_member,
            R.id.left,
            R.id.right,
            R.id.layout_fragment_zongqinhui,
            R.id.charge_clan_hall})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ic_shangyu:
                startActivity(new Intent(getActivity(), StoreWebActivity.class));
                break;
            case R.id.layout_fragment_zongqinhui:
                if (StringUtil.isEmpty(AppConfig.slongitude)) {
                    ViewInject.toast(getContext(), "请打开定位权限");
                    startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS",
                            Uri.parse("package:com.yuejian.meet")));
                } else {
                    startActivity(new Intent(getContext(), MainClanActivity.class));
                }
                break;
            case R.id.family_member:
                startActivity(new Intent(getContext(), FamilyMemberActivity.class));
                break;
            case R.id.zuci_layout:
                startActivity(new Intent(getContext(), ZuciMainActivity.class));
                break;
            case R.id.left:
                int index = clanHallViewpager.getCurrentItem();
                if (index > 0) {
                    index--;
                    clanHallViewpager.setCurrentItem(index);
                }
                break;
            case R.id.right:
                index = clanHallViewpager.getCurrentItem();
                if (index < clanHalls.size()) {
                    index++;
                    clanHallViewpager.setCurrentItem(index);
                }
                break;
            case R.id.charge_clan_hall:
//                startActivity(new Intent(getActivity(), ZuciNearbyActivity.class));
                break;
            case R.id.like_clan_hall:
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    ((CheckBox) view).setChecked(false);
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    clanLike(this.clanHalls.get(this.clanHallViewpager.getCurrentItem()).id);
                }
                break;
            case R.id.collect_clan_hall:
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    ((CheckBox) view).setChecked(false);
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {
                    collectClanHall(this.clanHalls.get(this.clanHallViewpager.getCurrentItem()).id);
                }
                break;
        }
    }

    public void onStop() {
        super.onStop();
        if (this.mediaPlayer != null && mediaPlayerController != null) {
            this.mediaPlayerController.stopMediaPlayer();
        }
    }

    public void onUserInvisible() {
        super.onUserInvisible();
        if (this.mediaPlayer != null) {
//            this.mediaPlayerController.stopMediaPlayer();
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    public void onUserVisible() {
        super.onUserVisible();
        try {
            initMediaController();
            this.mediaPlayer.reset();
            this.mediaPlayer.setDataSource(this.avDataList.get(this.currentPlayingIndex).av_url);
            this.mediaPlayer.prepare();
            updatePlayerInfo(this.currentPlayingIndex);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public void onWindowFocusChanged(boolean hasChanged) {
        super.onWindowFocusChanged(hasChanged);
        if ((hasChanged) && (!PreferencesUtil.readBoolean(getActivity(), "first_in_guide", false))) {
            this.handler.postDelayed(new Runnable() {
                public void run() {
                    initGuide();
                    PreferencesUtil.write(getActivity(), "first_in_guide", true);
                }
            }, 500L);
        }
    }

    public void receiverBus(String event) {
        super.receiverBus(event);
        if ("put_something".equals(event)) {
            if ((this.shakeGiftPopWindow != null) && (this.shakeGiftPopWindow.isShowing())) {
                this.shakeGiftPopWindow.dismiss();
            }
            showPutThingBottomSheet();
        } else if ("sign_every_day".equals(event)) {
            if (StringUtils.isEmpty(AppConfig.CustomerId)) {
                startActivity(new Intent(getActivity(), LoginActivity.class));

            }
            Intent intent = new Intent(getActivity(), EveryDaySignActivity.class);
            ArrayList signDates = new ArrayList();
            if (this.days != null) {
                for (SignDay day : days) {
                    signDates.add(day.signin_date);
                }
                intent.putExtra("signDates", signDates);
            }
            startActivity(intent);
        } else if ("add_family_member".equals(event)) {
            startActivity(new Intent(getActivity(), AddMemberActivity.class));
        } else if ("add_relative_finish".equals(event)) {
            myRelatives();
        } else if (event.contains("bind_relative_")) {
            this.relativePosition = Integer.valueOf(event.substring(event.lastIndexOf("_") + 1, event.length()));
            if (this.relativeDialog == null) {
                this.relativeDialog = new BindRelativeDialog(getActivity());
            }
            this.relativeDialog.setRelative(this.myRelativesList.get(this.relativePosition));
            this.relativeDialog.show();
        } else if (event.contains("family_id_")) {
            bindRelative(event.substring(event.lastIndexOf("_") + 1, event.length()), this.myRelativesList.get(this.relativePosition).id);
        } else if ("sign_today".equals(event)) {
            initLunerDate();
        } else if ("tong_xin_lu".equals(event)) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), GET_CONTACT);
        } else if ("sign_window".equals(event)) {
            popupSignWindow();
        } else if (event.contains("big_near_people_")) {
            zoomUpNearPeopleSearch(JSON.parseArray(event.replace("big_near_people_", ""), NearPerson.class));
        } else if (event.contains("reflash_people_")) {
            if (bigNearPeopleWindow != null && bigNearPeopleWindow.isShowing()) {
                List<NearPerson> reflashPeople = JSON.parseArray(event.replace("reflash_people_", ""), NearPerson.class);
                JSON.parseArray(event.replace("reflash_people_", ""), NearPerson.class);
                this.bigNearPeopleWindow.setData(reflashPeople);
            }
        } else if ("put_thing".equals(event)) {
            if (this.bigMoneyTree != null) {
                this.bigMoneyTree.dismiss();
            }
            this.rootView.findViewById(R.id.fangrujingxi).performClick();
        } else if ("yaoyiyao".equals(event)) {
            bigMoneyTree.dismiss();
            rootView.findViewById(R.id.yaoyiyao).performClick();
        }
    }

    private class GlideImageLoader extends ImageLoader {
        private GlideImageLoader() {
        }

        public void displayImage(Context paramContext, Object paramObject, ImageView paramImageView) {
            Glide.with(paramContext).load(paramObject).into(paramImageView);
        }
    }
}
