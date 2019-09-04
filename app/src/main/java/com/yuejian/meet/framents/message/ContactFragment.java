package com.yuejian.meet.framents.message;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.widgets.SwipeRefreshView;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zh02 on 2017/8/15.
 */

public class ContactFragment extends BaseFragment implements SpringView.OnFreshListener {
    private SpringView springView;
    private View contentView;
    private ListView list;
    private List<Contact> dataSource = new ArrayList<>();
    private ContactAdapter adapter;
    private int relationType = 0;
    private int pageIndex = 1;
    private View noDataTips = null;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        contentView = inflater.inflate(R.layout.fragment_contact, null);
        initView();
        if (user != null) {
            getRelationList(user.customer_id);
        }
        return contentView;
    }

    private void initView() {
        list = (ListView) contentView.findViewById(R.id.contact_list_view);
        adapter = new ContactAdapter();
        list.setAdapter(adapter);
        relationType = getArguments().getInt("relationType");
        springView = (SpringView) contentView.findViewById(R.id.springView);
        springView.setHeader(new DefaultHeader(getActivity()));
        springView.setFooter(new DefaultFooter(getActivity()));
        springView.setListener(this);
        noDataTips = contentView.findViewById(R.id.no_data_tips_layout);
    }

    private void getRelationList(String customerId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("relation_type", String.valueOf(relationType));
        params.put("pageIndex", String.valueOf(pageIndex));
        params.put("pageItemCount", String.valueOf(500));
        apiImp.getRelationList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (pageIndex == 1) {
                    dataSource.clear();
                }
                List<Contact> contactEntities = JSON.parseArray(data, Contact.class);
                if (contactEntities != null && contactEntities.size() > 0) {
                    if (!dataSource.containsAll(contactEntities)) {
                        dataSource.addAll(contactEntities);
                    }
                } else {
                    pageIndex--;
                    pageIndex = pageIndex <= 0 ? 1 : pageIndex;
                }
                adapter.notifyDataSetChanged();
                if (springView != null) {
                    springView.onFinishFreshAndLoad();
                }

                if (dataSource.isEmpty()) {
                    noDataTips.setVisibility(View.VISIBLE);
                    springView.setVisibility(View.GONE);
                } else {
                    noDataTips.setVisibility(View.GONE);
                    springView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @Override
    public void onRefresh() {
        if (user != null) {
            pageIndex = 1;
            getRelationList(user.customer_id);
        }
    }

    @Override
    public void onLoadmore() {
        if (user != null) {
            pageIndex++;
            getRelationList(user.customer_id);
        }
    }

    private class ContactAdapter extends BaseAdapter {

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
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getContext(), R.layout.item_contact, null);
                holder.age = (TextView) convertView.findViewById(R.id.contact_age);
                holder.name = (TextView) convertView.findViewById(R.id.contact_name);
                holder.photo = (ImageView) convertView.findViewById(R.id.contact_photo);
                holder.contactBtn = (Button) convertView.findViewById(R.id.contact_btn);
                holder.contact_parent_view = convertView.findViewById(R.id.contact_parent_view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Contact contact = dataSource.get(position);
            Glide.with(getContext()).load(contact.photo).asBitmap().error(new ColorDrawable(Color.parseColor("#e8e8e8"))).into(holder.photo);
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

            ImageView fqr = (ImageView) convertView.findViewById(R.id.faqr);
            fqr.setVisibility(View.GONE);
            if (contact.getIs_super() > 0) {
                fqr.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(contact.is_super == FqrEnum.city.getValue() ? R.mipmap.ic_shi : contact.is_super == FqrEnum.province.getValue() ? R.mipmap.ic_sheng : R.mipmap.ic_guo).asBitmap().into(fqr);
            }

//            if (StringUtils.isEmpty(contact.is_super) || "0".equals(contact.is_super)) {
//                fqr.setVisibility(View.INVISIBLE);
//            } else if ("1".equals(contact.is_super)) {
//                fqr.setVisibility(View.VISIBLE);
//                fqr.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_faqr_nationwide));
//            } else if ("2".equals(contact.is_super)) {
//                fqr.setVisibility(View.VISIBLE);
//                fqr.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.faqr));
//            }

            String btnText = getString(R.string.add_attention);
            holder.contactBtn.setSelected(false);
            if (relationType == Constants.GET_FOCUS) {
                btnText = getString(R.string.attention2);
                holder.contactBtn.setSelected(true);
            } else if (relationType == Constants.GET_BLACK_LIST || relationType == Constants.GET_FRIENDS) {
                holder.contactBtn.setVisibility(View.GONE);
            }
            holder.contactBtn.setText(btnText);
            holder.contactBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    String text = button.getText().toString();
                    if (relationType == Constants.GET_FANS) {
                        if (getString(R.string.add_attention).equals(text)) {
                            if (user != null && StringUtils.isNotEmpty(user.customer_id)) {
                                focusCustomer(user.customer_id, contact.customer_id, Constants.ADD_FOCUS, holder.contactBtn);
                            }
                        } else if (getString(R.string.attention2).equals(text)) {
                            if (user != null && StringUtils.isNotEmpty(user.customer_id)) {
                                focusCustomer(user.customer_id, contact.customer_id, Constants.CANCEL_FOCUS, holder.contactBtn);
                            }
                        }
                    } else if (relationType == Constants.GET_FOCUS) {
                        if (getString(R.string.attention2).equals(text)) {
                            if (user != null && StringUtils.isNotEmpty(user.customer_id)) {
                                focusCustomer(user.customer_id, contact.customer_id, Constants.CANCEL_FOCUS, holder.contactBtn);
                            }
                        } else if (getString(R.string.add_attention).equals(text)) {
                            if (user != null && StringUtils.isNotEmpty(user.customer_id)) {
                                focusCustomer(user.customer_id, contact.customer_id, Constants.ADD_FOCUS, holder.contactBtn);
                            }
                        }
                    }
                }
            });

            holder.contact_parent_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUitls.goToPersonHome(mContext, contact.customer_id);
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView name;
            TextView age;
            ImageView photo;
            Button contactBtn;
            View contact_parent_view;
        }
    }

    private void focusCustomer(String customerId, String opCustomerId, int bindType, final Button button) {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        params.put("op_customer_id", opCustomerId);
        params.put("bind_type", String.valueOf(bindType));
        apiImp.bindRelation(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                String text = button.getText().toString();
                if (relationType == Constants.GET_FOCUS) {
                    if (getString(R.string.attention2).equals(text)) {
                        button.setText(getString(R.string.add_attention));
                        button.setSelected(false);
                    } else if (getString(R.string.add_attention).equals(text)) {
                        button.setText(getString(R.string.attention2));
                        button.setSelected(true);
                    }
                } else if (relationType == Constants.GET_FANS) {
                    if (getString(R.string.add_attention).equals(text)) {
                        button.setText(getString(R.string.attention2));
                        button.setSelected(true);
                    } else if (getString(R.string.attention2).equals(text)) {
                        button.setText(getString(R.string.add_attention));
                        button.setSelected(false);
                    }
                }
            }


            @Override
            public void onFailed(String errCode, String errMsg, int id) {
            }
        });
    }

    @Override
    public void onUserVisible() {
        super.onUserVisible();
        if (user != null) {
            pageIndex = 1;
            getRelationList(user.customer_id);
        }
    }
}
