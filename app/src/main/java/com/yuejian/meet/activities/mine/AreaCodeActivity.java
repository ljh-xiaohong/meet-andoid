package com.yuejian.meet.activities.mine;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.AreaCodeAdapter;
import com.yuejian.meet.bean.AreaCodeEntity;
import com.yuejian.meet.utils.CharacterParser;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.CleanableEditText;
import com.yuejian.meet.widgets.SideBar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * 选择国家和区号
 */
public class AreaCodeActivity extends BaseActivity implements TextView.OnEditorActionListener,CleanableEditText.ContentClear{

    @Bind(R.id.area_lv)
    ListView listView;
    CleanableEditText editText;
    SideBar sideBar;
    PinyinComparator pinyinComparator;
    private CharacterParser characterParser;
    List<AreaCodeEntity> listAllDate=new ArrayList<>();
    List<AreaCodeEntity> listDate=new ArrayList<>();
    AreaCodeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_area_code);
        initData();
    }
    public void initData(){
        setTitleText(getString(R.string.Select_the_country_and_area_code));
        editText= (CleanableEditText) findViewById(R.id.et_area_search);
        sideBar= (SideBar) findViewById(R.id.area_slide_bar);
        editText.setOnEditorActionListener(this);
        editText.setRegister(this);
        mAdapter=new AreaCodeAdapter(listView,listDate,R.layout.item_area_code_layout);
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        pinyinComparator=new PinyinComparator();
        characterParser = CharacterParser.getInstance();
        String json;
        json = Utils.getFromAssets("areaCode.json", this);
        json=Utils.s2tOrT2s(json);
        listAllDate= filledData(JSON.parseArray(json, AreaCodeEntity.class));
        search();

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 触摸按下时的操作
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 触摸移动时的操作
                        Utils.hideSystemKeyBoard(mContext,editText);

                        break;
                    case MotionEvent.ACTION_UP:
                        // 触摸抬起时的操作

                        break;
                }
                return false;
            }
        });
    }
    //搜索
    public void search(){
        listDate.clear();
        String searchContent=editText.getText().toString().trim();
        if (StringUtil.isEmpty(searchContent)){
            listDate.addAll(listAllDate);
        }else {
            for (AreaCodeEntity info:listAllDate){
                if (info.getNation().contains(searchContent)){
                    listDate.add(info);
                }
            }
        }
        Collections.sort(listDate, pinyinComparator);
        mAdapter.setListDate(listDate);
        mAdapter.refresh(listDate);
    }

    @OnClick({R.id.area_imgBtn_search})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.area_imgBtn_search:
                search();
                Utils.hideSystemKeyBoard(this,editText);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            search();
            Utils.hideSystemKeyBoard(this,editText);
            return true;
        }
        return false;
    }

    @Override
    public void ClearText() {
        search();
    }
    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<AreaCodeEntity> filledData(List<AreaCodeEntity> date) {
        List<AreaCodeEntity> mSortList = new ArrayList<AreaCodeEntity>();
        for (int i = 0; i < date.size(); i++) {
            AreaCodeEntity contact = date.get(i);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(contact.getNation());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                contact.setSortLetters(sortString.toUpperCase());
            } else {
                contact.setSortLetters("#");
            }
            mSortList.add(contact);
        }
        return mSortList;
    }

    private class PinyinComparator implements Comparator<AreaCodeEntity> {

        public int compare(AreaCodeEntity o1, AreaCodeEntity o2) {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            if (o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }
    }
}
