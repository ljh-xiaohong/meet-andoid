package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.bean.Reward;
import com.yuejian.meet.utils.StringUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 动态打赏详情adpter
 */

public class ActionRewardAdapter extends FKAdapter<Reward> {
    private AdapterHolder mHelper;
    private Context context;
    public boolean isMySelfReword = false;


    public ActionRewardAdapter(AbsListView view, List<Reward> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
    }

    public void convert(AdapterHolder helper, Reward item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(AdapterHolder helper, Reward item, final int position) {
        this.mHelper = helper;
        mHelper.setText(R.id.reward_name, item.getSurname() + item.getName());
        mHelper.setText(R.id.reward_create_time, StringUtils.friendlyTime(item.getCreate_time()));
        if (isMySelfReword && StringUtils.isNotEmpty(item.gift_name)) {
            mHelper.getView(R.id.gift_name).setVisibility(View.VISIBLE);
            mHelper.setText(R.id.gift_name, item.gift_name);
            mHelper.getView(R.id.shang).setVisibility(View.GONE);
        } else {
            mHelper.getView(R.id.shang).setVisibility(View.VISIBLE);
            mHelper.getView(R.id.gift_name).setVisibility(View.GONE);
        }
        mHelper.getView(R.id.gift_cnt_layout).setVisibility("0".equals(item.cnt) || StringUtils.isEmpty(item.cnt) ? View.GONE : View.VISIBLE);
        mHelper.setText(R.id.gift_cnt, " x " + item.cnt);
        Glide.with(context).load(item.getPhoto()).asBitmap().placeholder(R.mipmap.ic_default).into((ImageView) mHelper.getView(R.id.reward_photo));
    }
}
