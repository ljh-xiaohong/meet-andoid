package com.yuejian.meet.framents.mine;

import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.framents.base.BaseFragment;

import butterknife.Bind;

public class MineInsideFragment extends BaseFragment {

    @Bind(R.id.fragment_mine_inside_create)
    RecyclerView mRecycleView;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        return inflater.inflate(R.layout.fragment_mine_inside_create, container, false);
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(new CreateAdapter());


    }


    class CreateAdapter extends RecyclerView.Adapter<CreateAdapter.CreateVH> {


        @Override
        public CreateVH onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextView textView = new TextView(getContext());
            textView.setText("123456");


            return new CreateVH(textView);
        }

        @Override
        public void onBindViewHolder(CreateVH createVH, int i) {

        }

        @Override
        public int getItemCount() {
            return 100;
        }

        class CreateVH extends RecyclerView.ViewHolder {

            public CreateVH(View itemView) {
                super(itemView);
            }


        }
    }


}
