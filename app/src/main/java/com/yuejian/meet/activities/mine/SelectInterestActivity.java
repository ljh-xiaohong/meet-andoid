package com.yuejian.meet.activities.mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.SurnameAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.SurnameEntity;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zh02 on 2017/8/18.
 */

public class SelectInterestActivity extends BaseActivity {
    @Bind(R.id.interest_list)
    ListView interestList;

    private List<Map<String, List<String>>> dataSource = new ArrayList<>();
    private InterestAdapter adapter = new InterestAdapter();
    private int selectCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_interest);
        String title = getIntent().getStringExtra("title");
        setTitleText(title);

        TextView save = (TextView) findViewById(R.id.txt_titlebar_confirm);
        save.setVisibility(View.VISIBLE);
        save.setSelected(true);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        interestList.setAdapter(adapter);
        int type = getIntent().getIntExtra("type", -1);
        getInterestData(type);
        IntentFilter filter = new IntentFilter("selected_specialty");
        filter.addAction("unselected_specialty");
        registerReceiver(receiver, filter);
        String interest = getIntent().getStringExtra("interest");
        String specality = getIntent().getStringExtra("speciality");
        String[] tmps = null;
        if (StringUtils.isNotEmpty(interest)) {
            tmps = interest.split("、");
        } else if (StringUtils.isNotEmpty(specality)) {
            tmps = specality.split("、");
        }
        selectedInterests = new ArrayList<>();
        if (tmps != null) {
            for (String str : tmps) {
                if (StringUtils.isNotEmpty(str)) {
                    selectedInterests.add(str);
                }
            }
        }
        selectCount = selectedInterests.size();
    }

    private void getInterestData(int type) {
        if (type == -1) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", String.valueOf(type));
        apiImp.getDictData(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    JSONObject json = new JSONObject(data);
                    Iterator<String> keys = json.keys();
                    dataSource.clear();
                    while (keys.hasNext()) {
                        Map<String, List<String>> map = new HashMap<String, List<String>>();
                        String key = keys.next();
                        List<String> list = JSON.parseArray(json.getString(key), String.class);
                        map.put(key, list);
                        dataSource.add(map);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void finish() {
        if (selectedInterests.size() > 0) {
            Intent intent = new Intent();
            String ss = "";
            for (String s : selectedInterests) {
                ss += s + "、";
            }
            ss = ss.substring(0, ss.lastIndexOf("、"));
            intent.putExtra("interest", ss);
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }

    private List<String> selectedInterests = new ArrayList<>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("selected_specialty".equals(action)) {
                String specialty = intent.getStringExtra("specialty");
                if (!selectedInterests.contains(specialty)) {
                    selectedInterests.add(specialty);
                }
            } else if ("unselected_specialty".equals(action)) {
                String specialty = intent.getStringExtra("specialty");
                if (selectedInterests.contains(specialty)) {
                    selectedInterests.remove(specialty);
                }
            }
        }
    };


    private class InterestAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getBaseContext(), R.layout.interest_item, null);
                holder.header = (TextView) convertView.findViewById(R.id.header);
                holder.gridView = (GridLayout) convertView.findViewById(R.id.interest_grid_view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Map<String, List<String>> data = dataSource.get(position);
            String key = data.keySet().iterator().next();
            holder.header.setText(data.keySet().iterator().next());
            List<String> list = data.get(key);
            holder.gridView.removeAllViews();
            int height = DensityUtils.dip2px(getBaseContext(), 28);
            int margin = DensityUtils.dip2px(getBaseContext(), 8);
            int width = (DensityUtils.getScreenW(getBaseContext()) - margin * 5) / 3;
            for (String string : list) {
                CheckBox box = (CheckBox) View.inflate(getBaseContext(), R.layout.item_check_box, null);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.height = height;
                layoutParams.width = width;
                layoutParams.setMargins(0, margin, margin, margin);
                box.setText(string);
                if (selectedInterests.contains(string)) {
                    box.setChecked(true);
                }
                box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CompoundButton buttonView = (CompoundButton) v;
                        if (buttonView.isChecked()) {
                            if (selectCount >= 3) {
                                buttonView.setChecked(false);
                                Toast.makeText(mContext, "最多只能选择三个兴趣哦~", Toast.LENGTH_SHORT).show();
                            } else {
                                selectCount++;
                                Intent intent = new Intent("selected_specialty");
                                intent.putExtra("specialty", buttonView.getText().toString());
                                getBaseContext().sendBroadcast(intent);
                            }
                        } else {
                            selectCount--;
                            if (selectCount < 0) {
                                selectCount = 0;
                            }
                            Intent intent = new Intent("unselected_specialty");
                            intent.putExtra("specialty", buttonView.getText().toString());
                            getBaseContext().sendBroadcast(intent);
                        }
                    }
                });
                holder.gridView.addView(box, layoutParams);
            }
            return convertView;
        }

        private class ViewHolder {
            public TextView header;
            public GridLayout gridView;
        }
    }
}
