package com.yuejian.meet.activities.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.adapters.CommtentZanAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommentZanActivity extends AppCompatActivity {
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.title)
    TextView title;
    private CommtentZanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_zan_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        back.setOnClickListener(v -> finish());
        adapter = new CommtentZanAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
