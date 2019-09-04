package com.yuejian.meet.framents.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.myenum.FqrEnum;
import com.yuejian.meet.R;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Contact;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.framents.base.BaseFragment;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.CharacterParser;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.SideBar;
import com.yuejian.meet.widgets.SwipeRefreshView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zh02 on 2017/8/16.
 */

public class FriendsFragment extends BaseFragment {
    @Bind(R.id.friends_list)
    ListView friendsList;
    @Bind(R.id.friends_slide_bar)
    SideBar sideBar;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Contact> contacts = new ArrayList<>();
    private FriendsSortAdapter adapter = null;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    protected void initData() {
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    friendsList.setSelection(position);
                }
            }
        });

        swipeRefreshLayout.setRefreshing(user != null);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (user != null) {
                    getFriendsList(user.customer_id);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
            }
        });

        adapter = new FriendsSortAdapter(getContext(), contacts);
        friendsList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user != null && StringUtils.isNotEmpty(user.customer_id)) {
            getFriendsList(user.customer_id);
        }
    }

    private void getFriendsList(String customerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("relation_type", String.valueOf(Constants.GET_FRIENDS));
        params.put("pageItemCount", String.valueOf(300));
        apiImp.getRelationList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                List<Contact> contactList = JSON.parseArray(data, Contact.class);
                if (contactList != null && contactList.size() > 0) {
                    contacts.clear();
                    contacts.addAll(filledData(contactList));
                    // 根据a-z进行排序源数据
//                    Collections.sort(contacts, pinyinComparator);
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<Contact> filledData(List<Contact> date) {
        List<Contact> mSortList = new ArrayList<Contact>();
        for (int i = 0; i < date.size(); i++) {
            Contact contact = date.get(i);
            contact.setCompany_name("");
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(contact.getSurname());
            if(pinyin!=null){
                if(pinyin.length()>0) {
                    String sortString = pinyin.substring(0, 1).toUpperCase();
                    // 正则表达式，判断首字母是否是英文字母
                    if (sortString.matches("[A-Z]")) {
                        contact.setSortLetters(sortString.toUpperCase());
                    } else {
                        contact.setSortLetters("#");
                    }
                }else {
                    contact.setSortLetters("#");
                }
            }else {
                contact.setSortLetters("#");
            }

            mSortList.add(contact);
        }
        return mSortList;

    }

    public class FriendsSortAdapter extends BaseAdapter implements SectionIndexer {
        private List<Contact> list = null;
        private Context mContext;

        public FriendsSortAdapter(Context mContext, List<Contact> list) {
            this.mContext = mContext;
            this.list = list;
        }

        public int getCount() {
            return this.list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup arg2) {
            ViewHolder holder = null;
            final Contact contact = list.get(position);
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(R.layout.item_contact, null);
                holder.name = (TextView) view.findViewById(R.id.contact_name);
                holder.letter = (TextView) view.findViewById(R.id.contact_letter);
                holder.photo = (ImageView) view.findViewById(R.id.contact_photo);
                holder.age = (TextView) view.findViewById(R.id.contact_age);
                holder.itemt_wire = view.findViewById(R.id.itemt_wire);
                view.findViewById(R.id.contact_btn).setVisibility(View.GONE);
                holder.faqr = (ImageView) view.findViewById(R.id.faqr);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            //根据position获取分类的首字母的char ascii值
            int section = getSectionForPosition(position);
            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                holder.itemt_wire.setVisibility(View.GONE);
                holder.letter.setVisibility(View.VISIBLE);
                holder.letter.setText(contact.getSortLetters()==null?"":contact.getSortLetters());
            } else {
                holder.itemt_wire.setVisibility(View.VISIBLE);
                holder.letter.setVisibility(View.GONE);
            }

            Glide.with(getContext()).load(contact.photo).into(holder.photo);
            holder.age.setText(" " + contact.age);
            int sexIcon = "1".equals(contact.sex) ? R.mipmap.ic_man : R.mipmap.ic_woman;
            Drawable sexDrawable = getResources().getDrawable(sexIcon);
            sexDrawable.setBounds(0, 0, sexDrawable.getMinimumWidth(), sexDrawable.getMinimumHeight());//必须设置图片大小，否则不显示
            holder.age.setCompoundDrawables(sexDrawable, null, null, null);
            holder.name.setText((contact.surname==null?"":contact.surname) + contact.name);
            if ("1".equals(contact.sex)) {
                holder.age.setBackground(getResources().getDrawable(R.drawable.shape_man));
            } else {
                holder.age.setBackground(getResources().getDrawable(R.drawable.shape_woman));
            }

            View parent = (View) holder.name.getParent();
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUitls.goToPersonHome(mContext, contact.customer_id);
                }
            });

            ImageView faqr = holder.faqr;
            faqr.setVisibility(View.GONE);
            if (contact.getIs_super()>0){
                faqr.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(contact.is_super== FqrEnum.city.getValue()?R.mipmap.ic_shi:contact.is_super== FqrEnum.province.getValue()?R.mipmap.ic_sheng:R.mipmap.ic_guo).asBitmap().into(faqr);
            }
//            if (StringUtils.isEmpty(contact.is_super) || "0".equals(contact.is_super)) {
//                faqr.setVisibility(View.INVISIBLE);
//            } else if ("1".equals(contact.is_super)) {
//                faqr.setVisibility(View.VISIBLE);
//                faqr.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_faqr_nationwide));
//            } else if ("2".equals(contact.is_super)) {
//                faqr.setVisibility(View.VISIBLE);
//                faqr.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.faqr));
//            }
            return view;
        }

        private class ViewHolder {
            TextView name;
            TextView age;
            TextView letter;
            ImageView photo;
            ImageView faqr;
            View itemt_wire;
        }


        /**
         * 根据ListView的当前位置获取分类的首字母的char ascii值
         */
        public int getSectionForPosition(int position) {
            return list.get(position).getSortLetters().charAt(0);
        }

        /**
         * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
         */
        public int getPositionForSection(int section) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = list.get(i).getSortLetters();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }

            return -1;
        }


        @Override
        public Object[] getSections() {
            return null;
        }
    }

    private class PinyinComparator implements Comparator<Contact> {

        public int compare(Contact o1, Contact o2) {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            if (o2.getSurname().equals("#")) {
                return -1;
            } else if (o1.getSurname().equals("#")) {
                return 1;
            } else {
                return o1.getSurname().compareTo(o2.getSurname());
            }
        }
    }
}
