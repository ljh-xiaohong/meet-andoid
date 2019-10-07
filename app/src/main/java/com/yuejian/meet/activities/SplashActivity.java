package com.yuejian.meet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.meituan.android.walle.WalleChannelReader;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.PreferencesUtil;

/**
 * Created by zh03 on 2017/7/19.
 */

public class SplashActivity extends Activity {

    private int[] imgs = new int[]{R.mipmap.onboarding1, R.mipmap.onboarding2, R.mipmap.onboarding3, R.mipmap.onboarding4};
    private String[] titles = new String[]{"家族圈", "商业圈", "创作", "社交圈"};
    private String[] txts = new String[]{"凝聚全球家人，建立传承血脉家圈", "聚焦宗族优质商业资源，打造诚信商业联盟", "海量模板，快速编辑\n实用简单，分享美好", "精准人脉社交圈，家族动态资讯随时掌握"};
    private int[] dots = new int[]{R.id.yuandian_0, R.id.yuandian_1, R.id.yuandian_2, R.id.yuandian_3};
    private ViewPager viewPager = null;
    private ImageView[] mImageViews;
    private TextView mTitleView;
    private TextView mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        viewPager = (ViewPager) findViewById(R.id.viewpaper);
        mTitleView = (TextView) findViewById(R.id.tv_splash_title);
        mContentView = (TextView) findViewById(R.id.tv_splash_content);
        viewPager.setOverScrollMode(ViewPager.OVER_SCROLL_NEVER);
        mTitleView.setText(titles[0]);
        mContentView.setText(txts[0]);
        mImageViews = new ImageView[imgs.length];
        for (int r = 0; r < imgs.length; r++) {
            ImageView imageView = new ImageView(getBaseContext());
            mImageViews[r] = imageView;
            imageView.setImageResource(imgs[r]);
        }
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                try {
                    container.addView(mImageViews[position % mImageViews.length], 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mImageViews[position % mImageViews.length];
            }

            @Override
            public int getCount() {
                return imgs.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViews[position % mImageViews.length]);
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.length; i++) {
                    if (i == position) {
                        mTitleView.setText(titles[i]);
                        mContentView.setText(txts[i]);
                        ImageView yuandian = (ImageView) findViewById(dots[position]);
                        yuandian.setImageResource(R.drawable.splash_circle_dot_sel);
                    } else {
                        ImageView yuandian = (ImageView) findViewById(dots[i]);
                        yuandian.setImageResource(R.drawable.splash_circle_dot_nor);
                    }
                }
                if (viewPager.getCurrentItem() == imgs.length - 1) {
                    findViewById(R.id.btn_know_more).setVisibility(View.VISIBLE);
                    findViewById(R.id.ship).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.btn_know_more).setVisibility(View.GONE);
                    findViewById(R.id.ship).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        String name = WalleChannelReader.getChannel(getApplication());
        if (name == null || name.equals("")) {
            name = "geli";
        }
        new ApiImp().postChannel(name, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    public void onKnowMore(View view) {
        boolean b = PreferencesUtil.readBoolean(getBaseContext(), Constants.HAVE_START_UP);
        if (!b) {
            PreferencesUtil.write(getBaseContext(), Constants.HAVE_START_UP, true);
            Intent intent = new Intent();
            if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                intent.setClass(getBaseContext(), LoginActivity.class);
//                intent.setClass(getBaseContext(), MainActivity.class);
//                intent.putExtra("mine_login", false);
            } else {
                ImUtils.loginIm();//登录im
                intent.setClass(getBaseContext(), MainActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }
}
