package com.yuejian.meet.framents.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuejian.meet.R;
import com.yuejian.meet.framents.base.BaseFragment;

public class MineBusinessActivityFragment extends BaseFragment {


    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_mine_inside_create, container, false);
    }


}
