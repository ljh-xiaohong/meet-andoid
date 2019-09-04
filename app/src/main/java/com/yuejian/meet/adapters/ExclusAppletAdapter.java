package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.applet.ExclusiveAppletActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.AppletJsonListEntity;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.StringUtils;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 专属小程序adpter
 */

public class ExclusAppletAdapter extends FKAdapter<AppletJsonListEntity> {
    private AdapterHolder mHelper;
    private Context context;
    public int type;


    public ExclusAppletAdapter(AbsListView view, List<AppletJsonListEntity> mDatas, int itemLayoutId,int type) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
        this.type=type;
    }

    public void convert(AdapterHolder helper, AppletJsonListEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(AdapterHolder helper, final AppletJsonListEntity item, final int position) {
        this.mHelper = helper;
        helper.getView(R.id.item_name).setVisibility(StringUtils.isEmpty(item.getDesc())?View.GONE:View.VISIBLE);
        if(StringUtils.isEmpty(item.getDesc())){
            helper.setText(R.id.item_desc,item.getName());
        }else {
            helper.setText(R.id.item_name,item.getName());
            helper.setText(R.id.item_desc,item.getDesc());
        }
        final RadioButton button=helper.getView(R.id.item_radio);
        button.setChecked(item.isSelect);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ExclusiveAppletActivity)mCxt).select(item,type);
            }
        });
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ExclusiveAppletActivity)mCxt).select(item,type);
            }
        });
    }

}
