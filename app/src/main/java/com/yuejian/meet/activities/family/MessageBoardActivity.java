package com.yuejian.meet.activities.family;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.MessageBoard;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.springview.DefaultFooter;
import com.yuejian.meet.widgets.springview.DefaultHeader;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.umeng.socialize.a.b.d.i;

/**
 * 留言板
 */
public class MessageBoardActivity extends BaseActivity implements SpringView.OnFreshListener {
    private MessageBoardAdapter adapter;
    private List<MessageBoard> dataSource = new ArrayList();
    private EditText editText = null;
    private String id;
    private int pageIndex = 1;
    private SpringView springView;

    private void focusCustomer(String paramString1, String paramString2, final int paramInt, final TextView paramTextView) {
        HashMap localHashMap = new HashMap();
        localHashMap.put("customer_id", paramString1);
        localHashMap.put("op_customer_id", paramString2);
        localHashMap.put("bind_type", String.valueOf(paramInt));
        this.apiImp.bindRelation(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                if (paramInt == 1) {
                    paramTextView.setSelected(true);
                    paramTextView.setText(R.string.attention2);
                } else if (paramInt == 2) {
                    paramTextView.setSelected(false);
                    paramTextView.setText(R.string.add_attention);
                }

            }
        });
    }

    private void getMessageForBoard() {
        HashMap localHashMap = new HashMap();
        localHashMap.put("id", this.id);
        localHashMap.put("pageIndex", String.valueOf(this.pageIndex));
        this.apiImp.getMessageForBoard(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                List<MessageBoard> messageBoards = JSON.parseArray(paramAnonymousString, MessageBoard.class);
                if (pageIndex == 1) {
                    dataSource.clear();
                }
                if ((messageBoards.isEmpty()) && (pageIndex < 1)) {
                    pageIndex = 1;
                }

                dataSource.addAll(messageBoards);
                adapter.notifyDataSetChanged();
                springView.onFinishFreshAndLoad();
            }
        });
    }

    private void release() {
        if (StringUtils.isEmpty(this.editText.getText().toString())) {
            Toast.makeText(this.mContext, R.string.leave_a_message, Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> params = new HashMap();
        params.put("id", this.id);
        params.put("customer_id", AppConfig.CustomerId);
        params.put("content", this.editText.getText().toString());
        this.apiImp.addClanHallComment(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                editText.setText("");
                findViewById(R.id.edit_layout).setVisibility(View.GONE);
                Utils.hideSystemKeyBoard(getBaseContext(), findViewById(R.id.edit_layout));
                getMessageForBoard();
            }
        });
    }

    @OnClick({R.id.edit_btn, R.id.txt_titlebar_release,R.id.release})
    public void onClick(View paramView) {
        super.onClick(paramView);
        switch (paramView.getId()) {
            case R.id.edit_btn:
                findViewById(R.id.edit_layout).setVisibility(View.VISIBLE);
                Utils.showKB(findViewById(R.id.edit_layout), this);
                break;
            case R.id.txt_titlebar_release:

                break;

            case R.id.release:
                release();
                findViewById(R.id.edit_layout).setVisibility(View.GONE);
                Utils.hideSystemKeyBoard(getBaseContext(), findViewById(R.id.edit_layout));
                break;
        }
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_message_board);
        setTitleText(getString(R.string.message_board));
        initBackButton(true);
        this.id = getIntent().getStringExtra("id");
        this.editText = ((EditText) findViewById(R.id.edit));
        this.springView = ((SpringView) findViewById(R.id.spring_view));
        this.springView.setHeader(new DefaultHeader(this));
        this.springView.setFooter(new DefaultFooter(this));
        this.springView.setListener(this);
        ListView listView = (ListView) findViewById(R.id.list);
        this.adapter = new MessageBoardAdapter();
        listView.setAdapter(this.adapter);
        getMessageForBoard();
    }

    public void onLoadmore() {
        this.pageIndex += 1;
        getMessageForBoard();
    }

    public void onRefresh() {
        this.pageIndex = 1;
        getMessageForBoard();
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if (findViewById(R.id.edit_layout).getVisibility() == View.VISIBLE) {
            findViewById(R.id.edit_layout).setVisibility(View.GONE);
            Utils.hideSystemKeyBoard(this, findViewById(R.id.edit_layout));
        }
        return super.onTouchEvent(paramMotionEvent);
    }

    private class MessageBoardAdapter extends BaseAdapter {
        private MessageBoardAdapter() {
        }

        public int getCount() {
            return dataSource.size();
        }

        public Object getItem(int paramInt) {
            return dataSource.get(paramInt);
        }

        public long getItemId(int paramInt) {
            return paramInt;
        }

        public View getView(int paramInt, View paramView, final ViewGroup paramViewGroup) {
            ViewHolder holder = null;
            if (paramView == null) {
                holder = new ViewHolder();
                paramView = View.inflate(getBaseContext(), R.layout.item_message_board, null);
                holder.age = ((TextView) paramView.findViewById(R.id.age));
                holder.time = ((TextView) paramView.findViewById(R.id.time));
                holder.city = ((TextView) paramView.findViewById(R.id.city));
                holder.content = ((TextView) paramView.findViewById(R.id.content));
                holder.name = ((TextView) paramView.findViewById(R.id.name));
                holder.photo = ((ImageView) paramView.findViewById(R.id.photo));
                holder.focusBtn = ((TextView) paramView.findViewById(R.id.focus));
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
                final MessageBoard messageBoard = dataSource.get(paramInt);
                Glide.with(getBaseContext()).load(messageBoard.photo).into(holder.photo);
                holder.name.setText(String.valueOf(messageBoard.surname + messageBoard.name));
                holder.city.setText(messageBoard.family_area);
                holder.content.setText(messageBoard.content);
                String time = StringUtils.friendlyTime(messageBoard.create_time);
                holder.time.setText(time);
                Utils.setAgeAndSexView(getBaseContext(), holder.age, messageBoard.sex, messageBoard.age);
                if (messageBoard.customer_id.equals(AppConfig.CustomerId)) {
                    holder.focusBtn.setVisibility(View.GONE);
                }
                final ViewHolder finalHolder = holder;
                holder.focusBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        if (finalHolder.focusBtn.isSelected()) {
                            focusCustomer(AppConfig.CustomerId, messageBoard.customer_id, i, finalHolder.focusBtn);
                        }
                    }
                });

                if ("1".equals(messageBoard.relation_type)) {
                    holder.focusBtn.setSelected(false);
                    holder.focusBtn.setText(R.string.attention2);
                } else {
                    holder.focusBtn.setSelected(true);
                    holder.focusBtn.setText(R.string.add_attention);
                }


                holder.photo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        Intent intent = new Intent(getBaseContext(), WebActivity.class);
                        intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + messageBoard.customer_id);
                        startActivity(intent);
                    }
                });

            }
            return paramView;
        }

        class ViewHolder {
            TextView age;
            TextView city;
            TextView content;
            TextView focusBtn;
            TextView name;
            ImageView photo;
            TextView time;
        }
    }
}
