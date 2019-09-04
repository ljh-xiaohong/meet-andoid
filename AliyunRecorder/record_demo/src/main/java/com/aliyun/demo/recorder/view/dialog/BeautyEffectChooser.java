package com.aliyun.demo.recorder.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.RadioGroup;

import com.aliyun.demo.R;
import com.aliyun.demo.recorder.util.SharedPreferenceUtils;
import com.aliyun.demo.recorder.view.effects.face.AlivcBeautyFaceFragment;
import com.aliyun.demo.recorder.view.effects.filter.AlivcFilterChooseFragment;
import com.aliyun.demo.recorder.view.effects.filter.EffectInfo;
import com.aliyun.demo.recorder.view.effects.filter.interfaces.OnFilterItemClickListener;
import com.aliyun.demo.recorder.view.effects.skin.AlivcBeautySkinFragment;
import com.aliyun.svideo.base.widget.beauty.BeautyParams;
import com.aliyun.svideo.base.widget.beauty.enums.BeautyLevel;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyDetailClickListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyFaceItemSeletedListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautyModeChangeListener;
import com.aliyun.svideo.base.widget.beauty.listener.OnBeautySkinItemSeletedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 美颜模块整体dialog, 包含滤镜, 美颜, 美肌
 * @author xlx
 */
public class BeautyEffectChooser extends BasePageChooser {

    /**
     * 美颜参数
     */
    private BeautyParams beautyParams;
    /**
     * 滤镜fragment
     */
    private AlivcFilterChooseFragment filterChooseFragment;
    /**
     * 美颜fragment
     */
    private AlivcBeautyFaceFragment beautyFaceFragment;
    /**
     * 美肌fragment
     */
    private AlivcBeautySkinFragment beautySkinFragment;

    /**
     * 美颜微调点击listener
     */
    private OnBeautyDetailClickListener onBeautyFaceDetailClickListener;
    /**
     * 美肌微调点击listener
     */
    private OnBeautyDetailClickListener onBeautySkinDetailClickListener;
    /**
     * 滤镜item点击listener
     */
    private OnFilterItemClickListener onFilterItemClickListener;
    /**
     * 美颜item选中listener
     */
    private OnBeautyFaceItemSeletedListener onItemSeletedListener;
    /**
     * 美肌item选中listener
     */
    private OnBeautySkinItemSeletedListener onBeautySkinItemSeletedListener;
    /**
     * 美颜模式切换listener (普通or高级)
     */
    private OnBeautyModeChangeListener onBeautyModeChangeListener;
    private int filterPosition;
    /**
     * 当前viewpager的选中下标
     */
    public int currentTabPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //适配有底部导航栏的手机，在full的style下会盖住部分视图的bug
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.QUDemoFullFitStyle);
    }

    @Override
    public List<Fragment> createPagerFragmentList() {
        List<Fragment> fragments = new ArrayList<>();
        filterChooseFragment = new AlivcFilterChooseFragment();
        beautyFaceFragment = new AlivcBeautyFaceFragment();
        beautySkinFragment = new AlivcBeautySkinFragment();


        fragments.add(filterChooseFragment);
        fragments.add(beautyFaceFragment);
        fragments.add(beautySkinFragment);

        initFilter();
        initBeautyFace();
        initBeautySkin();
        // dialog的tab切换监听
        setOnUpdatePageSelectedListener(new OnUpdatePageSelectedListener() {
            @Override
            public void onPageSelected(int position) {
                currentTabPosition = position;
                beautyFaceFragment.updatePageIndex(position);
                beautySkinFragment.updatePageIndex(position);
            }
        });
        return fragments;
    }

    private void initFilter() {
        filterChooseFragment.setOnFilterItemClickListener(new OnFilterItemClickListener() {
            @Override
            public void onItemClick(EffectInfo effectInfo, int index) {
                if (onFilterItemClickListener != null) {
                    onFilterItemClickListener.onItemClick(effectInfo, index);
                }
            }
        });
    }

    private void initBeautySkin() {
        beautySkinFragment.setBeautyParams(beautyParams);

        beautySkinFragment.setOnBeautySkinItemSelectedlistener(new OnBeautySkinItemSeletedListener() {
            @Override
            public void onItemSelected(int postion) {
                if (onBeautySkinItemSeletedListener != null) {
                    onBeautySkinItemSeletedListener.onItemSelected(postion);
                }
            }
        });

        // 详情
        beautySkinFragment.setOnBeautyDetailClickListener(new OnBeautyDetailClickListener() {
            @Override
            public void onDetailClick() {
                if (onBeautySkinDetailClickListener != null) {
                    onBeautySkinDetailClickListener.onDetailClick();
                }
            }
        });
    }

    private void initBeautyFace() {
        beautyFaceFragment.setBeautyParams(beautyParams);

        // 档位选择
        beautyFaceFragment.setOnBeautyFaceItemSeletedListener(new OnBeautyFaceItemSeletedListener() {
            @Override
            public void onNormalSelected(int postion, BeautyLevel beautyLevel) {
                if (onItemSeletedListener != null) {
                    onItemSeletedListener.onNormalSelected(postion, beautyLevel);
                }
            }

            @Override
            public void onAdvancedSelected(int postion, BeautyLevel beautyLevel) {
                if (onItemSeletedListener != null) {
                    onItemSeletedListener.onAdvancedSelected(postion, beautyLevel);
                }
            }
        });

        // 高级or普通模式切换
        beautyFaceFragment.setOnBeautyModeChangeListener(new OnBeautyModeChangeListener() {
            @Override
            public void onModeChange(RadioGroup group, int checkedId) {
                if (onBeautyModeChangeListener != null) {
                    onBeautyModeChangeListener.onModeChange(group, checkedId);
                }
            }
        });

        // 高级详情
        beautyFaceFragment.setOnBeautyDetailClickListener(new OnBeautyDetailClickListener() {
            @Override
            public void onDetailClick() {
                if (onBeautyFaceDetailClickListener != null) {
                    onBeautyFaceDetailClickListener.onDetailClick();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        filterChooseFragment.setFilterPosition(filterPosition);
    }

    /**
     * 设置美颜美肌参数
     * @param beautyParams 美颜美肌参数
     */
    public void setBeautyParams(BeautyParams beautyParams) {
        this.beautyParams = beautyParams;
    }

    /**
     * 美颜微调按钮点击listener
     * @param listener OnBeautyDetailClickListener
     */
    public void setOnBeautyFaceDetailClickListener(
        OnBeautyDetailClickListener listener) {
        this.onBeautyFaceDetailClickListener = listener;
    }

    /**
     * 美肌微调按钮点击listener
     * @param listener OnBeautyDetailClickListener
     */
    public void setOnBeautySkinDetailClickListener(
        OnBeautyDetailClickListener listener) {
        this.onBeautySkinDetailClickListener = listener;
    }

    /**
     * 设置滤镜item点击listener
     * @param listener OnFilterItemClickListener
     */
    public void setOnFilterItemClickListener(OnFilterItemClickListener listener) {
        this.onFilterItemClickListener = listener;
    }

    /**
     * 设置美颜item点击listener
     * @param listener OnBeautyFaceItemSeletedListener
     */
    public void setOnBeautyFaceItemSeletedListener(OnBeautyFaceItemSeletedListener listener) {
        this.onItemSeletedListener = listener;
    }


    /**
     * 设置美肌item点击listener
     * @param listener OnBeautySkinItemSeletedListener
     */
    public void setOnBeautySkinSelectedListener(OnBeautySkinItemSeletedListener listener) {
        this.onBeautySkinItemSeletedListener = listener;
    }

    /**
     * 美颜模式切换listener
     * @param listener OnBeautyModeChangeListener
     */
    public void setOnBeautyModeChangeListener(
        OnBeautyModeChangeListener listener) {
        this.onBeautyModeChangeListener = listener;
    }

    public void setFilterPosition(int filterPosition) {
        this.filterPosition = filterPosition;
    }

    public int getCurrentTabIndex() {
        return currentTabPosition;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

}
