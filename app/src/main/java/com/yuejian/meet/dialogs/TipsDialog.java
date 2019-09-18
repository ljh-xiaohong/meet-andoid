package com.yuejian.meet.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.video.common.utils.ScreenUtils;
import com.yuejian.meet.R;

public class TipsDialog extends DialogFragment {

    private View view;

    private View v_Cancel;

    private View v_Confirm;

    private TextView tv_Contents;

    private OnTipsDialogListener Listener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogFullStyle);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        view = inflater.inflate(R.layout.dialog_tips, container);
        initView(view);

        if (null != Listener) {
            Listener.onCreatedView(tv_Contents);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(
                (int) (ScreenUtils.getWidth(getContext()) * 0.8),
                (int) (ScreenUtils.getHeight(getContext()) * 0.35)
        );

    }


    //    @Override
//    public int show(FragmentTransaction transaction, String tag) {
//        return super.show(transaction, tag);
//    }
//
//    @Override
//    public void show(FragmentManager manager, String tag) {
//        if (isStateSaved()) {
//            return;
//        }
//        super.show(manager, tag);
//    }

    private void initView(View view) {
        v_Cancel = view.findViewById(R.id.dialog_cancel);
        v_Cancel.setOnClickListener(view1 -> {
            if (null != this.Listener) {
                this.Listener.onDismiss(this);
            }
        });

        v_Confirm = view.findViewById(R.id.dialog_confirm);
        v_Confirm.setOnClickListener(view1 -> {
            if (null != this.Listener) {
                this.Listener.onConfirm(this);
            }
        });

        tv_Contents = view.findViewById(R.id.dialog_content);

    }

    public void setContents(String s) {
        tv_Contents.setText(s);
    }



    public interface OnTipsDialogListener {
        void onDismiss(TipsDialog dialog);

        void onConfirm(TipsDialog dialog);

        void onCreatedView(TextView Contents);
    }

    public void setOnTipsDialogListener(OnTipsDialogListener dialogListener) {
        this.Listener = dialogListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
        if (null != this.Listener) {
            this.Listener.onDismiss(this);
        }
    }


}
