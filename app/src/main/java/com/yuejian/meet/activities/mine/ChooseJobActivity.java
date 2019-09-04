package com.yuejian.meet.activities.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择你从事的职业
 *
 * @author lizhixin
 * @date 2016/4/11
 */
public class ChooseJobActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.list_job_parent)
    ListView mListJobParent;
    @Bind(R.id.list_job_sub)
    ListView mListJobSub;
    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton mBack;
    Activity aty;
    private ArrayList<String> subData = new ArrayList<>();
    private String selectedJob = "";

    private List<Object> dataSource;
    private SubJobAdapter mSubAdapter;
    private ParentAdapter mParentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRootView();
        aty = this;
        ButterKnife.bind(this);
        initData();
        initWidget();
        setTitleText("选择职业");
        findViewById(R.id.txt_titlebar_send_moment).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void setRootView() {
        setContentView(R.layout.activity_choose_job);
    }

    protected void initWidget() {
        mBack.setOnClickListener(this);
        mBack.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.txt_titlebar_send_moment, R.id.titlebar_imgBtn_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_titlebar_send_moment:
                if (StringUtils.isNotEmpty(selectedJob)) {
                    Intent intent = new Intent();
                    intent.putExtra("job", selectedJob);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(mContext, "请选择相应的职业", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.titlebar_imgBtn_back:
                finish();
                break;
        }
    }

    protected void initData() {
        dataSource = new ArrayList<>();
        String json = Utils.getFromAssets("job.json", aty);
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object1 = array.getJSONObject(i);
                Iterator<String> keys = object1.keys();
                while (keys.hasNext()) {
                    String key1 = keys.next();
                    dataSource.add(key1);
                    JSONArray array2 = object1.getJSONArray(key1);
                    for (int k = 0; k < array2.length(); k++) {
                        JSONObject object2 = array2.getJSONObject(k);
                        Iterator<String> keys2 = object2.keys();
                        while (keys2.hasNext()) {
                            String key2 = keys2.next();
                            JSONArray value = object2.getJSONArray(key2);
                            ArrayList<String> jobs = new ArrayList<>();
                            for (int j = 0; j < value.length(); j++) {
                                jobs.add(value.optString(j));
                            }
                            SubItem item = new SubItem();
                            item.subJobName = key2;
                            item.subJobs = jobs;
                            dataSource.add(item);
                        }
                    }
                }
            }
            mParentAdapter = new ParentAdapter();
            mListJobParent.setAdapter(mParentAdapter);
            mSubAdapter = new SubJobAdapter();
            mSubAdapter.setData(subData);
            mListJobSub.setAdapter(mSubAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ParentAdapter extends BaseAdapter {

        private final static int HEAD = 0;

        @Override
        public int getCount() {
            return dataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return dataSource.get(position) instanceof String ? HEAD : 1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                if (getItemViewType(position) == HEAD) {
                    convertView = View.inflate(getBaseContext(), R.layout.item_choose_job_parent, null);
                    holder.jobText = (TextView) convertView.findViewById(R.id.job_parent);
                } else {
                    convertView = View.inflate(getBaseContext(), R.layout.item_choose_job_sub, null);
                    holder.jobText = (TextView) convertView.findViewById(R.id.job_text);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Object object = dataSource.get(position);
            if (object instanceof String) {
                String text = (String) object;
                holder.jobText.setText(text);
            } else if (object instanceof SubItem) {
                final SubItem item = (SubItem) object;
                holder.jobText.setText(item.subJobName);
                holder.jobText.setSelected(item.isSelected);
//                if (item.isSelected) {
//                    holder.jobText.setBackgroundColor(Color.WHITE);
//                } else {
//                    holder.jobText.setBackgroundColor(Color.parseColor("#FFF9F1"));
//                }
                holder.jobText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.isSelected = true;
                        for (Object object : dataSource) {
                            if (object instanceof SubItem) {
                                SubItem subItem = (SubItem) object;
                                if (subItem != item) {
                                    subItem.isSelected = false;
                                }
                            }
                        }
                        notifyDataSetChanged();
                        subData.clear();
                        subData.addAll(item.subJobs);
                        mSubAdapter.notifyDataSetChanged();
                    }
                });
            }
            return convertView;
        }
    }

    public class SubJobAdapter extends BaseAdapter {
        ArrayList<String> subData;
        private View currentView;

        public void setData(ArrayList<String> subData) {
            this.subData = subData;
        }

        @Override
        public int getCount() {
            return subData.size();
        }


        @Override
        public Object getItem(int position) {
            return subData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (currentView != null) {
                currentView.clearFocus();
                ViewGroup parent = (ViewGroup) mListJobParent.getParent();
                parent.requestFocus();
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(getApplication(), R.layout.item_job_choose_3, null);
                holder.jobText = (TextView) convertView.findViewById(R.id.txt_job_item);
                holder.xuanze = (ImageView) convertView.findViewById(R.id.xuanze);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.jobText.setText(subData.get(position));
            final ViewHolder finalHolder = holder;
            convertView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        selectedJob = subData.get(position);
                    }
                    finalHolder.jobText.setSelected(hasFocus);
                    finalHolder.xuanze.setVisibility(hasFocus ? View.VISIBLE : View.INVISIBLE);
                }
            });
            currentView = convertView;
            return convertView;
        }
    }

    static class ViewHolder {
        CheckBox checkJob;
        TextView jobText;
        ImageView xuanze;
    }

    private class SubItem {
        String subJobName;
        boolean isSelected;
        ArrayList<String> subJobs;
    }
}
