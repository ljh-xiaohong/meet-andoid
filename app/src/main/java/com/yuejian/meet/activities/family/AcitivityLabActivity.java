package com.yuejian.meet.activities.family;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ActivityLabAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ActivityLabEntity;
import com.yuejian.meet.widgets.MyNestedScrollView;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

import static com.aliyun.video.common.utils.DensityUtils.dip2px;


public class AcitivityLabActivity extends BaseActivity implements SpringView.OnFreshListener {

    @Bind(R.id.activity_activity_recycleview)
    RecyclerView recyclerView;

    @Bind(R.id.activity_activity_nestedScrollView)
    MyNestedScrollView nestedScrollView;
//
//    @Bind(R.id.activity_activity_springView)
//    SpringView springView;

    @Bind(R.id.activity_activity_title_one)
    TextView title_one;

    @Bind(R.id.activity_activity_title_two)
    TextView title_two;

    @Bind(R.id.activity_activity_title)
    View titleBar;

    @Bind(R.id.activity_activity_back)
    View back;

    @Bind(R.id.activity_activity_back_)
    View back1;

    @Bind(R.id.activity_activity_content)
    TextView content;

    @Bind(R.id.activity_activity_follow)
    TextView follow;

    @Bind(R.id.activity_activity_background_img)
    ImageView title_img;

    String clId, customerId;

    ActivityLabAdapter adapter;

    private int pageIndex = 1;
    private int pageItemCount = 10;

    ActivityLabEntity labEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_lab);
//        springView.setFooter(new DefaultFooter(mContext));
//        springView.setHeader(new DefaultHeader(mContext));
//        springView.setListener(this);
        if (!getInitData()) return;
        adapter = new ActivityLabAdapter(recyclerView, mContext);
        initListener();
        onRefresh();

    }

    @OnClick({R.id.activity_activity_back, R.id.activity_activity_back_, R.id.activity_activity_follow})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_activity_back:
                finish();
                break;
            case R.id.activity_activity_back_:
                finish();
                break;
            case R.id.activity_activity_follow:
                AddFollow(labEntity.getLabel().isIsFocus());
                break;
        }

    }

    private void initListener() {
        nestedScrollView.setonScrollChanged(new MyNestedScrollView.OnScrollViewListener() {
            @Override
            public void ScrollView(int l, int t, int oldl, int oldt) {
                titleBar.setVisibility(t >= dip2px(AcitivityLabActivity.this, 44) ? View.VISIBLE : View.GONE);
            }
        });

        adapter.setOnItemClickListener((view, position1) -> {
            ActivityLabEntity.Content content = labEntity.getContent().get(position1);
            switch (content.getType()) {

                //文章
                case 2:

                    break;

                //视频
                case 4:
                    VideoActivity.startActivity(mContext, content.getId() + "", AppConfig.CustomerId, content.getCoveSizeType() == 0 ? true : false);
                    break;
            }
        });
    }

    public static void startActivity(Context context, String clId, String customerId) {
        Intent intent = new Intent(context, AcitivityLabActivity.class);
        intent.putExtra("AcitivityLabActivity.clId", clId);
        intent.putExtra("AcitivityLabActivity.customerId", customerId);
        context.startActivity(intent);
    }

    private boolean getInitData() {
        clId = getIntent().getStringExtra("AcitivityLabActivity.clId");
        customerId = getIntent().getStringExtra("AcitivityLabActivity.customerId");
        return clId != null && customerId != null;
    }

    private void loadDataFromNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("clId", clId);
        params.put("customerId", customerId);
        params.put("pageIndex", pageIndex);
        params.put("pageItemCount", pageItemCount);
        apiImp.findContentByLabel(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                JSONObject jo = JSON.parseObject(data);
                if (jo == null && !jo.getString("code").equals("0")) return;
                labEntity = JSON.parseObject(jo.getString("data"), ActivityLabEntity.class);
                if (labEntity != null) {
                    if (labEntity.getContent() != null && labEntity.getContent().size() > 0)
                        adapter.refresh(labEntity.getContent());
                    if (labEntity.getLabel() != null) {
                        Glide.with(mContext).load(labEntity.getLabel().getCoverUrl()).into(title_img);
                        title_one.setText(labEntity.getLabel().getTitle());
                        title_two.setText(labEntity.getLabel().getTitle());
                        content.setText(labEntity.getLabel().getDes());
                        follow.setBackgroundResource(labEntity.getLabel().isIsFocus() ? R.drawable.bg_activity_add_w : R.drawable.bg_activity_add);
                        follow.setText(labEntity.getLabel().isIsFocus() ? "已关注" : "加关注");
                    }

                }


            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }


    @Override
    public void onRefresh() {
        pageIndex = 1;
        loadDataFromNet();
    }

    @Override
    public void onLoadmore() {
        ++pageIndex;
        loadDataFromNet();
    }

    public void AddFollow(boolean isFollows) {
        Map<String, Object> params = new HashMap<>();
        params.put("clId", clId);
        params.put("customerId", customerId);
        //0-取消关注 1-添加关注
        params.put("type", isFollows ? 0 : 1);
        apiImp.addContentLabelCustomer(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {

                JSONObject jo = JSON.parseObject(data);
                if (jo == null || !jo.getString("code").equals("0")) return;
                labEntity.getLabel().setIsFocus(!isFollows);
                follow.setBackgroundResource(labEntity.getLabel().isIsFocus() ? R.drawable.bg_activity_add_w : R.drawable.bg_activity_add);
                follow.setText(labEntity.getLabel().isIsFocus() ? "已关注" : "加关注");

            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }
}
