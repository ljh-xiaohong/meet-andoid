package com.yuejian.meet.activities.mine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;

import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.entity.BusCallEntity;
import com.netease.nim.uikit.app.myenum.BusEnum;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.utils.MyGifImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifDrawable;

/**
 * @author : ljh
 * @time : 2019/8/20 15:04
 * @desc :
 */
public class GifActivity extends BaseActivity {

    @Bind(R.id.fragment_container)
    MyGifImageView noAttributeIvGif;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.tv3)
    TextView tv3;
    @Bind(R.id.tv4)
    TextView tv4;
    @Bind(R.id.tv5)
    TextView tv5;
    @Bind(R.id.door_close)
    ImageView doorClose;
    @Bind(R.id.bg)
    View bg;
    @Bind(R.id.lay)
    LinearLayout lay;
    private MediaController mediaController;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        tv1.setTag(1);
        tv2.setTag(2);
        tv3.setTag(3);
        tv4.setTag(4);
        tv5.setTag(5);
        bg.setTag(6);
        lay.setTag(7);
        Combo(tv1);
    }
    public void alphaAnimation(View v){
        // 创建透明度动画，第一个参数是开始的透明度，第二个参数是要转换到的透明度
        AlphaAnimation alphaAni = new AlphaAnimation(1.0f, 0);

        //设置动画执行的时间，单位是毫秒
        alphaAni.setDuration(1000);

        // 设置动画重复次数
        // -1或者Animation.INFINITE表示无限重复，正数表示重复次数，0表示不重复只播放一次
        alphaAni.setRepeatCount(0);
        // 将缩放动画和旋转动画放到动画插值器
        AnimationSet as = new AnimationSet(false);
        as.addAnimation(alphaAni);
        v.startAnimation(as);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if ((int) v.getTag() == 7){
                    // 创建透明度动画，第一个参数是开始的透明度，第二个参数是要转换到的透明度
                    AlphaAnimation alphaAni = new AlphaAnimation(1.0f, 0);

                    //设置动画执行的时间，单位是毫秒
                    alphaAni.setDuration(500);

                    // 设置动画重复次数
                    // -1或者Animation.INFINITE表示无限重复，正数表示重复次数，0表示不重复只播放一次
                    alphaAni.setRepeatCount(0);
                    // 将缩放动画和旋转动画放到动画插值器
                    AnimationSet as = new AnimationSet(true);
                    as.addAnimation(alphaAni);
                    doorClose.startAnimation(as);
                    noAttributeIvGif.setImageResource(R.drawable.door_new);
                    as.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }
                        Intent  intent;
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mediaController = new MediaController(GifActivity.this.getApplicationContext());
                            mediaController.setMediaPlayer((GifDrawable) noAttributeIvGif.getDrawable());
                            mediaController.show();
                            doorClose.setVisibility(View.GONE);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    intent = new Intent(mContext, MainActivity.class);
                                    BusCallEntity entity = new BusCallEntity();
                                    entity.setCallType(BusEnum.LOGIN_UPDATE);
                                    Bus.getDefault().post(entity);
                                    startActivity(intent);
                                    GifActivity.this.finish();
                                }
                            }, 2000);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    /**
     * 动画平移+渐变
     *
     * @param v
     */
    public void Combo(View v) {
        /*
         * TranslateAnimation translateAni = new TranslateAnimation(fromXType,
         * fromXValue, toXType, toXValue, fromYType, fromYValue, toYType,
         * toYValue);
         */
        //参数1～2：x轴的开始位置
        //参数3～4：y轴的开始位置
        //参数5～6：x轴的结束位置
        //参数7～8：x轴的结束位置
        TranslateAnimation translateAni = new TranslateAnimation(
                //X轴初始位置
                Animation.RELATIVE_TO_SELF, 0.0f,
                //X轴移动的结束位置
                Animation.RELATIVE_TO_SELF, 0.0f,
                //y轴开始位置
                Animation.RELATIVE_TO_SELF, 1.0f,
                //y轴移动后的结束位置
                Animation.RELATIVE_TO_SELF, 0.0f);

        //设置动画执行的时间，单位是毫秒
        translateAni.setDuration(1000);

        // 设置动画重复次数
        // -1或者Animation.INFINITE表示无限重复，正数表示重复次数，0表示不重复只播放一次
        translateAni.setRepeatCount(0);

        // 创建透明度动画，第一个参数是开始的透明度，第二个参数是要转换到的透明度
        AlphaAnimation alphaAni = new AlphaAnimation(0.0f, 1);

        //设置动画执行的时间，单位是毫秒
        alphaAni.setDuration(1000);

        // 设置动画重复次数
        // -1或者Animation.INFINITE表示无限重复，正数表示重复次数，0表示不重复只播放一次
        alphaAni.setRepeatCount(0);


        // 将缩放动画和旋转动画放到动画插值器
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(alphaAni);
        as.addAnimation(translateAni);
        v.startAnimation(as);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if ((int) v.getTag() == 1) {
                    tv2.setText("颜氏大家族");
                    Combo(tv2);
                } else if ((int) v.getTag() == 2) {
                    tv3.setText("您是颜姓");
                    Combo(tv3);
                } else if ((int) v.getTag() == 3) {
                    tv4.setText("第 1567 位");
                    Combo(tv4);
                } else if ((int) v.getTag() == 4) {
                    tv5.setText("回家的宗亲");
                    Combo(tv5);
                } else if ((int) v.getTag() == 5) {
                    alphaAnimation(bg);
                    alphaAnimation(lay);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (noAttributeIvGif != null) {
            noAttributeIvGif.setImageDrawable(null);
        }
        if (mediaController != null) {
            mediaController.clearAnimation();
            mediaController.hide();
        }
    }
}
