package com.yuejian.meet.framents.family;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.api.utils.PreferencesMessage;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.message.GroupChatActivity;
import com.yuejian.meet.activities.mine.ContactActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.activities.mine.SettingActivity;
import com.yuejian.meet.activities.search.MainSearchActivity;
import com.yuejian.meet.adapters.MyFragmentPagerAdapter;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.DadanPreference;
import com.yuejian.meet.framents.creation.PosterFragment;
import com.yuejian.meet.widgets.FamilyCircleTitleView;

import java.util.ArrayList;

import butterknife.Bind;


/**
 * @author : g000gle
 * @time : 2019/05/09 15:00
 * @desc : 首页 - 家圈Fragment
 */
public class FamilyCircleContainerFragment extends BaseFragment
        implements ViewPager.OnPageChangeListener, FamilyCircleTitleView.OnTitleViewClickListener {

    @Bind(R.id.family_circle_title_view)
    FamilyCircleTitleView mFamilyCircleTitleView;
    @Bind(R.id.vp_family_circle_content)
    ViewPager mContentPager;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nv_family_circle)
    NavigationView mNavigationView;

    ImageView drawerBack;
    View allTheme;
    View videoTheme;
    View articleTheme;
    View activityAndDemand;
    View globalFamilyName;
    View setting;


    private FamilyCircleRecommendFragment recommendFragment;
    private FamilyCircleFollowFragment followFragment;
    private FamilyCircleSameCityFragment cityFragment;


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_family_circle_container, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);

        ArrayList<Fragment> mFragmentList = new ArrayList<>();
//        mFragmentList.add(new MyFamliyFragment());
        mFragmentList.add(followFragment = new FamilyCircleFollowFragment());
        mFragmentList.add(recommendFragment = new FamilyCircleRecommendFragment());
        mFragmentList.add(cityFragment = new FamilyCircleSameCityFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList);
        mContentPager.setAdapter(adapter);
        mContentPager.setOffscreenPageLimit(2);
        mContentPager.addOnPageChangeListener(this);
        mFamilyCircleTitleView.setOnTitleViewClickListener(this);
        setCurrentItem(1);
        //抽屉控件初始化
        View headerView = mNavigationView.getHeaderView(0);
        drawerBack = (ImageView) headerView.findViewById(R.id.iv_drawer_close);
        allTheme = headerView.findViewById(R.id.ll_sidebar_item_all);
        videoTheme = headerView.findViewById(R.id.ll_sidebar_item_video);
        articleTheme = headerView.findViewById(R.id.ll_sidebar_item_article);
        activityAndDemand = headerView.findViewById(R.id.ll_sidebar_item_activity);
        globalFamilyName = headerView.findViewById(R.id.ll_sidebar_item_global);
        setting = headerView.findViewById(R.id.ll_sidebar_item_setting);
        initDrawerListener();
    }

    private void initDrawerListener() {
        drawerBack.setOnClickListener(view -> mDrawerLayout.closeDrawer(GravityCompat.START));
        allTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
//                recommendFragment.loadDataFromNet(FamilyCircleRecommendFragment.Type.video, 1, 20, true);
                recommendFragment.loadDataFromNet(1, 10);
            }
        });
        activityAndDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                ((MainActivity) getContext()).selectNext("2");
            }
        });
        videoTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
//                recommendFragment.loadDataFromNet(FamilyCircleRecommendFragment.Type.video, 1, 20, false);
                recommendFragment.loadDataFromNet(1, 10);
            }
        });

        articleTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
//                recommendFragment.loadDataFromNet(FamilyCircleRecommendFragment.Type.article, 1, 20, false);
                recommendFragment.loadDataFromNet(1, 10);
            }
        });

        globalFamilyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                if (PreferencesMessage.get(mContext, PreferencesMessage.family_id + AppConfig.CustomerId, "0").equals("0")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(R.string.Fill_in_the_location);
                    builder.setPositiveButton(R.string.To_fill_in, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            BusCallEntity busCallEntity = new BusCallEntity();
                            busCallEntity.setCallType(BusEnum.Bangding_Family);
                            Bus.getDefault().post(busCallEntity);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, null);
                    builder.show();
                    return;
                }
                startActivity(new Intent(getContext(), GroupChatActivity.class));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent toSettingIntent = new Intent(getActivity(), SettingActivity.class);
                toSettingIntent.putExtra("mine", new Mine());
                startActivity(toSettingIntent);
            }
        });


    }


    @Override
    protected void initData() {
        super.initData();
    }

    /**
     * 切换页面
     *
     * @param position 分类角标
     */
    private void setCurrentItem(int position) {
        if (position==0&&!DadanPreference.getInstance(getActivity()).getBoolean("isLogin")){
            Intent intent= new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        mContentPager.setCurrentItem(position);
        resetFamilyCircleTitleView(position);


        if (position == 1) {
            //打开手势滑动
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            //禁止手势滑动
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    private void resetFamilyCircleTitleView(int position) {
        mFamilyCircleTitleView.setSelectedTitle(position);

        switch (position) {
            //通讯录
            case 0:
                mFamilyCircleTitleView.setImageBtnIcon(-1, -1, null);
                mFamilyCircleTitleView.setImageBtnClick(v -> startActivity(new Intent(getActivity(), MainSearchActivity.class)), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ContactActivity.class);
                        intent.putExtra("indexItem", 1);
                        startActivity(intent);
                    }
                });
                break;
                //搜索
            case 1:
                mFamilyCircleTitleView.setImageBtnIcon(-1, -1, null);
                mFamilyCircleTitleView.setImageBtnClick(view -> mDrawerLayout.openDrawer(GravityCompat.START),
                        view -> startActivity(new Intent(getActivity(), MainSearchActivity.class)));
                break;
                //城市地址
            case 2:
                mFamilyCircleTitleView.setImageBtnIcon(-1, -1, AppConfig.city);
                mFamilyCircleTitleView.setImageBtnClick(null, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                setCurrentItem(0);
                break;
            case 1:
                setCurrentItem(1);
                break;
            case 2:
                setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTitleViewClick(int position) {
        switch (position) {
            case 0:
                setCurrentItem(0);
                break;
            case 1:
                setCurrentItem(1);
                break;
            case 2:
                setCurrentItem(2);
                break;
            default:
                break;
        }
    }
}
