package com.yuejian.meet.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yuejian.meet.R;

/**
 * @author : g000gle
 * @time : 2019/5/18 12:59
 * @desc : 底部支付dialog
 */
public class PaymentBottomDialog {

    private final Dialog mBottomDialog;
    private LinearLayout mZhifubaoItem;
    private LinearLayout mWechatItem;

    public PaymentBottomDialog(Context context, View.OnClickListener zhifubaoListener, View.OnClickListener wechatListener) {
        mBottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_payment, null);
        mZhifubaoItem = (LinearLayout) contentView.findViewById(R.id.ll_payment_item_zhifubao);
        mWechatItem   = (LinearLayout) contentView.findViewById(R.id.ll_payment_item_wechat);
        ImageView closeBtn = (ImageView) contentView.findViewById(R.id.iv_close_payment);
        closeBtn.setOnClickListener(v -> mBottomDialog.dismiss());
        mBottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomDialog.setCanceledOnTouchOutside(true);
        mBottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        mZhifubaoItem.setOnClickListener(zhifubaoListener);
        mWechatItem.setOnClickListener(wechatListener);
    }

    public void show() {
        if (mBottomDialog != null) {
            mBottomDialog.show();
        } else {
            throw new NullPointerException("PaymentBottomDialog should init first.");
        }
    }
}
