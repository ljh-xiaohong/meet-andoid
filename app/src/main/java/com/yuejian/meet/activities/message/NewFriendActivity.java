package com.yuejian.meet.activities.message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuejian.meet.R;
import com.yuejian.meet.adapters.NewFriendAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewFriendActivity extends AppCompatActivity {
    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.back)
    ImageView back;
    private NewFriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_zan_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setText("新朋友");
        back.setOnClickListener(v -> finish());
        adapter = new NewFriendAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
