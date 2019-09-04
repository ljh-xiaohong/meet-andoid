package com.yuejian.meet.framents.family;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.NearPerson;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.CardConfig;
import com.yuejian.meet.widgets.CardSwipeCallback;
import com.yuejian.meet.widgets.OverLayCardLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearPersonCardSearch extends BaseFragment {
    private CardSwipeCallback callback = new CardSwipeCallback();
    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message paramAnonymousMessage) {
            if (paramAnonymousMessage.what == 1) {
                getRootView().findViewById(R.id.foucus).setSelected(true);
                handler.sendEmptyMessageDelayed(2, 200L);
            } else if (paramAnonymousMessage.what == 2) {
                getRootView().findViewById(R.id.foucus).setSelected(false);
            }
            return false;
        }
    });
    private List<NearPerson> nearPersonList = new ArrayList();
    private RecyclerView recyclerView = null;

    private void focusCustomer(String paramString1, String paramString2) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", paramString1);
        localHashMap.put("op_customer_id", paramString2);
        localHashMap.put("bind_type", String.valueOf(1));
        this.apiImp.bindRelation(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
            }
        });
    }

    private void requestNearPeople() {
        HashMap localHashMap = new HashMap();
        if (StringUtils.isNotEmpty(AppConfig.CustomerId)) {
            localHashMap.put("my_customer_id", AppConfig.CustomerId);
        }
        localHashMap.put("longitude", String.valueOf(AppConfig.slongitude));
        localHashMap.put("latitude", String.valueOf(AppConfig.slatitude));
        this.apiImp.findNearPerson(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                List<NearPerson> nearPeople = JSON.parseArray(paramAnonymousString, NearPerson.class);
                if ((nearPeople != null) && !nearPeople.isEmpty()) {
                    nearPersonList.addAll(nearPeople);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    Bus.getDefault().post("reflash_people_" + JSON.toJSONString(nearPersonList));
                }
            }
        });
    }

    protected View inflaterView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        return paramLayoutInflater.inflate(R.layout.fragment_near_person_search, null);
    }

    protected void initData() {
        super.initData();
        this.recyclerView = ((RecyclerView) getRootView().findViewById(R.id.tantan_recyclerview));
        CardConfig.initConfig(getActivity());
        this.recyclerView.setLayoutManager(new OverLayCardLayoutManager());
        this.recyclerView.setAdapter(new RandomListAdapter());
        ((DefaultItemAnimator) this.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        this.fragmentRootView.findViewById(R.id.foucus).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    return;
                }
                callback.toRight(recyclerView);
            }
        });
        this.fragmentRootView.findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                callback.toLeft(recyclerView);
            }
        });
        this.fragmentRootView.findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                callback.toRight(recyclerView);
                handler.sendEmptyMessage(1);
            }
        });
        this.callback.setSwipeListener(new CardSwipeCallback.OnSwipeListener() {
            public void onSwipeTo(RecyclerView.ViewHolder paramAnonymousViewHolder, float paramAnonymousFloat) {
                if (paramAnonymousFloat == ItemTouchHelper.RIGHT) {
                    handler.sendEmptyMessage(1);
                }
            }

            public void onSwiped(int index, int position) {
                if (index >= nearPersonList.size()) {
                    return;
                }

                if (position == ItemTouchHelper.RIGHT) {
                    focusCustomer(AppConfig.CustomerId, nearPersonList.get(index).customer_id);
                    handler.sendEmptyMessage(1);
                }

                if ((position == 2) || (position == 1)) {
                    nearPersonList.add(0, nearPersonList.remove(index));
                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    nearPersonList.remove(index);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }

                if (nearPersonList.size() == 0) {
                    requestNearPeople();
                }

            }
        });
        new ItemTouchHelper(this.callback).attachToRecyclerView(this.recyclerView);
        requestNearPeople();
    }

    public void onResume() {
        super.onResume();
    }

    public void receiverBus(String paramString) {
        super.receiverBus(paramString);
        if ("toRight".equals(paramString)) {
            this.callback.toRight(this.recyclerView);
        } else if ("toLeft".equals(paramString)) {
            this.callback.toLeft(this.recyclerView);
        }
    }

    private class RandomListAdapter extends RecyclerView.Adapter<NearPersonCardSearch.ViewHolder> {
        @Override
        public int getItemCount() {
            return nearPersonList.size();
        }

        @Override
        public NearPersonCardSearch.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NearPersonCardSearch.ViewHolder(View.inflate(getActivity(), R.layout.item_tantan, null));
        }

        @Override
        public void onBindViewHolder(NearPersonCardSearch.ViewHolder holder, int position) {
            NearPerson localNearPerson = nearPersonList.get(position);
            Glide.with(getContext()).load(localNearPerson.photo).into(holder.icon);
            holder.icon.setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    String json = "big_near_people_" + JSON.toJSONString(nearPersonList);
                    Bus.getDefault().post(json);
                }
            });
            Utils.setAgeAndSexView(getContext(), holder.age, localNearPerson.sex, localNearPerson.age);

            if ("-1".equals(localNearPerson.distance)) {
                holder.location.setVisibility(View.GONE);
            } else {
                holder.location.setVisibility(View.VISIBLE);
                holder.location.setText(new StringBuffer(" " + localNearPerson.distance + "km"));
            }
            holder.name.setText(new StringBuffer(localNearPerson.surname + localNearPerson.name));
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView age = null;
        ImageView icon = null;
        TextView location = null;
        TextView name = null;

        public ViewHolder(View paramView) {
            super(paramView);
            this.name = ((TextView) paramView.findViewById(R.id.name));
            this.icon = ((ImageView) paramView.findViewById(R.id.icon));
            this.age = ((TextView) paramView.findViewById(R.id.person_age));
            this.location = ((TextView) paramView.findViewById(R.id.location));
        }
    }
}
