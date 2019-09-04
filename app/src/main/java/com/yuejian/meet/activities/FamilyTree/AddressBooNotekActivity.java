package com.yuejian.meet.activities.FamilyTree;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.group.SelectContactActivity;
import com.yuejian.meet.adapters.AddressBooNoteAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.BookJson;
import com.yuejian.meet.bean.Contact;
import com.yuejian.meet.utils.CharacterParser;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 通讯录
 */
public class AddressBooNotekActivity extends BaseActivity {
    @Bind(R.id.boo_list)
    ListView listView;
    @Bind(R.id.check_box)
    CheckBox check_box;
    AddressBooNoteAdapter mAdapter;

    List<BookJson> bookJsonsList = new ArrayList<>();
    /**
     * 获取库Phone表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER = 1;
    private CharacterParser characterParser;
    PinyinComparator pinyinComparator;

//    item_address_boo_member
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_boo_notek);
        initData();
    }
    public void initData(){
        setTitleText(getString(R.string.treeadd_name22));
        characterParser = CharacterParser.getInstance();
        pinyinComparator=new PinyinComparator();
        mAdapter=new AddressBooNoteAdapter(listView,bookJsonsList,R.layout.item_address_boo_member);
        listView.setAdapter(mAdapter);
        getPhoneContacts();
    }

    @OnClick({R.id.check_box,R.id.launch_invite})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.check_box:
                if (check_box.isChecked()){
                    for (int i=0;i<bookJsonsList.size();i++){
                        bookJsonsList.get(i).setSelect(true);
                    }
                    mAdapter.refresh(bookJsonsList);
                }else{
                    for (int i=0;i<bookJsonsList.size();i++){
                        bookJsonsList.get(i).setSelect(false);
                    }
                    mAdapter.refresh(bookJsonsList);
                }
                break;
            case R.id.launch_invite:
                postRequst();
                break;
        }
    }
    public void postRequst(){
        if (StringUtils.isEmpty(getPhone())){
            ViewInject.toast(this,R.string.treeadd_name23);
            return;
        }
        Map<String,Object> params=new HashMap<>();
        params.put("customer_id",user.getCustomer_id());
        params.put("phones",getPhone());
        apiImp.noteInvite(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                finish();
                ViewInject.toast(getApplication(),R.string.treeadd_name24);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
    public String getPhone(){
        String phoneArray="";
        for (int i=0;i<bookJsonsList.size();i++){
            if (bookJsonsList.get(i).getSelect()){
                phoneArray+=bookJsonsList.get(i).getMobile()+",";
            }
        }
        if (!StringUtils.isEmpty(phoneArray)){
            phoneArray=phoneArray.substring(0,phoneArray.length()-1);
        }
        phoneArray=phoneArray.replace(" ","");
        return phoneArray;
    }

    // 获取手机联系人
    private void getPhoneContacts(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

        }else {
            ContentResolver resolver = mContext.getContentResolver();
            // 获取手机联系人
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PHONES_PROJECTION, null, null, null);
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    BookJson bookJson = new BookJson();
                    bookJson.setByname(phoneCursor.getString(PHONES_DISPLAY_NAME));
                    bookJson.setMobile(phoneCursor.getString(PHONES_NUMBER));
                    bookJsonsList.add(bookJson);
                };
                phoneCursor.close();
                bookJsonsList=filledData(bookJsonsList);
                Collections.sort(bookJsonsList, pinyinComparator);
                mAdapter.refresh(bookJsonsList);
                mAdapter.setListDate(bookJsonsList);
            }
        }
    }
    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<BookJson> filledData(List<BookJson> date) {
        List<BookJson> mSortList = new ArrayList<BookJson>();
        for (int i = 0; i < date.size(); i++) {
            BookJson contact = date.get(i);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(contact.getByname());
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
    private class PinyinComparator implements Comparator<BookJson> {

        public int compare(BookJson o1, BookJson o2) {
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
