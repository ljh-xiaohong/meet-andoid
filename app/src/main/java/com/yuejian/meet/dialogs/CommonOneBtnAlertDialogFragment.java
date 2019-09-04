package com.yuejian.meet.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.utils.DensityUtils;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * <b>创建时间</b> 2016/4/27 <br>
 *
 * @author zhouwenjun
 */
public class CommonOneBtnAlertDialogFragment extends BaseDialogFragment {

    @Bind(R.id.txt_common_alert_dialog_title)
    TextView mTxtTitle;
    @Bind(R.id.btn_common_alert_dialog_confirm)
    Button mBtnConfirm;
    private String title;
    private OnConfirmListener confirmListener;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.common_alert_dialog_layout, container, false);
    }

    /**
     * 获取dialog实例
     *
     * @param title
     * @return
     */
    public static CommonOneBtnAlertDialogFragment newInstance(String title) {
        CommonOneBtnAlertDialogFragment adf = new CommonOneBtnAlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);

        adf.setArguments(bundle);
        return adf;
    } public static CommonOneBtnAlertDialogFragment newInstance(String title,String btnTxt) {
        CommonOneBtnAlertDialogFragment adf = new CommonOneBtnAlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("btnTxt", btnTxt);

        adf.setArguments(bundle);
        return adf;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        if (getArguments().getString("title") != null) {
            title = getArguments().getString("title");
            mTxtTitle.setText(title);
        }
        if (getArguments().getString("btnTxt") != null) {
            title = getArguments().getString("btnTxt");
            mBtnConfirm.setText(title);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout((int) (DensityUtils.getScreenW(getActivity()) * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick({R.id.btn_common_alert_dialog_confirm})
    public void setClickListener(View view) {
        switch (view.getId()) {
            case R.id.btn_common_alert_dialog_confirm:
                if (confirmListener != null){
                    confirmListener.onConfirm();
                }
                this.dismiss();
                break;
        }
    }

    public void setOnConfirmListener(OnConfirmListener listener){
        this.confirmListener = listener;
    }

    public interface OnConfirmListener{
        void onConfirm();
    }
}
