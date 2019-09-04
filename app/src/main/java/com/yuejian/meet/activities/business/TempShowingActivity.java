package com.yuejian.meet.activities.business;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;

import butterknife.Bind;

/**
 * @author : g000gle
 * @time : 2019/6/4 09:51
 * @desc : 临时展示商圈二级页面
 */
public class TempShowingActivity extends BaseActivity {

    @Bind(R.id.iv_temp_showing_back_btn)
    ImageView mBackBtn;
    @Bind(R.id.tv_temp_showing_title)
    TextView mTitleView;

    @Bind(R.id.sv_temp_showing)
    ScrollView mScrollView;
    @Bind(R.id.iv_temp_showing_img_1)
    ImageView mImageView1;
    @Bind(R.id.iv_temp_showing_img_2)
    ImageView mImageView2;
    @Bind(R.id.iv_temp_showing_img_3)
    ImageView mImageView3;

    @Bind(R.id.tv_temp_showing_bottom_title)
    TextView mBottomTitle;
    @Bind(R.id.tv_temp_showing_bottom_content)
    TextView mBottomContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_showing);

        String title = getIntent().getStringExtra("title");
        mTitleView.setText(title);
        mBackBtn.setOnClickListener(v -> finish());

        mScrollView.setOnTouchListener((v, event) -> true);

        switch (title) {
            case "精资源":
                Glide.with(this).load(R.mipmap.jingziyuan_top).into(mImageView1);
                Glide.with(this).load(R.mipmap.jingziyuan_01).into(mImageView2);
                Glide.with(this).load(R.mipmap.jingziyuan_02).into(mImageView3);
                mBottomTitle.setText("热门资源分享平台：");
                mBottomContent.setText("开通VIP特权用户可在商圈通过发布资源，展示自己拥有的精资源，同时开展商务活动等等");
                break;
            case "好项目":
                Glide.with(this).load(R.mipmap.haoxiangmu_top).into(mImageView1);
                Glide.with(this).load(R.mipmap.haoxiangmu_01).into(mImageView2);
                Glide.with(this).load(R.mipmap.haoxiangmu_02).into(mImageView3);
                mBottomTitle.setText("寻找好项目：");
                mBottomContent.setText("开通VIP特权用户可在商圈通过发布项目，展示自己的好项目，寻找合伙人");
                break;
            case "易赚宝":
                Glide.with(this).load(R.mipmap.yizhuanbao_top).into(mImageView1);
                Glide.with(this).load(R.mipmap.yizhuanbao_01).into(mImageView2);
                Glide.with(this).load(R.mipmap.yizhuanbao_02).into(mImageView3);
                mBottomTitle.setText("分享转发就能赚钱：");
                mBottomContent.setText("开通VIP特权用户可推送转发优质产品信息链接，商品售出后推广用户可获得商家承诺的推广佣金");
                break;
            case "优品购":
                Glide.with(this).load(R.mipmap.youpingou_top).into(mImageView1);
                Glide.with(this).load(R.mipmap.youpingou_01).into(mImageView2);
                Glide.with(this).load(R.mipmap.youpingou_02).into(mImageView3);
                mBottomTitle.setText("专注优质商城：");
                mBottomContent.setText("开通VIP特权用户可上传优质产品信息进入优品购商城");
                break;
        }

    }
}
