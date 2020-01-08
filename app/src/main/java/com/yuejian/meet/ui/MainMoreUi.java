package com.yuejian.meet.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.netease.nim.uikit.app.AppConfig;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.find.ScannerActivity;
import com.yuejian.meet.activities.group.SelectContactActivity;
import com.yuejian.meet.activities.mine.CreateArticleActivity;
import com.yuejian.meet.activities.mine.LoginActivity;
import com.yuejian.meet.utils.AppManager;
import com.yuejian.meet.utils.ImUtils;

public class MainMoreUi {
    Activity context;
    protected LayoutInflater mInflater;
    private View mPoupView = null;
    private PopupWindow mPoupWindow = null;

    public MainMoreUi(Activity paramActivity) {
        this.context = paramActivity;
    }

    private void bindPopMenuEvent(View paramView) {
        paramView.findViewById(R.id.ll_main_create_group).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                MainMoreUi.this.mPoupWindow.dismiss();
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    ImUtils.isLoginIm=false;
                    MainMoreUi.this.context.startActivity(new Intent(MainMoreUi.this.context, LoginActivity.class));
                    AppManager.finishAllActivity();
                    return;
                }
                Intent intent = new Intent(context, SelectContactActivity.class);
                MainMoreUi.this.context.startActivityForResult(intent, 66);
            }
        });
        paramView.findViewById(R.id.ll_main_scan).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent intent = new Intent(MainMoreUi.this.context, ScannerActivity.class);
                intent.putExtra("from_main", true);
                MainMoreUi.this.context.startActivity(intent);
                MainMoreUi.this.mPoupWindow.dismiss();
            }
        });
        paramView.findViewById(R.id.ll_main_article).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                MainMoreUi.this.mPoupWindow.dismiss();
                if (StringUtil.isEmpty(AppConfig.CustomerId)) {
                    ImUtils.isLoginIm=false;
                    MainMoreUi.this.context.startActivity(new Intent(MainMoreUi.this.context, LoginActivity.class));
                    AppManager.finishAllActivity();
                    return;
                }
                MainMoreUi.this.showMenuListDialog();
            }
        });
    }

    private void showMenuListDialog() {
        View localView = View.inflate(this.context, R.layout.layout_essay_menu_list, null);
        final AlertDialog localAlertDialog = new AlertDialog.Builder(this.context).setView(localView).create();
        localView.findViewById(R.id.mrl).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localAlertDialog.dismiss();
                Intent intent = new Intent(MainMoreUi.this.context, CreateArticleActivity.class);
                intent.putExtra("create_type", 2);
                MainMoreUi.this.context.startActivity(intent);
            }
        });
        localView.findViewById(R.id.xmqy).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localAlertDialog.dismiss();
                Intent intent = new Intent(MainMoreUi.this.context, CreateArticleActivity.class);
                intent.putExtra("create_type", 1);
                MainMoreUi.this.context.startActivity(intent);
            }
        });

        localView.findViewById(R.id.sj).setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                localAlertDialog.dismiss();
                Intent intent = new Intent(MainMoreUi.this.context, CreateArticleActivity.class);
                intent.putExtra("create_type", 3);
                MainMoreUi.this.context.startActivity(intent);
            }
        });
        localAlertDialog.show();
    }

    public void showBottomPopupWindow(View paramView) {
        if (this.mPoupView == null) {
            this.mInflater = LayoutInflater.from(this.context);
            this.mPoupView = this.mInflater.inflate(R.layout.main_more_itemlayout, null);
            this.mPoupView.setBackgroundColor(Color.parseColor("#00000000"));
            bindPopMenuEvent(this.mPoupView);
            this.mPoupWindow = new PopupWindow(this.mPoupView, -1, -2);
            this.mPoupWindow.setTouchable(true);
            this.mPoupWindow.setFocusable(true);
            this.mPoupWindow.setOutsideTouchable(true);
            this.mPoupWindow.setTouchInterceptor(new View.OnTouchListener() {
                public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                    return false;
                }
            });
            this.mPoupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                }
            });
            this.mPoupWindow.setBackgroundDrawable(new ColorDrawable(0));
        }
        int[] params = new int[2];
        paramView.getLocationOnScreen(params);
        this.mPoupWindow.showAtLocation(paramView, 0, 0, params[1] + paramView.getHeight());
    }
}
