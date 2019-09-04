package com.yuejian.meet.widgets;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;

import com.mcxiaoke.bus.Bus;
import com.yuejian.meet.R;
import com.yuejian.meet.utils.ScreenUtils;
import com.yuejian.meet.utils.Utils;

public class BigMoneyTree {
    private Activity activity;
    private PopupWindow popupWindow = null;
    private ViewGroup rootView = null;

    public BigMoneyTree(Activity paramActivity) {
        this.activity = paramActivity;
        initWindow();
    }

    private void initWindow() {
        if (this.popupWindow == null) {
            this.popupWindow = new PopupWindow(this.activity);
            this.popupWindow.setWidth(ScreenUtils.getScreenWidth(this.activity));
            this.popupWindow.setHeight(ScreenUtils.getScreenHeight(this.activity) - Utils.dp2px(this.activity, 44.0F));
            this.popupWindow.setBackgroundDrawable(new ColorDrawable());
            this.rootView = ((ViewGroup) View.inflate(this.activity, R.layout.layout_big_money_tree, null));
            this.popupWindow.setContentView(this.rootView);
            this.rootView.findViewById(R.id.yaoyiyao).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Bus.getDefault().post("yaoyiyao");
                }
            });
            this.rootView.findViewById(R.id.put_thing_btn).setOnClickListener(new View.OnClickListener() {
                public void onClick(View paramAnonymousView) {
                    Bus.getDefault().post("put_thing");
                }
            });
        }
    }

    public void dismiss() {
        if (this.popupWindow.isShowing()) {
            this.popupWindow.dismiss();
        }
    }

    public void show() {
        this.popupWindow.showAtLocation(this.activity.getWindow().getDecorView(), 83, 0, 0);
    }
}
