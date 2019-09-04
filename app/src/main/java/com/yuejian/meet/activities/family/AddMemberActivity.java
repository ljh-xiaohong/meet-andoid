package com.yuejian.meet.activities.family;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnClick;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddMemberActivity
        extends BaseActivity {
    private PopupWindow addAppellationWindow = null;
    private String appellation;
    private ListView appellationListView = null;
    private ArrayList<String> appellations = new ArrayList();
    private ArrayAdapter<String> arrayAdapter = null;
    private List<ImageView> imageViews = new ArrayList();
    private int[] imgs = {R.mipmap.boy_b, R.mipmap.boy_b1, R.mipmap.boy_b2, R.mipmap.boy_b3, R.mipmap.boy_b4,
            R.mipmap.girl_b, R.mipmap.girl_b1, R.mipmap.girl_b2, R.mipmap.girl_b3, R.mipmap.girl_b4};
    private String[] imgsNames = {"boy_b", "boy_b1", "boy_b2", "boy_b3", "boy_b4", "girl_b", "girl_b1", "girl_b2", "girl_b3", "girl_b4"};
    private ViewPager viewPager;

    private void addRelatives() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        params.put("relations", this.appellation);
        params.put("photo", "https://yuejian-app.oss-cn-shenzhen.aliyuncs.com/jiaren/" + this.imgsNames[this.viewPager.getCurrentItem()] + ".png");
        this.apiImp.addRelatives(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                finish();
                Bus.getDefault().post("add_relative_finish");
            }
        });
    }

    private void initViewPager() {
        ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
        int[] arrayOfInt = this.imgs;
        int j = arrayOfInt.length;
        int i = 0;
        while (i < j) {
            int k = arrayOfInt[i];
            ImageView localImageView = new ImageView(this);
            localImageView.setLayoutParams(localLayoutParams);
            localImageView.setImageResource(k);
            this.imageViews.add(localImageView);
            i += 1;
        }
        this.viewPager = ((ViewPager) findViewById(R.id.vp));
        this.viewPager.setAdapter(new PagerAdapter() {
            public void destroyItem(ViewGroup paramAnonymousViewGroup, int paramAnonymousInt, Object paramAnonymousObject) {
                paramAnonymousViewGroup.removeView(imageViews.get(paramAnonymousInt));
            }

            public int getCount() {
                return imageViews.size();
            }

            public Object instantiateItem(ViewGroup paramAnonymousViewGroup, int paramAnonymousInt) {
                paramAnonymousViewGroup.addView((View) imageViews.get(paramAnonymousInt), 0);
                return imageViews.get(paramAnonymousInt);
            }

            public boolean isViewFromObject(View paramAnonymousView, Object paramAnonymousObject) {
                return paramAnonymousView == paramAnonymousObject;
            }
        });
        this.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int paramAnonymousInt) {
                super.onPageSelected(paramAnonymousInt);
                if (paramAnonymousInt < 5) {
                    appellations.clear();
                    appellations.add("爸爸");
                    appellations.add("爷爷");
                    appellations.add("哥哥");
                    appellations.add("弟弟");
                    appellations.add("儿子");
                    appellations.add("孙子");
                    appellations.add("配偶");
                    return;
                }
                appellations.clear();
                appellations.add("妈妈");
                appellations.add("奶奶");
                appellations.add("姐姐");
                appellations.add("妹妹");
                appellations.add("女儿");
                appellations.add("孙女");
                appellations.add("配偶");
            }
        });
        appellations.clear();
        appellations.add("爸爸");
        appellations.add("爷爷");
        appellations.add("哥哥");
        appellations.add("弟弟");
        appellations.add("儿子");
        appellations.add("孙子");
        appellations.add("配偶");
    }

    private void showAddAppellationWindow() {
        if (this.addAppellationWindow == null) {
            this.addAppellationWindow = new PopupWindow(this);
            View localView = View.inflate(this, R.layout.dialog_appellation, null);
            this.appellationListView = ((ListView) localView.findViewById(R.id.list));
            this.appellationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                    appellation = appellations.get(paramAnonymousInt);
                    ((TextView) findViewById(R.id.add_member)).setText(appellation);
                    addAppellationWindow.dismiss();
                }
            });
            this.addAppellationWindow.setContentView(localView);
            this.addAppellationWindow.setHeight(-2);
            this.addAppellationWindow.setWidth(-2);
            this.addAppellationWindow.setBackgroundDrawable(new ColorDrawable());
            this.addAppellationWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    ScreenUtils.setScreenAlpha(AddMemberActivity.this, 1.0F);
                }
            });
        }
        if (!this.addAppellationWindow.isShowing()) {
            this.arrayAdapter = new ArrayAdapter(this, R.layout.item_appellation, appellations.toArray(new String[this.appellations.size()]));
            this.appellationListView.setAdapter(this.arrayAdapter);
            this.arrayAdapter.notifyDataSetChanged();
            ScreenUtils.setScreenAlpha(this, 0.4F);
            this.addAppellationWindow.showAtLocation(findViewById(R.id.content), 17, 0, Utils.dp2px(this, 10.0F));
        }
    }

    public void onBackPressed() {
        if ((this.addAppellationWindow != null) && (this.addAppellationWindow.isShowing())) {
            this.addAppellationWindow.dismiss();
            return;
        }
        if (this.appellation != null) {
            Toast.makeText(this.mContext, "请点击确定", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

    @OnClick({R.id.txt_titlebar_send_moment, R.id.add_member})
    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.add_member:
                showAddAppellationWindow();
                break;
            case R.id.txt_titlebar_send_moment:
                if(StringUtils.isEmpty(appellation)) {
                    Toast.makeText(mContext, "请选择家人称谓", Toast.LENGTH_SHORT).show();
                    return;
                }
                int currentItem = this.viewPager.getCurrentItem();
                if (this.appellations.contains("爸爸")) {
                    if (imgsNames[currentItem].contains("boy")) {
                        addRelatives();
                    } else {
                        Toast.makeText(this.mContext, "您选择的图片与称谓不相符", Toast.LENGTH_SHORT).show();
                    }
                } else if (appellations.contains("妈妈")) {
                    if (imgsNames[currentItem].contains("girl")) {
                        addRelatives();
                    } else {
                        Toast.makeText(this.mContext, "您选择的图片与称谓不相符", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    protected void onCreate(@Nullable Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_add_family);
        setTitleText("添加家人");
        initBackButton(true);
        findViewById(R.id.txt_titlebar_send_moment).setVisibility(View.VISIBLE);
        initViewPager();
    }
}
