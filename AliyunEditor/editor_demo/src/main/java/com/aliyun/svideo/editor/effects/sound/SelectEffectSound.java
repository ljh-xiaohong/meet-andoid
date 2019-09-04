package com.aliyun.svideo.editor.effects.sound;

import com.aliyun.svideo.editor.effects.control.EffectInfo;
import com.aliyun.svideo.editor.msg.body.SelectColorFilter;

public class SelectEffectSound {
    private EffectInfo mEffectInfo;
    private EffectInfo mOldInfo;
    private int mIndex;

    private SelectEffectSound(SelectEffectSound.Builder builder) {
        mEffectInfo = builder.mEffectInfo;
        mIndex = builder.mIndex;
        mOldInfo = builder.mOldInfo;
    }


    public static final class Builder {
        private EffectInfo mEffectInfo;
        private int mIndex;
        private EffectInfo mOldInfo;

        public Builder() {
        }

        public SelectEffectSound.Builder effectInfo(EffectInfo val) {
            mEffectInfo = val;
            return this;
        }

        public SelectEffectSound.Builder index(int val) {
            mIndex = val;
            return this;
        }

        public SelectEffectSound.Builder oldInfo(EffectInfo val) {
            mOldInfo = val;
            return this;
        }

        public SelectEffectSound build() {
            return new SelectEffectSound(this);
        }
    }

    public EffectInfo getEffectInfo() {
        return mEffectInfo;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int mIndex) {
        this.mIndex = mIndex;
    }

    public EffectInfo getOldInfo() {
        return mOldInfo;
    }

    public void setOldInfo(EffectInfo mOldInfo) {
        this.mOldInfo = mOldInfo;
    }
}
