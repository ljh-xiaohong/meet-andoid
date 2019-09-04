package com.yuejian.meet.dialogs;

import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.yuejian.meet.R;

import butterknife.Bind;

/**
 * 加载对话框
 * <b>创建时间</b> 2016/5/7 <br>
 *
 * @author zhouwenjun
 */
public class LoadingDialogFragment extends BaseDialogFragment {

    @Bind(R.id.txt_loading_msg)
    TextView mTxtLoadingMsg;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.loding_dialog_layout, container, false);
    }

    /**
     * 获取dialog实例
     *
     * @param info
     * @return
     */
    public static LoadingDialogFragment newInstance(String info) {
        LoadingDialogFragment adf = new LoadingDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("alert-info", info);
        adf.setArguments(bundle);
        adf.setCancelable(false);

        return adf;
    }

    @Override
    protected void initData() {
        super.initData();
        if (getArguments().getString("alert-info") != null) {
            mTxtLoadingMsg.setText(getArguments().getString("alert-info"));
        }
    }

    public void setTxtLoadingMsg(String msg) {
        mTxtLoadingMsg.setText(msg);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        Runnable run = new Runnable() {

            @Override
            public void run() {
                startProgress();
            }
        };
        Handler han = new Handler();
        han.postAtTime(run, 100);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            WindowManager.LayoutParams windowParams = window.getAttributes();
            windowParams.dimAmount = 0f;
            windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(windowParams);
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded() && !this.isVisible() && !this.isRemoving()) {
            super.show(manager, tag);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void dismiss() {
        try {
            super.dismissAllowingStateLoss();
            if (dismissListener != null) {
                dismissListener.onDismiss();
            }
            isShowing = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        isShowing = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void startProgress() {
    }

    private OnDismissListener dismissListener = null;

    public void setOnDismissListener(OnDismissListener listener) {
        dismissListener = listener;
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}

