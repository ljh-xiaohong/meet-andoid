package com.yuejian.meet.activities.family;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.InviteRank;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

/**
 *邀请家人
 */
public class MyInviteRangeActivity extends BaseActivity implements SpringView.OnFreshListener{
    @Bind(R.id.invite_spring)
    SpringView springView;
    private MyInviteAdapter adapter = null;
    private List<InviteRank> dataSource = new ArrayList();
    int pageIndex=1;


    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_my_invite);
        setTitleText(getString(R.string.Invite_your_family));
        springView.setFooter(new DefaultFooter(this));
        springView.setHeader(new DefaultHeader(this));
        springView.setListener(this);
        initBackButton(true);
        this.adapter = new MyInviteAdapter();
        ((ListView) findViewById(R.id.list)).setAdapter(this.adapter);
        inviteRankingMyList();
    }


    private void inviteRankingMyList() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", AppConfig.CustomerId);
        localHashMap.put("pageIndex", pageIndex+"");
        localHashMap.put("pageItemCount", Constants.pageItemCount);

        this.apiImp.inviteRankingMyList(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                springView.onFinishFreshAndLoad();
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                if (pageIndex==1){
                    dataSource.clear();
                }
                dataSource.addAll(JSON.parseArray(paramAnonymousString, InviteRank.class));
                adapter.notifyDataSetChanged();
                springView.onFinishFreshAndLoad();
            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        inviteRankingMyList();
    }

    @Override
    public void onLoadmore() {
        pageIndex+=1;
        inviteRankingMyList();
    }

    private class MyInviteAdapter
            extends BaseAdapter {
        private MyInviteAdapter() {
        }

        public int getCount() {
            return dataSource.size();
        }

        public Object getItem(int paramInt) {
            return dataSource.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            ViewHolder holder = null;
            if (paramView == null) {
                holder = new ViewHolder();
                paramView = View.inflate(getBaseContext(), R.layout.item_my_invite, null);
                holder.name = ((TextView) paramView.findViewById(R.id.name));
                holder.jf = ((TextView) paramView.findViewById(R.id.jf));
                holder.regTime = ((TextView) paramView.findViewById(R.id.reg_time));
                holder.photo = ((ImageView) paramView.findViewById(R.id.photo));
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            final InviteRank localInviteRank = (InviteRank) dataSource.get(paramInt);
            holder.name.setText(String.valueOf(localInviteRank.surname + localInviteRank.name));
            String str = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date(Long.parseLong(localInviteRank.register_date)));
            Long longtime=1533020900061l;
            if (localInviteRank.jf.equals("100")){
                holder.jf.setText(String.valueOf(getString(R.string.text_mine_money_lable)+"+100"));
            }else {
                holder.jf.setText(String.valueOf(getString(R.string.integral) + localInviteRank.jf));
            }
            holder.regTime.setText(String.valueOf(getString(R.string.registration_date)+"：" + str));
            Glide.with(getBaseContext()).load(localInviteRank.photo).into(holder.photo);
//            holder.jf.setText(String.valueOf(getString(R.string.integral) + localInviteRank.jf));
            paramView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), WebActivity.class);
                    intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + localInviteRank.customer_id);
                    startActivity(intent);
                }
            });
            return paramView;
        }

        class ViewHolder {
            TextView jf;
            TextView name;
            ImageView photo;
            TextView regTime;
        }
    }
}
