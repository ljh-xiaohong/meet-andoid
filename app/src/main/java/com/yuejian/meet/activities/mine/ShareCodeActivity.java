package com.yuejian.meet.activities.mine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.app.entity.UserEntity;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.NewHomeEntity;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;
import com.yuejian.meet.widgets.CircleImageView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * @author :
 * @time : 2018/11/20 16:56
 * @desc : 百家姓ID 分享
 * @version: V1.0
 * @update : 2018/11/20 16:56
 */

public class ShareCodeActivity extends BaseActivity {
    @Bind(R.id.user_photo)
    CircleImageView photo;
    @Bind(R.id.user_name)
    TextView user_name;
    @Bind(R.id.user_corp)
    TextView user_corp;
    @Bind(R.id.txt_phone)
    TextView txt_phone;
    @Bind(R.id.txt_email)
    TextView txt_email;
    @Bind(R.id.txt_location)
    TextView txt_location;
    @Bind(R.id.shaer_code)
    ImageView shaer_code;
    @Bind(R.id.email_layout)
    LinearLayout email_layout;
    @Bind(R.id.phone_layout)
    LinearLayout phone_layout;
    @Bind(R.id.content_layout)
    LinearLayout content_layout;
    @Bind(R.id.location_layout)
    LinearLayout location_layout;
    NewHomeEntity entity;
    UserEntity userEntity;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_code);
        setTitleText("分享二维码");
        initData();
    }

    public void initData() {

        requstData();
    }

    public void requstData() {
        Map<String, Object> params = new HashMap<>();
        params.put("customer_id", user.getCustomer_id());
        params.put("my_customer_id", user.getCustomer_id());
        params.put("isAction", "false");
        apiImp.getUserInfoV3(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                entity = JSON.parseObject(data, NewHomeEntity.class);
                userEntity = JSON.parseObject(entity.getCustomer(), UserEntity.class);
                setLayout();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }

    public void setLayout() {

        Glide.with(this).load(userEntity.getPhoto()).placeholder(R.mipmap.ic_default).into(photo);
        user_corp.setText(userEntity.getCompany_name());
        user_corp.setVisibility(StringUtils.isEmpty(userEntity.getCompany_name()) ? View.GONE : View.VISIBLE);
        user_name.setText(userEntity.getSurname() + userEntity.getName());
        txt_phone.setText(userEntity.getMobile());
        txt_email.setText(userEntity.getEmail());
        txt_location.setText(userEntity.getPosition());
        location_layout.setVisibility(StringUtils.isEmpty(userEntity.getPosition()) ? View.GONE : View.VISIBLE);
        phone_layout.setVisibility(StringUtils.isEmpty(userEntity.getMobile()) ? View.GONE : View.VISIBLE);
        email_layout.setVisibility(StringUtils.isEmpty(userEntity.getEmail()) ? View.GONE : View.VISIBLE);
        int size = Utils.dp2px(getBaseContext(), 48.0F);
        Bitmap qrCode = Utils.generateBitmap(UrlConstant.ExplainURL.QRCODE_SHARE + "?customer_id=" + user.customer_id + "&inviteCode=" + user.invite_code, size, size);
        shaer_code.setImageBitmap(qrCode);
    }

    @OnClick({R.id.share_wechat, R.id.share_qq, R.id.share_find})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.share_wechat://分享到微信
                bitmap = createViewBitmap(content_layout);
                Utils.shareAllType(this, bitmap, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.share_qq://分享到qq
//                bitmap = createViewBitmap(content_layout);
//                Utils.shareAllType(this,bitmap, SHARE_MEDIA.QQ);
                break;
            case R.id.share_find://分享到朋友圈
                bitmap = createViewBitmap(content_layout);
                Utils.shareAllType(this, bitmap, SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
}
