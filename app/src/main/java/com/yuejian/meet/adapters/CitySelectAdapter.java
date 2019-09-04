package com.yuejian.meet.adapters;

import android.view.View;
import android.widget.AbsListView;

import com.yuejian.meet.R;
import com.yuejian.meet.bean.RegionEntity;
import com.yuejian.meet.bean.SurnameEntity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;

import net.sf.json.JSON;

import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 姓氏adpter
 */

public class CitySelectAdapter extends FKAdapter<RegionEntity> {
    private AdapterHolder mHelper;


    public CitySelectAdapter(AbsListView view, List<RegionEntity> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
    }
    public void convert(AdapterHolder helper, RegionEntity item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    private void initNearByData(AdapterHolder helper, RegionEntity item, final int position){
        this.mHelper=helper;
        if (item.getProvince()!=null){
            mHelper.setText(R.id.txt_city_name,item.getProvince());
        }else if (item.getCity()!=null){
            mHelper.setText(R.id.txt_city_name,item.getCity());
        }else if (item.getArea()!=null){
            mHelper.setText(R.id.txt_city_name,item.getArea());
        }
        if (item.getSublist()==null){
            mHelper.getView(R.id.city_info).setVisibility(View.GONE);
            mHelper.getView(R.id.ciyt_info_layout).setVisibility(View.GONE);
//            mHelper.getView(R.id.select_label).setVisibility(View.VISIBLE);
        }else {
            mHelper.getView(R.id.city_info).setVisibility(View.VISIBLE);
            mHelper.getView(R.id.ciyt_info_layout).setVisibility(View.VISIBLE);
//            mHelper.getView(R.id.select_label).setVisibility(View.GONE);
            String areaName1=item.getSublist().replace("\"","").replace("[","").replace("]","");
            String[] area= areaName1.split(",");
            String areaName="";
            for (int i=0;i<area.length;i++){
                if (areaName.equals("")){
                    areaName=area[i];
                }else {
                    areaName+="/"+area[i];
                }
            }
            mHelper.setText(R.id.city_info,"包含: "+areaName);
        }
//        TextView by_name=mHelper.getView(R.id.txt_feed_title);
//        by_name.setText("职业"+position);
    }
}
