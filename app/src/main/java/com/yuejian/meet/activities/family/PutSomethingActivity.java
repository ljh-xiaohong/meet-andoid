package com.yuejian.meet.activities.family;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnClick;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.utils.PreferencesIm;
import com.netease.nim.uikit.app.Adapter.LineGridView;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.Gift;
import com.yuejian.meet.bean.MyWallet;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.GiftDetailAdapter;
import com.yuejian.meet.widgets.GiftPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PutSomethingActivity extends BaseActivity {
    private List<GridView> mGiftViews;
    private int thingType = -1;
    private String thingValue;
    private ViewPager viewPager = null;

    private void createDot(int paramInt) {
        LinearLayout localLinearLayout = (LinearLayout) findViewById(R.id.dot);
        int size = Utils.dp2px(this, 10.0F);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(size, size);
        localLayoutParams.gravity = 17;
        int margin = Utils.dp2px(this, 8.0F);
        localLayoutParams.setMargins(margin, margin, margin, margin);
        localLinearLayout.removeAllViews();
        for (int i = 0; i < paramInt; i++) {
            View dots = new View(this);
            dots.setBackground(getResources().getDrawable(R.drawable.selector_dot_gift));
            dots.setSelected(i == 0);
            localLinearLayout.addView(dots, localLayoutParams);
        }
    }

    private GridView createGiftGridView(final List<Gift> paramList, int paramInt1, int paramInt2, int paramInt3) {
        final LineGridView gridView = new LineGridView(this.mContext);
        gridView.setSelector(R.drawable.selector_select_gift_bg);
        gridView.setNumColumns(4);
        gridView.setLayoutParams(new ViewGroup.LayoutParams(paramInt1, paramInt2));
        gridView.setAdapter(new GiftDetailAdapter(gridView, this.mContext, paramList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int position, long paramAnonymousLong) {
                for (Gift g : paramList) {
                    g.setSelected(false);
                }
                Gift gift = paramList.get(position);
                gift.isSelected = !gift.isSelected;
                BaseAdapter adapter = (BaseAdapter) gridView.getAdapter();
                adapter.notifyDataSetChanged();
            }
        });
        return gridView;
    }

    private void initGiftContainer() {
        initData();
    }

    private void initPager(int paramInt1, int paramInt2, List<Gift> paramList) {
        ArrayList localArrayList = new ArrayList();
        int i = 0;
        Iterator localIterator = paramList.iterator();
        paramList = localArrayList;
        while (localIterator.hasNext()) {
            paramList.add((Gift) localIterator.next());
            if (paramList.size() == 8) {
                GridView giftGridView = createGiftGridView(paramList, paramInt1, paramInt2, i);
                this.mGiftViews.add(giftGridView);
                paramList = new ArrayList();
                i += 1;
            }
        }
        if (paramList.size() > 0) {
            this.mGiftViews.add(createGiftGridView(paramList, paramInt1, paramInt2, i));
        }
    }

    private void putInBasket(int type, String value) {
        if (StringUtils.isEmpty(AppConfig.CustomerId)) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        if (StringUtils.isEmpty(value)) {
            Toast.makeText(this, "请选择要放进的东西", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap localHashMap = new HashMap();
        localHashMap.put("type", String.valueOf(type));
        localHashMap.put("value", value);
        localHashMap.put("customer_id", AppConfig.CustomerId);
        this.apiImp.putInBasket(localHashMap, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                Toast.makeText(PutSomethingActivity.this.mContext, "成功投放", Toast.LENGTH_SHORT).show();
                PutSomethingActivity.this.finish();
            }
        });
    }

    public void initData() {
        this.viewPager = ((ViewPager) findViewById(R.id.gift_vpager_container));
        int i = (int) AppConfig.width;
        int j = Utils.dp2px(this, 2.0F);
        j = i / 5 * 2 + Utils.dp2px(this, 33.0F) + j * 2;
        Object localObject1 = JSON.parseArray(PreferencesIm.get(this.mContext, "gift_image", "[]"), Gift.class);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i, j);
        this.viewPager.setLayoutParams(layoutParams);
        this.mGiftViews = new ArrayList();
        initPager(i, j, (List) localObject1);
        GiftPagerAdapter giftPagerAdapter = new GiftPagerAdapter(this.mGiftViews);
        this.viewPager.setAdapter(giftPagerAdapter);
        createDot(i);
        this.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int paramAnonymousInt) {
                super.onPageSelected(paramAnonymousInt);
                ViewGroup localViewGroup = (ViewGroup) PutSomethingActivity.this.findViewById(R.id.dot);
                int i = 0;
                if (i < localViewGroup.getChildCount()) {
                    if (i == paramAnonymousInt) {
                        localViewGroup.getChildAt(paramAnonymousInt).setSelected(true);
                    } else {
                        localViewGroup.getChildAt(i).setSelected(false);
                    }
                }
            }
        });
        HashMap<String, Object> params = new HashMap();
        params.put("customer_id", AppConfig.CustomerId);
        this.apiImp.getMyWallet(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                MyWallet myWallet = JSON.parseObject(paramAnonymousString, MyWallet.class);
                if (myWallet != null) {
                    ((TextView) PutSomethingActivity.this.findViewById(R.id.total_coin)).setText(myWallet.total_bal);
                }
            }
        });
    }

    @OnClick({R.id.put_thing_btn})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.put_thing_btn:
                if (this.thingType == 1) {
                    this.thingValue = ((EditText) findViewById(R.id.xinlihua_ed)).getText().toString();
                    if (this.thingValue.length() > 499) {
                        Toast.makeText(this.mContext, "超出500字无法放入心里话喔", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (this.thingType == 2) {
                        this.thingValue = ((EditText) findViewById(R.id.coin_ed)).getText().toString();
                    }
                }
                putInBasket(this.thingType, this.thingValue);
                break;
        }
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_put_something);
        initBackButton(true);
        setTitleText(getIntent().getStringExtra("title"));
        this.thingType = getIntent().getIntExtra("type", -1);
        switch (this.thingType) {
            case 1:
                findViewById(R.id.xlh).setVisibility(View.VISIBLE);
                findViewById(R.id.root).setBackgroundResource(R.mipmap.xinzhi_bg);
                break;
            case 2:
                findViewById(R.id.coin_layout).setVisibility(View.VISIBLE);
                break;
            case 3:
                findViewById(R.id.gift_container).setVisibility(View.VISIBLE);
                break;
        }
        initGiftContainer();
    }
}
