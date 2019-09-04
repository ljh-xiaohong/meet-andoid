package com.yuejian.meet.framents.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.home.VideoCallInfoActivity;
import com.yuejian.meet.adapters.CallRecordsAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.CallRecordsEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 通话记录
 */
public class CallRecordsFragment extends BaseFragment implements SpringView.OnFreshListener, AdapterView.OnItemClickListener {
    @Bind(R.id.call_spring)
    SpringView springView;
    @Bind(R.id.call_listview)
    ListView listView;
    List<CallRecordsEntity> listData = new ArrayList<>();
    CallRecordsAdapter mAdapter;
    ApiImp api = new ApiImp();
    int pageIndex = 1;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_call_records, container, false);
    }


    @Override
    protected void initData() {
        springView.setFooter(new DefaultFooter(getContext()));
        springView.setHeader(new DefaultHeader(getContext()));
        springView.setListener(this);
        listView.setOnItemClickListener(this);
        mAdapter = new CallRecordsAdapter(listView, listData, R.layout.layout_call_records_list_item);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        requstData();
    }

    public void requstData() {
        if (AppConfig.userEntity == null) {
            pageIndex = 1;
            listData.clear();
            mAdapter.refresh(listData);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("pageIndex", pageIndex + "");
        params.put("pageItemCount", Constants.pageItemCount);
        api.getVideoRecord(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex == 1) {
                    listData.clear();
                }
                pageIndex++;
                listData.addAll(JSON.parseArray(data, CallRecordsEntity.class));
                mAdapter.refresh(listData);
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
                if (isDelSucceed) {
                    isDelSucceed = false;
                    isAllDel();
                }
                BusCallEntity entity = new BusCallEntity();
                entity.setCallType(BusEnum.DELETE_VIDEO_RECORD);
                Bus.getDefault().post(entity);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }
            }
        });
    }

    String delData = "";

    @Override
    public void onBusCallback(BusCallEntity event) {
        if (event.getCallType() == BusEnum.LOGOUT) {
            listData.clear();
            mAdapter.refresh(listData);
        } else if (event.getCallType() == BusEnum.IM_LOGIN) {
            pageIndex = 1;
            requstData();
        } else if (event.getCallType() == BusEnum.OPEN_compile) {//编辑
            if (event.getId().equals("1")) {
                isCompile(event.getUsable());
            }
        } else if (event.getCallType() == BusEnum.IS_SELECTLAA) {//是否全选
            if (event.getId().equals("1")) {
                isSelectAll(event.getUsable());
            }
        } else if (event.getCallType() == BusEnum.MESSGE_DEL) {//删除
            if (!event.getId().equals("1")) {
                return;
            }
            delData = "";
            for (int i = 0; i < listData.size(); i++) {
                if (listData.get(i).getSelect()) {
                    if (delData.equals("")) {
                        delData += listData.get(i).getChannel_id();
                    } else {
                        delData += "," + listData.get(i).getChannel_id();
                    }
                }
            }
            if (!delData.equals(""))
                delRequstData();
            else
                ViewInject.shortToast(getContext(), "请至少选择一条记录");
        } else if (event.getCallType() == BusEnum.LOGIN_UPDATE) {
            pageIndex = 1;
            requstData();
        }
    }

    //是否编辑
    public void isCompile(Boolean isOpen) {
        mAdapter.setIsCheck(isOpen);
        for (int i = 0; i < listData.size(); i++) {
            listData.get(i).setSelect(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    //是否全选
    public void isSelectAll(Boolean isSelect) {
        for (int i = 0; i < listData.size(); i++) {
            listData.get(i).setSelect(isSelect);
        }
        mAdapter.notifyDataSetChanged();
    }

    Boolean isDelSucceed = false;

    //删除通话记录
    public void delRequstData() {
        Map<String, Object> params = new HashMap<>();
        params.put("channel_id", delData);
        api.delVideoRecord(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                pageIndex = 1;
                isDelSucceed = true;
                requstData();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    ///是否已全部删完
    public void isAllDel() {
        if (listData.size() == 0) {
            BusCallEntity entity = new BusCallEntity();
            entity.setCallType(BusEnum.IS_DELETE_ALL);
            Bus.getDefault().post(entity);
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        requstData();
    }

    @Override
    public void onLoadmore() {
        requstData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), VideoCallInfoActivity.class);
        intent.putExtra("video_id", listData.get(position).getVideo_id());
        startActivity(intent);
    }

}
