package com.yuejian.meet;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.find.FindMemberActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.PreferencesUtil;
import com.yuejian.meet.utils.StringUtils;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/25/025.
 */

public class SplashNewActivity extends BaseActivity {

    private TextView memberIndex = null;
    private TextView welcome = null;
    private TextView title = null;
    private TextView surname = null;
    private TextView goBackHome = null;
    private int moveDistance = 0;
    private Button nextStep = null;
    private View door = null;
    private View overlay = null;
    private ImageView openDoor = null;
    private View content = null;
    private ImageView wulian = null;
    private View welcomeLayout = null;
    private Button nnextStep = null;

    private static boolean isLookedHistory = false;
    private static boolean isVisitedClaim = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_new_activity);
        moveDistance = (int) getResources().getDimension(R.dimen.dp_30);
        welcome = (TextView) findViewById(R.id.welcome);
        title = (TextView) findViewById(R.id.surname_family_title);
        surname = (TextView) findViewById(R.id.your_surname);
        goBackHome = (TextView) findViewById(R.id.gobackhome);
        nextStep = (Button) findViewById(R.id.next_step);
        openDoor = (ImageView) findViewById(R.id.open_door);
        content = findViewById(R.id.content_layout_after_open_door);
        door = findViewById(R.id.door);
        wulian = (ImageView) findViewById(R.id.wulian);
        overlay = findViewById(R.id.overlay);
        welcomeLayout = findViewById(R.id.welcome_layout);
        nnextStep = (Button) findViewById(R.id.next_next_step);
        openDoor.setImageResource(R.drawable.open_door_anim_drawable);
        nextStep.setEnabled(false);
        nextStep.setOnClickListener(this);
        nnextStep.setOnClickListener(this);

        String titleString = title.getText().toString();
        String t = titleString.replaceAll("#", AppConfig.userEntity.surname);
        title.setText(t);

        String surnameString = surname.getText().toString();
        String s = surnameString.replaceAll("#", AppConfig.userEntity.surname);
        surname.setText(s);
        if (StringUtils.isNotEmpty(AppConfig.CustomerId)) {
            getUserRank(AppConfig.CustomerId);
        }
    }


    private void initMemberIndex(String rank) {
        memberIndex = (TextView) findViewById(R.id.your_surname_family_index);
        String index = "第 " + rank + " 位";
        Spannable string = new SpannableString(index);
        string.setSpan(new ForegroundColorSpan(Color.parseColor("#b89477")), index.indexOf("第 ") + 1, index.lastIndexOf(" 位"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new RelativeSizeSpan(1.6f), index.indexOf("第 ") + 1, index.lastIndexOf(" 位"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        memberIndex.setText(string);
    }

    private void startAnimation() {
        welcome.animate().translationYBy(-moveDistance).alphaBy(1f).setDuration(700).withEndAction(new Runnable() {
            @Override
            public void run() {
                title.animate().translationYBy(-moveDistance).alphaBy(1f).setDuration(700).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        surname.animate().translationYBy(-moveDistance).alphaBy(1f).setDuration(700).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                memberIndex.animate().translationYBy(-moveDistance).alphaBy(1f).setDuration(700).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        goBackHome.animate().translationYBy(-moveDistance).alphaBy(1f).setDuration(700).withEndAction(new Runnable() {
                                            @Override
                                            public void run() {
                                                nextStep.setEnabled(true);
                                                nextStep.animate().alphaBy(0.8f).setDuration(700).start();
                                            }
                                        }).start();
                                    }
                                }).start();
                            }
                        }).start();
                    }
                }).start();
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_step:
                if (welcomeLayout.getAlpha() == 1) {
                    startOpenDoorAnimation();
                    findViewById(R.id.next_step).setEnabled(false);
                }
                break;
            case R.id.next_next_step:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                String rewordIds = PreferencesUtil.get(MyApplication.context, Constants.HAVE_SHOW_UP_GUIDE, "");
                if (!rewordIds.contains(AppConfig.CustomerId)) {
                    rewordIds += AppConfig.CustomerId + ",";
                }
                PreferencesUtil.write(MyApplication.context, Constants.HAVE_SHOW_UP_GUIDE, rewordIds);
                finish();
                break;
        }
    }

    public void onHistory(View view) {
        if (AppConfig.userEntity != null) {
            getSurNameSource(AppConfig.userEntity.surname);
        }
    }

    public void onClaim(View view) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(Constants.URL, Constants.BAI_SHAN_JS);
        intent.putExtra(Constants.NO_TITLE_BAR, true);
        startActivity(intent);
    }

    private void getSurNameSource(String surname) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("surname", surname);
        apiImp.getSurNameSource(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                try {
                    String url = new JSONObject(data).getString("url");
                    Intent intent = new Intent(SplashNewActivity.this, WebActivity.class);
                    intent.putExtra(Constants.URL, url);
                    startActivity(intent);
                    isLookedHistory = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    private boolean openedDoor = false;

    private void startOpenDoorAnimation() {
        content.setAlpha(0.0f);
        overlay.animate().alpha(0.0f).setDuration(800).withStartAction(new Runnable() {
            @Override
            public void run() {
                openedDoor = false;
            }
        }).withEndAction(new Runnable() {
            @Override
            public void run() {
                overlay.setVisibility(View.GONE);
                AnimationDrawable animationDrawable = (AnimationDrawable) openDoor.getDrawable();
                animationDrawable.setOneShot(true);
                animationDrawable.start();
                int duration = 0;
                for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                    duration += animationDrawable.getDuration(i);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openDoor.animate().alpha(0.0f).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float fraction = animation.getAnimatedFraction();
                                wulian.setAlpha(1 - fraction);
                            }
                        }).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                openDoor.setVisibility(View.GONE);
                            }
                        }).setDuration(1000).start();
                    }
                }, duration);
                content.animate().alpha(1f).setDuration(3000).withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        content.setVisibility(View.VISIBLE);
                    }
                }).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        nnextStep.animate().alphaBy(0.8f).setDuration(1000).start();
                    }
                }).start();
            }
        }).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                welcomeLayout.setAlpha(1 - fraction);
                if (fraction == 1) {
                    welcomeLayout.setVisibility(View.GONE);
                    openedDoor = true;
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void getUserRank(String customerId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", customerId);
        apiImp.getUserRank(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                initMemberIndex(data);
                startAnimation();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
