package com.yuejian.meet.widgets;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.Prize;
import com.yuejian.meet.utils.CustomRotateAnim;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.HashMap;

public class ShakeGiftPopWindow {
    private Activity activity = null;
    private ApiImp apiImp = new ApiImp();
    private View contentView = null;
    private int full_gift_direction = 0;
    private boolean isPlaying = false;
    private Prize prize = null;
    private PopupWindow window = null;

    public ShakeGiftPopWindow(Activity paramActivity) {
        this.activity = paramActivity;
        this.full_gift_direction = (ScreenUtils.getScreenHeight(paramActivity) + Utils.dp2px(paramActivity, 200.0F));
        initialWindow();
    }

    private View initialContentView() {
        return View.inflate(this.activity, R.layout.pop_up_yaoyiyao, null);
    }

    private void initialWindow() {
        if (this.window == null) {
            this.window = new PopupWindow();
            this.window.setWidth(ScreenUtils.getScreenWidth(this.activity));
            this.window.setHeight(ScreenUtils.getScreenHeight(this.activity));
        }
    }

    private void shakeIt() {
        if (StringUtils.isEmpty(AppConfig.CustomerId)) {
            this.activity.startActivity(new Intent(this.activity, LoginActivity.class));
            dismiss();
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", AppConfig.CustomerId);
        this.apiImp.luckDraw(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                prize = JSON.parseObject(data, Prize.class);
                if (prize != null) {
                    if (ShakeGiftPopWindow.this.prize.type == 1) {
                        showXinLiHuaAnimation();
                    } else if (prize.type == 2) {
                        showHongbaoAnimation();
                    } else if (prize.type == 3) {
                        showGiftAnimation();
                    } else {
                        showNothing();
                    }
                }
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                showNothing();
            }
        });
    }

    private void showGiftAnimation() {
        this.isPlaying = true;
        FrameLayout component = (FrameLayout) this.contentView.findViewById(R.id.component);
        component.removeAllViews();
        FrameLayout giftView = (FrameLayout) View.inflate(this.activity, R.layout.gift, null);
        FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        localLayoutParams.gravity = 17;
        component.addView(giftView, localLayoutParams);
        Glide.with(this.activity).load(this.prize.value).into((ImageView) giftView.findViewById(R.id.gift_icon));
        final TextView giftName = (TextView) giftView.findViewById(R.id.gift_name);
        giftName.setAlpha(0.0F);
        giftName.setText(new StringBuffer(this.prize.value_name + " x 1"));
        final FrameLayout gift = (FrameLayout) giftView.findViewById(R.id.gift);
        gift.animate().alpha(1.0F).setDuration(1000L).withEndAction(new Runnable() {
            public void run() {
                ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(gift, "y", 80.0F);
                localObjectAnimator.setRepeatCount(2);
                localObjectAnimator.setDuration(500L);
                localObjectAnimator.addListener(new Animator.AnimatorListener() {
                    public void onAnimationCancel(Animator paramAnonymous2Animator) {
                    }

                    public void onAnimationEnd(Animator paramAnonymous2Animator) {
                        ImageView gaiZi = (ImageView) gift.findViewById(R.id.gaizi);
                        gaiZi.setPivotX(0.0F);
                        gaiZi.setPivotY(1800.0F);
                        gaiZi.animate().y(-1000.0F).rotation(-120.0F).withStartAction(new Runnable() {
                            public void run() {
                                gift.findViewById(R.id.gift_icon).animate().scaleX(1.2F).scaleY(1.2F).alpha(0.0F).setDuration(1000L).withStartAction(new Runnable() {
                                    public void run() {
                                        ImageView shanGuang = (ImageView) gift.findViewById(R.id.shanguang);
                                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(shanGuang, "rotation", 360.0F);
                                        animator1.setRepeatCount(-1);
                                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(shanGuang, "alpha", 1.0F);
                                        ImageView giftIcon = (ImageView) gift.findViewById(R.id.gift_icon);
                                        ObjectAnimator animator3 = ObjectAnimator.ofFloat(giftIcon, "alpha", 1.0F);
                                        ObjectAnimator animator4 = ObjectAnimator.ofFloat(giftIcon, "scaleX", 1.0F);
                                        ObjectAnimator animator5 = ObjectAnimator.ofFloat(giftIcon, "scaleY", 1.0F);
                                        animator3.addListener(new Animator.AnimatorListener() {
                                            public void onAnimationCancel(Animator paramAnonymous5Animator) {
                                            }

                                            public void onAnimationEnd(Animator paramAnonymous5Animator) {
                                                ShakeGiftPopWindow.this.showOwner();
                                                giftName.animate().alpha(1.0F).setDuration(800L).start();
                                            }

                                            public void onAnimationRepeat(Animator paramAnonymous5Animator) {
                                            }

                                            public void onAnimationStart(Animator paramAnonymous5Animator) {
                                            }
                                        });
                                        AnimatorSet localAnimatorSet = new AnimatorSet();
                                        localAnimatorSet.setInterpolator(new LinearInterpolator());
                                        localAnimatorSet.setDuration(1000L);
                                        localAnimatorSet.playTogether(animator1, animator2, animator3, animator4, animator5);
                                        localAnimatorSet.start();
                                    }
                                }).start();
                            }
                        }).setDuration(3000L).start();
                    }

                    public void onAnimationRepeat(Animator paramAnonymous2Animator) {
                    }

                    public void onAnimationStart(Animator paramAnonymous2Animator) {
                    }
                });
                localObjectAnimator.reverse();
            }
        }).start();
    }

    private void showHongbaoAnimation() {
        this.isPlaying = true;
        FrameLayout component = (FrameLayout) this.contentView.findViewById(R.id.component);
        component.removeAllViews();
        FrameLayout hongbaoView = (FrameLayout) View.inflate(this.activity, R.layout.hongbao, null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        layoutParams.gravity = 17;
        component.addView(hongbaoView, layoutParams);
        ((TextView) hongbaoView.findViewById(R.id.coin)).setText(new StringBuffer(this.prize.value + " 金币"));
        final FrameLayout hongbao = (FrameLayout) hongbaoView.findViewById(R.id.hongbao);
        hongbao.animate().alpha(1.0F).setDuration(1000L).withEndAction(new Runnable() {
            public void run() {
                CustomRotateAnim localCustomRotateAnim = CustomRotateAnim.getCustomRotateAnim();
                localCustomRotateAnim.setDuration(1000L);
                localCustomRotateAnim.setRepeatCount(1);
                localCustomRotateAnim.setInterpolator(new LinearInterpolator());
                hongbao.startAnimation(localCustomRotateAnim);
                localCustomRotateAnim.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationEnd(Animation paramAnonymous2Animation) {
                        hongbao.findViewById(R.id.jiangjin_view).animate().y(0.0F).setDuration(600L).withEndAction(new Runnable() {
                            public void run() {
                                showOwner();
                            }
                        }).start();
                    }

                    public void onAnimationRepeat(Animation paramAnonymous2Animation) {
                    }

                    public void onAnimationStart(Animation paramAnonymous2Animation) {
                    }
                });
            }
        }).start();
    }

    private void showNothing() {
        FrameLayout localFrameLayout = (FrameLayout) this.contentView.findViewById(R.id.component);
        localFrameLayout.removeAllViews();
        ImageView localImageView = new ImageView(this.activity);
        localImageView.setImageResource(R.mipmap.shemmedoumeiyaodao);
        FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, -2);
        localLayoutParams.gravity = 17;
        localFrameLayout.addView(localImageView, localLayoutParams);
    }

    private void showOwner() {
        if (this.prize == null) {
            return;
        }
        FrameLayout component = (FrameLayout) this.contentView.findViewById(R.id.component);
        component.removeAllViews();
        View owner = View.inflate(this.activity, R.layout.owner, null);
        ImageView ownerIcon = (ImageView) owner.findViewById(R.id.owner_icon);
        Glide.with(this.activity).load(this.prize.photo).into(ownerIcon);
        ((TextView) owner.findViewById(R.id.owner_icon)).setText(new StringBuffer(this.prize.surname + this.prize.name));
        TextView age = (TextView) owner.findViewById(R.id.age);
        Utils.setAgeAndSexView(this.activity, age, this.prize.sex, this.prize.age);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        component.addView(owner, layoutParams);
        owner.findViewById(R.id.ownerview).animate().alpha(1.0F).yBy(70.0F).setDuration(1000L).withEndAction(new Runnable() {
            public void run() {
//                ShakeGiftPopWindow.access$002(ShakeGiftPopWindow.this, false);
                isPlaying = false;
            }
        }).start();
        owner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ImUtils.toP2PCaht(ShakeGiftPopWindow.this.activity, ShakeGiftPopWindow.this.prize.customer_id);
            }
        });
    }

    private void showXinLiHuaAnimation() {
        this.isPlaying = true;
        FrameLayout component = (FrameLayout) this.contentView.findViewById(R.id.component);
        component.removeAllViews();
        final View xinlihua = View.inflate(this.activity, R.layout.xinlihua, null);
        TextView text = (TextView) xinlihua.findViewById(R.id.xinlihuatext);
        text.setText(this.prize.value);
        xinlihua.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                IMMessage textMessage = MessageBuilder.createTextMessage(ShakeGiftPopWindow.this.prize.customer_id, SessionTypeEnum.P2P, "\n" + ShakeGiftPopWindow.this.prize.value);
                NIMClient.getService(MsgService.class).sendMessage(textMessage, false);
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        component.addView(xinlihua, layoutParams);
        xinlihua.findViewById(R.id.xinlihuatext).animate().alpha(1.0F).setDuration(1000L).withEndAction(new Runnable() {
            public void run() {
                xinlihua.findViewById(R.id.xinlihuatext).animate().yBy(-200.0F).setDuration(800L).withEndAction(new Runnable() {
                    public void run() {
                        ShakeGiftPopWindow.this.showOwner();
                    }
                }).start();
            }
        }).start();
    }

    public void dismiss() {
        this.window.dismiss();
        this.contentView.findViewById(R.id.full_gift).animate().translationY(-this.full_gift_direction).setDuration(100L).start();
    }

    public boolean isShowing() {
        return this.window.isShowing();
    }

    public void show() {
        this.contentView = initialContentView();
        this.contentView.findViewById(R.id.yaoyiyao).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (ShakeGiftPopWindow.this.isPlaying) {
                    return;
                }
                ((FrameLayout) ShakeGiftPopWindow.this.contentView.findViewById(R.id.component)).removeAllViews();
                ShakeGiftPopWindow.this.shakeIt();
            }
        });
        this.contentView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                ShakeGiftPopWindow.this.dismiss();
            }
        });
        this.contentView.findViewById(R.id.put_thing_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Bus.getDefault().post("put_something");
            }
        });
        this.window.setContentView(this.contentView);
        this.window.showAtLocation(this.activity.getWindow().getDecorView(), 81, 0, 0);
        if (this.contentView != null) {
            this.isPlaying = true;
            this.contentView.findViewById(R.id.full_gift).animate().translationY(this.full_gift_direction).setDuration(3000L).withEndAction(new Runnable() {
                public void run() {
                    ShakeGiftPopWindow.this.shakeIt();
                }
            }).start();
        }
    }
}
