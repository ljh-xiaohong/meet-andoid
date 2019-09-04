package com.yuejian.meet.activities.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 选择你从事的职业
 *
 * @author lizhixin
 * @date 2016/4/11
 */
public class ChooseIndustryActivity extends BaseActivity implements AdapterView.OnItemClickListener ,View.OnClickListener,TextView.OnEditorActionListener{

    @Bind(R.id.txt_titlebar_title)
    TextView mTxtTitleBarTitle;
    @Bind(R.id.list_job_parent)
    ListView mListJobParent;
    @Bind(R.id.list_job_sub)
    ListView mListJobSub;
    @Bind(R.id.list_job_seek)
    ListView list_job_seek;
    @Bind(R.id.job_ohoose_cancel)
    TextView job_ohoose_cancel;
    @Bind(R.id.titlebar_imgBtn_back)
    ImageButton mBack;
    @Bind(R.id.txt_titlebar_confirm)
    TextView txt_titlebar_confirm;
    @Bind(R.id.et_job_search)
    EditText et_job_search;
    Activity aty;
    List<String> list_All_seek=new ArrayList<>();

    private List<Map<String, Object>> mListData;
    private int clickTemp = 0;
    private String jobs="";
    private SubJobAdapter mSubAdapter;
    private ParentAdapter mParentAdapter;
    private SeekJobAdapter seekJobAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_industry);
        aty=this;
        setRootView();
        initData();
        initWidget();
    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEND ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
            search(v.getText().toString());
            return true;
        }
        return false;
    }
    public void search(String jobsSeek){
        list_All_seek.clear();
        for (int i=0;i<mListData.size();i++){
            ArrayList<String> data = (ArrayList<String>) mListData.get(i).get("jobs");
            for (int y=0;y<data.size();y++){
                if (data.get(y).contains(jobsSeek)){
                    list_All_seek.add(mListData.get(i).get("job").toString()+"->"+data.get(y));
                }
            }
        }
        hintKbTwo();
    }
    //此方法只是关闭软键盘
    public void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void setRootView() {
        et_job_search.setOnEditorActionListener(this);
        txt_titlebar_confirm.setVisibility(View.VISIBLE);
        txt_titlebar_confirm.setSelected(true);
        setTitleText(getString(R.string.title_job));
    }

    protected void initWidget() {
        mTxtTitleBarTitle.setText(R.string.title_job);
        mBack.setOnClickListener(this);
        mBack.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.txt_titlebar_confirm,R.id.et_job_search,R.id.job_ohoose_cancel})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_imgBtn_back:
                finish();
                break;
            case R.id.job_imgBtn_search:
                search(et_job_search.getText().toString());
                break;
            case R.id.et_job_search:
                job_ohoose_cancel.setVisibility(View.VISIBLE);
                list_job_seek.setVisibility(View.VISIBLE);
                break;
            case R.id.job_ohoose_cancel:
                job_ohoose_cancel.setVisibility(View.GONE);
                list_job_seek.setVisibility(View.GONE);
                hintKbTwo();
                break;
            case R.id.txt_titlebar_confirm:
                if (jobs.equals("")){
                    ViewInject.shortToast(this,R.string.choose_indeustry_1);
                    return;
                }else {
                    BusCallEntity entity=new BusCallEntity();
                    entity.setCallType(BusEnum.JOBS);
                    entity.setData(jobs);
                    Bus.getDefault().post(entity);
                    finish();
                }
                break;
        }
    }

    protected void initData() {
//        boolean isEn = "en".equals(PreferencesUtil.get(aty, PreferencesUtil.KEY_SWITCH_LANGUAGE, ""));
        String json;
//        if (isEn) {
//            json = Utils.getFromAssets("job_en.json", aty);
//        } else {
            json = Utils.getFromAssets("industry.json", aty);
        json=Utils.s2tOrT2s(json);
//        }
        mListData = Utils.parseJsonStr3(json);
        ArrayList<String> data = new ArrayList<>();
        if (mListData != null && mListData.size() > 0) {
            data = (ArrayList<String>) mListData.get(0).get("jobs");
        }
        mParentAdapter = new ParentAdapter();
        mListJobParent.setAdapter(mParentAdapter);

        mSubAdapter = new SubJobAdapter();
        mSubAdapter.setData(data);
        mListJobSub.setAdapter(mSubAdapter);

        seekJobAdapter=new SeekJobAdapter();
        seekJobAdapter.setData(list_All_seek);
        list_job_seek.setAdapter(seekJobAdapter);
        seekJobAdapter.notifyDataSetChanged();

        mListJobParent.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        jobs="";
        ArrayList<String> data = (ArrayList<String>) mListData.get(position).get("jobs");
        mSubAdapter.setData(data);
        mParentAdapter.setSelection(position);
        mParentAdapter.notifyDataSetChanged();
        mSubAdapter.notifyDataSetChanged();
    }


    public class ParentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setSelection(int position) {
            clickTemp = position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(getApplication(), R.layout.item_job_choose_1, null);
                AutoUtils.autoSize(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_job_item = (TextView) convertView.findViewById(R.id.txt_job_item);
            holder.layout_job_item = (LinearLayout) convertView.findViewById(R.id.layout_job_item);
            holder.txt_job_item.setText(mListData.get(position).get("job") + "");
            if (clickTemp == position) {
                holder.layout_job_item.setBackgroundResource(R.color.text_while);
                holder.txt_job_item.setSelected(true);
            } else {
                holder.layout_job_item.setBackgroundResource(R.color.divider_line_color);
                holder.txt_job_item.setSelected(false);
            }
            return convertView;
        }
    }

    public class SubJobAdapter extends BaseAdapter {
        ArrayList<String> subData;

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
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(getApplication(), R.layout.item_job_choose, null);
                AutoUtils.autoSize(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_job_item = (TextView) convertView.findViewById(R.id.txt_job_item);
            holder.txt_job_item.setText(subData.get(position) + "");
            holder.txt_job_item.setSelected(false);
            final ViewHolder finalHolder = holder;
            if (jobs.equals(subData.get(position))){
                finalHolder.txt_job_item.setSelected(true);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jobs=subData.get(position);
                    mSubAdapter.notifyDataSetChanged();
//                    Intent intent = new Intent();
//                    intent.putExtra("job", subData.get(position) + "");
//                    ChooseIndustryActivity.this.setResult(RESULT_OK, intent);
//                    ChooseIndustryActivity.this.finish();
                }
            });
            return convertView;
        }
    }
    public class SeekJobAdapter extends BaseAdapter {
        List<String> subData;

        public void setData(List<String> subData) {
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
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = View.inflate(getApplication(), R.layout.item_job_choose, null);
                AutoUtils.autoSize(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txt_job_item = (TextView) convertView.findViewById(R.id.txt_job_item);
            holder.txt_job_item.setText(subData.get(position) + "");
            holder.txt_job_item.setSelected(false);
            final ViewHolder finalHolder = holder;
            if (subData.get(position).contains(jobs)&&!jobs.equals("")){
                finalHolder.txt_job_item.setSelected(true);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jobs=subData.get(position).substring(subData.get(position).indexOf("->")+2);
                    seekJobAdapter.notifyDataSetChanged();
//                    Intent intent = new Intent();
//                    intent.putExtra("job", subData.get(position) + "");
//                    ChooseIndustryActivity.this.setResult(RESULT_OK, intent);
//                    ChooseIndustryActivity.this.finish();
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        TextView txt_job_item;
        LinearLayout layout_job_item;
    }
}
