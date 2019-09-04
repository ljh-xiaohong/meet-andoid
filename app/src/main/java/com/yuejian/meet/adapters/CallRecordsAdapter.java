package com.yuejian.meet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.CallRecordsEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 通话记录adpter
 */

public class CallRecordsAdapter extends FKAdapter<CallRecordsEntity> {
    private AdapterHolder mHelper;
    private Context context;
    private Boolean isCheck = false;


    public CallRecordsAdapter(AbsListView view, List<CallRecordsEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
    }

    public void setIsCheck(Boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void convert(AdapterHolder helper, CallRecordsEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(AdapterHolder helper, final CallRecordsEntity item, final int position) {
        this.mHelper = helper;
        try {
            int duration = Integer.valueOf(item.duration);
            int hour = duration / 60;
            int min = duration - (hour * 60);
            String hourString = "";
            if (hour >= 10) {
                hourString = "" + hour;
            } else {
                hourString = "0" + hour;
            }
            String minString = "";
            if (min >= 10) {
                minString = "" + min;
            } else {
                minString = "0" + min;
            }
            String durationStr = hourString + ":" + minString + ":00";
            mHelper.setText(R.id.call_phone_time, (item.getIs_send().equals("1") ? mCxt.getString(R.string.To_the_length) : mCxt.getString(R.string.call_the_length)) + " " + durationStr);
        } catch (Exception e) {
        }
        mHelper.getView(R.id.check_box_call).setVisibility(isCheck ? View.VISIBLE : View.GONE);
        final CheckBox check_box_call = mHelper.getView(R.id.check_box_call);
        check_box_call.setChecked(item.isSelect);
        mHelper.getView(R.id.check_box_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setSelect(!item.isSelect);
                check_box_call.setChecked(item.isSelect);
            }
        });
        mHelper.getView(R.id.call_go_come).setSelected(item.getIs_send().equals("1") ? false : true);
        mHelper.setText(R.id.txt_call_name, item.getSurname() + item.getName());
        mHelper.setText(R.id.txt_call_age, " " + item.getAge());
        mHelper.setText(R.id.txt_call_tiem, StringUtils.friendlyTime(item.getStart_time()));
        mHelper.getView(R.id.txt_call_age).setSelected(item.getSex().equals("1") ? true : false);
        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.img_call_head));
        mHelper.setClickListener(R.id.img_call_head, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUitls.goToPersonHome(mCxt, item.customer_id);
            }
        });

    }
}
