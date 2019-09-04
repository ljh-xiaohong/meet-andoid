package com.yuejian.meet.adapters;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.UserFeedbackActivity;
import com.yuejian.meet.adapters.base.AdapterHolder;
import com.yuejian.meet.adapters.base.FKAdapter;
import com.yuejian.meet.bean.BugTypeEntity;
import com.yuejian.meet.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh03 on 2017/6/13.
 * 标签选择adpter
 */

public class TagTypSelAdapter extends FKAdapter<String> {
    private AdapterHolder mHelper;
    private Context context;
    private List<String> list=new ArrayList<>();


    public TagTypSelAdapter(AbsListView view, List<String> mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.context = view.getContext();
    }

    public void convert(AdapterHolder helper, String item, boolean isScrolling, int position) {
        convert(helper, getItem(position), isScrolling);
        initNearByData(helper, item, position);
    }
    public String getSelTag(){
        String array="";
        for (String info:list){
            if (StringUtils.isEmpty(array)){
                array=info;
            }else {
                array+=","+info;
            }
        }
        return array;
    }
    public void setSelTag(String name,String type){
        if (type.equals("0")){
            list.add(name);
        }else {
            for (int i=0;i<list.size();i++){
                if (list.get(i).equals(name)){
                    list.remove(i);
                    break;
                }
            }
        }
    }

    private void initNearByData(final AdapterHolder helper, final String item, final int position) {
        this.mHelper = helper;
        Object top=helper.getView(R.id.tag_type_name).getTag();
        String tag=null==top?"0":top.toString();;
        tag=StringUtils.isEmpty(tag)?"0":tag;
        helper.setText(R.id.tag_type_name,item);
        helper.getView(R.id.tag_type_name).setTag(tag);
        helper.getView(R.id.tag_type_name).setSelected(!tag.equals("0"));
        final String finalTag = tag;
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.size()<3 || !finalTag.equals("0")){
                    helper.getView(R.id.tag_type_name).setTag(finalTag.equals("0")?"1":"0");
                    setSelTag(item,finalTag);
                    notifyDataSetChanged();
                }
            }
        });
    }
}
