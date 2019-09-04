package com.yuejian.meet.widgets;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BigTanTanWindow {
    private Activity activity;
    private PopupWindow bigNearPeopleWindow = null;
    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message paramAnonymousMessage) {
            if (paramAnonymousMessage.what == 1) {
                rootView.findViewById(R.id.foucus).setSelected(true);
                handler.sendEmptyMessageDelayed(2, 200L);
            } else if (paramAnonymousMessage.what == 2) {
                rootView.findViewById(R.id.foucus).setSelected(false);
            }
            return false;
        }
    });
    private List<NearPerson> nearPersonList = new ArrayList<>();
    private RecyclerView recyclerView = null;
    private ViewGroup rootView;

    public BigTanTanWindow(Activity paramActivity) {
        this.activity = paramActivity;
        initWindow();
    }

    private void focusCustomer(String customerId, String opCustomerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("op_customer_id", opCustomerId);
        params.put("bind_type", String.valueOf(1));
        new ApiImp().bindRelation(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
            }
        });
    }

    private void initWindow() {
        if (this.bigNearPeopleWindow == null) {
            int i = ScreenUtils.getScreenHeight(this.activity);
            int j = Utils.dp2px(this.activity, 44.0F);
            this.rootView = ((ViewGroup) View.inflate(this.activity, R.layout.layout_big_near_people_window, null));
            this.bigNearPeopleWindow = new PopupWindow(this.activity);
            this.bigNearPeopleWindow.setHeight(i - j);
            this.bigNearPeopleWindow.setWidth(ScreenUtils.getScreenWidth(this.activity));
            this.bigNearPeopleWindow.setContentView(this.rootView);
            this.bigNearPeopleWindow.setBackgroundDrawable(new ColorDrawable());
            initData();
        }
    }

    private void requestNearPeople() {
        HashMap<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(AppConfig.CustomerId)) {
            params.put("my_customer_id", AppConfig.CustomerId);
        }
        params.put("longitude", String.valueOf(AppConfig.slongitude));
        params.put("latitude", String.valueOf(AppConfig.slatitude));
        new ApiImp().findNearPerson(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                List<NearPerson> nearPeople = JSON.parseArray(paramAnonymousString, NearPerson.class);
                if ((paramAnonymousString != null) && (!paramAnonymousString.isEmpty())) {
                    nearPersonList.addAll(nearPeople);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    public void dismiss() {
        if (this.bigNearPeopleWindow != null) {
            this.bigNearPeopleWindow.dismiss();
        }
    }

    protected void initData() {
        this.recyclerView = ((RecyclerView) this.rootView.findViewById(R.id.tantan_recyclerview));
        this.recyclerView.setLayoutManager(new OverLayCardLayoutManager());
        this.recyclerView.setAdapter(new RandomListAdapter());
        ((DefaultItemAnimator) this.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        final CardSwipeCallback cardSwipeCallback = new CardSwipeCallback();
        this.rootView.findViewById(R.id.right).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                    return;
                }
                cardSwipeCallback.toRight(recyclerView);
                Bus.getDefault().post("toRight");
            }
        });
        this.rootView.findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                cardSwipeCallback.toLeft(recyclerView);
                Bus.getDefault().post("toLeft");
            }
        });
        this.rootView.findViewById(R.id.foucus).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                cardSwipeCallback.toRight(recyclerView);
                handler.sendEmptyMessage(1);
                Bus.getDefault().post("toRight");
            }
        });
        cardSwipeCallback.setSwipeListener(new CardSwipeCallback.OnSwipeListener() {
            public void onSwipeTo(RecyclerView.ViewHolder paramAnonymousViewHolder, float paramAnonymousFloat) {
                if (paramAnonymousFloat == ItemTouchHelper.RIGHT) {
                    handler.sendEmptyMessage(1);
                }
            }

            public void onSwiped(int position, int paramAnonymousInt2) {
                if (position >= nearPersonList.size()) {
                    return;
                }
                if (paramAnonymousInt2 == ItemTouchHelper.RIGHT ){
                    focusCustomer(AppConfig.CustomerId, (nearPersonList.get(position)).customer_id);
                    handler.sendEmptyMessage(1);
                }
                if ((paramAnonymousInt2 == 2) || (paramAnonymousInt2 == 1)) {
                    nearPersonList.add(0, nearPersonList.remove(position));
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
                nearPersonList.remove(position);
                recyclerView.getAdapter().notifyDataSetChanged();
                if (nearPersonList.size() == 0) {
                    requestNearPeople();
                }
            }
        });
        new ItemTouchHelper(cardSwipeCallback).attachToRecyclerView(this.recyclerView);
    }

    public boolean isShowing() {
        return (this.bigNearPeopleWindow != null) && (this.bigNearPeopleWindow.isShowing());
    }

    public void setData(List<NearPerson> paramList) {
        this.nearPersonList.addAll(paramList);
        this.recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void show() {
        if (this.bigNearPeopleWindow != null) {
            this.bigNearPeopleWindow.showAtLocation(this.activity.getWindow().getDecorView(), 80, 0, 0);
        }
    }

    private class RandomListAdapter
            extends RecyclerView.Adapter<BigTanTanWindow.ViewHolder> {
        private RandomListAdapter() {
        }

        public int getItemCount() {
            return nearPersonList.size();
        }

        public void onBindViewHolder(BigTanTanWindow.ViewHolder viewHolder, int paramInt) {
            NearPerson nearPerson = nearPersonList.get(paramInt);
            Glide.with(activity).load(nearPerson.photo).into(viewHolder.icon);
            Utils.setAgeAndSexView(activity, viewHolder.age, nearPerson.sex, nearPerson.age);
            viewHolder.location.setText(new StringBuffer(" " + nearPerson.distance + "km"));
            viewHolder.name.setText(new StringBuffer(nearPerson.surname + nearPerson.name));
        }

        public BigTanTanWindow.ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
            return new BigTanTanWindow.ViewHolder(View.inflate(activity, R.layout.item_tantan_big, null));
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
