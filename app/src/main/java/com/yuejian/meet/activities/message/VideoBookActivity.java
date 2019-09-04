package com.yuejian.meet.activities.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.Bind;
import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.VideoCallInfoActivity;
import com.yuejian.meet.adapters.CallRecordsAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.CallRecordsEntity;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;
import com.yuejian.meet.widgets.springview.SpringView.OnFreshListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通话记录
 */
public class VideoBookActivity extends BaseActivity implements SpringView.OnFreshListener, AdapterView.OnItemClickListener {
    String delData = "";
    List<CallRecordsEntity> listData = new ArrayList();
    @Bind(R.id.call_listview)
    ListView listView;
    CallRecordsAdapter mAdapter;
    int pageIndex = 1;
    @Bind(R.id.call_spring)
    SpringView springView;

    public void initData() {
        this.springView.setFooter(new DefaultFooter(this));
        this.springView.setHeader(new DefaultHeader(this));
        this.springView.setListener(this);
        this.listView.setOnItemClickListener(this);
        this.mAdapter = new CallRecordsAdapter(this.listView, this.listData, R.layout.layout_call_records_list_item);
        this.listView.setAdapter(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
        requstData();
    }

    public void initView() {
        setTitleText(getString(R.string.call_records));
    }

    public void onClick(View paramView) {}

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_video_book);
        initView();
        initData();
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
        Intent intent = new Intent(this, VideoCallInfoActivity.class);
        intent.putExtra("video_id", ((CallRecordsEntity)this.listData.get(paramInt)).getVideo_id());
        startActivity(intent);
    }

    public void onLoadmore() {
        requstData();
    }

    public void onRefresh() {
        this.pageIndex = 1;
        requstData();
    }

    public void requstData() {
        if (AppConfig.userEntity == null)
        {
            this.pageIndex = 1;
            this.listData.clear();
            this.mAdapter.refresh(this.listData);
            return;
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("pageIndex", this.pageIndex + "");
        localHashMap.put("pageItemCount", "20");
        this.apiImp.getVideoRecord(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
                if (VideoBookActivity.this.springView != null) {
                    VideoBookActivity.this.springView.onFinishFreshAndLoad();
                }
            }

            public void onSuccess(String data, int paramAnonymousInt) {
                if (VideoBookActivity.this.pageIndex == 1) {
                    VideoBookActivity.this.listData.clear();
                }
                pageIndex += 1;
                VideoBookActivity.this.listData.addAll(JSON.parseArray(data, CallRecordsEntity.class));
                VideoBookActivity.this.mAdapter.refresh(VideoBookActivity.this.listData);
                if (VideoBookActivity.this.springView != null) {
                    VideoBookActivity.this.springView.onFinishFreshAndLoad();
                }
                BusCallEntity busCallEntity = new BusCallEntity();
                busCallEntity.setCallType(BusEnum.DELETE_VIDEO_RECORD);
                Bus.getDefault().post(busCallEntity);
            }
        });
    }
}
