package com.yuejian.meet.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.yuejian.meet.R;

import java.util.HashMap;
import java.util.Map;

public class MoreDialog extends BaseDialog implements View.OnClickListener {

    LinearLayout ll;

    BaseAdapter adapter;

    private OnclickItemListener listener;

    private Map<View, Integer> viewMap;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.QUDemoFullStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_more, container);
        ll = view.findViewById(R.id.dialog_more_layout);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != this.adapter && this.adapter.getCount() > 0) {
            viewMap = new HashMap<>();
            for (int i = 0; i < this.adapter.getCount(); i++) {
                View child = this.adapter.getView(i, null, ll);
                viewMap.put(child, i);
                child.setOnClickListener(this);
                ll.addView(child);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;

    }

    public void setOnclickItemListener(OnclickItemListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(viewMap.get(view));
            return;
        }
    }

    public interface OnclickItemListener {
        void onItemClick(int position);
    }




}
