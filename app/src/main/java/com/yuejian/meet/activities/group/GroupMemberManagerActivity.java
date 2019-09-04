package com.yuejian.meet.activities.group;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.MembersEntity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * Created by pro on 2017/11/27.
 */

public class GroupMemberManagerActivity extends BaseActivity implements SpringView.OnFreshListener,TextView.OnEditorActionListener,CleanableEditText.ContentClear{

    CleanableEditText search;
    @Bind(R.id.txt_titlebar_delete_member)
    TextView deleteButton;
    @Bind(R.id.txt_titlebar_member_number)
    TextView memberNumber;
    private ListView memberListView;
    private SpringView springView;
    private List<MembersEntity> dataSource = new ArrayList<>();
    private String tId = "";
    private Boolean isDelect=false;
    private ManagerMemberAdapter adapter = null;
    private List<String> selectedIds = new ArrayList<>();
    ApiImp api=new ApiImp();
    LoadingDialogFragment dialog;
    int pageIndex=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_manager);
        dialog=LoadingDialogFragment.newInstance("正在操作..");
        search= (CleanableEditText) findViewById(R.id.et_group_member_search);
        search.setOnEditorActionListener(this);
        search.setRegister(this);
        springView= (SpringView) findViewById(R.id.group_manager_spring);
        springView.setHeader(new DefaultHeader(this));
        springView.setFooter(new DefaultFooter(this));
        springView.setListener(this);
        tId = getIntent().getStringExtra("tId");
        isDelect=getIntent().getBooleanExtra("isDelect",false);
        deleteButton.setText(isDelect?"删除":"确定");
        memberNumber.setVisibility(isDelect?View.VISIBLE:View.GONE);
        setTitleText("聊天成员");
        findViewById(R.id.titlebar_delete_member_layout).setVisibility(View.VISIBLE);
        memberListView = (ListView) findViewById(R.id.manager_member_list);
        adapter = new ManagerMemberAdapter();
        memberListView.setAdapter(adapter);
        findAllMemberList();
    }

    private HashMap<String, Object> params = null;

    private void findAllMemberList() {
        if (params == null) {
            params = new HashMap<>();
        } else {
            params.clear();
        }

        params.put("t_id", tId);
        params.put("keyword", search.getText().toString());
        params.put("pageIndex", pageIndex+"");
        params.put("pageItemCount", Constants.pageItemCount);
        apiImp.getGroupMemberList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex==1){
                    dataSource.clear();
                }
                pageIndex++;
                List<MembersEntity> membersEntities = JSON.parseArray(data, MembersEntity.class);
                if (membersEntities.size()>0){
                    if (membersEntities.get(0).getCustomer_id().equals(AppConfig.CustomerId)){
                        membersEntities.remove(0);
                    }
                }
                for (String customerId:selectedIds){
                    for (int i=0;i<membersEntities.size();i++){
                        if (customerId.equals(membersEntities.get(i).customer_id)){
                            membersEntities.get(i).setSelect(true);
                            break;
                        }
                    }
                }
                dataSource.addAll(membersEntities);
                adapter.notifyDataSetChanged();
                if (springView!=null){
                    springView.onFinishFreshAndLoad();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                ViewInject.shortToast(mContext,"获取信息失败");
                finish();
            }
        });

    }

    @OnClick({R.id.txt_titlebar_delete_member,R.id.group_member_search})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_titlebar_delete_member:
                if (isDelect)
                    deleteMembers();
                else
                    setAdbicatGroup();
                break;
            case R.id.group_member_search:
                pageIndex=1;
                findAllMemberList();
                break;
        }
    }

    /**
     * 踢人
     */
    private void deleteMembers() {
        if (selectedIds.size()==0){
            ViewInject.shortToast(mContext,"请至少选择一个成员");
            return;
        }
        String groupId="";
        for (String id:selectedIds){
            if (groupId.equals("")){
                groupId=id;
            }else {
                groupId=groupId+","+id;
            }
        }
        if (dialog!=null)
            dialog.show(getFragmentManager(),"");
        Map<String,Object> params=new HashMap<>();
        params.put("my_customer_id", AppConfig.CustomerId);
        params.put("customer_ids",groupId);
        params.put("t_id",tId);
        api.getGroupKick(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)
                    dialog.dismiss();
            }
        });
    }

    /**
     * 群主转让
     */
    public void setAdbicatGroup(){
        if (selectedIds.size()!=1){
            ViewInject.shortToast(mContext,"请选择一位成员");
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("old_customer_id",AppConfig.CustomerId);
        params.put("new_customer_id",selectedIds.get(0));
        params.put("t_id",tId);
        api.getAdbicatGroup(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                Intent intent=new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                if (dialog!=null)
                    dialog.dismiss();
            }
        });
    }

    @Override
    public void onRefresh() {
        pageIndex=1;
        findAllMemberList();
    }

    @Override
    public void onLoadmore() {
        findAllMemberList();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&event.getAction()==0)) {
            pageIndex=1;
            findAllMemberList();
            Utils.hideSystemKeyBoard(this,search);
            return true;
        }
        return false;
    }
    public void selectMember(){
        memberNumber.setText("("+selectedIds.size()+")");
    }
    Boolean isOne=false;
    @Override
    public void ClearText() {
        if (isOne){
            pageIndex=1;
            findAllMemberList();
        }
        isOne=true;
    }

    private class ManagerMemberAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return dataSource.size();
        }

        @Override
        public Object getItem(int i) {
            return dataSource.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_manager_member, null);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.member_check_box);
                holder.name = (TextView) convertView.findViewById(R.id.text_member_name);
                holder.age = (TextView) convertView.findViewById(R.id.member_age);
                holder.icon = (ImageView) convertView.findViewById(R.id.img_member_icon);
                holder.member_faqr = (ImageView) convertView.findViewById(R.id.member_faqr);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final MembersEntity member = dataSource.get(position);
            Glide.with(getBaseContext().getApplicationContext()).load(member.photo).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_default).into(holder.icon);
            holder.name.setText(member.getSurname()+member.name);
            holder.age.setText(" "+member.age);
            boolean isMale = "1".equals(member.sex);
            holder.age.setSelected(isMale?true:false);
            holder.checkBox.setChecked(member.isSelect);
            holder.member_faqr.setVisibility(member.getIs_family_master()>0?View.VISIBLE:View.GONE);

            final ViewHolder finalHolder = holder;
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    member.setSelect(finalHolder.checkBox.isChecked());
                    if (finalHolder.checkBox.isChecked()){
                        if (isDelect){
                            selectedIds.add(member.customer_id);
                            member.setSelect(true);
                        }else if (dataSource.size()>1){
                            selectedIds.clear();
                            selectedIds.add(member.customer_id);
                            for (int i=0;i<dataSource.size();i++){
                                if (!dataSource.get(i).getCustomer_id().equals(member.customer_id)){
                                    dataSource.get(i).setSelect(false);
                                }
                            }
                            notifyDataSetChanged();
                        }
                    }else {
                        selectedIds.remove(member.customer_id);
                    }
                    selectMember();
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalHolder.checkBox.setChecked(!finalHolder.checkBox.isChecked());
                    member.setSelect(finalHolder.checkBox.isChecked());
                    if (finalHolder.checkBox.isChecked()){
                        if (isDelect){
                            selectedIds.add(member.customer_id);
                            member.setSelect(true);
                        }else if (dataSource.size()>1){
                            selectedIds.clear();
                            selectedIds.add(member.customer_id);
                            for (int i=0;i<dataSource.size();i++){
                                if (!dataSource.get(i).getCustomer_id().equals(member.customer_id)){
                                    dataSource.get(i).setSelect(false);
                                }
                            }
                            notifyDataSetChanged();
                        }
                    }else {
                        selectedIds.remove(member.customer_id);
                    }
                    selectMember();
                }
            });
            return convertView;
        }

        class ViewHolder {
            CheckBox checkBox;
            TextView name;
            TextView age;
            ImageView icon;
            ImageView member_faqr;
        }
    }
}
