package com.yuejian.meet.activities.FamilyTree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.TreeTagAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.FriendTreeTagEntity;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.InnerGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 关系选择标签
 */
public class TreeTagActivity extends BaseActivity implements TextView.OnEditorActionListener{
    @Bind(R.id.txt_titlebar_save)
    TextView txt_titlebar_save;
    @Bind(R.id.tree_grid1)
    InnerGridView gridView1;
    @Bind(R.id.tree_grid2)
    InnerGridView gridView2;
    @Bind(R.id.tree_grid3)
    InnerGridView gridView3;
    @Bind(R.id.tree_grid4)
    InnerGridView gridView4;
    @Bind(R.id.tree_grid5)
    InnerGridView gridView5;
    @Bind(R.id.tree_layout1)
    LinearLayout tree_layout1;
    @Bind(R.id.tree_layout2)
    LinearLayout tree_layout2;
    @Bind(R.id.tree_layout3)
    LinearLayout tree_layout3;
    @Bind(R.id.tree_layout4)
    LinearLayout tree_layout4;
    @Bind(R.id.tree_layout5)
    LinearLayout tree_layout5;
    @Bind(R.id.et_search_all)
    CleanableEditText et_search;
    List<FriendTreeTagEntity> listData=new ArrayList<>();
    List<FriendTreeTagEntity> listData1=new ArrayList<>();
    List<FriendTreeTagEntity> listData2=new ArrayList<>();
    List<FriendTreeTagEntity> listData3=new ArrayList<>();
    List<FriendTreeTagEntity> listData4=new ArrayList<>();
    List<FriendTreeTagEntity> listData5=new ArrayList<>();
    FriendTreeTagEntity entity;
    TreeTagAdapter mAdapter1;
    TreeTagAdapter mAdapter2;
    TreeTagAdapter mAdapter3;
    TreeTagAdapter mAdapter4;
    TreeTagAdapter mAdapter5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_tag);
        initData();
    }
    public void setSel(FriendTreeTagEntity item){
        this.entity=item;
        pudataData();
    }
    public void pudataData(){
        int i=0;
        for (; i<listData1.size(); i++){
            listData1.get(i).setSelect(listData1.get(i).getId()==entity.getId());
        }
        i=0;
        for (; i<listData2.size(); i++){
            listData2.get(i).setSelect(listData2.get(i).getId()==entity.getId());
        }
        i=0;
        for (; i<listData3.size(); i++){
            listData3.get(i).setSelect(listData3.get(i).getId()==entity.getId());
        }
        i=0;
        for (; i<listData4.size(); i++){
            listData4.get(i).setSelect(listData4.get(i).getId()==entity.getId());
        }
        i=0;
        for (; i<listData5.size(); i++){
            listData5.get(i).setSelect(listData5.get(i).getId()==entity.getId());
        }
        loadLayout();
    }
    public void initData(){
        setTitleText(getString(R.string.treeadd_name27));
        txt_titlebar_save.setText(R.string.confirm);
        txt_titlebar_save.setVisibility(View.VISIBLE);
        et_search.setOnEditorActionListener(this);
        mAdapter1=new TreeTagAdapter(gridView1,listData1,R.layout.item_tree_tag_layout);
        mAdapter2=new TreeTagAdapter(gridView2,listData2,R.layout.item_tree_tag_layout);
        mAdapter3=new TreeTagAdapter(gridView3,listData3,R.layout.item_tree_tag_layout);
        mAdapter4=new TreeTagAdapter(gridView4,listData4,R.layout.item_tree_tag_layout);
        mAdapter5=new TreeTagAdapter(gridView5,listData5,R.layout.item_tree_tag_layout);
        gridView1.setAdapter(mAdapter1);
        gridView2.setAdapter(mAdapter2);
        gridView3.setAdapter(mAdapter3);
        gridView4.setAdapter(mAdapter4);
        gridView5.setAdapter(mAdapter5);
        requstData();
    }
    public void requstData(){
        Map<String,Object> params=new HashMap<>();
        params.put("friend_name",et_search.getText().toString());
        apiImp.getFriendTreeTag(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                listData= JSON.parseArray(data,FriendTreeTagEntity.class);
                listData1.clear();
                listData2.clear();
                listData3.clear();
                listData4.clear();
                listData5.clear();
                for (FriendTreeTagEntity info:listData){
                    if (info.getFriend_type()==1){
                        listData1.add(info);
                    }
                    if (info.getFriend_type()==2){
                        listData2.add(info);
                    }
                    if (info.getFriend_type()==3){
                        listData3.add(info);
                    }
                    if (info.getFriend_type()==4){
                        listData4.add(info);
                    }
                    if (info.getFriend_type()==5){
                        listData5.add(info);
                    }
                }
                loadLayout();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    @OnClick({R.id.txt_titlebar_save})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.txt_titlebar_save:
                    if (entity==null){
                        ViewInject.toast(this,R.string.treeadd_name23);
                        return;
                    }
                Intent intent=new Intent();
                    intent.putExtra("friend_name",entity.getFriend_name());
                    setResult(RESULT_OK,intent);
                    finish();
                break;
        }
    }

    public void loadLayout(){
        tree_layout1.setVisibility(listData1.size()==0? View.GONE:View.VISIBLE);
        tree_layout2.setVisibility(listData2.size()==0? View.GONE:View.VISIBLE);
        tree_layout3.setVisibility(listData3.size()==0? View.GONE:View.VISIBLE);
        tree_layout4.setVisibility(listData4.size()==0? View.GONE:View.VISIBLE);
        tree_layout5.setVisibility(listData5.size()==0? View.GONE:View.VISIBLE);
        mAdapter1.refresh(listData1);
        mAdapter2.refresh(listData2);
        mAdapter3.refresh(listData3);
        mAdapter4.refresh(listData4);
        mAdapter5.refresh(listData5);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            requstData();
            hintKbTwo();
            return true;
        }
        return false;
    }
    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
