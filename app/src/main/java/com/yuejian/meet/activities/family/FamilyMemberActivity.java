package com.yuejian.meet.activities.family;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.common.ChooseIndustryActivity;
import com.yuejian.meet.activities.common.SelectMemberCityActivity;
import com.yuejian.meet.activities.common.SurnameActivity;
import com.yuejian.meet.adapters.MemberAdapter;
import com.yuejian.meet.adapters.MemberAdapter2;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.FamilyMasterEntity;
import com.yuejian.meet.bean.MemberCntEntity;
import com.yuejian.meet.bean.MembersAllEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.bean.RegionEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.home.ActionFragment;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.DoubleSeekBar;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyMemberActivity extends BaseActivity implements SpringView.OnFreshListener, AdapterView.OnItemClickListener {
    @Bind(R.id.member_sv)
    SpringView springView;
    @Bind(R.id.member_list)
    ListView listView;
    @Bind(R.id.member_surname)
    TextView member_surname;
    @Bind(R.id.member_city)
    TextView member_city;
    @Bind(R.id.dialog_view)
    View mLayout;
    @Bind(R.id.member_mlayout)
    LinearLayout member_mlayout;
    @Bind(R.id.select_list_composing)
    ImageView select_list_composing;
    boolean isSelList = false;

    TextView txt_member_job, tv_age_range, txt_member_sex_all, txt_member_sex_man, txt_member_sex_women;
    TextView member_list_load;
    ImageView close_member_dialog;
    DoubleSeekBar dsb;
    View viewHeader, viewLoadMore;
    ApiImp api = new ApiImp();
    MemberAdapter mAdapter;
    MemberAdapter2 mAdapterTwo;
    List<MembersEntity> listData = new ArrayList<>();
    MemberActivityHeaderUi memberActivityHeaderUi;
    private View mPoupView = null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;
    Intent intent;
    int pageIndex = 1;//第几页
    boolean isRequst = false, isSelProvince = false;
    public String surname = "全部";
    String province = "全国";
    String age = "0,100";
    String memberSex = "全部";
    String temporarySex = "全部";
    String jobs = "全部";
    String city = "";
    String district = "";
    String areaName = "";
    private long lastClick;
    private int mTotalItemCount;//item总数
    private boolean mIsLoading = false;//是否正在加载
    private boolean isSeListIndext = false;

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_family_member);
        setTitleText("宗亲");
        initData();
    }

    private void bindPopMenuEvent(View view) {
        RelativeLayout member_choose_job = (RelativeLayout) view.findViewById(R.id.member_choose_job);
        Button member_moer_confirm = (Button) view.findViewById(R.id.member_moer_confirm);
        txt_member_job = (TextView) view.findViewById(R.id.txt_member_job);
        close_member_dialog = (ImageView) view.findViewById(R.id.close_member_dialog);
        tv_age_range = (TextView) view.findViewById(R.id.tv_age_range);
        txt_member_sex_all = (TextView) view.findViewById(R.id.txt_member_sex_all);
        txt_member_sex_man = (TextView) view.findViewById(R.id.txt_member_sex_man);
        txt_member_sex_women = (TextView) view.findViewById(R.id.txt_member_sex_women);
        if ("全部".equals(memberSex)) {
            txt_member_sex_all.setSelected(true);
        } else if ("男".equals(memberSex)) {
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
        txt_member_sex_all.setOnClickListener(this);
        txt_member_sex_man.setOnClickListener(this);
        close_member_dialog.setOnClickListener(this);
        txt_member_sex_women.setOnClickListener(this);
        dsb.setOnSeekBarChangeListener(new DoubleSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(DoubleSeekBar seekBar, int progressLow, int progressHigh) {
                tv_age_range.setText(String.valueOf(progressLow + "-" + progressHigh));
            }

            @Override
            public void onProgressAfter() {
            }
        });
    }

    private void getRequestData() {
        if ("".equals(province) || isRequst) {
            return;
        }
        isRequst = true;
        final Map<String, Object> params = new HashMap<>();
        params.put("pageItemCount", Constants.pageItemCount);
        params.put("pageIndex", pageIndex + "");
        if (isSelProvince || province.equals("全国"))
            params.put("province", province);
        if (district.equals("全部") || district.equals("全国")) {
            params.put("area_name", "");
        } else {
            params.put("area_name", areaName);
        }
        if (!memberSex.equals("全部")) {
            params.put("sex", memberSex.equals("男") ? "1" : "0");
        }
        params.put("industry", "全部".equals(jobs) ? "" : jobs);
        params.put("surname", surname == null ? "" : surname);
        params.put("age_range", age);
        params.put("latitude", "0".equals(AppConfig.slatitude) ? "" : AppConfig.slatitude);
        params.put("longitude", "0".equals(AppConfig.slongitude) ? "" : AppConfig.slongitude);
        api.getMember(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex == 1) {
                    listData.clear();
                }
                try {
                    final MembersAllEntity membersAllEntity = JSON.parseObject(data, MembersAllEntity.class);
                    List<MembersEntity> membersEntities = JSON.parseArray(membersAllEntity.getMembers(), MembersEntity.class);
                    if (membersEntities.size() < Integer.parseInt(Constants.pageItemCount)) {
                        member_list_load.setText("没有更多数据加载了");
                    } else {
                        member_list_load.setText("正在加载更多");
                        mIsLoading = false;
                    }
                    if (membersEntities.isEmpty()) {
                        pageIndex--;
                        pageIndex = pageIndex <= 0 ? 1 : pageIndex;
                    }
                    listData.addAll(membersEntities);
                    if (pageIndex == 1) {
                        MemberCntEntity memberCntEntity = JSON.parseObject(membersAllEntity.getCnt(), MemberCntEntity.class);
                        String familyState = "2";
                        FamilyMasterEntity familyMasterEntity = null;
                        if (province.equals("全国") && !StringUtil.isEmpty(membersAllEntity.getSurnameMaster())) {
                            familyState = "1";
                            familyMasterEntity = JSON.parseObject(membersAllEntity.getSurnameMaster(), FamilyMasterEntity.class);
                        } else if (isSelProvince) {
                            familyState = "3";
                            familyMasterEntity = JSON.parseObject(membersAllEntity.getProvinceMaster(), FamilyMasterEntity.class);
                        } else {
                            familyMasterEntity = JSON.parseObject(membersAllEntity.getFamilyMaster(), FamilyMasterEntity.class);
                        }
                        memberActivityHeaderUi.setFamilyMaster(familyMasterEntity, familyState);
                        memberActivityHeaderUi.setMemberCnt(memberCntEntity);
                        memberActivityHeaderUi.setReviewCnt(membersAllEntity.getFamilyMasterCount());
                    }
                    if (province.equals("全国") || surname.equals("全部")) {
                        if (!surname.equals("全部") && !StringUtil.isEmpty(membersAllEntity.getSurnameMaster())) {
                            viewHeader.setVisibility(View.VISIBLE);
                        } else {
                            viewHeader.setVisibility(View.GONE);
                        }
                    } else {
                        if (isSelProvince && membersAllEntity.getProvinceMaster().length() < 10) {
                            viewHeader.setVisibility(View.GONE);
                        } else {
                            viewHeader.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isSelList) {
                    mAdapter.refresh(listData);
                } else {
                    mAdapterTwo.refresh(listData);
                }
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                isRequst = false;
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                isRequst = false;
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
            }
        });
    }

    private void showBottomPopupWindow() {
        if (mPoupView == null) {
            mInflater = LayoutInflater.from(getBaseContext());
            mPoupView = mInflater.inflate(R.layout.activity_member_more_select, null);
            bindPopMenuEvent(mPoupView);
            mPoupWindow = new PopupWindow(mPoupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                    member_mlayout.setVisibility(View.GONE);
                }
            });
            ColorDrawable dw = new ColorDrawable(0x90000000);
            mPoupWindow.setBackgroundDrawable(dw);
        }
        int[] location = new int[2];
        mLayout.getLocationOnScreen(location);
        mPoupWindow.showAtLocation(mLayout, Gravity.NO_GRAVITY, 0, location[1] + mLayout.getHeight());
        backgroundAlpha(1f);
        member_mlayout.setVisibility(View.VISIBLE);
    }

    public void backgroundAlpha(float paramFloat) {
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        localLayoutParams.alpha = paramFloat;
        getWindow().setAttributes(localLayoutParams);
    }

    public void getAreaName() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("province", this.province);
        localHashMap.put("city", this.city);
        localHashMap.put("area", this.district);
        this.api.getAreaName(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                RegionEntity regionEntity = JSON.parseObject(paramAnonymousString, RegionEntity.class);
                areaName = regionEntity.getArea_name() == null ? "" : regionEntity.getArea_name();
                district = areaName.substring(areaName.indexOf(" ") + 1, areaName.length());
                pageIndex = 1;
                getRequestData();
            }
        });
    }

    public String getDistrict() {
        return this.areaName;
    }

    public String getSurname() {
        return this.surname;
    }

    protected void initData() {
        pageIndex = 1;
        viewLoadMore = View.inflate(getBaseContext(), R.layout.member_list_load_more, null);
        member_list_load = (TextView) viewLoadMore.findViewById(R.id.member_list_load);
        viewHeader = findViewById(R.id.member_sponsor);
        select_list_composing.setSelected(isSelList);
        memberActivityHeaderUi = new MemberActivityHeaderUi(this, getBaseContext(), viewHeader);
        springView.setHeader(new DefaultHeader(getBaseContext()));
//        springView.setFooter(new DefaultFooter(getBaseContext()));
        springView.setListener(this);
//        listView.setOnItemClickListener(this);
        mAdapterTwo = new MemberAdapter2(this, listData);
        mAdapter = new MemberAdapter(listView, listData, R.layout.item_member_fragment);
        listView.setAdapter(isSelList ? mAdapter : mAdapterTwo);

        listView.addFooterView(viewLoadMore);
        getRequestData();
        member_surname.setText(surname);
        member_city.setText(district.equals("") ? city.equals("") ? province : city : district);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
                int lastVisibleIndex = listView.getLastVisiblePosition();
//                if (isSelList){
//                    selVisibleIndex=lastVisibleIndex;
//                }else {
//                    selVisibleIndex=lastVisibleIndex*2;
//                }
                if (!mIsLoading && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastVisibleIndex == mTotalItemCount - 1) {
                    mIsLoading = true;
                    pageIndex += 1;
                    getRequestData();
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (isSelList) {
//                        selVisibleIndex = listView.getFirstVisiblePosition();
                    } else {
//                        selVisibleIndex = listView.getFirstVisiblePosition() * 2;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mTotalItemCount = totalItemCount;
            }
        });
    }

    public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
    }

    @Override
    public void onBusCallback(BusCallEntity event) {
        if (ActionFragment.isSel) return;
        if (event.getCallType() == BusEnum.LOGIN_UPDATE) {
            member_surname.setText("全部");
            member_city.setText("全国");
            surname = "全部";
            province = "全国";
            pageIndex = 1;
            getRequestData();
        } else if (event.getCallType() == BusEnum.LOCATION_UPDATE) {
        } else if (event.getCallType() == BusEnum.SURNAME_UPDATE) {
            surname = event.getData();
            pageIndex = 1;
            member_surname.setText(surname);
            getRequestData();
        } else if (event.getCallType() == BusEnum.district) {
            isSelProvince = false;
            if (AppConfig.district.equals(event.getData())) {
                province = AppConfig.province;
                city = AppConfig.city;
                district = AppConfig.district;
                member_city.setText(district.equals("") ? city : district);
                getAreaName();
                return;
            } else if (StringUtils.isAutonomy(event.getData())) {
                province = event.getData();
                city = event.getData();
                district = event.getData();
            } else if ("全部".equals(event.getData())) {
                province = "全国";
                district = "全国";
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
            getRequestData();
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
                getRequestData();
            }
        } else if (event.getCallType() == BusEnum.JOBS) {
            if (txt_member_job != null)
                txt_member_job.setText(event.getData() + " ");
        } else if (event.getCallType() == BusEnum.NETWOR_CONNECTION) {
            if (listData.size() == 0)
                getRequestData();
        }
    }

    @OnClick({R.id.member_more, R.id.name_layout, R.id.member_city, R.id.ll_member_select_layout, R.id.select_list_composing})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name_layout://姓氏
                intent = new Intent(getBaseContext(), SurnameActivity.class);
                if (user != null) {
                    intent.putExtra("meSurname", user.getSurname());
                } else {
                    intent.putExtra("meSurname", "全部");
                }
                startActivity(intent);
                break;
            case R.id.member_city://城市
                if (AppConfig.city != null) {
                    intent = new Intent(getBaseContext(), SelectMemberCityActivity.class);
                    if (StringUtils.isAutonomy(AppConfig.province)) {
                        intent.putExtra("city", AppConfig.province);
                    } else {
                        intent.putExtra("city", AppConfig.district);
                    }
                    intent.putExtra("isSovereignty", false);
                    startActivity(intent);
                }
                break;
            case R.id.member_more://更多
                showBottomPopupWindow();
                break;
            case R.id.member_choose_job://选择行业
                intent = new Intent(getBaseContext(), ChooseIndustryActivity.class);
                startActivity(intent);
                break;
            case R.id.member_moer_confirm://确定
                jobs = txt_member_job.getText().toString().trim();
                age = tv_age_range.getText().toString().replace("-", ",");
                memberSex = temporarySex;
                pageIndex = 1;
                mPoupWindow.dismiss();
                getRequestData();
                break;
            case R.id.close_member_dialog://关闭弹窗
                if (mPoupWindow!=null&&mPoupWindow.isShowing())
                mPoupWindow.dismiss();
                break;
            case R.id.txt_member_sex_all://全部
                temporarySex = "全部";
                txt_member_sex_women.setSelected(false);
                txt_member_sex_man.setSelected(false);
                txt_member_sex_all.setSelected(true);
                break;
            case R.id.txt_member_sex_man://男
                temporarySex = "男";
                txt_member_sex_women.setSelected(false);
                txt_member_sex_man.setSelected(true);
                txt_member_sex_all.setSelected(false);
                break;
            case R.id.txt_member_sex_women://女
                temporarySex = "女";
                txt_member_sex_women.setSelected(true);
                txt_member_sex_man.setSelected(false);
                txt_member_sex_all.setSelected(false);
                break;
            case R.id.ll_member_select_layout:
                // 两次点击小于300ms 也就是连续点击
                if (System.currentTimeMillis() - lastClick < 700) {// 判断是否是执行了双击事件
                    if (listData.size() > 0)
                        listView.setSelection(0);
                }
                lastClick = System.currentTimeMillis();
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
//                if (selVisibleIndex > 4)
//                    listView.setSelection(isSelList ? selVisibleIndex : (selVisibleIndex / 2));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String customerId = "";
        if (listView.getHeaderViewsCount() > 0) {
            if (position == 0) {
                return;
            }
            customerId = listData.get(position - 1).customer_id;
        } else {
            customerId = listData.get(position).customer_id;
        }
        AppUitls.goToPersonHome(mContext, customerId);
    }


    public void onLoadmore() {
        this.pageIndex += 1;
        getRequestData();
    }

    public void onRefresh() {
        this.pageIndex = 1;
        getRequestData();
    }
}
