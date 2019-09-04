package com.yuejian.meet.framents.creation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.netease.nim.uikit.api.DataCallback;
import com.netease.nim.uikit.app.AppConfig;
import com.yuejian.meet.MainActivity;
import com.yuejian.meet.api.DataIdCallback;
import com.yuejian.meet.api.http.ApiImp;
import com.yuejian.meet.api.http.Impl.FeedsApiImpl;
import com.yuejian.meet.app.MyApplication;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.utils.OssUtils;
import com.yuejian.meet.utils.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : g000gle
 * @time : 2019/6/25 17:53
 * @desc : 接受com.aliyun.svideo.editor.publish.UploadActivity.java发送的广播
 */
public class UploadVideoBroadReceiver extends BroadcastReceiver {

    private static final String ACTION = "com.yuejian.meet.uploadvideo";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            String videoUrl = intent.getStringExtra("video_url");
            String imgUrl = intent.getStringExtra("img_url");
            String videoDesc = intent.getStringExtra("title");
            imgUrl.replace(".jpeg", ".jpg");

            String ossVideoUrl = OssUtils.getTimeNmaeVideo();

            new FeedsApiImpl().upLoadImageFileToOSS(videoUrl, ossVideoUrl, MyApplication.context, new DataCallback<FeedsResourceBean>() {
                    @Override
                public void onSuccess(FeedsResourceBean data) {
                    String ossImageUrl = OssUtils.getTimeNmaeJpg();
                    new FeedsApiImpl().upLoadImageFileToOSS(imgUrl, ossImageUrl, MyApplication.context, new DataCallback<FeedsResourceBean>() {
                        @Override
                        public void onSuccess(FeedsResourceBean data) {
                            Map<String, Object> params = new HashMap<>();
                            params.put("customer_id", AppConfig.userEntity.customer_id);
                            params.put("title", videoDesc);
                            params.put("content", OssUtils.getOssUploadingUrl(ossVideoUrl));
                            params.put("photo_and_video_url", OssUtils.getOssUploadingUrl(ossImageUrl));
                            params.put("label_id", String.valueOf(0)); //标签ID
                            params.put("is_reprint", String.valueOf(0)); //0为默认，1为允许转载
                            params.put("is_comment", String.valueOf(0)); //0不允许，1为允许评论(默认)
                            params.put("music_url", "");
                            params.put("template_id", "");
                            params.put("vip_type", "");
                            params.put("vip_deploy_id", "");

                            new ApiImp().publishedVideo(params, this, new DataIdCallback<String>() {
                                @Override
                                public void onSuccess(String data, int id) {
                                    ViewInject.shortToast(context, "发布成功");
                                    Intent intent1 = new Intent(context, MainActivity.class);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent1.putExtra("tag", "publishedvideo");
                                    context.startActivity(intent1);
                                }

                                @Override
                                public void onFailed(String errCode, String errMsg, int id) {
                                }
                            });
                        }

                        @Override
                        public void onFailed(String errCode, String errMsg) {

                        }
                    });
                }

                @Override
                public void onFailed(String errCode, String errMsg) {
                }
            });
        }
    }
}
