package com.yuejian.meet.framents.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuejian.meet.R;
import com.yuejian.meet.framents.base.BaseFragment;

/**
 * Created by zh02 on 2017/8/25.
 */

public class EssayFragment extends BaseFragment {
    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_essay, container ,false);
    }
}
