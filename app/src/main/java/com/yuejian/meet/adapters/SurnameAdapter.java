package com.yuejian.meet.adapters;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.yuejian.meet.R;
import com.yuejian.meet.bean.SurnameEntity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 姓氏adpter
 */

public class SurnameAdapter extends FKAdapter<SurnameEntity> {
    private AdapterHolder mHelper;


    public SurnameAdapter(AbsListView view, List<SurnameEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }

    public void convert(AdapterHolder helper, SurnameEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }

    private void initNearByData(AdapterHolder helper, SurnameEntity item, final int position) {
        this.mHelper = helper;
        mHelper.setText(R.id.txt_name, item.getSurname());
//        TextView by_name=mHelper.getView(R.id.txt_feed_title);
//        by_name.setText("职业"+position);

        View view = mHelper.getView(R.id.txt_name);
        if (view instanceof CheckBox) {
            CheckBox box = (CheckBox) view;
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (selectCount >= 3) {
                            buttonView.setChecked(false);
                        } else {
                            selectCount++;
                            Intent intent = new Intent("selected_specialty");
                            intent.putExtra("specialty", buttonView.getText().toString());
                            mCxt.sendBroadcast(intent);
                        }
                    } else {
                        selectCount--;
                        if (selectCount < 0) {
                            selectCount = 0;
                        }
                        Intent intent = new Intent("unselected_specialty");
                        intent.putExtra("specialty", buttonView.getText().toString());
                        mCxt.sendBroadcast(intent);
                    }
                }
            });
        }
    }

    private int selectCount = 0;
}
