package com.yuejian.meet.activities.creation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.adapters.ShopAdapter;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.bean.ShopEntity;
import com.yuejian.meet.utils.ViewInject;
import com.yuejian.meet.widgets.ShopView;

import org.androidannotations.annotations.Click;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

public class ShopActivity extends BaseActivity {

    @Bind(R.id.activity_shop_back)
    View back;

    @Bind(R.id.activity_shop_search)
    View search;

    @Bind(R.id.activity_shop_RecyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.activity_shop_confirm)
    View confirm;

    List<ShopEntity> goods;

    ShopAdapter adapter;

    public static final String RESULT_ENETIY = "ShopActivity.Entity";

    private ShopEntity nextEntity = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        adapter = new ShopAdapter(recyclerView, this);
        getDataFromNet();
        adapter.setOnItemClickListener((view, position) -> {
            if (nextEntity != null) nextEntity.setClick(false);

            nextEntity = adapter.getData().get(position);
            nextEntity.setClick(true);
            adapter.notifyDataSetChanged();

        });

    }

    @OnClick({R.id.activity_shop_confirm, R.id.activity_shop_back, R.id.activity_shop_search})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_shop_confirm:
                if (nextEntity == null) {
                    ViewInject.shortToast(mContext, "请选择商品");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(RESULT_ENETIY, nextEntity);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.activity_shop_back:
                finish();
                break;

            case R.id.activity_shop_search:
                break;
        }

    }

    public static void startActivityForResult(Activity context, int requestCode) {
        Intent intent = new Intent(context, ShopActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    private void getDataFromNet() {
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", AppConfig.CustomerId);
        params.put("pageIndex", 1);
        params.put("pageItemCount", 10);
        apiImp.getVipShopGoodsList(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                if (checkIsLife()) return;
                JSONObject jo = JSON.parseObject(data);
                if (jo == null && !jo.getString("code").equals("0")) return;
                goods = JSON.parseArray(jo.getString("data"), ShopEntity.class);
                if (goods == null) return;
                adapter.refresh(goods);
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }


}
