package com.aliyun.svideo.editor.effects.sound;

import com.aliyun.editor.AudioEffectType;
import com.aliyun.svideo.editor.R;
import com.aliyun.svideo.editor.effects.control.EffectInfo;
import com.aliyun.svideo.editor.effects.control.SoundEffectInfo;

import java.util.ArrayList;
import java.util.List;

public class MockEffectSoundData {

    private static List<SoundEffectInfo> mSoundData = new ArrayList<>();
    private static int[] mIconResource = {
        R.drawable.alivc_svideo_effect_sound_default,
        R.drawable.alivc_svideo_effect_sound_loli,
        R.drawable.alivc_svideo_effect_sound_uncle,
        R.drawable.alivc_svideo_effect_sound_echo,
        R.drawable.alivc_svideo_effect_sound_reverb
    };
    static {
        // 无
        SoundEffectInfo soundEffect1 = new SoundEffectInfo();
        soundEffect1.id = -1;
        soundEffect1.soundName ="原音";
        soundEffect1.audioEffectType = AudioEffectType.EFFECT_TYPE_DEFAULT;
        soundEffect1.imgIcon = mIconResource[0];
        mSoundData.add(soundEffect1);

        // 萝莉
        SoundEffectInfo soundEffect2 = new SoundEffectInfo();
        soundEffect2.audioEffectType = AudioEffectType.EFFECT_TYPE_LOLITA;
        soundEffect2.soundWeight = 50;
        soundEffect2.soundName = "萝莉";
        soundEffect2.imgIcon = mIconResource[1];
        mSoundData.add(soundEffect2);

        // 大叔
        SoundEffectInfo soundEffect3 = new SoundEffectInfo();
        soundEffect3.audioEffectType = AudioEffectType.EFFECT_TYPE_UNCLE;
        soundEffect3.soundWeight = 50;
        soundEffect3.soundName = "大叔";
        soundEffect3.imgIcon = mIconResource[2];
        mSoundData.add(soundEffect3);

        // 回音
        SoundEffectInfo soundEffect4 = new SoundEffectInfo();
        soundEffect4.audioEffectType = AudioEffectType.EFFECT_TYPE_ECHO;
        soundEffect4.soundWeight = 20;
        soundEffect4.soundName = "回音";
        soundEffect4.imgIcon = mIconResource[3];
        mSoundData.add(soundEffect4);

        // ktv
        SoundEffectInfo soundEffect5 = new SoundEffectInfo();
        soundEffect5.audioEffectType = AudioEffectType.EFFECT_TYPE_REVERB;
        soundEffect5.soundWeight = 50;
        soundEffect5.soundName = "KTV";
        soundEffect5.imgIcon = mIconResource[4];
        mSoundData.add(soundEffect5);
    }

    public static List<SoundEffectInfo> getEffectSound() {
        return mSoundData;
    }

    public static SoundEffectInfo getEffectSound(int index) {
        return mSoundData.get(index);
    }
}
