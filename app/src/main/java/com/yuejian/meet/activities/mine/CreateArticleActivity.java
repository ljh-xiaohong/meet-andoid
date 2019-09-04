package com.yuejian.meet.activities.mine;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.session.constant.Extras;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.dialogs.LoadingDialogFragment;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.StringUtils;
import com.yuejian.meet.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 编辑文章
 * Created by zh02 on 2017/8/25.
 */

public class CreateArticleActivity extends BaseActivity {
    @Bind(R.id.web_essay_content)
    WebView webContentView;
    @Bind(R.id.famous_person_name_edit)
    EditText mrlNameEdit;
    @Bind(R.id.fp_age_edit)
    EditText mrlAgeEdit;
    @Bind(R.id.essay_title_edit)
    EditText qyTitleEdit;
    @Bind(R.id.paste_from_name)
    EditText pasteFromName;
    @Bind(R.id.paste_from_url)
    EditText pasteFromUrl;
    @Bind(R.id.paste_layout)
    View pasteLayout;
    @Bind(R.id.paste_check)
    RadioButton pasteCheck;
    @Bind(R.id.edit_check)
    RadioButton editCheck;


    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessage5;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILE_CHOOSER_RESULT_CODE = 1;
    private String photoUrl = "";

    private int createType = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);
        setTitleText(getString(R.string.article_title));
        createType = getIntent().getIntExtra("create_type", -1);
        initWebView();
        initView();

    }

    private void initView() {
        //名人录
        if (createType == 2) {
            findViewById(R.id.mrl_name_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.mrl_age_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.title_layout).setVisibility(View.GONE);
        } else if (createType == 1 || createType == 3) {
            findViewById(R.id.title_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.mrl_name_layout).setVisibility(View.GONE);
            findViewById(R.id.mrl_age_layout).setVisibility(View.GONE);
        }
        qyTitleEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        mrlAgeEdit.setInputType(InputType.TYPE_NULL);
        mrlAgeEdit.setFocusable(false);
        mrlAgeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(CreateArticleActivity.this).create();
                View contentView = View.inflate(CreateArticleActivity.this, R.layout.item_famous_person_age_pull_down, null);
                TextView currentAge = (TextView) contentView.findViewById(R.id.current_age);
                TextView modernAge = (TextView) contentView.findViewById(R.id.modern_age);
                TextView ancientAge = (TextView) contentView.findViewById(R.id.ancient_age);
                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView view = (TextView) v;
                        String tag = (String) view.getTag();
                        if (tag != null) {
                            try {
                                mrlAgeEdit.setText(view.getText().toString());
                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                currentAge.setOnClickListener(listener);
                modernAge.setOnClickListener(listener);
                ancientAge.setOnClickListener(listener);
                dialog.setView(contentView);
                dialog.show();
            }
        });

        qyTitleEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        mrlNameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        pasteCheck.setChecked(false);
        editCheck.setChecked(true);
    }


    private void initWebView() {
        loadingDialogFragment = LoadingDialogFragment.newInstance(getString(R.string.uploading_pictures));
        String url = UrlConstant.getWebUrl() + "editArticle.html";
        url = url.replace("https", "http");
        WebSettings settings = webContentView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setAllowFileAccess(true);
        settings.setSupportMultipleWindows(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webContentView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        webContentView.setWebViewClient(new MyWebClient());// 新建浏览器客户端，不调用系统浏览器
        webContentView.setWebChromeClient(new WebChromeClient() {
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                showMoreSelection(FILE_CHOOSER_RESULT_CODE, false);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg);
            }

            // For Lollipop 5.0+ DevicesCar
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                try {
                    if (mUploadMessage5 != null) {
                        mUploadMessage5.onReceiveValue(null);
                        mUploadMessage5 = null;
                    }
                    mUploadMessage5 = filePathCallback;
                    showMoreSelection(REQUEST_SELECT_FILE, false);
                } catch (Exception e) {
                    mUploadMessage5 = null;
                    return false;
                }
                return true;
            }
        });
        webContentView.loadUrl(url);
        webContentView.loadUrl("javascript:alert(injectedObject.toString())");
    }


    @OnCheckedChanged({R.id.paste_check, R.id.edit_check})
    public void onFocusChange(CompoundButton view, boolean isChecked) {
        if (view.getId() == R.id.paste_check) {
            if (isChecked) {
                pasteLayout.setVisibility(View.VISIBLE);
                webContentView.setVisibility(View.GONE);
            } else {
                pasteLayout.setVisibility(View.GONE);
                webContentView.setVisibility(View.VISIBLE);
            }
        }
    }


    @OnClick({R.id.create_essay, R.id.fanhui, R.id.upload_photo})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fanhui:
                Utils.showNoTitleDialog(this, getString(R.string.article_hint), getString(R.string.confirm), getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //do nothing
                    }
                });
                break;
            case R.id.create_essay:
                releaseArticle();
                break;
            case R.id.upload_photo:
                showMoreSelection(select_photo, true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (select_photo == requestCode) {
                String filePath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
                ImageView uploadPhoto = (ImageView) findViewById(R.id.upload_photo);
                Glide.with(this).load(new File(filePath)).into(uploadPhoto);
                uploadPhoto(filePath);
            } else {
                if (requestCode == REQUEST_SELECT_FILE || requestCode == FILE_CHOOSER_RESULT_CODE) {
                    if (mUploadMessage == null && mUploadMessage5 == null) {
                        return;
                    }
                    String filePath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
                    Uri uri = Uri.fromFile(new File(filePath));
                    if (mUploadMessage != null) {
                        mUploadMessage.onReceiveValue(uri);
                        mUploadMessage = null;
                    } else {
                        mUploadMessage5.onReceiveValue(new Uri[]{uri});
                        mUploadMessage5 = null;
                    }
                }
            }
        } else {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            } else if (mUploadMessage5 != null) {
                mUploadMessage5.onReceiveValue(null);
                mUploadMessage5 = null;
            }
        }
    }

    private static final int select_photo = 11100;

    public class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (1 == msg.what) {
                createArticle(String.valueOf(msg.obj));
            }
            return false;
        }
    });

    private void createArticle(String content) {
        if (loadingDialogFragment != null && loadingDialogFragment.isShowing) {
            return;
        }
        String title = qyTitleEdit.getText().toString();
        String fpName = mrlNameEdit.getText().toString();
        String fpAge = mrlAgeEdit.getText().toString();
        if (StringUtils.isEmpty(photoUrl)) {
            Toast.makeText(mContext, getString(R.string.article_hint1), Toast.LENGTH_SHORT).show();
            return;
        }
        //文章模块(1.起源 ,2.名人录. 3.事件)
        if (createType == 1 || createType == 3) {
            if (StringUtils.isEmpty(title)) {
                Toast.makeText(mContext, R.string.article_hint2, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (createType == 2) {
            if (StringUtils.isEmpty(fpAge)) {
                Toast.makeText(mContext, R.string.article_hint3, Toast.LENGTH_SHORT).show();
                return;
            }

            if (StringUtils.isEmpty(fpName)) {
                Toast.makeText(mContext, R.string.article_hint4, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        findViewById(R.id.create_essay).setEnabled(false);
        HashMap<String, Object> params = new HashMap<>();
        params.put("customer_id", user.customer_id);
        if (StringUtils.isEmpty(title)) {
            title = fpName;
        }
        params.put("article_title", title);
        params.put("article_type", String.valueOf(createType));
        int ageType = 0;
        if (getString(R.string.article_equals).equals(fpAge)) {
            ageType = 1;
        } else if (getString(R.string.article_equals1).equals(fpAge)) {
            ageType = 2;
        } else if (getString(R.string.article_equals2).equals(fpAge)) {
            ageType = 3;
        }
        params.put("article_age", String.valueOf(ageType));
        params.put("article_photo", photoUrl);
        String url = pasteFromUrl.getText().toString();
        String from = pasteFromName.getText().toString();
        if (pasteCheck.isChecked()) {
            if (StringUtils.isEmpty(url)) {
                Toast.makeText(mContext, R.string.article_hint6, Toast.LENGTH_SHORT).show();
                return;
            }
            content = "";
        } else {
            url = "";
            from = "";
        }
        params.put("article_url", url);
        params.put("article_from", from);
        params.put("article_content", content);
        apiImp.createArticle(params, this, new DataIdCallback<String>() {
            @Override
            public void onSuccess(String data, int id) {
                findViewById(R.id.create_essay).setEnabled(true);
                qyTitleEdit.setText("");
                try {
                    Toast.makeText(mContext, new JSONObject(data).getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {
                findViewById(R.id.create_essay).setEnabled(true);
            }
        });
    }

    private void releaseArticle() {
        webContentView.loadUrl("javascript:window.java_obj.getSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

    private LoadingDialogFragment loadingDialogFragment;

    private void uploadPhoto(String filePath) {
        loadingDialogFragment.setCancelable(false);
        loadingDialogFragment.show(getFragmentManager(), "load");
        FeedsApiImpl api = new FeedsApiImpl();
        api.upLoadImageFileToOSS(filePath, OssUtils.getTimeNmaeJpg(), this, new DataCallback<FeedsResourceBean>() {
            @Override
            public void onSuccess(FeedsResourceBean data) {
                photoUrl = data.imageUrl;
                loadingDialogFragment.dismiss();
            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                Toast.makeText(mContext, "上传图片失败", Toast.LENGTH_SHORT).show();
                loadingDialogFragment.dismiss();
                findViewById(R.id.create_essay).setEnabled(true);
            }
        });
    }

    private void showMoreSelection(final int selectPhotoType, final boolean isCupture) {
//        String[] items = {"手机拍照", "从相册里选择"};
//        final OptionPicker picker = new OptionPicker(this, items) {
//            @Override
//            protected void onCancel() {
//                super.onCancel();
//                if (mUploadMessage != null) {
//                    mUploadMessage.onReceiveValue(null);
//                    mUploadMessage = null;
//                } else if (mUploadMessage5 != null) {
//                    mUploadMessage5.onReceiveValue(null);
//                    mUploadMessage5 = null;
//                }
//            }
//        };
//        picker.setTitleText(R.string.title_choose);
//        picker.setTextSize(14);
//        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
//            @Override
//            public void onOptionPicked(int position, String option) {
//                if ("手机拍照".equals(option)) {
//                    selectAlbums(false, selectPhotoType, isCupture);
//                } else if ("从相册里选择".equals(option)) {
//                    selectAlbums(true, selectPhotoType, isCupture);
//                }
//            }
//        });
//        picker.show();

        Utils.pickPhotos(this, selectPhotoType, isCupture, Constants.PORTRAIT_IMAGE_WIDTH, Constants.PORTRAIT_IMAGE_WIDTH, new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                } else if (mUploadMessage5 != null) {
                    mUploadMessage5.onReceiveValue(null);
                    mUploadMessage5 = null;
                }
            }
        });
    }

//    private void selectAlbums(Boolean isLocal, int requestCode, boolean isCupture) {
//        if (isLocal) {
//            if (isCupture) {
//                PickImageActivity.start(this, requestCode, PickImageActivity.FROM_LOCAL, ImgUtils.imgTempFile(), false, 1, false, true, Constants.PORTRAIT_IMAGE_WIDTH, Constants.PORTRAIT_IMAGE_WIDTH);
//            } else {
//                PickImageActivity.start(this, requestCode, PickImageActivity.FROM_LOCAL, ImgUtils.imgTempFile());
//            }
//        } else {
//            if (isCupture) {
//                PickImageActivity.start(this, requestCode, PickImageActivity.FROM_CAMERA, ImgUtils.imgTempFile(), false, 1, false, true, Constants.PORTRAIT_IMAGE_WIDTH, Constants.PORTRAIT_IMAGE_WIDTH);
//            } else {
//                PickImageActivity.start(this, requestCode, PickImageActivity.FROM_CAMERA, ImgUtils.imgTempFile());
//            }
//        }
//    }


    private final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getSource(String html) {
            Document document = Jsoup.parse(html);
            Elements content = document.select("div#content");
            if (!pasteCheck.isChecked() && StringUtils.isNotEmpty(content.select("div.placeholader").html())) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, R.string.article_hint5, Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            String element = content.html();
            Message message = handler.obtainMessage();
            message.what = 1;
            message.obj = element;
            handler.sendMessage(message);
        }
    }

    @Override
    public void onBackPressed() {
        Utils.showNoTitleDialog(this, getString(R.string.article_hint), getString(R.string.confirm), getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });
    }
}