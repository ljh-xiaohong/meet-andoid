package com.yuejian.meet.widgets;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.family.BindFamilyActivity;
import com.yuejian.meet.activities.web.WebActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.Relative;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.ImUtils;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import java.util.Calendar;
import java.util.HashMap;

public class BindRelativeDialog {
    private Activity activity;
    private View contentView = null;
    private ImageView imageView;
    private PopupWindow popupWindow = null;
    private Relative relative = null;
    private WaveView waveView = null;

    public BindRelativeDialog(Activity paramActivity) {
        this.activity = paramActivity;
        initData();
    }

    private void chat() {
        ImUtils.toP2PCaht(this.activity, this.relative.customer_id);
    }

    private void delete() {
        if (this.relative == null) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("my_customer_id", AppConfig.CustomerId);
        params.put("relative_id", this.relative.id);
        new ApiImp().deleteRelative(params, this, new DataIdCallback<String>() {
            public void onFailed(String paramAnonymousString1, String paramAnonymousString2, int paramAnonymousInt) {
            }

            public void onSuccess(String paramAnonymousString, int paramAnonymousInt) {
                Bus.getDefault().post("add_relative_finish");
            }
        });
    }

    private void sayHi() {
        int i = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String message = null;
        if ((i >= 6) && (i < 11)) {
            message = "早安";
        }
        if ((i >= 11) && (i < 14)) {
            message = "午安";
        } else if ((i >= 14) && (i < 18)) {
            message = "下午好";
        } else if ((i < 18) && (i <= 24)) {
            message = "晚上好";
        } else {
            Toast.makeText(this.activity, "现在不适合打招呼哦", Toast.LENGTH_SHORT).show();
        }
        message = message + "我亲爱的家人，送你今日这份的好心情和满满的幸福!";
        NIMClient.getService(MsgService.class)
                .sendMessage(MessageBuilder.createTextMessage(this.relative.customer_id, SessionTypeEnum.P2P, message), false);
    }

    public void dismiss() {
        this.popupWindow.dismiss();
        this.waveView.stop();
    }

    protected void initData() {
        if (this.popupWindow == null) {
            this.popupWindow = new PopupWindow(this.activity);
            this.contentView = View.inflate(this.activity, R.layout.dialog_layout_bind_relative, null);
            this.imageView = ((ImageView) this.contentView.findViewById(R.id.img));
            this.waveView = ((WaveView) this.contentView.findViewById(R.id.wave));
            this.waveView.setDuration(1500L);
            this.waveView.setStyle(Paint.Style.STROKE);
            this.waveView.setSpeed(500);
            int i = Utils.dp2px(this.activity, 122.0F);
            this.waveView.setInitialRadius(i);
            this.waveView.setMaxRadius(i * 1.2F);
            this.contentView.findViewById(R.id.person_home).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    if (BindRelativeDialog.this.relative != null) {
                        Intent intent = new Intent(BindRelativeDialog.this.activity, WebActivity.class);
                        intent.putExtra("url", Constants.PERSON_HOME + "&customer_id=" + BindRelativeDialog.this.relative.customer_id);
                        BindRelativeDialog.this.activity.startActivity(intent);
                    }
                }
            });
            this.contentView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    BindRelativeDialog.this.delete();
                    BindRelativeDialog.this.dismiss();
                }
            });
            this.contentView.findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    BindRelativeDialog.this.chat();
                }
            });
            this.contentView.findViewById(R.id.sayhi).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    BindRelativeDialog.this.sayHi();
                }
            });
            this.contentView.findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    BindRelativeDialog.this.contentView.findViewById(R.id.lianxilayout).setVisibility(View.VISIBLE);
                }
            });
            this.contentView.findViewById(R.id.quxiao).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    BindRelativeDialog.this.contentView.findViewById(R.id.lianxilayout).setVisibility(View.GONE);
                }
            });
            this.contentView.findViewById(R.id.weixin_friend).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    if (AppConfig.userEntity == null) {
                        return;
                    }
                    Utils.umengShareForPhatForm(SHARE_MEDIA.WEIXIN,
                            BindRelativeDialog.this.activity,
                            BitmapFactory.decodeResource(BindRelativeDialog.this.activity.getResources(), R.mipmap.app_logo),
                            "您收到一份邀请",
                            "成为传承使者，团结家族宗亲，享受丰富家族资源，快来加入吧!",
                            UrlConstant.ExplainURL.QRCODE_SHARE3
                                    + "?customer_id="
                                    + AppConfig.userEntity.customer_id
                                    + "&inviteCode="
                                    + AppConfig.userEntity.invite_code);
                }
            });
            this.contentView.findViewById(R.id.tongxunlu).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Bus.getDefault().post("tong_xin_lu");
                }
            });
            this.popupWindow.setWidth(ScreenUtils.getScreenWidth(this.activity));
            this.popupWindow.setHeight(ScreenUtils.getScreenHeight(this.activity) - Utils.dp2px(this.activity, 40.0F) - ScreenUtils.getStatusBarHeight(this.activity));
            this.popupWindow.setContentView(this.contentView);
            this.popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A1000000")));
            this.popupWindow.setAnimationStyle(R.style.popmenu_animation);
        }
    }

    public boolean isShowing() {
        return this.popupWindow.isShowing();
    }

    public void setRelative(Relative paramRelative) {
        this.relative = paramRelative;
    }

    public void show() {
        if (this.relative != null) {
            Glide.with(this.activity).load(this.relative.photo).into(this.imageView);
            if ((StringUtils.isEmpty(relative.surname)) || (StringUtils.isEmpty(this.relative.name))) {
                this.contentView.findViewById(R.id.please).setVisibility(View.VISIBLE);
                this.contentView.findViewById(R.id.please).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View paramAnonymousView) {
                        Intent intent = new Intent(BindRelativeDialog.this.activity, BindFamilyActivity.class);
                        BindRelativeDialog.this.activity.startActivity(intent);
                    }
                });
                this.contentView.findViewById(R.id.relative_family).setVisibility(View.VISIBLE);
                this.contentView.findViewById(R.id.layout2).setVisibility(View.GONE);
            } else {
                this.contentView.findViewById(R.id.please).setVisibility(View.GONE);
                this.contentView.findViewById(R.id.relative_family).setVisibility(View.GONE);
                this.contentView.findViewById(R.id.layout2).setVisibility(View.VISIBLE);
                this.imageView.setAlpha(1.0F);
                this.waveView.start();
            }
        }
        this.popupWindow.showAtLocation(this.activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }
}
