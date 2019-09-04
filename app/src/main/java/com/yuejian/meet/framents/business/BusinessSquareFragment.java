package com.yuejian.meet.framents.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.umeng.socialize.utils.Log;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.CoverPersonListItemEntity;
import com.yuejian.meet.bean.HotChatGroupEntity;
import com.yuejian.meet.bean.PositionInfo;
import com.yuejian.meet.bean.TagEntity;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.widgets.BusinessSquareItemView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author : g000gle
 * @time : 2019/06/24 16:13
 * @desc : 首页 - 商圈广场Fragment
 */
public class BusinessSquareFragment extends BaseFragment implements BusinessSquareItemView.OnSquareItemViewClickListener {
    //    @Bind(R.id.tv_cover_person_date)
//    TextView mCoverPersonDataView;
//    @Bind(R.id.iv_cover_person_img)
//    ImageView mCoverPersonImg;
//    @Bind(R.id.tv_cover_person_title)
//    TextView mCoverPersonTitle;
//    @Bind(R.id.tv_cover_person_job)
//    TextView mCoverPersonJob;
//    @Bind(R.id.tv_cover_person_more)
//    TextView mCoverPersonMore;

    //封面人物 更多
    @Bind(R.id.fragment_business_square_new_toplist_more)
    TextView tv_top_more;
    //封面人物 添加
    @Bind(R.id.fragment_business_square_new_toplist_layout)
    LinearLayout ll_top_list;
    //热门社群 更多
    @Bind(R.id.fragment_business_square_new_grouplist_more)
    TextView tv_chatgroup_more;
    //热门社群 添加
    @Bind(R.id.fragment_business_square_new_grouplist_layout)
    LinearLayout ll_chatgroup_list;
    //热门名帖 更多
    @Bind(R.id.fragment_business_square_new_taglist_more)
    TextView tv_hottag_more;
    //热门名帖 添加
    @Bind(R.id.fragment_business_square_new_taglist_layout)
    LinearLayout ll_hottag_list;
    @Bind(R.id.fragment_business_square_new_title)
    TextView tv_titleInfo;
    private boolean isOpenTop, isOpenChat, isOpenTag;

    private String province = "", city = "";

    //封面人物 列表
    private List<CoverPersonListItemEntity> coverPersonListItemEntities;

    private List<HotChatGroupEntity> hotChatGroupEntities;

    private List<TagEntity> tagEntities;


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        // return inflater.inflate(R.layout.fragment_business_square, container, false);
        return inflater.inflate(R.layout.fragment_business_square_new, container, false);
    }


//    public static BusinessSquareFragment newInstance(String crimeId) {
//        BusinessSquareFragment fragment = new BusinessSquareFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(CRIMEID, crimeId);
//        fragment.setArguments(args);
//        return fragment;
//    }


    @Override
    public void onSquareItemClick(View view, BusinessSquareItemView.TYPE type) {

        //处理item的跳转界面
        switch (type) {

            case TAG:

                AppUitls.goToPersonHome(getContext(), ((TagEntity) view.getTag()).getCustomer_id());
                break;

            case TOP:
                startAtivityTop((view.getTag()), province, city);

                break;

            case CHATGROUP:


                break;

        }

    }

    private void startAtivityTop(Object entity, String proince, String city) {

        Intent intent = new Intent(getActivity(), WebActivity.class);
        String url = "";
        if (entity instanceof CoverPersonListItemEntity) {
            url = UrlConstant.apiUrl() + "square/cover_story_sue.html?customer_id=" + user.customer_id + "&id=" + ((CoverPersonListItemEntity) entity).id + "&address=" + proince + "-" + city;
            intent.putExtra("customer_id", ((CoverPersonListItemEntity) entity).customer_id);

        } else {
            url = UrlConstant.apiUrl() + "square/cover_story_sue.html?customer_id=" + user.customer_id + "&id=" + ((CoverPersonListItemEntity.mapList) entity).getId() + "&address=" + proince + "-" + city;
            intent.putExtra("customer_id", ((CoverPersonListItemEntity.mapList) entity).getCustomer_id());
        }
        intent.putExtra("url", url);
        intent.putExtra("No_Title", true);
        startActivity(intent);

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            //封面
            case R.id.fragment_business_square_new_toplist_more:
                if (null == coverPersonListItemEntities || coverPersonListItemEntities.get(0).getMapList().isEmpty())
                    return;

                if (isOpenTop) {
                    //收起
                    ll_top_list.removeAllViews();
                    BusinessSquareItemView view = new BusinessSquareItemView(getContext());
                    view.setType(BusinessSquareItemView.TYPE.TOP, coverPersonListItemEntities.get(0), this);
                    ll_top_list.addView(view);
                    tv_top_more.setText(getResources().getString(R.string.business_more));
                    tv_top_more.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_shangquan_more_unfold), null);

                } else {
                    //放出
                    ll_top_list.removeAllViews();

                    BusinessSquareItemView view = new BusinessSquareItemView(getContext());
                    view.setType(BusinessSquareItemView.TYPE.TOP, coverPersonListItemEntities.get(0), this);
                    ll_top_list.addView(view);

                    for (CoverPersonListItemEntity.mapList entity : coverPersonListItemEntities.get(0).getMapList()) {
                        BusinessSquareItemView in = new BusinessSquareItemView(getContext());
                        in.setType(BusinessSquareItemView.TYPE.TOP, entity, this);
                        ll_top_list.addView(in);
                    }
                    tv_top_more.setText(getResources().getString(R.string.business_more_up));
                    tv_top_more.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_shangquan_more_fold), null);

                }
                isOpenTop = !isOpenTop;

                break;
            //社群
            case R.id.fragment_business_square_new_grouplist_more:

                if (null == hotChatGroupEntities || hotChatGroupEntities.isEmpty())
                    return;

                //如果 按了更多
                if (isOpenChat) {
                    //收起
                    ll_chatgroup_list.removeAllViews();
                    BusinessSquareItemView view = new BusinessSquareItemView(getContext());
                    view.setType(BusinessSquareItemView.TYPE.CHATGROUP, hotChatGroupEntities.get(0), this);
                    ll_chatgroup_list.addView(view);
                    tv_chatgroup_more.setText(getResources().getString(R.string.business_more));
                    tv_chatgroup_more.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_shangquan_more_unfold), null);


                } else {
                    //放出
                    ll_chatgroup_list.removeAllViews();
                    for (HotChatGroupEntity i : hotChatGroupEntities) {
                        BusinessSquareItemView view = new BusinessSquareItemView(getContext());
                        view.setType(BusinessSquareItemView.TYPE.CHATGROUP, i, this);
                        ll_chatgroup_list.addView(view);
                    }
                    tv_chatgroup_more.setText(getResources().getString(R.string.business_more_up));
                    tv_chatgroup_more.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_shangquan_more_fold), null);

                }
                isOpenChat = !isOpenChat;
                break;
            //名帖
            case R.id.fragment_business_square_new_taglist_more:
                if (null == tagEntities || tagEntities.isEmpty()) return;
                //如果 按了更多
                if (isOpenTag) {
                    //收起
                    ll_hottag_list.removeAllViews();
                    BusinessSquareItemView view = new BusinessSquareItemView(getContext());
                    view.setType(BusinessSquareItemView.TYPE.TAG, tagEntities.get(0), this);
                    ll_hottag_list.addView(view);
                    tv_hottag_more.setText(getResources().getString(R.string.business_more));
                    tv_hottag_more.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_shangquan_more_unfold), null);
                } else {
                    //放出
                    ll_hottag_list.removeAllViews();
                    for (TagEntity i : tagEntities) {
                        BusinessSquareItemView view = new BusinessSquareItemView(getContext());
                        view.setType(BusinessSquareItemView.TYPE.TAG, i, this);
                        ll_hottag_list.addView(view);

                    }
                    tv_hottag_more.setText(getResources().getString(R.string.business_more_up));
                    tv_hottag_more.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_shangquan_more_fold), null);
                }
                isOpenTag = !isOpenTag;


                break;


        }
    }

    @Override
    protected void initData() {
        super.initData();


        getPosition(new DataIdCallback<PositionInfo>() {
            @Override
            public void onSuccess(PositionInfo data, int id) {
                loadDataFromNet(data.getProvince(), data.getCity());
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });


        this.tv_chatgroup_more.setOnClickListener(this);
        this.tv_top_more.setOnClickListener(this);
        this.tv_hottag_more.setOnClickListener(this);
//      //封面人物
//        Map<String, String> params = new HashMap<>();
//        params.put("province", "广东省");
//        params.put("city", "珠海市");
//        apiImp.getCoverPersonListDo(params, this, new DataIdCallback<String>() {
//            @Override
//            public void onSuccess(String data, int id) {
//                List<CoverPersonListItemEntity> coverPersonListItemEntities = JSON.parseArray(data, CoverPersonListItemEntity.class);
//                if (coverPersonListItemEntities.size() > 0) {
//                    //获取列表第一项进行展示
//                    CoverPersonListItemEntity itemEntity = coverPersonListItemEntities.get(0);
//                    mCoverPersonDataView.setText(String.format("◆ 第一期 %s.%s ◆", itemEntity.year, itemEntity.month));
//                    Glide.with(BusinessSquareFragment.this).load(itemEntity.cover_url).into(mCoverPersonImg);
//                    mCoverPersonTitle.setText(String.format("%s: %s", itemEntity.username, itemEntity.motto));
//                    mCoverPersonJob.setText(itemEntity.positions);
//                    mCoverPersonMore.setOnClickListener(v -> {
//                        Intent intent = new Intent(getActivity(), WebActivity.class);
//                        String url = UrlConstant.apiUrl() + "square/cover_story_sue.html?customer_id=" + user.customer_id + "&id=" + itemEntity.id + "&address=广东省-珠海市";
//                        intent.putExtra("url", url);
//                        intent.putExtra("No_Title", true);
//                        startActivity(intent);
//                    });
//                }
//            }
//
//            @Override
//            public void onFailed(String errCode, String errMsg, int id) {
//
//            }
//        });


//        Map<String, String> params = new HashMap<>();
//        params.put("customer_id", user.getCustomer_id());
//        apiImp.getRoupAllName(params, this, new DataIdCallback<String>() {
//            @Override
//            public void onSuccess(String data, int id) {
//                List<GroupChatEntity> groupChatEntityList = new ArrayList<>();
//                GroupAllEntity groupAllEntity = JSON.parseObject(data, GroupAllEntity.class);
//                List<GroupEntity> listData = JSON.parseArray(groupAllEntity.getCity_list(), GroupEntity.class);
//                if (listData != null) System.out.println("lalalallalalalalalalal");
//            }
//
//            @Override
//            public void onFailed(String errCode, String errMsg, int id) {
//
//            }
//        });
    }


    /**
     * 获取封面人物数据及显示
     *
     * @param params
     */
    public void getCoverPersonListDo(Map<String, Object> params, Object context, BusinessSquareItemView.OnSquareItemViewClickListener listener) {
        apiImp.getCoverPersonListDo(params, context, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                //TODO 获取数据后 给控件填写item，及信息
                // data=data.replaceAll("\\\\","");
                coverPersonListItemEntities = JSON.parseArray(data, CoverPersonListItemEntity.class);
                ll_top_list.removeAllViews();

                BusinessSquareItemView view = new BusinessSquareItemView(getContext());
                view.setType(BusinessSquareItemView.TYPE.TOP, coverPersonListItemEntities.get(0), listener);
                tv_titleInfo.setText(String.format("◆ 第%s期 %s ◆", coverPersonListItemEntities.get(0).month, coverPersonListItemEntities.get(0).year));
                ll_top_list.addView(view);

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                //
            }
        });
    }

    ;

    /**
     * 获取热门社群
     *
     * @param params
     * @param context
     */
    public void getChatGroupListDo(Map<String, String> params, Object context, BusinessSquareItemView.OnSquareItemViewClickListener listener) {


        apiImp.getChatGroupListDo(null, context, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                //TODO 获取数据后 给控件填写item，及信息
                ll_chatgroup_list.removeAllViews();
                hotChatGroupEntities = JSON.parseArray(data, HotChatGroupEntity.class);
                BusinessSquareItemView view = new BusinessSquareItemView(getContext());
                view.setType(BusinessSquareItemView.TYPE.CHATGROUP, hotChatGroupEntities.get(0), listener);
                ll_chatgroup_list.addView(view);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    /**
     * 获取热门名帖
     *
     * @param params
     * @param context
     */
    public void getTagListDo(Map<String, String> params, Object context, BusinessSquareItemView.OnSquareItemViewClickListener listener) {


        apiImp.getTagListDo(null, context, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                ll_hottag_list.removeAllViews();
                //todo 获取数据后 给控件填写item及信息
                tagEntities = JSON.parseArray(data, TagEntity.class);
                for (TagEntity entity : tagEntities) {
                    BusinessSquareItemView view = new BusinessSquareItemView(getContext());
                    view.setType(BusinessSquareItemView.TYPE.TAG, entity, listener);
                    ll_hottag_list.addView(view);
                }

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }


    public void loadDataFromNet(String province, String city) {
        this.province = province;
        this.city = city;
//        Log.e("sfd56", province + "," + city + "");
        Map<String, Object> params = new HashMap<>();
        params.put("province", province);
        params.put("city", city);
        params.put("type", "1");

        getCoverPersonListDo(params, getContext(), this);
        getChatGroupListDo(new HashMap<>(), getContext(), this);
        getTagListDo(new HashMap<>(), getContext(), this);

    }

}
