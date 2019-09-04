package com.yuejian.meet.activities.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.UserPreferences;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nim.uikit.common.media.picker.model.PhotoInfo;
import com.netease.nim.uikit.common.media.picker.model.PickerContract;
import com.yuejian.meet.R;
import com.yuejian.meet.activities.base.BaseActivity;
import com.yuejian.meet.common.Constants;
import com.yuejian.meet.utils.PreferencesUtil;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 聊天
 * Created by zh02 on 2017/8/7.
 */

public class ChatSettingActivity extends BaseActivity {
    private final static int REQUEST_SELECT_PHOTO = 10;

    @Bind(R.id.chat_bg_img)
    ImageView chatBgImg;
    @Bind(R.id.voice_use_ear_cup)
    CheckBox voiceUseEarCup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_setting);
        setTitleText(getString(R.string.chat));

        String chatBgImagePath = PreferencesUtil.get(getApplicationContext(), Constants.KEY_CHAT_BACKGROUND_IMAGE, "");
        Glide.with(getBaseContext()).load(new File(chatBgImagePath)).into(chatBgImg);

        boolean isOpenNewMessageNotify = PreferencesUtil.readBoolean(getApplicationContext(), Constants.KEY_VOICE_USE_EAR_CUP,true);
        voiceUseEarCup.setChecked(isOpenNewMessageNotify);
        voiceUseEarCup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferencesUtil.write(getApplicationContext(), Constants.KEY_VOICE_USE_EAR_CUP, isChecked);
                UserPreferences.setEarPhoneModeEnable(isChecked);
            }
        });
    }

    @OnClick({R.id.select_photo_from_albums})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_photo_from_albums:
                selectPhoto();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == REQUEST_SELECT_PHOTO) {
                List<PhotoInfo> photosInfo = PickerContract.getPhotos(data);
                if (photosInfo.size() > 0) {
                    String bgImagePath = photosInfo.get(0).getAbsolutePath();
                    PreferencesUtil.put(getApplicationContext(), Constants.KEY_CHAT_BACKGROUND_IMAGE, bgImagePath);
                    Glide.with(getBaseContext()).load(new File(bgImagePath)).into(chatBgImg);
                }
            }
        }
    }

    private void selectPhoto() {
        PickImageActivity.start(this, REQUEST_SELECT_PHOTO, PickImageActivity.FROM_LOCAL, "", false, 1, true, false, 0, 0);
    }
}
