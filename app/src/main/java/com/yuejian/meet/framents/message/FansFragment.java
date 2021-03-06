package com.yuejian.meet.framents.message;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.FriendListAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.NewFriendBean;
import com.yuejian.meet.bean.ResultBean;
import com.yuejian.meet.utils.CommonUtil;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.letterList.FirstLetterUtil;
import com.yuejian.meet.widgets.letterList.LetterComparator;
import com.yuejian.meet.widgets.letterList.LetterSideBarView;
import com.yuejian.meet.widgets.letterList.PinnedHeaderDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.tencent.bugly.beta.tinker.TinkerManager.getApplication;

/**
 * @author : ljh
 * @time : 2019/9/8 11:10
 * @desc :
 */
public class FansFragment extends Fragment implements FriendListAdapter.OnFollowListItemClickListener {

    @Bind(R.id.list)
    RecyclerView fansList;
    @Bind(R.id.ll_family_follow_list_empty)
    LinearLayout llFamilyFollowListEmpty;
    @Bind(R.id.main_side_bar)
    LetterSideBarView mainSideBar;
    private FriendListAdapter mFansAdapter;

    public FansFragment() {
    }

    //是否可见
    public boolean isVisible = false;
    //是否初始化完成
    public boolean isInit = false;
    //是否已经加载过
    public boolean isLoadOver = false;

    //界面可见时再加载数据(该方法在onCreate()方法之前执行。)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisible = isVisibleToUser;
        setParam();
    }

    /**
     * 初始化一些参数，完成懒加载和数据只加载一次的效果
     * isInit = true：此Fragment初始化完成
     * isLoadOver = false：此Fragment没有加载过
     * isVisible = true：此Fragment可见
     */
    private void setParam() {
        if (isInit && !isLoadOver && isVisible) {
            //加载数据
            initData();
        }
    }

    public ApiImp apiImp = new ApiImp();
    List<NewFriendBean.DataBean> mList = new ArrayList<>();

    private void initData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        if (getArguments().getInt("type") == 0) {
            params.put("type", 0);
        } else if (getArguments().getInt("type") == 1) {
            params.put("type", 1);
        } else {
            params.put("type", 3);
        }
        apiImp.getRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if(llFamilyFollowListEmpty==null)return;
                mList.clear();
                NewFriendBean bean = new Gson().fromJson(data, NewFriendBean.class);
                if (bean.getCode() != 0) {
                    ViewInject.shortToast(getActivity(), bean.getMessage());
                    return;
                }
                Collections.sort(bean.getData());
                mList.addAll(bean.getData());
                if (mList.size() > 0) {
                    llFamilyFollowListEmpty.setVisibility(View.GONE);
                } else {
                    llFamilyFollowListEmpty.setVisibility(View.VISIBLE);
                }
                mFansAdapter.refresh(mList);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(getActivity(), errMsg);
            }
        });
    }

    private View view;// 需要返回的布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {// 优化View减少View的创建次数
            view = inflater.inflate(R.layout.fans_fragment, null);
            isInit = true;
            setParam();
        }
        ButterKnife.bind(this, view);
        initView();
        return view;
    }
    private LinearLayoutManager llm;
    private void initView() {
        boolean isNew;
        if (getArguments().getInt("type") == 0) {
            isNew = true;
        } else {
            isNew = false;
        }
        mFansAdapter = new FriendListAdapter(getActivity(), this, apiImp, 1);
        fansList.setAdapter(mFansAdapter);
        mFansAdapter.setOnClickListener(new FriendListAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                if (mList.get(position).getRelationType() == 2 || mList.get(position).getRelationType() == 3) {
                    /*
                     * 获得view填充器对象
                     */
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    /*
                     * 得到加载view
                     */
                    View v = inflater.inflate(R.layout.dialog_tips_layout_one, null);
                    TextView message = v.findViewById(R.id.message);// 提示内容
                    TextView title = v.findViewById(R.id.title);// 提示标题
                    ImageView cancel_img = v.findViewById(R.id.cancel_img);// 提示标题
                    Button negativeButton = v.findViewById(R.id.negativeButton);// 提示文字
                    message.setText("确定不再关注？");// 设置内容
                    title.setText("");// 设置标题

                    Dialog loadingDialog = new Dialog(getActivity());// 创建自定义样式dialog
                    loadingDialog.setCancelable(true);// 可以用“返回键”取消
                    loadingDialog.setCanceledOnTouchOutside(true);//
                    loadingDialog.setContentView(v);// 设置布局
                    cancel_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadingDialog.dismiss();
                        }
                    });
                    negativeButton.setText("确定");
                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadingDialog.dismiss();
                            getAttention(position);
                        }
                    });
                    loadingDialog.show();
                }else{
                    getAttention(position);
                }
            }
        });
        llm= new LinearLayoutManager(getActivity());
        fansList.setLayoutManager(llm);
        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        fansList.addItemDecoration(decoration);
        fansList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = llm.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition==-1) return;
                  //可见范围内的第一项的位置
                    for (int i=0;i<mainSideBar.getIndexItems().length;i++){
                        if (mainSideBar.getIndexItems()[i].equals(FirstLetterUtil.getFirstLetter(mList.get(firstVisibleItemPosition).getName()))){
                            mainSideBar.setCurrentIndex(i);
                        }
                    }
            }
        });
        mainSideBar.setVisibility(View.VISIBLE);
        mainSideBar.setOnSelectIndexItemListener(new LetterSideBarView.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String letter) {
                for (int i=0; i<mList.size(); i++) {
                    if (FirstLetterUtil.getFirstLetter(mList.get(i).getName()).equals(letter)) {
                        ((LinearLayoutManager) fansList.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }
    //关注拉黑操作
    private void getAttention(int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("customerId", AppConfig.CustomerId);
        map.put("opCustomerId", mList.get(position).getCustomerId());
        if (mList.get(position).getRelationType()==1){
            map.put("type", "1");
        }else if (mList.get(position).getRelationType()==2||mList.get(position).getRelationType()==3){
            map.put("type", "2");
        }else if (mList.get(position).getRelationType()==4){
            map.put("type", "4");
        }
        apiImp.bindRelation(map, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ResultBean loginBean = new Gson().fromJson(data, ResultBean.class);
                ViewInject.shortToast(getApplication(), loginBean.getMessage());
                initData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onListItemClick(int type, int id) {

    }
}
