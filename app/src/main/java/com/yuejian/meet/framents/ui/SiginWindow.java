package com.yuejian.meet.framents.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.activities.family.EveryDaySignActivity;
import com.yuejian.meet.activities.mine.VerifyCenterActivity;

public class SiginWindow {
    Activity context;
    private View mPoupView = null;
    protected LayoutInflater mInflater;
    private PopupWindow mPoupWindow = null;
    TextView cotent,check,window_title;
    ImageView window_photo;
    int type=1;


    public SiginWindow(Activity context){
        this.context=context;
    }
    /**
     * 底部PopupWindow
     */
    public void showBottomPopupWindow(View secroll_view) {
        if (mPoupView == null) {
            mInflater = LayoutInflater.from(context);
            mPoupView = mInflater.inflate(R.layout.item_dialog_ligin_widdow_layout, null);
            bindPopMenuEvent(mPoupView);
        }
        mPoupWindow = new PopupWindow(mPoupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            mPoupWindow.setAnimationStyle(R.style.PopupAnimation);
        mPoupWindow.setTouchable(true);
        mPoupWindow.setFocusable(true);
        mPoupWindow.setOutsideTouchable(true);
        mPoupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mPoupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        ColorDrawable dw = new ColorDrawable(00000000);
        mPoupWindow.setBackgroundDrawable(dw);
        mPoupWindow.showAtLocation(secroll_view, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.7f);
    }
    public void bindPopMenuEvent(View view){
        cotent= (TextView) view.findViewById(R.id.cotent);
        window_title= (TextView) view.findViewById(R.id.window_title);
        window_photo= (ImageView) view.findViewById(R.id.window_photo);
        check= (TextView) view.findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mPoupWindow)
                    mPoupWindow.dismiss();
                if (type==1){
                    context.startActivity(new Intent(context, EveryDaySignActivity.class));
                }else {
                    context.startActivity(new Intent(context,VerifyCenterActivity.class));
                }
            }
        });
        view.findViewById(R.id.window_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mPoupWindow)
                    mPoupWindow.dismiss();
            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    /**
     * 设置内容
     * @param content
     */
    public void setContext(String content,int type){
        cotent.setText(content);
        this.type=type;
        if (type==1){
            check.setText(R.string.invite_check_out);
            window_title.setText(R.string.Sign_in_successfully);
            window_photo.setImageResource(R.mipmap.img_qiandao);
        }else {
            check.setText(R.string.Immediately_binding);
            window_title.setText(R.string.bangding_phone);
            window_photo.setImageResource(R.mipmap.img_bangding);
        }
    }
}
