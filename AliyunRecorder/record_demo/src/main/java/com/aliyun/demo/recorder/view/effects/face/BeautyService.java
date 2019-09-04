package com.aliyun.demo.recorder.view.effects.face;

import android.content.Context;
import android.support.annotation.IntDef;

import com.aliyun.demo.recorder.faceunity.FaceUnityManager;
import com.aliyun.demo.recorder.util.SharedPreferenceUtils;
import com.aliyun.svideo.base.widget.beauty.BeautyParams;
import com.aliyun.svideo.base.widget.beauty.enums.BeautyLevel;
import com.aliyun.svideo.base.widget.beauty.enums.BeautyMode;

public class BeautyService {

    public static final int BEAUTY_FACE = 0;
    public static final int BEAUTY_SKIN = 1;

    @IntDef({BEAUTY_FACE, BEAUTY_SKIN})
    public @interface BeautyType {}

    private FaceUnityManager faceUnityManager;

    private BeautyParams beautyParams;

    public BeautyParams bindFaceUnity(Context context, FaceUnityManager faceUnityManager) {
        this.faceUnityManager = faceUnityManager;
        return initBeautyParam(context);
    }

    private BeautyParams initBeautyParam(Context context) {
        int beautyFaceLevel = SharedPreferenceUtils.getBeautyFaceLevel(context);

        int beautySkinLevel = SharedPreferenceUtils.getBeautySkinLevel(context);


        float  beautyfaceValue = checkBeautyParam(beautyFaceLevel) / 100;
        float  beautySkinValue = checkBeautyParam(beautySkinLevel) / 100;

        beautyParams = new BeautyParams();
        beautyParams.beautyBuffing = (int)(beautyfaceValue * 10);
        if (faceUnityManager != null) {
            faceUnityManager
                .setFaceBeautyWhite(beautyfaceValue)
                .setFaceBeautyRuddy(beautyfaceValue)
                .setFaceBeautyBuffing(beautyfaceValue * 10 * 0.6f)
                .setFaceBeautyBigEye(beautySkinValue * 1.5f)
                .setFaceBeautySlimFace(beautySkinValue);
        }

        return beautyParams;
    }

    private float checkBeautyParam(int level) {
        BeautyLevel beautyLevel;
        switch (level) {
            case 0:
                beautyLevel = BeautyLevel.BEAUTY_LEVEL_ZERO;
                break;
            case 1:
                beautyLevel = BeautyLevel.BEAUTY_LEVEL_ONE;
                break;
            case 2:
                beautyLevel = BeautyLevel.BEAUTY_LEVEL_TWO;
                break;
            case 3:
                beautyLevel = BeautyLevel.BEAUTY_LEVEL_THREE;
                break;
            case 4:
                beautyLevel = BeautyLevel.BEAUTY_LEVEL_FOUR;
                break;
            case 5:
                beautyLevel = BeautyLevel.BEAUTY_LEVEL_FIVE;
                break;
            default:
                beautyLevel = BeautyLevel.BEAUTY_LEVEL_THREE;
                break;
        }
        return beautyLevel.getValue();
    }

    public void updateAll(BeautyParams beautyParams) {
        if (beautyParams == null) {
            throw new IllegalArgumentException("beautyParams is null");
        }

        faceUnityManager
            .setFaceBeautyWhite(beautyParams.beautyWhite / 100)
            .setFaceBeautyRuddy(beautyParams.beautyRuddy / 100)
            .setFaceBeautyBuffing(beautyParams.beautyBuffing / 10 * 0.6f)
            .setFaceBeautySlimFace(beautyParams.beautySlimFace / 100)
            .setFaceBeautyBigEye(beautyParams.beautyBigEye / 100 * 1.5f);
    }

    public void setBeautyParam(BeautyParams beautyParams, @BeautyType int beautyType) {
        if (beautyParams == null) {
            throw new IllegalArgumentException("beautyParams is null");
        }
        this.beautyParams = beautyParams;
        if (beautyType == BEAUTY_FACE) {
            faceUnityManager
                .setFaceBeautyWhite(beautyParams.beautyWhite / 100)
                .setFaceBeautyRuddy(beautyParams.beautyRuddy / 100)
                .setFaceBeautyBuffing(beautyParams.beautyBuffing / 10 * 0.6f);
        } else {
            faceUnityManager
                .setFaceBeautySlimFace(beautyParams.beautySlimFace / 100 * 1.5f)
                .setFaceBeautyBigEye(beautyParams.beautyBigEye / 100 * 1.5f);
        }

    }

    public BeautyParams getBeautyParam() {
        return beautyParams;
    }

    public void changeBeautyMode(BeautyMode beautyMode) {

        if (beautyMode == BeautyMode.Normal) {

        } else {

        }
    }

    public void unbindFaceUnity() {
        faceUnityManager = null;
    }

    public void saveBeautyMode(Context context, BeautyMode beautyMode) {
        SharedPreferenceUtils.setBeautyMode(context, beautyMode);
    }


    public void saveSelectParam(Context context, int beautyNormalFacePosition, int beautyFacePosition,
                                int beautySkinPosition) {
        SharedPreferenceUtils.setBeautyNormalFaceLevel(context, beautyNormalFacePosition);
        SharedPreferenceUtils.setBeautyFaceLevel(context, beautyFacePosition);
        SharedPreferenceUtils.setBeautySkinLevel(context, beautySkinPosition);
    }
}
