package com.yuejian.meet.framents.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.ActionInfoActivity;
import com.yuejian.meet.activities.home.ActionMessageActivity;
import com.yuejian.meet.activities.web.VideoViewActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.PersonAction;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.AppinitUtil;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.SwipeRefreshView;
import com.yuejian.meet.widgets.ninegridimageview.ItemImageClickListener;
import com.yuejian.meet.widgets.ninegridimageview.NineGridImageView;
import com.yuejian.meet.widgets.ninegridimageview.NineGridImageViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/12.
 */

public class PersonActionsFragment extends BaseFragment {
    @Bind(R.id.person_action_list)
    ListView actionList;
    @Bind(R.id.ap_swipe_refresh_layout)
    SwipeRefreshView swipeRefreshLayout;
    @Bind(R.id.action_message)
    LinearLayout actionMessageLayout;

    private final int FROM_PERSON_ACTION = 1222;
    private List<PersonAction> personActions = new ArrayList<>();
    private PersonActionAdapter adapter = new PersonActionAdapter();
    private String customerId;
    private int pageIndex = 1;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_person_actions, container, false);
    }

    @Override
    protected void initData() {
        actionList.setAdapter(adapter);
        setListViewHeightBasedOnChildren(actionList);
        customerId = getActivity().getIntent().getStringExtra(Constants.KEY_CUSTOMER_ID);
        actionMessageLayout.setVisibility(AppConfig.CustomerId.equals(customerId) ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setItemCount(5);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_blue_bright, R.color.colorPrimaryDark,
                android.R.color.holo_orange_dark, android.R.color.holo_red_dark, android.R.color.holo_purple);
        // 手动调用,通知系统去测量
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                handler.sendEmptyMessageDelayed(UPDATE_DATA, 1200);
            }
        });
        swipeRefreshLayout.setOnLoadMoreListener(new SwipeRefreshView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageIndex++;
                handler.sendEmptyMessageDelayed(UPDATE_DATA, 1200);

            }
        });
        getPersonActionList(customerId);
        actionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PersonAction action = personActions.get(position);
                Intent intent = new Intent(getContext(), ActionInfoActivity.class);
                intent.putExtra("action_id", action.action_id);
                getActivity().startActivityForResult(intent, FROM_PERSON_ACTION);
            }
        });
    }

    private String dataJson = "";

    private void getPersonActionList(String customerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", "16");
        apiImp.getMyActionList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(final String data, int id) {
                dataJson = data;
                List<PersonAction> actions = JSON.parseArray(dataJson, PersonAction.class);
                if (actions != null && actions.size() > 0) {
                    if (personActions.isEmpty()) {
                        personActions.addAll(actions);
                    } else {
                        List<PersonAction> tmp = new ArrayList<>();
                        for (PersonAction action : actions) {
                            boolean isExisted = false;
                            for (PersonAction a : personActions) {
                                if (!a.action_id.equals(action.action_id)) {
                                    isExisted = false;
                                } else {
                                    isExisted = true;
                                    break;
                                }
                            }
                            if (!isExisted) {
                                tmp.add(action);
                            }
                        }
                        personActions.addAll(tmp);
                    }

                    if (personActions.isEmpty()) {
                        actionMessageLayout.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                }
                if (personActions.isEmpty()) {
                    getRootView().findViewById(R.id.no_data_tips_layout).setVisibility(View.VISIBLE);
                } else {
                    getRootView().findViewById(R.id.no_data_tips_layout).setVisibility(View.GONE);
                }
                handler.sendEmptyMessage(UPDATE_FINISH);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private static final int UPDATE_DATA = 1111;
    private static final int UPDATE_FINISH = 1112;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == UPDATE_DATA) {
                getPersonActionList(customerId);
            } else if (msg.what == UPDATE_FINISH) {
                if (swipeRefreshLayout == null) return false;
                if (pageIndex == 1) {
                    swipeRefreshLayout.setRefreshing(false);
                } else if (pageIndex > 1) {
                    swipeRefreshLayout.setLoading(false);
                }
            }
            return false;
        }
    });

    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + (int) mContext.getResources().getDimension(R.dimen.px_40);
        listView.setLayoutParams(params);
    }

    @OnClick({R.id.action_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_message:
                Intent intent = new Intent(getActivity(), ActionMessageActivity.class);
                intent.putExtra("from_person_action", true);
                startActivity(intent);
                break;
        }
    }


    private class PersonActionAdapter extends BaseAdapter {

        private ColorDrawable placeholderColor;

        public PersonActionAdapter() {
            placeholderColor = new ColorDrawable(Color.parseColor("#e8e8e8"));
        }

        @Override
        public int getCount() {
            return personActions.size();
        }

        @Override
        public Object getItem(int position) {
            return personActions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getContext(), R.layout.item_person_action, null);
                holder.actionTime = (TextView) convertView.findViewById(R.id.action_time);
                holder.actionContent = (TextView) convertView.findViewById(R.id.action_content);
                holder.actionPhotosGridView = (NineGridImageView<String>) convertView.findViewById(R.id.action_photos_grid);
                holder.videoPic = (ImageView) convertView.findViewById(R.id.video_img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final PersonAction action = personActions.get(position);
            holder.actionContent.setText(action.action_title);
            holder.actionTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.px_42));
            String time = StringUtils.friendlyTime(action.create_time);
            if (time.contains(AppinitUtil.yesterdayAgo)) {
                holder.actionTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.px_56));
                holder.actionTime.setText(time);
            } else if (time.contains("月") && !time.contains("年")) {
                SpannableString ss = new SpannableString(time);
                RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.4f);
                ss.setSpan(relativeSizeSpan, 0, time.indexOf("月") + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.actionTime.setText(ss);
            } else if ("刚刚".equals(time)) {
                SpannableString ss = new SpannableString(time);
                RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.4f);
                ss.setSpan(relativeSizeSpan, 0, time.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.actionTime.setText(ss);
            } else {
                holder.actionTime.setText(time);
            }

            ViewGroup videoLayout = (ViewGroup) holder.videoPic.getParent();
            videoLayout.setVisibility(View.GONE);
            holder.actionPhotosGridView.setVisibility(View.VISIBLE);
            if ("2".equals(action.url_type)) {
                videoLayout.setVisibility(View.VISIBLE);
                holder.actionPhotosGridView.setVisibility(View.GONE);
                Glide.with(getContext()).load(action.video_first_url).into(holder.videoPic);
                holder.videoPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), VideoViewActivity.class);
                        intent.putExtra(Constants.VIDEO_URL, action.action_url);
                        startActivity(intent);
                    }
                });
            } else {
                final String[] urls = action.action_url.split(",");
                handlerPictures(holder, urls);
            }
            return convertView;
        }

        class ViewHolder {
            TextView actionTime;
            NineGridImageView<String> actionPhotosGridView;
            TextView actionContent;
            ImageView videoPic;
        }

        private void handlerPictures(ViewHolder holder, final String[] urls) {
            holder.actionPhotosGridView.setVisibility(View.VISIBLE);
            List<String> actionPhotos = new ArrayList<>();
            for (String url : urls) {
                if (StringUtils.isNotEmpty(url) && actionPhotos.size() <= 4) {
                    actionPhotos.add(url);
                }
            }
            holder.actionPhotosGridView.setAdapter(new NineGridImageViewAdapter<String>() {
                @Override
                protected void onDisplayImage(Context context, ImageView imageView, String s) {
                    Glide.with(getContext()).load(s).asBitmap().placeholder(placeholderColor).into(imageView);
                }

                @Override
                protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                    super.onItemImageClick(context, imageView, index, list);
                }
            });
            holder.actionPhotosGridView.setShowStyle(NineGridImageView.STYLE_GRID);
            if (actionPhotos.size() <= 1) {
                holder.actionPhotosGridView.setImagesData(actionPhotos);
            } else if (actionPhotos.size() == 2) {
                holder.actionPhotosGridView.setImagesData(actionPhotos, NineGridImageView.LEFTROWSPAN, new int[]{2, 2});
            } else if (actionPhotos.size() == 3) {
                holder.actionPhotosGridView.setImagesData(actionPhotos, NineGridImageView.LEFTROWSPAN, new int[]{2, 2});
            } else {
                holder.actionPhotosGridView.setImagesData(actionPhotos, NineGridImageView.NOSPAN, new int[]{2, 2});
            }

            holder.actionPhotosGridView.setItemImageClickListener(new ItemImageClickListener<String>() {
                @Override
                public void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                    if (getActivity() == null) return;
                    List<String> actionPhotos = new ArrayList<>();
                    for (String url : urls) {
                        if (StringUtils.isNotEmpty(urls)) {
                            actionPhotos.add(url);
                        }
                    }
                    Utils.displayImages(getActivity(), actionPhotos, 0, null);
                }
            });
        }
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        refresh();
    }

    public void refresh() {
        pageIndex = 1;
        getPersonActionList(customerId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == FROM_PERSON_ACTION && data != null) {
                String del_action = data.getStringExtra("del_action");
                for (PersonAction action : personActions) {
                    if (action.action_id.equals(del_action)) {
                        personActions.remove(action);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
