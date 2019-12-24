package com.yuejian.meet.api.http.Impl;

import android.content.Context;
import android.util.Log;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.netease.nim.uikit.api.DataCallback;
import com.yuejian.meet.api.http.PutObjectSamples;
import com.yuejian.meet.api.http.UrlConstant;
import com.yuejian.meet.bean.FeedsResourceBean;
import com.yuejian.meet.utils.AppinitUtil;
import com.yuejian.meet.utils.FCLoger;

public class FeedsApiImpl{

    private OSS oss;
    /**
     * 上传图片文件
     *
     * @param imagePath
     * @param imageName
     */
    private void upLoadImageFile(String imagePath, String imageName) {
    }

    private void initOssConfig(Context context) {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(UrlConstant.accessKeyId, UrlConstant.accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(context, UrlConstant.endpoint, credentialProvider, conf);
    }

    /**
     * @param imagePath   本地图片要上传的图片
     * @param imageName   服务oss上的路径名加图片名字
     * @param context       上下文
     * @param callback      回调
     */
    public void upLoadImageFileToOSS(String imagePath, final String imageName, Context context, final DataCallback<FeedsResourceBean> callback) {
        initOssConfig(context);
        FCLoger.debug("上传的图片文件路径为：" + imagePath);
        new PutObjectSamples(oss, UrlConstant.bucket, imageName, imagePath).asyncPutObjectFromLocalFile(new DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                String mImageUrl = UrlConstant.downloadObject + imageName;
                Log.e("tag", "图片上传成功：" + mImageUrl);
                FeedsResourceBean feedsResourceBean = new FeedsResourceBean(mImageUrl, "");
                feedsResourceBean.setImageUrl(mImageUrl);
                callback.onSuccess(feedsResourceBean);
            }

            @Override
            public void onSuccess(String data, int id) {

            }

            @Override
            public void onFailed(String errCode, String errMsg) {
                callback.onFailed("1001", "上传失败!");
            }

            @Override
            public void onFailed(String errCode, String errMsg, int id) {

            }
        });
    }


}
