package com.yuejian.meet.framents.family;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.creation.ArticleDetailsActivity;
import com.yuejian.meet.activities.creation.VideoDetailsActivity;
import com.yuejian.meet.activities.find.ScannerActivity;
import com.yuejian.meet.activities.home.ReleaseActivity;
import com.yuejian.meet.activities.home.ReportActivity;
import com.yuejian.meet.activities.mine.MyDialogActivity;
import com.yuejian.meet.activities.search.SearchActivity;
import com.yuejian.meet.adapters.FamilyCircleFollowListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.FamilyFollowEntity;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.ui.SingleLineItemDecoration;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;

/**
 * @author : g000gle
 * @time : 2019/5/23 09:42
 * @desc : 首页 - 家圈 - 动态Fragment
 */
public class FamilyCircleFollowFragment extends BaseFragment
        implements SpringView.OnFreshListener, FamilyCircleFollowListAdapter.OnFollowListItemClickListener {

    @Bind(R.id.rv_family_circle_follow_list)
    RecyclerView mRecyclerView;
    @Bind(R.id.spring_family_follow_list)
    SpringView mSpringView;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout mEmptyList;
    @Bind(R.id.search_all)
    ImageView searchAll;
    @Bind(R.id.sweep_code)
    LinearLayout sweepCode;
    @Bind(R.id.et_search_all)
    TextView etSearchAll;
    @Bind(R.id.btn_release)
    RelativeLayout btnRelease;

    private FamilyCircleFollowListAdapter mFollowListAdapter;

    private int mNextPageIndex = 1;
    private int pageCount = 20;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_family_circle_follow, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mFollowListAdapter = new FamilyCircleFollowListAdapter(getActivity(), this, apiImp, getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mFollowListAdapter);
        mFollowListAdapter.setOnClickListener(new FamilyCircleFollowListAdapter.onClickListener() {
            @Override
            public void onClick(int position, boolean me) {
                initPopwindow(position,me);
            }

            @Override
            public void onComment(int position) {
                Intent intent=new Intent(getActivity(), MyDialogActivity.class);
                intent.putExtra("crId",followEntities.get(position).getId()+"");
                startActivity(intent);
            }
        });
        mRecyclerView.addItemDecoration(new SingleLineItemDecoration(20));
        btnRelease.setVisibility(View.VISIBLE);
        mSpringView.setFooter(new DefaultFooter(getContext()));
        mSpringView.setHeader(new DefaultHeader(getContext()));
        mSpringView.setListener(this);
        mSpringView.callFresh();
    }

    private View popupView;
    private PopupWindow window;

    private void initPopwindow(int position, boolean me) {
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        popupView = inflater.inflate(R.layout.popupwindow, null);
        window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //设置动画
        window.setAnimationStyle(R.style.popup_window_anim);
        // 设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        stateWindow.getBackground().setAlpha(10);
        //设置可以获取焦点
        window.setFocusable(true);
        //设置可以触摸弹出框以外的区域
        window.setOutsideTouchable(true);
        // 更新popupwindow的状态
        window.update();
//        //添加pop窗口关闭事件
        window.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        popupView.findViewById(R.id.bg).setOnClickListener(v -> window.dismiss());
        popupView.findViewById(R.id.cancel).setOnClickListener(v -> window.dismiss());
        TextView delect = popupView.findViewById(R.id.delect);
        TextView uninterested = popupView.findViewById(R.id.uninterested);
        TextView report = popupView.findViewById(R.id.report);
        if (me){
            delect.setVisibility(View.VISIBLE);
            uninterested.setVisibility(View.GONE);
            report.setVisibility(View.GONE);
            delect.setOnClickListener(v -> delectContent(followEntities.get(position).getId()));
        }else {
            delect.setVisibility(View.GONE);
            uninterested.setVisibility(View.VISIBLE);
            uninterested.setOnClickListener(v ->notInterested(followEntities.get(position).getId()));
            report.setVisibility(View.VISIBLE);
            report.setOnClickListener(v -> {
                Intent intent= new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("id",followEntities.get(position).getId());
                startActivityForResult(intent,1);
            });
        }
    }

    //删除
    private void delectContent(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("type", "1");
        map.put("id", id);
        apiImp.postDelectAction(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ResultBean loginBean=new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(getApplication(), loginBean.getMessage());
                onRefresh();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }
    //不感兴趣
    private void notInterested(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("type", "1");
        map.put("id", id);
        apiImp.postLoseInterest(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ResultBean loginBean=new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(getApplication(), loginBean.getMessage());
                onRefresh();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }


    //加载数据
    List<FamilyFollowEntity.DataBean> followEntities =new ArrayList<>();
    FamilyFollowEntity followEntitie;
    private void loadDataFromNet(String type, int page, int count) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("pageIndex", String.valueOf(page));
        map.put("pageItemCount", String.valueOf(count));
        apiImp.getAttentionFamilyCricleDo(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                followEntitie=new Gson().fromJson(data,FamilyFollowEntity.class);
                if (followEntitie.getCode()!=0) {
                    ViewInject.shortToast(getActivity(),followEntitie.getMessage());
                    return;
                }
                followEntities.addAll(followEntitie.getData());
                if (followEntities.size() > 0) {
                    mEmptyList.setVisibility(View.GONE);
                }else{
                    mEmptyList.setVisibility(View.VISIBLE);
                }
                if (page <= 1) {
                    //上拉最新
                    mFollowListAdapter.refresh(followEntities);
                } else {
                    //下拉更多
                    if (followEntitie.getData().size()!=pageCount){
                        ViewInject.shortToast(getActivity(),"已经是最后一页");
                    }else {
                        mFollowListAdapter.Loadmore(followEntities);
                    }
                }
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (mSpringView != null) {
                    mSpringView.onFinishFreshAndLoad();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        followEntities.clear();
        mNextPageIndex = 1;
        loadDataFromNet("0", mNextPageIndex, pageCount);
    }

    @Override
    public void onLoadmore() {
        loadDataFromNet("0", ++mNextPageIndex, pageCount);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.search_all, R.id.sweep_code, R.id.et_search_all,R.id.btn_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_release:
                getActivity().startActivityForResult(new Intent(getActivity(), ReleaseActivity.class),1);
                break;
            case R.id.search_all:
            case R.id.et_search_all:
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.sweep_code:
                getActivity().startActivity(new Intent(getActivity(), ScannerActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==7){
            followEntities.clear();
            mNextPageIndex = 1;
            loadDataFromNet("0", mNextPageIndex, pageCount);
        }
    }
}
