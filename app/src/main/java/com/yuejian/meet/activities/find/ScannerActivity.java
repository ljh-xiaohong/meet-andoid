package com.yuejian.meet.activities.find;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.mcxiaoke.bus.annotation.BusReceiver;
import com.mylhyl.zxing.scanner.OnScannerCompletionListener;
import com.mylhyl.zxing.scanner.ScannerOptions;
import com.mylhyl.zxing.scanner.ScannerView;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.BusMessage;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.AppUitls;
import com.yuejian.meet.utils.DecodeImageCallback;
import com.yuejian.meet.utils.DecodeImageThread;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;


import butterknife.OnClick;

/**
 * 扫一扫
 * Created by Administrator on 2018/1/22/022.
 */

public class ScannerActivity extends BaseActivity {
    private static final int IMAGE = 1012;
    private ScannerView scannerView;
    private ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
//        initBackButton(false);
//        TextView close = (TextView) findViewById(R.id.titlebar_text_back);
//        close.setVisibility(View.VISIBLE);
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        TextView myQRCode = (TextView) findViewById(R.id.txt_titlebar_save);
//        myQRCode.setVisibility(View.VISIBLE);
//        myQRCode.setText(R.string.scanner_content);
//        myQRCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivityForResult(new Intent(ScannerActivity.this, MyQrCodeActivity.class), 1313);
//            }
//        });
//        setTitleText(getString(R.string.scanner_title));
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
        scannerView = (ScannerView) findViewById(R.id.scanner_view);
        ScannerOptions options = new ScannerOptions.Builder()
                .setTipTextSize(12)
                .setTipText("请对准二维码")
                .setFrameCornerColor(0xFFE45757)
                .setMediaResId(R.raw.weixin_beep)
                .build();
        scannerView.setScannerOptions(options);
        scannerView.setOnScannerCompletionListener(new OnScannerCompletionListener() {
            @Override
            public void onScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
                String result = rawResult.getText();
                Log.d("Sanner", result);
                if (StringUtils.isNotEmpty(result)) {
                    handleScanResult(result);
                }
            }
        });

    }

    private void handleScanResult(String result) {
        if (scannerView != null) {
            scannerView.onPause();
        }
        findViewById(R.id.go_to_grally).setVisibility(View.GONE);
        if (result.contains(UrlConstant.ExplainURL.QRCODE_SHARE)) {
            String customerId = Utils.getValueByName(result, "customer_id");
            AppUitls.goToPersonHome(this, customerId);
        } else {
            Intent intent = new Intent();
            intent.putExtra("scanner_result", result);
            setResult(RESULT_OK, intent);
        }
        finish();
    }


    @Override
    protected void onResume() {
        scannerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        scannerView.onPause();
        super.onPause();
    }

    @OnClick({R.id.go_to_grally})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_to_grally:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == IMAGE && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                final String imagePath = c.getString(columnIndex);
                Log.d("imagePath", imagePath);
//                QRDecode.decodeQR(imagePath, new OnScannerCompletionListener() {
//                    @Override
//                    public void onScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
//                        if (rawResult != null) {
//                            handleScanResult(rawResult.getText());
//                        } else {
//                            Toast.makeText(getBaseContext(), "未识别到二维码", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                new DecodeImageThread(imagePath, new DecodeImageCallback() {
                    @Override
                    public void decodeSucceed(Result result) {
                        if (result != null) {
                            handleScanResult(result.getText());
                        }
                    }
                    @Override
                    public void decodeFail(int type, String reason) {
                        Toast.makeText(getBaseContext(), R.string.scanner_toast, Toast.LENGTH_SHORT).show();
                    }
                }).run();
                c.close();
            } if (requestCode == 1313 && data != null && data.getBooleanExtra("go_to_index", false)) {
                finish();
            }
        }
    }

    @BusReceiver
    public void receiveBusMessage(BusMessage busMessage) {
        if (BusMessage.QRCODE_MESSAGE.equals(busMessage.type)) {
            if (busMessage.message instanceof String) {
                String message = (String) busMessage.message;
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                handleScanResult(message);
            }
        }
    }
}
