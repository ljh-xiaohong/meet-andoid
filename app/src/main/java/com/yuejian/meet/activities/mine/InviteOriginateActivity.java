package com.yuejian.meet.activities.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.activities.home.ArticleInfoActivity;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.Mine;
import com.yuejian.meet.utils.DensityUtils;
import com.yuejian.meet.utils.ImageUtils;
import com.yuejian.meet.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zh02 on 2017/8/15.
 */

public class InviteOriginateActivity extends BaseActivity {

    @Bind(R.id.qr_code_customer_photo)
    ImageView customerPhoto;
    @Bind(R.id.qr_code_customer_name)
    TextView customerName;
    @Bind(R.id.qr_code_customer_id)
    TextView customerId;
    @Bind(R.id.originate_invite_code)
    TextView inviteCode;
    @Bind(R.id.qr_code_img)
    ImageView qrCodeImage;
    @Bind(R.id.qr_view_source)
    LinearLayout qrViewSource;

    private Mine mine = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_originate);
        setTitleText("邀请码");
        mine = (Mine) getIntent().getSerializableExtra("mine");
        if (mine == null) finish();
        Glide.with(this).load(mine.photo).asBitmap().into(customerPhoto);
        customerName.setText(mine.surname + mine.name);
        customerId.setText("约见号：" + mine.customer_id);
        inviteCode.setText(inviteCode.getText().toString() + mine.invite_code);
        int size = (int) getResources().getDimension(R.dimen.px_400);
        Bitmap qrCode =
                Utils.generateBitmap(UrlConstant.ExplainURL.QRCODE_SHARE3 + "?customer_id=" + user.customer_id + "&inviteCode=" + user.invite_code,
                        size, size);
        qrCodeImage.setImageBitmap(qrCode);
    }

    @OnClick({R.id.share_qr_code, R.id.save_in_local, R.id.copy_qr_code})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_qr_code:
                Utils.umengShareByList(this, BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo),
                        "您收到一份邀请", "成为传承使者，团结家族宗亲，享受丰富家族资源，快来加入吧！",
                        UrlConstant.ExplainURL.QRCODE_SHARE3 + "?customer_id=" + user.customer_id + "&inviteCode=" + user.invite_code);
                break;
            case R.id.save_in_local:
                qrViewSource.setDrawingCacheEnabled(true);
                Bitmap tBitmap = qrViewSource.getDrawingCache();
                tBitmap = Bitmap.createBitmap(tBitmap);
                qrViewSource.setDrawingCacheEnabled(false);
                tBitmap = ImageUtils.reduce(tBitmap, DensityUtils.dip2px(this, 300), DensityUtils.dip2px(this, 362), false);
                Utils.saveImageToGallery(getBaseContext(), tBitmap, "传承使者二维码");
                ArrayList<Bitmap> bitmaps = new ArrayList<>();
                bitmaps.add(tBitmap);
                Utils.displayImages(this, bitmaps, 0, null);
                break;
            case R.id.copy_qr_code:
//                UrlConstant.ExplainURL.QRCODE_SHARE3 + "?customer_id=" + user.customer_id + "&inviteCode=" + user.invite_code
                Utils.copyText(getBaseContext(), mine.invite_code);
                break;
        }
    }
}
