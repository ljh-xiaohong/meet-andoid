package com.yuejian.meet.activities.message;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.google.gson.Gson;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.FirstAdapter;
import com.yuejian.meet.adapters.SecondAdapter;
import com.yuejian.meet.adapters.SelectAdapter;
import com.yuejian.meet.bean.ChooseIndustryBean;
import com.yuejian.meet.bean.SelectBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选择你从事的职业
 *
 * @author lizhixin
 * @date 2016/4/11
 */
public class NewChooseIndustryActivity extends Activity {


    @Bind(R.id.first_list)
    RecyclerView firstList;
    @Bind(R.id.second_recyclerview)
    RecyclerView secondRecyclerview;
    @Bind(R.id.select_recyclerview)
    RecyclerView selectRecyclerview;
    private SecondAdapter secondAdapter;
    private FirstAdapter firstAdapter;
    private SelectAdapter selectAdapter;

    private List<ChooseIndustryBean.DataBean> firstData=new ArrayList<>();
    private List<ChooseIndustryBean.DataBean.JobsBean> secondData=new ArrayList<>();
    private List<SelectBean> selectData=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_choose_industry);
        ButterKnife.bind(this);
        initView();
        initData();
    }
    private int firstPosition;
    private int secondPosition;
    public void initView() {
        firstAdapter = new FirstAdapter(this,firstData);
        firstList.setLayoutManager(new LinearLayoutManager(this));
        firstList.setAdapter(firstAdapter);
        firstAdapter.setClick(new FirstAdapter.OnClick() {
            @Override
            public void click(int position) {
                firstPosition=position;
                secondData.clear();
                secondData.addAll(firstData.get(position).getJobs());
                secondAdapter.notifyDataSetChanged();
            }
        });
        secondAdapter = new SecondAdapter(this,secondData);
        secondRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        secondRecyclerview.setAdapter(secondAdapter);
        secondAdapter.setClick(new SecondAdapter.OnClick() {
            @Override
            public void click(int position) {
                secondPosition=position;
                if (secondData.get(position).isIsSelect()) {
                    SelectBean selectBean = new SelectBean();
                    selectBean.setName(secondData.get(position).getJob());
                    selectBean.setFirstPosition(firstPosition);
                    selectBean.setSecondPosition(secondPosition);
                    selectData.add(selectBean);
                }else {
                    for (int i=0;i<selectData.size();i++){
                        if (selectData.get(i).getName().equals(secondData.get(position).getJob())){
                            selectData.remove(i);
                            break;
                        }
                    }
                }
                selectAdapter.notifyDataSetChanged();
            }
        });
//
        selectAdapter = new SelectAdapter(this,selectData);
        selectRecyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        selectRecyclerview.setAdapter(selectAdapter);
        selectAdapter.setClick(new SelectAdapter.OnClick() {
            @Override
            public void click(int position) {
                firstData.get(selectData.get(position).getFirstPosition()).getJobs().get(selectData.get(position).getSecondPosition()).setIsSelect(false);
                secondAdapter.notifyDataSetChanged();
                selectData.remove(position);
                selectAdapter.notifyDataSetChanged();
            }
        });
    }
    public void initData(){
        StringBuilder newstringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = getResources().getAssets().open("industry.json");
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(isr);
            String jsonLine;
            while ((jsonLine = reader.readLine()) != null) {
                newstringBuilder.append(jsonLine);
            }
            reader.close();
            isr.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result =  newstringBuilder .toString();
        Gson gson = new Gson();
        ChooseIndustryBean chooseIndustryBean = gson.fromJson(result, ChooseIndustryBean.class);
        firstData.addAll(chooseIndustryBean.getData());
        firstAdapter.notifyDataSetChanged();
        secondData.addAll(chooseIndustryBean.getData().get(0).getJobs());
        secondAdapter.notifyDataSetChanged();
    }
}
