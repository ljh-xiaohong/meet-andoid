package com.yuejian.meet.activities.family;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.mcxiaoke.bus.Bus;
import com.netease.nim.uikit.app.AppConfig;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yuejian.meet.R;
import com.yuejian.meet.adapters.ActivityLabAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.bean.ActivityLabEntity;
import com.yuejian.meet.widgets.MyNestedScrollView;
import com.yuejian.meet.widgets.springview.SpringView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.aliyun.video.common.utils.DensityUtils.dip2px;


public class ActivityLabActivity extends AppCompatActivity implements SpringView.OnFreshListener, View.OnClickListener {

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
        fullScreen(this);
        ButterKnife.bind(this);
        Bus.getDefault().register(this);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) back.getLayoutParams();
        lp.topMargin = getStatusBarHeight();
        ViewGroup.MarginLayoutParams titleLp = (ViewGroup.MarginLayoutParams) titleBar.getLayoutParams();
        titleLp.topMargin = getStatusBarHeight();


//        springView.setFooter(new DefaultFooter(mContext));
//        springView.setHeader(new DefaultHeader(mContext));
//        springView.setListener(this);
        if (!getInitData()) return;
        adapter = new ActivityLabAdapter(recyclerView, this);
        initListener();
        onRefresh();

    }

    private void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    //设置6.0 状态栏深色浅色切换
    public boolean setCommonUI(boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (dark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                if (decorView.getSystemUiVisibility() != vis) {
                    decorView.setSystemUiVisibility(vis);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId 颜色
     */
    public void setStatusBarColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(colorId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTintManager,需要先将状态栏设置为透明
            fullScreen(this);
            SystemBarTintManager systemBarTintManager = new SystemBarTintManager(this);
            systemBarTintManager.setStatusBarTintEnabled(true);//显示状态栏
            systemBarTintManager.setStatusBarTintColor(colorId);//设置状态栏颜色
        }
    }


    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        Bus.getDefault().unregister(this);
    }

    @OnClick({R.id.activity_activity_back, R.id.activity_activity_back_, R.id.activity_activity_follow})
    public void onClick(View v) {
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
        nestedScrollView.setonScrollChanged((l, t, oldl, oldt) -> {

                    titleBar.setVisibility(t >= dip2px(ActivityLabActivity.this, 44) ? View.VISIBLE : View.GONE);
                    if (t >= dip2px(ActivityLabActivity.this, 44)) {
                        titleBar.setVisibility(View.VISIBLE);
                        setCommonUI(true);
                        setStatusBarColor(Color.parseColor("#ffffff"));
                    } else {
                        titleBar.setVisibility(View.GONE);
                        setCommonUI(false);
                        setStatusBarColor(Color.parseColor("#00ffffff"));
                    }
                }
        );

        adapter.setOnItemClickListener((view, position1) -> {
            ActivityLabEntity.Content content = labEntity.getContent().get(position1);
            switch (content.getType()) {

                //文章
                case 2:
                    ArticleActivity.startActivity(this, content.getId() + "", AppConfig.CustomerId);
                    break;

                //视频
                case 4:
                    VideoActivity.startActivity(this, content.getId() + "", AppConfig.CustomerId, content.getCoveSizeType() == 0 ? true : false);
                    break;
            }
        });
    }

    public static void startActivity(Context context, String clId, String customerId) {
        Intent intent = new Intent(context, ActivityLabActivity.class);
        intent.putExtra("ActivityLabActivity.clId", clId);
        intent.putExtra("ActivityLabActivity.customerId", customerId);
        context.startActivity(intent);
    }

    private boolean getInitData() {
        clId = getIntent().getStringExtra("ActivityLabActivity.clId");
        customerId = getIntent().getStringExtra("ActivityLabActivity.customerId");
        return clId != null && customerId != null;
    }

    private void loadDataFromNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("clId", clId);
        params.put("customerId", customerId);
        params.put("pageIndex", pageIndex);
        params.put("pageItemCount", pageItemCount);
        new ApiImp().findContentByLabel(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                JSONObject jo = JSON.parseObject(data);
                if (jo == null && !jo.getString("code").equals("0")) return;
                labEntity = JSON.parseObject(jo.getString("data"), ActivityLabEntity.class);
                if (labEntity != null) {
                    if (labEntity.getContent() != null && labEntity.getContent().size() > 0)
                        adapter.refresh(labEntity.getContent());
                    if (labEntity.getLabel() != null) {
                        Glide.with(ActivityLabActivity.this).load(labEntity.getLabel().getCoverUrl()).into(title_img);
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
        new ApiImp().addContentLabelCustomer(params, this, new DataIdCallback<String>() {
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
